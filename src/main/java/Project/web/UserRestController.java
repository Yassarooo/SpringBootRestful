package Project.web;

import Project.domain.AppUser;
import Project.repository.AppUserRepository;
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

    /**
     * Web service for getting all the appUsers in the application.
     *
     * @return list of all AppUser
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<AppUser> users() {
        return (List<AppUser>) appUserRepository.findAll();
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
     * Method for adding a appUser
     *
     * @param appUser
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser) {
        if (appUserRepository.findByUsername(appUser.getUsername()) != null) {
            throw new RuntimeException("Username already exist");
        }
        return new ResponseEntity<AppUser>(appUserRepository.save(appUser), HttpStatus.CREATED);
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
        if (appUserRepository.findByUsername(appUser.getUsername()) != null
                && appUserRepository.findByUsername(appUser.getUsername()).getId() != appUser.getId()) {
            throw new RuntimeException("Username already exist");
        }
        return appUserRepository.save(appUser);
    }

}
