package com.esatus.ssi.bkamt.controller.verification.service.impl;

import com.esatus.ssi.bkamt.controller.verification.domain.ValidationResult;
import com.esatus.ssi.bkamt.controller.verification.service.HardwareDidValidationService;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class HardwareDidValidationServiceImpl implements HardwareDidValidationService {
    private static final String ALGO = "SHA256withECDSA";

    /**
     *
     * @param nonce The nonce generate by the verification controller
     * @param hardwareDid The hardware did. "Represents" the public key
     * @param hardwareDidProof the hardware did proof.
     * @return result indicating is the validation succeeded or not
     */
    @Override
    public ValidationResult Validate(String nonce, String hardwareDidProof, String hardwareDid) {
        try {
            boolean isValid = this.ValidateSignature(nonce, hardwareDid, hardwareDidProof);
            return new ValidationResult(isValid);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException | UnsupportedEncodingException exception) {
            return new ValidationResult(false);
        }
    }

    /**
     *
     * @param nonce The nonce generate by the verification controller
     * @param hardwareDid The hardware did. "Represents" the public key
     * @param hardwareDidProof the hardware did proof.
     * @return boolean indicating if the validation succeeded
     */
    public boolean ValidateSignature(String nonce, String hardwareDid, String hardwareDidProof)
        throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        Signature ecdsaVerify = Signature.getInstance(ALGO);
        KeyFactory kf = KeyFactory.getInstance("EC");

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(hardwareDid));

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(hardwareDidProof.getBytes(StandardCharsets.UTF_8));

        byte[] nonceBytes = GetNonce(nonce, 1);
        return ecdsaVerify.verify(Base64.getDecoder().decode(nonceBytes));
    }

    private static byte[] GetNonce(String issuerNonce, Integer endingAsInt)
    {
        byte ending = endingAsInt.byteValue();

        byte[] bytesNonce = Base64.getDecoder().decode(issuerNonce);
        byte[] bytesConst = new byte[] { ending };
        byte[] bytesNonceAndConst = new byte[bytesNonce.length + bytesConst.length];
        System.arraycopy(bytesNonce, 0, bytesNonceAndConst, 0, bytesNonce.length);
        System.arraycopy(bytesConst, 0, bytesNonceAndConst, bytesNonce.length, bytesConst.length);
        return bytesNonceAndConst;
    }
}
