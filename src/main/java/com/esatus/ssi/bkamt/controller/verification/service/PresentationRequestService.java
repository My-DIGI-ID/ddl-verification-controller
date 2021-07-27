package com.esatus.ssi.bkamt.controller.verification.service;

import com.esatus.ssi.bkamt.controller.verification.service.dto.PresentationRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.PresentationRequestsAlreadyExists;

public interface PresentationRequestService {
    // TODO: Which errors can occur?
    PresentationRequestDTO createPresentationRequest(PresentationRequestDTO presentationRequestDTO) throws PresentationRequestsAlreadyExists;
}
