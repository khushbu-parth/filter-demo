package com.library.info;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class ZSecurity {
    private static Cipher ecipher;
    private static byte[] salt = new byte[8];
    private static int iterationCount = 2000;
    private static String pass;
    private static byte[] iv;
    SecretKey secret;
    Context context;

    public ZSecurity(Context c, String passPhrase) {
        try {
            this.context=c;
            pass=passPhrase;
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(salt);
            SecretKeyFactory factory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount,
                    256);
            SecretKey secretKey = factory.generateSecret(keySpec);
            secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
            ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ecipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = ecipher.getParameters();
            iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public String EncryptToFinalTransferText(String str) {
        try {
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);
            return Base64.encodeToString(enc, Base64.DEFAULT)+Base64.encodeToString(salt, Base64.DEFAULT)+Base64.encodeToString(iv, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String str) {
        String text = str.substring(0, (str.length() - 38));
        String RSalt = str.substring((str.length() - 38), (str.length() - 25));
        String RIV = str.substring((str.length() - 25));
        byte[] saltt = Base64.decode(RSalt, Base64.DEFAULT);
        byte[] IVV = Base64.decode(RIV, Base64.DEFAULT);


        try {
            SecretKeyFactory factory2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            KeySpec keySpec2 = new PBEKeySpec(pass.toCharArray(), saltt, iterationCount, 256);
            SecretKey secretKey2 = factory2.generateSecret(keySpec2);
            SecretKeySpec secret2 = new SecretKeySpec(secretKey2.getEncoded(), "AES");
            Cipher dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            dcipher.init(Cipher.DECRYPT_MODE, secret2, new IvParameterSpec(IVV));

            byte[] dec = Base64.decode(text, Base64.DEFAULT);

            byte[] utf8 = dcipher.doFinal(dec);

            return new String(utf8, "UTF8");
        } catch (BadPaddingException e) {
            Toast.makeText(context, "Wrong password", Toast.LENGTH_SHORT).show();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
