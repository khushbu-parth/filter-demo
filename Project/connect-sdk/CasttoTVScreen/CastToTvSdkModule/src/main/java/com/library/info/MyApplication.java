
package com.library.info;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback;

import java.util.Date;
import java.util.List;

import static com.library.info.CastTvAppManager.ADMOB_AO;
import static com.library.info.CastTvAppManager.app_adShowStatus;
import static com.library.info.CastTvAppManager.app_customAdStatus;
import static com.library.info.CastTvAppManager.app_customAppOpenStatus;
import static com.library.info.CustomAppOpenAdCastTv.isactivitystart;

public class MyApplication extends Application implements ActivityLifecycleCallbacks, LifecycleObserver {
    public static AppOpenAdManager appOpenAdManager;
    public Activity currentActivity;
    String appopenid = "";
    private boolean isActivityPausedAfter = false;
    public static String admobAppOpenLoadingStatus = "show";
    public static boolean isappbackground = false;

    @Override
    public void onCreate() {
        super.onCreate();


        this.registerActivityLifecycleCallbacks(this);


        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        appOpenAdManager = new AppOpenAdManager();
    }


    /**
     * LifecycleObserver method that shows the app open ad when the app moves to foreground.
     */
    @OnLifecycleEvent(Event.ON_START)
    protected void onMoveToForeground() {
        isappbackground = false;
        Log.e("admobAppOpen", "onMoveToForeground");

    }

    /**
     * ActivityLifecycleCallback methods.
     */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        isActivityPausedAfter = false;
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;
        Log.e("admobAppOpen", "isactivitystart :: " + !isactivitystart);
        Log.e("admobAppOpen", "isActivityPausedAfter :: " + isActivityPausedAfter);

