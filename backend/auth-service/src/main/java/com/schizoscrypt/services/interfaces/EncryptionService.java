package com.schizoscrypt.services.interfaces;

public interface EncryptionService {
    String encrypt(String data);

    String decrypt(String encryptedData);
}
