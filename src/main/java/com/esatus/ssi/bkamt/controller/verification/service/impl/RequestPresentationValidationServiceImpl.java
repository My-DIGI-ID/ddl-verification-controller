package com.esatus.ssi.bkamt.controller.verification.service.impl;

import com.esatus.ssi.bkamt.controller.verification.domain.RequestPresentationValidationResult;
import com.esatus.ssi.bkamt.controller.verification.service.RequestPresentationValidationService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for presentation request validation.
 */
@Service
public class RequestPresentationValidationServiceImpl implements RequestPresentationValidationService {
    @Override
    public RequestPresentationValidationResult Validate(VerificationRequestDTO verificationRequestDTO) {
        // Unpack the data
        List<BasicDBObject> metaData = new ArrayList<BasicDBObject>();

        boolean dataValid = this.validateData(metaData);

        if(!dataValid) {
            return new RequestPresentationValidationResult(false, "Validation of data failed");
        }

        return new RequestPresentationValidationResult(true, "Validation of data succeeded");
    }

    @Override
    public boolean validateData(List<BasicDBObject> metaData) {
        return true;
    }
}
