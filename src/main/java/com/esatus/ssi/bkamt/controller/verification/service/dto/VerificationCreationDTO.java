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

    @Override
    public String toString() {
        return "VerificationDTO{" +
            "id='" + id + '\'' +
            ", apiKey='" + apiKey + '\'' +
            ", name='" + name +
            "}";
    }
}
