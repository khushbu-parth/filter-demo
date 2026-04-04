package com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class IncomingCallReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        BoloCallHandler.getInstance().onIncomingCallRecived(context, intent);
    }
}
