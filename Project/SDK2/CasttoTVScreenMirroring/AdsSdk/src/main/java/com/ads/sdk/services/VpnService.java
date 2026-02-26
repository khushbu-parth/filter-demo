package com.ads.sdk.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.ads.sdk.adsClass.NativeLoader;
import com.ads.sdk.configs.Config;

public class VpnService extends Service {

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Config.log("VpnService", "onTaskRemoved: ");
        if (NativeLoader.nativeAdView != null){
            NativeLoader.nativeAdView.destroy();
        }
        if (NativeLoader.nativeBannerAdView != null){
            NativeLoader.nativeBannerAdView.destroy();
        }
        if (NativeLoader.bannerAdView != null){
            NativeLoader.bannerAdView.destroy();
        }

        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
