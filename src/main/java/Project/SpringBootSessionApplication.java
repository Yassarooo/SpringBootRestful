package Project;

import Project.config.FileStorageProperties;
import Project.domain.Parameters;
import Project.domain.Role;
import Project.service.ParamsService;
import Project.service.RoleService;
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
    ParamsService paramsService;

    public static void main(String[] args) {

        SpringApplication.run(SpringBootSessionApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {
        /*
        Role uRole = new Role();
        uRole.setName("USER");
        uRole.setDescription("USER Role (Only Manage cars without managing users)");
        roleService.CreateRole(uRole);
        Role aRole = new Role();
        aRole.setName("ADMIN");
        aRole.setDescription("ADMIN Role (Manage cars and users)");
        roleService.CreateRole(aRole);

        Parameters sedan = new Parameters("Sedan", 4, 6f);
        paramsService.createOrUpdateParam(sedan, false);
        Parameters sport = new Parameters("Sport", 4, 10f);
        paramsService.createOrUpdateParam(sport, false);
        Parameters compact = new Parameters("Compact", 4, 4f);
        paramsService.createOrUpdateParam(compact, false);
        Parameters convertible = new Parameters("Convertible", 2, 6f);
        paramsService.createOrUpdateParam(convertible, false);
        Parameters Pickup = new Parameters("Pickup", 4, 6f);
        paramsService.createOrUpdateParam(Pickup, false);
        Parameters suv = new Parameters("SUV", 6, 9f);
        paramsService.createOrUpdateParam(suv, false);
        Parameters coupe = new Parameters("Coupe", 4, 9f);
        paramsService.createOrUpdateParam(coupe, false);
        Parameters limousine = new Parameters("Limousine", 8, 10f);
        paramsService.createOrUpdateParam(limousine, false);
*/
    }

}