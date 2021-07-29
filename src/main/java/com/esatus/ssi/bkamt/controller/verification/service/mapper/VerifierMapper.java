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
