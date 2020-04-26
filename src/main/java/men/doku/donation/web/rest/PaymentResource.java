package men.doku.donation.web.rest;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import men.doku.donation.domain.Donation;
import men.doku.donation.service.PaymentService;
import men.doku.donation.service.dto.MibRequestDTO;
import men.doku.donation.service.dto.MibResponseDTO;
import men.doku.donation.service.dto.PaymentDTO;

@RestController
@RequestMapping("/api")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(DonationResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    
    private final PaymentService paymentService;

    public PaymentResource(PaymentService paymentService) {
        this.paymentService = paymentService;
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
        Optional<Donation> donation = paymentService.findOneByPaymentSlug(slug);
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
        PaymentDTO payment = new PaymentDTO(paymentDTO.getDonation(), paymentService.payment(paymentDTO));
        return ResponseEntity.ok().body(payment);
    }

    /**
     * Simulator MIB
     * 
     * Tester simulator
     * curl -X POST -d "MALLID=1&SERVICEID=1&ACQUIRERID=1&INVOICENUMBER=20200426204800&
     *      CURRENCY=IDR&AMOUNT=10000&SESSIONID=abcdefghijklmnopqrstuvwxyz&AUTH1=0819688869&
     *      BASKET=donasi,10000,1,10000&WORDS=1234567890123456" 
     *      http://localhost:8080/api/payments/simulator 
     * 
     * Scenario 0 no parameter sent, unexpected error
     * Scenario 1 Failed amount > 10000000
     * Scenario 2 Timeout OVO ID = 080000000000
     * Scenario 3 not getting response after 30s 
     * Other than this, success.
     * 
     * @param mibRequestDTO object mibRequestDTO like request to MIB using x-www-form-url-encoded
     * @return
     */
    @PostMapping(
        path = "/payments/simulator",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<MibResponseDTO> simulatorMib(@Valid MibRequestDTO mibRequestDTO) {
        log.debug("URL ENCODED request to simulator MIB : {}", mibRequestDTO);
        return ResponseEntity.ok().body(paymentService.simulatorMib(mibRequestDTO));
    }
}
