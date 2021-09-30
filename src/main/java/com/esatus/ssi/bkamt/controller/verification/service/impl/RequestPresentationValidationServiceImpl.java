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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.esatus.ssi.bkamt.agent.client.model.V10PresentationExchange;
import com.esatus.ssi.bkamt.agent.model.Presentation;
import com.esatus.ssi.bkamt.controller.verification.domain.RequestPresentationValidationResult;
import com.esatus.ssi.bkamt.controller.verification.domain.ValidationResult;
import com.esatus.ssi.bkamt.controller.verification.service.HardwareDidValidationService;
import com.esatus.ssi.bkamt.controller.verification.service.RequestPresentationValidationService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service class for presentation request validation.
 */
@Service
public class RequestPresentationValidationServiceImpl implements RequestPresentationValidationService {

  @Autowired
  HardwareDidValidationService hardwareDidValidationService;

  @Value("${ssibk.verification.controller.expiryCheck.attribute}")
  private String expiryCheckAttribute;

  @Value("${ssibk.verification.controller.expiryCheck.format}")
  private String expiryCheckFormat;

  @Value("${ssibk.verification.controller.expiryCheck.validity}")
  private String expiryCheckValidity;

  @Value("${ssibk.verification.controller.hardware-binding}")
  private boolean hardwareBinding;

  @Override
  public RequestPresentationValidationResult validatePresentationExchange(V10PresentationExchange presentationExchange,
      VerificationRequestDTO verificationRequest) {

    Presentation presentation =
        new ObjectMapper().convertValue(presentationExchange.getPresentation(), Presentation.class);

    Map<String, Map<String, String>> values =
        (Map) presentation.getRequestedProof().getRevealedAttrGroups().getDdl().getValues();
    String issuedDate = values.get(expiryCheckAttribute).get("raw");

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    Date now = cal.getTime();

    boolean isValid = issueDateValid(issuedDate, expiryCheckFormat, now, "0");

    if (hardwareBinding && isValid) {
      RequestPresentationValidationResult verificationRequestHB =
          validateHardwareDid(presentationExchange, verificationRequest, presentation, values);

      if (verificationRequestHB != null) {
        return verificationRequestHB;
      }
    }

    if (isValid) {
      return new RequestPresentationValidationResult(true, "Verification succeeded");
    } else {
      return new RequestPresentationValidationResult(false, "Verification failed");
    }
  }

  /**
   * Validates the given hardware did. The hardware did "represents" the public key. The method gets the nonce from the
   * verification requests and concatenates it with 0x2. Then the hardwareDidValidationService is called to check if the
   * nonce from the initial verification request is the same nonce we get from the presentation. After this check
   * succeeds the service checks if the hardware did proof was secured with the hardware did
   *
   * @param presentationExchange
   * @param verificationRequest
   * @param presentation
   * @param values
   * @return
   */
  private RequestPresentationValidationResult validateHardwareDid(V10PresentationExchange presentationExchange,
      VerificationRequestDTO verificationRequest, Presentation presentation, Map<String, Map<String, String>> values) {

    Map<String, String> selfAttestedAttr = (Map) presentation.getRequestedProof().getSelfAttestedAttrs();
    String hardwareDIDProof = selfAttestedAttr.get("hardwareDidProof");
    Map<String, String> hardwareDidMap = values.get("hardwareDid");
    String hardwareDID = hardwareDidMap.get("raw");

    Map<String, String> presentationRequest = (Map<String, String>) presentationExchange.getPresentationRequest();
    String nonce = presentationRequest.get("nonce");

    if (!nonce.equals(verificationRequest.getNonce())) {
      return new RequestPresentationValidationResult(false,
          "Nonce validation failed. Nonce from request does not match nonce from the verification record for verification: "
              + verificationRequest.getVerificationId());
    }

    ValidationResult validationResult = hardwareDidValidationService.validate(hardwareDID, hardwareDIDProof, nonce);

    if (!validationResult.getSuccess()) {
      return new RequestPresentationValidationResult(false, "Verification of hardware did failed");
    }

    return null;
  }

  @Override
  public boolean issueDateValid(String issueDate, String format, Date now, String validityValue) {
    DateFormat simpleDateFormat = new SimpleDateFormat(format);

    try {
      Instant dateOfIssuingInstant = simpleDateFormat.parse(issueDate).toInstant();

      Date dateOfIssuing = Date.from(dateOfIssuingInstant);

      Instant start = now.toInstant().minus(Long.parseLong(validityValue), ChronoUnit.DAYS);
      Instant end = now.toInstant().plus(Long.parseLong("1"), ChronoUnit.DAYS);

      return dateOfIssuing.before(Date.from(end))
          && (dateOfIssuing.after(Date.from(start)) || dateOfIssuing.equals(Date.from(start)));
    } catch (ParseException e) {
      e.printStackTrace();
      return false;
    }
  }
}
