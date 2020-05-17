package men.doku.donation.service.dto;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecaptchaVerifyResponseDTO {

    Boolean success;

    @JsonProperty("challenge_ts")
    String challengeTs;

    String hostname;

    @JsonProperty("error-codes")
    String[] errorCodes;

    Long score;

    String action;

    public RecaptchaVerifyResponseDTO() {
    }

    public RecaptchaVerifyResponseDTO(Boolean success, String challengeTs, String hostname, String[] errorCodes,
            Long score, String action) {
        this.success = success;
        this.challengeTs = challengeTs;
        this.hostname = hostname;
        this.errorCodes = errorCodes;
        this.score = score;
        this.action = action;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getChallengeTs() {
        return challengeTs;
    }

    public void setChallengeTs(String challengeTs) {
        this.challengeTs = challengeTs;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String[] getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(String[] errorCodes) {
        this.errorCodes = errorCodes;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "RecaptchaVerifyResponseDTO [action=" + action + ", challengeTs=" + challengeTs + ", errorCodes="
                + Arrays.toString(errorCodes) + ", hostname=" + hostname + ", score=" + score + ", success=" + success
                + "]";
    }
}