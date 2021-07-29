package com.esatus.ssi.bkamt.controller.verification.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PresentationRequestCreationDTO {
    @NotNull
    private String id;

    @NotBlank
    @NotNull
    private String threadId;

    public PresentationRequestCreationDTO() {
        // Empty constructor needed for Jackson.
    }
}
