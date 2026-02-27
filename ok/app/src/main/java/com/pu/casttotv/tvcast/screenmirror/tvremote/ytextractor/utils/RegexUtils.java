package com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    public static String matchGroup(String str, String str2) {
        Matcher matcher = Pattern.compile(str, 32).matcher(str2);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static List<String> getAllMatches(String str, String str2) {
        Matcher matcher = Pattern.compile(str, 32).matcher(str2);
        ArrayList arrayList = new ArrayList();
        while (matcher.find()) {
            arrayList.add(matcher.group());
        }
        return arrayList;
    }

    public static boolean hasMatch(String str, String str2) {
        return Pattern.compile(str, 32).matcher(str2).find();
    }

    public static String getSignatureFromUrl(String str) {
        return matchGroup("(?<=&(sig|s|signature)=).*?([^&]{1,})", str);
    }

    public static String putDechipheredPart(String str, String str2) {
        return str.replaceFirst("(?<=&(sig|s|signature)=).*?([^&]{1,})", "signature=" + str2);
    }
}
