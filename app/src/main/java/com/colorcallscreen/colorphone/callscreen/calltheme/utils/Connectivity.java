package com.colorcallscreen.colorphone.callscreen.calltheme.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Connectivity {
    public static boolean isConnectionFast(int i, int i2) {
        if (i == 1) {
            return true;
        }
        if (i == 0) {
            switch (i2) {
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 13:
                case 14:
                case 15:
                    return true;
                case 4:
                case 7:
                case 11:
                default:
                    return false;
            }
        }
        return false;
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
    }
}
