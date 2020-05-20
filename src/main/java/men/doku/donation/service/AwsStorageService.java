package men.doku.donation.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import men.doku.donation.config.ApplicationProperties;

@Service
public class AwsStorageService {

    private final Logger log = LoggerFactory.getLogger(AwsStorageService.class);

    private ApplicationProperties applicationProperties;

    public AwsStorageService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    private AmazonS3 client() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(applicationProperties.getAws().getRegion())
                .build();
    }

    public String getPresignedUrlUpload(String fileName) {
        String newName = FilenameUtils.getBaseName(fileName) + "-" 
                + UUID.randomUUID().toString() 
                + "." + FilenameUtils.getExtension(fileName);
        log.debug("bucket {}", applicationProperties.getAws().getS3().getBucket());
        log.debug("new name {}", newName);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(applicationProperties.getAws().getS3().getBucket().getUpload(), newName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(Date.from(Instant.now().plus(3, ChronoUnit.MINUTES)));
        return client().generatePresignedUrl(request).toString();
    } 

    public String copyFromUploadToCdn(String fileName) {
        log.debug("Copy file from bucket Upload to CDN {}", fileName);
        String bucketUpload = applicationProperties.getAws().getS3().getBucket().getUpload();
        log.debug("Copy from {}", bucketUpload);
        String bucketCdn = applicationProperties.getAws().getS3().getBucket().getCdn();
        log.debug("Copy to {}", bucketCdn);
        client().copyObject(bucketUpload, fileName, bucketCdn, fileName);
        log.debug("Copy success");
        client().setObjectAcl(bucketCdn, fileName, CannedAccessControlList.PublicRead);
        log.debug("Make public");
        return client().getUrl(bucketCdn, fileName).toExternalForm();
    }
}