package com.esatus.ssi.bkamt.controller.verification.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.esatus.ssi.bkamt.controller.verification.service.VerificationService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationCreationDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationDTO;

/**
 * Service class for managing verifications.
 */
@Service
public class VerificationServiceImpl implements VerificationService {
	
	private final Logger log = LoggerFactory.getLogger(VerificationServiceImpl.class);

	@Override
	public VerificationDTO createVerification(VerificationCreationDTO verificationDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteVerification(String id) {
		// TODO Auto-generated method stub
		
	}

}
