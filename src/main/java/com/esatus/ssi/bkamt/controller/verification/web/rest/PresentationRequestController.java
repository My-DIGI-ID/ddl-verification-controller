package com.esatus.ssi.bkamt.controller.verification.web.rest;

import com.esatus.ssi.bkamt.controller.verification.service.PresentationRequestService;
import com.esatus.ssi.bkamt.controller.verification.service.VerifierService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.PresentationRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerifierCreationDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerifierDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.PresentationRequestsAlreadyExists;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.VerifierAlreadyExistsException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * Controller for creating a new presentation request
 */
@Tag(name = "Presentation Requests", description = "Handle presentation requests requests from scanned QR codes")
@RestController
@RequestMapping("/api")
public class PresentationRequestController {
    private final Logger log = LoggerFactory.getLogger(PresentationRequestController.class);

    @Autowired
    PresentationRequestService presentationRequestService;

    /**
     * {@code POST /presentation-request} : Create a new verification
     *
     * @param presentationRequestDTO the verification to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         the body the new presentation requests, or with status
     *         {@code 400 (Bad Request)} {@code 400 (Bad Request)} if a presentation request
     *         with the given name does already exist.
     */
    @PostMapping("/presentation-request")
    public ResponseEntity<PresentationRequestDTO> createPresentationRequest(@Valid @RequestBody PresentationRequestDTO presentationRequestDTO) {

        try {
            PresentationRequestDTO createdPresentationRequest  = this.presentationRequestService.createPresentationRequest(presentationRequestDTO);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdPresentationRequest.getId()).toUri();
            return ResponseEntity.created(location).body(createdPresentationRequest);
        } catch (PresentationRequestsAlreadyExists e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
