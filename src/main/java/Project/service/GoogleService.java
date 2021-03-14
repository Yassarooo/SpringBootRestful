package Project.service;

import Project.domain.AppUser;
import Project.domain.Role;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * @author Austin Oyugi
 * @email austinoyugi@gmail.com
 * @since 3/4/2021
 */

@Service
public class GoogleService {

    private final RestTemplate restTemplate;

    @Value("${app.google.clientID}")
    private String CLIENT_ID;

    @Autowired
    JwtUserDetailsService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private GoogleIdTokenVerifier googleIdTokenVerifier;

    private static final HttpTransport transport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new JacksonFactory();
    private static final String MY_APP_GOOGLE_CLIENT_ID = "965172490679-k1n80f3adjn24jp81jq1bp5pvp2puefc.apps.googleusercontent.com";

    @Autowired
    public GoogleService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public Map<String, Object> loginUser(String googleToken) throws Exception {

        Map<String, Object> response = new LinkedHashMap<>();

        if (googleToken.isEmpty()) {
            throw new Exception("Empty token");
        }

        Optional<GoogleIdToken.Payload> payload = Optional.empty();

        try {
            GoogleIdToken.Payload payload1 = verify2(googleToken);

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
        } else {

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

    public GoogleIdToken.Payload verifyToken(String idTokenString) throws Exception {

        GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenString);

        if (idToken == null)
            throw new Exception();

        return idToken.getPayload();
    }

    public GoogleIdToken.Payload verify2(final String idTokenString) throws GeneralSecurityException, IOException {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(MY_APP_GOOGLE_CLIENT_ID))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);// <-- verifier.verify returns null !!!
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                if (Boolean.valueOf(payload.getEmailVerified())) {
                    return idToken.getPayload();
                }
                else{

                    System.out.println("The Email isnt verified !!!");
                    return null;
                }
            } else {
                System.out.println("The *idToken* object is null !!!");
                return null;
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private AppUser convertTo(GoogleIdToken.Payload payload) {
        return new AppUser((long) 0, String.valueOf(payload.get("name")), payload.getEmail(), payload.getEmail(), String.valueOf(payload.get("gender")), "googleuserpassword", String.valueOf(payload.get("picture")), new Date(), new ArrayList<Role>(), true);
    }

    public Map<?, ?> verifyTokenUsingRest(String token) {

        return restTemplate.getForObject(
                "https://oauth2.googleapis.com/tokeninfo?id_token=".concat(token),
                Map.class
        );

    }
}