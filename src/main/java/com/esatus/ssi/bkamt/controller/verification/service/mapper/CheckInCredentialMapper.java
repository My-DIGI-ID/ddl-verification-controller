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

package com.esatus.ssi.bkamt.controller.verification.service.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.esatus.ssi.bkamt.controller.verification.domain.CheckInCredential;
import com.esatus.ssi.bkamt.controller.verification.service.dto.CheckInCredentialDTO;

@Service
public class CheckInCredentialMapper {

    @Autowired
    MasterIdMapper masterIdMapper;

    @Autowired
    CorporateIdMapper corporateIdMapper;

    public CheckInCredentialDTO checkInCredentialToCheckInCredentialDTO(CheckInCredential checkInCredential) {
        if (checkInCredential == null) {
            return null;
        } else {

            CheckInCredentialDTO checkInCredentialDTO = new CheckInCredentialDTO();
            checkInCredentialDTO.setId(checkInCredential.getId());
            checkInCredentialDTO.setHotelId(checkInCredential.getHotelId());
            checkInCredentialDTO.setDeskId(checkInCredential.getDeskId());
            checkInCredentialDTO.setScanDate(checkInCredential.getScanDate());
            checkInCredentialDTO.setMasterId(this.masterIdMapper.masterIdToMasterIdDTO(checkInCredential.getMasterId()));
            checkInCredentialDTO.setCorporateId(this.corporateIdMapper.corporateIdToCorporateIdDTO(checkInCredential.getCorporateId()));
            checkInCredentialDTO.setValid(checkInCredential.isValid());

            return checkInCredentialDTO;
        }
    }

    public List<CheckInCredentialDTO> checkInCredentialsToCheckInCredentialDTOs(List<CheckInCredential> checkInCredentials) {
        return checkInCredentials.stream().filter(Objects::nonNull).map(this::checkInCredentialToCheckInCredentialDTO).collect(Collectors.toList());
    }
}
