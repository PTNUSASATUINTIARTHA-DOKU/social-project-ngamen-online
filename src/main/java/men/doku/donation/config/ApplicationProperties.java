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
    private Gmail gmail = new Gmail();

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

    public Gmail getGmail() {
        return gmail;
    }

    public void setGmail(Gmail gmail) {
        this.gmail = gmail;
    }

    public static class Gmail {

        private String username = new String();
        private String credential = new String();
        private String credentialFolder = new String();
    
        public String getUsername() {
            return username;
        }
    
        public void setUsername(String username) {
            this.username = username;
        }
    
        public String getCredential() {
            return credential;
        }
    
        public void setCredential(String credential) {
            this.credential = credential;
        }

        public String getCredentialFolder() {
            return credentialFolder;
        }
    
        public void setCredentialFolder(String credentialFolder) {
            this.credentialFolder = credentialFolder;
        }
    }    
}