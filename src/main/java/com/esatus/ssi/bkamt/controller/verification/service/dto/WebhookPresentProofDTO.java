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

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebhookPresentProofDTO {

    @JsonProperty("presentation_exchange_id")
    private String presentationExchangeId;

    @JsonProperty("connection_id")
    private String connectionId;

    @JsonProperty("thread_id")
    private String threadId;

    @JsonProperty("initiator")
    private String initiator;

    @JsonProperty("state")
    private String state;

    /**
    @JsonProperty("presentation_proposal_dict")
    private String presentationProposalDict;

    @JsonProperty("presentation_request")
    private String presentationRequest;

    @JsonProperty("presentation")
    private String presentation;
    */

    @JsonProperty("verified")
    private String verified;

    public WebhookPresentProofDTO() {
    }

    public String getPresentationExchangeId() {
        return presentationExchangeId;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getInitiator() {
        return initiator;
    }

    public String getState() {
        return state;
    }

    public String getVerified() {
        return verified;
    }

    @Override
    public String toString() {
        return "WebhookPresentProofDTO [connectionId=" + connectionId + ", initiator=" + initiator
                + ", presentationExchangeId=" + presentationExchangeId + ", state=" + state + ", threadId=" + threadId
                + ", verified=" + verified + "]";
    }
}
