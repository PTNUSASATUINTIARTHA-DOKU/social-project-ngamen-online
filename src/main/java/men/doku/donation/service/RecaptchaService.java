package men.doku.donation.service;

import java.time.Duration;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import men.doku.donation.config.ApplicationProperties;
import men.doku.donation.service.dto.RecaptchaVerifyRequestDTO;
import men.doku.donation.service.dto.RecaptchaVerifyResponseDTO;
import men.doku.donation.service.mapper.DTOMapper;

@Service
public class RecaptchaService {

    private final Logger log = LoggerFactory.getLogger(RecaptchaService.class);

    private final ApplicationProperties applicationProperties;
    private final RestTemplate restTemplate;
    private final DTOMapper dtoMapper;

    public RecaptchaService(ApplicationProperties applicationProperties, RestTemplateBuilder restTemplateBuilder,
            DTOMapper dtoMapper) {
        this.applicationProperties = applicationProperties;
        this.restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30)).build();
        this.dtoMapper = dtoMapper;
    }

    public Boolean checkRecaptcha(String token, String remoteIp) {
        if (applicationProperties.getRecaptcha().getActive()) {
            log.debug("TOKEN {}", token);
            log.debug("REMOTE IP {}", remoteIp);
            Optional<String> recaptchaKey = getKey();
            if (recaptchaKey.isPresent()) {
                RecaptchaVerifyRequestDTO request = new RecaptchaVerifyRequestDTO(recaptchaKey.get(), token, remoteIp);
                log.debug("REQUEST {}", request);
                String url = applicationProperties.getRecaptcha().getVerifyUrl();
                log.debug("URL {}", url);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(
                    dtoMapper.recaptchaVerifyRequestDTOToMultiValueMap(request), headers);
                ResponseEntity<RecaptchaVerifyResponseDTO> response = new ResponseEntity<>(HttpStatus.PROCESSING);
                log.debug("Entity {}", entity);
                try {
                    response = this.restTemplate.postForEntity(url, entity, RecaptchaVerifyResponseDTO.class);
                    log.debug("Response {}", response);
                } catch (RestClientException rce) {
                    log.error("RestClientException {}", rce.getMessage());
                }
                if (response.getStatusCode() == HttpStatus.OK) {
                    return (response.getBody().getSuccess());
                }
            }
            return false;
        } else {
            return applicationProperties.getRecaptcha().getSimulatorResult();
        }
    }

    private Optional<String> getKey() {
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(applicationProperties.getRecaptcha().getRegion()).build();
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(applicationProperties.getRecaptcha().getKey());
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
        String secretString = "";
        if (getSecretValueResult.getSecretString() != null) {
            secretString = getSecretValueResult.getSecretString();
        } else {
            secretString = new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        RecaptchaKey recaptcha;
        try {
            recaptcha = objectMapper.readValue(secretString, RecaptchaKey.class);
            return Optional.of(recaptcha.getKey());
        } catch (JsonProcessingException e) {
            log.error("Unable to parse Recaptcha Key from AWS {} ", e);
            return Optional.empty();
        }
    }
}

class RecaptchaKey {

    @JsonProperty("reCAPTCHA")
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}