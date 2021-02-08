package Project.web;

import Project.domain.User;
import Project.repository.UserRepository;
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
    private UserRepository userRepository;

    /**
     * Web service for getting all the appUsers in the application.
     *
     * @return list of all User
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> users() {
        return (List<User>) userRepository.findAll();
    }

    /**
     * Web service for getting a user by his username
     *
     * @param username appUser username
     * @return appUser
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public ResponseEntity<User> userByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<User>(user, HttpStatus.OK);
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
    public ResponseEntity<User> deleteUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
        } else if (user.getUsername().equalsIgnoreCase(loggedUsername)) {
            throw new RuntimeException("You cannot delete your account");
        } else {
            userRepository.delete(user);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }

    }

    /**
     * Method for adding a user
     *
     * @param user
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exist");
        }
        return new ResponseEntity<User>(userRepository.save(user), HttpStatus.CREATED);
    }

    /**
     * Method for editing an user details
     *
     * @param user
     * @return modified user
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    public User updateUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null
                && userRepository.findByUsername(user.getUsername()).getId() != user.getId()) {
            throw new RuntimeException("Username already exist");
        }
        return userRepository.save(user);
    }

}
