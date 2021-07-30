package com.esatus.ssi.bkamt.controller.verification.service;

import com.esatus.ssi.bkamt.controller.verification.domain.RequestPresentationValidationResult;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;

public interface RequestPresentationValidationService {
    RequestPresentationValidationResult Validate(VerificationRequestDTO verificationRequestDTO);
}
