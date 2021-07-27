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
import com.esatus.ssi.bkamt.controller.verification.repository.VerifierRepository;
import com.esatus.ssi.bkamt.controller.verification.service.VerifierService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerifierCreationDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerifierDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.VerifierAlreadyExistsException;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.VerifierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing verifications.
 */
@Service
public class VerifierServiceImpl implements VerifierService {

	private final Logger log = LoggerFactory.getLogger(VerifierServiceImpl.class);

    @Autowired
    private VerifierRepository verifierRepository;

    @Autowired
    private VerifierMapper verifierMapper;

	@Override
	public VerifierDTO createVerifier(VerifierCreationDTO verifierCreationDTO) throws VerifierAlreadyExistsException {
        Optional<Verifier> existingVerification = verifierRepository.findOneByName(verifierCreationDTO.getName().toLowerCase());
        if (existingVerification.isPresent()) {
            throw new VerifierAlreadyExistsException();
        }

        Verifier verifier = new Verifier();
        verifier.setId(verifierCreationDTO.getId().toLowerCase());
        verifier.setName(verifierCreationDTO.getName());
        verifier.setApiKey(verifierCreationDTO.getApiKey());

        verifierRepository.save(verifier);
        log.debug("Created Information for verifier: {}", verifier);
        return this.verifierMapper.verifierToVerifierDTO(verifier);
	}

    @Override
    public boolean verifierExists(String apiKey) {
        log.debug("check if verifier record with apiKey exists");
        Optional<VerifierDTO> verifier = this.getVerifier(apiKey);
        return verifier.isPresent();
    }

    @Override
    public void invalidateVerification(String verificationId) {
	    // TODO: Do we need to delete the whole verification or do we just empty the metadata?
        // For now we will delete the whole record from the database
        log.debug("invalidate verification with id {}", verificationId);
        verifierRepository.deleteById(verificationId);
    }

    @Override
	public Optional<VerifierDTO> getVerifier(String apiKey) {
		log.debug("get verifier by apiKey");
		return verifierRepository.findByApiKey(apiKey).map(verifierMapper::verifierToVerifierDTO);
	}
}
