package com.yanzhenjie.andserver;

import android.text.TextUtils;
import java.util.Locale;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpTrace;

public enum RequestMethod {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH(HttpPatch.METHOD_NAME),
    DELETE("DELETE"),
    OPTIONS(HttpOptions.METHOD_NAME),
    TRACE(HttpTrace.METHOD_NAME);
    
    private String method;

    private RequestMethod(String str) {
        this.method = str;
    }

    public String getValue() {
        return this.method;
    }

    public static RequestMethod reverse(String str) {
        if (TextUtils.isEmpty(str)) {
            return GET;
        }
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        upperCase.hashCode();
        char c = 65535;
        switch (upperCase.hashCode()) {
            case -531492226:
                if (upperCase.equals(HttpOptions.METHOD_NAME)) {
                    c = 0;
                    break;
                }
                break;
            case 70454:
                if (upperCase.equals("GET")) {
                    c = 1;
                    break;
                }
                break;
            case 79599:
                if (upperCase.equals("PUT")) {
                    c = 2;
                    break;
                }
                break;
            case 2213344:
                if (upperCase.equals("HEAD")) {
                    c = 3;
                    break;
                }
                break;
            case 2461856:
                if (upperCase.equals("POST")) {
                    c = 4;
                    break;
                }
                break;
            case 75900968:
                if (upperCase.equals(HttpPatch.METHOD_NAME)) {
                    c = 5;
                    break;
                }
                break;
            case 80083237:
                if (upperCase.equals(HttpTrace.METHOD_NAME)) {
                    c = 6;
                    break;
                }
                break;
            case 2012838315:
                if (upperCase.equals("DELETE")) {
                    c = 7;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return OPTIONS;
            case 1:
                return GET;
            case 2:
                return PUT;
            case 3:
                return HEAD;
            case 4:
                return POST;
            case 5:
                return PATCH;
            case 6:
                return TRACE;
            case 7:
                return DELETE;
            default:
                return GET;
        }
    }
}
