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

    private String mibUrl = new String();

    public String getMibUrl() {
        return mibUrl;
    }

    public void setMibUrl(String mibUrl) {
        this.mibUrl = mibUrl;
    }

}
