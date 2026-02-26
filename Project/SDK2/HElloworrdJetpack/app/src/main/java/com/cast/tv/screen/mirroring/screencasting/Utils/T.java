package com.cast.tv.screen.mirroring.screencasting.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.cast.tv.screen.mirroring.screencasting.CastApp;

public class T {
    private static boolean isShow = true;

    private T() {
    }

    public static void showShort(Context context, String str) {
        if (isShow) {
            toast(context, str, 0);
        }
    }

    public static void showLong(Context context, String str) {
        if (isShow) {
            toast(context, str, 1);
        }
    }

    public static void alwaysShow(Context context, String str) {
        toast(context, str, 0);
    }

    private static void toast(Context context, String str, int i) {
        if (context == null) {
            context = CastApp.mContext;
        }
        if (i < 0) {
            i = 0;
        }
        if (str == null || TextUtils.isEmpty(str)) {
            return;
        }
        Toast.makeText(context, str, i).show();
    }
}
