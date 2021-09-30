package com.esatus.ssi.bkamt.controller.verification.domain;

public class ValidationResult {
    public Boolean success;

    public ValidationResult(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
