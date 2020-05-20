package men.doku.donation.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import men.doku.donation.service.AwsStorageService;

@RestController
@RequestMapping("/api")
public class AwsStorageResource {

    private final Logger log = LoggerFactory.getLogger(AwsStorageResource.class);

    private final AwsStorageService awsStorageService;

    public AwsStorageResource(AwsStorageService awsStorageService) {
        this.awsStorageService = awsStorageService;
    }

    /**
     * Generate pre signed url
     * 
     * @param paymentDTO object PaymentDTO contains DonationDTO and Transaction
     * @return
     */
    @GetMapping("/storages/url/{fileName}")
    public ResponseEntity<String> getPresignedUrlUpload(@PathVariable String fileName) {
        log.debug("REST request to get AWS S3 presigned URL with fileName : {}", fileName);
        return ResponseEntity.ok().header(null).body(awsStorageService.getPresignedUrlUpload(fileName));
    }
}