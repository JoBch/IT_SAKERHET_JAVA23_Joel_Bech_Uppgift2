package com.example.TimeCapsuleServer.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

@Component
public class AESUtil {

    private static final int KEY_LENGTH = 256; //AES-256
    private static final int ITERATIONS = 65536; //Number of PBKDF2 iterations
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    //Encrypts a message using AES encryption
    public String encryptMessage(String message, String password, String salt) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(makeKeyFromPassword(password, salt), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    //Decrypts an encrypted message using AES decryption
    public String decryptMessage(String encryptedMessage, String password, String salt) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(makeKeyFromPassword(password, salt), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(decryptedBytes);
    }

    //Makes an AES key from the password and salt
    private byte[] makeKeyFromPassword(String password, String salt) throws Exception {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        return factory.generateSecret(spec).getEncoded();
    }

    //Generates a random salt to store with my message
    public String generateSalt() {
        byte[] salt = new byte[16]; //16 bytes = 128 bits
        new Random().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt); //Encode to Base64 for easy storage
    }
}
