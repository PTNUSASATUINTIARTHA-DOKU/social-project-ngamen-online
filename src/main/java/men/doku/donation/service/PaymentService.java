package men.doku.donation.service;

import men.doku.donation.domain.Transaction;
import men.doku.donation.service.dto.MibRequestDTO;
import men.doku.donation.service.dto.MibResponseDTO;

/**
 * Service Interface for managing {@link Transaction}.
 */
public interface PaymentService {

    /**
     * Simulator MIB
     * 
     * @param mibRequestDTO request like MIB
     * @return
     */
    MibResponseDTO simulatorMib(MibRequestDTO mibRequestDTO);
    
}
