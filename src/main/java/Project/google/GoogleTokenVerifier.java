package Project.google;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;

public class GoogleTokenVerifier {

    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    @Autowired
    public GoogleTokenVerifier(GoogleIdTokenVerifier googleIdTokenVerifier) {
        this.googleIdTokenVerifier = googleIdTokenVerifier;
    }

    public GoogleIdToken verify(String idToken) throws GeneralSecurityException, IOException {
        GoogleIdToken id_token = googleIdTokenVerifier.verify(idToken);
        if (id_token == null)
            throw new GeneralSecurityException("Unauthenticated User by google");
        else
            return id_token;
    }

    public boolean verify(GoogleIdToken googleIdToken) throws GeneralSecurityException, IOException {
        return googleIdTokenVerifier.verify(googleIdToken);
    }

    public boolean verify(IdToken idToken) {
        return googleIdTokenVerifier.verify(idToken);
    }
}