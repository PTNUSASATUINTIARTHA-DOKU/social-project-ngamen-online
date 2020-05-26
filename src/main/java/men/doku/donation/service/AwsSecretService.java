package men.doku.donation.service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.PutSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import men.doku.donation.config.ApplicationProperties;

@Service
public class AwsSecretService {

    private final Logger log = LoggerFactory.getLogger(AwsSecretService.class);

    private AWSSecretsManager awsSecretsManager;
    private Map<String, String> secrets = new HashMap<>();

    public AwsSecretService(ApplicationProperties applicationProperties) {
        awsSecretsManager = AWSSecretsManagerClientBuilder.standard()
                .withRegion(applicationProperties.getAws().getRegion()).build();
    }

    public String getSecret(String secretId) {
        log.debug("Request AWS Get Secret for id {}", secretId);

        if (secrets.get(secretId) != null) {
            log.debug("Local Secret exists, no need to go to AwsSecretManager");
            return secrets.get(secretId);
        }

        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretId);
        GetSecretValueResult getSecretValueResult = null;

        try {
            getSecretValueResult = awsSecretsManager.getSecretValue(getSecretValueRequest);
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

        String result = "";
        if (getSecretValueResult.getSecretString() != null) {
            result = getSecretValueResult.getSecretString();
        } else {
            result = new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
        }

        secrets.put(secretId, result);
        return result;
    }

    public void setSecret(String secretId, String secret) {
        log.debug("Request AWS Set Secret for id {}", secretId);
        PutSecretValueRequest putSecretValueRequest = new PutSecretValueRequest().withSecretId(secretId)
                .withSecretString(secret);
        awsSecretsManager.putSecretValue(putSecretValueRequest);
        secrets.put(secretId, secret);
    }
}
