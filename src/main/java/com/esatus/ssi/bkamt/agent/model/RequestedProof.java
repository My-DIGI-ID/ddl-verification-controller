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

import com.esatus.ssi.bkamt.controller.verification.client.model.RevealedAttrGroups;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestedProof {

    @JsonProperty("revealed_attr_groups")
    private RevealedAttrGroups revealedAttrGroups;

    @JsonProperty("revealed_attrs")
    private Object revealedAttrs;

    @JsonProperty("unrevealed_attrs")
    private Object unrevealed_attrs;

    @JsonProperty("self_attested_attrs")
    private Object selfAttestedAttrs;

    @JsonProperty("predicates")
    private Object predicates;

    public RequestedProof() {}

    public RevealedAttrGroups getRevealedAttrGroups() {
        return this.revealedAttrGroups;
    }

    @Override
    public String toString() {
        return "RequestedProof [revealedAttrGroups=" + revealedAttrGroups + "]";
    }
}
