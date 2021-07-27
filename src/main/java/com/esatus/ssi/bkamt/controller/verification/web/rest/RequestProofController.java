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
import com.esatus.ssi.bkamt.controller.verification.service.ProofService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller for connecting with the Indy / Aries hotel agent.
 */
@Tag(name = "Proof Requests", description = "Handle proof requests from scanned QR codes")
@RestController
@RequestMapping("/api")
public class RequestProofController {

    private final Logger log = (Logger) LoggerFactory.getLogger(RequestProofController.class);

    @Autowired
    ProofService proofService;

    // The method for requesting a connectionless proof
    @GetMapping(value = "/proof")
    public ResponseEntity<Void> sendRedirect(@RequestParam(name = "hotelId") String hotelId, @RequestParam(name = "deskId") String deskId) throws JsonProcessingException {

        log.debug("REST request to log-in at desk {} and hotel {}", deskId, hotelId);

        URI proofURI = this.proofService.getProofURI(hotelId, deskId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(proofURI);

        return new ResponseEntity<Void>(httpHeaders, HttpStatus.TEMPORARY_REDIRECT);
    }

}
