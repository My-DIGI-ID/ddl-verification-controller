package com.esatus.ssi.bkamt.controller.verification.service.mapper;

import com.esatus.ssi.bkamt.controller.verification.domain.PresentationRequest;
import com.esatus.ssi.bkamt.controller.verification.service.dto.PresentationRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PresentationRequestMapper {
    public PresentationRequest presentationRequestDTOToPresentationRequest(PresentationRequestDTO presentationRequestDTO) {
        if (presentationRequestDTO == null) {
            return null;
        } else {
            PresentationRequest presentationRequest = new PresentationRequest();
            presentationRequest.setId(presentationRequestDTO.getId());
            presentationRequest.setThreadId(presentationRequestDTO.getThreadId());

            return presentationRequest;

        }
    }

    public PresentationRequestDTO presentationRequestToPresentationRequestDTO(PresentationRequest presentationRequest) {
        if (presentationRequest == null) {
            return null;
        } else {
            PresentationRequestDTO presentationRequestDTO = new PresentationRequestDTO();
            presentationRequestDTO.setId(presentationRequest.getId());
            presentationRequestDTO.setThreadId(presentationRequest.getThreadId());

            return presentationRequestDTO;
        }
    }

    public List<PresentationRequestDTO> presentationRequestsToPresentationRequestsDTOs(List<PresentationRequest> presentationRequests) {
        return presentationRequests.stream().filter(Objects::nonNull).map(this::presentationRequestToPresentationRequestDTO).collect(Collectors.toList());
    }

    public List<PresentationRequest> presentationRequestDTOsToPresentationRequests(List<PresentationRequestDTO> presentationRequestDTOS) {
        return presentationRequestDTOS.stream().filter(Objects::nonNull).map(this::presentationRequestDTOToPresentationRequest).collect(Collectors.toList());
    }

    public PresentationRequest presentationRequestFromId(String id) {
        if (id == null) {
            return null;
        }
        PresentationRequest presentationRequest = new PresentationRequest();
        presentationRequest.setId(id);
        return presentationRequest;
    }
}
