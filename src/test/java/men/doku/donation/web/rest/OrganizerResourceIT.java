package men.doku.donation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import men.doku.donation.DonationApp;
import men.doku.donation.domain.Organizer;
import men.doku.donation.domain.enumeration.IsActiveStatus;
import men.doku.donation.repository.OrganizerRepository;
import men.doku.donation.service.OrganizerService;
/**
 * Integration tests for the {@link OrganizerResource} REST controller.
 */
@SpringBootTest(classes = DonationApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class OrganizerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_MDR = 100F;
    private static final Float UPDATED_MDR = 99F;

    private static final Float DEFAULT_SHARING = 100F;
    private static final Float UPDATED_SHARING = 99F;

    private static final String DEFAULT_LAST_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_UPDATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_MALL_ID = 1;
    private static final Integer UPDATED_MALL_ID = 2;

    private static final Integer DEFAULT_CHAIN_MALL_ID = 1;
    private static final Integer UPDATED_CHAIN_MALL_ID = 2;

    private static final String DEFAULT_SHARED_KEY = "AAAAAAAAAA";
    private static final String UPDATED_SHARED_KEY = "BBBBBBBBBB";

    private static final Integer DEFAULT_SERVICE_ID = 1;
    private static final Integer UPDATED_SERVICE_ID = 2;

    private static final Integer DEFAULT_ACQUIRER_ID = 1;
    private static final Integer UPDATED_ACQUIRER_ID = 2;

    private static final IsActiveStatus DEFAULT_STATUS = IsActiveStatus.ACTIVE;
    private static final IsActiveStatus UPDATED_STATUS = IsActiveStatus.DISABLED;

    @Autowired
    private OrganizerRepository organizerRepository;

    @Mock
    private OrganizerRepository organizerRepositoryMock;

    @Mock
    private OrganizerService organizerServiceMock;

    @Autowired
    private OrganizerService organizerService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizerMockMvc;

    private Organizer organizer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organizer createEntity(EntityManager em) {
        Organizer organizer = new Organizer()
            .name(DEFAULT_NAME)
            .url(DEFAULT_URL)
            .email(DEFAULT_EMAIL)
            .bankAccountNumber(DEFAULT_BANK_ACCOUNT_NUMBER)
            .bankAccountName(DEFAULT_BANK_ACCOUNT_NAME)
            .bankName(DEFAULT_BANK_NAME)
            .mdr(DEFAULT_MDR)
            .sharing(DEFAULT_SHARING)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedAt(DEFAULT_LAST_UPDATED_AT)
            .mallId(DEFAULT_MALL_ID)
            .chainMallId(DEFAULT_CHAIN_MALL_ID)
            .sharedKey(DEFAULT_SHARED_KEY)
            .serviceId(DEFAULT_SERVICE_ID)
            .acquirerId(DEFAULT_ACQUIRER_ID)
            .status(DEFAULT_STATUS);
        return organizer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organizer createUpdatedEntity(EntityManager em) {
        Organizer organizer = new Organizer()
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .email(UPDATED_EMAIL)
            .bankAccountNumber(UPDATED_BANK_ACCOUNT_NUMBER)
            .bankAccountName(UPDATED_BANK_ACCOUNT_NAME)
            .bankName(UPDATED_BANK_NAME)
            .mdr(UPDATED_MDR)
            .sharing(UPDATED_SHARING)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedAt(UPDATED_LAST_UPDATED_AT)
            .mallId(UPDATED_MALL_ID)
            .chainMallId(UPDATED_CHAIN_MALL_ID)
            .sharedKey(UPDATED_SHARED_KEY)
            .serviceId(UPDATED_SERVICE_ID)
            .acquirerId(UPDATED_ACQUIRER_ID)
            .status(UPDATED_STATUS);
        return organizer;
    }

    @BeforeEach
    public void initTest() {
        organizer = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganizer() throws Exception {
        int databaseSizeBeforeCreate = organizerRepository.findAll().size();

        // Create the Organizer
        restOrganizerMockMvc.perform(post("/api/organizers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizer)))
            .andExpect(status().isCreated());

        // Validate the Organizer in the database
        List<Organizer> organizerList = organizerRepository.findAll();
        assertThat(organizerList).hasSize(databaseSizeBeforeCreate + 1);
        Organizer testOrganizer = organizerList.get(organizerList.size() - 1);
        assertThat(testOrganizer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganizer.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testOrganizer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOrganizer.getBankAccountNumber()).isEqualTo(DEFAULT_BANK_ACCOUNT_NUMBER);
        assertThat(testOrganizer.getBankAccountName()).isEqualTo(DEFAULT_BANK_ACCOUNT_NAME);
        assertThat(testOrganizer.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
        assertThat(testOrganizer.getMdr()).isEqualTo(DEFAULT_MDR);
        assertThat(testOrganizer.getSharing()).isEqualTo(DEFAULT_SHARING);
        assertThat(testOrganizer.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testOrganizer.getLastUpdatedAt()).isEqualTo(DEFAULT_LAST_UPDATED_AT);
        assertThat(testOrganizer.getMallId()).isEqualTo(DEFAULT_MALL_ID);
        assertThat(testOrganizer.getChainMallId()).isEqualTo(DEFAULT_CHAIN_MALL_ID);
        assertThat(testOrganizer.getSharedKey()).isEqualTo(DEFAULT_SHARED_KEY);
        assertThat(testOrganizer.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
        assertThat(testOrganizer.getAcquirerId()).isEqualTo(DEFAULT_ACQUIRER_ID);
        assertThat(testOrganizer.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createOrganizerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organizerRepository.findAll().size();

        // Create the Organizer with an existing ID
        organizer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizerMockMvc.perform(post("/api/organizers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizer)))
            .andExpect(status().isBadRequest());

        // Validate the Organizer in the database
        List<Organizer> organizerList = organizerRepository.findAll();
        assertThat(organizerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizerRepository.findAll().size();
        // set the field null
        organizer.setName(null);

        // Create the Organizer, which fails.

        restOrganizerMockMvc.perform(post("/api/organizers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizer)))
            .andExpect(status().isBadRequest());

        List<Organizer> organizerList = organizerRepository.findAll();
        assertThat(organizerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizerRepository.findAll().size();
        // set the field null
        organizer.setEmail(null);

        // Create the Organizer, which fails.

        restOrganizerMockMvc.perform(post("/api/organizers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizer)))
            .andExpect(status().isBadRequest());

        List<Organizer> organizerList = organizerRepository.findAll();
        assertThat(organizerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrganizers() throws Exception {
        // Initialize the database
        organizerRepository.saveAndFlush(organizer);

        // Get all the organizerList
        restOrganizerMockMvc.perform(get("/api/organizers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].bankAccountNumber").value(hasItem(DEFAULT_BANK_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankAccountName").value(hasItem(DEFAULT_BANK_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].mdr").value(hasItem(DEFAULT_MDR.doubleValue())))
            .andExpect(jsonPath("$.[*].sharing").value(hasItem(DEFAULT_SHARING.doubleValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].lastUpdatedAt").value(hasItem(DEFAULT_LAST_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].mallId").value(hasItem(DEFAULT_MALL_ID)))
            .andExpect(jsonPath("$.[*].chainMallId").value(hasItem(DEFAULT_CHAIN_MALL_ID)))
            .andExpect(jsonPath("$.[*].sharedKey").value(hasItem(DEFAULT_SHARED_KEY)))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID)))
            .andExpect(jsonPath("$.[*].acquirerId").value(hasItem(DEFAULT_ACQUIRER_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    public void getAllOrganizersWithEagerRelationshipsIsEnabled() throws Exception {
        when(organizerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        restOrganizerMockMvc.perform(get("/api/organizers?eagerload=true"))
            .andExpect(status().isOk());

        verify(organizerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllOrganizersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(organizerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        restOrganizerMockMvc.perform(get("/api/organizers?eagerload=true"))
            .andExpect(status().isOk());

        verify(organizerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getOrganizer() throws Exception {
        // Initialize the database
        organizerRepository.saveAndFlush(organizer);

        // Get the organizer
        restOrganizerMockMvc.perform(get("/api/organizers/{id}", organizer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organizer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.bankAccountNumber").value(DEFAULT_BANK_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.bankAccountName").value(DEFAULT_BANK_ACCOUNT_NAME))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME))
            .andExpect(jsonPath("$.mdr").value(DEFAULT_MDR.doubleValue()))
            .andExpect(jsonPath("$.sharing").value(DEFAULT_SHARING.doubleValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY))
            .andExpect(jsonPath("$.lastUpdatedAt").value(DEFAULT_LAST_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.mallId").value(DEFAULT_MALL_ID))
            .andExpect(jsonPath("$.chainMallId").value(DEFAULT_CHAIN_MALL_ID))
            .andExpect(jsonPath("$.sharedKey").value(DEFAULT_SHARED_KEY))
            .andExpect(jsonPath("$.serviceId").value(DEFAULT_SERVICE_ID))
            .andExpect(jsonPath("$.acquirerId").value(DEFAULT_ACQUIRER_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrganizer() throws Exception {
        // Get the organizer
        restOrganizerMockMvc.perform(get("/api/organizers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganizer() throws Exception {
        // Initialize the database
        organizerService.save(organizer);

        int databaseSizeBeforeUpdate = organizerRepository.findAll().size();

        // Update the organizer
        Organizer updatedOrganizer = organizerRepository.findById(organizer.getId()).get();
        // Disconnect from session so that the updates on updatedOrganizer are not directly saved in db
        em.detach(updatedOrganizer);
        updatedOrganizer
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .email(UPDATED_EMAIL)
            .bankAccountNumber(UPDATED_BANK_ACCOUNT_NUMBER)
            .bankAccountName(UPDATED_BANK_ACCOUNT_NAME)
            .bankName(UPDATED_BANK_NAME)
            .mdr(UPDATED_MDR)
            .sharing(UPDATED_SHARING)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedAt(UPDATED_LAST_UPDATED_AT)
            .mallId(UPDATED_MALL_ID)
            .chainMallId(UPDATED_CHAIN_MALL_ID)
            .sharedKey(UPDATED_SHARED_KEY)
            .serviceId(UPDATED_SERVICE_ID)
            .acquirerId(UPDATED_ACQUIRER_ID)
            .status(UPDATED_STATUS);

        restOrganizerMockMvc.perform(put("/api/organizers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrganizer)))
            .andExpect(status().isOk());

        // Validate the Organizer in the database
        List<Organizer> organizerList = organizerRepository.findAll();
        assertThat(organizerList).hasSize(databaseSizeBeforeUpdate);
        Organizer testOrganizer = organizerList.get(organizerList.size() - 1);
        assertThat(testOrganizer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganizer.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testOrganizer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrganizer.getBankAccountNumber()).isEqualTo(UPDATED_BANK_ACCOUNT_NUMBER);
        assertThat(testOrganizer.getBankAccountName()).isEqualTo(UPDATED_BANK_ACCOUNT_NAME);
        assertThat(testOrganizer.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testOrganizer.getMdr()).isEqualTo(UPDATED_MDR);
        assertThat(testOrganizer.getSharing()).isEqualTo(UPDATED_SHARING);
        assertThat(testOrganizer.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testOrganizer.getLastUpdatedAt()).isEqualTo(UPDATED_LAST_UPDATED_AT);
        assertThat(testOrganizer.getMallId()).isEqualTo(UPDATED_MALL_ID);
        assertThat(testOrganizer.getChainMallId()).isEqualTo(UPDATED_CHAIN_MALL_ID);
        assertThat(testOrganizer.getSharedKey()).isEqualTo(UPDATED_SHARED_KEY);
        assertThat(testOrganizer.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testOrganizer.getAcquirerId()).isEqualTo(UPDATED_ACQUIRER_ID);
        assertThat(testOrganizer.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganizer() throws Exception {
        int databaseSizeBeforeUpdate = organizerRepository.findAll().size();

        // Create the Organizer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizerMockMvc.perform(put("/api/organizers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizer)))
            .andExpect(status().isBadRequest());

        // Validate the Organizer in the database
        List<Organizer> organizerList = organizerRepository.findAll();
        assertThat(organizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrganizer() throws Exception {
        // Initialize the database
        organizerService.save(organizer);

        int databaseSizeBeforeDelete = organizerRepository.findAll().size();

        // Delete the organizer
        restOrganizerMockMvc.perform(delete("/api/organizers/{id}", organizer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Organizer> organizerList = organizerRepository.findAll();
        assertThat(organizerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
