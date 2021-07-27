package com.esatus.ssi.bkamt.controller.verification.service.dto;

import com.esatus.ssi.bkamt.controller.verification.domain.VerificationRequest;
import com.mongodb.BasicDBObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.UUID;

public class VerificationRequestDTO {
    @Null
    private String id;

    @NotBlank
    @NotNull
    private String threadId;

    @NotBlank
    @NotNull
    private String callbackUrl;

    @Null
    private List<BasicDBObject> data;

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

    public List<BasicDBObject> getData() {
        return data;
    }

    public void setData(List<BasicDBObject> data) {
        this.data = data;
    }
}
