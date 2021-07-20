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

package com.esatus.ssi.bkamt.controller.verification.service.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.esatus.ssi.bkamt.controller.verification.domain.Desk;
import com.esatus.ssi.bkamt.controller.verification.service.dto.DeskDTO;

/**
 * Mapper for the entity {@link Desk} and its DTO called {@link DeskDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as
 * MapStruct support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class DeskMapper {

    public Desk deskDTOToDesk(DeskDTO deskDTO) {
        if (deskDTO == null) {
            return null;
        } else {

            Desk desk = new Desk();
            desk.setId(deskDTO.getId());
            desk.setName(deskDTO.getName());

            return desk;
        }
    }

    public DeskDTO deskToDeskDTO(Desk desk) {
        if (desk == null) {
            return null;
        } else {

            DeskDTO deskDTO = new DeskDTO();
            deskDTO.setId(desk.getId());
            deskDTO.setName(desk.getName());

            return deskDTO;
        }
    }

    public List<DeskDTO> desksToDeskDTOs(List<Desk> desks) {
        return desks.stream().filter(Objects::nonNull).map(this::deskToDeskDTO).collect(Collectors.toList());
    }

    public List<Desk> deskDTOsToDesks(List<DeskDTO> deskDTOs) {
        return deskDTOs.stream().filter(Objects::nonNull).map(this::deskDTOToDesk).collect(Collectors.toList());
    }
}
