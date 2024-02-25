package tn.esprit.services;
import tn.esprit.entities.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class VerificationCodeGenerator {

    public static String generateVerificationCode(User user) {
        // Generate a hash code based on the user information
        int hashCode = user.hashCode();

        // Generate a random salt
        String randomSalt = generateRandomSalt();

        // Combine hash code and random salt
        String combinedString = hashCode + randomSalt;

        // Hash the combined string using SHA-256
        String hashedString = hashString(combinedString);

        // Extract a subset of the hash value as the verification code

        return hashedString.substring(0, 6);
    }

    private static String generateRandomSalt() {
        // Generate a random salt using SecureRandom
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return new String(salt);
    }

    private static String hashString(String input) {
        // Hash the input string using SHA-256
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
