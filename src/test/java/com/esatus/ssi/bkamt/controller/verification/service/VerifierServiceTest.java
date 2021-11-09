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
import java.util.ArrayList;
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
import com.esatus.ssi.bkamt.controller.verification.models.Data;
import com.esatus.ssi.bkamt.controller.verification.models.VerificationRequestMetadata;
import com.esatus.ssi.bkamt.controller.verification.repository.VerificationRequestRepository;
import com.esatus.ssi.bkamt.controller.verification.repository.VerifierRepository;
import com.esatus.ssi.bkamt.controller.verification.service.impl.VerifierServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class VerifierServiceTest {

  @InjectMocks
  @Autowired
  private VerifierServiceImpl verifierService;

  @Mock
  VerifierRepository verifierRepository;

  @Mock
  private VerificationRequestRepository verificationRequestRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void GetAll_RepositoryFindAllCalledOnce() {

    Mockito.when(verifierRepository.findAll()).thenReturn(new ArrayList<>());
    verifierService.getAll();
    Mockito.verify(verifierRepository, Mockito.times(1)).findAll();
  }

  @Test
  public void InvalidateVerification_RepositoryDeleteByIdCalledOnce() {
    Mockito.doNothing().when(verificationRequestRepository).deleteById(Mockito.isA(String.class));

    UUID verificationId = UUID.randomUUID();
    String vIdStr = verificationId.toString();
    verifierService.invalidateVerification(vIdStr);
    Mockito.verify(verificationRequestRepository, Mockito.times(1)).deleteByVerificationId(vIdStr);
  }

  @Test
  public void checkMetaDataCompliance_ClassOnly() {
    var verificationRequestMetadata = new VerificationRequestMetadata();
    var isValid = verifierService.checkMetaDataCompliance(verificationRequestMetadata);

    assertEquals(true, isValid);
  }

  @Test
  public void checkMetaDataCompliance_CorrectMetadata() {
    var verificationRequestMetadata = new VerificationRequestMetadata();
    verificationRequestMetadata.setAdditionalProperty("", new Object());
    verificationRequestMetadata.setCallbackURL("");
    verificationRequestMetadata.setData(new Data());
    verificationRequestMetadata.setSelfAttested(new ArrayList<Object>());
    verificationRequestMetadata.setValidUntil("");

    var isValid = verifierService.checkMetaDataCompliance(verificationRequestMetadata);

    assertEquals(true, isValid);
  }

  @Test
  public void checkMetaDataCompliance_verificationRequesMetaDataIsNull() {
    var isValid = verifierService.checkMetaDataCompliance(null);

    assertEquals(false, isValid);
  }
}
