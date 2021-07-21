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

import com.esatus.ssi.bkamt.controller.verification.domain.Verification;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link VerificationMapper}.
 */
public class VerificationMapperTest {

    private static final String DEFAULT_ID = "id1";

    private VerificationMapper verificationMapper;
    private Verification verification;
    private VerificationDTO verificationDTO;

    @BeforeEach
    public void init() {
        verificationMapper = new VerificationMapper();

        verification = new Verification();
        verification.setId("123");
        verification.setName("esatus-test");
        verification.setApiKey("ABC123");

        verificationDTO = new VerificationDTO(verification);
    }

    @Test
    public void verificationsToVerificationsDTO() {
        List<Verification> verifications = new ArrayList<>();
        verifications.add(verification);
        verifications.add(null);

        List<VerificationDTO> verificationDTOS = verificationMapper.verificationsToVerificationsDTO(verifications);

        assertThat(verificationDTOS).isNotEmpty();
        assertThat(verificationDTOS).size().isEqualTo(1);
    }

    @Test
    public void VerificationsDTOToVerifications() {
        List<VerificationDTO> verificationDTOs = new ArrayList<>();
        verificationDTOs.add(verificationDTO);
        verificationDTOs.add(null);

        List<Verification> verifications = verificationMapper.verificationDTOsToVerifications(verificationDTOs);

        assertThat(verifications).isNotEmpty();
        assertThat(verifications).size().isEqualTo(1);
    }

    @Test
    public void verificationDTOToVerificationMapWithNullVerificationShouldReturnNull() {
        assertThat(verificationMapper.verificationDTOToVerification(null)).isNull();
    }

    @Test
    public void testVerificationFromId() {
        assertThat(verificationMapper.verificationFromId(DEFAULT_ID).getId()).isEqualTo(DEFAULT_ID);
        assertThat(verificationMapper.verificationFromId(null)).isNull();
    }
}
