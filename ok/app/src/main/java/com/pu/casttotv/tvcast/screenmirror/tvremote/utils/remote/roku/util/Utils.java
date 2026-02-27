package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.util;

import android.os.Build;

public class Utils {
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= 11;
    }
}
