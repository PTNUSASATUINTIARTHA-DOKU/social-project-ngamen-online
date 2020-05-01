package men.doku.donation.service.impl;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import men.doku.donation.config.ApplicationProperties;
import men.doku.donation.config.Constants;
import men.doku.donation.domain.Donation;
import men.doku.donation.domain.Transaction;
import men.doku.donation.domain.enumeration.TransactionStatus;
import men.doku.donation.repository.TransactionRepository;
import men.doku.donation.security.SecurityUtils;
import men.doku.donation.service.DonationService;
import men.doku.donation.service.OrganizerService;
import men.doku.donation.service.TransactionService;
import men.doku.donation.service.dto.MibRequestDTO;
import men.doku.donation.service.dto.MibResponseDTO;
import men.doku.donation.service.mapper.MibMapper;

/**
 * Service Implementation for managing {@link Transaction}.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final OrganizerService organizerService;
    private final DonationService donationService;
    private final MibMapper mibMapper;
    private final ApplicationProperties applicationProperties;
    private final RestTemplate restTemplate;

    public TransactionServiceImpl(
        TransactionRepository transactionRepository,
        OrganizerService organizerService,
        DonationService donationService,
        MibMapper mibMapper,
        ApplicationProperties applicationProperties,
        RestTemplateBuilder restTemplateBuilder
        ) {
        this.transactionRepository = transactionRepository;
        this.organizerService = organizerService;
        this.donationService = donationService;
        this.mibMapper = mibMapper;
        this.applicationProperties = applicationProperties;
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Save a transaction.
     *
     * @param transaction the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Transaction save(Transaction transaction) {
        log.debug("Request by {} to save Transaction : {}", SecurityUtils.getCurrentUserLogin().get(), transaction);
        transaction.setLastUpdatedAt(Instant.now());
        transaction.setLastUpdatedBy(SecurityUtils.getCurrentUserLogin().map(usr -> usr).orElse("SYSTEM"));
        return transactionRepository.save(transaction);
    }

    /**
     * Get all the transactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> findAll(Pageable pageable) {
        final String login = SecurityUtils.getCurrentUserLogin().get();
        log.debug("Request by {} to get all Transactions", login);
        if(SecurityUtils.isCurrentUserInRole(Constants.ADMIN)) {
            return transactionRepository.findAll(pageable);
        } else {
            List<Long> organizerIds = organizerService.findAllIdsOwnedWithEagerRealtionships(login);
            return transactionRepository.findAllByOrganizerIds(organizerIds, pageable);
        }
    }

    /**
     * Get one transaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Transaction> findOne(Long id) {
        log.debug("Request by {} to get Transaction : {}", SecurityUtils.getCurrentUserLogin().get(), id);
        return transactionRepository.findById(id);
    }

    /**
     * Delete the transaction by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.warn("Request by {} to delete Transaction {} Forbidden", SecurityUtils.getCurrentUserLogin().get(), id);
    }
 
    /**
     * Initiate transaction
     * 
     * @param transaction the transaction entity
     * @return Transaction the transaction entity
     */
    @Override
    public Transaction pay(Transaction transaction) {
        log.debug("Request to initiatePayment : {}", transaction);
        MibResponseDTO mibResponseDTO = new MibResponseDTO();
        Optional<Donation> don = donationService.findOne(transaction.getDonation().getId());
        if (!don.isPresent()) {
            mibResponseDTO.setResult(TransactionStatus.FAILED.toString());
            mibResponseDTO.setResponseCode("DNF");
            mibResponseDTO.setMessage("Donation Not Found");
        } else {
            transaction.setDonation(don.get());
            MibRequestDTO mibRequestDTO = mibMapper.toMibRequestDTO(transaction, don.get().getOrganizer());
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
        return save(mibMapper.mibResponseToTransaction(mibResponseDTO, transaction));
    }    
}
