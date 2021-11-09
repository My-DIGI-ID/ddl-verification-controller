/*
 * Copyright 2021 Bundesrepublik Deutschland
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.esatus.ssi.bkamt.controller.verification.service;

import static junit.framework.TestCase.assertEquals;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import com.esatus.ssi.bkamt.controller.verification.domain.VerificationRequest;
import com.esatus.ssi.bkamt.controller.verification.models.Data;
import com.esatus.ssi.bkamt.controller.verification.models.VerificationRequestMetadata;
import com.esatus.ssi.bkamt.controller.verification.repository.VerificationRequestRepository;
import com.esatus.ssi.bkamt.controller.verification.service.dto.VerificationRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.service.impl.VerificationRequestServiceImpl;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.VerificationRequestMapper;

@RunWith(MockitoJUnitRunner.class)
public class VerificationRequestServiceTest {

  @InjectMocks
  @Autowired
  private VerificationRequestServiceImpl verificationRequestService;

  @Mock
  private VerificationRequestRepository verificationRepository;

  @Mock
  private VerificationRequestMapper verificationRequestMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void CreateVerificationRequest_VerificationRepositorySave_WasCalledOnce() {
    Mockito.when(verificationRepository.save(Mockito.isA(VerificationRequest.class)))
        .thenReturn(new VerificationRequest());
    Mockito
        .when(verificationRequestMapper
            .verificationRequestToVerificationRequestDTO(Mockito.isA(VerificationRequest.class)))
        .thenReturn(new VerificationRequestDTO());

    VerificationRequestMetadata verificationRequestMetadata = new VerificationRequestMetadata();
    verificationRequestMetadata.setCallbackURL("");
    verificationRequestMetadata.setValidUntil("");
    verificationRequestMetadata.setData(new Data());

    verificationRequestService.createVerificationRequest(verificationRequestMetadata, "");

    Mockito.verify(verificationRepository, Mockito.times(1)).save(Mockito.isA(VerificationRequest.class));
  }

  @Test
  public void CreateVerificationRequest_VerificationRequestMapperVerificationRequestToVerificationRequestDTO_WasCalledOnce() {
    Mockito.when(verificationRepository.save(Mockito.isA(VerificationRequest.class)))
        .thenReturn(new VerificationRequest());
    Mockito
        .when(verificationRequestMapper
            .verificationRequestToVerificationRequestDTO(Mockito.isA(VerificationRequest.class)))
        .thenReturn(new VerificationRequestDTO());

    VerificationRequestMetadata verificationRequestMetadata = new VerificationRequestMetadata();
    verificationRequestMetadata.setCallbackURL("");
    verificationRequestMetadata.setValidUntil("");
    verificationRequestMetadata.setData(new Data());

    verificationRequestService.createVerificationRequest(verificationRequestMetadata, "");

    Mockito.verify(verificationRequestMapper, Mockito.times(1))
        .verificationRequestToVerificationRequestDTO(Mockito.isA(VerificationRequest.class));
  }

  @Test
  public void checkMetaDataCompliance_CorrectVerificationId() {
    UUID verificationId = UUID.randomUUID();
    String vIdStr = verificationId.toString();
    var isValid = verificationRequestService.checkVerificationIdCompliance(vIdStr);

    assertEquals(true, isValid);
  }

  @Test
  public void checkMetaDataCompliance_WrongVerificationId() {
    var isValid = verificationRequestService.checkVerificationIdCompliance(new String());

    assertEquals(false, isValid);
  }
}
