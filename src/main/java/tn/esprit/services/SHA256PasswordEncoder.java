package tn.esprit.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256PasswordEncoder {
    public static String hashPassword(String text) {
        String password = text;

        String pass = null;
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Add password bytes to digest
            md.update(password.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // Convert bytes to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            // Print out the hash
            System.out.println("SHA-256 Hash: " + sb.toString());
            pass = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return pass;
    }
}
