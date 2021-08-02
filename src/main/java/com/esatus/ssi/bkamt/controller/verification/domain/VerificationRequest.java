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

import com.esatus.ssi.bkamt.controller.verification.models.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Represents a presentation request for a digital driver license from any id wallet.
 */
@org.springframework.data.mongodb.core.mapping.Document(collection = "jhi_ddl_verification_request")
public class VerificationRequest {

    @Id
    private String id;

    private String verificationId;

    private String presentationExchangeId;

    @NotNull
    private String callbackUrl;

    private String validUntil;

    private Data data;

    public VerificationRequest() {
        this.verificationId = UUID.randomUUID().toString();
    }

    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPresentationExchangeId() {
        return presentationExchangeId;
    }

    public void setPresentationExchangeId(String presentationExchangeId) {
        this.presentationExchangeId = presentationExchangeId;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        validUntil = validUntil;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }
}
