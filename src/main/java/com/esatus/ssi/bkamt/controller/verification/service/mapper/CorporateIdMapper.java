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
import com.esatus.ssi.bkamt.controller.verification.domain.CorporateId;
import com.esatus.ssi.bkamt.controller.verification.service.dto.CorporateIdDTO;

@Service
public class CorporateIdMapper {

    public CorporateIdDTO corporateIdToCorporateIdDTO(CorporateId corporateId) {
        if (corporateId == null) {
            return null;
        } else {

            CorporateIdDTO corporateIdDto = new CorporateIdDTO();
            corporateIdDto.setFirstName(corporateId.getFirstName());
            corporateIdDto.setFamilyName(corporateId.getFamilyName());
            corporateIdDto.setCompanyName(corporateId.getCompanyName());
            corporateIdDto.setCompanySubject(corporateId.getCompanySubject());
            corporateIdDto.setCompanyAddressCity(corporateId.getCompanyAddressCity());
            corporateIdDto.setCompanyAddressZipCode(corporateId.getCompanyAddressZipCode());
            corporateIdDto.setCompanyAddressStreet(corporateId.getCompanyAddressStreet());

            return corporateIdDto;
        }
    }

    public CorporateId corporateIdDTOToCorporateId(CorporateIdDTO corporateIdDTO) {
        if (corporateIdDTO == null) {
            return null;
        } else {

            CorporateId corporateId = new CorporateId();
            corporateId.setFirstName(corporateIdDTO.getFirstName());
            corporateId.setFamilyName(corporateIdDTO.getFamilyName());
            corporateId.setCompanyName(corporateIdDTO.getCompanyName());
            corporateId.setCompanySubject(corporateIdDTO.getCompanySubject());
            corporateId.setCompanyAddressCity(corporateIdDTO.getCompanyAddressCity());
            corporateId.setCompanyAddressZipCode(corporateIdDTO.getCompanyAddressZipCode());
            corporateId.setCompanyAddressStreet(corporateIdDTO.getCompanyAddressStreet());

            return corporateId;
        }
    }

}
