package com.esatus.ssi.bkamt.controller.verification.service.mapper;

import com.esatus.ssi.bkamt.controller.verification.domain.User;
import com.esatus.ssi.bkamt.controller.verification.domain.Verification;
import com.esatus.ssi.bkamt.controller.verification.service.dto.UserDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VerificationMapper {
    public Verification verificationDTOToVerification(VerificationDTO verificationDTO) {
        if (verificationDTO == null) {
            return null;
        } else {
            Verification verification = new Verification();
            verification.setId(verificationDTO.getId());
            verification.setApiKey(verificationDTO.getApiKey());
            verification.setName(verificationDTO.getName());

            return verification;

        }
    }

    public VerificationDTO verificationToVerificationDTO(Verification verification) {
        if (verification == null) {
            return null;
        } else {
            VerificationDTO verificationDTO = new VerificationDTO();
            verificationDTO.setId(verification.getId());
            verificationDTO.setName(verification.getName());
            verificationDTO.setApiKey(verification.getApiKey());

            return verificationDTO;
        }
    }

    public List<VerificationDTO> verificationsToVerificationsDTO(List<Verification> verifications) {
        return verifications.stream().filter(Objects::nonNull).map(this::verificationToVerificationDTO).collect(Collectors.toList());
    }

    public List<Verification> verificationDTOsToVerifications(List<VerificationDTO> verificationDtos) {
        return verificationDtos.stream().filter(Objects::nonNull).map(this::verificationDTOToVerification).collect(Collectors.toList());
    }

    public Verification verificationFromId(String id) {
        if (id == null) {
            return null;
        }
        Verification verification = new Verification();
        verification.setId(id);
        return verification;
    }
}
