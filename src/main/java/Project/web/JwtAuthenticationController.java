package Project.web;


import java.io.IOException;
import java.security.Principal;
import java.util.*;

import Project.facebook.FacebookService;
import Project.google.GoogleService;
import Project.service.JwtTokenUtil;
import Project.domain.AppUser;
import Project.service.JwtUserDetailsService;
import Project.repository.AppUserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static java.lang.System.currentTimeMillis;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private FacebookService facebookService;
    @Autowired
    private GoogleService googleService;


    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    /**
     * @param username
     * @param password
     * @return JSON contains token and user after success authentication.
     * @throws IOException
     */

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) throws Exception {

        userDetailsService.authenticate(username, password);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            appUser = appUserRepository.findByEmail(username);
        }
        final String token = jwtTokenUtil.generateToken(userDetails);

        Map<String, Object> tokenMap = new HashMap<String, Object>();

        if (token != null) {
            tokenMap.put("token", token);
            tokenMap.put("user", appUser);

            return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.OK);
        } else {
            tokenMap.put("token", null);
            return new ResponseEntity<Map<String, Object>>(tokenMap, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody AppUser appUser) {

        if (appUserRepository.findByUsername(appUser.getUsername()) != null) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        if (appUserRepository.findByEmail(appUser.getEmail()) != null) {
            return new ResponseEntity(HttpStatus.MULTI_STATUS);
        }

        appUser.setAcctype(AppUser.Account_Type.Guest);
        userDetailsService.save(appUser);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/checktoken", method = RequestMethod.POST)
    public ResponseEntity<?> checktoken(@RequestParam String token) {
        try {
            if (jwtTokenUtil.isTokenExpired(token))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/facebooklogin")
    public ResponseEntity<?> facebookAuth(@RequestParam String token) throws Exception {
        return new ResponseEntity<Map<String, Object>>(facebookService.loginUser(token), HttpStatus.OK);
    }

    @PostMapping("/googlelogin")
    public ResponseEntity<?> googleAuth(@RequestParam String token) throws Exception {
        System.out.println(currentTimeMillis());
        return new ResponseEntity<Map<String, Object>>(googleService.loginUser(token), HttpStatus.OK);
    }

    /**
     * This method will return the logged user.
     *
     * @param principal
     * @return Principal java security principal object
     */
    @RequestMapping("/user")
    public AppUser user(Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        return appUserRepository.findByUsername(loggedUsername);
    }
}
