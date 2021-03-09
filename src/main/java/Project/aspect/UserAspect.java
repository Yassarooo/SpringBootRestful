package Project.aspect;

import java.util.Arrays;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging execution of service and repository Spring components.
 *
 * @author Ramesh Fadatare
 */

@Aspect
@Component
public class UserAspect {

    private final static Logger log = Logger.getLogger(UserAspect.class.getName());
    private Authentication auth;

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(Project..*)" +
            " || within(Project.service..*)" +
            " || within(Project.web..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.severe("Exception in " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "() with cause = ");
        if (e.getCause() != null)
            log.severe("" + e.getCause());
        else
            log.severe("NULL");
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        auth = SecurityContextHolder.getContext().getAuthentication();

        try {
            log.info("Enter: " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "() with argument[s] = " +
                    Arrays.toString(joinPoint.getArgs()) + " By :" + auth.getName());
        }
        catch (Exception e){
            log.severe("Exception in : " + Arrays.toString(joinPoint.getArgs()) + " in " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "()");
            throw e;
        }

        try {
            Object result = joinPoint.proceed();

            log.info("Exit: " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "() with result = " +
                    result + " By :" + auth.getName());

            return result;
        } catch (Exception e) {
            log.severe("Exception in : " + Arrays.toString(joinPoint.getArgs()) + " in " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "()");
            throw e;
        }
    }
}