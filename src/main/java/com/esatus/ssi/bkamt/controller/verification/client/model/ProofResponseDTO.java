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

package com.esatus.ssi.bkamt.controller.verification.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProofResponseDTO {

    @JsonProperty("presentation_exchange_id")
    private String presentationExchangeId;
    @JsonProperty("auto_present")
    private Boolean autoPresent;
    @JsonProperty("presentation_request_dict")
    private ProofRequestDict proofRequestDict;
    @JsonProperty("role")
    private String role;
    @JsonProperty("state")
    private String state;
    @JsonProperty("presentation_request")
    private PresentationRequest presentationRequest;
    @JsonProperty("trace")
    private String trace;
    @JsonProperty("initiator")
    private String initiator;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("thread_id")
    private String threadId;
    @JsonProperty("updated_at")
    private String updatedAt;

    public ProofResponseDTO() {}

    public String getPresentationExchangeId() {
        return presentationExchangeId;
    }

    public Boolean getAutoPresent() {
        return autoPresent;
    }

    public ProofRequestDict getProofRequestDict() {
        return proofRequestDict;
    }

    public String getRole() {
        return role;
    }

    public String getState() {
        return state;
    }

    public PresentationRequest getPresentationRequest() {
        return presentationRequest;
    }

    public String getTrace() {
        return trace;
    }

    public String getInitiator() {
        return initiator;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
