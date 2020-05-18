package men.doku.donation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Donation.
 * <p>
 * Properties are configured in the {@code application.yml} file. See
 * {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String name = new String();
    private String mibUrl = new String();
    private Report report = new Report();
    private Gmail gmail = new Gmail();
    private Recaptcha recaptcha = new Recaptcha();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMibUrl() {
        return mibUrl;
    }

    public void setMibUrl(String mibUrl) {
        this.mibUrl = mibUrl;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Gmail getGmail() {
        return gmail;
    }

    public void setGmail(Gmail gmail) {
        this.gmail = gmail;
    }

    public Recaptcha getRecaptcha() {
        return recaptcha;
    }

    public void setRecaptcha(Recaptcha recaptcha) {
        this.recaptcha = recaptcha;
    }

    public static class Gmail {

        private Boolean active = false;
        private String username = new String();
        private Api api = new Api();
    
        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }

        public String getUsername() {
            return username;
        }
    
        public void setUsername(String username) {
            this.username = username;
        }
    
        public Api getApi() {
            return api;
        }
    
        public void setApi(Api api) {
            this.api = api;
        }
    }  
    
    public static class Api {

        private String host = new String();
        private Integer port = 0;
        private String secret = new String();
        private String region = new String();
        private String credentialFolder = new String();

        public String getCredentialFolder() {
            return credentialFolder;
        }
    
        public void setCredentialFolder(String credentialFolder) {
            this.credentialFolder = credentialFolder;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }
    }

    public static class Report {

        private String folder = new String();
        private String basename = new String();

        public String getFolder() {
            return folder;
        }

        public void setFolder(String folder) {
            this.folder = folder;
        }

        public String getBasename() {
            return basename;
        }

        public void setBasename(String basename) {
            this.basename = basename;
        }
    }

    public static class Recaptcha {

        private String key = new String();
        private Boolean active = false;
        private String verifyUrl = new String();
        private String region = new String();
        private Float simulatorResult = 1F;
        private Threshold threshold = new Threshold();

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }

        public String getVerifyUrl() {
            return verifyUrl;
        }

        public void setVerifyUrl(String verifyUrl) {
            this.verifyUrl = verifyUrl;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public Float getSimulatorResult() {
            return simulatorResult;
        }

        public void setSimulatorResult(Float simulatorResult) {
            this.simulatorResult = simulatorResult;
        }

        public Threshold getThreshold() {
            return threshold;
        }

        public void setThreshold(Threshold threshold) {
            this.threshold = threshold;
        }
    }

    public static class Threshold {

        private Float payment = 0F;
        private Float login = 0F;

        public Float getPayment() {
            return payment;
        }

        public void setPayment(Float payment) {
            this.payment = payment;
        }

        public Float getLogin() {
            return login;
        }

        public void setLogin(Float login) {
            this.login = login;
        }
    }
}
