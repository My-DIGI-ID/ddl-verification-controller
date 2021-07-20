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

public class WebhookConnectionDTO {

    @JsonProperty("connection_id")
    private String connectionId;

    private String alias;

    @JsonProperty("state")
    private String state;

    @JsonProperty("my_did")
    private String myDid;

    @JsonProperty("their_did")
    private String theirDid;

    public WebhookConnectionDTO() {
    }

    public WebhookConnectionDTO(String connectionId, String alias, String state) {
        this.connectionId = connectionId;
        this.alias = alias;
        this.state = state;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
