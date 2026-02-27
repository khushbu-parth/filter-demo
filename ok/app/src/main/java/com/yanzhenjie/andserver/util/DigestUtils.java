package com.yanzhenjie.andserver.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

public abstract class DigestUtils {
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String md5DigestAsHex(String str) {
        return md5DigestAsHex(str.getBytes());
    }

    public static String md5DigestAsHex(byte[] bArr) {
        return digestAsHexString(MessageDigestAlgorithms.MD5, bArr);
    }

    private static MessageDigest getDigest(String str) {
        try {
            return MessageDigest.getInstance(str);
        } catch (NoSuchAlgorithmException e2) {
            throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + str + "\"", e2);
        }
    }

    private static byte[] digest(String str, byte[] bArr) {
        return getDigest(str).digest(bArr);
    }

    private static String digestAsHexString(String str, byte[] bArr) {
        return new String(digestAsHexChars(str, bArr));
    }

    private static char[] digestAsHexChars(String str, byte[] bArr) {
        return encodeHex(digest(str, bArr));
    }

    private static char[] encodeHex(byte[] bArr) {
        char[] cArr = new char[32];
        for (int i = 0; i < 32; i += 2) {
            byte b = bArr[i / 2];
            char[] cArr2 = HEX_CHARS;
            cArr[i] = cArr2[(b >>> 4) & 15];
            cArr[i + 1] = cArr2[b & 15];
        }
        return cArr;
    }
}
