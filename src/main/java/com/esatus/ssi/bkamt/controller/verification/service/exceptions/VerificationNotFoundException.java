package com.esatus.ssi.bkamt.controller.verification.service.exceptions;

public class VerificationNotFoundException extends Exception {
    public VerificationNotFoundException() {
        super("Verification could not be found");
    }
}
