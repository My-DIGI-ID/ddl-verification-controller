package com.esatus.ssi.bkamt.controller.verification.domain;

public class RequestPresentationValidationResult {
    private final boolean isValid;
    private final String message;

    public RequestPresentationValidationResult(boolean valid, String message) {
        this.isValid = valid;
        this.message = message;
    }

    public boolean isValid() {
        return isValid;
    }
    public String getMessage() {
        return message;
    }
}
