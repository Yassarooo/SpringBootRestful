package Project.service;

import Project.domain.facebook.FacebookUser;
import Project.domain.google.GoogleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleClient {

    @Autowired
    private RestTemplate restTemplate;

    private final String GOOGLE_API_BASE = "https://accounts.google.com";

    public GoogleUser getUser(String idToken) {
        String path = "oauth2/v3/tokeninfo?id_token={id_token}";
        final Map<String, String> variables = new HashMap<>();
        variables.put("id_token", idToken);
        return restTemplate
                .getForObject(GOOGLE_API_BASE + path, GoogleUser.class, variables);
    }
}