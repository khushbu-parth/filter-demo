package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils;

import android.content.Context;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;

public class CommandHelper {
    public static String getDeviceURL(Context context) {
        if (TVConnectUtils.getInstance().getConnectableDevice() == null) {
            return "";
        }
        String ipAddress = TVConnectUtils.getInstance().getConnectableDevice().getIpAddress();
        return "http://" + ipAddress + ":8060";
    }

    public static String getIconURL(Context context, String str) {
        if (TVConnectUtils.getInstance().getConnectableDevice() == null) {
            return "";
        }
        return ("http://" + TVConnectUtils.getInstance().getConnectableDevice().getIpAddress() + ":8060") + "/query/icon/" + str;
    }
}
