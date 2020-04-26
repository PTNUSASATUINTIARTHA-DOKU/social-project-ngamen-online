package men.doku.donation.service;

import java.util.Optional;

import men.doku.donation.domain.Donation;
import men.doku.donation.domain.Transaction;
import men.doku.donation.service.dto.MibRequestDTO;
import men.doku.donation.service.dto.MibResponseDTO;
import men.doku.donation.service.dto.PaymentDTO;

/**
 * Service Interface for managing {@link Transaction}.
 */
public interface PaymentService {


    /**
     * Get one donation by slug.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Donation> findOneByPaymentSlug(String paymentSlug);

    /**
     * Initiate transaction
     * 
     * @param payment the payment DTO
     * @return Transaction the transaction entity
     */
    Transaction payment(PaymentDTO payment);

    /**
     * Simulator MIB
     * 
     * @param mibRequestDTO request like MIB
     * @return
     */
    MibResponseDTO simulatorMib(MibRequestDTO mibRequestDTO);
    
}
