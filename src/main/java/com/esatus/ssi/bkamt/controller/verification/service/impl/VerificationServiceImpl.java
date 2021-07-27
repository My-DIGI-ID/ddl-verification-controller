package com.esatus.ssi.bkamt.controller.verification.service.impl;

import com.esatus.ssi.bkamt.controller.verification.domain.Verifier;
import com.esatus.ssi.bkamt.controller.verification.repository.VerifierRepository;
import com.esatus.ssi.bkamt.controller.verification.service.VerificationService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerifierCreationDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerifierDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.VerificationAlreadyExistsException;
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
public class VerificationServiceImpl implements VerificationService  {

	private final Logger log = LoggerFactory.getLogger(VerificationServiceImpl.class);

    @Autowired
    private VerifierRepository verifierRepository;

    @Autowired
    private VerifierMapper verifierMapper;

	@Override
	public VerifierDTO createVerification(VerifierCreationDTO verificationDto) throws VerificationAlreadyExistsException {
        Optional<Verifier> existingVerification = verifierRepository.findOneByName(verificationDto.getName().toLowerCase());
        if (existingVerification.isPresent()) {
            throw new VerificationAlreadyExistsException();
        }

        Verifier verifier = new Verifier();
        verifier.setId(verificationDto.getId().toLowerCase());
        verifier.setName(verificationDto.getName());
        verifier.setApiKey(verificationDto.getApiKey());

        verifierRepository.save(verifier);
        log.debug("Created Information for Verification: {}", verifier);
        return this.verifierMapper.verifierToVerifierDTO(verifier);
	}

    @Override
    public boolean verificationExists(String apiKey) {
        log.debug("check if verification record with apiKey exists");
        Optional<VerifierDTO> verification = this.getVerification(apiKey);
        return  verification.isPresent();
    }

    @Override
	public Optional<VerifierDTO> getVerification(String apiKey) {
		log.debug("get verification by apiKey");
		return verifierRepository.findByApiKey(apiKey).map(verifierMapper::verifierToVerifierDTO);
	}
}
