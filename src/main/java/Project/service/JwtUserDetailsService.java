package Project.service;

import Project.aspect.UserAspect;
import Project.domain.AppUser;
import Project.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.Collections.emptyList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final static Logger log = Logger.getLogger(UserAspect.class.getName());


    public JwtUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            System.err.println("Username Not Found");
            throw new UsernameNotFoundException(username);
        }

        return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), appUser.getAuthorities());
    }

    public AppUser save(AppUser appUser) {
        AppUser newAppUser = new AppUser();
        newAppUser.setUsername(appUser.getUsername().toLowerCase().trim());
        newAppUser.setName(appUser.getName().trim());
        newAppUser.setRoles(appUser.getRoles());
        newAppUser.setGender(appUser.getGender().toLowerCase().trim());
        newAppUser.setEmail(appUser.getEmail().toLowerCase().trim());
        newAppUser.setPhonenumber(appUser.getPhonenumber());
        newAppUser.setDob(appUser.getDob());
        newAppUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(newAppUser);
    }

    public List<AppUser> getAllUsers() {
        List<AppUser> carList = (List<AppUser>) appUserRepository.findAll();

        if (carList.size() > 0) {
            return carList;
        } else {
            return new ArrayList<AppUser>();
        }
    }

    @Transactional
    public AppUser updateUser(AppUser u) {
        try {
            u.setUsername(u.getUsername().toLowerCase().trim());
            u.setEmail(u.getEmail().toLowerCase().trim());
            u.setGender(u.getGender().toLowerCase().trim());
            u = appUserRepository.save(u);
            return u;
        } catch (ObjectOptimisticLockingFailureException e) {
            log.severe("Somebody has already updated the amount for item:{} in concurrent transaction.");
            throw e;
        }
    }


}