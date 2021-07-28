package com.esatus.ssi.bkamt.controller.verification.web.rest;

import com.esatus.ssi.bkamt.controller.verification.service.ProofService;
import com.esatus.ssi.bkamt.controller.verification.service.VerificationRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * Controller for creating a presentation request
 */
@Tag(name = "Proof Requests", description = "Handle proof requests from scanned QR codes")
@RestController
@RequestMapping("/api")
public class PresentationRequestController {

    private final Logger log = (Logger) LoggerFactory.getLogger(PresentationRequestController.class);

    @Autowired
    ProofService proofService;

    @Autowired
    VerificationRequestService verificationRequestService;

    @PostMapping(value = "/presentation-request")
    public ResponseEntity<Void> sendRedirect(@RequestParam(name = "verificationId") String verificationId) throws JsonProcessingException {

        log.debug("REST request to create a presentation request for verificationid {}", verificationId );

        // We have to validate the data


        URI proofURI = this.proofService.getProofURI(verificationId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(proofURI);

        return new ResponseEntity<Void>(httpHeaders, HttpStatus.TEMPORARY_REDIRECT);
    }
}
