package men.doku.donation.web.rest;

import men.doku.donation.DonationApp;
import men.doku.donation.domain.Donation;
import men.doku.donation.repository.DonationRepository;
import men.doku.donation.service.DonationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import men.doku.donation.domain.enumeration.IsActiveStatus;
/**
 * Integration tests for the {@link DonationResource} REST controller.
 */
@SpringBootTest(classes = DonationApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class DonationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_URL = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_UPDATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final IsActiveStatus DEFAULT_STATUS = IsActiveStatus.ACTIVE;
    private static final IsActiveStatus UPDATED_STATUS = IsActiveStatus.DISABLED;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonationService donationService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDonationMockMvc;

    private Donation donation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Donation createEntity(EntityManager em) {
        Donation donation = new Donation()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .url(DEFAULT_URL)
            .imageUrl(DEFAULT_IMAGE_URL)
            .paymentUrl(DEFAULT_PAYMENT_URL)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedAt(DEFAULT_LAST_UPDATED_AT)
            .status(DEFAULT_STATUS);
        return donation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Donation createUpdatedEntity(EntityManager em) {
        Donation donation = new Donation()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .imageUrl(UPDATED_IMAGE_URL)
            .paymentUrl(UPDATED_PAYMENT_URL)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedAt(UPDATED_LAST_UPDATED_AT)
            .status(UPDATED_STATUS);
        return donation;
    }

    @BeforeEach
    public void initTest() {
        donation = createEntity(em);
    }

    @Test
    @Transactional
    public void createDonation() throws Exception {
        int databaseSizeBeforeCreate = donationRepository.findAll().size();

        // Create the Donation
        restDonationMockMvc.perform(post("/api/donations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(donation)))
            .andExpect(status().isCreated());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeCreate + 1);
        Donation testDonation = donationList.get(donationList.size() - 1);
        assertThat(testDonation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDonation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDonation.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testDonation.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testDonation.getPaymentUrl()).isEqualTo(DEFAULT_PAYMENT_URL);
        assertThat(testDonation.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testDonation.getLastUpdatedAt()).isEqualTo(DEFAULT_LAST_UPDATED_AT);
        assertThat(testDonation.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createDonationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = donationRepository.findAll().size();

        // Create the Donation with an existing ID
        donation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDonationMockMvc.perform(post("/api/donations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(donation)))
            .andExpect(status().isBadRequest());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = donationRepository.findAll().size();
        // set the field null
        donation.setName(null);

        // Create the Donation, which fails.

        restDonationMockMvc.perform(post("/api/donations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(donation)))
            .andExpect(status().isBadRequest());

        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = donationRepository.findAll().size();
        // set the field null
        donation.setPaymentUrl(null);

        // Create the Donation, which fails.

        restDonationMockMvc.perform(post("/api/donations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(donation)))
            .andExpect(status().isBadRequest());

        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastUpdatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = donationRepository.findAll().size();
        // set the field null
        donation.setLastUpdatedBy(null);

        // Create the Donation, which fails.

        restDonationMockMvc.perform(post("/api/donations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(donation)))
            .andExpect(status().isBadRequest());

        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = donationRepository.findAll().size();
        // set the field null
        donation.setLastUpdatedAt(null);

        // Create the Donation, which fails.

        restDonationMockMvc.perform(post("/api/donations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(donation)))
            .andExpect(status().isBadRequest());

        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = donationRepository.findAll().size();
        // set the field null
        donation.setStatus(null);

        // Create the Donation, which fails.

        restDonationMockMvc.perform(post("/api/donations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(donation)))
            .andExpect(status().isBadRequest());

        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDonations() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get all the donationList
        restDonationMockMvc.perform(get("/api/donations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].paymentUrl").value(hasItem(DEFAULT_PAYMENT_URL)))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].lastUpdatedAt").value(hasItem(DEFAULT_LAST_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getDonation() throws Exception {
        // Initialize the database
        donationRepository.saveAndFlush(donation);

        // Get the donation
        restDonationMockMvc.perform(get("/api/donations/{id}", donation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(donation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.paymentUrl").value(DEFAULT_PAYMENT_URL))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY))
            .andExpect(jsonPath("$.lastUpdatedAt").value(DEFAULT_LAST_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDonation() throws Exception {
        // Get the donation
        restDonationMockMvc.perform(get("/api/donations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDonation() throws Exception {
        // Initialize the database
        donationService.save(donation);

        int databaseSizeBeforeUpdate = donationRepository.findAll().size();

        // Update the donation
        Donation updatedDonation = donationRepository.findById(donation.getId()).get();
        // Disconnect from session so that the updates on updatedDonation are not directly saved in db
        em.detach(updatedDonation);
        updatedDonation
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .imageUrl(UPDATED_IMAGE_URL)
            .paymentUrl(UPDATED_PAYMENT_URL)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedAt(UPDATED_LAST_UPDATED_AT)
            .status(UPDATED_STATUS);

        restDonationMockMvc.perform(put("/api/donations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDonation)))
            .andExpect(status().isOk());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
        Donation testDonation = donationList.get(donationList.size() - 1);
        assertThat(testDonation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDonation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDonation.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testDonation.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testDonation.getPaymentUrl()).isEqualTo(UPDATED_PAYMENT_URL);
        assertThat(testDonation.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testDonation.getLastUpdatedAt()).isEqualTo(UPDATED_LAST_UPDATED_AT);
        assertThat(testDonation.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingDonation() throws Exception {
        int databaseSizeBeforeUpdate = donationRepository.findAll().size();

        // Create the Donation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonationMockMvc.perform(put("/api/donations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(donation)))
            .andExpect(status().isBadRequest());

        // Validate the Donation in the database
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDonation() throws Exception {
        // Initialize the database
        donationService.save(donation);

        int databaseSizeBeforeDelete = donationRepository.findAll().size();

        // Delete the donation
        restDonationMockMvc.perform(delete("/api/donations/{id}", donation.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Donation> donationList = donationRepository.findAll();
        assertThat(donationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
