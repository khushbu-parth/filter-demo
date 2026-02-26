package com.library.info;

import android.content.Context;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class HiddenTesla {
    public static String ice_hound(Context context, String value) throws Exception {
        byte[] values = Base64.decode(value, Base64.DEFAULT);
        SecretKeySpec secretKeySpec = new SecretKeySpec(context.getResources().getString(R.string.wireshark).getBytes(), CastTvAESSUtils.decryptA(BuildConfig.ALGORITHM));
        Cipher cipher = Cipher.getInstance(CastTvAESSUtils.decryptA(BuildConfig.MODE));
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(CastTvAESSUtils.decryptA(BuildConfig.IV).getBytes()));
        return new String(cipher.doFinal(values));
    }

    public static String reverse_ice_Hound(Context context, String value) throws Exception {
        byte[] data = value.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(context.getResources().getString(R.string.wireshark).getBytes(), CastTvAESSUtils.decryptA(BuildConfig.ALGORITHM));
        Cipher cipher = Cipher.getInstance(CastTvAESSUtils.decryptA(BuildConfig.MODE));
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(CastTvAESSUtils.decryptA(BuildConfig.IV).getBytes()));
        byte[] encryptedData = cipher.doFinal(data);
        return Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }

}