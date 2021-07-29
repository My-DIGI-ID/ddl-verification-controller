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
            verificationRequest.setThreadId(verificationRequestDTO.getThreadId());

            return verificationRequest;

        }
    }

    public VerificationRequestDTO verificationRequestToVerificationRequestDTO(VerificationRequest verificationRequest) {
        if (verificationRequest == null) {
            return null;
        } else {
            VerificationRequestDTO verificationRequestDTO = new VerificationRequestDTO();
            verificationRequestDTO.setId(verificationRequest.getId());
            verificationRequestDTO.setThreadId(verificationRequest.getThreadId());

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
