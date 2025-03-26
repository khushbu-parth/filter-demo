package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.WebApplication;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.receivers.NetworkChangeReceiver;

public class NetworkUtil {
    private static final int TYPE_MOBILE = 2;
    private static final int TYPE_NOT_CONNECTED = 0;
    private static final int TYPE_WIFI = 1;

    private static int getConnectivityStatus(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return 0;
        }
        if (activeNetworkInfo.getType() == 1) {
            return 1;
        }
        return activeNetworkInfo.getType() == 0 ? 2 : 0;
    }

    public static String getConnectivityStatusString(Context context) {
        int connectivityStatus = getConnectivityStatus(context);
        NetworkChangeReceiver.setOnNetoworkChangeListener(WebApplication.getInstance().downloadService);
        if (connectivityStatus == 1) {
            return "Wifi enabled";
        }
        if (connectivityStatus == 2) {
            return "Mobile data enabled";
        }
        if (connectivityStatus == 0) {
            return "Notconnected";
        }
        return null;
    }

    public static boolean getMobileConnectivityStatus(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 0;
    }

    public static boolean isConnected() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) WebApplication.getInstance().getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null || (activeNetworkInfo.getType() != 1 && activeNetworkInfo.getType() != 0)) {
            return false;
        }
        return true;
    }
}
