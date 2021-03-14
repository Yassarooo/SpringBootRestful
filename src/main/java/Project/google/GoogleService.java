package Project.google;

import Project.domain.AppUser;
import Project.domain.Role;
import Project.google.GoogleTokenVerifier;
import Project.service.JwtTokenUtil;
import Project.service.JwtUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

@Service
public class GoogleService {

    private final RestTemplate restTemplate;

    @Autowired
    JwtUserDetailsService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private GoogleTokenVerifier googleTokenVerifier;

    @Autowired
    public GoogleService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, String> getMapFromGoogleTokenString(final String idTokenString) {
        BufferedReader in = null;
        try {
            // get information from token by contacting the google_token_verify_tool url :
            in = new BufferedReader(new InputStreamReader(
                    ((HttpURLConnection) (new URL("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + idTokenString.trim()))
                            .openConnection()).getInputStream(), Charset.forName("UTF-8")));

            // read information into a string buffer :
            StringBuffer b = new StringBuffer();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                b.append(inputLine + "\n");
            }

            // transforming json string into Map<String,String> :
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(b.toString(), objectMapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));

            // exception handling :
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("\n\n\tFailed to transform json to string\n");
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    // chack the "email_verified" and "email" values in token payload
    private boolean verifyEmail(final Map<String, String> tokenPayload) {
        if (tokenPayload.get("email_verified") != null && tokenPayload.get("email") != null) {
            try {
                return Boolean.valueOf(tokenPayload.get("email_verified")) && tokenPayload.get("email").contains("@gmail.");
            } catch (Exception e) {
                System.out.println("\n\n\tCheck emailVerified failed - cannot parse " + tokenPayload.get("email_verified") + " to boolean\n");
            }
        } else {
            System.out.println("\n\n\tCheck emailVerified failed - required information missing in the token");
        }
        return false;
    }

    // check token expiration is after now :
    private boolean checkExpirationTime(final Map<String, String> tokenPayload) {
        try {
            if (tokenPayload.get("exp") != null) {
                // the "exp" value is in seconds and Date().getTime is in mili seconds
                return Long.parseLong(tokenPayload.get("exp") + "000") > new java.util.Date().getTime();
            } else {
                System.out.println("\n\n\tCheck expiration failed - required information missing in the token\n");
            }
        } catch (Exception e) {
            System.out.println("\n\n\tCheck expiration failed - cannot parse " + tokenPayload.get("exp") + " into long\n");
        }
        return false;
    }

    // check that at least one CLIENT_ID matches with token values
    private boolean checkAudience(final Map<String, String> tokenPayload) {
        if (tokenPayload.get("aud") != null && tokenPayload.get("azp") != null) {
            List<String> pom = Arrays.asList("MY_CLIENT_ID_1",
                    "MY_CLIENT_ID_2",
                    "MY_CLIENT_ID_3");

            if (pom.contains(tokenPayload.get("aud")) || pom.contains(tokenPayload.get("azp"))) {
                return true;
            } else {
                System.out.println("\n\n\tCheck audience failed - audiences differ\n");
                return false;
            }
        }
        System.out.println("\n\n\tCheck audience failed - required information missing in the token\n");
        return false;
    }

    // verify google token payload :
    public boolean doTokenVerification(final Map<String, String> tokenPayload) {
        if (tokenPayload != null) {
            return verifyEmail(tokenPayload) // check that email address is verifies
                    && checkExpirationTime(tokenPayload) // check that token is not expired
                    && checkAudience(tokenPayload) // check audience
                    ;
        }
        return false;
    }


    public Map<String, Object> loginUser(String idToken) throws Exception {

        if (idToken.isEmpty()) {
            throw new Exception("Empty token");
        }

        Optional<GoogleIdToken.Payload> payload = Optional.empty();

        try {
            GoogleIdToken.Payload payload1 = googleTokenVerifier.verify(idToken).getPayload();

            payload = Optional.of(payload1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assert payload.isPresent();

        if (!payload.get().getEmailVerified()) {

            throw new Exception("Email Must Be Verified");
        }

        AppUser user = userService.findUserByUsername(payload.get().getEmail());

        if (user == null) {
            user = userService.save(convertTo(payload.get()));
        }

        Authentication auth = userService.authenticate(user.getEmail(), "googleuserpassword");

        SecurityContextHolder.getContext().setAuthentication(auth);
        final UserDetails userDetails = userService.loadUserByUsername(user.getEmail());

        String token = jwtTokenUtil.generateToken(userDetails);

        Map<String, Object> tokenMap = new HashMap<String, Object>();


        if (token != null) {
            tokenMap.put("token", token);
            tokenMap.put("user", user);

            return tokenMap;
        } else {
            tokenMap.put("token", null);
            throw new UsernameNotFoundException("token == null , unable to login google user id " + user.getId());
        }
    }


    private AppUser convertTo(GoogleIdToken.Payload payload) {
        return new AppUser((long) 0, String.valueOf(payload.get("name")), payload.getEmail(), payload.getEmail(), "None", "googleuserpassword", String.valueOf(payload.get("picture")), new Date(), new ArrayList<Role>(), true);
    }

    public Map<?, ?> verifyTokenUsingRest(String token) {

        return restTemplate.getForObject(
                "https://oauth2.googleapis.com/tokeninfo?id_token=".concat(token),
                Map.class
        );

    }
}