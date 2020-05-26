package men.doku.donation.service;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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
import men.doku.donation.web.rest.HttpReqRespUtils;

@Service
public class RecaptchaService {

    private final Logger log = LoggerFactory.getLogger(RecaptchaService.class);

    private final ApplicationProperties applicationProperties;
    private final RestTemplate restTemplate;
    private final DTOMapper dtoMapper;
    private final AwsSecretService awsSecretService;

    public RecaptchaService(ApplicationProperties applicationProperties, RestTemplateBuilder restTemplateBuilder,
            DTOMapper dtoMapper, AwsSecretService awsSecretService) {
        this.applicationProperties = applicationProperties;
        this.restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30)).build();
        this.dtoMapper = dtoMapper;
        this.awsSecretService = awsSecretService;
    }

    public Optional<Float> checkRecaptcha(String token, HttpServletRequest servletRequest, String action) {
        if (applicationProperties.getGoogle().getRecaptcha().getActive()) {
            String remoteIp = HttpReqRespUtils.getClientIpAddress(servletRequest);
            log.debug("TOKEN {}", token);
            log.debug("REMOTE IP {}", remoteIp);
            String recaptchaSecret = awsSecretService.getSecret(applicationProperties.getAws().getSecret().getRecaptcha());
            ObjectMapper objectMapper = new ObjectMapper();
            RecaptchaKey recaptcha = new RecaptchaKey();
            try {
                recaptcha = objectMapper.readValue(recaptchaSecret, RecaptchaKey.class);
            } catch (JsonProcessingException e) {
                log.error("Unable to parse Recaptcha Key from AWS {} ", e);
                Optional.empty();
            }    
            if (recaptcha != null) {
                RecaptchaVerifyRequestDTO request = new RecaptchaVerifyRequestDTO(recaptcha.getKey(), token, remoteIp);
                log.debug("REQUEST {}", request);
                String url = applicationProperties.getGoogle().getRecaptcha().getVerifyUrl();
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
                    return Optional.empty();
                }
                if (response.getStatusCode() == HttpStatus.OK) {
                    if (response.getBody().getSuccess()) {
                        return Optional.of(response.getBody().getScore());
                    } else {
                        return Optional.of(0F);
                    }
                } else {
                    return Optional.of(0F);
                }
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.of(applicationProperties.getGoogle().getRecaptcha().getSimulatorResult());
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