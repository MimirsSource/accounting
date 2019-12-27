package de.mimirssource.accounting.web.rest;

import de.mimirssource.accounting.AccountingApp;
import de.mimirssource.accounting.domain.Ledger;
import de.mimirssource.accounting.repository.LedgerRepository;
import de.mimirssource.accounting.service.LedgerService;
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
 * Integration tests for the {@link LedgerResource} REST controller.
 */
@SpringBootTest(classes = AccountingApp.class)
public class LedgerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private LedgerService ledgerService;

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

    private MockMvc restLedgerMockMvc;

    private Ledger ledger;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LedgerResource ledgerResource = new LedgerResource(ledgerService);
        this.restLedgerMockMvc = MockMvcBuilders.standaloneSetup(ledgerResource)
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
    public static Ledger createEntity(EntityManager em) {
        Ledger ledger = new Ledger()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE);
        return ledger;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ledger createUpdatedEntity(EntityManager em) {
        Ledger ledger = new Ledger()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE);
        return ledger;
    }

    @BeforeEach
    public void initTest() {
        ledger = createEntity(em);
    }

    @Test
    @Transactional
    public void createLedger() throws Exception {
        int databaseSizeBeforeCreate = ledgerRepository.findAll().size();

        // Create the Ledger
        restLedgerMockMvc.perform(post("/api/ledgers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ledger)))
            .andExpect(status().isCreated());

        // Validate the Ledger in the database
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeCreate + 1);
        Ledger testLedger = ledgerList.get(ledgerList.size() - 1);
        assertThat(testLedger.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLedger.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLedger.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createLedgerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ledgerRepository.findAll().size();

        // Create the Ledger with an existing ID
        ledger.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLedgerMockMvc.perform(post("/api/ledgers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ledger)))
            .andExpect(status().isBadRequest());

        // Validate the Ledger in the database
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ledgerRepository.findAll().size();
        // set the field null
        ledger.setName(null);

        // Create the Ledger, which fails.

        restLedgerMockMvc.perform(post("/api/ledgers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ledger)))
            .andExpect(status().isBadRequest());

        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = ledgerRepository.findAll().size();
        // set the field null
        ledger.setDescription(null);

        // Create the Ledger, which fails.

        restLedgerMockMvc.perform(post("/api/ledgers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ledger)))
            .andExpect(status().isBadRequest());

        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = ledgerRepository.findAll().size();
        // set the field null
        ledger.setCreationDate(null);

        // Create the Ledger, which fails.

        restLedgerMockMvc.perform(post("/api/ledgers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ledger)))
            .andExpect(status().isBadRequest());

        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLedgers() throws Exception {
        // Initialize the database
        ledgerRepository.saveAndFlush(ledger);

        // Get all the ledgerList
        restLedgerMockMvc.perform(get("/api/ledgers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ledger.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getLedger() throws Exception {
        // Initialize the database
        ledgerRepository.saveAndFlush(ledger);

        // Get the ledger
        restLedgerMockMvc.perform(get("/api/ledgers/{id}", ledger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ledger.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLedger() throws Exception {
        // Get the ledger
        restLedgerMockMvc.perform(get("/api/ledgers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLedger() throws Exception {
        // Initialize the database
        ledgerService.save(ledger);

        int databaseSizeBeforeUpdate = ledgerRepository.findAll().size();

        // Update the ledger
        Ledger updatedLedger = ledgerRepository.findById(ledger.getId()).get();
        // Disconnect from session so that the updates on updatedLedger are not directly saved in db
        em.detach(updatedLedger);
        updatedLedger
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE);

        restLedgerMockMvc.perform(put("/api/ledgers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLedger)))
            .andExpect(status().isOk());

        // Validate the Ledger in the database
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeUpdate);
        Ledger testLedger = ledgerList.get(ledgerList.size() - 1);
        assertThat(testLedger.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLedger.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLedger.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingLedger() throws Exception {
        int databaseSizeBeforeUpdate = ledgerRepository.findAll().size();

        // Create the Ledger

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLedgerMockMvc.perform(put("/api/ledgers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ledger)))
            .andExpect(status().isBadRequest());

        // Validate the Ledger in the database
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLedger() throws Exception {
        // Initialize the database
        ledgerService.save(ledger);

        int databaseSizeBeforeDelete = ledgerRepository.findAll().size();

        // Delete the ledger
        restLedgerMockMvc.perform(delete("/api/ledgers/{id}", ledger.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
