package men.doku.donation.service.impl;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import men.doku.donation.domain.Donation;
import men.doku.donation.domain.Transaction;
import men.doku.donation.domain.enumeration.TransactionStatus;
import men.doku.donation.service.DonationService;
import men.doku.donation.service.PaymentService;
import men.doku.donation.service.TransactionService;
import men.doku.donation.service.dto.MibRequestDTO;
import men.doku.donation.service.dto.MibResponseDTO;
import men.doku.donation.service.dto.PaymentDTO;

/**
 * Service Implementation for managing {@link Transaction}.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final DonationService donationService;
    private final TransactionService transactionService;

    public PaymentServiceImpl(DonationService donationService, TransactionService transactionService) {
        this.donationService = donationService;
        this.transactionService = transactionService;
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
        Transaction transaction = payment.getTransaction();
        // Process to MIB here
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {}
        transaction.setOvoIdMasked("ovoIdMasked");
        TransactionStatus status = TransactionStatus.SUCCESS;
        transaction.setStatus(status);
        return transactionService.save(transaction);
    }

    /**
     * Simulator MIB
     * 
     * @param mibRequestDTO request like MIB
     * @return
     */
    @Override
    public MibResponseDTO simulatorMib(MibRequestDTO mibRequestDTO) {
        String paymentDate = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(Instant.now());
        String result = "SUCCESS";
        String responseCode = "00";
        String message = "PAYMENT APPROVED";
        String paymentSystrace = String.format("%06d", ThreadLocalRandom.current().nextInt(1, 1000000));
        String approvalCode = String.format("%06d", ThreadLocalRandom.current().nextInt(1, 1000000));
        String paymentHostRefNumber = String.format("%06d", ThreadLocalRandom.current().nextInt(1, 1000000));

        // Scenario Failed amount != 10000 
        if(mibRequestDTO.getAMOUNT() != 10000) {
            result = "FAILED";
            responseCode = "51";
            message = "Exceed Transaction Limit";
            approvalCode = "";
        } else 
        // Scenario Timeout MALLID != 1 || SERVICEID != 1 || ACQUIRERID != 1
        if (mibRequestDTO.getMALLID() != 1 || mibRequestDTO.getSERVICEID() != 1 || 
                    mibRequestDTO.getACQUIRERID() != 1) {
            result = "FAILED";
            responseCode = "TO";
            message = "Request TimeOut";
            approvalCode = "";
            paymentHostRefNumber = "";
        }

        MibResponseDTO mibResponseDTO = new MibResponseDTO(mibRequestDTO.getMALLID(),
            UUID.randomUUID().toString().replaceAll("-", ""),
            mibRequestDTO.getAUTH1(),
            mibRequestDTO.getINVOICENUMBER(),
            mibRequestDTO.getAMOUNT(),
            mibRequestDTO.getCURRENCY(),
            mibRequestDTO.getSESSIONID(),
            Long.valueOf(paymentDate),
            result,
            responseCode,
            message,
            paymentSystrace,
            approvalCode,
            paymentHostRefNumber
        );
        return mibResponseDTO;
    }

}
