package com.cast.tv.screen.mirroring.screencasting.Callback;

import com.lib.screening.bean.DeviceInfo;

import java.util.List;


public interface DLNADeviceChangeCallback {
    void onDeviceChange(List<DeviceInfo> list);
}
