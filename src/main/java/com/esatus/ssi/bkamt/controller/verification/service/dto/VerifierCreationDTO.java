/*
 * Copyright 2021 Bundesrepublik Deutschland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.esatus.ssi.bkamt.controller.verification.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class VerifierCreationDTO {
	@NotNull
    private String id;

	@NotBlank
    @NotNull
    private String apiKey;

    @NotBlank
    @NotNull
    private String name;

	public VerifierCreationDTO() {
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
        return "VerifierDTO{" +
            "id='" + id + '\'' +
            ", apiKey='" + apiKey + '\'' +
            ", name='" + name +
            "}";
    }
}
