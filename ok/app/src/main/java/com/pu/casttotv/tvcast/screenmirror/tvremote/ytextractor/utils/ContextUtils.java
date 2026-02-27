package com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

public class ContextUtils {
    public static Context context;

    public static void init(Context context2) {
        context = context2;
    }

    public static void CopytoClip(String str) {
        ((ClipboardManager) context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("clipboard", str));
        Toast.makeText(context, "Copied", 0).show();
    }
}
