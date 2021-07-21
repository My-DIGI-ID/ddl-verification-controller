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

import javax.validation.Valid;

import com.esatus.ssi.bkamt.controller.verification.service.exceptions.VerificationAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.esatus.ssi.bkamt.controller.verification.security.AuthoritiesConstants;
import com.esatus.ssi.bkamt.controller.verification.service.VerificationService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.UserCreationDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.UserDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationCreationDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.HotelNotFoundException;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.UserAlreadyExistsException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing verifications.
 */
@Tag(name = "Verification", description = "Manage verification")
@RestController
@RequestMapping("/api")
public class VerificationController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
	VerificationService verificationService;

	/**
	 * {@code POST  /verification} : Create a new verification
	 *
	 * @param verificationDTO the verification to create
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         the body the new verification, or with status
	 *         {@code 400 (Bad Request)} {@code 400 (Bad Request)} if a verification
	 *         with the given name does already exist.
	 */
	@PostMapping("/verifications")
	@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
	@Operation(security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<VerificationDTO> createVerification(@Valid @RequestBody VerificationCreationDTO verificationCreationDTO) {

        try {
            VerificationDTO createdVerification = this.verificationService.createVerification(verificationCreationDTO);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdVerification.getId()).toUri();
            return ResponseEntity.created(location).body(createdVerification);
        } catch (VerificationAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
	}
}
