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

package com.esatus.ssi.bkamt.agent.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProofRequestDict {

    @JsonProperty("@type")
    private String type;
    @JsonProperty("@id")
    private String id;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("request_presentations~attach")
    private RequestPresentationAttach[] requestPresentationsAttach;

    public ProofRequestDict() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public RequestPresentationAttach[] getRequestPresentationsAttach() {
        return requestPresentationsAttach;
    }

    public void setRequestPresentationsAttach(RequestPresentationAttach[] requestPresentationsAttach) {
        this.requestPresentationsAttach = requestPresentationsAttach;
    }
}
