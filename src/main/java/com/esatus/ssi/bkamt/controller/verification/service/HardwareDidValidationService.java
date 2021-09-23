package com.esatus.ssi.bkamt.controller.verification.service;

import com.esatus.ssi.bkamt.controller.verification.domain.ValidationResult;

public interface HardwareDidValidationService {
  ValidationResult validate(String nonce, String hardwareDidProof, String hardwareDid);
}
