package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.model.Device;

public class PreferenceUtils {
    public static Device getConnectedDevice(Context context) throws Exception {
        Device device = DBUtils.getDevice(context, PreferenceManager.getDefaultSharedPreferences(context).getString("serial_number", null));
        if (device != null) {
            return device;
        }
        throw new Exception("Device not connected");
    }
}
