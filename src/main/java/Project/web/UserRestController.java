package Project.web;

import Project.domain.AppUser;
import Project.repository.AppUserRepository;
import Project.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller for authentication and user details. All the web services of
 * this rest controller will be only accessible for ADMIN users only
 */
@RestController
@RequestMapping(value = "/api")
public class UserRestController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    /**
     * Web service for getting all the appUsers in the application.
     *
     * @return lit of all AppUser
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = userDetailsService.getAllUsers();
        return new ResponseEntity<List<AppUser>> (users,HttpStatus.OK);
    }

    /**
     * Web service for getting a user by his username
     *
     * @param username appUser username
     * @return appUser
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public ResponseEntity<AppUser> userByUserName(@PathVariable String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            return new ResponseEntity<AppUser>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<AppUser>(appUser, HttpStatus.OK);
        }
    }

    /**
     * Method for deleting a user by his UserName
     *
     * @param username
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<AppUser> deleteUser(@PathVariable String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        if (appUser == null) {
            return new ResponseEntity<AppUser>(HttpStatus.NO_CONTENT);
        } else if (appUser.getUsername().equalsIgnoreCase(loggedUsername)) {
            throw new RuntimeException("You cannot delete your account");
        } else {
            appUserRepository.delete(appUser);
            return new ResponseEntity<AppUser>(appUser, HttpStatus.OK);
        }
    }

    /**
     * Method for editing an appUser details
     *
     * @param appUser
     * @return modified appUser
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    public AppUser updateUser(@RequestBody AppUser appUser) {
        return userDetailsService.updateUser(appUser);
    }

}
