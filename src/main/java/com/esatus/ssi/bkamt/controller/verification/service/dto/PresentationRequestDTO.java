package com.esatus.ssi.bkamt.controller.verification.service.dto;

import com.esatus.ssi.bkamt.controller.verification.domain.PresentationRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PresentationRequestDTO {
    @NotNull
    private String id;

    @NotBlank
    @NotNull
    private String threadId;

    public PresentationRequestDTO() {
        // Empty constructor needed for Jackson.
    }

    public PresentationRequestDTO(PresentationRequest presentationRequest) {
        this.id = presentationRequest.getId();
        this.threadId = presentationRequest.getThreadId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }
}
