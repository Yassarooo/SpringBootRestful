package Project.aspect;

import Project.domain.Car;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CarServiceAspect {
    private Authentication auth;

    @Before("execution(* Project.service.CarService.getAllCars())")
    public void beforeGetCar() {
        auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Get All Cars - by " + auth.getName());
    }

    @Before("execution(* Project.service.CarService.createOrUpdateCar(Project.domain.Car , Boolean )) && args(c,update)")
    public void beforecreateCar(Car c, Boolean update) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        if (update) {
            System.out.println("update Car -" + c.getId() + "-" + c.getName() + " - by " + auth.getName());
        } else {
            System.out.println("Create New Car -" + c.getName() + " - by " + auth.getName());
        }
    }

    @AfterReturning("execution(* Project.service.CarService.createOrUpdateCar(Project.domain.Car , Boolean )) && args(c,update)")
    public void aftercreateCar(Car c, Boolean update) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        if (update) {
            System.out.println("updated Car -" + c.getId() + "-" + c.getName() + " - by " + auth.getName());
        } else {
            System.out.println("Created New Car -" + c.getName() + " - by " + auth.getName());
        }
    }

    @Before("execution(* Project.service.CarService.deleteCarById(Long)) && args(id)")
    public void beforedeleteCar(Long id) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("delete Car - ID" + id + " - by " + auth.getName());
    }

    @AfterReturning("execution(* Project.service.CarService.deleteCarById(Long)) && args(id)")
    public void afterdeleteCar(Long id) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("deleted Car - ID" + id + " - by " + auth.getName());
    }

    @Before("execution(* Project.service.CarService.getCarById(Long)) && args(id)")
    public void beforegetCar(Long id) {
        auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("get Car -ID " + id + " - by " + auth.getName());
    }

}