        if (isActivityPausedAfter && !isactivitystart) {
            Log.e("admobAppOpen", "onActivityResumed if");
            appOpenAdManager.showAdIfAvailable(currentActivity);
        } else {
            Log.e("admobAppOpen", "onActivityResumed else");
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        isActivityPausedAfter = false;

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @OnLifecycleEvent(Event.ON_STOP)
    public void onAppBackgrounded() {
        Log.e("admobAppOpen", "onAppBackgrounded: 6");
        isActivityPausedAfter = true;
        isappbackground = true;
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;
    }

    public interface OnShowAdCompleteListener {
        void onShowAdComplete();

        void onShowAdFail();
    }

    public class AppOpenAdManager {
        public AppOpenAd appOpenAd = null;
        private boolean isShowingAd = false;


        private long loadTime = 0;


        public AppOpenAdManager() {

        }

        public void loadAd(Context context) {

            if (!admobAppOpenLoadingStatus.equals("show") && !admobAppOpenLoadingStatus.equals("fail")) {
                Log.e("admobAppOpen", "appopen return" + admobAppOpenLoadingStatus);
                return;
            }

            if (app_adShowStatus == 1) {
                appopenid = ADMOB_AO[0];
                Log.d("admobAppOpen", "log_2");
            } else {
                Log.d("admobAppOpen", "log_3");
                return;
            }
            if (appopenid == null || appopenid.isEmpty()) {
                return;
            }

            Log.d("admobAppOpen", "log_7");

            admobAppOpenLoadingStatus = "loading";
            Log.e("admobAppOpen", "appopen request");
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(context, appopenid,
                    request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    new AppOpenAdLoadCallback() {

                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            appOpenAd = ad;
                            loadTime = (new Date()).getTime();
                            Log.d("admobAppOpen", "log_8");
                            admobAppOpenLoadingStatus = "loaded";
                            Log.e("admobAppOpen", "appopen onAdLoaded : " + admobAppOpenLoadingStatus);
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            appOpenAd = null;
                            Log.d("admobAppOpen", "log_9");
                            admobAppOpenLoadingStatus = "fail";
                            Log.e("admobAppOpen", "appopen onAdFailedToLoad : " + admobAppOpenLoadingStatus);
                        }
                    });
        }

        public void loadAdSplash(Context context, final CastTvAppOpenCallBackListener myCallback1) {
            if (!admobAppOpenLoadingStatus.equals("show") && !admobAppOpenLoadingStatus.equals("fail")) {
                Log.e("admobAppOpen", "appopen return" + admobAppOpenLoadingStatus);
                return;
            }


            if (app_adShowStatus == 1) {
                appopenid = ADMOB_AO[0];
                Log.d("admobAppOpen", "log_2");
            } else {
                Log.d("admobAppOpen", "log_3");
                return;
            }


            Log.d("admobAppOpen", "log_7");
            admobAppOpenLoadingStatus = "loading";
            Log.e("admobAppOpen", "appopen first loading");
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(context, appopenid,
                    request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    new AppOpenAdLoadCallback() {

                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            appOpenAd = ad;
                            loadTime = (new Date()).getTime();
                            Log.d("admobAppOpen_", "log_8");
                            admobAppOpenLoadingStatus = "loaded";
                            Log.e("admobAppOpen", "appopen First onAdLoaded : " + admobAppOpenLoadingStatus);
                            if (myCallback1 != null) {
                                myCallback1.callbackCall(1);
                            }
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            Log.d("admobAppOpen_", "log_9");
                            admobAppOpenLoadingStatus = "fail";
                            Log.e("admobAppOpen", "appopen First onAdFailedToLoad : " + admobAppOpenLoadingStatus);
                            if (myCallback1 != null) {
                                myCallback1.callbackCall(0);
                            }
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


        public void showAdIfAvailable(@NonNull final Activity activity) {
            try {
                showAdIfAvailable(
                        activity,
                        new OnShowAdCompleteListener() {
                            @Override
                            public void onShowAdComplete() {
                                // Empty because the user will go back to the activity that shows the ad.
                            }

                            @Override
                            public void onShowAdFail() {
                                if (app_adShowStatus == 1 && app_customAdStatus == 1 && app_customAppOpenStatus == 1) {
                                    ActivityManager am = (ActivityManager) activity.getSystemService(ACTIVITY_SERVICE);
                                    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                                    if (!taskInfo.get(0).topActivity.getClassName().equals(BuildConfig.LIBRARY_PACKAGE_NAME + ".CustomAppOpenAds_Activity")) {
                                        CastTvCustomModel customModel = CastTvAppManager.getInstance(activity).getMyCustomAd();
                                        if (customModel != null) {
                                            CustomAppOpenAdCastTv.newIntentResume(activity, new OnShowAdCompleteListener() {
                                                @Override
                                                public void onShowAdComplete() {

                                                }

                                                @Override
                                                public void onShowAdFail() {

                                                }
                                            }, customModel);
                                        }
                                    }
                                }
                            }
                        });
            } catch (Exception e) {
                Log.e("admobAppOpen__e", " :: " + e.getMessage());
            }

        }

        public void showAdIfAvailable(@NonNull final Activity activity, @NonNull final OnShowAdCompleteListener onShowAdCompleteListener) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.e("admobAppOpen", "showAdIfAvailable: isShowingAd " + isShowingAd);
                onShowAdCompleteListener.onShowAdFail();
                return;
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                onShowAdCompleteListener.onShowAdFail();
                loadAd(activity);
                Log.e("admobAppOpen", "showAdIfAvailable: !isAdAvailable " + !isAdAvailable());
                return;
            }

            appOpenAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        /** Called when full screen content is dismissed. */
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            isShowingAd = false;
                            if (!isappbackground) {
                                Log.e("admobAppOpen", "isappbackground: " + isappbackground);
                                onShowAdCompleteListener.onShowAdComplete();
                            }
                        }

                        /** Called when fullscreen content failed to show. */
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            isShowingAd = false;
                            onShowAdCompleteListener.onShowAdFail();
                            admobAppOpenLoadingStatus = "fail";
                        }

                        /** Called when fullscreen content is shown. */
                        @Override
                        public void onAdShowedFullScreenContent() {
                            admobAppOpenLoadingStatus = "show";
                            Log.e("admobAppOpen", "appopen  onAdShowedFullScreenContent : " + admobAppOpenLoadingStatus);
                            loadAd(activity);
                        }
                    });

            isShowingAd = true;
            appOpenAd.show(activity);
        }
    }
}
