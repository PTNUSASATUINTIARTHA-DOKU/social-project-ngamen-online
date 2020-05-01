package men.doku.donation.service.impl;

import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import men.doku.donation.domain.Transaction;
import men.doku.donation.service.PaymentService;
import men.doku.donation.service.dto.MibRequestDTO;
import men.doku.donation.service.dto.MibResponseDTO;
import men.doku.donation.service.mapper.MibMapper;

/**
 * Service Implementation for managing {@link Transaction}.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final MibMapper mibMapper;

    public PaymentServiceImpl(MibMapper mibMapper) {
        this.mibMapper = mibMapper;
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
        if (mibRequestDTO.getAUTH1().equals("080000000000")) {
            result = "FAILED";
            responseCode = "TO";
            message = "Request TimeOut";
            approvalCode = "";
            paymentHostRefNumber = "";
        } else 
        // Scenario not getting response after 40s        
        if (mibRequestDTO.getAUTH1().equals("089999999999")) {
            try {
                Thread.sleep(40000);
            } catch (InterruptedException e) {
                log.error("Payment Simulator sleep interrupted", e);
            }
        } else 
        // Scenario not getting response after 70s
        if (mibRequestDTO.getAUTH1().equals("081111111111")) {
            try {
                Thread.sleep(40000);
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
