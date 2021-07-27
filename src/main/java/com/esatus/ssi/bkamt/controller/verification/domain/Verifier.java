package com.esatus.ssi.bkamt.controller.verification.domain;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A verifier.
 */
@org.springframework.data.mongodb.core.mapping.Document(collection = "jhi_verifier")
public class Verifier {

	@Id
    private String id;

	@NotNull
	@Field("api_key")
    private String apiKey;

	@NotNull
    private String name;

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
