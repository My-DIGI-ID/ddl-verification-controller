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
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.esatus.ssi.bkamt.controller.verification.service.ProofService;
import com.esatus.ssi.bkamt.controller.verification.service.RequestPresentationValidationService;
import com.esatus.ssi.bkamt.controller.verification.service.VerificationRequestService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller for creating a presentation request
 */
@Tag(name = "Proof Requests", description = "Handle proof requests from scanned QR codes")
@RestController
@RequestMapping("/api")
public class RequestProofController {

  private final Logger log = LoggerFactory.getLogger(RequestProofController.class);

  @Autowired
  ProofService proofService;

  @Autowired
  VerificationRequestService verificationRequestService;

  @Autowired
  RequestPresentationValidationService requestPresentationValidationService;

  /**
   * {@code POST  /proof} : Invalidates metadata
   *
   * @param verificationId id of the verification
   * @return status {@code 307 (Temporary Redirect)} when the proof was successfully created, with status
   *         {@code 500 (Internal Server Error)} when creation failed or {@code 404 (Not found)} when there is no
   *         verification with the given verification id in the database
   */
  @GetMapping(value = "/proof", produces = "application/json")
  public ResponseEntity sendRedirect(@RequestParam(name = "verificationId") String verificationId) {
    log.debug("REST request to create a presentation request for verificationId {}", verificationId);

    boolean isVerificationIdCompliant = verificationRequestService.checkVerificationIdCompliance(verificationId);

    if (!isVerificationIdCompliant) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "VerificationId compliance check failed");
    }

    Optional<VerificationRequestDTO> verificationRequest =
        verificationRequestService.getByVerificationId(verificationId);

    if (verificationRequest.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    try {
      URI proofURI = this.proofService.createProofRequest(verificationId);
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.setLocation(proofURI);

      return new ResponseEntity<>(httpHeaders, HttpStatus.TEMPORARY_REDIRECT);
    } catch (Exception e) {
      log.debug("Error creating proof request");
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
