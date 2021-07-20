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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddressDTO {

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String street;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String houseNumber;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String postalCode;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String city;

    public AddressDTO() {
        // Empty needed by Jackson
    }

    public AddressDTO(@NotBlank @NotNull @Size(max = 50) String street, @NotBlank @NotNull @Size(max = 50) String houseNumber, @NotBlank @NotNull @Size(max = 50) String postalCode, @NotBlank @NotNull @Size(max = 50) String city) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "AddressDTO [city=" + city + ", houseNumber=" + houseNumber + ", postalCode=" + postalCode + ", street="
                + street + "]";
    }
}
