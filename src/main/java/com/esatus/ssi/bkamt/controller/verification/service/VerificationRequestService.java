package com.esatus.ssi.bkamt.controller.verification.service;

import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.PresentationRequestsAlreadyExists;

import java.util.Optional;

public interface VerificationRequestService {
    VerificationRequestDTO createVerificationRequest(VerificationRequestDTO verificationRequestDTO) throws PresentationRequestsAlreadyExists;
    Optional<VerificationRequestDTO> getById(String id);
    Optional<VerificationRequestDTO> getByThreadId(String threadId);
}
