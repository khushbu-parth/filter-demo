package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.webkit.URLUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes4.dex */
public class Commons {
    public static List<String> extractUrls(String str) {
        ArrayList arrayList = new ArrayList();
        String trim = str.trim();
        if (URLUtil.isValidUrl(trim)) {
            arrayList.add(trim);
        } else {
            Matcher matcher = Pattern.compile("\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)(\\w+:)?(([-\\w]+\\.)+(com|org|net|gov|mil|biz|info|mobi|name|aero|jobs|museum|travel|[a-z]{2}))(:[\\d]{1,5})?(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b").matcher(trim);
            while (matcher.find()) {
                arrayList.add(matcher.group());
            }
        }
        return arrayList;
    }
}
