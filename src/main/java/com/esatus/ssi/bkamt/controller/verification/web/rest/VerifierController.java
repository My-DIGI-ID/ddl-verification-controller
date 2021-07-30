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

import com.esatus.ssi.bkamt.controller.verification.models.VerificationRequestMetadata;
import com.esatus.ssi.bkamt.controller.verification.service.VerificationRequestService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.PresentationRequestsAlreadyExists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.esatus.ssi.bkamt.controller.verification.service.VerifierService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

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

    @Autowired
    VerificationRequestService verificationRequestService;

    private static final String RETURN_URL = "didcomm://ssi.ddl.example.com/api/v1/proof?verificationId=";

    /**
     * {@code POST /init} : Initialize verification request
     *
     * @param verificationRequestMetadata the verification to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         the body the new presentation requests, or with status
     *         {@code 400 (Bad Request)} {@code 400 (Bad Request)} if a presentation request
     *         with the given name does already exist.
     */
    @PostMapping("/init")
    public URI createPresentationRequest(@Valid @RequestBody VerificationRequestMetadata verificationRequestMetadata) throws URISyntaxException {
        boolean isMetaDataCompliant = verifierService.chekMetaDataCompliance(verificationRequestMetadata);

        if(!isMetaDataCompliant) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Meta data compliance check failed");
        }

        try {
            VerificationRequestDTO createdVerificationRequest  = this.verificationRequestService.createVerificationRequest(verificationRequestMetadata);
            return new URI(RETURN_URL + createdVerificationRequest.getId());
        } catch (PresentationRequestsAlreadyExists e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

	/**
	 * {@code POST  /invalidate} : Invalidates metadata
	 *
	 * @param verificationId id of the verification to invalidate the meta data for
	 * @return status {@code 200 (Ok)} when the invalidation was successful or with status
     *         {@code 400 (Bad Request)} when the invalidation failed
	 */
	@PostMapping("/invalidate")
	public ResponseEntity<Void> invalidateVerification(@RequestParam(name = "verificationId") String verificationId) {
        log.debug("REST request to invalid meta data for verification with id {}", verificationId);

        try {
            verifierService.invalidateVerification(verificationId);
        } catch (Exception e) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
