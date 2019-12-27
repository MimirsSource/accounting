package de.mimirssource.accounting.web.rest;

import de.mimirssource.accounting.AccountingApp;
import de.mimirssource.accounting.domain.Balance;
import de.mimirssource.accounting.repository.BalanceRepository;
import de.mimirssource.accounting.service.BalanceService;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static de.mimirssource.accounting.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BalanceResource} REST controller.
 */
@SpringBootTest(classes = AccountingApp.class)
public class BalanceResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private BalanceService balanceService;

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

    private MockMvc restBalanceMockMvc;

    private Balance balance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BalanceResource balanceResource = new BalanceResource(balanceService);
        this.restBalanceMockMvc = MockMvcBuilders.standaloneSetup(balanceResource)
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
    public static Balance createEntity(EntityManager em) {
        Balance balance = new Balance()
            .date(DEFAULT_DATE)
            .amount(DEFAULT_AMOUNT);
        return balance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Balance createUpdatedEntity(EntityManager em) {
        Balance balance = new Balance()
            .date(UPDATED_DATE)
            .amount(UPDATED_AMOUNT);
        return balance;
    }

    @BeforeEach
    public void initTest() {
        balance = createEntity(em);
    }

    @Test
    @Transactional
    public void createBalance() throws Exception {
        int databaseSizeBeforeCreate = balanceRepository.findAll().size();

        // Create the Balance
        restBalanceMockMvc.perform(post("/api/balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balance)))
            .andExpect(status().isCreated());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeCreate + 1);
        Balance testBalance = balanceList.get(balanceList.size() - 1);
        assertThat(testBalance.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBalance.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createBalanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = balanceRepository.findAll().size();

        // Create the Balance with an existing ID
        balance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBalanceMockMvc.perform(post("/api/balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balance)))
            .andExpect(status().isBadRequest());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = balanceRepository.findAll().size();
        // set the field null
        balance.setDate(null);

        // Create the Balance, which fails.

        restBalanceMockMvc.perform(post("/api/balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balance)))
            .andExpect(status().isBadRequest());

        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = balanceRepository.findAll().size();
        // set the field null
        balance.setAmount(null);

        // Create the Balance, which fails.

        restBalanceMockMvc.perform(post("/api/balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balance)))
            .andExpect(status().isBadRequest());

        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBalances() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList
        restBalanceMockMvc.perform(get("/api/balances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(balance.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)));
    }
    
    @Test
    @Transactional
    public void getBalance() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get the balance
        restBalanceMockMvc.perform(get("/api/balances/{id}", balance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(balance.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT));
    }

    @Test
    @Transactional
    public void getNonExistingBalance() throws Exception {
        // Get the balance
        restBalanceMockMvc.perform(get("/api/balances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBalance() throws Exception {
        // Initialize the database
        balanceService.save(balance);

        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();

        // Update the balance
        Balance updatedBalance = balanceRepository.findById(balance.getId()).get();
        // Disconnect from session so that the updates on updatedBalance are not directly saved in db
        em.detach(updatedBalance);
        updatedBalance
            .date(UPDATED_DATE)
            .amount(UPDATED_AMOUNT);

        restBalanceMockMvc.perform(put("/api/balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBalance)))
            .andExpect(status().isOk());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
        Balance testBalance = balanceList.get(balanceList.size() - 1);
        assertThat(testBalance.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBalance.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingBalance() throws Exception {
        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();

        // Create the Balance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBalanceMockMvc.perform(put("/api/balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balance)))
            .andExpect(status().isBadRequest());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBalance() throws Exception {
        // Initialize the database
        balanceService.save(balance);

        int databaseSizeBeforeDelete = balanceRepository.findAll().size();

        // Delete the balance
        restBalanceMockMvc.perform(delete("/api/balances/{id}", balance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
