package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.content.Context;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;

/* loaded from: classes4.dex */
public class URLAddFilter {
    public static boolean IsContainsAdURL(Context context, String str) {
        String lowerCase = str.toLowerCase();
        for (String str2 : context.getResources().getStringArray(R.array.ad_site_filters)) {
            if (lowerCase.contains(str2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean DoNotCheckIf(Context context, String str) {
        String lowerCase = str.toLowerCase();
        for (String str2 : context.getResources().getStringArray(R.array.ristricted_sites)) {
            if (lowerCase.contains(str2)) {
                return true;
            }
        }
        return false;
    }
}
