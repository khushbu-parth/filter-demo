package com.cast.tv.screen.mirroring.screencasting.Utils;

import android.text.TextUtils;
import android.util.Log;

import com.cast.tv.screen.mirroring.screencasting.CastApp;

public class L {
    private static final boolean isEnableLog = CastApp.DEBUG;

    private L() {
    }

    public static void v(String str, String str2) {
        logger(str, str2, 1);
    }

    public static void d(String str, String str2) {
        logger(str, str2, 2);
    }

    public static void i(String str, String str2) {
        logger(str, str2, 3);
    }

    public static void w(String str, String str2) {
        logger(str, str2, 4);
    }

    public static void e(String str, String str2) {
        logger(str, str2, 5);
    }

    private static void logger(String str, String str2, int i) {
        if (isEnableLog && !TextUtils.isEmpty(str2)) {
            if (TextUtils.isEmpty(str)) {
                str = "PDFScanner";
            }
            if (i == 1) {
                Log.v(str, str2);
            } else if (i == 2) {
                Log.d(str, str2);
            } else if (i == 3) {
                Log.i(str, str2);
            } else if (i == 4) {
                Log.w(str, str2);
            } else {
                Log.e(str, str2);
            }
        }
    }
}
