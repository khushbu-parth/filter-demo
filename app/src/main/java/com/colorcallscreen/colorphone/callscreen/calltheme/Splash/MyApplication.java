package com.colorcallscreen.colorphone.callscreen.calltheme.Splash;

import com.ads.control.admob.Admob;
import com.ads.control.admob.AppOpenManager;
import com.ads.control.ads.AperoAd;
import com.ads.control.billing.AppPurchase;
import com.ads.control.config.AdjustConfig;
import com.ads.control.config.AperoAdConfig;
import com.ads.control.config.AppsflyerConfig;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends BoloApplication {

    private static MyApplication context;

    public static MyApplication getApplication() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        Admob.getInstance().setNumToShowAds(0);

        initAds();

    }

    private void initAds() {
        String environment = true ? AperoAdConfig.ENVIRONMENT_DEVELOP : AperoAdConfig.ENVIRONMENT_PRODUCTION;
        aperoAdConfig = new AperoAdConfig(this, AperoAdConfig.PROVIDER_ADMOB, environment);

        // Optional: enable ads resume
        aperoAdConfig.setIdAdResume(getContext().getResources().getString(R.string.ads_app_open));

        // Optional: setup list device test - recommended to use
        listTestDevice.add("EC25F576DA9B6CE74778B268CB87E431");
        aperoAdConfig.setListDeviceTest(listTestDevice);

        AperoAd.getInstance().init(this, aperoAdConfig, false);

        // Auto disable ad resume after user click ads and back to app
        Admob.getInstance().setDisableAdResumeWhenClickAds(true);
        // If true -> onNextAction() is called right after Ad Interstitial showed
        Admob.getInstance().setOpenActivityAfterShowInterAds(true);
    }
}
