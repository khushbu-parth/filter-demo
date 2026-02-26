package com.ads.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.view.ViewGroup;

import com.ads.sdk.adsClass.AppOpenManager;
import com.ads.sdk.adsClass.InterstitialLoader;
import com.ads.sdk.adsClass.NativeLoader;
import com.ads.sdk.configs.Config;
import com.ads.sdk.configs.PreferenceManager;
import com.ads.sdk.interfaces.OnShowAdCompleteListener;
import com.ads.sdk.services.VpnService;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAdView;

public class SdkManager {
    private static InterstitialLoader interstitialLoader;
    private static NativeLoader nativeLoader;

    public static void initialize(Application mContext, Class<?> splashClass) {
        PreferenceManager.init(mContext);
        MobileAds.initialize(mContext.getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Config.log("SdkManager", "onInitializationComplete: " + initializationStatus.getAdapterStatusMap());
            }
        });
        new AppOpenManager(mContext, splashClass);
        interstitialLoader = new InterstitialLoader();
        nativeLoader = new NativeLoader();
    }

    public static void loadBanner(Activity activity, ViewGroup view) {
        if (Config.isNetworkAvailable(activity) && PreferenceManager.getAdsFlag() && !PreferenceManager.getBannerID().isEmpty()) {
            if (nativeLoader != null) {
                if (NativeLoader.bannerAdView != null) {
                    if (view != null) {
                        try {
                            view.removeAllViews();
                            view.addView(NativeLoader.bannerAdView);

                            nativeLoader.loadBanner(activity);
                        } catch (IllegalStateException e) {
                            Config.log(SdkManager.class.getName(), "Banner Exception: " + e.toString());
                        }

                    }
                } else {
                    nativeLoader.loadBanner(activity);
                }
            } else {
                nativeLoader = new NativeLoader();
            }
        }
    }

    public static void loadNative(Activity activity, ViewGroup view) {
        if (Config.isNetworkAvailable(activity) && PreferenceManager.getAdsFlag() && !PreferenceManager.getNativeID().isEmpty()) {
            if (nativeLoader != null) {
                if (NativeLoader.nativeAdView != null) {
                    if (view != null) {
                        NativeAdView adView =
                                (NativeAdView) activity.getLayoutInflater().inflate(R.layout.native_medium_view, view, false);
                        nativeLoader.populateNativeAdView(NativeLoader.nativeAdView, adView, false);
                        try {
                            view.removeAllViews();
                            view.addView(adView);

                            nativeLoader.loadNative(activity);
                        } catch (IllegalStateException e) {
                            Config.log(SdkManager.class.getName(), "Native Exception: " + e.toString());
                        }
                    }
                } else {
                    nativeLoader.loadNative(activity);
                }
            } else {
                nativeLoader = new NativeLoader();
            }
        }
    }

    public static void loadNativeBanner(Activity activity, ViewGroup view) {
        if (Config.isNetworkAvailable(activity) && PreferenceManager.getAdsFlag() && !PreferenceManager.getNativeID().isEmpty()) {
            if (nativeLoader != null) {
                if (NativeLoader.nativeBannerAdView != null) {
                    if (view != null) {
                        NativeAdView adView =
                                (NativeAdView) activity.getLayoutInflater().inflate(R.layout.native_small_view, view, false);
                        nativeLoader.populateNativeAdView(NativeLoader.nativeBannerAdView, adView, true);
                        try {
                            view.removeAllViews();
                            view.addView(adView);

                            nativeLoader.loadNativeBanner(activity);
                        } catch (IllegalStateException e) {
                            Config.log(SdkManager.class.getName(), "NativeBanner Exception: " + e.toString());
                        }
                    }
                } else {
                    nativeLoader.loadNativeBanner(activity);
                }
            } else {
                nativeLoader = new NativeLoader();
            }
        }
    }

    public static void showInterstitialAd(Activity activity, OnShowAdCompleteListener listener) {
        if (Config.isNetworkAvailable(activity) && PreferenceManager.getAdsFlag() && !PreferenceManager.getInterstitialID().isEmpty()) {
            if (interstitialLoader != null) {
                interstitialLoader.showAdIfAvailable(activity, listener);
            } else {
                interstitialLoader = new InterstitialLoader();
                listener.onShowAdComplete();
            }
        } else {
            listener.onShowAdComplete();
        }
    }

    public static void loadInterstitialAd(Activity activity) {
        if (Config.isNetworkAvailable(activity) && PreferenceManager.getAdsFlag() && !PreferenceManager.getInterstitialID().isEmpty()) {
            if (interstitialLoader != null) {
                interstitialLoader.loadAd(activity);
            } else {
                interstitialLoader = new InterstitialLoader();
            }
        }
    }

    public static void finalExit(Activity activity) {
        if (Config.isServiceRunning(activity, VpnService.class)) {
            activity.stopService(new Intent(activity, VpnService.class));
        }
        if (NativeLoader.nativeAdView != null) {
            NativeLoader.nativeAdView.destroy();
            NativeLoader.nativeAdView = null;
        }
        if (NativeLoader.nativeBannerAdView != null) {
            NativeLoader.nativeBannerAdView.destroy();
            NativeLoader.nativeBannerAdView = null;
        }
        if (NativeLoader.bannerAdView != null) {
            NativeLoader.bannerAdView.destroy();
            NativeLoader.bannerAdView = null;
        }
        activity.finishAffinity();
    }
}
