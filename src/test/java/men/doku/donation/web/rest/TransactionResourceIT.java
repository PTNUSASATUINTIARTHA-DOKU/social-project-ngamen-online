package men.doku.donation.web.rest;

import men.doku.donation.DonationApp;
import men.doku.donation.domain.Transaction;
import men.doku.donation.repository.TransactionRepository;
import men.doku.donation.service.TransactionService;

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

import men.doku.donation.domain.enumeration.PaymentChannel;
import men.doku.donation.domain.enumeration.TransactionStatus;
/**
 * Integration tests for the {@link TransactionResource} REST controller.
 */
@SpringBootTest(classes = DonationApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class TransactionResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SESSION_ID = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_BASKET = "AAAAAAAAAA";
    private static final String UPDATED_BASKET = "BBBBBBBBBB";

    private static final String DEFAULT_OVO_ID_MASKED = "AAAAAAAAAA";
    private static final String UPDATED_OVO_ID_MASKED = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_INFORMATION = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_INFORMATION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_AMOUNT = 10000L;
    private static final Long UPDATED_AMOUNT = 10001L;

    private static final PaymentChannel DEFAULT_PAYMENT_CHANNEL = PaymentChannel.OVO;
    private static final PaymentChannel UPDATED_PAYMENT_CHANNEL = PaymentChannel.OVO;

    private static final Integer DEFAULT_MALL_ID = 1;
    private static final Integer UPDATED_MALL_ID = 2;

    private static final String DEFAULT_TRX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TRX_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_RESPONSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_SYSTRACE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_SYSTRACE = "BBBBBBBBBB";

    private static final String DEFAULT_APPROVAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_APPROVAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_HOST_REF_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_HOST_REF_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CAPTCHA_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_CAPTCHA_TOKEN = "BBBBBBBBBB";

    private static final Float DEFAULT_CAPTCHA_SCORE = 1F;
    private static final Float UPDATED_CAPTCHA_SCORE = 2F;

    private static final String DEFAULT_LAST_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_UPDATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final TransactionStatus DEFAULT_STATUS = TransactionStatus.INITIATE;
    private static final TransactionStatus UPDATED_STATUS = TransactionStatus.PROCESS;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionMockMvc;

    private Transaction transaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .sessionId(DEFAULT_SESSION_ID)
            .basket(DEFAULT_BASKET)
            .ovoIdMasked(DEFAULT_OVO_ID_MASKED)
            .deviceInformation(DEFAULT_DEVICE_INFORMATION)
            .name(DEFAULT_NAME)
            .mobile(DEFAULT_MOBILE)
            .email(DEFAULT_EMAIL)
            .amount(DEFAULT_AMOUNT)
            .paymentChannel(DEFAULT_PAYMENT_CHANNEL)
            .mallId(DEFAULT_MALL_ID)
            .trxCode(DEFAULT_TRX_CODE)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .responseCode(DEFAULT_RESPONSE_CODE)
            .message(DEFAULT_MESSAGE)
            .paymentSystrace(DEFAULT_PAYMENT_SYSTRACE)
            .approvalCode(DEFAULT_APPROVAL_CODE)
            .paymentHostRefNumber(DEFAULT_PAYMENT_HOST_REF_NUMBER)
            .captchaToken(DEFAULT_CAPTCHA_TOKEN)
            .captchaScore(DEFAULT_CAPTCHA_SCORE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedAt(DEFAULT_LAST_UPDATED_AT)
            .status(DEFAULT_STATUS);
        return transaction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createUpdatedEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .sessionId(UPDATED_SESSION_ID)
            .basket(UPDATED_BASKET)
            .ovoIdMasked(UPDATED_OVO_ID_MASKED)
            .deviceInformation(UPDATED_DEVICE_INFORMATION)
            .name(UPDATED_NAME)
            .mobile(UPDATED_MOBILE)
            .email(UPDATED_EMAIL)
            .amount(UPDATED_AMOUNT)
            .paymentChannel(UPDATED_PAYMENT_CHANNEL)
            .mallId(UPDATED_MALL_ID)
            .trxCode(UPDATED_TRX_CODE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .responseCode(UPDATED_RESPONSE_CODE)
            .message(UPDATED_MESSAGE)
            .paymentSystrace(UPDATED_PAYMENT_SYSTRACE)
            .approvalCode(UPDATED_APPROVAL_CODE)
            .paymentHostRefNumber(UPDATED_PAYMENT_HOST_REF_NUMBER)
            .captchaToken(UPDATED_CAPTCHA_TOKEN)
            .captchaScore(UPDATED_CAPTCHA_SCORE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedAt(UPDATED_LAST_UPDATED_AT)
            .status(UPDATED_STATUS);
        return transaction;
    }

    @BeforeEach
    public void initTest() {
        transaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransaction() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // Create the Transaction
        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isCreated());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate + 1);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testTransaction.getSessionId()).isEqualTo(DEFAULT_SESSION_ID);
        assertThat(testTransaction.getBasket()).isEqualTo(DEFAULT_BASKET);
        assertThat(testTransaction.getOvoIdMasked()).isEqualTo(DEFAULT_OVO_ID_MASKED);
        assertThat(testTransaction.getDeviceInformation()).isEqualTo(DEFAULT_DEVICE_INFORMATION);
        assertThat(testTransaction.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTransaction.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testTransaction.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testTransaction.getPaymentChannel()).isEqualTo(DEFAULT_PAYMENT_CHANNEL);
        assertThat(testTransaction.getMallId()).isEqualTo(DEFAULT_MALL_ID);
        assertThat(testTransaction.getTrxCode()).isEqualTo(DEFAULT_TRX_CODE);
        assertThat(testTransaction.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testTransaction.getResponseCode()).isEqualTo(DEFAULT_RESPONSE_CODE);
        assertThat(testTransaction.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testTransaction.getPaymentSystrace()).isEqualTo(DEFAULT_PAYMENT_SYSTRACE);
        assertThat(testTransaction.getApprovalCode()).isEqualTo(DEFAULT_APPROVAL_CODE);
        assertThat(testTransaction.getPaymentHostRefNumber()).isEqualTo(DEFAULT_PAYMENT_HOST_REF_NUMBER);
        assertThat(testTransaction.getCaptchaToken()).isEqualTo(DEFAULT_CAPTCHA_TOKEN);
        assertThat(testTransaction.getCaptchaScore()).isEqualTo(DEFAULT_CAPTCHA_SCORE);
        assertThat(testTransaction.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testTransaction.getLastUpdatedAt()).isEqualTo(DEFAULT_LAST_UPDATED_AT);
        assertThat(testTransaction.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // Create the Transaction with an existing ID
        transaction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkInvoiceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setInvoiceNumber(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransactions() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList
        restTransactionMockMvc.perform(get("/api/transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].sessionId").value(hasItem(DEFAULT_SESSION_ID)))
            .andExpect(jsonPath("$.[*].basket").value(hasItem(DEFAULT_BASKET)))
            .andExpect(jsonPath("$.[*].ovoIdMasked").value(hasItem(DEFAULT_OVO_ID_MASKED)))
            .andExpect(jsonPath("$.[*].deviceInformation").value(hasItem(DEFAULT_DEVICE_INFORMATION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].paymentChannel").value(hasItem(DEFAULT_PAYMENT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].mallId").value(hasItem(DEFAULT_MALL_ID)))
            .andExpect(jsonPath("$.[*].trxCode").value(hasItem(DEFAULT_TRX_CODE)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].responseCode").value(hasItem(DEFAULT_RESPONSE_CODE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].paymentSystrace").value(hasItem(DEFAULT_PAYMENT_SYSTRACE)))
            .andExpect(jsonPath("$.[*].approvalCode").value(hasItem(DEFAULT_APPROVAL_CODE)))
            .andExpect(jsonPath("$.[*].paymentHostRefNumber").value(hasItem(DEFAULT_PAYMENT_HOST_REF_NUMBER)))
            .andExpect(jsonPath("$.[*].captchaToken").value(hasItem(DEFAULT_CAPTCHA_TOKEN)))
            .andExpect(jsonPath("$.[*].captchaScore").value(hasItem(DEFAULT_CAPTCHA_SCORE.doubleValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].lastUpdatedAt").value(hasItem(DEFAULT_LAST_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get the transaction
        restTransactionMockMvc.perform(get("/api/transactions/{id}", transaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transaction.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.sessionId").value(DEFAULT_SESSION_ID))
            .andExpect(jsonPath("$.basket").value(DEFAULT_BASKET))
            .andExpect(jsonPath("$.ovoIdMasked").value(DEFAULT_OVO_ID_MASKED))
            .andExpect(jsonPath("$.deviceInformation").value(DEFAULT_DEVICE_INFORMATION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.paymentChannel").value(DEFAULT_PAYMENT_CHANNEL.toString()))
            .andExpect(jsonPath("$.mallId").value(DEFAULT_MALL_ID))
            .andExpect(jsonPath("$.trxCode").value(DEFAULT_TRX_CODE))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.responseCode").value(DEFAULT_RESPONSE_CODE))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.paymentSystrace").value(DEFAULT_PAYMENT_SYSTRACE))
            .andExpect(jsonPath("$.approvalCode").value(DEFAULT_APPROVAL_CODE))
            .andExpect(jsonPath("$.paymentHostRefNumber").value(DEFAULT_PAYMENT_HOST_REF_NUMBER))
            .andExpect(jsonPath("$.captchaToken").value(DEFAULT_CAPTCHA_TOKEN))
            .andExpect(jsonPath("$.captchaScore").value(DEFAULT_CAPTCHA_SCORE.doubleValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY))
            .andExpect(jsonPath("$.lastUpdatedAt").value(DEFAULT_LAST_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTransaction() throws Exception {
        // Get the transaction
        restTransactionMockMvc.perform(get("/api/transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransaction() throws Exception {
        // Initialize the database
        transactionService.save(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId()).get();
        // Disconnect from session so that the updates on updatedTransaction are not directly saved in db
        em.detach(updatedTransaction);
        updatedTransaction
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .sessionId(UPDATED_SESSION_ID)
            .basket(UPDATED_BASKET)
            .ovoIdMasked(UPDATED_OVO_ID_MASKED)
            .deviceInformation(UPDATED_DEVICE_INFORMATION)
            .name(UPDATED_NAME)
            .mobile(UPDATED_MOBILE)
            .email(UPDATED_EMAIL)
            .amount(UPDATED_AMOUNT)
            .paymentChannel(UPDATED_PAYMENT_CHANNEL)
            .mallId(UPDATED_MALL_ID)
            .trxCode(UPDATED_TRX_CODE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .responseCode(UPDATED_RESPONSE_CODE)
            .message(UPDATED_MESSAGE)
            .paymentSystrace(UPDATED_PAYMENT_SYSTRACE)
            .approvalCode(UPDATED_APPROVAL_CODE)
            .paymentHostRefNumber(UPDATED_PAYMENT_HOST_REF_NUMBER)
            .captchaToken(UPDATED_CAPTCHA_TOKEN)
            .captchaScore(UPDATED_CAPTCHA_SCORE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedAt(UPDATED_LAST_UPDATED_AT)
            .status(UPDATED_STATUS);

        restTransactionMockMvc.perform(put("/api/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTransaction)))
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testTransaction.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testTransaction.getBasket()).isEqualTo(UPDATED_BASKET);
        assertThat(testTransaction.getOvoIdMasked()).isEqualTo(UPDATED_OVO_ID_MASKED);
        assertThat(testTransaction.getDeviceInformation()).isEqualTo(UPDATED_DEVICE_INFORMATION);
        assertThat(testTransaction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransaction.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testTransaction.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTransaction.getPaymentChannel()).isEqualTo(UPDATED_PAYMENT_CHANNEL);
        assertThat(testTransaction.getMallId()).isEqualTo(UPDATED_MALL_ID);
        assertThat(testTransaction.getTrxCode()).isEqualTo(UPDATED_TRX_CODE);
        assertThat(testTransaction.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testTransaction.getResponseCode()).isEqualTo(UPDATED_RESPONSE_CODE);
        assertThat(testTransaction.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testTransaction.getPaymentSystrace()).isEqualTo(UPDATED_PAYMENT_SYSTRACE);
        assertThat(testTransaction.getApprovalCode()).isEqualTo(UPDATED_APPROVAL_CODE);
        assertThat(testTransaction.getPaymentHostRefNumber()).isEqualTo(UPDATED_PAYMENT_HOST_REF_NUMBER);
        assertThat(testTransaction.getCaptchaToken()).isEqualTo(UPDATED_CAPTCHA_TOKEN);
        assertThat(testTransaction.getCaptchaScore()).isEqualTo(UPDATED_CAPTCHA_SCORE);
        assertThat(testTransaction.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testTransaction.getLastUpdatedAt()).isEqualTo(UPDATED_LAST_UPDATED_AT);
        assertThat(testTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Create the Transaction

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc.perform(put("/api/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTransaction() throws Exception {
        // Initialize the database
        transactionService.save(transaction);

        int databaseSizeBeforeDelete = transactionRepository.findAll().size();

        // Delete the transaction
        restTransactionMockMvc.perform(delete("/api/transactions/{id}", transaction.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
