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

import org.springframework.stereotype.Service;
import com.esatus.ssi.bkamt.controller.verification.domain.MasterId;
import com.esatus.ssi.bkamt.controller.verification.service.dto.MasterIdDTO;

@Service
public class MasterIdMapper {

    public MasterIdDTO masterIdToMasterIdDTO(MasterId masterId) {
        if (masterId == null) {
            return null;
        } else {

            MasterIdDTO masterIdDTO = new MasterIdDTO();
            masterIdDTO.setFirstName(masterId.getFirstName());
            masterIdDTO.setFamilyName(masterId.getFamilyName());
            masterIdDTO.setAddressCountry(masterId.getAddressCountry());
            masterIdDTO.setAddressCity(masterId.getAddressCity());
            masterIdDTO.setAddressZipCode(masterId.getAddressZipCode());
            masterIdDTO.setAddressStreet(masterId.getAddressStreet());
            masterIdDTO.setDateOfExpiry(masterId.getDateOfExpiry());
            masterIdDTO.setDateOfBirth(masterId.getDateOfBirth());

            return masterIdDTO;
        }
    }

    public MasterId masterIdDTOToMasterId(MasterIdDTO masterIdDTO) {
        if (masterIdDTO == null) {
            return null;
        } else {

            MasterId masterId = new MasterId();
            masterId.setFirstName(masterIdDTO.getFirstName());
            masterId.setFamilyName(masterIdDTO.getFamilyName());
            masterId.setAddressCountry(masterIdDTO.getAddressCountry());
            masterId.setAddressCity(masterIdDTO.getAddressCity());
            masterId.setAddressZipCode(masterIdDTO.getAddressZipCode());
            masterId.setAddressStreet(masterIdDTO.getAddressStreet());
            masterId.setDateOfExpiry(masterIdDTO.getDateOfExpiry());
            masterId.setDateOfBirth(masterIdDTO.getDateOfBirth());

            return masterId;
        }
    }
}
