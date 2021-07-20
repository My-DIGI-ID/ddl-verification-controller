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

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class HotelDTO {

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String id;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String name;

    @NotNull
    @Valid
    private AddressDTO address;

    @NotNull
    @Valid
    private List<DeskDTO> desks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public List<DeskDTO> getDesks() {
        return desks;
    }

    public void setDesks(List<DeskDTO> desks) {
        this.desks = desks;
    }

    @Override
    public String toString() {
        return "HotelDTO [address=" + address + ", desks=" + desks + ", id=" + id + ", name=" + name + "]";
    }
}
