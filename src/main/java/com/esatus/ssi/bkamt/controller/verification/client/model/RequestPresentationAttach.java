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

import com.esatus.ssi.bkamt.agent.model.Base64Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestPresentationAttach {

    @JsonProperty("@id")
    private String id;

    @JsonProperty("mime-type")
    private String mimeType;

    @JsonProperty("data")
    private Base64Payload data;

    public RequestPresentationAttach() {};

    public void setId(String id) {
        this.id = id;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setData(Base64Payload data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Base64Payload getData() {
        return data;
    }
}
