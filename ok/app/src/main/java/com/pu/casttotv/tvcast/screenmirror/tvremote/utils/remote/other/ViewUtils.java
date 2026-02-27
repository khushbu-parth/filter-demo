package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.other;

import android.content.Context;
import android.os.Vibrator;

public class ViewUtils {
    public static void provideHapticFeedback(Context context, int i) {
        try {
            if (SharefUtils.getInstance(context).getHapticFeedBack()) {
                ((Vibrator) context.getSystemService("vibrator")).vibrate((long) i);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
