package Project.web;

import Project.domain.AppUser;
import Project.domain.VerificationToken;
import Project.registration.OnRegistrationCompleteEvent;
import Project.service.JwtUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestController
public class RegistrationRestController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private JwtUserDetailsService userService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private Environment env;

    public RegistrationRestController() {
        super();
    }

    // Registration
    @PostMapping("/registration")
    public ResponseEntity<String> registerUserAccount(@RequestBody AppUser accountDto, final HttpServletRequest request) {
        LOGGER.debug("Registering user account with information: {}", accountDto);

        final AppUser registered = userService.save(accountDto);
        if (registered == null) {
            return new ResponseEntity<String>("failed", HttpStatus.CONFLICT);
        }
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    // User activation - verification
    @GetMapping("/resendEmail")
    public ResponseEntity<String> resendRegistrationToken(final HttpServletRequest request, @RequestParam("email") final String email) {

        final VerificationToken newToken = userService.generateNewVerificationToken(email);
        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, email));
        return new ResponseEntity<String>("Email sent successfully", HttpStatus.OK);

    }

    // confirm activation
    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.POST)
    public ResponseEntity<AppUser> registrationConfirm(@RequestParam("token") String token, @RequestParam("email") String email) {
        return new ResponseEntity<AppUser>(userService.ActivateUser(token, email), HttpStatus.OK);
    }

    //check if username or email is used or no
    @PostMapping("/checkusername")
    public ResponseEntity<String> checkUsernameOrEmail(@RequestParam String username) {
        if (userService.loadUserByUsername(username) == null)
            return new ResponseEntity<String>("success", HttpStatus.OK);
        else if (userService.loadUserByUsername(username) != null) ;
        return new ResponseEntity<String>("failed", HttpStatus.CONFLICT);
    }


    // ============== NON-API ============

    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final String emailaddress) {
        final String confirmationUrl = contextPath + "/#/registrationConfirm.html/" + emailaddress + "/" + newToken.getToken();
        final String message = messages.getMessage("message.regSuccLink", null, "You have registered successfully. To confirm your registration, please enter this code:\n " + newToken.getCode() + "\n or click on the below link.", locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, emailaddress);
    }

    private SimpleMailMessage constructEmail(String subject, String body, String emailaddress) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(emailaddress);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
