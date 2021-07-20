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

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.esatus.ssi.bkamt.controller.verification.security.AuthoritiesConstants;
import com.esatus.ssi.bkamt.controller.verification.service.CheckInCredentialService;
import com.esatus.ssi.bkamt.controller.verification.service.EmitterService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.CheckInCredentialDTO;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing checkIn credentials.
 */
@Tag(name = "CheckInCredentials", description = "Manage your checkInCredentials")
@RestController
@RequestMapping("/api")
public class CheckInCredentialController {

    private final Logger log = LoggerFactory.getLogger(CheckInCredentialController.class);

    @Autowired
    private CheckInCredentialService checkInCredentialService;

    @Autowired
    private EmitterService emitterService;

    /**
     * {@code GET  /checkin-credentials} : get the checkin-credentials.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of checkin-credentials in body.
     */
    @GetMapping("/checkin-credentials")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<CheckInCredentialDTO>> getCheckInDesk(@RequestParam("deskId") String deskId) {
        log.debug("REST request to get checkin-credential");

        return ResponseEntity.ok(this.checkInCredentialService.getDeskCredentials(deskId));
    }

    /**
     * {@code GET  /checkin-credentials/id : get the "id" checkinCredential.
     *
     * @param id the id of the checkin credential to retrieve.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     */
    @GetMapping("/checkin-credentials/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CheckInCredentialDTO> getCheckInCredential(@PathVariable String id) {
        log.debug("REST request to get CheckInCredential : {}", id);
        Optional<CheckInCredentialDTO> checkInCredentialDTO = this.checkInCredentialService
                .getCheckInCredentialById(id);
        return ResponseUtil.wrapOrNotFound(checkInCredentialDTO);
    }

    @GetMapping("/checkin-credentials/subscribe")
    public SseEmitter subscribeToEvents(@RequestParam("hotelId") String hotelId,
            @RequestParam("deskId") String deskId, HttpServletResponse response) {
        log.debug("Subscribing with hotelId {} and deskId {}", hotelId, deskId);
        response.addHeader("X-Accel-Buffering", "no");
        return emitterService.createEmitter(hotelId, deskId);
    }
}
