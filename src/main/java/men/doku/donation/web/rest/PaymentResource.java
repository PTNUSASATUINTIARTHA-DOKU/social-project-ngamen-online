package men.doku.donation.web.rest;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import men.doku.donation.domain.Donation;
import men.doku.donation.domain.Transaction;
import men.doku.donation.service.DonationService;
import men.doku.donation.service.PaymentService;
import men.doku.donation.service.TransactionService;
import men.doku.donation.service.dto.MibRequestDTO;
import men.doku.donation.service.dto.MibResponseDTO;

@RestController
@RequestMapping("/api")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(DonationResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    
    private final PaymentService paymentService;
    private final DonationService donationService;
    private final TransactionService transactionService;

    public PaymentResource(PaymentService paymentService, DonationService donationService, TransactionService transactionService) {
        this.paymentService = paymentService;
        this.donationService = donationService;
        this.transactionService = transactionService;
    }

    /**
     * {@code GET  /payments/:slug/slug} : get the "slug" payment.
     *
     * @param slug the slug of the donation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payments/{slug}/slug")
    public ResponseEntity<Donation> getDonationByPaymentSlug(@PathVariable String slug) {
        log.debug("REST request to get Donation by Payment Slug : {}", slug);
        Donation exampleDonation = new Donation();
        exampleDonation.setPaymentSlug(slug);
        Optional<Donation> donation = donationService.findOne(Example.of(exampleDonation));
        return ResponseUtil.wrapOrNotFound(donation);
    }

    /**
     * {@code GET  /payments/:invoice/invoice} : get the "slug" payment.
     *
     * @param invoice the invoice of the transaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payments/{invoice}/invoice")
    public ResponseEntity<Transaction> getTransactionByInvoiceNumber(@PathVariable String invoice) {
        log.debug("REST request to get Transaction by Invoice Number : {}", invoice);
        Transaction exampleTransaction = new Transaction();
        exampleTransaction.setInvoiceNumber(invoice);
        Optional<Transaction> transaction = transactionService.findOne(Example.of(exampleTransaction));
        return ResponseUtil.wrapOrNotFound(transaction);
    }


    /**
     * Initiate Payment
     * 
     * @param paymentDTO object PaymentDTO contains DonationDTO and Transaction
     * @return
     */
    @PostMapping("/payments")
    public ResponseEntity<Transaction> initiatePayment(@Valid @RequestBody Transaction transaction) {
        log.debug("REST request to initiate Payment : {}", transaction);
        Transaction result = transactionService.pay(transaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, "Transaction", transaction.getId().toString()))
            .body(result);
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
