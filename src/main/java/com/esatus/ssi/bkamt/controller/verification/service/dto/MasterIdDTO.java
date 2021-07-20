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

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MasterIdDTO {

    @NotNull
    private String firstName;

    @NotNull
    private String familyName;

    @NotNull
    private String addressStreet;

    @NotNull
    private String addressCountry;

    @NotNull
    private String addressCity;

    @NotNull
    private String addressZipCode;

    @NotNull
    private LocalDate dateOfExpiry;

    @NotNull
    private LocalDate dateOfBirth;

    public LocalDate getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(LocalDate dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    @JsonIgnore
    public void setDateOfExpiryFromString(String dateOfExpiry) {
        this.dateOfExpiry = LocalDate.parse(dateOfExpiry, DateTimeFormatter.BASIC_ISO_DATE);
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonIgnore
    public void setDateOfBirthFromString(String dateOfBirth) {
        this.dateOfBirth = LocalDate.parse(dateOfBirth, DateTimeFormatter.BASIC_ISO_DATE);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressZipCode() {
        return addressZipCode;
    }

    public void setAddressZipCode(String addressZipCode) {
        this.addressZipCode = addressZipCode;
    }

    @Override
    public String toString() {
        return "MasterIdDTO [addressCity=" + addressCity + ", addressCountry=" + addressCountry + ", addressStreet="
                + addressStreet + ", addressZipCode=" + addressZipCode + ", familyName=" + familyName + ", firstName="
                + firstName + ", dateOfBirth=" + dateOfBirth + "]";
    }

}
