package com.esatus.ssi.bkamt.controller.verification.service.exceptions;

public class VerificationAlreadyExistsException extends Exception {
    public VerificationAlreadyExistsException() {
        super("Another verification with the given name already exists.");
    }
}
