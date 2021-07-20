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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import com.esatus.ssi.bkamt.controller.verification.domain.CheckInCredential;
import com.esatus.ssi.bkamt.controller.verification.domain.User;
import com.esatus.ssi.bkamt.controller.verification.repository.CheckInCredentialRepository;
import com.esatus.ssi.bkamt.controller.verification.security.AuthoritiesConstants;
import com.esatus.ssi.bkamt.controller.verification.web.rest.CheckInCredentialController;

/**
 * Integration tests for the {@link CheckInCredentialController} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.USER)
@SpringBootTest(classes = VerificationControllerApp.class)
public class CheckInCredentialControllerIT {

    private static final String DEFAULT_ID = "id_1";
    private static final String DEFAULT_HOTEL_ID = "hotel_1";
    private static final String DEFAULT_DESK_ID = "desk_1";

    private static final String DEFAULT_LOGIN = "user1";
    private static final String DEFAULT_PASSWORD = "super_secret";

    @Autowired
    private CheckInCredentialRepository checkInCredentialRepository;

    @Autowired
    private MockMvc restCheckInCredentialMockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private CheckInCredential checkInCredential;

    public CheckInCredential createEntity() {
        CheckInCredential checkInCredential = new CheckInCredential();
        checkInCredential.setId(DEFAULT_ID);
        checkInCredential.setHotelId(DEFAULT_HOTEL_ID);
        checkInCredential.setDeskId(DEFAULT_DESK_ID);
        return checkInCredential;
    }

    private User createUser() {
        User user = new User();
        user.setLogin(DEFAULT_LOGIN);
        user.setFirstName("fn");
        user.setLastName("ln");
        user.setEmail(null);
        user.setHotelId(DEFAULT_ID);
        user.setPassword(this.passwordEncoder.encode(DEFAULT_PASSWORD));
        return user;
    }

    @BeforeEach
    public void initTest() {
        checkInCredentialRepository.deleteAll();
        checkInCredential = this.createEntity();
        this.createUser();
    }

    @Test
    public void getCheckInCredentialById() throws Exception {
        // Initialize the database
        checkInCredentialRepository.save(checkInCredential);

        // Get the CheckInCredential
        restCheckInCredentialMockMvc.perform(get("/api/checkin-credentials/{id}", checkInCredential.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(DEFAULT_ID))
            .andExpect(jsonPath("$.hotelId").value(DEFAULT_HOTEL_ID))
            .andExpect(jsonPath("$.deskId").value(DEFAULT_DESK_ID));
    }

    @Test
    public void getNonExistingCheckInCredential() throws Exception {
        restCheckInCredentialMockMvc.perform(get("/api/checkin-credentials/unknown")).andExpect(status().isNotFound());
    }
}
