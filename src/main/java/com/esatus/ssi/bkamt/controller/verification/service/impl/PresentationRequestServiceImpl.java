/*
 * Copyright 2021 Bundesrepublik Deutschland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.esatus.ssi.bkamt.controller.verification.service.impl;

import com.esatus.ssi.bkamt.controller.verification.domain.PresentationRequest;
import com.esatus.ssi.bkamt.controller.verification.repository.PresentationRequestRepository;
import com.esatus.ssi.bkamt.controller.verification.service.PresentationRequestService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.PresentationRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.PresentationRequestsAlreadyExists;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.PresentationRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing presentation requests.
 */
@Service
public class PresentationRequestServiceImpl implements PresentationRequestService {

    private final Logger log = LoggerFactory.getLogger(PresentationRequestServiceImpl.class);

    @Autowired
    private PresentationRequestRepository presentationRequestRepository;

    @Autowired
    private PresentationRequestMapper presentationRequestMapper;

    @Override
    public PresentationRequestDTO createPresentationRequest(PresentationRequestDTO presentationRequestDTO) throws PresentationRequestsAlreadyExists {

        Optional<PresentationRequest> existingPresentation = presentationRequestRepository.findOneByThreadId(presentationRequestDTO.getThreadId().toLowerCase());
        if (existingPresentation.isPresent()) {
            throw new PresentationRequestsAlreadyExists();
        }

        PresentationRequest presentationRequest = new PresentationRequest();
        presentationRequest.setId(presentationRequestDTO.getId().toLowerCase());
        presentationRequest.setThreadId(presentationRequestDTO.getThreadId());

        presentationRequestRepository.save(presentationRequest);
        log.debug("Created Information for presentationRequest: {}", presentationRequest);
        return this.presentationRequestMapper.presentationRequestToPresentationRequestDTO(presentationRequest);
    }
}
