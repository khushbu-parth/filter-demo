package com.colorcallscreen.colorphone.callscreen.calltheme;

import android.util.Base64;
import com.bumptech.glide.load.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AESUTIL {
    private static String INIT_VECTOR = "encryptionIntVec";
    private static String SECRET_KEY = "aesEncryptionKey";

    public static String encrypt(String str) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_VECTOR.getBytes(Key.STRING_CHARSET_NAME));
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(Key.STRING_CHARSET_NAME), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(1, secretKeySpec, ivParameterSpec);
            return Base64.encodeToString(cipher.doFinal(str.getBytes()), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String str) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_VECTOR.getBytes(Key.STRING_CHARSET_NAME));
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(Key.STRING_CHARSET_NAME), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(2, secretKeySpec, ivParameterSpec);
            return new String(cipher.doFinal(Base64.decode(str, 0)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
