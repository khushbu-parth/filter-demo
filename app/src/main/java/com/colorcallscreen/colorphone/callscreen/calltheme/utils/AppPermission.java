package com.colorcallscreen.colorphone.callscreen.calltheme.utils;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;


public class AppPermission {
    public static void checkUpdateOverlayPermissionGiven(final Activity activity) {
        activity.runOnUiThread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.AppPermission.1
            @Override 
            public void run() {
                new Handler().postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.AppPermission.1.1
                    @Override 
                    public void run() {
                        if (!Settings.canDrawOverlays(activity)) {
                            AppPermission.checkUpdateOverlayPermissionGiven(activity);
                            return;
                        }
                        try {
                            activity.finishActivity(102);
                        } catch (Exception unused) {
                        }
                    }
                }, 100L);
            }
        });
    }

    public static void overlayPermission(Activity activity) {
        if (Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(activity)) {
            return;
        }
        Helper.openOverlaySetting(activity);
        checkUpdateOverlayPermissionGiven(activity);
    }
}
