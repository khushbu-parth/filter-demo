package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.browser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/* loaded from: classes4.dex */
public class WebBroadCast extends BroadcastReceiver {
    private FileBrowserActivity mActivity;
    private Intent mService;

    public static void onServerStart(Context context, String str, int i) {
        sendBroadcast(context, 1, str, i);
    }

    private static void sendBroadcast(Context context, int i, String str, int i2) {
        Intent intent = new Intent("com.product.webserver.receiver");
        intent.putExtra("CMD_KEY", i);
        intent.putExtra("MESSAGE_KEY", str);
        intent.putExtra("PORT_KEY", i2);
        context.sendBroadcast(intent);
    }

    public WebBroadCast(FileBrowserActivity fileBrowserActivity) {
        this.mActivity = fileBrowserActivity;
        this.mService = new Intent(fileBrowserActivity, WebServer.class);
    }

    public void register() {
        this.mActivity.registerReceiver(this, new IntentFilter("com.product.webserver.receiver"));
    }

    public void startServer() {
        Intent intent;
        try {
            FileBrowserActivity fileBrowserActivity = this.mActivity;
            if (fileBrowserActivity == null || (intent = this.mService) == null) {
                return;
            }
            fileBrowserActivity.startService(intent);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void stopServer() {
        Intent intent;
        try {
            FileBrowserActivity fileBrowserActivity = this.mActivity;
            if (fileBrowserActivity == null || (intent = this.mService) == null) {
                return;
            }
            fileBrowserActivity.stopService(intent);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("com.product.webserver.receiver".equals(intent.getAction())) {
            int intExtra = intent.getIntExtra("CMD_KEY", 0);
            if (intExtra == 1) {
                this.mActivity.onServerStart(intent.getStringExtra("MESSAGE_KEY"), intent.getIntExtra("PORT_KEY", 0));
            } else if (intExtra == 2) {
                this.mActivity.onServerError(intent.getStringExtra("MESSAGE_KEY"));
            } else if (intExtra != 4) {
            } else {
                this.mActivity.onServerStop();
            }
        }
    }
}
