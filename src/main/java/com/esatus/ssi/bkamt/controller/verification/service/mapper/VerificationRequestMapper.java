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

import com.esatus.ssi.bkamt.controller.verification.domain.VerificationRequest;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VerificationRequestMapper {
    public VerificationRequest presentationRequestDTOToPresentationRequest(VerificationRequestDTO verificationRequestDTO) {
        if (verificationRequestDTO == null) {
            return null;
        } else {
            VerificationRequest verificationRequest = new VerificationRequest();
            verificationRequest.setId(verificationRequestDTO.getId());
            verificationRequest.setPresentationExchangeId(verificationRequestDTO.getPresentationExchangeId());
            verificationRequest.setVerificationId(verificationRequestDTO.getVerificationId());
            verificationRequest.setData(verificationRequestDTO.getData());
            verificationRequest.setCallbackUrl(verificationRequestDTO.getCallbackUrl());

            return verificationRequest;

        }
    }

    public VerificationRequestDTO verificationRequestToVerificationRequestDTO(VerificationRequest verificationRequest) {
        if (verificationRequest == null) {
            return null;
        } else {
            VerificationRequestDTO verificationRequestDTO = new VerificationRequestDTO();
            verificationRequestDTO.setId(verificationRequest.getId());
            verificationRequestDTO.setPresentationExchangeId(verificationRequest.getPresentationExchangeId());
            verificationRequestDTO.setVerificationId(verificationRequest.getVerificationId());
            verificationRequestDTO.setData(verificationRequest.getData());
            verificationRequestDTO.setCallbackUrl(verificationRequest.getCallbackUrl());
            verificationRequestDTO.setVerifier(verificationRequest.getVerifier());

            return verificationRequestDTO;
        }
    }

    public List<VerificationRequestDTO> presentationRequestsToPresentationRequestsDTOs(List<VerificationRequest> verificationRequests) {
        return verificationRequests.stream().filter(Objects::nonNull).map(this::verificationRequestToVerificationRequestDTO).collect(Collectors.toList());
    }

    public List<VerificationRequest> presentationRequestDTOsToPresentationRequests(List<VerificationRequestDTO> verificationRequestDTOS) {
        return verificationRequestDTOS.stream().filter(Objects::nonNull).map(this::presentationRequestDTOToPresentationRequest).collect(Collectors.toList());
    }
}
