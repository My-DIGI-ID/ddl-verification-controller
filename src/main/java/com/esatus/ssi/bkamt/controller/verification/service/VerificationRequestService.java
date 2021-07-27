package com.esatus.ssi.bkamt.controller.verification.service;

import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.PresentationRequestsAlreadyExists;

public interface VerificationRequestService {
    VerificationRequestDTO createVerificationRequest(VerificationRequestDTO verificationRequestDTO) throws PresentationRequestsAlreadyExists;
}
