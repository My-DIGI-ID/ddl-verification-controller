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
import com.esatus.ssi.bkamt.controller.verification.service.UserService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.UserCreationDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.UserDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.HotelNotFoundException;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.UserAlreadyExistsException;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.UserNotFoundException;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.UserWithLoginAlreadyExists;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing users.
 */
@Tag(name = "Users", description = "Manage your users")
@RestController
@RequestMapping("/api")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    /**
     * {@code POST  /users} : Create a new user
     *
     * @param userDTO the user to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         the body the new user, or with status {@code 400 (Bad Request)} if a
     *         user with the given ID already exists or with status
     *         {@code 400 (Bad Request)} if a hotel with the given hotelId does not
     *         exist.
     */
    @PostMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreationDTO userCreationDTO) {

        log.debug("REST request to create a new user : {}", userCreationDTO);

        try {
            UserDTO createdUser = this.userService.createUser(userCreationDTO);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(createdUser.getId()).toUri();
            return ResponseEntity.created(location).body(createdUser);
        } catch (UserAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (HotelNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * {@code PUT  /users} : Updates an existing user.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated user, or with status {@code 404 (Not Found)} if the user
     *         does not exist.
     * @throws UserWithLoginAlreadyExists
     * @throws HotelNotFoundException
     */
    @PutMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO)
            throws UserWithLoginAlreadyExists, HotelNotFoundException {

        log.debug("REST request to update a user : {}", userDTO);

        try {
            UserDTO updatedUser = this.userService.updateUser(userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * {@code GET  /users} : get all the users.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of users in body.
     */
    @GetMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.debug("REST request to get all users");

        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    /**
     * {@code GET  /users/:id} : get the "id" user.
     *
     * @param id the id of the user to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        log.debug("REST request to get user : {}", id);
        Optional<UserDTO> userDTO = this.userService.getUser(id);
        return ResponseUtil.wrapOrNotFound(userDTO);
    }

    /**
     * {@code DELETE  /users/:id} : delete the "id" user.
     *
     * @param id the id of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {

        log.debug("REST request to delete user : {}", id);

        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
