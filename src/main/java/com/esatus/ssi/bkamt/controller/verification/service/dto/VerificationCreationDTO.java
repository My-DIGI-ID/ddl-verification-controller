package com.esatus.ssi.bkamt.controller.verification.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class VerificationCreationDTO {
	@NotNull
    private String id;
	
	@NotBlank
    @NotNull
    private String apiKey;
	
	@NotBlank
    @NotNull
    private String name;
	
	public VerificationCreationDTO() {
        // Empty constructor needed for Jackson.
    }
}
