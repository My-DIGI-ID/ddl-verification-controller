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

public class ProofRecordDTO {

    @JsonProperty("state")
    private String state;

    @JsonProperty("role")
    private String role;

    @JsonProperty("presentation_request_dict")
    private ProofRequestDict proofRequestDict;

    @JsonProperty("auto_present")
    private Boolean autoPresent;

    @JsonProperty("connection_id")
    private String connectionId;

    @JsonProperty("presentation_exchange_id")
    private String presentationExchangeId;

    @JsonProperty("presentation_request")
    private PresentationRequest presentationRequest;

    @JsonProperty("presentation")
    private Presentation presentation;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("verified")
    private String verified;

    @JsonProperty("initiator")
    private String initiator;

    @JsonProperty("thread_id")
    private String threadId;

    @JsonProperty("trace")
    private Boolean trace;

    @JsonProperty("updated_at")
    private String updatedAt;

    public ProofRecordDTO() {}

    public String getState() {
        return state;
    }

    public String getRole() {
        return role;
    }

    public ProofRequestDict getProofRequestDict() {
        return proofRequestDict;
    }

    public Boolean getAutoPresent() {
        return autoPresent;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public String getPresentationExchangeId() {
        return presentationExchangeId;
    }

    public PresentationRequest getPresentationRequest() {
        return presentationRequest;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getVerified() {
        return verified;
    }

    public String getInitiator() {
        return initiator;
    }

    public String getThreadId() {
        return threadId;
    }

    public Boolean getTrace() {
        return trace;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "ProofRecordDTO [autoPresent=" + autoPresent + ", connectionId=" + connectionId + ", createdAt="
                + createdAt + ", initiator=" + initiator + ", presentation=" + presentation
                + ", presentationExchangeId=" + presentationExchangeId + ", presentationRequest=" + presentationRequest
                + ", proofRequestDict=" + proofRequestDict + ", role=" + role + ", state=" + state + ", threadId="
                + threadId + ", trace=" + trace + ", updatedAt=" + updatedAt + ", verified=" + verified + "]";
    }
}
