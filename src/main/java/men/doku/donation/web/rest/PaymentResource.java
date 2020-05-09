package men.doku.donation.web.rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import men.doku.donation.domain.Donation;
import men.doku.donation.domain.Organizer;
import men.doku.donation.domain.Transaction;
import men.doku.donation.security.AuthoritiesConstants;
import men.doku.donation.service.DailyReportService;
import men.doku.donation.service.DonationService;
import men.doku.donation.service.MailService;
import men.doku.donation.service.OrganizerService;
import men.doku.donation.service.PaymentService;
import men.doku.donation.service.TransactionService;
import men.doku.donation.service.dto.DailyReportSuccessDTO;
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
    private final DailyReportService dailyReportService;
    private final OrganizerService organizerService;
    private final MailService mailService;

    public PaymentResource(PaymentService paymentService, DonationService donationService,
            TransactionService transactionService, DailyReportService dailyReportService,
            OrganizerService organizerService, MailService mailService) {
        this.paymentService = paymentService;
        this.donationService = donationService;
        this.transactionService = transactionService;
        this.dailyReportService = dailyReportService;
        this.organizerService = organizerService;
        this.mailService = mailService;
    }

    /**
     * {@code GET  /payments/:slug/slug} : get the "slug" payment.
     *
     * @param slug the slug of the donation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the donation, or with status {@code 404 (Not Found)}.
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
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the donation, or with status {@code 404 (Not Found)}.
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
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, "Transaction",
                transaction.getId().toString())).body(result);
    }

    /**
     * Simulator MIB
     * 
     * Tester simulator curl -X POST -d
     * "MALLID=1&SERVICEID=1&ACQUIRERID=1&INVOICENUMBER=20200426204800&
     * CURRENCY=IDR&AMOUNT=10000&SESSIONID=abcdefghijklmnopqrstuvwxyz&AUTH1=0819688869&
     * BASKET=donasi,10000,1,10000&WORDS=1234567890123456"
     * http://localhost:8080/api/payments/simulator
     * 
     * Scenario 0 no parameter sent, unexpected error Scenario 1 Failed amount >
     * 10000000 Scenario 2 Timeout OVO ID = 080000000000 Scenario 3 not getting
     * response after 30s Other than this, success.
     * 
     * @param mibRequestDTO object mibRequestDTO like request to MIB using
     *                      x-www-form-url-encoded
     * @return
     */
    @PostMapping(path = "/payments/simulator", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<MibResponseDTO> simulatorMib(@Valid MibRequestDTO mibRequestDTO) {
        log.debug("URL ENCODED request to simulator MIB : {}", mibRequestDTO);
        return ResponseEntity.ok().body(paymentService.simulatorMib(mibRequestDTO));
    }

    /**
     * {@code GET  /payments/report/data} : get daily report data.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of Daily Reports in body.
     */
    @GetMapping(value ="/payments/report/data", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Map<String, List<DailyReportSuccessDTO>>> generateDailyReportData() {
        log.debug("REST request to create daily report");
        Map<String, List<DailyReportSuccessDTO>> report = dailyReportService.generateData();
        return ResponseEntity.ok().body(report);
    }

    /**
     * {@code GET  /payments/report/data} : get daily report data.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of Daily Reports in body.
     * @throws IOException
     */
    @GetMapping(value = "/payments/report/file/{organizerId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public @ResponseBody byte[] generateDailyReportFile(@PathVariable Long organizerId) throws IOException {
        log.debug("REST request to create daily report");
        Organizer organizer = organizerService.findOne(organizerId).get();
        String key = organizer.getName() + "|" + organizer.getEmail();
        Map<String, List<DailyReportSuccessDTO>> data = dailyReportService.generateData();
        List<DailyReportSuccessDTO> report = data.get(key);
        String filename = dailyReportService.generateFile(organizer.getName() , report);    
        FileInputStream is = new FileInputStream(filename);
        return FileCopyUtils.copyToByteArray(is);
    }

    /**
     * {@code GET  /payments/report/data} : get daily report data.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of Daily Reports in body.
     * @throws IOException
     */
    @GetMapping(value = "/payments/report/email/{organizerId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> emailDailyReport(@PathVariable Long organizerId) throws IOException {
        log.debug("REST request to create daily report");
        Organizer organizer = organizerService.findOne(organizerId).get();
        String date = Instant.now().atZone(ZoneId.of("Asia/Jakarta")).toLocalDate().format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
        String fileName = "reports/donation_" + organizer.getName().replace(' ', '_') + "_"  + date + ".csv";
        MimeMessageHelper message = mailService.sendEmailWithAttachment(organizer.getEmail(), "Saweran.charity Daily Report " + date, "Dear all, <br/>Terlampir laporan harian.<br/>Terima kasih.", fileName.substring(8), fileName);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert(applicationName, "Daily Report ", fileName)).body(message);
    }
}
