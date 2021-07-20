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

public class RevealedAttrValuesMasterId {

    @JsonProperty("firstName")
    private Property firstName;

    @JsonProperty("familyName")
    private Property familyName;

    @JsonProperty("addressZipCode")
    private Property addressZipCode;

    @JsonProperty("addressCity")
    private Property addressCity;

    @JsonProperty("addressStreet")
    private Property addressStreet;

    @JsonProperty("addressCountry")
    private Property addressCountry;

    @JsonProperty("dateOfExpiry")
    private Property dateOfExpiry;

    @JsonProperty("dateOfBirth")
    private Property dateOfBirth;

    public Property getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(Property dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public Property getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Property dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Property getFirstName() {
        return firstName;
    }

    public void setFirstName(Property firstName) {
        this.firstName = firstName;
    }

    public Property getFamilyName() {
        return familyName;
    }

    public void setFamilyName(Property familyName) {
        this.familyName = familyName;
    }

    public Property getAddressZipCode() {
        return addressZipCode;
    }

    public void setAddressZipCode(Property addressZipCode) {
        this.addressZipCode = addressZipCode;
    }

    public Property getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(Property addressCity) {
        this.addressCity = addressCity;
    }

    public Property getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(Property addressStreet) {
        this.addressStreet = addressStreet;
    }

    public Property getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(Property addressCountry) {
        this.addressCountry = addressCountry;
    }

    @Override
    public String toString() {
        return "RevealedAttrValuesMasterId [addressCity=" + addressCity + ", addressCountry=" + addressCountry
                + ", addressStreet=" + addressStreet + ", addressZipCode=" + addressZipCode + ", familyName="
                + familyName + ", firstName=" + firstName + ", dateOfBirth=" + dateOfBirth + "]";
    }

}
