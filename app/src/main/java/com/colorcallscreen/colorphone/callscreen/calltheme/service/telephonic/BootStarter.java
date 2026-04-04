package com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


public class BootStarter extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        try {
            context.getApplicationContext().registerReceiver(new IncomingCallReceiver(), new IntentFilter("android.intent.action.PHONE_STATE"));
        } catch (Exception unused) {
        }
        BoloCallHandler.getInstance().startPhoneStateService(context);
    }
}
