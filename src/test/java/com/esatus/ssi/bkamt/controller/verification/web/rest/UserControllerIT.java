/*
 * Copyright 2021 Bundesrepublik Deutschland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.esatus.ssi.bkamt.controller.verification.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.hasItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.esatus.ssi.bkamt.controller.verification.VerificationControllerApp;
import com.esatus.ssi.bkamt.controller.verification.domain.Authority;
import com.esatus.ssi.bkamt.controller.verification.domain.Hotel;
import com.esatus.ssi.bkamt.controller.verification.domain.User;
import com.esatus.ssi.bkamt.controller.verification.repository.HotelRepository;
import com.esatus.ssi.bkamt.controller.verification.repository.UserRepository;
import com.esatus.ssi.bkamt.controller.verification.security.AuthoritiesConstants;
import com.esatus.ssi.bkamt.controller.verification.service.dto.UserCreationDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.UserDTO;
import com.esatus.ssi.bkamt.controller.verification.web.rest.UserController;

/**
 * Integration tests for the {@link UserController} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@SpringBootTest(classes = VerificationControllerApp.class)
public class UserControllerIT {

    private static final String DEFAULT_ID = "id_1";
    private static final String ANOTHER_ID = "id_2";
    private static final String DEFAULT_LOGIN = "homer";
    private static final String ANOTHER_LOGIN = "marge";
    private static final String DEFAULT_FIRSTNAME = "Homer";
    private static final String DEFAULT_LASTNAME = "Simpson";
    private static final String DEFAULT_EMAIL = "homer@springfield.com";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String DEFAULT_HOTEL_ID = "hotel1";

    private static final String MORE_THAN_50_CHARS = "12345678901234567890123456789012345678901234567890x";
    private static final String MORE_THAN_254_CHARS = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234x";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private MockMvc restUserMockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User anotherUser;
    private User user;
    private Hotel hotel;

    public User createEntity() {
        User user = new User();
        user.setId(DEFAULT_ID);
        user.setLogin(DEFAULT_LOGIN);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setEmail(DEFAULT_EMAIL);
        user.setHotelId(DEFAULT_HOTEL_ID);
        user.setPassword(this.passwordEncoder.encode(DEFAULT_PASSWORD));
        Set<Authority> userAuthorities = this.giveAuthority();
        user.setAuthorities(userAuthorities);

        return user;
    }

    public User createAnotherEntity() {
        User anotherUser = new User();
        anotherUser.setId(ANOTHER_ID);
        anotherUser.setLogin(ANOTHER_LOGIN);
        anotherUser.setFirstName(DEFAULT_FIRSTNAME);
        anotherUser.setLastName(DEFAULT_LASTNAME);
        anotherUser.setEmail(DEFAULT_EMAIL);
        anotherUser.setHotelId(DEFAULT_HOTEL_ID);
        anotherUser.setPassword(this.passwordEncoder.encode(DEFAULT_PASSWORD));
        Set<Authority> userAuthorities = this.giveAuthority();
        user.setAuthorities(userAuthorities);

        return anotherUser;
    }

    public Hotel createHotel() {
        Hotel hotel = new Hotel();
        hotel.setId(DEFAULT_HOTEL_ID);
        hotel.setName("My Hotel");
        return hotel;
    }

    @BeforeEach
    public void initTest() {
        userRepository.deleteAll();
        hotel = this.createHotel();
        user = this.createEntity();
        anotherUser = this.createAnotherEntity();
    }

    @Test
    public void createUser() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel);

        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the user
        UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        restUserMockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(userCreationDTO))).andExpect(status().isCreated());

        // Validate the user in the database
        assertPersistedUsers(users -> {
            assertThat(users).hasSize(databaseSizeBeforeCreate + 1);
            User testUser = users.get(users.size() - 1);
            assertThat(testUser.getId()).isNotNull();
            assertThat(testUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
            assertThat(testUser.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
            assertThat(testUser.getLastName()).isEqualTo(DEFAULT_LASTNAME);
            assertThat(testUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
            assertThat(testUser.getHotelId()).isEqualTo(hotel.getId());
            assertThat(passwordEncoder.matches(DEFAULT_PASSWORD, testUser.getPassword())).isTrue();

        });
    }

    @Test
    public void createUserWithoutEmail() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel);

        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the user
        UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        restUserMockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(userCreationDTO))).andExpect(status().isCreated());

        // Validate the user in the database
        assertPersistedUsers(users -> {
            assertThat(users).hasSize(databaseSizeBeforeCreate + 1);
            User testUser = users.get(users.size() - 1);
            assertThat(testUser.getId()).isNotNull();
            assertThat(testUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
            assertThat(testUser.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
            assertThat(testUser.getLastName()).isEqualTo(DEFAULT_LASTNAME);
            assertThat(testUser.getEmail()).isNull();
            assertThat(testUser.getHotelId()).isEqualTo(hotel.getId());
            assertThat(passwordEncoder.matches(DEFAULT_PASSWORD, testUser.getPassword())).isTrue();

        });
    }

    @Test
    public void createUserWithNonExistingHotelId() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel);

        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the user
        UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId("invalid");
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Validate the user in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    public void createUserWithExistingLogin() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel);

        // Create the user
        UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        // 1st call should work fine
        restUserMockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(userCreationDTO))).andExpect(status().isCreated());

        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // 2nd call should faile
        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Validate the user in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    public void createUserWithInvalidFields() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the user without a login
        UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setLogin(null);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Create the user with a too long login
        userCreationDTO.setLogin(MORE_THAN_50_CHARS);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Create the user with an empty login
        userCreationDTO.setLogin("");
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Create the user with a too long firstname
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(MORE_THAN_50_CHARS);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Create the user without a lastname
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(null);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Create the user with a too long lastname
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(MORE_THAN_50_CHARS);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Create the user with an empty lastname
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname("");
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Create the user with a too long email
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail(MORE_THAN_254_CHARS);
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Create the user with a too short email
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail("abc");
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Create the user without a hotelId
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId(null);
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Create the user with a too long hotelId
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId(MORE_THAN_50_CHARS);
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Create the user with an empty hotelId
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId("");
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Create the user without a password
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword(null);

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Create the user with a too long password
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword(MORE_THAN_50_CHARS);

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Create the user with a too short password
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword("1234567");

        restUserMockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userCreationDTO)))
                .andExpect(status().isBadRequest());

        // Validate the user in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    public void updateUser() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel);
        userRepository.save(user);

        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Update the user
        UserDTO userUpdateDTO = new UserDTO();
        userUpdateDTO.setId(user.getId());
        userUpdateDTO.setLogin(DEFAULT_LOGIN);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(DEFAULT_LASTNAME);
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId(hotel.getId());

        restUserMockMvc.perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO))).andExpect(status().isOk());

        // Validate the user in the database
        assertPersistedUsers(users -> {
            assertThat(users).hasSize(databaseSizeBeforeCreate + 0);
            User testUser = users.get(users.size() - 1);
            assertThat(testUser.getId()).isNotNull();
            assertThat(testUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
            assertThat(testUser.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
            assertThat(testUser.getLastName()).isEqualTo(DEFAULT_LASTNAME);
            assertThat(testUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
            assertThat(testUser.getHotelId()).isEqualTo(hotel.getId());

        });
    }

    @Test
    public void updateUserWithNonExistingHotelId() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel);

        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Update the user
        UserDTO userUpdateDTO = new UserDTO();
        userUpdateDTO.setLogin(DEFAULT_LOGIN);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(DEFAULT_LASTNAME);
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId("invalid");

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Validate the user in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    public void updateUserWithExistingLogin() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel);
        userRepository.save(user);
        userRepository.save(anotherUser);

        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Update the user
        UserDTO userUpdateDTO = new UserDTO();
        userUpdateDTO.setId(user.getId());
        userUpdateDTO.setLogin(ANOTHER_LOGIN);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(DEFAULT_LASTNAME);
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId(hotel.getId());

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isInternalServerError());

        // Validate the user in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    public void updateUserWithInvalidFields() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Update the user without a login
        UserDTO userUpdateDTO = new UserDTO();
        userUpdateDTO.setLogin(null);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(DEFAULT_LASTNAME);
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId(hotel.getId());

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Update the user with a too long login
        userUpdateDTO.setLogin(MORE_THAN_50_CHARS);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(DEFAULT_LASTNAME);
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId(hotel.getId());

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Update the user with an empty login
        userUpdateDTO.setLogin("");
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(DEFAULT_LASTNAME);
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId(hotel.getId());

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Update the user with a too long firstname
        userUpdateDTO.setLogin(DEFAULT_LOGIN);
        userUpdateDTO.setFirstname(MORE_THAN_50_CHARS);
        userUpdateDTO.setLastname(DEFAULT_LASTNAME);
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId(hotel.getId());

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Update the user without a lastname
        userUpdateDTO.setLogin(DEFAULT_LOGIN);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(null);
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId(hotel.getId());

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Update the user with a too long lastname
        userUpdateDTO.setLogin(DEFAULT_LOGIN);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(MORE_THAN_50_CHARS);
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId(hotel.getId());

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Update the user with an empty lastname
        userUpdateDTO.setLogin(DEFAULT_LOGIN);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname("");
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId(hotel.getId());

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Update the user with a too long email
        userUpdateDTO.setLogin(DEFAULT_LOGIN);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(DEFAULT_LASTNAME);
        userUpdateDTO.setEmail(MORE_THAN_254_CHARS);
        userUpdateDTO.setHotelId(hotel.getId());

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Update the user with a too short email
        userUpdateDTO.setLogin(DEFAULT_LOGIN);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(DEFAULT_LASTNAME);
        userUpdateDTO.setEmail("abc");
        userUpdateDTO.setHotelId(hotel.getId());

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Update the user without a hotelId
        userUpdateDTO.setLogin(DEFAULT_LOGIN);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(DEFAULT_LASTNAME);
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId(null);

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Update the user with a too long hotelId
        userUpdateDTO.setLogin(DEFAULT_LOGIN);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(DEFAULT_LASTNAME);
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId(MORE_THAN_50_CHARS);

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Update the user with an empty hotelId
        userUpdateDTO.setLogin(DEFAULT_LOGIN);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(DEFAULT_LASTNAME);
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId("");

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Update the user without a password
        userUpdateDTO.setLogin(DEFAULT_LOGIN);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(DEFAULT_LASTNAME);
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId(hotel.getId());

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Update the user with a too long password
        userUpdateDTO.setLogin(DEFAULT_LOGIN);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(DEFAULT_LASTNAME);
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId(hotel.getId());

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Update the user with a too short password
        userUpdateDTO.setLogin(DEFAULT_LOGIN);
        userUpdateDTO.setFirstname(DEFAULT_FIRSTNAME);
        userUpdateDTO.setLastname(DEFAULT_LASTNAME);
        userUpdateDTO.setEmail(DEFAULT_EMAIL);
        userUpdateDTO.setHotelId(hotel.getId());

        restUserMockMvc
                .perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userUpdateDTO)))
                .andExpect(status().isBadRequest());

        // Validate the user in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    public void getAllUsers() throws Exception {
        // Initialize the database
        userRepository.save(user);

        // Get all the users as admin
        restUserMockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(DEFAULT_ID)))
        .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)));
    }

    @Test
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    public void getUser() throws Exception {
        // Initialize the database
        User createdUser = userRepository.save(user);

        // Get the user as admin
        restUserMockMvc.perform(get("/api/users/{id}", createdUser.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
                .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
                .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
                .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$.hotelId").value(DEFAULT_HOTEL_ID));
    }

    @Test
    public void getNonExistingUser() throws Exception {

        // Get a user that does not exist
        restUserMockMvc.perform(get("/api/users/unknown")).andExpect(status().isNotFound());
    }

    @Test
    public void deleteUser() throws Exception {
        // Initialize the database
        User createdUser = userRepository.save(user);
        int databaseSizeBeforeDelete = userRepository.findAll().size();

        // Delete the user
        restUserMockMvc.perform(delete("/api/users/{id}", createdUser.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database is empty
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeDelete - 1));
    }

    @Test
    @WithMockUser(authorities = AuthoritiesConstants.USER)
    public void createUserWithUserAuthority() throws Exception {

        // Create the user
        UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setLogin(DEFAULT_LOGIN);
        userCreationDTO.setFirstname(DEFAULT_FIRSTNAME);
        userCreationDTO.setLastname(DEFAULT_LASTNAME);
        userCreationDTO.setEmail(DEFAULT_EMAIL);
        userCreationDTO.setHotelId(hotel.getId());
        userCreationDTO.setPassword(DEFAULT_PASSWORD);

        // Expect this to return Forbidden as no one but an Admin can create a user
        restUserMockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(userCreationDTO))).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = AuthoritiesConstants.USER)
    public void getAllUsersWithUserAuthority() throws Exception {

        // Trying to get all users with the authority of USER, should return forbidden
        restUserMockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = AuthoritiesConstants.USER)
    public void getUserWithUserAuthority() throws Exception {

        // Trying to get a specific user while not having the Admin role assigned, returns forbidden
        restUserMockMvc.perform(get("/api/users/id").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = AuthoritiesConstants.USER)
    public void deleteUserWithUserAuthority() throws Exception {

        // Trying to perform a delete with a USER authority, returns forbidden. Only allowed for Admin role
        restUserMockMvc.perform(delete("/api/users/id").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void assertPersistedUsers(Consumer<List<User>> userAssertion) {
        userAssertion.accept(userRepository.findAll());
    }

    private Set<Authority> giveAuthority() {

        // Here we give the user authority with the role USER
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);
        Set<Authority> userAuthorities = new HashSet<Authority>();
        userAuthorities.add(userAuthority);

        return userAuthorities;
    }

}
