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

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Presentation {

    @JsonProperty("requested_proof")
    private RequestedProof requestedProof;

    @JsonProperty("identifiers")
    private Identifier[] identifiers;

    public Presentation() {
    }

    public RequestedProof getRequestedProof() {
        return requestedProof;
    }

    public Identifier[] getIdentifiers() {
        return identifiers;
    }

    @Override
    public String toString() {
        return "Presentation [identifiers=" + Arrays.toString(identifiers) + ", requestedProof=" + requestedProof + "]";
    }
}
