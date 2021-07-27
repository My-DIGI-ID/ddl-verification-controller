package com.esatus.ssi.bkamt.controller.verification.service.exceptions;

public class VerifierAlreadyExistsException extends Exception {
    public VerifierAlreadyExistsException() {
        super("Another verifier with the given name already exists.");
    }
}
