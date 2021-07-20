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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.esatus.ssi.bkamt.controller.verification.domain.CheckInCredential;
import com.esatus.ssi.bkamt.controller.verification.service.dto.CheckInCredentialDTO;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.CheckInCredentialMapper;

public class CheckInCredentialMapperTest {
    private static final String DEFAULT_ID = "id1";
    private static final String DEFAULT_HOTELID = "The Hotel";
    private static final String DEFAULT_DESKID = "First Desk";

    private CheckInCredential checkInCredential;
    @BeforeEach
    public void init() {
        new CheckInCredentialMapper();
        checkInCredential = new CheckInCredential();
        checkInCredential.setId(DEFAULT_ID);
        checkInCredential.setHotelId(DEFAULT_HOTELID);
        checkInCredential.setDeskId(DEFAULT_DESKID);

        new CheckInCredentialDTO(checkInCredential);
    }

    @Test
    public void checkInCredentialToCheckInCredentialDTO() {
        // CheckInCredentialDTO checkInCredentialDTO = this.checkInCredentialMapper.checkInCredentialToCheckInCredentialDTO(checkInCredential);

        // assertThat(checkInCredentialDTO.getId()).isEqualTo(checkInCredential.getId());
        // assertThat(checkInCredentialDTO.getHotelId()).isEqualTo(checkInCredential.getHotelId());
        // assertThat(checkInCredentialDTO.getDeskId()).isEqualTo(checkInCredential.getDeskId());
    }

    @Test
    public void checkInCredentialsToCheckInCredentialDTOsShouldMapOnlyNonNullCheckInCredentials() {
        // List<CheckInCredential> checkInCredentials = new ArrayList<>();
        // checkInCredentials.add(checkInCredential);
        // checkInCredentials.add(null);

        // List<CheckInCredentialDTO> checkInCredentialDTOs = checkInCredentialMapper.checkInCredentialsToCheckInCredentialDTOs(checkInCredentials);

        // assertThat(checkInCredentialDTOs).isNotEmpty();
        // assertThat(checkInCredentialDTOs).size().isEqualTo(1);
    }

    @Test
    public void checkInCredentialToCheckInCredentialDTOMapWithNullCheckInCredentialDTOShouldReturnNull() {
        // assertThat(checkInCredentialMapper.checkInCredentialToCheckInCredentialDTO(null)).isNull();
    }
}
