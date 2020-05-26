package men.doku.donation.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import men.doku.donation.config.ApplicationProperties;

@Service
public class GmailService {

    private final Logger log = LoggerFactory.getLogger(GmailService.class);
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final String USER = "me";

    private Gmail gmail;

    // private static final List<String> SCOPES = Arrays.asList(
    // GmailScopes.MAIL_GOOGLE_COM, GmailScopes.GMAIL_COMPOSE,
    // GmailScopes.GMAIL_SEND, GmailScopes.GMAIL_LABELS,
    // GmailScopes.GMAIL_INSERT, GmailScopes.GMAIL_MODIFY,
    // GmailScopes.GMAIL_READONLY);
    private final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_COMPOSE, GmailScopes.GMAIL_SEND);

    private final ApplicationProperties applicationProperties;
    private final GoogleOauthService googleOauthService;

    public GmailService(ApplicationProperties applicationProperties, GoogleOauthService googleOauthService) {
        this.applicationProperties = applicationProperties;
        this.googleOauthService = googleOauthService;
    }

    @PostConstruct
    public void initiateGmail() throws IOException, GeneralSecurityException {
        log.info("Initiate Gmail Service");
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        if (applicationProperties.getGoogle().getGmail().getActive()) {
            gmail = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, googleOauthService.getCredential(
                    applicationProperties.getGoogle().getGmail().getCredentialFolder(), SCOPES, HTTP_TRANSPORT))
                            .setApplicationName(applicationProperties.getName()).build();
        }
    }

    public Boolean isAvailable() {
        return gmail != null;
    }

    public Message send(Message message) throws IOException, GeneralSecurityException {
        try {
            message = gmail.users().messages().send(USER, message).execute();
            return message;
        } catch (IOException e) {
            googleOauthService.resetCredential();
            initiateGmail();
            throw e;
        }
    }
}