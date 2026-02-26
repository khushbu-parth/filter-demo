package com.ads.sdk.adsClass;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ads.sdk.configs.Config;
import com.ads.sdk.configs.PreferenceManager;
import com.ads.sdk.interfaces.OnShowAdCompleteListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class InterstitialLoader {
    private static final String LOG_TAG = "InterstitialLoader";

    private InterstitialAd interstitialAd;

    private boolean isLoadingAd = false;
    public static boolean isShowingAd = false;

    private int showCount = 0;

    public void loadAd(Activity activity) {
        if (isLoadingAd || isAdAvailable()) {
            return;
        }

        isLoadingAd = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                activity,
                PreferenceManager.getInterstitialID(),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        Log.i(LOG_TAG, "onAdLoaded");
                        interstitialAd = ad;
                        isLoadingAd = false;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.i(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                        interstitialAd = null;
                        isLoadingAd = false;
                    }
                });
    }

    private boolean isAdAvailable() {
        return interstitialAd != null;
    }

    public void showAdIfAvailable(
            @NonNull Activity activity,
            @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        if (isShowingAd) {
            Config.log(LOG_TAG, "The app open ad is already showing.");
            return;
        }

        if (!isAdAvailable()) {
            Config.log(LOG_TAG, "The app open ad is not ready yet.");
            onShowAdCompleteListener.onShowAdComplete();
            loadAd(activity);
            return;
        }

        if (showCount > PreferenceManager.getInterstitialFrequency() || showCount == PreferenceManager.getInterstitialFrequency()) {
            Config.log(LOG_TAG, "Will show ad.");

            showCount = 0;
            interstitialAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            Config.log(LOG_TAG, "The ad was dismissed.");

                            interstitialAd = null;
                            isShowingAd = false;
                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            Config.log(LOG_TAG, "The ad failed to show.");

                            interstitialAd = null;
                            isShowingAd = false;
                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            Config.log(LOG_TAG, "The ad was shown.");
                        }
                    });
            isShowingAd = true;
            interstitialAd.show(activity);
        } else {
            showCount++;
            onShowAdCompleteListener.onShowAdComplete();
        }
    }
}
