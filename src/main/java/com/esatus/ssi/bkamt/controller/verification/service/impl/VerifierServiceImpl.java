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
	public Optional<VerifierDTO> getVerifier(String apiKey) {
		log.debug("get verifier by apiKey");
		return verifierRepository.findByApiKey(apiKey).map(verifierMapper::verifierToVerifierDTO);
	}
}
