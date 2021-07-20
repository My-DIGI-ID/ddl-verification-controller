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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.esatus.ssi.bkamt.controller.verification.domain.Desk;
import com.esatus.ssi.bkamt.controller.verification.service.dto.DeskDTO;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.DeskMapper;

/**
 * Unit tests for {@link DeskMapper}.
 */
public class DeskMapperTest {

    private static final String DEFAULT_ID = "desk1";
    private static final String DEFAULT_NAME = "Blue Desk";

    private DeskMapper deskMapper;
    private Desk desk;
    private DeskDTO deskDTO;

    @BeforeEach
    public void init() {
        deskMapper = new DeskMapper();
        desk = new Desk();
        desk.setId(DEFAULT_ID);
        desk.setName(DEFAULT_NAME);

        deskDTO = new DeskDTO(desk);
    }

    @Test
    public void deskToDeskDTO() {
        DeskDTO deskDTO = this.deskMapper.deskToDeskDTO(desk);

        assertThat(deskDTO.getId()).isEqualTo(desk.getId());
        assertThat(deskDTO.getName()).isEqualTo(desk.getName());
    }

    @Test
    public void deskDTOToDesk() {
        Desk desk = this.deskMapper.deskDTOToDesk(deskDTO);

        assertThat(desk.getId()).isEqualTo(deskDTO.getId());
        assertThat(desk.getName()).isEqualTo(deskDTO.getName());
    }

    @Test
    public void desksToDeskDTOsShouldMapOnlyNonNullDesks() {
        List<Desk> desks = new ArrayList<>();
        desks.add(desk);
        desks.add(null);

        List<DeskDTO> deskDTOs = deskMapper.desksToDeskDTOs(desks);

        assertThat(deskDTOs).isNotEmpty();
        assertThat(deskDTOs).size().isEqualTo(1);
    }

    @Test
    public void deskDTOsToDesksShouldMapOnlyNonNullDesks() {
        List<DeskDTO> deskDTOs = new ArrayList<>();
        deskDTOs.add(deskDTO);
        deskDTOs.add(null);

        List<Desk> desks = deskMapper.deskDTOsToDesks(deskDTOs);

        assertThat(desks).isNotEmpty();
        assertThat(desks).size().isEqualTo(1);
    }

    @Test
    public void deskToDeskDTOMapWithNullDeskDTOShouldReturnNull() {
        assertThat(deskMapper.deskToDeskDTO(null)).isNull();
    }

    @Test
    public void deskDTOToDeskMapWithNullDeskShouldReturnNull() {
        assertThat(deskMapper.deskDTOToDesk(null)).isNull();
    }
}
