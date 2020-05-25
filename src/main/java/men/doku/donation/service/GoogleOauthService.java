package men.doku.donation.service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import men.doku.donation.config.ApplicationProperties;

@Service
public class GoogleOauthService {

    private final Logger log = LoggerFactory.getLogger(GoogleOauthService.class);

    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final String ACCESS_TYPE = "offline";
    private final String USER = "me";

    private ApplicationProperties applicationProperties;
    private AwsSecretService awsSecretService;
    private Credential credential;

    public GoogleOauthService(ApplicationProperties applicationProperties, AwsSecretService awsSecretService) {
        this.applicationProperties = applicationProperties;
        this.awsSecretService = awsSecretService;
    }

    public String getRefreshToken() {
        return awsSecretService.getSecret(applicationProperties.getAws().getSecret().getOauth());
    }

    public void setRefreshToken(String refreshToken) {
        awsSecretService.setSecret(applicationProperties.getAws().getSecret().getOauth(), refreshToken);
    }

    /**
     * Creates an authorized Credential object.
     * 
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException              If the credentials.json file cannot be
     *                                  found.
     * @throws GeneralSecurityException
     */
    public Credential getCredentials(String storedCredentialFolder, List<String> scopes,
            final NetHttpTransport httpTransport) throws IOException, GeneralSecurityException {
        // Load client secrets.
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new StringReader(awsSecretService.getSecret(applicationProperties.getAws().getSecret().getGmail())));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
                clientSecrets, scopes).setDataStoreFactory(new FileDataStoreFactory(new File(storedCredentialFolder)))
                        .setAccessType(ACCESS_TYPE).build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setHost(applicationProperties.getGoogle().getGmail().getHost())
                .setPort(applicationProperties.getGoogle().getGmail().getPort()).build();
        credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize(USER);
        log.debug("Access token {}", credential.getAccessToken());
        if (credential.getRefreshToken() != null) {
            log.info("Refresh token exist. Store to AWS.");
            setRefreshToken(credential.getRefreshToken());
        }
        return credential;
    }

    @Scheduled(cron = "0 50 * * * *")
    public void generate() {
        try {
            credential.setRefreshToken(getRefreshToken());
            log.debug("Old Access token {}", credential.getAccessToken());
            if (credential.refreshToken()) {
                log.info("Refresh Google Token Success");
            } else {
                log.warn("Refresh Google Token Failed");
            }
            log.debug("New Access token {}", credential.getAccessToken());
        } catch (IOException e) {
            log.error("Failed to Refresh Google Token {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
