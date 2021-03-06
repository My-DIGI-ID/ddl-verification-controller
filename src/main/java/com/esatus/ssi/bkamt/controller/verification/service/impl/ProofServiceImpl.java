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

import static java.security.DrbgParameters.Capability.RESEED_ONLY;
import java.math.BigInteger;
import java.net.URI;
import java.security.DrbgParameters;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.esatus.ssi.bkamt.agent.model.CustomService;
import com.esatus.ssi.bkamt.agent.model.EmptyDTO;
import com.esatus.ssi.bkamt.agent.model.Presentation;
import com.esatus.ssi.bkamt.agent.model.ProofRequestDict;
import com.esatus.ssi.bkamt.agent.model.ProofRequestThread;
import com.esatus.ssi.bkamt.agent.model.RequestPresentationAttach;
import com.esatus.ssi.bkamt.controller.verification.client.AgentClient;
import com.esatus.ssi.bkamt.controller.verification.domain.RequestPresentationValidationResult;
import com.esatus.ssi.bkamt.controller.verification.models.Data;
import com.esatus.ssi.bkamt.controller.verification.models.Data__1;
import com.esatus.ssi.bkamt.controller.verification.models.VerificationResponse;
import com.esatus.ssi.bkamt.controller.verification.service.MetaDataValidator;
import com.esatus.ssi.bkamt.controller.verification.service.NotificationService;
import com.esatus.ssi.bkamt.controller.verification.service.ProofService;
import com.esatus.ssi.bkamt.controller.verification.service.RequestPresentationValidationService;
import com.esatus.ssi.bkamt.controller.verification.service.VerificationRequestService;
import com.esatus.ssi.bkamt.controller.verification.service.VerifierService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.WebhookPresentProofDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.HardwareDidMalformedException;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.MetaDataInvalidException;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.PresentationExchangeInvalidException;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.VerificationNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
  RequestPresentationValidationService requestPresentationValidationService;

  @Autowired
  VerificationRequestService verificationRequestService;

  private @Value("${ssibk.verification.controller.agent.apikey}") String apikey;
  private @Value("${ssibk.verification.controller.agent.endpoint}") String agentEndpoint;
  private @Value("${ssibk.verification.controller.agent.endpointName}") String agentEndpointName;
  private @Value("${ssibk.verification.controller.agent.recipientkey}") String agentRecipientKey;
  private @Value("${ssibk.verification.controller.agent.ddl.credential_definition_ids}") String[] ddlCredDefIdsString;
  private @Value("${ssibk.verification.controller.agent.ddl.requested_attributes}") String[] ddlRequestedAttributes;
  private @Value("${ssibk.verification.controller.hardware-binding}") boolean hardwareBinding;

  private static final String DIDCOMM_URL = "didcomm://example.org?m=";
  private static final String ARIES_MESSAGE_TYPE =
      "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/present-proof/1.0/request-presentation";
  private static final String ARIES_ATTACH_ID = "libindy-request-presentation-0";

  SecureRandom secureRandom = new SecureRandom();

  @Override
  public URI createProofRequest(String verificationId) throws VerificationNotFoundException {
    VerificationRequestDTO verificationRequest = getVerificationById(verificationId);
    V10PresentationCreateRequestRequest connectionlessProofCreationRequest =
        this.createPresentation(verificationRequest);
    V10PresentationExchange proofResponseDTO = sendProofRequest(connectionlessProofCreationRequest);
    ConnectionlessProofRequest connectionlessProofRequest = this.prepareConnectionlessProofRequest(proofResponseDTO);

    updateVerificationByPresentationExchangeId(verificationId, proofResponseDTO.getPresentationExchangeId());

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

  private VerificationRequestDTO getVerificationById(String verificationId) throws VerificationNotFoundException {
    var verificationRequestOptional = verificationRequestService.getByVerificationId(verificationId);

    if (verificationRequestOptional.isEmpty()) {
      throw new VerificationNotFoundException();
    }
    return verificationRequestOptional.get();
  }

  private V10PresentationExchange sendProofRequest(
      V10PresentationCreateRequestRequest connectionlessProofCreationRequest) {
    V10PresentationExchange proofResponseDTO = null;
    try {
      proofResponseDTO = this.acapyClient.createProofRequest(apikey, connectionlessProofCreationRequest);
      log.debug("agent created a proof request: {}", proofResponseDTO);
    } catch (Exception e) {
      log.debug(e.getMessage());
    }

    return proofResponseDTO;
  }

  private void updateVerificationByPresentationExchangeId(String verificationId, String threadId)
      throws VerificationNotFoundException {
    verificationRequestService.updatePresentationExchangeId(verificationId, threadId);
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

    CustomService service = new CustomService();
    service.setRecipientKeys(Collections.singletonList(this.agentRecipientKey));
    service.setRoutingKeys(Collections.emptyList());
    service.setServiceEndpoint(agentEndpoint);
    service.setEndpointName(agentEndpointName);
    connectionlessProofRequest.setService(service);

    ProofRequestThread proofRequestThread = new ProofRequestThread();
    EmptyDTO empty = new EmptyDTO();
    proofRequestThread.setReceivedOrders(empty);
    proofRequestThread.setThreadId(threadId);
    connectionlessProofRequest.setThread(proofRequestThread);

    return connectionlessProofRequest;
  }

  private V10PresentationCreateRequestRequest createPresentation(VerificationRequestDTO verificationRequest)
      throws VerificationNotFoundException {
    Map<String, IndyProofReqAttrSpec> requestedAttributes = createRequestedAttributes(verificationRequest);

    // Composing the proof request
    IndyProofRequest proofRequest = new IndyProofRequest();
    proofRequest.setName("Proof request");
    proofRequest.setRequestedPredicates(new HashMap<>());
    proofRequest.setRequestedAttributes(requestedAttributes);
    proofRequest.setVersion("0.1");
    String generatedNonce = generateNonce(80);
    proofRequest.setNonce(generatedNonce);

    // Update the verification record with the created nonce
    this.verificationRequestService.updateNonce(verificationRequest.getVerificationId(), generatedNonce);

    V10PresentationCreateRequestRequest connectionlessProofCreationRequest = new V10PresentationCreateRequestRequest();
    connectionlessProofCreationRequest.setComment("string");
    connectionlessProofCreationRequest.setProofRequest(proofRequest);

    return connectionlessProofCreationRequest;
  }

  /**
   * Generate a decimal nonce, using SecureRandom and DRBG algorithm
   *
   * @param numberOfBits The length of the nonce in bit
   * @return A string representation of the nonce
   */
  public String generateNonce(int numberOfBits) {
    byte[] nonce = new byte[numberOfBits / 8];
    SecureRandom rand = null;
    try {
      rand = SecureRandom.getInstance("DRBG", DrbgParameters.instantiation(128, RESEED_ONLY, null));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    if (rand != null) {
      rand.nextBytes(nonce);
    }
    // 2021-09-11: Fix to generate only non-negative values. Kudos to @PaulWen
    BigInteger r = new BigInteger(1, nonce);

    return r.toString();
  }

  private void addDDLAttributes(Map<String, IndyProofReqAttrSpec> requestedAttributes) {
    List<Map<String, String>> ddlRestrictions = new ArrayList<>(ddlCredDefIdsString.length);

    log.debug("addDDLAttributes: " + ddlCredDefIdsString);

    for (String s : ddlCredDefIdsString) {
      Map<String, String> temp = new HashMap<>();
      temp.put("cred_def_id", s);
      ddlRestrictions.add(temp);
    }

    IndyProofReqAttrSpec proofRequestDdl = new IndyProofReqAttrSpec();
    // ArrayList<String> reqAttributes = (ArrayList<String>) Arrays.asList(ddlRequestedAttributes);
    ArrayList<String> reqAttributes = new ArrayList<>(Arrays.asList(ddlRequestedAttributes));
    proofRequestDdl.setNames(reqAttributes);

    if (hardwareBinding) {
       reqAttributes.add("hardwareDid");
    }

    // Restriction regarding Revocation
    AllOfIndyProofReqAttrSpecNonRevoked nonRevokedRestriction = new AllOfIndyProofReqAttrSpecNonRevoked();
    // nonRevokedRestriction.setFrom(0);
    nonRevokedRestriction.setTo((int) Instant.now().getEpochSecond());

    proofRequestDdl.setNonRevoked(nonRevokedRestriction);
    proofRequestDdl.setRestrictions(ddlRestrictions);
    requestedAttributes.put("ddl", proofRequestDdl);
  }

  private Map<String, IndyProofReqAttrSpec> createRequestedAttributes(VerificationRequestDTO verificationRequest) {
    Data data = verificationRequest.getData();

    if (data == null) {
      return new HashMap<String, IndyProofReqAttrSpec>();
    }

    var additionalProperties = verificationRequest.getData().getAdditionalProperties();

    var requestedAttributes = new HashMap<String, IndyProofReqAttrSpec>();

    for (String key : additionalProperties.keySet()) {
      var attribute = new IndyProofReqAttrSpec();
      attribute.setName(key.toLowerCase());
      requestedAttributes.put(attribute.getName(), attribute);
    }

    addDDLAttributes(requestedAttributes);
    addSelfAttestAttributes(verificationRequest, requestedAttributes);

    return requestedAttributes;
  }

  private void addSelfAttestAttributes(VerificationRequestDTO verificationRequest,
      HashMap<String, IndyProofReqAttrSpec> requestedAttributes) {
    List<Object> selfAttestedAttributes = verificationRequest.getSelfAttested();

    for (Object key : selfAttestedAttributes) {
      var attributeName = (String) key;
      var attribute = new IndyProofReqAttrSpec();
      attribute.setName(attributeName);
      requestedAttributes.put(attributeName, attribute);
    }

    if (hardwareBinding) {
      var attribute = new IndyProofReqAttrSpec();
      attribute.setName("hardwareDidProof");
      requestedAttributes.put("hardwareDidProof", attribute);
    }
  }

  @Override
  public void handleProofWebhook(WebhookPresentProofDTO webhookPresentProofDTO) throws VerificationNotFoundException,
      MetaDataInvalidException, PresentationExchangeInvalidException, HardwareDidMalformedException {
    log.debug("presentation exchange record is in state {}", webhookPresentProofDTO.getState());

    String currentState = webhookPresentProofDTO.getState();

    if ("verified".equals(currentState)) {
      log.debug("handle state {}", currentState);
      handleVerifiedState(webhookPresentProofDTO);
    }
  }

  private void handleVerifiedState(WebhookPresentProofDTO webhookPresentProofDTO)
      throws VerificationNotFoundException, MetaDataInvalidException, HardwareDidMalformedException {
    V10PresentationExchange presentationExchange =
        this.acapyClient.getProofRecord(apikey, webhookPresentProofDTO.getPresentationExchangeId());

    VerificationRequestDTO verificationRequest =
        getVerificationRequestByPresentationExchangeId(webhookPresentProofDTO.getPresentationExchangeId());

    RequestPresentationValidationResult validationResult =
        requestPresentationValidationService.validatePresentationExchange(presentationExchange, verificationRequest);

    String callbackUrl = verificationRequest.getCallbackUrl();

    if (!validationResult.isValid()) {
      var message = validationResult.getMessage();

      VerificationResponse response = new VerificationResponse();
      response.setCode(500);
      response.setVerified(false);
      response.setMessage(message);

      this.notificationService.executeCallback(callbackUrl, response);
    } else {

      validateVerification(verificationRequest);
      VerificationResponse response = buildVerificationResponse(presentationExchange);

      this.notificationService.executeCallback(callbackUrl, response);
      this.acapyClient.deleteProofRecord(apikey, webhookPresentProofDTO.getPresentationExchangeId());
    }
  }

  private void validateVerification(VerificationRequestDTO verificationRequest) throws MetaDataInvalidException {
    var metaDataValid = metaDataValidator.validateMetaData(verificationRequest);

    if (!metaDataValid) {
      throw new MetaDataInvalidException();
    }
  }

  private VerificationRequestDTO getVerificationRequestByPresentationExchangeId(String presentationExchangeId)
      throws VerificationNotFoundException {
    Optional<VerificationRequestDTO> vr =
        verificationRequestService.getByPresentationExchangeId(presentationExchangeId);

    if (vr.isEmpty())
      throw new VerificationNotFoundException();

    return vr.get();
  }

  private VerificationResponse buildVerificationResponse(V10PresentationExchange presentationExchange) {
    VerificationResponse response = new VerificationResponse();
    response.setCode(200);
    response.setVerified(true);

    Presentation presentation =
        new ObjectMapper().convertValue(presentationExchange.getPresentation(), Presentation.class);

    Data__1 data = new Data__1();

    Map<String, Map<String, String>> values = (Map<String, Map<String, String>>) presentation.getRequestedProof()
        .getRevealedAttrGroups().getDdl().getValues();

    Arrays.stream(ddlRequestedAttributes).forEach((String attributeName) -> {
      String attributeValue = values.get(attributeName).get("raw");

      if (!attributeValue.isEmpty() && !attributeValue.isBlank()) {
        data.setAdditionalProperty(attributeName, attributeValue);
      }
    });

    response.setData(data);
    return response;
  }

  // Is not currently supported by ID Wallet
  private Data__1 buildResponseData(VerificationRequestDTO verificationRequest) {
    Data data = verificationRequest.getData();
    Data__1 data1 = new Data__1();

    for (var additionalProperty : data.getAdditionalProperties().entrySet()) {
      data1.setAdditionalProperty(additionalProperty.getKey(), additionalProperty.getValue());
    }
    return data1;
  }
}
