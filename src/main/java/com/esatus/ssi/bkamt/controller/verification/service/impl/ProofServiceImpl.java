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

import java.net.URI;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.esatus.ssi.bkamt.agent.client.model.AllOfIndyProofReqAttrSpecNonRevoked;
import com.esatus.ssi.bkamt.agent.client.model.IndyProofReqAttrSpec;
import com.esatus.ssi.bkamt.agent.client.model.IndyProofRequest;
import com.esatus.ssi.bkamt.agent.client.model.V10PresentationCreateRequestRequest;
import com.esatus.ssi.bkamt.agent.client.model.V10PresentationExchange;
import com.esatus.ssi.bkamt.agent.model.Base64Payload;
import com.esatus.ssi.bkamt.agent.model.ConnectionlessProofRequest;
import com.esatus.ssi.bkamt.controller.verification.client.AgentClient;
import com.esatus.ssi.bkamt.controller.verification.client.model.EmptyDTO;
import com.esatus.ssi.bkamt.controller.verification.client.model.Presentation;
import com.esatus.ssi.bkamt.controller.verification.client.model.ProofRequestDict;
import com.esatus.ssi.bkamt.controller.verification.client.model.ProofRequestService;
import com.esatus.ssi.bkamt.controller.verification.client.model.ProofRequestThread;
import com.esatus.ssi.bkamt.controller.verification.client.model.RequestPresentationAttach;
import com.esatus.ssi.bkamt.controller.verification.client.model.RevealedAttrValuesMasterId;
import com.esatus.ssi.bkamt.controller.verification.service.NotificationService;
import com.esatus.ssi.bkamt.controller.verification.service.ProofService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.MasterIdDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.WebhookPresentProofDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProofServiceImpl implements ProofService {

  private final Logger log = LoggerFactory.getLogger(ProofServiceImpl.class);

  @Autowired
  AgentClient acapyClient;

  @Autowired
  NotificationService notificationService;

  private @Value("${ssibk.hotel.controller.agent.apikey}") String apikey;
  private @Value("${ssibk.hotel.controller.agent.endpoint}") String agentEndpoint;
  private @Value("${ssibk.hotel.controller.agent.endpointName}") String agentEndpointName;
  private @Value("${ssibk.hotel.controller.agent.recipientkey}") String agentRecipientKey;
  private @Value("${ssibk.hotel.controller.agent.masterid.credential_definition_ids}") String masterIdCredDefIdsString;

  private static final String DIDCOMM_URL = "didcomm://example.org?m=";
  private static final String ARIES_MESSAGE_TYPE =
      "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/present-proof/1.0/request-presentation";
  private static final String ARIES_ATTACH_ID = "libindy-request-presentation-0";

  SecureRandom secureRandom = new SecureRandom();

  @Override
  public URI getProofURI(String hotelId, String deskId) {

    // prepare a proof request DTO and send it to the agent
    V10PresentationCreateRequestRequest connectionlessProofCreationRequest = this.prepareConnectionlessProofRequest();
    V10PresentationExchange proofResponseDTO =
        this.acapyClient.createProofRequest(apikey, connectionlessProofCreationRequest);
    log.debug("agent created a proof request: {}", proofResponseDTO);

    // prepare a connectionless proof request
    ConnectionlessProofRequest connectionlessProofRequest = this.prepareConnectionlessProofRequest(proofResponseDTO);

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

  private V10PresentationCreateRequestRequest prepareConnectionlessProofRequest() {

    Map<String, IndyProofReqAttrSpec> requestedAttributes = new HashMap<>();
    this.addMasterIdAttributes(requestedAttributes);

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

  private void addMasterIdAttributes(Map<String, IndyProofReqAttrSpec> requestedAttributes) {
    // Restriction regarding CredDefs for masterId
    String[] masterIdCredDefIds = masterIdCredDefIdsString.split(",");
    List<Map<String, String>> masterIdRestrictions = new ArrayList<Map<String, String>>(masterIdCredDefIds.length);
    for (int i = 0; i < masterIdCredDefIds.length; i++) {
      Map<String, String> temp = new HashMap<String, String>();
      temp.put("cred_def_id", masterIdCredDefIds[i]);
      masterIdRestrictions.add(temp);
    }

    IndyProofReqAttrSpec proofRequestMasterId = new IndyProofReqAttrSpec();
    List<String> names = Arrays.asList("firstName", "familyName", "addressStreet", "addressZipCode", "addressCountry",
        "addressCity", "dateOfExpiry", "dateOfBirth");
    proofRequestMasterId.setNames(names);

    // Restriction regarding Revocation
    AllOfIndyProofReqAttrSpecNonRevoked nonRevokedRestriction = new AllOfIndyProofReqAttrSpecNonRevoked();
    nonRevokedRestriction.setTo((int) Instant.now().getEpochSecond());

    proofRequestMasterId.setNonRevoked(nonRevokedRestriction);
    proofRequestMasterId.setRestrictions(masterIdRestrictions);

    requestedAttributes.put("masterId", proofRequestMasterId);
  }


  @Override
  public void handleProofWebhook(WebhookPresentProofDTO webhookPresentProofDTO) {

    log.debug("presentation exchange record is in state {}", webhookPresentProofDTO.getState());

    String presentationExchangeId = webhookPresentProofDTO.getPresentationExchangeId();

    if (webhookPresentProofDTO.getState().equals("verified")) {

    } else if (webhookPresentProofDTO.getState().equals("presentation_received")) {

    } else {
      log.debug("ignore this state");
    }
  }

  private MasterIdDTO createMasterIdDTO(V10PresentationExchange proofRecordDTO) {
    MasterIdDTO masterId = new MasterIdDTO();
    this.log.debug(proofRecordDTO.toString());

    ObjectMapper mapper = new ObjectMapper();

    try {
      // TODO if it is a map, use mapper.convertValue!
      Presentation presentation = mapper.readValue(proofRecordDTO.getPresentation().toString(), Presentation.class);

      RevealedAttrValuesMasterId values =
          presentation.getRequestedProof().getRevealedAttrGroups().getMasterId().getValues();
      masterId.setFirstName(values.getFirstName().getRaw());
      masterId.setFamilyName(values.getFamilyName().getRaw());
      masterId.setAddressStreet(values.getAddressStreet().getRaw());
      masterId.setAddressZipCode(values.getAddressZipCode().getRaw());
      masterId.setAddressCity(values.getAddressCity().getRaw());
      masterId.setAddressCountry(values.getAddressCountry().getRaw());
      masterId.setDateOfExpiryFromString(values.getDateOfExpiry().getRaw());
      masterId.setDateOfBirthFromString(values.getDateOfBirth().getRaw());

      return masterId;
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }
}
