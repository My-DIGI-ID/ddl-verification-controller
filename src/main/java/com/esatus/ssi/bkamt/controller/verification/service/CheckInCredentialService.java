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

package com.esatus.ssi.bkamt.controller.verification.service;

import java.util.List;
import java.util.Optional;
import com.esatus.ssi.bkamt.controller.verification.domain.CheckInCredential;
import com.esatus.ssi.bkamt.controller.verification.service.dto.CheckInCredentialDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.CorporateIdDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.MasterIdDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.CheckinCredentialNotFoundException;

public interface CheckInCredentialService {

    List<CheckInCredentialDTO> getDeskCredentials(String deskId);

    void createCheckInCredential(String hotelId, String deskId, String presentationExchangeId);

    CheckInCredential updateCheckinCredential(String presentationExchangeId, MasterIdDTO masterid, CorporateIdDTO corporateIdDTO) throws CheckinCredentialNotFoundException;

    CheckInCredential updateValidity(String presentationExchangeId, boolean valid) throws CheckinCredentialNotFoundException;

    Optional<CheckInCredentialDTO> getCheckInCredentialById(String id);
}
