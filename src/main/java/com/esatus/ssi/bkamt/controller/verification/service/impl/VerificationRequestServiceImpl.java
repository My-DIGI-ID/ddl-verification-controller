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

import com.esatus.ssi.bkamt.controller.verification.domain.VerificationRequest;
import com.esatus.ssi.bkamt.controller.verification.models.VerificationRequestMetadata;
import com.esatus.ssi.bkamt.controller.verification.repository.VerificationRequestRepository;
import com.esatus.ssi.bkamt.controller.verification.service.VerificationRequestService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.PresentationRequestsAlreadyExists;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.VerificationNotFoundException;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.VerificationRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing presentation requests.
 */
@Service
public class VerificationRequestServiceImpl implements VerificationRequestService {

    private final Logger log = LoggerFactory.getLogger(VerificationRequestServiceImpl.class);

    @Autowired
    private VerificationRequestRepository verificationRepository;

    @Autowired
    private VerificationRequestMapper verificationRequestMapper;

    @Override
    public VerificationRequestDTO createVerificationRequest(VerificationRequestMetadata verificationRequestMetadata) {
        VerificationRequest verificationRequest = new VerificationRequest();
        verificationRequest.setCallbackUrl(verificationRequestMetadata.getCallbackURL());
        verificationRequest.setValidUntil(verificationRequestMetadata.getValidUntil());
        verificationRequest.setData(verificationRequestMetadata.getData());

        this.verificationRepository.save(verificationRequest);
        log.debug("Created verification request: {}", verificationRequest);
        return this.verificationRequestMapper.verificationRequestToVerificationRequestDTO(verificationRequest);
    }

    @Override
    public Optional<VerificationRequestDTO> getById(String id) {
        log.debug("get verification request by id {}", id);
        return verificationRepository.findById(id).map(verificationRequestMapper::verificationRequestToVerificationRequestDTO);
    }

    @Override
    public Optional<VerificationRequestDTO> getByThreadId(String threadId) {
        log.debug("get verification request by threadId {}", threadId);
        return verificationRepository.findOneByThreadId(threadId).map(verificationRequestMapper::verificationRequestToVerificationRequestDTO);
    }

    @Override
    public void updateThreadId(String verificationId, String threadId) throws VerificationNotFoundException {
        Optional<VerificationRequest> verificationOpt = verificationRepository.findByVerificationId(verificationId);

        if (verificationOpt.isEmpty()) {
            throw new VerificationNotFoundException();
        }

        VerificationRequest vr = verificationOpt.get();
        vr.setThreadId(threadId);

        log.debug("update verification {} and set thread id {}", verificationId, threadId);
        this.verificationRequestMapper.verificationRequestToVerificationRequestDTO(vr);
    }
}
