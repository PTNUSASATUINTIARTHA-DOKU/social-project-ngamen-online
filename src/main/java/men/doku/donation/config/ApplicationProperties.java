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
    private Aws aws = new Aws();

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

    public Aws getAws() {
        return aws;
    }

    public void setAws(Aws aws) {
        this.aws = aws;
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

        private Boolean active = false;
        private String verifyUrl = new String();
        private Float simulatorResult = 1F;
        private Threshold threshold = new Threshold();

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
        private Float resetPassword = 0F;

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

        public Float getResetPassword() {
            return resetPassword;
        }

        public void setResetPassword(Float resetPassword) {
            this.resetPassword = resetPassword;
        }
    }

    public static class Aws {

        private Boolean active = false;
        private String region = new String();
        private Secret secret = new Secret();
        private S3 s3 = new S3();

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public Secret getSecret() {
            return secret;
        }

        public void setSecret(Secret secret) {
            this.secret = secret;
        }

        public S3 getS3() {
            return s3;
        }

        public void setS3(S3 s3) {
            this.s3 = s3;
        }
    }

    public static class Secret {

        private String gmail = new String();
        private String recaptcha = new String();

        public String getGmail() {
            return gmail;
        }

        public void setGmail(String gmail) {
            this.gmail = gmail;
        }

        public String getRecaptcha() {
            return recaptcha;
        }

        public void setRecaptcha(String recaptcha) {
            this.recaptcha = recaptcha;
        }
    }

    public static class S3 {

        private Bucket bucket = new Bucket();
        private Integer durationUpload = 0;
        private Integer durationDownload = 0;

        public Bucket getBucket() {
            return bucket;
        }

        public void setBucket(Bucket bucket) {
            this.bucket = bucket;
        }

        public Integer getDurationUpload() {
            return durationUpload;
        }

        public void setDurationUpload(Integer durationUpload) {
            this.durationUpload = durationUpload;
        }

        public Integer getDurationDownload() {
            return durationDownload;
        }

        public void setDurationDownload(Integer durationDownload) {
            this.durationDownload = durationDownload;
        }
    }

    public static class Bucket {

        private String upload = new String();
        private String cdn = new String();

        public String getUpload() {
            return upload;
        }

        public void setUpload(String upload) {
            this.upload = upload;
        }

        public String getCdn() {
            return cdn;
        }

        public void setCdn(String cdn) {
            this.cdn = cdn;
        }
    }
}
