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

public class RevealedAttrValuesDdl {

    @JsonProperty("Name")
    private Property name;

    @JsonProperty("Ausstellungsdatum")
    private Property ausstellungsdatum;

    public Property getName() {
        return name;
    }

    public void setName(Property name) {
        this.name = name;
    }

    public Property getAusstellungsdatum() {
        return ausstellungsdatum;
    }

    public void setAusstellungsdatum(Property ausstellungsdatum) {
        this.ausstellungsdatum = ausstellungsdatum;
    }

    @Override
    public String toString() {
        return "RevealedAttrValuesDdl{" +
            "name=" + name +
            ", ausstellungsdatum=" + ausstellungsdatum +
            '}';
    }
}
