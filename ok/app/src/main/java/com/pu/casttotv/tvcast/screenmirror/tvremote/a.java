package com.pu.casttotv.tvcast.screenmirror.tvremote;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes4.dex */
public class a {
    public static StringBuffer B(String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer(str);
        stringBuffer.append(str2);
        return stringBuffer;
    }

    public static StringBuilder G(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        return sb;
    }

    public static StringBuilder H(String str, int i, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(i);
        sb.append(str2);
        return sb;
    }

    public static StringBuilder K(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        return sb;
    }

    public static StringBuilder L(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        return sb;
    }

    public static StringBuilder M(String str, String str2, String str3, String str4, String str5) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        sb.append(str4);
        sb.append(str5);
        return sb;
    }

    public static Set P() {
        return Collections.synchronizedSet(new HashSet());
    }

    public static String r(String str, String str2) {
        return str + str2;
    }

    public static String t(String str, String str2, String str3) {
        return str + str2 + str3;
    }

    public static String y(StringBuilder sb, String str, String str2) {
        sb.append(str);
        sb.append(str2);
        return sb.toString();
    }
}
