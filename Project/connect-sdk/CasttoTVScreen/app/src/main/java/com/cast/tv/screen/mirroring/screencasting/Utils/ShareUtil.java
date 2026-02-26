package com.cast.tv.screen.mirroring.screencasting.Utils;

import android.content.Context;
import android.content.Intent;

public class ShareUtil {
    private static final String APP_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=console.casttotv.screenconnect.mirroring";

    public static void safedk_Context_startActivity_97cb3195734cf5c9cc3418feeafa6dd6(Context p0, Intent p1) {
        if (p1 == null) {
            return;
        }
        p0.startActivity(p1);
    }

    public static void shareApp(Context context, String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", APP_DOWNLOAD_URL);
        safedk_Context_startActivity_97cb3195734cf5c9cc3418feeafa6dd6(context, Intent.createChooser(intent, str));
    }
}
