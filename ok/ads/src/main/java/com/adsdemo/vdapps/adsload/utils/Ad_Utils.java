package com.adsdemo.vdapps.adsload.utils;

import android.util.Log;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Ad_Utils {
    private static String randomString(int len, String mPassword) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++)
            sb.append(mPassword.charAt(rnd.nextInt(mPassword.length())));

        return sb.toString();
    }


    public static String generateAlphaNumericString(int len) {

        String regexDigit = "\\d+";
        String regexAlphabets = "[a-zA-Z]+";
        String randomAlphaNumString;

        do {
            randomAlphaNumString = randomString(len, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");

        } while (randomAlphaNumString.matches(regexDigit) || randomAlphaNumString.matches(regexAlphabets));
        return randomAlphaNumString;
    }

    public static String[] getCurrentAndFutureDate(int totalDays) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String current = simpledateformat.format(calendar.getTime());
        Log.e("parth", "current: " + current);

        calendar.setTime(simpledateformat.parse(current));
        calendar.add(Calendar.DATE, totalDays);

        String future = simpledateformat.format(calendar.getTime());
        Log.e("parth", "Future: " + future);

        return new String[]{current, future};
    }
}
