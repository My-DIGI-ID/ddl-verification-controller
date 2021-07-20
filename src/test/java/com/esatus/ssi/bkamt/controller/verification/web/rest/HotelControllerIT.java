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

import static org.hamcrest.Matchers.hasItem;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
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
import com.esatus.ssi.bkamt.controller.verification.domain.Hotel;
import com.esatus.ssi.bkamt.controller.verification.domain.User;
import com.esatus.ssi.bkamt.controller.verification.repository.HotelRepository;
import com.esatus.ssi.bkamt.controller.verification.repository.UserRepository;
import com.esatus.ssi.bkamt.controller.verification.security.AuthoritiesConstants;
import com.esatus.ssi.bkamt.controller.verification.service.dto.AddressDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.DeskDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.HotelDTO;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.AddressMapper;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.DeskMapper;
import com.esatus.ssi.bkamt.controller.verification.web.rest.HotelController;

/**
 * Integration tests for the {@link HotelController} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@SpringBootTest(classes = VerificationControllerApp.class)
public class HotelControllerIT {

    private static final String DEFAULT_ID = "hotel_1";
    private static final String DEFAULT_NAME = "Hotel 1 - Berlin City";
    private static final String UPDATED_NAME = "Hotel 1 - Hamburg City";
    private static final AddressDTO DEFAULT_ADDRESS = new AddressDTO("street", "houseNumber", "postalCode", "city");
    private List<DeskDTO> DEFAULT_DESKS = Arrays.asList(new DeskDTO("desk_1", "Desk 1"),
            new DeskDTO("desk_2", "Desk 2"));
    private List<DeskDTO> INCORRECT_DESKS = Arrays.asList(new DeskDTO("desk_1", "Desk 1"),
            new DeskDTO("desk_2", "Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 22Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk 2Desk"));

    private static final String DEFAULT_LOGIN = "user1";
    private static final String DEFAULT_PASSWORD = "super_secret";
    private static final String MORE_THAN_50_CHARS = "12345678901234567890123456789012345678901234567890x";

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeskMapper deskMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private MockMvc restHotelMockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Hotel hotel;

    private User user;

    public Hotel createEntity() {
        Hotel hotel = new Hotel();
        hotel.setId(DEFAULT_ID);
        hotel.setName(DEFAULT_NAME);
        hotel.setAddress(this.addressMapper.addressDTOToAddress(DEFAULT_ADDRESS));
        hotel.setDesks(this.deskMapper.deskDTOsToDesks(DEFAULT_DESKS));
        return hotel;
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
        hotelRepository.deleteAll();
        hotel = this.createEntity();
        user = this.createUser();
    }

    @Test
    public void createHotel() throws Exception {
        int databaseSizeBeforeCreate = hotelRepository.findAll().size();

        // Create the hotel
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setId(DEFAULT_ID);
        hotelDTO.setName(DEFAULT_NAME);
        hotelDTO.setAddress(DEFAULT_ADDRESS);
        hotelDTO.setDesks(DEFAULT_DESKS);

        restHotelMockMvc.perform(post("/api/hotels").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(hotelDTO))).andExpect(status().isCreated());

        // Validate the hotel in the database
        assertPersistedHotels(hotels -> {
            assertThat(hotels).hasSize(databaseSizeBeforeCreate + 1);
            Hotel testHotel = hotels.get(hotels.size() - 1);
            assertThat(testHotel.getId()).isEqualTo(DEFAULT_ID);
            assertThat(testHotel.getName()).isEqualTo(DEFAULT_NAME);
            assertThat(testHotel.getAddress()).isNotNull();
            assertThat(testHotel.getDesks()).isEqualTo(this.deskMapper.deskDTOsToDesks(DEFAULT_DESKS));
        });
    }

    @Test
    public void createHotelWithExistingId() throws Exception {

        // Create the hotel
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setId(DEFAULT_ID);
        hotelDTO.setName(DEFAULT_NAME);
        hotelDTO.setAddress(DEFAULT_ADDRESS);
        hotelDTO.setDesks(DEFAULT_DESKS);

        // 1st call should work fine
        restHotelMockMvc.perform(post("/api/hotels").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(hotelDTO))).andExpect(status().isCreated());

        int databaseSizeBeforeCreate = hotelRepository.findAll().size();

        // 2nd call should faile
        restHotelMockMvc.perform(post("/api/hotels").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(hotelDTO))).andExpect(status().isBadRequest());

        // Validate the hotel in the database
        assertPersistedHotels(hotels -> assertThat(hotels).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    public void createHotelWithInvalidFields() throws Exception {
        int databaseSizeBeforeCreate = hotelRepository.findAll().size();

        // Create the hotel without an id
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setId(null);
        hotelDTO.setName(DEFAULT_NAME);
        hotelDTO.setAddress(DEFAULT_ADDRESS);
        hotelDTO.setDesks(DEFAULT_DESKS);

        restHotelMockMvc.perform(post("/api/hotels").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(hotelDTO))).andExpect(status().isBadRequest());

        // Create the hotel with a too long id
        hotelDTO.setId(MORE_THAN_50_CHARS);
        hotelDTO.setName(DEFAULT_NAME);
        hotelDTO.setAddress(DEFAULT_ADDRESS);
        hotelDTO.setDesks(DEFAULT_DESKS);

        restHotelMockMvc.perform(post("/api/hotels").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(hotelDTO))).andExpect(status().isBadRequest());

        // Create the hotel without a name
        hotelDTO.setId(DEFAULT_ID);
        hotelDTO.setName(null);
        hotelDTO.setAddress(DEFAULT_ADDRESS);
        hotelDTO.setDesks(DEFAULT_DESKS);

        restHotelMockMvc.perform(post("/api/hotels").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(hotelDTO))).andExpect(status().isBadRequest());


        // Create the hotel with a desk name that is too long
        hotelDTO.setId(DEFAULT_ID);
        hotelDTO.setName(DEFAULT_NAME);
        hotelDTO.setAddress(DEFAULT_ADDRESS);
        hotelDTO.setDesks(INCORRECT_DESKS);
        // this should fail

        restHotelMockMvc.perform(post("/api/hotels").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(hotelDTO))).andExpect(status().isBadRequest());

        // Create the hotel with a too long name
        hotelDTO.setId(DEFAULT_ID);
        hotelDTO.setName(MORE_THAN_50_CHARS);
        hotelDTO.setAddress(DEFAULT_ADDRESS);
        hotelDTO.setDesks(DEFAULT_DESKS);

        restHotelMockMvc.perform(post("/api/hotels").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(hotelDTO))).andExpect(status().isBadRequest());

        // Create the hotel without desks
        hotelDTO.setId(DEFAULT_ID);
        hotelDTO.setName(DEFAULT_NAME);
        hotelDTO.setAddress(DEFAULT_ADDRESS);
        hotelDTO.setDesks(null);

        restHotelMockMvc.perform(post("/api/hotels").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(hotelDTO))).andExpect(status().isBadRequest());

        // Validate the hotel in the database
        assertPersistedHotels(hotels -> assertThat(hotels).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    public void getAllHotels() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel);

        // Get all the hotels
        restHotelMockMvc.perform(get("/api/hotels").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(DEFAULT_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
        // .andExpect(jsonPath("$.[*].desks").value(DEFAULT_DESKS));
    }

    @Test
    public void getHotel() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel);

        // Get the hotel
        restHotelMockMvc.perform(get("/api/hotels/{id}", hotel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(DEFAULT_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
        // .andExpect(jsonPath("$.desks").value(hasItem(DEFAULT_DESKS)));
    }

    @Test
    public void getNonExistingHotel() throws Exception {
        restHotelMockMvc.perform(get("/api/hotels/unknown")).andExpect(status().isNotFound());
    }

    @Test
    public void updateHotel() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel);
        int databaseSizeBeforeUpdate = hotelRepository.findAll().size();

        // Update the hotel
        Hotel updatedHotel = hotelRepository.findById(hotel.getId()).get();

        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setId(updatedHotel.getId());
        hotelDTO.setName(UPDATED_NAME);
        hotelDTO.setAddress(DEFAULT_ADDRESS);
        hotelDTO.setDesks(DEFAULT_DESKS);

        restHotelMockMvc.perform(put("/api/hotels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hotelDTO)))
            .andExpect(status().isOk());

        // Validate the hotel in the database
        assertPersistedHotels(hotels -> {
            assertThat(hotels).hasSize(databaseSizeBeforeUpdate);
            Hotel testHotel = hotels.get(hotels.size() - 1);
            assertThat(testHotel.getName()).isEqualTo(UPDATED_NAME);
        });
    }

    @Test
    public void updateNonExistingHotel() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel);

        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setId("unknown");
        hotelDTO.setName(DEFAULT_NAME);
        hotelDTO.setAddress(DEFAULT_ADDRESS);
        hotelDTO.setDesks(DEFAULT_DESKS);

        restHotelMockMvc.perform(put("/api/hotels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hotelDTO)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void deleteHotel() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel);
        int databaseSizeBeforeDelete = hotelRepository.findAll().size();

        // Delete the hotel
        restHotelMockMvc.perform(delete("/api/hotels/{id}", hotel.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        assertPersistedHotels(hotels -> assertThat(hotels).hasSize(databaseSizeBeforeDelete - 1));
    }

    @Test
    @WithMockUser(authorities = AuthoritiesConstants.USER)
    public void createHotelWithUserAuthority() throws Exception {

        // Create the hotel
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setId(DEFAULT_ID);
        hotelDTO.setName(DEFAULT_NAME);
        hotelDTO.setAddress(DEFAULT_ADDRESS);
        hotelDTO.setDesks(DEFAULT_DESKS);

        restHotelMockMvc.perform(post("/api/hotels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hotelDTO)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = AuthoritiesConstants.USER)
    public void updateHotelWithUserAuthority() throws Exception {

        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setId(DEFAULT_ID);
        hotelDTO.setName(DEFAULT_NAME);
        hotelDTO.setAddress(DEFAULT_ADDRESS);
        hotelDTO.setDesks(DEFAULT_DESKS);

        restHotelMockMvc.perform(put("/api/hotels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hotelDTO)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = AuthoritiesConstants.USER)
    public void getAllHotelsWithUserAuthority() throws Exception {
        restHotelMockMvc.perform(get("/api/hotels")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = AuthoritiesConstants.USER)
    public void getHotelWithUserAuthority() throws Exception {
        restHotelMockMvc.perform(get("/api/hotels/id")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = AuthoritiesConstants.USER)
    public void deleteHotelWithUserAuthority() throws Exception {
        restHotelMockMvc.perform(delete("/api/hotels/id")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = DEFAULT_LOGIN, password = DEFAULT_PASSWORD)
    public void getMyHotel() throws Exception {
        // Initialize the database
        hotelRepository.save(hotel);
        userRepository.save(user);

        restHotelMockMvc.perform(get("/api/my-hotel")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(DEFAULT_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @WithMockUser(username = DEFAULT_LOGIN, password = DEFAULT_PASSWORD)
    public void getMyHotelWithNonExistentHotelId() throws Exception {
        // Initialize the database
        userRepository.save(user);

        restHotelMockMvc.perform(get("/api/my-hotel")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());
    }

    private void assertPersistedHotels(Consumer<List<Hotel>> hotelAssertion) {
        hotelAssertion.accept(hotelRepository.findAll());
    }

}
