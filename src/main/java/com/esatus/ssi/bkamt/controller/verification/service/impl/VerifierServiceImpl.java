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

import com.esatus.ssi.bkamt.controller.verification.domain.Verifier;
import com.esatus.ssi.bkamt.controller.verification.models.VerificationRequestMetadata;
import com.esatus.ssi.bkamt.controller.verification.repository.VerificationRequestRepository;
import com.esatus.ssi.bkamt.controller.verification.repository.VerifierRepository;
import com.esatus.ssi.bkamt.controller.verification.service.VerifierService;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.VerifierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing verifications.
 */
@Service
public class VerifierServiceImpl implements VerifierService {

	private final Logger log = LoggerFactory.getLogger(VerifierServiceImpl.class);

    @Autowired
    private VerifierRepository verifierRepository;

    @Autowired
    private VerificationRequestRepository verificationRequestRepository;

    @Autowired
    private VerifierMapper verifierMapper;

    @Override
    public List<Verifier> getAll() {
        log.debug("get all verifiers");
        return verifierRepository.findAll();
    }

    @Override
    public void invalidateVerification(String verificationId) {
        log.debug("invalidate verification with id {}", verificationId);
        verificationRequestRepository.deleteById(verificationId);
    }

    @Override
    public boolean checkMetaDataCompliance(VerificationRequestMetadata verificationRequestMetadata) {
        return true;
    }
}
