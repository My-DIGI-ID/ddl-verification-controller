package com.esatus.ssi.bkamt.controller.verification.service.exceptions;

public class PresentationRequestsAlreadyExists extends Exception {
    public PresentationRequestsAlreadyExists() {
        super("Another presentation requests with the given name already exists.");
    }
}
