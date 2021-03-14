package Project.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({GoogleIdentifierProperties.class})
@EnableConfigurationProperties(GoogleIdentifierProperties.class)
public class GoogleIdentifierConfig {

    private final GoogleIdentifierProperties googleIdentifierProperties;

    @Autowired
    public GoogleIdentifierConfig(GoogleIdentifierProperties googleIdentifierProperties) {
        this.googleIdentifierProperties = googleIdentifierProperties;
    }

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier(JacksonFactory jacksonFactory, HttpTransport httpTransport) {
        return new GoogleIdTokenVerifier.Builder(httpTransport, jacksonFactory)
                .setIssuer(googleIdentifierProperties.getIssuer())
                .setAudience(Arrays.asList(googleIdentifierProperties.getClients()))
                .build();
    }

    @Bean
    public JacksonFactory jacksonFactory() {
        return new JacksonFactory();
    }

    @Bean
    public HttpTransport httpTransport() {
        return new NetHttpTransport();
    }

    @Bean
    public GoogleTokenVerifier googleTokenVerifierTemplate(GoogleIdTokenVerifier googleIdTokenVerifier) {
        return new GoogleTokenVerifier(googleIdTokenVerifier);
    }

}