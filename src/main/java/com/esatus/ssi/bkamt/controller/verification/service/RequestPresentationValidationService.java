package com.esatus.ssi.bkamt.controller.verification.service;

import com.esatus.ssi.bkamt.controller.verification.domain.RequestPresentationValidationResult;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import com.mongodb.BasicDBObject;

import java.util.List;

public interface RequestPresentationValidationService {
    RequestPresentationValidationResult Validate(VerificationRequestDTO verificationRequestDTO);
    boolean validateData(List<BasicDBObject> metaData);

}
