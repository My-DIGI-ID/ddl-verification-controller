package com.esatus.ssi.bkamt.controller.verification.service.impl;

import com.esatus.ssi.bkamt.agent.client.model.V10PresentationExchange;
import com.esatus.ssi.bkamt.controller.verification.domain.RequestPresentationValidationResult;
import com.esatus.ssi.bkamt.controller.verification.service.RequestPresentationValidationService;
import org.springframework.stereotype.Service;

/**
 * Service class for presentation request validation.
 */
@Service
public class RequestPresentationValidationServiceImpl implements RequestPresentationValidationService {
    @Override
    public RequestPresentationValidationResult validatePresentationExchange(V10PresentationExchange presentationExchange) {
        return new RequestPresentationValidationResult(true, "Validation of data succeeded");
    }
}
