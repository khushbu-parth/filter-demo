package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.other;

import android.content.Context;
import android.content.SharedPreferences;

public class SharefUtils {
    private static SharefUtils sharefUtils;
    private Context context;

    public static SharefUtils getInstance(Context context2) {
        if (sharefUtils == null) {
            sharefUtils = new SharefUtils(context2);
        }
        return sharefUtils;
    }

    public SharefUtils(Context context2) {
        this.context = context2;
    }

    public void setHapticFeedBack(boolean z) {
        SharedPreferences.Editor edit = this.context.getSharedPreferences("settings", 0).edit();
        edit.putBoolean("haptic_feedback", z);
        edit.apply();
    }

    public boolean getHapticFeedBack() {
        return this.context.getSharedPreferences("settings", 0).getBoolean("haptic_feedback", true);
    }
}
