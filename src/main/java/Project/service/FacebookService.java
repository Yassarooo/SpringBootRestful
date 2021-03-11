package Project.service;

import Project.domain.AppUser;
import Project.domain.Role;
import Project.domain.facebook.FacebookUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class FacebookService {

    @Autowired
    private FacebookClient facebookClient;
    @Autowired
    private JwtUserDetailsService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public Map<String, Object> loginUser(String fbAccessToken) throws Exception {
        FacebookUser facebookUser = facebookClient.getUser(fbAccessToken);
        if (facebookUser == null) {
            throw new UsernameNotFoundException("unable to login facebook user id " + facebookUser.getId());
        } else {
            AppUser user = userService.findById(facebookUser.getId());
            if (user == null) {
                user = userService.save(convertTo(facebookUser));
            }

            userService.authenticate(facebookUser.getEmail(), "facebookuser");
            final UserDetails userDetails = userService.loadUserByUsername(facebookUser.getEmail());
            String token = jwtTokenUtil.generateToken(userDetails);
            Map<String, Object> tokenMap = new HashMap<String, Object>();


            if (token != null) {
                tokenMap.put("token", token);
                tokenMap.put("user", user);

                return tokenMap;
            } else {
                tokenMap.put("token", null);
                throw new UsernameNotFoundException("token == null , unable to login facebook user id " + facebookUser.getId());
            }
        }


    }

    private AppUser convertTo(FacebookUser facebookUser) {
        String name = facebookUser.getFirstName() + " " + facebookUser.getLastName();
        return new AppUser(facebookUser.getId(), name, facebookUser.getEmail(), facebookUser.getEmail(), facebookUser.getGender(), "facebookuserpassword", new Date(), new ArrayList<Role>(), true);
    }

    private String generateUsername(String firstName, String lastName) {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        return String.format("%s.%s.%06d", firstName, lastName, number);
    }

    private String generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return new String(password);
    }
}