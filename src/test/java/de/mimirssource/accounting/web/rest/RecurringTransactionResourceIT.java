package de.mimirssource.accounting.web.rest;

import de.mimirssource.accounting.AccountingApp;
import de.mimirssource.accounting.domain.RecurringTransaction;
import de.mimirssource.accounting.repository.RecurringTransactionRepository;
import de.mimirssource.accounting.service.RecurringTransactionService;
import de.mimirssource.accounting.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static de.mimirssource.accounting.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RecurringTransactionResource} REST controller.
 */
@SpringBootTest(classes = AccountingApp.class)
public class RecurringTransactionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    private static final Integer DEFAULT_DATE_OF_MONTH = 1;
    private static final Integer UPDATED_DATE_OF_MONTH = 2;

    @Autowired
    private RecurringTransactionRepository recurringTransactionRepository;

    @Autowired
    private RecurringTransactionService recurringTransactionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restRecurringTransactionMockMvc;

    private RecurringTransaction recurringTransaction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecurringTransactionResource recurringTransactionResource = new RecurringTransactionResource(recurringTransactionService);
        this.restRecurringTransactionMockMvc = MockMvcBuilders.standaloneSetup(recurringTransactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecurringTransaction createEntity(EntityManager em) {
        RecurringTransaction recurringTransaction = new RecurringTransaction()
            .name(DEFAULT_NAME)
            .amount(DEFAULT_AMOUNT)
            .dateOfMonth(DEFAULT_DATE_OF_MONTH);
        return recurringTransaction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecurringTransaction createUpdatedEntity(EntityManager em) {
        RecurringTransaction recurringTransaction = new RecurringTransaction()
            .name(UPDATED_NAME)
            .amount(UPDATED_AMOUNT)
            .dateOfMonth(UPDATED_DATE_OF_MONTH);
        return recurringTransaction;
    }

    @BeforeEach
    public void initTest() {
        recurringTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecurringTransaction() throws Exception {
        int databaseSizeBeforeCreate = recurringTransactionRepository.findAll().size();

        // Create the RecurringTransaction
        restRecurringTransactionMockMvc.perform(post("/api/recurring-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recurringTransaction)))
            .andExpect(status().isCreated());

        // Validate the RecurringTransaction in the database
        List<RecurringTransaction> recurringTransactionList = recurringTransactionRepository.findAll();
        assertThat(recurringTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        RecurringTransaction testRecurringTransaction = recurringTransactionList.get(recurringTransactionList.size() - 1);
        assertThat(testRecurringTransaction.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRecurringTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testRecurringTransaction.getDateOfMonth()).isEqualTo(DEFAULT_DATE_OF_MONTH);
    }

    @Test
    @Transactional
    public void createRecurringTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recurringTransactionRepository.findAll().size();

        // Create the RecurringTransaction with an existing ID
        recurringTransaction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecurringTransactionMockMvc.perform(post("/api/recurring-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recurringTransaction)))
            .andExpect(status().isBadRequest());

        // Validate the RecurringTransaction in the database
        List<RecurringTransaction> recurringTransactionList = recurringTransactionRepository.findAll();
        assertThat(recurringTransactionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = recurringTransactionRepository.findAll().size();
        // set the field null
        recurringTransaction.setName(null);

        // Create the RecurringTransaction, which fails.

        restRecurringTransactionMockMvc.perform(post("/api/recurring-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recurringTransaction)))
            .andExpect(status().isBadRequest());

        List<RecurringTransaction> recurringTransactionList = recurringTransactionRepository.findAll();
        assertThat(recurringTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = recurringTransactionRepository.findAll().size();
        // set the field null
        recurringTransaction.setAmount(null);

        // Create the RecurringTransaction, which fails.

        restRecurringTransactionMockMvc.perform(post("/api/recurring-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recurringTransaction)))
            .andExpect(status().isBadRequest());

        List<RecurringTransaction> recurringTransactionList = recurringTransactionRepository.findAll();
        assertThat(recurringTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateOfMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = recurringTransactionRepository.findAll().size();
        // set the field null
        recurringTransaction.setDateOfMonth(null);

        // Create the RecurringTransaction, which fails.

        restRecurringTransactionMockMvc.perform(post("/api/recurring-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recurringTransaction)))
            .andExpect(status().isBadRequest());

        List<RecurringTransaction> recurringTransactionList = recurringTransactionRepository.findAll();
        assertThat(recurringTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecurringTransactions() throws Exception {
        // Initialize the database
        recurringTransactionRepository.saveAndFlush(recurringTransaction);

        // Get all the recurringTransactionList
        restRecurringTransactionMockMvc.perform(get("/api/recurring-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recurringTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].dateOfMonth").value(hasItem(DEFAULT_DATE_OF_MONTH)));
    }
    
    @Test
    @Transactional
    public void getRecurringTransaction() throws Exception {
        // Initialize the database
        recurringTransactionRepository.saveAndFlush(recurringTransaction);

        // Get the recurringTransaction
        restRecurringTransactionMockMvc.perform(get("/api/recurring-transactions/{id}", recurringTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recurringTransaction.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.dateOfMonth").value(DEFAULT_DATE_OF_MONTH));
    }

    @Test
    @Transactional
    public void getNonExistingRecurringTransaction() throws Exception {
        // Get the recurringTransaction
        restRecurringTransactionMockMvc.perform(get("/api/recurring-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecurringTransaction() throws Exception {
        // Initialize the database
        recurringTransactionService.save(recurringTransaction);

        int databaseSizeBeforeUpdate = recurringTransactionRepository.findAll().size();

        // Update the recurringTransaction
        RecurringTransaction updatedRecurringTransaction = recurringTransactionRepository.findById(recurringTransaction.getId()).get();
        // Disconnect from session so that the updates on updatedRecurringTransaction are not directly saved in db
        em.detach(updatedRecurringTransaction);
        updatedRecurringTransaction
            .name(UPDATED_NAME)
            .amount(UPDATED_AMOUNT)
            .dateOfMonth(UPDATED_DATE_OF_MONTH);

        restRecurringTransactionMockMvc.perform(put("/api/recurring-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecurringTransaction)))
            .andExpect(status().isOk());

        // Validate the RecurringTransaction in the database
        List<RecurringTransaction> recurringTransactionList = recurringTransactionRepository.findAll();
        assertThat(recurringTransactionList).hasSize(databaseSizeBeforeUpdate);
        RecurringTransaction testRecurringTransaction = recurringTransactionList.get(recurringTransactionList.size() - 1);
        assertThat(testRecurringTransaction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRecurringTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testRecurringTransaction.getDateOfMonth()).isEqualTo(UPDATED_DATE_OF_MONTH);
    }

    @Test
    @Transactional
    public void updateNonExistingRecurringTransaction() throws Exception {
        int databaseSizeBeforeUpdate = recurringTransactionRepository.findAll().size();

        // Create the RecurringTransaction

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecurringTransactionMockMvc.perform(put("/api/recurring-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recurringTransaction)))
            .andExpect(status().isBadRequest());

        // Validate the RecurringTransaction in the database
        List<RecurringTransaction> recurringTransactionList = recurringTransactionRepository.findAll();
        assertThat(recurringTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRecurringTransaction() throws Exception {
        // Initialize the database
        recurringTransactionService.save(recurringTransaction);

        int databaseSizeBeforeDelete = recurringTransactionRepository.findAll().size();

        // Delete the recurringTransaction
        restRecurringTransactionMockMvc.perform(delete("/api/recurring-transactions/{id}", recurringTransaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RecurringTransaction> recurringTransactionList = recurringTransactionRepository.findAll();
        assertThat(recurringTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
