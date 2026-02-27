package com.yanzhenjie.andserver.util;

import android.text.TextUtils;
import java.text.ParseException;
import java.util.ArrayList;
import org.apache.httpcore.Header;
import org.apache.httpcore.HttpRequest;

public class HttpRequestParser {
    public static String getRequestPath(HttpRequest httpRequest) {
        String uri = httpRequest.getRequestLine().getUri();
        int indexOf = uri.indexOf("?");
        if (indexOf != -1) {
            uri = uri.substring(0, indexOf);
        } else {
            int indexOf2 = uri.indexOf("#");
            if (indexOf2 != -1) {
                uri = uri.substring(0, indexOf2);
            }
        }
        String[] split = uri.split("/");
        if (split.length <= 1) {
            return uri;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : split) {
            arrayList.add(UrlCoder.urlDecode(str, "utf-8"));
        }
        return TextUtils.join("/", arrayList);
    }

    public static long parseDateHeader(HttpRequest httpRequest, String str) {
        Header firstHeader = httpRequest.getFirstHeader(str);
        if (firstHeader == null) {
            return -1;
        }
        String value = firstHeader.getValue();
        try {
            return DateUtils.parseGMTToMillis(value);
        } catch (ParseException unused) {
            int indexOf = value.indexOf(59);
            if (indexOf == -1) {
                return -1;
            }
            try {
                return DateUtils.parseGMTToMillis(value.substring(0, indexOf));
            } catch (ParseException unused2) {
                return -1;
            }
        }
    }
}
