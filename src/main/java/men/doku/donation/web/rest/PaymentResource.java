package men.doku.donation.web.rest;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.jhipster.web.util.ResponseUtil;
import io.micrometer.core.ipc.http.HttpSender.Response;
import men.doku.donation.domain.Donation;
import men.doku.donation.service.DonationService;
import men.doku.donation.service.TransactionService;
import men.doku.donation.service.dto.PaymentDTO;

@RestController
@RequestMapping("/api")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(DonationResource.class);

    private static final String ENTITY_NAME = "payment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    
    private final DonationService donationService;
    private final TransactionService transactionService;

    public PaymentResource(DonationService donationService, TransactionService transactionService) {
        this.donationService = donationService;
        this.transactionService = transactionService;
    }

    /**
     * {@code GET  /payments/:slug} : get the "slug" payment.
     *
     * @param slug the slug of the payment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payments/{slug}")
    public ResponseEntity<PaymentDTO> getDonationByPaymentSlug(@PathVariable String slug) {
        log.debug("REST request to get Donation by Payment Slug : {}", slug);
        Optional<Donation> donation = donationService.findOneByPaymentSlug(slug);
        return ResponseEntity.ok().body(
            donation.map(don -> new PaymentDTO(don)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    /**
     * Initiate Payment
     * 
     * @param paymentDTO object PaymentDTO contains DonationDTO and Transaction
     * @return
     */
    @PostMapping("/payments")
    public ResponseEntity<PaymentDTO> initiatePayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        log.debug("REST request to initiate Payment : {}", paymentDTO);
        PaymentDTO payment = new PaymentDTO(paymentDTO.getDonation(), transactionService.payment(paymentDTO));
        return ResponseEntity.ok().body(payment);
    }

}