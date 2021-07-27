package com.esatus.ssi.bkamt.controller.verification.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.esatus.ssi.bkamt.controller.verification.domain.Verifier;


/**
 * A DTO representing a verifier
 */
public class VerifierDTO {
	@NotNull
    private String id;

	@NotBlank
    @NotNull
    private String apiKey;

	@NotBlank
    @NotNull
    private String name;

	public VerifierDTO() {
        // Empty constructor needed for Jackson.
    }

	public VerifierDTO(Verifier verifier) {
        this.id = verifier.getId();
        this.apiKey = verifier.getApiKey();
        this.name = verifier.getName();
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
