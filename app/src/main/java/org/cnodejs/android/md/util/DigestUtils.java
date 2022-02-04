package org.cnodejs.android.md.util;

import android.support.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class DigestUtils {

    public static final DigestUtils sha256 = new DigestUtils("SHA-256");

    private final String algorithm;

    private DigestUtils(@NonNull String algorithm) {
        this.algorithm = algorithm;
    }

    @NonNull
    public byte[] getRaw(@NonNull byte[] data) {
        try {
            return MessageDigest.getInstance(algorithm).digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    public byte[] getRaw(@NonNull String data) {
        return getRaw(data.getBytes(StandardCharsets.UTF_8));
    }

    @NonNull
    public String getHex(@NonNull byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : getRaw(data)) {
            sb.append(String.format("%02x", 0xFF & b));
        }
        return sb.toString();
    }

    @NonNull
    public String getHex(@NonNull String data) {
        return getHex(data.getBytes(StandardCharsets.UTF_8));
    }

}
