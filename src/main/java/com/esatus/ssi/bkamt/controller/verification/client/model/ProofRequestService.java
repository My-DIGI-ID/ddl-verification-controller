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

public class ProofRequestService {

    @JsonProperty("recipientKeys")
    private String[] recipientKeys;

    @JsonProperty("routingKeys")
    private String[] routingKeys;

    @JsonProperty("serviceEndpoint")
    private String serviceEndpoint;

    @JsonProperty("endpointName")
    private String endpointName;

    public ProofRequestService() {}

    public void setRecipientKeys(String[] recipientKeys) {
        this.recipientKeys = recipientKeys;
    }

    public void setRoutingKeys(String[] routingKeys) {
        this.routingKeys = routingKeys;
    }

    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }
}
