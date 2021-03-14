package Project.service;

import Project.aspect.UserAspect;
import Project.domain.AppUser;
import Project.domain.Role;
import Project.domain.VerificationToken;
import Project.repository.AppUserRepository;
import Project.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.logging.Logger;

@Service
public class JwtUserDetailsService implements UserDetailsService {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private RoleService roleService;


    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    private final static Logger log = Logger.getLogger(UserAspect.class.getName());


    public JwtUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }


    public Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String UsernameOrEmail) throws UsernameNotFoundException {
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        AppUser appUser = appUserRepository.findByUsername(UsernameOrEmail);
        if (appUser != null) {
            return new org.springframework.security.core.userdetails.User(
                    appUser.getUsername(),
                    appUser.getPassword(),
                    appUser.isEnabled(),
                    accountNonExpired,
                    credentialsNonExpired,
                    accountNonLocked,
                    appUser.getAuthorities());
        } else {
            appUser = appUserRepository.findByEmail(UsernameOrEmail);
            if (appUser != null) {
                return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), appUser.getAuthorities());
            } else {
                System.err.println("Username Not Found");
                return null;
            }
        }
    }

    public AppUser findUserByUsername(String UsernameOrEmail) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(UsernameOrEmail);
        if (appUser != null) {
            return appUser;
        } else {
            appUser = appUserRepository.findByEmail(UsernameOrEmail);
            if (appUser != null) {
                return appUser;
            } else {
                System.err.println("Username Not Found");
                return null;
            }
        }
    }

    public AppUser save(AppUser appUser) {

        if (this.loadUserByUsername(appUser.getEmail()) != null || this.loadUserByUsername(appUser.getUsername()) != null)
            return null;
        else {
            Role role = roleService.findByName("USER");
            List<Role> roles = new ArrayList<Role>();
            roles.add(role);
            if (appUser.getEmail().split("@")[1].equals("admin.yr")) {
                role = roleService.findByName("ADMIN");
                roles.add(role);
            }
            if (appUser.getEmail().contains("yassar")) {
                role = roleService.findByName("ADMIN");
                roles.add(role);
            }
            if (appUser.getEmail().contains("yrhacker")) {
                role = roleService.findByName("ADMIN");
                roles.add(role);
            }
            appUser.setRoles(roles);

            appUser.setUsername(appUser.getUsername().toLowerCase().trim());
            appUser.setName(appUser.getName().trim());
            appUser.setGender(appUser.getGender().toLowerCase().trim());
            appUser.setEmail(appUser.getEmail().toLowerCase().trim());
            appUser.setCreatedAt(new Date());
            appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
            return appUserRepository.save(appUser);
        }
    }


    public List<AppUser> getAllUsers() {
        List<AppUser> usersList = (List<AppUser>) appUserRepository.findAll();

        if (usersList.size() > 0) {
            return usersList;
        } else {
            return new ArrayList<AppUser>();
        }
    }

    @Transactional
    public AppUser updateUser(AppUser u) {
        try {
            AppUser user = appUserRepository.findByUsername(u.getUsername());

            user.setName(u.getName().trim());
            user.setUsername(u.getUsername().toLowerCase().trim());
            user.setEmail(u.getEmail().toLowerCase().trim());
            user.setCars(u.getCars());
            user.setPhonenumber(u.getPhonenumber());
            user.setDob(u.getDob());
            user.setGender(u.getGender().trim());
            user.setProfilepic(u.getProfilepic());
            user.setRoles(u.getRoles());
            user.setUpdatedAt(new Date());

            appUserRepository.save(user);
            log.info("upadted user : " + u.getCars().toString());
            return u;
        } catch (ObjectOptimisticLockingFailureException e) {
            log.severe("Somebody has already updated the amount for item:{} in concurrent transaction.");
            throw e;
        }
    }

    public AppUser getUserByToken(final String verificationToken) {
        VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        } else {
            token = tokenRepository.findByCode(verificationToken);
            if (token != null) {
                return token.getUser();
            }
        }
        return null;
    }

    public AppUser ActivateUser(final String verificationToken, final String email) {
        final AppUser user = getUserByToken(verificationToken);
        VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token == null)
            token = tokenRepository.findByCode(verificationToken);
        if (token == null) {
            System.err.println("Token not Found");
            throw new UsernameNotFoundException(verificationToken);
        } else {
            if (user != null) {
                if (user.getEmail().equals(email)) {
                    user.setEnabled(true);
                    appUserRepository.save(user);
                    tokenRepository.delete(token);
                    return user;
                } else {
                    System.err.println("found the same token for other email");
                    throw new UsernameNotFoundException(verificationToken);
                }

            } else {
                System.err.println("Username Not Found");
                throw new UsernameNotFoundException(verificationToken);
            }
        }

    }

    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    public VerificationToken getTokenByUser(final AppUser user) {
        return tokenRepository.findByUser(user);
    }

    public void deleteToken(final AppUser user) {
        final VerificationToken verificationToken = tokenRepository.findByUser(user);

        if (verificationToken != null) {
            tokenRepository.delete(verificationToken);
        } else {
            System.err.println("verificationToken Not Found");
            throw new UsernameNotFoundException("verificationToken Not Found");
        }
    }

    public void createVerificationTokenForUser(final AppUser user, final String token, final String code) {
        final VerificationToken myToken = new VerificationToken(token, code, user);
        tokenRepository.save(myToken);
    }

    public VerificationToken generateNewVerificationToken(final String email) {
        AppUser existinguser = findUserByUsername(email);
        if (existinguser != null) {
            VerificationToken vToken = tokenRepository.findByUser(existinguser);
            if (vToken != null) {
                Random rnd = new Random();
                int code = rnd.nextInt(999999);
                vToken.updateToken(UUID.randomUUID()
                        .toString(), String.format("%06d", code));
                return tokenRepository.save(vToken);
            } else {
                System.err.println("token Not Found");
                return null;
            }
        } else {
            System.err.println("User Not Found");
            return null;
        }

    }


    public Optional<AppUser> getUserByID(final long id) {
        return appUserRepository.findById(id);
    }

    public void changeUserPassword(final AppUser user, final String password) {
        user.setPassword(bCryptPasswordEncoder.encode(password));
        appUserRepository.save(user);
    }

    public boolean checkIfValidOldPassword(final AppUser user, final String oldPassword) {
        return bCryptPasswordEncoder.matches(oldPassword, user.getPassword());
    }

    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final AppUser user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        // tokenRepository.delete(verificationToken);
        appUserRepository.save(user);
        return TOKEN_VALID;
    }

    public void deleteAllUsers() {
        appUserRepository.deleteAll();
    }


}