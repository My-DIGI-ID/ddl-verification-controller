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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.esatus.ssi.bkamt.controller.verification.domain.Desk;

public class DeskDTO {

    @NotNull
    @Size(max = 50)
    private String id;

    @NotNull
    @Size(max = 50)
    private String name;

    public DeskDTO() {

    }

    public DeskDTO(Desk desk) {
        this.id = desk.getId();
        this.name = desk.getName();
    }

    public DeskDTO(@NotNull @Size(max = 50) String id, @NotNull @Size(max = 50) String name) {
        this.id = id;
        this.name = name;
    }

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

    @Override
    public String toString() {
        return "DeskDTO [id=" + id + ", name=" + name + "]";
    }
}
