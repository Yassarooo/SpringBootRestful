package Project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Configuration
@EnableJpaRepositories(basePackages = {
        "Project.repository"
})
@EnableTransactionManagement
public class H2TestProfileConfig {

        @Bean
        @Profile("test")
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.h2.Driver");
            dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
            dataSource.setUsername("jazara");
            dataSource.setPassword("jazara");

            return dataSource;
        }

}
