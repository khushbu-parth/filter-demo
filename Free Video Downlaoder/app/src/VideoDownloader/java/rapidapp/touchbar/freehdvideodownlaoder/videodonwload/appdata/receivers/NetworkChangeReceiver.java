package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.NetworkUtil;


public class NetworkChangeReceiver extends BroadcastReceiver {
    private static OnNetoworkChangeListener mTheListener;

    public interface OnNetoworkChangeListener {
        void onConnected();

        void onDisConnected();
    }

    public static void setOnNetoworkChangeListener(OnNetoworkChangeListener onNetoworkChangeListener) {
        mTheListener = onNetoworkChangeListener;
    }

    public void onReceive(Context context, Intent intent) {
        if (NetworkUtil.getConnectivityStatusString(context).equals("Notconnected")) {
            try {
                if (mTheListener != null) {
                    mTheListener.onDisConnected();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (mTheListener != null) {
                    mTheListener.onConnected();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
