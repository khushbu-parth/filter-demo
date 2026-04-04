package com.colorcallscreen.colorphone.callscreen.calltheme.service.components;

import android.content.Context;

import com.colorcallscreen.colorphone.callscreen.calltheme.service.BluetoothHeadsetUtils;


class BluetoothHelper extends BluetoothHeadsetUtils {
    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.BluetoothHeadsetUtils
    public void onHeadsetConnected() {
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.BluetoothHeadsetUtils
    public void onHeadsetDisconnected() {
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.BluetoothHeadsetUtils
    public void onScoAudioConnected() {
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.service.BluetoothHeadsetUtils
    public void onScoAudioDisconnected() {
    }

    public BluetoothHelper(Context context) {
        super(context);
    }
}
