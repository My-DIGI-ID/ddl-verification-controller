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
import java.util.Optional;

import javax.validation.Valid;

import com.esatus.ssi.bkamt.controller.verification.service.exceptions.VerifierAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.esatus.ssi.bkamt.controller.verification.service.VerifierService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerifierCreationDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerifierDTO;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing verifiers.
 */
@Tag(name = "Verifier", description = "Manage Verifier")
@RestController
@RequestMapping("/api")
public class VerifierController {

    private final Logger log = LoggerFactory.getLogger(VerifierController.class);

    @Autowired
    VerifierService verifierService;

	/**
	 * {@code POST  /verification} : Create a new verification
	 *
	 * @param verifierCreationDTO the verification to create
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         the body the new verification, or with status
	 *         {@code 400 (Bad Request)} {@code 400 (Bad Request)} if a verification
	 *         with the given name does already exist.
	 */
	@PostMapping("/verifier")
	public ResponseEntity<VerifierDTO> createVerification(@Valid @RequestBody VerifierCreationDTO verifierCreationDTO) {

        try {
            VerifierDTO createdVerification = this.verifierService.createVerifier(verifierCreationDTO);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdVerification.getId()).toUri();
            return ResponseEntity.created(location).body(createdVerification);
        } catch (VerifierAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
	}

	/**
	 * {@code GET  /verifier/{apiKey} : Returns the verification with the given api key.
	 *
	 * @param apiKey the api key of the verification to retrieve.
	 * @return the {@link ResponseEntity}  with status {@code 200 (OK)} and with body
     *         the verification, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/verifier/{apiKey}")
	public ResponseEntity<VerifierDTO> getVerification(@PathVariable String apiKey) {
        log.debug("REST request to get hotel : {}", apiKey);
        Optional<VerifierDTO> verificationDTO = this.verifierService.getVerifier(apiKey);
        return ResponseUtil.wrapOrNotFound(verificationDTO);
    }
}
