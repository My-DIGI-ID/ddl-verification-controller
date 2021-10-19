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

import java.util.Optional;
import com.esatus.ssi.bkamt.controller.verification.models.VerificationRequestMetadata;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.PresentationRequestsAlreadyExists;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.VerificationNotFoundException;

public interface VerificationRequestService {
  VerificationRequestDTO createVerificationRequest(VerificationRequestMetadata VerificationRequestMetadata,
      String verifier) throws PresentationRequestsAlreadyExists;

  Optional<VerificationRequestDTO> getById(String id);

  Optional<VerificationRequestDTO> getByPresentationExchangeId(String presentationExchangeId);

  Optional<VerificationRequestDTO> getByVerificationId(String verificationId);

  void updatePresentationExchangeId(String verificationId, String threadId) throws VerificationNotFoundException;

  void updateNonce(String verificationId, String generatedNonce) throws VerificationNotFoundException;

  boolean checkVerificationIdCompliance(String verificationId);
}
