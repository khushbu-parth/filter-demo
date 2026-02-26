package com.cast.tv.screen.mirroring.screencasting.Utils.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class NetStateChangeReceiver extends BroadcastReceiver {

    public static void registerReceiver(Context context) {
        context.registerReceiver(InstanceHolder.INSTANCE, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public static void unRegisterReceiver(Context context) {
        context.unregisterReceiver(InstanceHolder.INSTANCE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            NetUtil.mNetworkType.setValue(NetUtil.getNetworkType(context));
        }
    }

    private static class InstanceHolder {
        private static final NetStateChangeReceiver INSTANCE = new NetStateChangeReceiver();

        private InstanceHolder() {
        }
    }
}
