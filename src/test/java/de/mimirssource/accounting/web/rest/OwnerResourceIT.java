package de.mimirssource.accounting.web.rest;

import de.mimirssource.accounting.AccountingApp;
import de.mimirssource.accounting.domain.Owner;
import de.mimirssource.accounting.repository.OwnerRepository;
import de.mimirssource.accounting.service.OwnerService;
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
 * Integration tests for the {@link OwnerResource} REST controller.
 */
@SpringBootTest(classes = AccountingApp.class)
public class OwnerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private OwnerService ownerService;

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

    private MockMvc restOwnerMockMvc;

    private Owner owner;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OwnerResource ownerResource = new OwnerResource(ownerService);
        this.restOwnerMockMvc = MockMvcBuilders.standaloneSetup(ownerResource)
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
    public static Owner createEntity(EntityManager em) {
        Owner owner = new Owner()
            .name(DEFAULT_NAME);
        return owner;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Owner createUpdatedEntity(EntityManager em) {
        Owner owner = new Owner()
            .name(UPDATED_NAME);
        return owner;
    }

    @BeforeEach
    public void initTest() {
        owner = createEntity(em);
    }

    @Test
    @Transactional
    public void createOwner() throws Exception {
        int databaseSizeBeforeCreate = ownerRepository.findAll().size();

        // Create the Owner
        restOwnerMockMvc.perform(post("/api/owners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(owner)))
            .andExpect(status().isCreated());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeCreate + 1);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createOwnerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ownerRepository.findAll().size();

        // Create the Owner with an existing ID
        owner.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOwnerMockMvc.perform(post("/api/owners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(owner)))
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownerRepository.findAll().size();
        // set the field null
        owner.setName(null);

        // Create the Owner, which fails.

        restOwnerMockMvc.perform(post("/api/owners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(owner)))
            .andExpect(status().isBadRequest());

        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOwners() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList
        restOwnerMockMvc.perform(get("/api/owners?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(owner.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get the owner
        restOwnerMockMvc.perform(get("/api/owners/{id}", owner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(owner.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingOwner() throws Exception {
        // Get the owner
        restOwnerMockMvc.perform(get("/api/owners/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOwner() throws Exception {
        // Initialize the database
        ownerService.save(owner);

        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Update the owner
        Owner updatedOwner = ownerRepository.findById(owner.getId()).get();
        // Disconnect from session so that the updates on updatedOwner are not directly saved in db
        em.detach(updatedOwner);
        updatedOwner
            .name(UPDATED_NAME);

        restOwnerMockMvc.perform(put("/api/owners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOwner)))
            .andExpect(status().isOk());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Create the Owner

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnerMockMvc.perform(put("/api/owners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(owner)))
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOwner() throws Exception {
        // Initialize the database
        ownerService.save(owner);

        int databaseSizeBeforeDelete = ownerRepository.findAll().size();

        // Delete the owner
        restOwnerMockMvc.perform(delete("/api/owners/{id}", owner.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
