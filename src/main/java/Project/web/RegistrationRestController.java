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
import javax.validation.Valid;
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
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    // User activation - verification
    @GetMapping("/resendRegistrationToken")
    public ResponseEntity<String> resendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        final AppUser user = userService.getUser(newToken.getToken());
        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));
        return new ResponseEntity<String>("Email sent successfully", HttpStatus.OK);
    }

    // confirm activation
    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public ResponseEntity<AppUser> registrationConfirm(@RequestParam("token") String token) {
        return new ResponseEntity<AppUser>(userService.ActivateUser(token), HttpStatus.OK);
    }

    // ============== NON-API ============

    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final AppUser user) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, AppUser user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
