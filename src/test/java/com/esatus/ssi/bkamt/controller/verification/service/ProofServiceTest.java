package com.esatus.ssi.bkamt.controller.verification.service;

import com.esatus.ssi.bkamt.controller.verification.client.AgentClient;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.VerificationNotFoundException;
import com.esatus.ssi.bkamt.controller.verification.service.impl.ProofServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@RunWith(MockitoJUnitRunner.class)
public class ProofServiceTest {

    @InjectMocks
    @Autowired
    private ProofServiceImpl proofService;

    @Mock
    AgentClient acapyClient;

    @Mock
    VerifierService verifierService;

    @Mock
    NotificationService notificationService;

    @Mock
    MetaDataValidator metaDataValidator;

    @Mock
    VerificationRequestService verificationRequestService;

    @Test
    public void CreateProofRequest_VerificationRequestRepoReturnsEmptyOptional_ShouldThrowVerificationNotFoundException() {
        Mockito.when(verificationRequestService.getByVerificationId(Mockito.isA(String.class))).thenReturn(Optional.empty());
        assertThatThrownBy(() -> proofService.createProofRequest(""))
            .isInstanceOf(VerificationNotFoundException.class);
    }

//    @Test
//    public void CreateProofRequest_() {
//        // Mock ACA-Py Client
//        var v10PresentationExchange = new V10PresentationExchange();
//        v10PresentationExchange.setThreadId("SampleThreadId");
//
//        var proofRequestDict = new ProofRequestDict();
//        proofRequestDict.setId("ID");
//        proofRequestDict.setType("Type");
//        proofRequestDict.setComment("Comment");
//        v10PresentationExchange.setPresentationRequestDict(proofRequestDict);
//
//        Mockito.when(acapyClient.createProofRequest(Mockito.isA(String.class), Mockito.isA(V10PresentationCreateRequestRequest.class)))
//            .thenReturn(v10PresentationExchange);
//        // Mock VerificationRequestService
//        VerificationRequestDTO verificationRequest = new VerificationRequestDTO();
//        Mockito.when(verificationRequestService.getByVerificationId(Mockito.isA(String.class)))
//            .thenReturn(Optional.of(verificationRequest));
//    }

    @Test
    public void HandleProofWebhook_() {
        // Parse WebhookDTO from a json file
    }
}
