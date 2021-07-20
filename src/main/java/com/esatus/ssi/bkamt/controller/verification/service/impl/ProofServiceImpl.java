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
import com.esatus.ssi.bkamt.controller.verification.client.ACAPYClient;
import com.esatus.ssi.bkamt.controller.verification.client.model.Base64Payload;
import com.esatus.ssi.bkamt.controller.verification.client.model.ConnectionlessProofRequest;
import com.esatus.ssi.bkamt.controller.verification.client.model.EmptyDTO;
import com.esatus.ssi.bkamt.controller.verification.client.model.NonRevokedRestriction;
import com.esatus.ssi.bkamt.controller.verification.client.model.ProofRecordDTO;
import com.esatus.ssi.bkamt.controller.verification.client.model.ProofRequest;
import com.esatus.ssi.bkamt.controller.verification.client.model.ProofRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.client.model.ProofRequestProperty;
import com.esatus.ssi.bkamt.controller.verification.client.model.ProofRequestService;
import com.esatus.ssi.bkamt.controller.verification.client.model.ProofRequestThread;
import com.esatus.ssi.bkamt.controller.verification.client.model.ProofResponseDTO;
import com.esatus.ssi.bkamt.controller.verification.client.model.RequestPresentationAttach;
import com.esatus.ssi.bkamt.controller.verification.client.model.RequestedAttributes;
import com.esatus.ssi.bkamt.controller.verification.client.model.RequestedPredicates;
import com.esatus.ssi.bkamt.controller.verification.client.model.RevealedAttrValuesCorporateId;
import com.esatus.ssi.bkamt.controller.verification.client.model.RevealedAttrValuesMasterId;
import com.esatus.ssi.bkamt.controller.verification.domain.CheckInCredential;
import com.esatus.ssi.bkamt.controller.verification.service.CheckInCredentialService;
import com.esatus.ssi.bkamt.controller.verification.service.NotificationService;
import com.esatus.ssi.bkamt.controller.verification.service.ProofService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.CorporateIdDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.MasterIdDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.WebhookPresentProofDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.CheckinCredentialNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProofServiceImpl implements ProofService {

  private final Logger log = LoggerFactory.getLogger(ProofServiceImpl.class);

  @Autowired
  ACAPYClient acapyClient;

  @Autowired
  CheckInCredentialService checkInCredentialService;

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
    ProofRequestDTO connectionlessProofCreationRequest = this.prepareConnectionlessProofRequest();
    ProofResponseDTO proofResponseDTO = this.acapyClient.createProofRequest(apikey, connectionlessProofCreationRequest);
    log.debug("agent created a proof request: {}", proofResponseDTO);

    // create a new entry for this presentationExchangeId in the database
    this.checkInCredentialService.createCheckInCredential(hotelId, deskId,
        proofResponseDTO.getPresentationExchangeId());

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

  private ConnectionlessProofRequest prepareConnectionlessProofRequest(ProofResponseDTO proofResponseDTO) {
    String threadId = proofResponseDTO.getThreadId();
    ConnectionlessProofRequest connectionlessProofRequest = new ConnectionlessProofRequest();
    connectionlessProofRequest.setId(threadId);
    connectionlessProofRequest.setType(ARIES_MESSAGE_TYPE);

    RequestPresentationAttach requestPresentationAttach = new RequestPresentationAttach();
    requestPresentationAttach.setId(ARIES_ATTACH_ID);
    requestPresentationAttach.setMimeType(MediaType.APPLICATION_JSON_VALUE);
    Base64Payload base64Payload = new Base64Payload();
    base64Payload
        .setBase64(proofResponseDTO.getProofRequestDict().getRequestPresentationsAttach()[0].getData().getBase64());
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

  private ProofRequestDTO prepareConnectionlessProofRequest() {

    RequestedPredicates requestedPredicates = new RequestedPredicates();

    RequestedAttributes requestedAttributes = new RequestedAttributes();
    this.addMasterIdAttributes(requestedAttributes);

    // Composing the proof request
    ProofRequest proofRequest = new ProofRequest();
    proofRequest.setName("Proof request");
    proofRequest.setRequestedPredicates(requestedPredicates);
    proofRequest.setRequestedAttributes(requestedAttributes);
    proofRequest.setVersion("0.1");
    int nonce = secureRandom.nextInt();
    proofRequest.setNonce(String.valueOf(nonce));

    ProofRequestDTO connectionlessProofCreationRequest = new ProofRequestDTO();
    connectionlessProofCreationRequest.setComment("string");
    connectionlessProofCreationRequest.setProofRequest(proofRequest);

    return connectionlessProofCreationRequest;
  }

  private void addMasterIdAttributes(RequestedAttributes requestedAttributes) {
    // Restriction regarding CredDefs for masterId
    String[] masterIdCredDefIds = masterIdCredDefIdsString.split(",");
    List<Map<String, String>> masterIdRestrictions = new ArrayList<Map<String, String>>(masterIdCredDefIds.length);
    for (int i = 0; i < masterIdCredDefIds.length; i++) {
      Map<String, String> temp = new HashMap<String, String>();
      temp.put("cred_def_id", masterIdCredDefIds[i]);
      masterIdRestrictions.add(temp);
    }

    ProofRequestProperty proofRequestMasterId = new ProofRequestProperty();
    List<String> names = Arrays.asList("firstName", "familyName", "addressStreet", "addressZipCode", "addressCountry",
        "addressCity", "dateOfExpiry", "dateOfBirth");
    proofRequestMasterId.setNames(names);

    // Restriction regarding Revocation
    NonRevokedRestriction nonRevokedRestriction = new NonRevokedRestriction();
    nonRevokedRestriction.setFrom(0);
    nonRevokedRestriction.setTo((int) Instant.now().getEpochSecond());

    proofRequestMasterId.setNonRevokedRestriction(nonRevokedRestriction);
    proofRequestMasterId.setRestrictions(masterIdRestrictions);
    requestedAttributes.setMasterId(proofRequestMasterId);
  }


  @Override
  public void handleProofWebhook(WebhookPresentProofDTO webhookPresentProofDTO) {

    log.debug("presentation exchange record is in state {}", webhookPresentProofDTO.getState());

    String presentationExchangeId = webhookPresentProofDTO.getPresentationExchangeId();

    if (webhookPresentProofDTO.getState().equals("verified")) {
      boolean proofVerified =
          webhookPresentProofDTO.getVerified() != null && webhookPresentProofDTO.getVerified().equals("true");
      try {
        CheckInCredential checkInCredential =
            this.checkInCredentialService.updateValidity(presentationExchangeId, proofVerified);
        // inform subscribers about the new checkin credential
        this.notificationService.sendNotificationAboutNewCheckinCredentials(checkInCredential.getHotelId(),
            checkInCredential.getDeskId());
      } catch (CheckinCredentialNotFoundException e) {
        // log but do not rethrow
        log.error("A matching CheckInCredential was not found", e);
      } finally {
        // Delete proof presentation info from agent
        this.acapyClient.deleteProofRecord(apikey, presentationExchangeId);
      }
    } else if (webhookPresentProofDTO.getState().equals("presentation_received")) {
      log.debug("Update mongodb entry and delete information from agent");
      log.debug("Getting presentation information from the agent");

      // get the proof record from the agent
      ProofRecordDTO proofRecordDTO = this.acapyClient.getProofRecord(apikey, presentationExchangeId);

      // construct a corporateId out of the proof
      CorporateIdDTO corporateId = this.createCorporateIdDTO(proofRecordDTO);
      log.debug("Created corporate id: {}", corporateId);

      // construct a masterId out of the proof
      MasterIdDTO masterId = this.createMasterIdDTO(proofRecordDTO);
      log.debug("Created master id: {}", masterId);

      try {
        // Updating the mongo db entry with the data received via the proof
        this.checkInCredentialService.updateCheckinCredential(presentationExchangeId, masterId, corporateId);
      } catch (CheckinCredentialNotFoundException e) {
        // log but do not rethrow
        log.error("A matching CheckInCredential was not found", e);
      }
    } else {
      log.debug("ignore this state");
    }
  }

  private MasterIdDTO createMasterIdDTO(ProofRecordDTO proofRecordDTO) {
    MasterIdDTO masterId = new MasterIdDTO();
    this.log.debug(proofRecordDTO.toString());

    RevealedAttrValuesMasterId values =
        proofRecordDTO.getPresentation().getRequestedProof().getRevealedAttrGroups().getMasterId().getValues();
    masterId.setFirstName(values.getFirstName().getRaw());
    masterId.setFamilyName(values.getFamilyName().getRaw());
    masterId.setAddressStreet(values.getAddressStreet().getRaw());
    masterId.setAddressZipCode(values.getAddressZipCode().getRaw());
    masterId.setAddressCity(values.getAddressCity().getRaw());
    masterId.setAddressCountry(values.getAddressCountry().getRaw());
    masterId.setDateOfExpiryFromString(values.getDateOfExpiry().getRaw());
    masterId.setDateOfBirthFromString(values.getDateOfBirth().getRaw());

    return masterId;
  }

  private CorporateIdDTO createCorporateIdDTO(ProofRecordDTO proofRecordDTO) {
    CorporateIdDTO corporateId = new CorporateIdDTO();
    this.log.debug(proofRecordDTO.toString());

    RevealedAttrValuesCorporateId values =
        proofRecordDTO.getPresentation().getRequestedProof().getRevealedAttrGroups().getCorporateId().getValues();
    corporateId.setFirstName(values.getFirstName().getRaw());
    corporateId.setFamilyName(values.getLastName().getRaw());
    corporateId.setCompanyName(values.getFirmName().getRaw());
    corporateId.setCompanySubject(values.getFirmSubject().getRaw());
    corporateId.setCompanyAddressStreet(values.getFirmStreet().getRaw());
    corporateId.setCompanyAddressZipCode(values.getFirmPostalcode().getRaw());
    corporateId.setCompanyAddressCity(values.getFirmCity().getRaw());
    return corporateId;
  }

}
