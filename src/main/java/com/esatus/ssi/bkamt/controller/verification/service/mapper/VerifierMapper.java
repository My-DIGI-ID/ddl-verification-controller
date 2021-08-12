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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VerifierMapper {
    public Verifier verifierDTOToVerifier(VerifierDTO verifierDTO) {
        if (verifierDTO == null) {
            return null;
        } else {
            Verifier verifier = new Verifier();
            verifier.setId(verifierDTO.getId());
            verifier.setApiKey(verifierDTO.getApiKey());
            verifier.setName(verifierDTO.getName());

            return verifier;

        }
    }

    public VerifierDTO verifierToVerifierDTO(Verifier verifier) {
        if (verifier == null) {
            return null;
        } else {
            VerifierDTO verifierDTO = new VerifierDTO();
            verifierDTO.setId(verifier.getId());
            verifierDTO.setName(verifier.getName());
            verifierDTO.setApiKey(verifier.getApiKey());

            return verifierDTO;
        }
    }

    public List<VerifierDTO> verifiersToVerifierDTOs(List<Verifier> verifiers) {
        return verifiers.stream().filter(Objects::nonNull).map(this::verifierToVerifierDTO).collect(Collectors.toList());
    }

    public List<Verifier> verifierDTOsToVerifiers(List<VerifierDTO> verifierDtos) {
        return verifierDtos.stream().filter(Objects::nonNull).map(this::verifierDTOToVerifier).collect(Collectors.toList());
    }

    public Verifier verifierFromId(String id) {
        if (id == null) {
            return null;
        }
        Verifier verifier = new Verifier();
        verifier.setId(id);
        return verifier;
    }
}
