package com.esatus.ssi.bkamt.controller.verification.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;
import com.esatus.ssi.bkamt.controller.verification.domain.ValidationResult;
import com.esatus.ssi.bkamt.controller.verification.service.impl.HardwareDidValidationServiceImpl;

public class HardwareDidValidationServiceTest {

  @Test
  public void verifyValidSignatureTest() throws Exception {

    String nonce = "424349486652032028193032"; // to be concat with 2 and 2 times SHA-256
    String hardwareDID = "did:key:zDnaevDEspHKH2XvV5WpMpi4rcZ2yjPoZkv5jeB2j6dHbG5LE";
    String hardwareDIDProof =
        "MEQCIGBNInSnPwBCP9m0c4WY9xt+aCQgJvBKSuYw2f6q/87eAiAwWbIdq4AZC8Q3X42v97eJhCJ9iCqxZMZmFdwgYYzdSw==";

    HardwareDidValidationService hardwareDidValidationService = new HardwareDidValidationServiceImpl();

    ValidationResult result = hardwareDidValidationService.validate(hardwareDID, hardwareDIDProof, nonce);

    assertNotNull("Result must not be null", result);
    assertTrue("Signature validation should be successful", result.getSuccess());
  }

  @Test
  public void verifyInvalidSignatureTest() throws Exception {

    String nonce = "424349486652032028194032"; // to be concat with 2 and 2 times SHA-256
    String hardwareDID = "did:key:zDnaevDEspHKH2XvV5WpMpi4rcZ2yjPoZkv5jeB2j6dHbG5LE";
    String hardwareDIDProof =
        "MEQCIGBNInSnPwBCP9m0c4WY9xt+aCQgJvBKSuYw2f6q/87eAiAwWbIdq4AZC8Q3X42v97eJhCJ9iCqxZMZmFdwgYYzdSw==";

    HardwareDidValidationService hardwareDidValidationService = new HardwareDidValidationServiceImpl();

    ValidationResult result = hardwareDidValidationService.validate(hardwareDID, hardwareDIDProof, nonce);

    assertNotNull("Result must not be null", result);
    assertFalse("Signature validation should be unsuccessful", result.getSuccess());
  }

  @Test
  public void invalidKeyFormatTest() throws Exception {

    String nonce = "424349486652032028193032"; // to be concat with 2 and 2 times SHA-256
    String hardwareDID = "zDnaevDEspHKH2XvV5WpMpi4rcZ2yjPoZkv5jeB2j6dHbG5LE";
    String hardwareDIDProof =
        "MEQCIGBNInSnPwBCP9m0c4WY9xt+aCQgJvBKSuYw2f6q/87eAiAwWbIdq4AZC8Q3X42v97eJhCJ9iCqxZMZmFdwgYYzdSw==";

    HardwareDidValidationService hardwareDidValidationService = new HardwareDidValidationServiceImpl();

    ValidationResult result = hardwareDidValidationService.validate(hardwareDID, hardwareDIDProof, nonce);

    assertNotNull("Result must not be null", result);
    assertFalse("Signature validation should be unsuccessful", result.getSuccess());
  }

  @Test
  public void invalidKeyTest() throws Exception {

    String nonce = "424349486652032028193032"; // to be concat with 2 and 2 times SHA-256
    String hardwareDID = "did:key:zDnaerDaTF5BXEavCrfRZEk316dpbLsfPDZ3WJ5hRTPFU2169";
    String hardwareDIDProof =
        "MEQCIGBNInSnPwBCP9m0c4WY9xt+aCQgJvBKSuYw2f6q/87eAiAwWbIdq4AZC8Q3X42v97eJhCJ9iCqxZMZmFdwgYYzdSw==";

    HardwareDidValidationService hardwareDidValidationService = new HardwareDidValidationServiceImpl();

    ValidationResult result = hardwareDidValidationService.validate(hardwareDID, hardwareDIDProof, nonce);

    assertNotNull("Result must not be null", result);
    assertFalse("Signature validation should be unsuccessful", result.getSuccess());
  }

  @Test
  public void invalidHardwareDIDProofBase64Test() throws Exception {

    String nonce = "424349486652032028194032"; // to be concat with 2 and 2 times SHA-256
    String hardwareDID = "did:key:zDnaevDEspHKH2XvV5WpMpi4rcZ2yjPoZkv5jeB2j6dHbG5LE";
    String hardwareDIDProof =
        "MMEQCIGBNInSnPwBCP9m0c4WY9xt+aCQgJvBKSuYw2f6q/87eAiAwWbIdq4AZC8Q3X42v97eJhCJ9iCqxZMZmFdwgYYzdSw==";

    HardwareDidValidationService hardwareDidValidationService = new HardwareDidValidationServiceImpl();

    ValidationResult result = hardwareDidValidationService.validate(hardwareDID, hardwareDIDProof, nonce);

    assertNotNull("Result must not be null", result);
    assertFalse("Signature validation should be unsuccessful", result.getSuccess());
  }

  @Test
  public void invalidHardwareDIDProofTest() throws Exception {

    String nonce = "424349486652032028194032"; // to be concat with 2 and 2 times SHA-256
    String hardwareDID = "did:key:zDnaevDEspHKH2XvV5WpMpi4rcZ2yjPoZkv5jeB2j6dHbG5LE";
    String hardwareDIDProof = "TmljaHQgZ3V0";

    HardwareDidValidationService hardwareDidValidationService = new HardwareDidValidationServiceImpl();

    ValidationResult result = hardwareDidValidationService.validate(hardwareDID, hardwareDIDProof, nonce);

    assertNotNull("Result must not be null", result);
    assertFalse("Signature validation should be unsuccessful", result.getSuccess());
  }
}
