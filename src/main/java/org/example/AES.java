package org.example;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AES {
    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator=KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey=keyGenerator.generateKey();
        System.out.println("SecretKey "+ secretKey);
        return secretKey;
    }

    public static String encrypt(String algorithm, String input, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher= Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] cipherText=cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String algorithm, String cipherText, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher= Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] plainText=cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }
}
