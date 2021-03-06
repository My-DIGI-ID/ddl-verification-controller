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

package com.esatus.ssi.bkamt.controller.verification.service;

import java.util.Date;
import com.esatus.ssi.bkamt.agent.client.model.V10PresentationExchange;
import com.esatus.ssi.bkamt.controller.verification.domain.RequestPresentationValidationResult;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.HardwareDidMalformedException;

public interface RequestPresentationValidationService {
  RequestPresentationValidationResult validatePresentationExchange(V10PresentationExchange presentationExchange,
      VerificationRequestDTO verificationRequest) throws HardwareDidMalformedException;

  boolean issueDateValid(String validUntil, String format, Date now, String validityValue);
}
