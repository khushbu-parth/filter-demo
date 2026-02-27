package com.yanzhenjie.andserver.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class UrlCoder {
    public static String urlDecode(String str, String str2) {
        try {
            return URLDecoder.decode(str, str2);
        } catch (UnsupportedEncodingException unused) {
            return str;
        }
    }
}
