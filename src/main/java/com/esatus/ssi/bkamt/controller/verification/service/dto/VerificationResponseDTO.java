package com.esatus.ssi.bkamt.controller.verification.service.dto;

import java.net.URI;

public class VerificationResponseDTO {
    private URI uri;
    private String verificationId;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }
}
