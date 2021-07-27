package com.esatus.ssi.bkamt.controller.verification.service;

import com.esatus.ssi.bkamt.controller.verification.service.dto.PresentationRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.PresentationRequestsAlreadyExists;

public interface PresentationRequestService {
    PresentationRequestDTO createPresentationRequest(PresentationRequestDTO presentationRequestDTO) throws PresentationRequestsAlreadyExists;
}
