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

public class RevealedAttrValuesCorporateId {

    @JsonProperty("firmCity")
    private Property firmCity;

    @JsonProperty("firmStreet")
    private Property firmStreet;

    @JsonProperty("firmPostalcode")
    private Property firmPostalcode;

    @JsonProperty("firmName")
    private Property firmName;

    @JsonProperty("firmSubject")
    private Property firmSubject;

    @JsonProperty("firstName")
    private Property firstName;

    @JsonProperty("lastName")
    private Property lastName;

    public Property getFirmCity() {
        return firmCity;
    }

    public void setFirmCity(Property firmCity) {
        this.firmCity = firmCity;
    }

    public Property getFirmStreet() {
        return firmStreet;
    }

    public void setFirmStreet(Property firmStreet) {
        this.firmStreet = firmStreet;
    }

    public Property getFirmPostalcode() {
        return firmPostalcode;
    }

    public void setFirmPostalcode(Property firmPostalcode) {
        this.firmPostalcode = firmPostalcode;
    }

    public Property getFirmName() {
        return firmName;
    }

    public void setFirmName(Property firmName) {
        this.firmName = firmName;
    }

    public Property getFirmSubject() {
        return firmSubject;
    }

    public void setFirmSubject(Property firmSubject) {
        this.firmSubject = firmSubject;
    }

    public Property getFirstName() {
        return firstName;
    }

    public void setFirstName(Property firstName) {
        this.firstName = firstName;
    }

    public Property getLastName() {
        return lastName;
    }

    public void setLastName(Property lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "RevealedAttrValuesCorporateId [firmCity=" + firmCity + ", firmName=" + firmName + ", firmPostalcode="
                + firmPostalcode + ", firmStreet=" + firmStreet + ", firmSubject=" + firmSubject + ", firstName="
                + firstName + ", lastName=" + lastName + "]";
    }
}
