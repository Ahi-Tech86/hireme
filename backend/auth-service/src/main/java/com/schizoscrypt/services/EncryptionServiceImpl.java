package com.schizoscrypt.services;

import com.schizoscrypt.exception.AppException;
import com.schizoscrypt.services.interfaces.EncryptionService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionServiceImpl implements EncryptionService {

    @Value("${application.security.encryption.secret-key}")
    private String stringSecretKey;

    private SecretKeySpec secretKey;
    private IvParameterSpec ivParameterSpec;

    @PostConstruct
    private void init() {
        byte[] key = Base64.getDecoder().decode(stringSecretKey);

        System.out.println(key.length);

        if (key.length != 32) {
            throw new IllegalArgumentException("Invalid key length for AES_256");
        }
        this.secretKey = new SecretKeySpec(key, "AES");

        byte[] iv = new byte[16];
        this.ivParameterSpec = new IvParameterSpec(iv);
    }

    @Override
    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new AppException("[ERROR] Couldn't encrypt token", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new AppException("[ERROR] Couldn't decrypt token", HttpStatus.BAD_REQUEST);
        }
    }
}
