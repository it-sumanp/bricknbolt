package com.bricknbolt.web.rest;

import com.bricknbolt.BricknboltApp;

import com.bricknbolt.domain.BoltUser;
import com.bricknbolt.repository.BoltUserRepository;
import com.bricknbolt.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.bricknbolt.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BoltUserResource REST controller.
 *
 * @see BoltUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BricknboltApp.class)
public class BoltUserResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "3";
    private static final String UPDATED_PHONE = "2";

    @Autowired
    private BoltUserRepository boltUserRepository;

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

    private MockMvc restBoltUserMockMvc;

    private BoltUser boltUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BoltUserResource boltUserResource = new BoltUserResource(boltUserRepository);
        this.restBoltUserMockMvc = MockMvcBuilders.standaloneSetup(boltUserResource)
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
    public static BoltUser createEntity(EntityManager em) {
        BoltUser boltUser = new BoltUser()
            .name(DEFAULT_NAME)
            .phone(DEFAULT_PHONE);
        return boltUser;
    }

    @Before
    public void initTest() {
        boltUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createBoltUser() throws Exception {
        int databaseSizeBeforeCreate = boltUserRepository.findAll().size();

        // Create the BoltUser
        restBoltUserMockMvc.perform(post("/api/bolt-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boltUser)))
            .andExpect(status().isCreated());

        // Validate the BoltUser in the database
        List<BoltUser> boltUserList = boltUserRepository.findAll();
        assertThat(boltUserList).hasSize(databaseSizeBeforeCreate + 1);
        BoltUser testBoltUser = boltUserList.get(boltUserList.size() - 1);
        assertThat(testBoltUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBoltUser.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void createBoltUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boltUserRepository.findAll().size();

        // Create the BoltUser with an existing ID
        boltUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoltUserMockMvc.perform(post("/api/bolt-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boltUser)))
            .andExpect(status().isBadRequest());

        // Validate the BoltUser in the database
        List<BoltUser> boltUserList = boltUserRepository.findAll();
        assertThat(boltUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = boltUserRepository.findAll().size();
        // set the field null
        boltUser.setName(null);

        // Create the BoltUser, which fails.

        restBoltUserMockMvc.perform(post("/api/bolt-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boltUser)))
            .andExpect(status().isBadRequest());

        List<BoltUser> boltUserList = boltUserRepository.findAll();
        assertThat(boltUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = boltUserRepository.findAll().size();
        // set the field null
        boltUser.setPhone(null);

        // Create the BoltUser, which fails.

        restBoltUserMockMvc.perform(post("/api/bolt-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boltUser)))
            .andExpect(status().isBadRequest());

        List<BoltUser> boltUserList = boltUserRepository.findAll();
        assertThat(boltUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBoltUsers() throws Exception {
        // Initialize the database
        boltUserRepository.saveAndFlush(boltUser);

        // Get all the boltUserList
        restBoltUserMockMvc.perform(get("/api/bolt-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boltUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())));
    }
    
    @Test
    @Transactional
    public void getBoltUser() throws Exception {
        // Initialize the database
        boltUserRepository.saveAndFlush(boltUser);

        // Get the boltUser
        restBoltUserMockMvc.perform(get("/api/bolt-users/{id}", boltUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(boltUser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBoltUser() throws Exception {
        // Get the boltUser
        restBoltUserMockMvc.perform(get("/api/bolt-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoltUser() throws Exception {
        // Initialize the database
        boltUserRepository.saveAndFlush(boltUser);

        int databaseSizeBeforeUpdate = boltUserRepository.findAll().size();

        // Update the boltUser
        BoltUser updatedBoltUser = boltUserRepository.findById(boltUser.getId()).get();
        // Disconnect from session so that the updates on updatedBoltUser are not directly saved in db
        em.detach(updatedBoltUser);
        updatedBoltUser
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE);

        restBoltUserMockMvc.perform(put("/api/bolt-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBoltUser)))
            .andExpect(status().isOk());

        // Validate the BoltUser in the database
        List<BoltUser> boltUserList = boltUserRepository.findAll();
        assertThat(boltUserList).hasSize(databaseSizeBeforeUpdate);
        BoltUser testBoltUser = boltUserList.get(boltUserList.size() - 1);
        assertThat(testBoltUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBoltUser.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingBoltUser() throws Exception {
        int databaseSizeBeforeUpdate = boltUserRepository.findAll().size();

        // Create the BoltUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoltUserMockMvc.perform(put("/api/bolt-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boltUser)))
            .andExpect(status().isBadRequest());

        // Validate the BoltUser in the database
        List<BoltUser> boltUserList = boltUserRepository.findAll();
        assertThat(boltUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBoltUser() throws Exception {
        // Initialize the database
        boltUserRepository.saveAndFlush(boltUser);

        int databaseSizeBeforeDelete = boltUserRepository.findAll().size();

        // Delete the boltUser
        restBoltUserMockMvc.perform(delete("/api/bolt-users/{id}", boltUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BoltUser> boltUserList = boltUserRepository.findAll();
        assertThat(boltUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoltUser.class);
        BoltUser boltUser1 = new BoltUser();
        boltUser1.setId(1L);
        BoltUser boltUser2 = new BoltUser();
        boltUser2.setId(boltUser1.getId());
        assertThat(boltUser1).isEqualTo(boltUser2);
        boltUser2.setId(2L);
        assertThat(boltUser1).isNotEqualTo(boltUser2);
        boltUser1.setId(null);
        assertThat(boltUser1).isNotEqualTo(boltUser2);
    }
}
