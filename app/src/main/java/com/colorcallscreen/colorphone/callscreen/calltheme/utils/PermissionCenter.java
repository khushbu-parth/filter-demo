package com.colorcallscreen.colorphone.callscreen.calltheme.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;


public class PermissionCenter {
    public static boolean isAccessibilityEnabled(Context context) {
        return true;
    }

    public static boolean isOverlayPermissionEnabled(Context context) {
        return Build.VERSION.SDK_INT >= 23 && Settings.canDrawOverlays(context);
    }
}
