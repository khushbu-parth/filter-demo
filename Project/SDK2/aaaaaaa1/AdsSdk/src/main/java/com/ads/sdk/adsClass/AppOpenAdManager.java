package com.ads.sdk.adsClass;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.ads.sdk.configs.Config;
import com.ads.sdk.configs.PreferenceManager;
import com.ads.sdk.interfaces.OnShowAdCompleteListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

public class AppOpenAdManager {

    private static final String LOG_TAG = "AppOpenAdManager";

    private AppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    public static boolean isShowingAd = false;

    private long loadTime = 0;

    public AppOpenAdManager() {
    }

    public void loadSplashAd(Activity activity, OnShowAdCompleteListener listener) {
        AdRequest request = new AdRequest.Builder().build();
        com.google.android.gms.ads.appopen.AppOpenAd.load(
                activity,
                PreferenceManager.getAppOpenID(),
                request,
                new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(com.google.android.gms.ads.appopen.AppOpenAd ad) {
                        Config.log(LOG_TAG, "onAdLoaded.");

                        appOpenAd = ad;
                        isLoadingAd = false;
                        loadTime = (new Date()).getTime();

                        appOpenAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        Config.log(LOG_TAG, "onAdDismissedFullScreenContent.");
                                        appOpenAd = null;
                                        isShowingAd = false;
                                        listener.onShowAdComplete();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        Config.log(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
                                        appOpenAd = null;
                                        isShowingAd = false;
                                        listener.onShowAdComplete();
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        Config.log(LOG_TAG, "onAdShowedFullScreenContent.");
                                    }
                                });
                        isShowingAd = true;
                        appOpenAd.show(activity);
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Config.log(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                        appOpenAd = null;
                        isLoadingAd = false;
                        listener.onShowAdComplete();
                    }
                });
    }

    public void loadAd(Activity context) {
        if (isLoadingAd || isAdAvailable()) {
            Config.log(LOG_TAG, "already available.");
            return;
        }

        isLoadingAd = true;
        AdRequest request = new AdRequest.Builder().build();
        com.google.android.gms.ads.appopen.AppOpenAd.load(
                context,
                PreferenceManager.getAppOpenID(),
                request,
                new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {

                    @Override
                    public void onAdLoaded(com.google.android.gms.ads.appopen.AppOpenAd ad) {
                        Config.log(LOG_TAG, "onAdLoaded.");
                        appOpenAd = ad;
                        isLoadingAd = false;
                        loadTime = (new Date()).getTime();
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Config.log(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                        appOpenAd = null;
                        isLoadingAd = false;
                    }
                });
    }

    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    private boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    public void showAdIfAvailable(
            @NonNull Activity activity, @NonNull Class<?> splashClass) {
        if (isShowingAd || InterstitialLoader.isShowingAd) {
            Config.log(LOG_TAG, "The app open ad is already showing.");
            return;
        }

        if (!isAdAvailable()) {
            Config.log(LOG_TAG, "The app open ad is not ready yet.");
            loadAd(activity);
            return;
        }

        if (activity.getClass().equals(splashClass)) {
            Config.log(LOG_TAG, "Splash activity running");
            return;
        }

        Config.log(LOG_TAG, "Will show ad.");
        appOpenAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Config.log(LOG_TAG, "onAdDismissedFullScreenContent.");
                        appOpenAd = null;
                        isShowingAd = false;
                        loadAd(activity);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        Config.log(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
                        appOpenAd = null;
                        isShowingAd = false;
                        loadAd(activity);
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        Config.log(LOG_TAG, "onAdShowedFullScreenContent.");
                    }
                });

        isShowingAd = true;
        appOpenAd.show(activity);
    }

}
