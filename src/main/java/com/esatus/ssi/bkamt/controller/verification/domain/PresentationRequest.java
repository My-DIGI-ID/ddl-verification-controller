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

package com.esatus.ssi.bkamt.controller.verification.domain;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

/**
 * Represents a presentation request for a digital driver license from any id wallet.
 */
@org.springframework.data.mongodb.core.mapping.Document(collection = "jhi_ddl_presentation_request")
public class PresentationRequest {

    @Id
    private String id;

    @NotNull
    private String threadId;

    // TODO: Defined more attributes
}
