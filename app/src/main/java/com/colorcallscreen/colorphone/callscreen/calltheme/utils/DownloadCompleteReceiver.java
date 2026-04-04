package com.colorcallscreen.colorphone.callscreen.calltheme.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class DownloadCompleteReceiver extends BroadcastReceiver {
    public static final String LOCAL_DOWNLOAD_ACTION = "download_complete";
    private static DownloadListener listener;

    
    public interface DownloadListener {
        void onDownloadComplete(Intent intent);
    }

    public static void setListener(DownloadListener downloadListener) {
        listener = downloadListener;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        intent.setAction(LOCAL_DOWNLOAD_ACTION);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
