package com.adsdemo.vdapps.adsload;

import static com.adsdemo.vdapps.adsload.AdsManager.getMyCustomAd;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.work.PeriodicWorkRequest;

import com.adsdemo.vdapps.adsload.activity.Ad_CustomAppopenActivity;
import com.adsdemo.vdapps.adsload.interfaces.MyCallback;
import com.adsdemo.vdapps.adsload.models.CustomAdModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    public static boolean isShowingAd;
    public AppOpenAd appOpenAd = null;
    public CountDownTimer countDownTimerFistTime = new CountDownTimer(PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, 10) {

        public void onFinish() {
        }

        public void onTick(long j9) {
            if (AdsManager.getAllOnlineData) {
                AppOpenManager.this.fetchAd();
                AppOpenManager.this.countDownTimerFistTime.cancel();
            }
        }
    };
    private Activity currentActivity;
    public AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private long loadTime = 0;
    MyCallback myCallback = null;
    private final Application myApplication;

    public AppOpenManager(Application application) {
        this.myApplication = application;
        application.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    public void fail_fetchAd() {
        if (!isAdAvailable()) {
            this.loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    loadAdError.getMessage();
                }

                public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                    super.onAdLoaded(appOpenAd);
                    AppOpenManager appOpenManager = AppOpenManager.this;
                    appOpenManager.appOpenAd = appOpenAd;
                    appOpenManager.loadTime = new Date().getTime();
                }
            };

            if (AdsManager.googleAdsStutas == 1) {
                if (!AdsManager.isSplash) {
                    if (!AdsManager.googleAppOpen.equals("")) {
                        AppOpenAd.load(this.myApplication, AdsManager.googleAppOpen, getAdRequest(), 1, this.loadCallback);
                    }else if (!AdsManager.googleAppOpen2.equals("")) {
                        AppOpenAd.load(this.myApplication, AdsManager.googleAppOpen2, getAdRequest(), 1, this.loadCallback);
                    }else if (AdsManager.customAds == 1 || AdsManager.customAds == 2) {
                        CustomAdModel customAdModel = getMyCustomAd();
                        Ad_CustomAppopenActivity.newIntent(currentActivity, myCallback, customAdModel);
                    }
                }else {
                    if (!AdsManager.googleAppOpen2.equals("")) {
                        AppOpenAd.load(this.myApplication, AdsManager.googleAppOpen2, getAdRequest(), 1, this.loadCallback);
                    } else if (!AdsManager.googleAppOpen.equals("")) {
                        AppOpenAd.load(this.myApplication, AdsManager.googleAppOpen, getAdRequest(), 1, this.loadCallback);
                    } else if (AdsManager.customAds == 1 || AdsManager.customAds == 2) {
                        CustomAdModel customAdModel = getMyCustomAd();
                        Ad_CustomAppopenActivity.newIntent(currentActivity, myCallback, customAdModel);
                    }
                }
            } else {
                if (!AdsManager.isSplash) {
                    if (AdsManager.customAds == 1 || AdsManager.customAds == 2) {
                        CustomAdModel customAdModel = getMyCustomAd();
                        Ad_CustomAppopenActivity.newIntent(currentActivity, myCallback, customAdModel);
                    }
                }
            }
        }
    }

    public void fetchAd() {
        if (!isAdAvailable()) {
            this.loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    AppOpenManager.this.fail_fetchAd();
                    loadAdError.getMessage();
                }

                public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                    super.onAdLoaded(appOpenAd);
                    AppOpenManager appOpenManager = AppOpenManager.this;
                    appOpenManager.appOpenAd = appOpenAd;
                    appOpenManager.loadTime = new Date().getTime();
                }
            };

            if (AdsManager.googleAdsStutas == 1) {
                if (!AdsManager.isSplash) {
                    if (!AdsManager.googleAppOpen.equals("")) {
                        AppOpenAd.load(this.myApplication, AdsManager.googleAppOpen, getAdRequest(), 1, this.loadCallback);
                    }else if (!AdsManager.googleAppOpen2.equals("")) {
                        AppOpenAd.load(this.myApplication, AdsManager.googleAppOpen2, getAdRequest(), 1, this.loadCallback);
                    }else if (AdsManager.customAds == 1 || AdsManager.customAds == 2) {
                        CustomAdModel customAdModel = getMyCustomAd();
                        Ad_CustomAppopenActivity.newIntent(currentActivity, myCallback, customAdModel);
                    }
                }else {
                    if (!AdsManager.googleAppOpen2.equals("")) {
                        AppOpenAd.load(this.myApplication, AdsManager.googleAppOpen2, getAdRequest(), 1, this.loadCallback);
                    } else if (!AdsManager.googleAppOpen.equals("")) {
                        AppOpenAd.load(this.myApplication, AdsManager.googleAppOpen, getAdRequest(), 1, this.loadCallback);
                    } else if (AdsManager.customAds == 1 || AdsManager.customAds == 2) {
                        CustomAdModel customAdModel = getMyCustomAd();
                        Ad_CustomAppopenActivity.newIntent(currentActivity, myCallback, customAdModel);
                    }
                }
            } else {
                if (!AdsManager.isSplash) {
                    if (AdsManager.customAds == 1 || AdsManager.customAds == 2) {
                        CustomAdModel customAdModel = getMyCustomAd();
                        Ad_CustomAppopenActivity.newIntent(currentActivity, myCallback, customAdModel);
                    }
                }
            }
        }
    }

    public boolean isAdAvailable() {
        return this.appOpenAd != null;
    }

    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
    }

    public void onActivityDestroyed(@NonNull Activity activity) {
        this.currentActivity = null;
    }

    public void onActivityPaused(@NonNull Activity activity) {
    }

    public void onActivityResumed(@NonNull Activity activity) {
        this.currentActivity = activity;
    }

    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
    }

    public void onActivityStarted(@NonNull Activity activity) {
        this.currentActivity = activity;
    }

    public void onActivityStopped(@NonNull Activity activity) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        showAdIfAvailable();
    }

    public void showAdIfAvailable() {
        if (isShowingAd || !isAdAvailable()) {
            this.countDownTimerFistTime.start();
        } else {
            this.appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                @Override
                public void onAdDismissedFullScreenContent() {
                    AppOpenManager appOpenManager = AppOpenManager.this;
                    appOpenManager.appOpenAd = null;
                    AppOpenManager.isShowingAd = false;
                    appOpenManager.fetchAd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    AppOpenManager.isShowingAd = true;
                }
            });
            if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                this.appOpenAd.show(this.currentActivity);
            }
        }
    }
}
