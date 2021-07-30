package com.esatus.ssi.bkamt.controller.verification.service;

import com.esatus.ssi.bkamt.controller.verification.models.VerificationRequestMetadata;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.PresentationRequestsAlreadyExists;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.VerificationNotFoundException;

import java.util.Optional;

public interface VerificationRequestService {
    VerificationRequestDTO createVerificationRequest(VerificationRequestMetadata VerificationRequestMetadata) throws PresentationRequestsAlreadyExists;
    Optional<VerificationRequestDTO> getById(String id);
    Optional<VerificationRequestDTO> getByThreadId(String threadId);
    void updateThreadId(String verificationId, String threadId) throws VerificationNotFoundException;
}
