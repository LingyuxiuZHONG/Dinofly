package com.kevin.dinofly;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {

    private static final int KEY_LENGTH = 32; // 32 bytes = 256 bits

    public static String generateRandomKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[KEY_LENGTH];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public static void main(String[] args) {
        String randomKey = generateRandomKey();
        System.out.println("Randomly generated key: " + randomKey);
    }
}
