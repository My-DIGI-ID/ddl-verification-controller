/*
 * Copyright 2021 Bundesrepublik Deutschland
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.esatus.ssi.bkamt.controller.verification.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.esatus.ssi.bkamt.controller.verification.domain.Verifier;
import com.esatus.ssi.bkamt.controller.verification.models.VerificationRequestMetadata;
import com.esatus.ssi.bkamt.controller.verification.service.VerificationRequestService;
import com.esatus.ssi.bkamt.controller.verification.service.VerifierService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationResponseDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.PresentationRequestsAlreadyExists;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing verifiers.
 */
@Tag(name = "Verifier", description = "Manage Verifier")
@RestController
@RequestMapping("/api")
@SecurityRequirements({@SecurityRequirement(name = "X-API-Key")})
public class VerifierController {

  private final Logger log = LoggerFactory.getLogger(VerifierController.class);

  @Autowired
  VerifierService verifierService;

  @Autowired
  VerificationRequestService verificationRequestService;

  @Value("${ssibk.verification.controller.endpoint}")
  private String endpoint;

  private static final String REQUEST_PATH = "/api/proof?verificationId=";

  /**
   * {@code POST /init} : Initialize verification request
   *
   * @param verificationRequestMetadata the verification to create
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} with the verification details or with status
   *         {@code 400 (Bad Request)} {@code 400 (Bad Request)} if a presentation request with the given name does
   *         already exist.
   */
  @PostMapping("/init")
  public ResponseEntity<VerificationResponseDTO> createPresentationRequest(
      @RequestHeader(value = "X-API-KEY") String verifierApiKey,
      @Valid @RequestBody VerificationRequestMetadata verificationRequestMetadata) throws URISyntaxException {
    boolean isMetaDataCompliant = verifierService.checkMetaDataCompliance(verificationRequestMetadata);

    if (!isMetaDataCompliant) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Meta data compliance check failed");
    }

    Optional<Verifier> verifierOptional = verifierService.getOneByApiKey(verifierApiKey);

    if (verifierOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    try {
      VerificationRequestDTO createdVerificationRequest = this.verificationRequestService
          .createVerificationRequest(verificationRequestMetadata, verifierOptional.get().getId());

      String verificationId = createdVerificationRequest.getVerificationId();

      VerificationResponseDTO response = new VerificationResponseDTO();
      response.setUri(new URI(endpoint + REQUEST_PATH + verificationId));
      response.setVerificationId(verificationId);

      return ResponseEntity.ok().body(response);
    } catch (PresentationRequestsAlreadyExists e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  /**
   * {@code POST  /invalidate} : Invalidates metadata
   *
   * @param verificationId id of the verification to invalidate the meta data for
   * @return status {@code 200 (Ok)} when the invalidation was successful or with status {@code 400 (Bad Request)} when
   *         the invalidation failed
   */
  @PostMapping("/invalidate")
  public ResponseEntity<Void> invalidateVerification(@RequestHeader(value = "X-API-KEY") String verifierApiKey,
      @RequestParam(name = "verificationId") String verificationId) {
    log.debug("REST request to invalid meta data for verification with id {}", verificationId);

    boolean isVerificationIdCompliant = verificationRequestService.checkVerificationIdCompliance(verificationId);

    if (!isVerificationIdCompliant) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "VerificationId compliance check failed");
    }

    Optional<Verifier> verifierOptional = verifierService.getOneByApiKey(verifierApiKey);
    Optional<VerificationRequestDTO> verificationRequestOptional =
        verificationRequestService.getByVerificationId(verificationId);

    if (verifierOptional.isEmpty() || verificationRequestOptional.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    String verifier = verifierOptional.get().getId();
    String verificationVerifier = verificationRequestOptional.get().getVerifier();

    if (!verifier.equals(verificationVerifier)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    try {
      verifierService.invalidateVerification(verificationId);
    } catch (Exception e) {
      return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<Void>(HttpStatus.OK);
  }
}
