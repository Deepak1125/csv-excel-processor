package com.training.codingstandards;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

public final class SecurityUtil {
    private static final SecureRandom RANDOM = new SecureRandom();

    private SecurityUtil() {
        // Utility class.
    }

    public static String hashIdentifier(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null");
        }
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }

    public static String sessionToken() {
        byte[] token = new byte[32];
        RANDOM.nextBytes(token);
        return HexFormat.of().formatHex(token);
    }

    public static boolean isAdmin(String password) {
        String configuredPassword = System.getenv("CSV_PROCESSOR_ADMIN_PASSWORD");
        return configuredPassword != null && password != null
                && MessageDigest.isEqual(password.getBytes(StandardCharsets.UTF_8),
                configuredPassword.getBytes(StandardCharsets.UTF_8));
    }
}
