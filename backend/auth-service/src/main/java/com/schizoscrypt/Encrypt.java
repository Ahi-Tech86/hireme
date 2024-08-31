package com.schizoscrypt;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class Encrypt {
    public static void main(String[] args) throws Exception{
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);

        SecretKey key = keyGen.generateKey();
        byte[] encodedKey = key.getEncoded();

        String base64key = Base64.getEncoder().encodeToString(encodedKey);
        System.out.println(base64key);
    }
}
