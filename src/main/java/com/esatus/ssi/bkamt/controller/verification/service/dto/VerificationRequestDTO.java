package com.esatus.ssi.bkamt.controller.verification.service.dto;

import com.esatus.ssi.bkamt.controller.verification.domain.VerificationRequest;
import com.esatus.ssi.bkamt.controller.verification.models.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class VerificationRequestDTO {
    @Null
    private String id;

    @Null
    private String presentationExchangeId;

    @NotBlank
    @NotNull
    private String callbackUrl;

    private String verificationId;
    
    private String verifier;

    private Data data;

    public VerificationRequestDTO() {
        // Empty constructor needed for Jackson.
    }

    public VerificationRequestDTO(VerificationRequest verificationRequest) {
        this.id = verificationRequest.getId();
        this.presentationExchangeId = verificationRequest.getPresentationExchangeId();
        this.callbackUrl = verificationRequest.getCallbackUrl();
        this.data = verificationRequest.getData();
        this.verificationId = verificationRequest.getVerificationId();
    }

    public String  getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public String getPresentationExchangeId() {
        return presentationExchangeId;
    }

    public void setPresentationExchangeId(String presentationExchangeId) {
        this.presentationExchangeId = presentationExchangeId;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

	public String getVerifier() {
		return verifier;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}
}
