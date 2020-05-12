package men.doku.donation.service;

import java.io.IOException;
import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;

import org.springframework.stereotype.Service;

import men.doku.donation.config.ApplicationProperties;

@Service
public class GmailService {

    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final String ACCESS_TYPE = "offline";
    private final String USER = "me";


    private Gmail gmail;
    // private static final List<String> SCOPES = Arrays.asList(
    //     GmailScopes.MAIL_GOOGLE_COM, GmailScopes.GMAIL_COMPOSE, GmailScopes.GMAIL_SEND, GmailScopes.GMAIL_LABELS,
    //     GmailScopes.GMAIL_INSERT, GmailScopes.GMAIL_MODIFY, GmailScopes.GMAIL_READONLY);
    private final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_COMPOSE, GmailScopes.GMAIL_SEND);

    private final ApplicationProperties applicationProperties;

    public GmailService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public Optional<Gmail> getGmail() {
        return Optional.ofNullable(gmail);
    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new StringReader(getSecret()));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(applicationProperties.getGmail().getApi().getCredentialFolder())))
                .setAccessType(ACCESS_TYPE)
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setHost(applicationProperties.getGmail().getApi().getHost())
                .setPort(applicationProperties.getGmail().getApi().getPort())
                .build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize(USER);
    }

    @PostConstruct
    public void initiateOAuth() throws IOException, GeneralSecurityException {
        if (applicationProperties.getGmail().getActive()) {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            gmail = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(applicationProperties.getName())
                    .build();    
        }
    }

    private String getSecret() {
        AWSSecretsManager client  = AWSSecretsManagerClientBuilder.standard()
                                        .withRegion(applicationProperties.getGmail().getApi().getRegion())
                                        .build();
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                        .withSecretId(applicationProperties.getGmail().getApi().getSecret());
        GetSecretValueResult getSecretValueResult = null;
    
        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        } catch (DecryptionFailureException e) {
            throw e;
        } catch (InternalServiceErrorException e) {
            throw e;
        } catch (InvalidParameterException e) {
            throw e;
        } catch (InvalidRequestException e) {
            throw e;
        } catch (ResourceNotFoundException e) {
            throw e;
        }
        if (getSecretValueResult.getSecretString() != null) {
            return getSecretValueResult.getSecretString();
        } else {
            return new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
        }
    }
}