package com.cast.tv.screen.mirroring.screencasting.Utils.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.lifecycle.MutableLiveData;

import com.cast.tv.screen.mirroring.screencasting.CastApp;
import com.cast.tv.screen.mirroring.screencasting.Utils.L;

public class NetUtil {
    private static final String TAG = "NetUtil";
    public static MutableLiveData<NetworkType> mNetworkType = new MutableLiveData<>();

    private NetUtil() {
    }

    public static String getConnectWifiSsid() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) CastApp.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return "No network";
        }
        String typeName = activeNetworkInfo.getTypeName();
        L.d(TAG, "typeName: " + typeName);
        return typeName.toLowerCase().contains("wifi") ? getConnectWifiSsid(CastApp.mContext) : typeName;
    }

    private static String getConnectWifiSsid(Context context) {
        WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
        L.d("wifiInfo", connectionInfo.toString());
        L.d("SSID 1 ", connectionInfo.getSSID());
        return connectionInfo.getSSID().replace("\"", "");
    }

    private static NetworkInfo getActiveNetworkInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
    }

    public static NetworkType getNetworkType(Context context) {
        NetworkType networkType;
        NetworkType networkType2 = NetworkType.NETWORK_NO;
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
            return networkType2;
        }
        if (activeNetworkInfo.getType() == 1) {
            return NetworkType.NETWORK_WIFI;
        }
        if (activeNetworkInfo.getType() == 0) {
            switch (activeNetworkInfo.getSubtype()) {
                case 1:
                case 2:
                case 4:
                case 7:
                case 11:
                case 16:
                    networkType = NetworkType.NETWORK_2G;
                    break;
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 14:
                case 15:
                case 17:
                    networkType = NetworkType.NETWORK_3G;
                    break;
                case 13:
                case 18:
                    networkType = NetworkType.NETWORK_4G;
                    break;
                default:
                    String subtypeName = activeNetworkInfo.getSubtypeName();
                    if (subtypeName.equalsIgnoreCase("TD-SCDMA") || subtypeName.equalsIgnoreCase("WCDMA") || subtypeName.equalsIgnoreCase("CDMA2000")) {
                        networkType = NetworkType.NETWORK_4G;
                        break;
                    } else {
                        networkType = NetworkType.NETWORK_UNKNOWN;
                        break;
                    }
            }
            return networkType;
        }
        return NetworkType.NETWORK_UNKNOWN;
    }
}
