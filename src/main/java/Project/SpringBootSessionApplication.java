package Project;
import Project.domain.Parameters;
import Project.service.ParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SpringBootSessionApplication implements CommandLineRunner {

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
        Parameters p = new Parameters();
        p.setPercentage(5f);
        p.setSeats(4);
        p.setId((long)1);
        paramsService.createOrUpdateParams(p);
    }

}