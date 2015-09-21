package org.cnodejs.android.md.util.codec;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Digest {

    public static final Digest MD5 = new Digest("MD5");
    public static final Digest SHA1 = new Digest("SHA-1");
    public static final Digest SHA256 = new Digest("SHA-256");
    public static final Digest SHA384 = new Digest("SHA-384");
    public static final Digest SHA512 = new Digest("SHA-512");

    private final MessageDigest md;

    private Digest(String algorithm) {
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public byte[] getRaw(byte[] input) {
        return md.digest(input);
    }

    public byte[] getRaw(String input) {
        return getRaw(input.getBytes(Charset.forName("UTF-8")));
    }

    public String getMessage(byte[] input) {
        byte[] buffer = getRaw(input);
        StringBuilder sb = new StringBuilder(buffer.length * 2);
        for (byte b : buffer) {
            sb.append(Character.forDigit((b >>> 4) & 15, 16));
            sb.append(Character.forDigit(b & 15, 16));
        }
        return sb.toString();
    }

    public String getMessage(String input) {
        return getMessage(input.getBytes(Charset.forName("UTF-8")));
    }

}
