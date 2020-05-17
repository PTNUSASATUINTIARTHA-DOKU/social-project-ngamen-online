package men.doku.donation.service.dto;

public class RecaptchaVerifyRequestDTO {
    
    String secret;
    String response;
    String remoteip;

    public RecaptchaVerifyRequestDTO() {
    }

    public RecaptchaVerifyRequestDTO(String secret, String response, String remoteip) {
        this.secret = secret;
        this.response = response;
        this.remoteip = remoteip;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRemoteip() {
        return remoteip;
    }

    public void setRemoteip(String remoteip) {
        this.remoteip = remoteip;
    }

    @Override
    public String toString() {
        return "RecaptchaVerifyRequestDTO [remoteip=" + remoteip + ", response=" + response + ", secret=" + secret
                + "]";
    }
}