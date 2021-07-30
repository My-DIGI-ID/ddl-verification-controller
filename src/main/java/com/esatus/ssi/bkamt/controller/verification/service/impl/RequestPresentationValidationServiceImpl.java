package com.esatus.ssi.bkamt.controller.verification.service.impl;

import com.esatus.ssi.bkamt.controller.verification.domain.RequestPresentationValidationResult;
import com.esatus.ssi.bkamt.controller.verification.models.Data;
import com.esatus.ssi.bkamt.controller.verification.service.RequestPresentationValidationService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import org.springframework.stereotype.Service;

/**
 * Service class for presentation request validation.
 */
@Service
public class RequestPresentationValidationServiceImpl implements RequestPresentationValidationService {
    @Override
    public RequestPresentationValidationResult Validate(VerificationRequestDTO verificationRequestDTO) {
        boolean untilDateValid = this.validateValidUntil();
        boolean dataValid = this.validateData(verificationRequestDTO.getData());

        if(!dataValid || !untilDateValid) {
            return new RequestPresentationValidationResult(false, "Validation of data failed");
        }

        return new RequestPresentationValidationResult(true, "Validation of data succeeded");
    }

    public boolean validateData(Data data) {
        return true;
    }

    private boolean validateValidUntil() {
        return true;
    }
}
