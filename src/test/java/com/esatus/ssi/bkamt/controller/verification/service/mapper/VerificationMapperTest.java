package com.esatus.ssi.bkamt.controller.verification.service.mapper;

import com.esatus.ssi.bkamt.controller.verification.domain.User;
import com.esatus.ssi.bkamt.controller.verification.domain.Verification;
import com.esatus.ssi.bkamt.controller.verification.service.dto.UserDTO;
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
    public void verificationToVerificationDTO() {
        List<Verification> verifications = new ArrayList<>();
        verifications.add(verification);
        verifications.add(null);

        List<VerificationDTO> verificationDTOS = verificationMapper.verificationsToVerificationsDTO(verifications);

        assertThat(verificationDTOS).isNotEmpty();
        assertThat(verificationDTOS).size().isEqualTo(1);
    }
}
