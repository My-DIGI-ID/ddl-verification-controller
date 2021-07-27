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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.esatus.ssi.bkamt.controller.verification.VerificationControllerApp;
import com.esatus.ssi.bkamt.controller.verification.client.AgentClient;
import com.esatus.ssi.bkamt.controller.verification.service.ProofService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.WebhookPresentProofDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Webhooks for ACAPY",
    description = "https://github.com/hyperledger/aries-cloudagent-python/blob/master/AdminAPI.md#administration-api-webhooks")
@RestController
@RequestMapping("/topic")
public class WebhookController {

  private static final Logger log = LoggerFactory.getLogger(VerificationControllerApp.class);

  @Autowired
  AgentClient acapyClient;

  @Autowired
  ProofService proofService;

  // TODO: Do we need the @Operation here? Routes are globally protected via AuthManager.
  @PostMapping("/present_proof")
  @Operation(security = @SecurityRequirement(name = "X-API-Key"))
  public ResponseEntity<Void> onProofRequestWebhook(@RequestBody WebhookPresentProofDTO webhookPresentProofDTO)
      throws JsonProcessingException {

    log.debug("Webhook for proof request with thread_id {}", webhookPresentProofDTO.getThreadId());
    log.debug("State of the proof: {}", webhookPresentProofDTO.getState());
    log.debug("Proof verified: {}", webhookPresentProofDTO.getVerified());

    this.proofService.handleProofWebhook(webhookPresentProofDTO);

    return ResponseEntity.noContent().build();
  }

}
