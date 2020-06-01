package men.doku.donation.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.config.JHipsterProperties;
import men.doku.donation.config.ApplicationProperties;
import men.doku.donation.config.Constants;
import men.doku.donation.domain.Donation;
import men.doku.donation.domain.Organizer;
import men.doku.donation.domain.User;
import men.doku.donation.domain.enumeration.IsActiveStatus;
import men.doku.donation.repository.DonationRepository;
import men.doku.donation.security.AuthoritiesConstants;
import men.doku.donation.security.SecurityUtils;
import men.doku.donation.service.AwsStorageService;
import men.doku.donation.service.DonationService;
import men.doku.donation.service.MailService;
import men.doku.donation.service.OrganizerService;

/**
 * Service Implementation for managing {@link Donation}.
 */
@Service
@Transactional
public class DonationServiceImpl implements DonationService {

    private final Logger log = LoggerFactory.getLogger(DonationServiceImpl.class);

    private final DonationRepository donationRepository;
    private final OrganizerService organizerService;
    private final AwsStorageService awsStorageService;
    private final MailService mailService;
    private final JHipsterProperties jHipsterProperties;
    private final ApplicationProperties applicationProperties;

    public DonationServiceImpl(DonationRepository donationRepository, OrganizerService organizerService,
            AwsStorageService awsStorageService, MailService mailService, JHipsterProperties jHipsterProperties,
            ApplicationProperties applicationProperties) {
        this.donationRepository = donationRepository;
        this.organizerService = organizerService;
        this.awsStorageService = awsStorageService;
        this.mailService = mailService;
        this.jHipsterProperties = jHipsterProperties;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a donation.
     *
     * @param donation the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Donation save(Donation donation) {
        final String login = SecurityUtils.getCurrentUserLogin().get();
        log.debug("Request by {} to save Donation : {}", login, donation);
        Organizer organizer = organizerService.findOne(donation.getOrganizer().getId()).get();
        if (donation.getLogo() != null && !donation.getLogo().startsWith("https://"))
            donation.setLogo(this.awsStorageService.copyFromUploadToCdn(donation.getLogo()));
        donation.setLastUpdatedAt(Instant.now());
        donation.setLastUpdatedBy(login);
        if (!organizer.getStatus().equals(IsActiveStatus.ACTIVE))
            donation.setStatus(organizer.getStatus());
        return donationRepository.save(donation);
    }

    @Override
    public Boolean checkDonationAuthority(Long donationId, String login) {
        if (!SecurityUtils.isCurrentUserInRole(Constants.ADMIN)) {
            List<Long> organizerIds = organizerService.findAllIdsOwnedWithEagerRealtionships(login);
            return (donationRepository.checkDonationAuthority(donationId, organizerIds) == 1);
        }
        return true;
    }

    /**
     * Update Donation Status based on Organizer Id and Organizer status
     * 
     * @param organizerId
     * @param organizerStatus
     */
    @Override
    public void updateStatusByOrganizerId(Long organizerId, IsActiveStatus organizerStatus) {
        donationRepository.findAllByOrganizerId(organizerId).forEach(donation -> {
            donation.setStatus(organizerStatus);
            save(donation);
        });
    }

    /**
     * Get all the donations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Donation> findAll(Pageable pageable) {
        final String login = SecurityUtils.getCurrentUserLogin().get();
        log.debug("Request by {} to get all Donations", login);
        if (SecurityUtils.isCurrentUserInRole(Constants.ADMIN)) {
            return donationRepository.findAll(pageable);
        } else {
            List<Long> organizerIds = organizerService.findAllIdsOwnedWithEagerRealtionships(login);
            return donationRepository.findAllByOrganizerIds(organizerIds, pageable);
        }
    }

    /**
     * Get one donation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Donation> findOne(Long id) {
        log.debug("Request by {} to get Donation : {}", SecurityUtils.getCurrentUserLogin().get(), id);
        return donationRepository.findById(id);
    }

    /**
     * Get one donation by Example.
     *
     * @param Example<S> the example of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Donation> findOne(Example<Donation> donation) {
        final String currentUser = SecurityUtils.getCurrentUserLogin().get();
        log.debug("Request by {} to get Donation : {}", currentUser, donation);
        return donationRepository.findOne(donation);
    }

    /**
     * Delete the donation by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        Donation donation = findOne(id).get();
        log.info("Request by {} to delete Donation : {}", SecurityUtils.getCurrentUserLogin().get(), donation);
        donationRepository.deleteById(id);
    }

    @Override
    public void sendEmail(Long id) {
        Donation donation = findOne(id).get();
        String paymentUrl = jHipsterProperties.getMail().getBaseUrl() + "/payment/" + donation.getPaymentSlug()
                + "/offline";
        File qr = generateQR(paymentUrl);
        Organizer organizer = donation.getOrganizer();
        List<User> users = organizerService.findUserEmails(organizer.getId());
        Optional<User> userOffline = users.stream()
                .filter(user -> user.getAuthorities().stream()
                        .filter(authority -> authority.getName().equals(AuthoritiesConstants.OFFLINE_STORE)).findFirst()
                        .isPresent())
                .findFirst();
        userOffline.ifPresent(user -> mailService.sendOfflineStoreCreationEmail(user, qr));
    }

    private File generateQR(String text) {
        try {
            QRCodeWriter qrWritter = new QRCodeWriter();
            BitMatrix bitMatrix = qrWritter.encode(text, BarcodeFormat.QR_CODE, 250, 250);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            File output = new File(applicationProperties.getReport().getFolder() + "qr.jpg");
            ImageIO.write(bufferedImage, "jpg", output);
            return output;    
        } catch (WriterException | IOException e) {
            log.error("Unable to Write QR {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
