package com.esatus.ssi.bkamt.controller.verification.service.impl;

import com.esatus.ssi.bkamt.controller.verification.models.Data;
import com.esatus.ssi.bkamt.controller.verification.service.MetaDataValidator;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;

public class MetaDataValidatorImpl implements MetaDataValidator {
    @Override
    public boolean validateMetaData(VerificationRequestDTO verificationRequest) {
        Data data = verificationRequest.getData();

        if(data == null) {
            return true;
        }

        if(data.getAdditionalProperties().size() == 0) {
            return true;
        }

        return true;
    }
}
