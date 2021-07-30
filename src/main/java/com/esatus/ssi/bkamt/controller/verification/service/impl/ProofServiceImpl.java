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

package com.esatus.ssi.bkamt.controller.verification.service.impl;

import com.esatus.ssi.bkamt.agent.client.model.IndyProofReqAttrSpec;
import com.esatus.ssi.bkamt.agent.client.model.IndyProofRequest;
import com.esatus.ssi.bkamt.agent.client.model.V10PresentationCreateRequestRequest;
import com.esatus.ssi.bkamt.agent.client.model.V10PresentationExchange;
import com.esatus.ssi.bkamt.agent.model.Base64Payload;
import com.esatus.ssi.bkamt.agent.model.ConnectionlessProofRequest;
import com.esatus.ssi.bkamt.controller.verification.client.AgentClient;
import com.esatus.ssi.bkamt.controller.verification.client.model.*;
import com.esatus.ssi.bkamt.controller.verification.models.Data;
import com.esatus.ssi.bkamt.controller.verification.models.Data__1;
import com.esatus.ssi.bkamt.controller.verification.models.VerificationResponse;
import com.esatus.ssi.bkamt.controller.verification.service.*;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.WebhookPresentProofDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.MetaDataInvalidException;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.VerificationNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProofServiceImpl implements ProofService {

    private final Logger log = LoggerFactory.getLogger(ProofServiceImpl.class);

    @Autowired
    AgentClient acapyClient;

    @Autowired
    VerifierService verifierService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    MetaDataValidator metaDataValidator;

    @Autowired
    VerificationRequestService verificationRequestService;

    private @Value("${ssibk.verification.controller.agent.apikey}")
    String apikey;
    private @Value("${ssibk.verification.controller.agent.endpoint}")
    String agentEndpoint;
    private @Value("${ssibk.verification.controller.agent.endpointName}")
    String agentEndpointName;
    private @Value("${ssibk.verification.controller.agent.recipientkey}")
    String agentRecipientKey;
    private @Value("${ssibk.verification.controller.agent.masterid.credential_definition_ids}")
    String masterIdCredDefIdsString;

    private static final String DIDCOMM_URL = "didcomm://example.org?m=";
    private static final String ARIES_MESSAGE_TYPE =
        "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/present-proof/1.0/request-presentation";
    private static final String ARIES_ATTACH_ID = "libindy-request-presentation-0";

    SecureRandom secureRandom = new SecureRandom();

    @Override
    public URI createProofRequest(String verificationId) throws VerificationNotFoundException {

        var verificationRequestOptional = verificationRequestService.getByVerificationId(verificationId);
        var verificationRequest = verificationRequestOptional.get();

        // prepare a proof request DTO and send it to the agent
        V10PresentationCreateRequestRequest connectionlessProofCreationRequest = this.prepareConnectionlessProofRequest(verificationRequest);
        V10PresentationExchange proofResponseDTO = this.acapyClient.createProofRequest(apikey, connectionlessProofCreationRequest);
        log.debug("agent created a proof request: {}", proofResponseDTO);

        // prepare a connectionless proof request
        ConnectionlessProofRequest connectionlessProofRequest = this.prepareConnectionlessProofRequest(proofResponseDTO);

        String threadId = proofResponseDTO.getThreadId();
        verificationRequestService.updateThreadId(verificationId, threadId);

        try {
            ObjectMapper mapper = new ObjectMapper();
            log.debug(mapper.writeValueAsString(connectionlessProofRequest));
            // Base64 encode the connectionless proof request
            String encodedUrl =
                Base64.getEncoder().encodeToString((mapper.writeValueAsString(connectionlessProofRequest)).getBytes());

            // return a URI that is consumable by the wallet app
            return URI.create(DIDCOMM_URL + encodedUrl);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private ConnectionlessProofRequest prepareConnectionlessProofRequest(V10PresentationExchange proofResponseDTO) {
        String threadId = proofResponseDTO.getThreadId();
        ConnectionlessProofRequest connectionlessProofRequest = new ConnectionlessProofRequest();
        connectionlessProofRequest.setId(threadId);
        connectionlessProofRequest.setType(ARIES_MESSAGE_TYPE);

        RequestPresentationAttach requestPresentationAttach = new RequestPresentationAttach();
        requestPresentationAttach.setId(ARIES_ATTACH_ID);
        requestPresentationAttach.setMimeType(MediaType.APPLICATION_JSON_VALUE);

        ProofRequestDict proofRequestDict =
            new ObjectMapper().convertValue(proofResponseDTO.getPresentationRequestDict(), ProofRequestDict.class);

        Base64Payload base64Payload = new Base64Payload();
        base64Payload.setBase64(proofRequestDict.getRequestPresentationsAttach()[0].getData().getBase64());
        requestPresentationAttach.setData(base64Payload);

        RequestPresentationAttach[] requestPresentationAttaches = new RequestPresentationAttach[1];
        requestPresentationAttaches[0] = requestPresentationAttach;

        connectionlessProofRequest.setRequestPresentationAttach(requestPresentationAttaches);

        ProofRequestService proofRequestService = new ProofRequestService();
        String[] keys = new String[1];
        keys[0] = this.agentRecipientKey;
        proofRequestService.setRecipientKeys(keys);
        String[] routingKeys = new String[0];
        proofRequestService.setRoutingKeys(routingKeys);
        proofRequestService.setServiceEndpoint(agentEndpoint);
        proofRequestService.setEndpointName(agentEndpointName);
        connectionlessProofRequest.setService(proofRequestService);

        ProofRequestThread proofRequestThread = new ProofRequestThread();
        EmptyDTO empty = new EmptyDTO();
        proofRequestThread.setReceivedOrders(empty);
        proofRequestThread.setThreadId(threadId);
        connectionlessProofRequest.setThread(proofRequestThread);

        return connectionlessProofRequest;
    }

    private V10PresentationCreateRequestRequest prepareConnectionlessProofRequest(VerificationRequestDTO verificationRequest) {
        Map<String, IndyProofReqAttrSpec> requestedAttributes = createRequestedAttributes(verificationRequest);

        // Composing the proof request
        IndyProofRequest proofRequest = new IndyProofRequest();
        proofRequest.setName("Proof request");
        proofRequest.setRequestedPredicates(new HashMap<>());
        proofRequest.setRequestedAttributes(requestedAttributes);
        proofRequest.setVersion("0.1");
        int nonce = secureRandom.nextInt();
        proofRequest.setNonce(String.valueOf(nonce));

        V10PresentationCreateRequestRequest connectionlessProofCreationRequest = new V10PresentationCreateRequestRequest();
        connectionlessProofCreationRequest.setComment("string");
        connectionlessProofCreationRequest.setProofRequest(proofRequest);

        return connectionlessProofCreationRequest;
    }

    private Map<String, IndyProofReqAttrSpec> createRequestedAttributes(VerificationRequestDTO verificationRequest) {
        // TODO: Add additional attributes?
        return new HashMap<String, IndyProofReqAttrSpec>();
    }

    @Override
    public void handleProofWebhook(WebhookPresentProofDTO webhookPresentProofDTO) throws VerificationNotFoundException, MetaDataInvalidException {

        log.debug("presentation exchange record is in state {}", webhookPresentProofDTO.getState());

        String currentState = webhookPresentProofDTO.getState();

        switch (currentState) {
            case "verified":
                handleVerifiedState(webhookPresentProofDTO);
            case "presentation_received":
                handlePresentationReceivedState(webhookPresentProofDTO);
                return;
            default:
                log.debug("ignore state {}", currentState);
        }
    }

    private void handlePresentationReceivedState(WebhookPresentProofDTO webhookPresentProofDTO) {
        log.debug("handle presentation received state {}", webhookPresentProofDTO.getState());
    }

    private void handleVerifiedState(WebhookPresentProofDTO webhookPresentProofDTO) throws VerificationNotFoundException, MetaDataInvalidException {
        String threadId = webhookPresentProofDTO.getThreadId();
        Optional<VerificationRequestDTO> vr = verificationRequestService.getByThreadId(threadId);

        if (vr.isEmpty()) {
            throw new VerificationNotFoundException();
        }

        VerificationRequestDTO verificationRequest = vr.get();

        var metaDataValid = metaDataValidator.validateMetaData(verificationRequest);

        if(!metaDataValid) {
            throw new MetaDataInvalidException();
        }


        VerificationResponse response = buildVerificationResponse(verificationRequest);

        String callbackUrl = verificationRequest.getCallbackUrl();
        this.notificationService.executeCallback(callbackUrl, response);
    }

    private VerificationResponse buildVerificationResponse(VerificationRequestDTO verificationRequest) {
        VerificationResponse response = new VerificationResponse();
        response.setCode(200);
        response.setVerified(true);

        Data__1 data1 = buildResponseData(verificationRequest);

        response.setData(data1);
        return response;
    }

    private Data__1 buildResponseData(VerificationRequestDTO verificationRequest) {
        Data data = verificationRequest.getData();
        Data__1 data1 = new Data__1();

        for (var additionalProperty : data.getAdditionalProperties().entrySet()) {
            data1.setAdditionalProperty(additionalProperty.getKey(), additionalProperty.getValue());
        }
        return data1;
    }
}
