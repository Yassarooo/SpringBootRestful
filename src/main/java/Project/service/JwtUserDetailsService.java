package Project.service;

import Project.domain.AppUser;
import Project.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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

        return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), emptyList());
    }

    public AppUser save(AppUser appUser) {
        AppUser newAppUser = new AppUser();
        newAppUser.setUsername(appUser.getUsername());
        newAppUser.setName(appUser.getName());
        newAppUser.setRoles(appUser.getRoles());
        newAppUser.setGender(appUser.getGender());
        newAppUser.setEmail(appUser.getEmail());
        newAppUser.setPhonenumber(appUser.getPhonenumber());
        newAppUser.setDob(appUser.getDob());
        newAppUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(newAppUser);
    }



}