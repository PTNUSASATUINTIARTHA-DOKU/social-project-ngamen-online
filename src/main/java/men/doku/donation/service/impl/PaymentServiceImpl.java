package men.doku.donation.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import men.doku.donation.config.ApplicationProperties;
import men.doku.donation.domain.Donation;
import men.doku.donation.domain.Transaction;
import men.doku.donation.domain.enumeration.TransactionStatus;
import men.doku.donation.service.DonationService;
import men.doku.donation.service.PaymentService;
import men.doku.donation.service.TransactionService;
import men.doku.donation.service.dto.MibRequestDTO;
import men.doku.donation.service.dto.MibResponseDTO;
import men.doku.donation.service.dto.PaymentDTO;
import men.doku.donation.service.mapper.MibMapper;

/**
 * Service Implementation for managing {@link Transaction}.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final DonationService donationService;
    private final TransactionService transactionService;
    private final RestTemplate restTemplate;
    private final ApplicationProperties applicationProperties;
    private final MibMapper mibMapper;

    public PaymentServiceImpl(DonationService donationService, 
            TransactionService transactionService,
            RestTemplateBuilder restTemplateBuilder,
            ApplicationProperties applicationProperties,
            MibMapper mibMapper) {
        this.donationService = donationService;
        this.transactionService = transactionService;
        this.restTemplate = restTemplateBuilder.build();
        this.applicationProperties = applicationProperties;
        this.mibMapper = mibMapper;
    }

    /**
     * Get one donation by slug.
     *
     * @param paymentSlug the paymentSlug of the entity.
     * @return the entity.
     */
    public Optional<Donation> findOneByPaymentSlug(String paymentSlug) {
        log.debug("Request to find one Donation by Payment Slug : {}", paymentSlug);
        Donation donation = new Donation();
        donation.setPaymentSlug(paymentSlug);
        return donationService.findOne(Example.of(donation));
    }

    /**
     * Initiate transaction
     * 
     * @param payment the payment DTO
     * @return Transaction the transaction entity
     */
    @Override
    public Transaction payment(PaymentDTO payment) {
        log.debug("Request to payment : {}", payment);
        MibResponseDTO mibResponseDTO = new MibResponseDTO();
        Transaction transaction = payment.getTransaction();
        Optional<Donation> don = donationService.findOne(payment.getDonation().getId());
        if (!don.isPresent()) {
            mibResponseDTO.setResult(TransactionStatus.FAILED.toString());
            mibResponseDTO.setResponseCode("DNF");
            mibResponseDTO.setMessage("Donation Not Found");
        } else {
            transaction.setDonation(don.get());
            MibRequestDTO mibRequestDTO = mibMapper.toMibRequestDTO(payment, don.get().getOrganizer());
            String url = applicationProperties.getMibUrl();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(
                    mibMapper.mibRequestToMultiValueMap(mibRequestDTO), headers);
            ResponseEntity<MibResponseDTO> response = this.restTemplate.postForEntity(url, entity,
                    MibResponseDTO.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                mibResponseDTO = response.getBody();
            } else {
                mibResponseDTO = mibMapper.mibRequestToMibResponse(mibRequestDTO);
                mibResponseDTO.setResult(TransactionStatus.FAILED.toString());
                mibResponseDTO.setResponseCode(String.valueOf(response.getStatusCode().value()));
                mibResponseDTO.setMessage("HTTP Status Code not 200 from Server MIB");
            }
        }
        
        return transactionService.save(mibMapper.mibResponseToTransaction(mibResponseDTO, transaction));
    }

    /**
     * Simulator MIB
     * 
     * @param mibRequestDTO request like MIB
     * @return
     */
    @Override
    public MibResponseDTO simulatorMib(MibRequestDTO mibRequestDTO) {
        String result = "SUCCESS";
        String responseCode = "00";
        String message = "PAYMENT APPROVED";
        String paymentSystrace = String.format("%06d", ThreadLocalRandom.current().nextInt(1, 1000000));
        String approvalCode = String.format("%06d", ThreadLocalRandom.current().nextInt(1, 1000000));
        String paymentHostRefNumber = String.format("%06d", ThreadLocalRandom.current().nextInt(1, 1000000));

        // Scenario no parameter sent
        if (mibRequestDTO.getAMOUNT() == null || mibRequestDTO.getAUTH1() == null) {
            result = "FAILED";
            responseCode = "ER";
            message = "System Failure";
            paymentSystrace = "";
            approvalCode = "";
            paymentHostRefNumber = "";
        } else
        // Scenario Failed amount > 10000000
        if (Long.valueOf(mibRequestDTO.getAMOUNT())  > 10000000) {
            result = "FAILED";
            responseCode = "51";
            message = "Exceed Transaction Limit";
            approvalCode = "";
        } else
        // Scenario Timeout OVO ID = 080000000000
        if (mibRequestDTO.getAUTH1() == "080000000000") {
            result = "FAILED";
            responseCode = "TO";
            message = "Request TimeOut";
            approvalCode = "";
            paymentHostRefNumber = "";
        } else 
        // Scenario not getting response after 30s        
        if (mibRequestDTO.getAUTH1() == "089999999999") {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                log.error("Payment Simulator sleep interrupted", e);
            }
        }

        MibResponseDTO mibResponseDTO = mibMapper.mibRequestToMibResponse(mibRequestDTO);
        mibResponseDTO.setResult(result);
        mibResponseDTO.setResponseCode(responseCode);
        mibResponseDTO.setMessage(message);
        mibResponseDTO.setPaymentSysTrace(paymentSystrace);
        mibResponseDTO.setApprovalCode(approvalCode);
        mibResponseDTO.setPaymentHostRefNumber(paymentHostRefNumber);
        return mibResponseDTO;
    }

}
