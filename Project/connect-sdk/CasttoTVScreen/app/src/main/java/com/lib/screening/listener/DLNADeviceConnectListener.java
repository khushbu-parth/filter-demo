package com.lib.screening.listener;

import com.lib.screening.bean.DeviceInfo;

public interface DLNADeviceConnectListener {
    public static final int CONNECT_INFO_DISCONNECT_SUCCESS = 212001;

    void onConnect(DeviceInfo deviceInfo, int i);

    void onDisconnect(DeviceInfo deviceInfo, int i, int i2);
}
