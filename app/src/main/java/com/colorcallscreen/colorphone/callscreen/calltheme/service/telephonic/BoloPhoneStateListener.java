package com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic;

import android.telephony.PhoneStateListener;


public class BoloPhoneStateListener extends PhoneStateListener {
    @Override // android.telephony.PhoneStateListener
    public void onCallStateChanged(int i, String str) {
        super.onCallStateChanged(i, str);
        if (i == 1) {
            BoloCallHandler.getInstance().onPhoneStartedRinging(str);
            return;
        }
        BoloCallHandler.getInstance().onPhoneStopRinging(str);
        if (i == 2) {
            BoloCallHandler.getInstance().onOffHookCall(str);
        }
    }
}
