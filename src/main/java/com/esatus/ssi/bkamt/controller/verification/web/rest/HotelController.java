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

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.esatus.ssi.bkamt.controller.verification.security.AuthoritiesConstants;
import com.esatus.ssi.bkamt.controller.verification.service.HotelService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.HotelDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.HotelAlreadyExistsException;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.HotelNotFoundException;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing hotels.
 */
@Tag(name = "Hotels", description = "Manage your hotels and desks")
@RestController
@RequestMapping("/api")
public class HotelController {

    private final Logger log = LoggerFactory.getLogger(HotelController.class);

    @Autowired
    HotelService hotelService;


    /**
     * {@code POST  /hotels} : Create new a hotel
     *
     * @param hotelDTO the hotel to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         the body the new hotel, or with status {@code 400 (Bad Request)} if a
     *         hotel with the given ID already exists.
     */
    @PostMapping("/hotels")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HotelDTO> createHotel(@Valid @RequestBody HotelDTO hotelDTO) {

        log.debug("REST request to create a new hotel : {}", hotelDTO);

        try {
            HotelDTO createdHotel = this.hotelService.createHotel(hotelDTO);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(createdHotel.getId()).toUri();
            return ResponseEntity.created(location).body(createdHotel);
        } catch (HotelAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * {@code PUT  /hotels} : Updates an existing hotel.
     *
     * @param hotelDTO the hotel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated hotel, or with status {@code 404 (Not Found)} if the
     *         hotel does not exist.
     */
    @PutMapping("/hotels")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HotelDTO> updateHotel(@Valid @RequestBody HotelDTO hotelDTO) {

        log.debug("REST request to update a hotel : {}", hotelDTO);

        try {
            HotelDTO updatedHotel = this.hotelService.updateHotel(hotelDTO);
            return ResponseEntity.ok(updatedHotel);
        } catch (HotelNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * {@code GET  /hotels} : get all the hotels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of hotels in body.
     */
    @GetMapping("/hotels")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<HotelDTO>> getAllHotels() {
        log.debug("REST request to get all hotels");

        return ResponseEntity.ok(this.hotelService.getAllHotels());
    }

    /**
     * {@code GET  /hotels/:id} : get the "id" hotel.
     *
     * @param id the id of the hotel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the hotel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hotels/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HotelDTO> getHotel(@PathVariable String id) {
        log.debug("REST request to get hotel : {}", id);
        Optional<HotelDTO> hotelDTO = this.hotelService.getHotel(id);
        return ResponseUtil.wrapOrNotFound(hotelDTO);
    }

    /**
     * {@code GET  /my-hotel} : get the hotel the user is assigned to.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the hotel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/my-hotel")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HotelDTO> getMyHotel() {
        log.debug("REST request to get hotel for user");
        Optional<HotelDTO> hotelDTO = this.hotelService.getMyHotel();
        return ResponseUtil.wrapOrNotFound(hotelDTO);
    }

    /**
     * {@code DELETE  /hotels/:id} : delete the "id" hotel.
     *
     * @param id the id of the hotel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hotels/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteHotel(@PathVariable String id) {

        log.debug("REST request to delete hotel : {}", id);

        this.hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }
}
