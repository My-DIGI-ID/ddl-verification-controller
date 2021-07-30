package com.esatus.ssi.bkamt.controller.verification.service.dto;

import com.esatus.ssi.bkamt.controller.verification.domain.VerificationRequest;
import com.esatus.ssi.bkamt.controller.verification.models.Data;
import com.esatus.ssi.bkamt.controller.verification.models.Data__1;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

public class VerificationRequestDTO {

    @Null
    private String id;

    @Null
    private String threadId;

    @NotBlank
    @NotNull
    private String callbackUrl;

    private Data data;

    public VerificationRequestDTO() {
        // Empty constructor needed for Jackson.
    }

    public VerificationRequestDTO(VerificationRequest verificationRequest) {
        this.id = verificationRequest.getId();
        this.threadId = verificationRequest.getThreadId();
        this.callbackUrl = verificationRequest.getCallbackUrl();
        this.data = verificationRequest.getData();
    }

    public String  getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
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
}
