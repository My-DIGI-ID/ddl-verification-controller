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

import com.esatus.ssi.bkamt.controller.verification.domain.Verifier;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerifierDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link VerifierMapper}.
 */
public class VerifierMapperTest {

    private static final String DEFAULT_ID = "id1";

    private VerifierMapper verifierMapper;
    private Verifier verifier;
    private VerifierDTO verifierDTO;

    @BeforeEach
    public void init() {
        verifierMapper = new VerifierMapper();

        verifier = new Verifier();
        verifier.setId("123");
        verifier.setName("esatus-test");
        verifier.setApiKey("ABC123");

        verifierDTO = new VerifierDTO(verifier);
    }

    @Test
    public void verificationsToVerificationsDTO() {
        List<Verifier> verifiers = new ArrayList<>();
        verifiers.add(verifier);
        verifiers.add(null);

        List<VerifierDTO> verifierDTOS = verifierMapper.verifiersToVerifierDTOs(verifiers);

        assertThat(verifierDTOS).isNotEmpty();
        assertThat(verifierDTOS).size().isEqualTo(1);
    }

    @Test
    public void VerificationsDTOToVerifications() {
        List<VerifierDTO> verifierDTOS = new ArrayList<>();
        verifierDTOS.add(verifierDTO);
        verifierDTOS.add(null);

        List<Verifier> verifiers = verifierMapper.verifierDTOsToVerifiers(verifierDTOS);

        assertThat(verifiers).isNotEmpty();
        assertThat(verifiers).size().isEqualTo(1);
    }

    @Test
    public void verificationDTOToVerificationMapWithNullVerificationShouldReturnNull() {
        assertThat(verifierMapper.verifierDTOToVerifier(null)).isNull();
    }

    @Test
    public void testVerificationFromId() {
        assertThat(verifierMapper.verifierFromId(DEFAULT_ID).getId()).isEqualTo(DEFAULT_ID);
        assertThat(verifierMapper.verifierFromId(null)).isNull();
    }
}
