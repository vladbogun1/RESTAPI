package main.java.ua.nure.bogun.apiproject.service;

import java.security.MessageDigest;
import java.util.Properties;

public class Hashing {
    public static String getHashedString(String base) throws RuntimeException {
        Properties prop = new Properties();
        try {
            prop.load(
                    Hashing.class.getClassLoader().getResourceAsStream("project.properties")
            );
            String alg = prop.getProperty("hashing.method");

            MessageDigest digest = MessageDigest.getInstance(alg);
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
