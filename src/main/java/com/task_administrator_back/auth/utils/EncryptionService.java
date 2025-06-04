package com.task_administrator_back.auth.utils;

import com.task_administrator_back.auth.exception.UnableToDecryptException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionService {

    @Value("${application.security.decryption.key}")
    private String SECRET_KEY;

    @Value("${application.security.decryption.iv}")
    private String SECRET_IV;

    public String decrypt(String encryptedPassword) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedPassword);
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(SECRET_IV.getBytes());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new UnableToDecryptException("Unable to decrypt password: " + e.getMessage());
        }
    }

}
