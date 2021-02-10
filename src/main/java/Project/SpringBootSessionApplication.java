package Project;

import Project.config.FileStorageProperties;
import Project.domain.AppUser;
import Project.domain.Car;
import Project.domain.Parameters;
import Project.domain.Role;
import Project.service.CarService;
import Project.service.ParamsService;
import Project.service.RoleService;
import Project.web.JwtAuthenticationController;
import Project.web.SpringSessionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties({FileStorageProperties.class})
@ComponentScan
@EntityScan(basePackages = {"Project.domain"})
@EnableJpaRepositories(basePackages = {"Project.repository"})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableTransactionManagement
@EnableCaching
public class SpringBootSessionApplication implements CommandLineRunner {

    @Autowired
    RoleService roleService;

    @Autowired
    JwtAuthenticationController jwtAuthenticationController;

    public static void main(String[] args) {

        SpringApplication.run(SpringBootSessionApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {
        Role uRole = new Role("USER", "USER Role (Only Manage cars without managing users)");
        Role aRole = new Role("ADMIN", "ADMIN Role (Manage cars and users)");
        roleService.CreateRole(uRole);
        roleService.CreateRole(aRole);


        //AppUser admin = new AppUser("Yassar Hammami", "yassar", "yassarhammami@admin.yr", "Male", "0992156565", "yassar", new Date(1999, 7, 9), null);
        //jwtAuthenticationController.createUser(admin);

        //Parameters param = new Parameters("SUV", 6, 8f);
        //Parameters param2 = new Parameters("Full-Size", 4, 5f);
        //paramsService.createOrUpdateParam(param, false);
        //paramsService.createOrUpdateParam(param2, false);

        //Car c = new Car("Kia Rio", 4000f, null, "", null, null, null, param2.getId().intValue() , "Very Good", 4, new Date(2009, 1, 1));
        //carService.createOrUpdateCar(c,false);
    }

}