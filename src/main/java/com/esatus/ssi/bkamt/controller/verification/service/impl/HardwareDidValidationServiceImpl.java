
package com.esatus.ssi.bkamt.controller.verification.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.util.Base64;
import org.bitcoinj.core.Base58;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.esatus.ssi.bkamt.controller.verification.domain.ValidationResult;
import com.esatus.ssi.bkamt.controller.verification.service.HardwareDidValidationService;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.HardwareDidMalformedException;

@Service
public class HardwareDidValidationServiceImpl implements HardwareDidValidationService {

  private final Logger log = LoggerFactory.getLogger(HardwareDidValidationServiceImpl.class);

  private static final String SIGNATURE_ALGORITHM = "SHA256withECDSA";
  private static final String KEY_ALGORITHM = "EC";
  private static final String CURVE = "secp256r1";
  private static final int FORMAT_BYTES = 2;

  @Override
  public ValidationResult validate(String hardwareDID, String hardwareDIDProof, String nonce) {
    try {
      boolean isValid = this.validateSignature(Base64.getDecoder().decode(hardwareDIDProof), getPublicKey(hardwareDID),
          deriveNonce(nonce));
      return new ValidationResult(isValid);
    } catch (GeneralSecurityException | HardwareDidMalformedException | IllegalArgumentException exception) {
      return new ValidationResult(false);
    }
  }

  private PublicKey getPublicKey(String hardwareDID) throws HardwareDidMalformedException, GeneralSecurityException {
    byte[] hardwareDIDKey = extractHardwareDidValue(hardwareDID);

    ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec(CURVE);

    ECNamedCurveSpec ecparams = new ECNamedCurveSpec(CURVE, spec.getCurve(), spec.getG(), spec.getN());
    ECPoint point = ECPointUtil.decodePoint(ecparams.getCurve(), hardwareDIDKey);

    PublicKey publicKey = KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new ECPublicKeySpec(point, ecparams));

    return publicKey;
  }

  private boolean validateHardwareDID(String hardwareDid) {
    String[] splitHardwareDid = hardwareDid.split(":");

    if (splitHardwareDid.length != 3) {
      return false;
    }

    return true;
  }

  private byte[] extractHardwareDidValue(String hardwareDid) throws HardwareDidMalformedException {
    boolean hardwareDidValid = validateHardwareDID(hardwareDid);

    if (!hardwareDidValid) {
      throw new HardwareDidMalformedException();
    }

    int index = hardwareDid.lastIndexOf(":");
    return extractPublicKey(hardwareDid.substring(index + 2));
  }

  private byte[] extractPublicKey(String hardwareDidValue) {
    byte[] decodedhDidValue = Base58.decode(hardwareDidValue);

    byte[] result = new byte[decodedhDidValue.length - FORMAT_BYTES];

    System.arraycopy(decodedhDidValue, FORMAT_BYTES, result, 0, result.length);

    return result;
  }

  private boolean validateSignature(byte[] hardwareDidProofValue, PublicKey publicKey, byte[] hashedNonce)
      throws GeneralSecurityException {
    Signature ecdsaVerify = Signature.getInstance(SIGNATURE_ALGORITHM);

    ecdsaVerify.initVerify(publicKey);
    ecdsaVerify.update(hashedNonce);

    return ecdsaVerify.verify(hardwareDidProofValue);
  }


  private byte[] deriveNonce(String nonce) throws NoSuchAlgorithmException {
    byte[] challenge = nonce.getBytes(StandardCharsets.UTF_8);

    challenge = Base64.getDecoder().decode(challenge);

    byte[] result = new byte[challenge.length + 1];
    System.arraycopy(challenge, 0, result, 0, challenge.length);

    result[challenge.length] = 0x2;

    return sha256(result);
  }

  private byte[] sha256(byte[] challenge) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");

    return digest.digest(challenge);
  }
}
