package com.esatus.ssi.bkamt.controller.verification.service.impl;

import com.esatus.ssi.bkamt.controller.verification.domain.Verification;
import com.esatus.ssi.bkamt.controller.verification.repository.VerificationRepository;
import com.esatus.ssi.bkamt.controller.verification.service.VerificationService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationCreationDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.VerificationAlreadyExistsException;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.VerificationMapper;
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
    private VerificationRepository verificationRepository;

    @Autowired
    private VerificationMapper verificationMapper;

	@Override
	public VerificationDTO createVerification(VerificationCreationDTO verificationDto) throws VerificationAlreadyExistsException {
        Optional<Verification> existingVerification = verificationRepository.findOneByName(verificationDto.getName().toLowerCase());
        if (existingVerification.isPresent()) {
            throw new VerificationAlreadyExistsException();
        }

        Verification verification = new Verification();
        verification.setId(verificationDto.getId().toLowerCase());
        verification.setName(verificationDto.getName());
        verification.setApiKey(verificationDto.getApiKey());

        verificationRepository.save(verification);
        log.debug("Created Information for Verification: {}", verification);
        return this.verificationMapper.verificationToVerificationDTO(verification);
	}

	@Override
	public void deleteVerification(String id) {
		log.debug("delete verification");
        this.verificationRepository.deleteById(id);
	}

	@Override
	public Optional<VerificationDTO> getVerification(String apiKey) {
		log.debug("get verification by apiKey");
		return verificationRepository.findByApiKey(apiKey).map(verificationMapper::verificationToVerificationDTO);
	}
}
