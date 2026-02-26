package com.library.info;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT;

public class CastTvAppManager {
    public static List<CastTvCustomModel> myAppMarketingList = new ArrayList<>();
    private static int totalAdInc = 0;

    public static String[] ADMOB_AO = {"", ""};
    public static String[] ADMOB_I = {"", ""};
    public static String[] ADMOB_N = {"", ""};
    public static String[] ADMOB_B = {"", ""};
    public static String admobintloadingstatus = "show";
    public static String admobnativeloadingstatus = "show";
    public static String banneradmobloadingstatus = "show";
    public static String mediumbanneradmobloadingstatus = "show";
    public static String smartbanneradmobloadingstatus = "show";
    public static String app_privacyPolicyLink = "";
    public static int app_adShowStatus = 0;
    public static int app_updateAppDialogStatus = 0;
    public static String app_versionCode = "";
    public static int app_forwardClickAd = 0;
    public static int app_backwardClickAd = 0;
    public static int app_vpnEnable = 0;
    public static String app_vpnCountry = "";
    public static String app_vpnCarrierId = "";
    public static String app_vpnBaseUrl = "";

    public static int app_forwardInterstitial = 0;
    public static int app_backwardInterstitial = 0;
    public static int app_nativeBig = 0;
    public static int app_nativeBanner = 0;
    public static int app_launchCountry = 0;
    public static int app_swipCountry = 0;
    public static int app_swipMain = 0;

    public static int developerDialog = 0;

    public static int VPNCompulsory = 0;
    public static long secondsRemaining = 0;
    public static int internetCompulsory = 1;
    public static int showDialogBeforeAd = 0;
    public static String nativespaceboxColor = "";
    public static String nativebg = "";
    public static String nativetitle = "";
    public static String nativebutton = "";
    public static String nativedescription = "";
    public static int nativespacebox = 0;
    public static int bannerspacebox = 0;
    public static int nativeExit = 0;
    public static int InterclickCountry = 0;
    public static int startScreen = 0;
    public static int exitScreen = 0;
    public static int app_customAdStatus = 0;
    public static int app_customAppOpenStatus = 0;
    public static int app_customInterstitialStatus = 0;
    public static int app_customNativeBannerStatus = 0;


    public static boolean isBlockedVPN = false;
    public static Activity activity;
    private static CastTvAppManager mInstance;
    private InterstitialAd mInterstitialAd = null;

    private AppOpenAd mAppOpenAd = null;

    private AdView adViewPreload = null;
    private NativeAd nativePreloadBannerAd = null;

    private AdView adViewPreloadMediumBanner = null;
    private AdView adViewFirstPreloadAdMediumBanner = null;
    private AdView adViewSecondAdMediumBanner = null;

    private AdView adViewPreloadSmartBanner = null;
    private AdView adViewSecondSmartBanner = null;

    private NativeAd nativePreloadAd = null;
    private NativeAd nativeFirstPreloadAd = null;

    public static int interInCounter = 0;
    public static int interOutCounter = 0;

    public int ad_dialog_time_in_second = 1;
    private Dialog dialog;
    public static boolean fromSplash = false;
    private static long loadTimeInterstitial = 0;
    private static long loadTimeBanner = 0;
    private static long loadTimeMediumBanner = 0;
    private static long loadTimeSmartBanner = 0;
    private static long loadTimeNative = 0;
    private static long loadTimeNativeBanner = 0;
    private static boolean isAppOpenLoading = false;
    private static int forwardAlternative = 0;
    private static int backwardAlternative = 0;


    public static CastTvAppManager getInstance(Activity activity) {
        CastTvAppManager.activity = activity;
        if (mInstance == null) {
            mInstance = new CastTvAppManager(activity);
        }
        return mInstance;
    }

    public CastTvAppManager(Activity activity) {
        CastTvAppManager.activity = activity;
    }

    /**
     * INTERSTITIAL ADS CODE START
     **/

    //Poo
    private boolean wasInterstitialLoadTimeLessThanNHoursAgo() {
        long dateDifference = (new Date()).getTime() - loadTimeInterstitial;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * (long) 4));
    }

    private boolean wasAppOpenLoadTimeLessThanNHoursAgo() {
        long dateDifference = (new Date()).getTime() - loadTimeInterstitial;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * (long) 4));
    }

    private boolean wasBannerLoadTimeLessThanNHoursAgo() {
        long dateDifference = (new Date()).getTime() - loadTimeBanner;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * (long) 4));
    }

    private boolean wasMediumBannerLoadTimeLessThanNHoursAgo() {
        long dateDifference = (new Date()).getTime() - loadTimeMediumBanner;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * (long) 4));
    }

    private boolean wasSmartBannerLoadTimeLessThanNHoursAgo() {
        long dateDifference = (new Date()).getTime() - loadTimeSmartBanner;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * (long) 4));
    }

    private boolean wasNativeLoadTimeLessThanNHoursAgo() {
        long dateDifference = (new Date()).getTime() - loadTimeNative;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * (long) 4));
    }

    private boolean wasNativeBannerLoadTimeLessThanNHoursAgo() {
        long dateDifference = (new Date()).getTime() - loadTimeNativeBanner;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * (long) 4));
    }


    public void loadAppOpenAd(Context context) {
        if (isAppOpenLoading || mAppOpenAd != null) {
            Log.e("admobInt", "wasAppOpenLoadTimeLessThanNHoursAgo : " + wasAppOpenLoadTimeLessThanNHoursAgo());
            return;
        }

        isAppOpenLoading = true;
        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(
                context,
                ADMOB_AO[0],
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {

                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        isAppOpenLoading = false;
                        mAppOpenAd = ad;
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        isAppOpenLoading = false;
                        mAppOpenAd = null;
                    }
                });
    }

    public void loadInterstitialAd(Context context) {
        if (!wasInterstitialLoadTimeLessThanNHoursAgo()) {
            Log.e("admobInt", "wasInterstitialLoadTimeLessThanNHoursAgo : " + wasInterstitialLoadTimeLessThanNHoursAgo());
            admobintloadingstatus = "fail";
        }

        if (!admobintloadingstatus.equals("show") && !admobintloadingstatus.equals("fail")) {
            Log.e("admobInt", "admobInt return");
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        Log.e("admobInt", "Send request");
        admobintloadingstatus = "loading";
        InterstitialAd.load(context, ADMOB_I[0], adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.e("admobInt", "load fail");
                admobintloadingstatus = "fail";
                mInterstitialAd = null;
                Log.e("Kp_inter_load", "onAdFailedToLoad: " + loadAdError.getMessage());
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                admobintloadingstatus = "loaded";
                loadTimeInterstitial = (new Date()).getTime();
                Log.e("admobInt", "loaded");
                mInterstitialAd = interstitialAd;
                Log.e("Kp_inter_load", "onAdLoaded: Done");
            }
        });
    }

    public void showInterstitialAd(Activity activity, AdCallbackListenerCastTv myCallback1) {
        if (app_adShowStatus == 0 || app_forwardInterstitial == 3) {
            if (myCallback1 != null) {
                myCallback1.callbackCall();
            }
            return;
        }

        int how_many_clicks = app_forwardClickAd;
        if (isBlockedVPN) {
            how_many_clicks = InterclickCountry;
        }

        callInterAdsShow(activity, myCallback1, how_many_clicks);
    }

    public void showInterstitialBackAd(Activity activity, AdCallbackListenerCastTv myCallback1) {
        if (app_adShowStatus == 0 || app_backwardInterstitial == 3) {
            if (myCallback1 != null) {
                myCallback1.callbackCall();
            }
            return;
        }

        int how_many_clicks = app_backwardClickAd;
        if (isBlockedVPN) {
            how_many_clicks = InterclickCountry;
        }

        callInterAdsShowBack(activity, myCallback1, how_many_clicks);
    }

    private void callInterAdsShow(final Activity activity, final AdCallbackListenerCastTv listener, int clickCount) {
        Log.e("AppManager", "callInterAdsShow: " + clickCount);
        if (clickCount != 0) {
            if (interInCounter % clickCount == 0) {
                if (app_forwardInterstitial == 2) {
                    showAlternatForwardAd(activity, listener);
                } else {
                    showNormalForwardAd(activity, listener);
                }
            } else {
                if (listener != null) {
                    listener.callbackCall();
                }
            }
            interInCounter++;
        } else {
            if (app_forwardInterstitial == 2) {
                showAlternatForwardAd(activity, listener);
            } else {
                showNormalForwardAd(activity, listener);
            }
        }
    }

    private void callInterAdsShowBack(final Activity activity, final AdCallbackListenerCastTv listener, int clickCount) {
        Log.e("AppManager", "callInterAdsShowBack: " + clickCount);
        if (clickCount != 0) {
            if (interOutCounter % clickCount == 0) {
                if (app_backwardInterstitial == 2) {
                    showAlternatBackwardAd(activity, listener);
                } else {
                    showNormalForwardAd(activity, listener);
                }
            } else {
                if (listener != null) {
                    listener.callbackCall();
                }
            }
            interOutCounter++;
        } else {
            if (app_backwardInterstitial == 2) {
                showAlternatBackwardAd(activity, listener);
            } else {
                showNormalForwardAd(activity, listener);
            }
        }
    }


    private void showNormalForwardAd(Activity activity, AdCallbackListenerCastTv listener) {
        if (mInterstitialAd != null) {
            ShowInterAllclick(listener);
        } else {
            if (app_customAdStatus == 1 && app_customInterstitialStatus == 1 && myAppMarketingList.size() > 0) {
                CastTvCustomModel customModel = getMyCustomAd();
                if (customModel != null) {
                    CastTvCustomFullScreenActivity.newIntent(activity, listener, customModel);
                } else {
                    loadInterstitialAd(activity);
                    if (listener != null) {
                        listener.callbackCall();
                    }
                }
            } else {
                loadInterstitialAd(activity);
                if (listener != null) {
                    listener.callbackCall();
                }
            }
        }
    }

    private void showAlternatForwardAd(Activity activity, AdCallbackListenerCastTv listener) {
        if (forwardAlternative == 1 || forwardAlternative > 1) {
            forwardAlternative = 0;
            if (mAppOpenAd != null) {
                displayAppopenDialog(activity);
                mAppOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        if (listener != null) {
                            listener.callbackCall();
                        }
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        mAppOpenAd = null;
                        if (listener != null) {
                            listener.callbackCall();
                        }
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mAppOpenAd = null;
                        loadAppOpenAd(activity);
                    }
                });
            } else {
                if (app_customAdStatus == 1 && app_customAppOpenStatus == 1 && myAppMarketingList.size() > 0) {
                    CastTvCustomModel customModel = getMyCustomAd();
                    if (customModel != null) {
                        CustomAppOpenAdCastTv.newIntent(activity, new SuccessListener() {
                            @Override
                            public void onSuccess() {
                                if (listener != null) {
                                    listener.callbackCall();
                                }
                            }
                        }, customModel);
                    } else {
                        loadAppOpenAd(activity);
                        if (listener != null) {
                            listener.callbackCall();
                        }
                    }
                } else {
                    loadAppOpenAd(activity);
                    if (listener != null) {
                        listener.callbackCall();
                    }
                }
            }
        } else {
            forwardAlternative++;
            if (mInterstitialAd != null) {
                ShowInterAllclick(listener);
            } else {
                if (app_customAdStatus == 1 && app_customInterstitialStatus == 1 && myAppMarketingList.size() > 0) {
                    CastTvCustomModel customModel = getMyCustomAd();
                    if (customModel != null) {
                        CastTvCustomFullScreenActivity.newIntent(activity, listener, customModel);
                    } else {
                        loadInterstitialAd(activity);
                        if (listener != null) {
                            listener.callbackCall();
                        }
                    }

                } else {
                    loadInterstitialAd(activity);
                    if (listener != null) {
                        listener.callbackCall();
                    }
                }
            }
        }
    }

    private void showAlternatBackwardAd(Activity activity, AdCallbackListenerCastTv listener) {
        if (backwardAlternative == 1 || backwardAlternative > 1) {
            backwardAlternative = 0;
            if (mAppOpenAd != null) {
                displayAppopenDialog(activity);
                mAppOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        if (listener != null) {
                            listener.callbackCall();
                        }
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        mAppOpenAd = null;
                        if (listener != null) {
                            listener.callbackCall();
                        }
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mAppOpenAd = null;
                        loadAppOpenAd(activity);
                    }
                });
            } else {
                if (app_customAdStatus == 1 && app_customAppOpenStatus == 1 && myAppMarketingList.size() > 0) {
                    CastTvCustomModel customModel = getMyCustomAd();
                    if (customModel != null) {
                        CustomAppOpenAdCastTv.newIntent(activity, new SuccessListener() {
                            @Override
                            public void onSuccess() {
                                if (listener != null) {
                                    listener.callbackCall();
                                }
                            }
                        }, customModel);
                    } else {
                        loadAppOpenAd(activity);
                        if (listener != null) {
                            listener.callbackCall();
                        }
                    }
                } else {
                    loadAppOpenAd(activity);
                    if (listener != null) {
                        listener.callbackCall();
                    }
                }
            }
        } else {
            backwardAlternative++;
            if (mInterstitialAd != null) {
                ShowInterAllclick(listener);
            } else {
                if (app_customAdStatus == 1 && app_customInterstitialStatus == 1 && myAppMarketingList.size() > 0) {
                    CastTvCustomModel customModel = getMyCustomAd();
                    if (customModel != null) {
                        CastTvCustomFullScreenActivity.newIntent(activity, listener, customModel);
                    } else {
                        loadInterstitialAd(activity);
                        if (listener != null) {
                            listener.callbackCall();
                        }
                    }

                } else {
                    loadInterstitialAd(activity);
                    if (listener != null) {
                        listener.callbackCall();
                    }
                }
            }
        }
    }

    private void ShowInterAllclick(AdCallbackListenerCastTv myCallback1) {
        displayInterDialog(activity);
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                if (myCallback1 != null) {
                    myCallback1.callbackCall();
                }

            }

            @Override
            public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                admobintloadingstatus = "fail";
                mInterstitialAd = null;
                if (myCallback1 != null) {
                    myCallback1.callbackCall();
                }
            }

            @Override
            public void onAdShowedFullScreenContent() {
                admobintloadingstatus = "show";
                mInterstitialAd = null;
                loadInterstitialAd(activity);
            }
        });
    }

    private void displayInterDialog(final Activity activity) {
        if (showDialogBeforeAd == 1 && !fromSplash) {
            Log.e("admobInt", "onAdLoaded: app_dialogBeforeAdShow");
            dialog = new Dialog(activity);
            View view1 = LayoutInflater.from(activity).inflate(R.layout.progress_dialog_view, null);
            dialog.setContentView(view1);
            dialog.setCancelable(false);
            Window window1 = dialog.getWindow();
            window1.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            new CountDownTimer(ad_dialog_time_in_second * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    dialog.dismiss();
                    mInterstitialAd.show(activity);
                }
            }.start();
        } else
            mInterstitialAd.show(activity);
    }

    private void displayAppopenDialog(final Activity activity) {
        if (showDialogBeforeAd == 1 && !fromSplash) {
            Log.e("admobInt", "onAdLoaded: app_dialogBeforeAdShow");
            dialog = new Dialog(activity);
            View view1 = LayoutInflater.from(activity).inflate(R.layout.progress_dialog_view, null);
            dialog.setContentView(view1);
            dialog.setCancelable(false);
            Window window1 = dialog.getWindow();
            window1.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            new CountDownTimer(ad_dialog_time_in_second * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    dialog.dismiss();
                    mAppOpenAd.show(activity);
                }
            }.start();
        } else
            mAppOpenAd.show(activity);
    }

    /** INTERSTITIAL ADS CODE END **/


    /**
     * NATIVE ADS CODE START
     **/
//    With First native
    public void showFirstNativeAds(final Activity activity, final ViewGroup nativeAdContainer, ImageView native_space_img, final int type) {
        final View adView = getView(activity, nativeAdContainer, type);
        manageSpaceBox(adView, nativeAdContainer);
        setViewHeight(activity, nativeAdContainer, type);

        if (app_adShowStatus == 0 || app_nativeBig == 3) {
            Log.e("admobNative", "showNativeAds: ");
            if (nativespacebox == 0)
                nativeAdContainer.setVisibility(View.GONE);
            return;
        }
        if (app_nativeBig == 1 && nativespacebox == 0 && ADMOB_N[0].isEmpty()) {
            Log.e("admobNative", "showNativeAds: ");
            if (native_space_img != null)
                native_space_img.setVisibility(View.VISIBLE);
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }
        if (app_nativeBig == 2 && nativespacebox == 0 && ADMOB_B[0].isEmpty()) {
            Log.e("admobBannerMedium-First", "showBannerMedium:  ");
            if (native_space_img != null)
                native_space_img.setVisibility(View.VISIBLE);
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        if (app_nativeBig == 1) {
            if (nativeFirstPreloadAd != null) {
                Log.e("admobNative_First", "show:  ");
                populateNativeAdView(nativeAdContainer, nativeFirstPreloadAd, adView, type);
                nativeFirstPreloadAd = null;
            } else {
                if (app_customAdStatus == 1 && app_customNativeBannerStatus == 1) {
                    showMyCustomNative(activity, nativeAdContainer, type);
                } else if (nativespacebox == 0) {
                    nativeAdContainer.setVisibility(View.GONE);
                }
            }
        } else if (app_nativeBig == 2) {
            if (adViewFirstPreloadAdMediumBanner != null) {
                if (((LinearLayout) adView.findViewById(R.id.ll_space) != null))
                    ((LinearLayout) adView.findViewById(R.id.ll_space)).setVisibility(View.GONE);
                loadTimeMediumBanner = (new Date()).getTime();
                nativeAdContainer.removeAllViews();
                Log.e("admobBannerMedium-First", "show:  ");
                nativeAdContainer.addView(adViewFirstPreloadAdMediumBanner);
                adViewFirstPreloadAdMediumBanner = null;
            } else {
                if (app_customAdStatus == 1 && app_customNativeBannerStatus == 1) {
                    showMyCustomNative(activity, nativeAdContainer, type);
                } else if (nativespacebox == 0) {
                    nativeAdContainer.setVisibility(View.GONE);
                }
            }
        }
    }

    public void preLoadFirstMediumBannerAds(Activity activity) {
        adViewFirstPreloadAdMediumBanner = new AdView(activity);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewFirstPreloadAdMediumBanner.setAdUnitId(ADMOB_B[0]);
        adViewFirstPreloadAdMediumBanner.setAdSize(AdSize.MEDIUM_RECTANGLE);
        Log.e("admobBannerMedium-First", "loading:  ");
        adViewFirstPreloadAdMediumBanner.loadAd(adRequest);
        adViewFirstPreloadAdMediumBanner.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.e("admobBannerMedium-First", "fail:  ");
                adViewFirstPreloadAdMediumBanner = null;

            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                Log.e("admobBannerMedium-First", "loaded:  ");

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });

    }

    //With Preload Big & Small native
    public void showNativeAds(final Activity activity, final ViewGroup nativeAdContainer, ImageView native_space_img, final int type) {
        final View adView = getView(activity, nativeAdContainer, type);
        manageSpaceBox(adView, nativeAdContainer);
        setViewHeight(activity, nativeAdContainer, type);

        if (app_adShowStatus == 0 || app_nativeBig == 3) {
            Log.e("admobNative", "showNativeAds: ");
            if (nativespacebox == 0)
                nativeAdContainer.setVisibility(View.GONE);
            return;
        }
        if (app_nativeBig == 1 && nativespacebox == 0 && ADMOB_N[0].isEmpty()) {
            Log.e("admobNative", "showNativeAds: ");
            if (native_space_img != null)
                native_space_img.setVisibility(View.VISIBLE);
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }
        if (app_nativeBig == 2 && nativespacebox == 0 && ADMOB_B[0].isEmpty()) {
            Log.e("admobNative", "showNativeAds: ");
            if (native_space_img != null)
                native_space_img.setVisibility(View.VISIBLE);
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        if (app_nativeBig == 1) {
            if (nativePreloadAd != null) {
                populateNativeAdView(nativeAdContainer, nativePreloadAd, adView, type);
                nativePreloadAd = null;
                Log.e("admobNative", "show:  ");
                admobnativeloadingstatus = "show";
                preLoadNativeAds(activity, type);
            } else {
                if (!admobnativeloadingstatus.equals("show") && !admobnativeloadingstatus.equals("fail")) {
                    Log.e("admobNative", "admobNative return");
                    if (app_customAdStatus == 1 && app_customNativeBannerStatus == 1) {
                        showMyCustomNative(activity, nativeAdContainer, type);
                    } else if (nativespacebox == 0) {
                        nativeAdContainer.setVisibility(View.GONE);
                    }
                    return;
                }
                AdLoader.Builder builder = new AdLoader.Builder(activity, ADMOB_N[0]);
                builder.forNativeAd(
                        new NativeAd.OnNativeAdLoadedListener() {
                            // OnLoadedListener implementation.
                            @Override
                            public void onNativeAdLoaded(NativeAd nativeAd) {
                                Log.e("admobNative", "load 1st native:  ");
                                // If this callback occurs after the activity is destroyed, you must call
                                // destroy and return or you may get a memory leak.
                                boolean isDestroyed = false;

                                isDestroyed = activity.isDestroyed();
                                if (isDestroyed || activity.isFinishing() || activity.isChangingConfigurations()) {
                                    nativeAd.destroy();
                                    return;
                                }
                                // You must call destroy on old ads when you are done with them,
                                // otherwise you will have a memory leak.
                                if (nativePreloadAd != null) {
                                    nativePreloadAd.destroy();
                                }

                                Log.e("admobNative", "loaded: 1 ");
                                admobnativeloadingstatus = "loaded";
                                loadTimeNative = (new Date()).getTime();
                                nativePreloadAd = nativeAd;
                                populateNativeAdView(nativeAdContainer, nativePreloadAd, adView, type);
                                nativePreloadAd = null;
                                Log.e("admobNative", "show:  ");
                                admobnativeloadingstatus = "show";
                                preLoadNativeAds(activity, type);

                            }
                        });

                LoadAd(builder, nativeAdContainer, type);
            }
        } else if (app_nativeBig == 2) {
            if (type == 1) {
                showMediumBanner(activity, nativeAdContainer, adView, type);
            } else {
                showSmartBanner(activity, nativeAdContainer, adView, type);
            }

        }

    }


    //No Preload
    public void showNativeAds2(Activity activity, final ViewGroup nativeAdContainer, final int type) {
        final View adView = getView(activity, nativeAdContainer, type);
        manageSpaceBox(adView, nativeAdContainer);
        setViewHeight(activity, nativeAdContainer, type);
        if (app_adShowStatus == 0 || app_nativeBig == 3) {
            if (nativespacebox == 0)
                nativeAdContainer.setVisibility(View.GONE);
            return;
        }
        if (app_nativeBig == 1 && nativespacebox == 0 && ADMOB_N[0].isEmpty()) {
            Log.e("admobNative2", "showNativeAds2: ");
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }
        if (app_nativeBig == 2 && nativespacebox == 0 && ADMOB_B[0].isEmpty()) {
            Log.e("admobBannerMedium-Sec", "showBannerMedium2:  ");
            nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        if (app_nativeBig == 1) {
            AdLoader.Builder builder = new AdLoader.Builder(activity, ADMOB_N[0]);
            builder.forNativeAd(
                    new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            Log.e("admobNative2", "load:  ");
                            populateNativeAdView(nativeAdContainer, nativeAd, adView, type);
                        }
                    });
            LoadAd2(builder, nativeAdContainer, type);
        } else if (app_nativeBig == 2) {
            if (type == 1) {
                showMediumBanner2(activity, nativeAdContainer, adView, type);
            } else {
                showSmartBanner2(activity, nativeAdContainer, adView, type);
            }
        }

    }


    //For Preload Ad Method  First Medium Banner
    public void preLoadFirstNativeAds(Activity activity, int type) {
        if (app_adShowStatus == 1) {
            AdLoader.Builder builder = new AdLoader.Builder(activity, ADMOB_N[0]);
            builder.forNativeAd(
                    new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            Log.e("admobNative_First", "loaded:  First");
                            nativeFirstPreloadAd = nativeAd;
                        }
                    });
            VideoOptions videoOptions = new VideoOptions.Builder().build();
            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).setAdChoicesPlacement(ADCHOICES_TOP_RIGHT).build();
            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(
                    new AdListener() {
                        @Override
                        public void onAdImpression() {

                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            Log.e("admobNative_First", "onAdFailedToLoad: First " + loadAdError.getCode());
                            nativeFirstPreloadAd = null;
                        }
                    }).build();

            Log.e("admobNative_First", "loading: First ");
            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    //For Preload Ad Method Big & Small native
    public void preLoadNativeAds(Activity activity, int type) {
        if (app_adShowStatus == 1) {
            if (!wasNativeLoadTimeLessThanNHoursAgo()) {
                Log.e("admobNative_time", "wasNativeLoadTimeLessThanNHoursAgo : " + wasNativeLoadTimeLessThanNHoursAgo());
                admobnativeloadingstatus = "fail";
            } else {
                Log.e("admobNative_else", "wasNativeLoadTimeLessThanNHoursAgo : " + wasNativeLoadTimeLessThanNHoursAgo());
            }
            if (!admobnativeloadingstatus.equals("show") && !admobnativeloadingstatus.equals("fail")) {
                return;
            }
            AdLoader.Builder builder = new AdLoader.Builder(activity, ADMOB_N[0]);
            builder.forNativeAd(
                    new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            Log.e("admobNative", "loaded:  ");
                            admobnativeloadingstatus = "loaded";
                            nativePreloadAd = nativeAd;
                            loadTimeNative = (new Date()).getTime();
                        }
                    });
            finalPreLoadNativeAd(builder, type);
        }
    }

    public void finalPreLoadNativeAd(AdLoader.Builder builder, int type) {
        VideoOptions videoOptions = new VideoOptions.Builder().build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).setAdChoicesPlacement(ADCHOICES_TOP_RIGHT).build();
        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(
                new AdListener() {
                    @Override
                    public void onAdImpression() {

                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e("admobNative_finalPre", "onAdFailedToLoad:  " + loadAdError.getCode());
                        admobnativeloadingstatus = "fail";

                    }
                }).build();

        Log.e("admobNative_finalPre", "loading:  ");
        admobnativeloadingstatus = "loading";
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    //With Preload Medium Banner native
    public void showMediumBanner(final Activity activity, final ViewGroup nativeAdContainer, View view, int type) {
        final FrameLayout middum_banner_container = view.findViewById(R.id.middum_banner_container);
        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(view);
        if (adViewPreloadMediumBanner != null) {
            middum_banner_container.removeAllViews();
            Log.e("admobBannerMedium", "show:");
            middum_banner_container.addView(adViewPreloadMediumBanner);
            adViewPreloadMediumBanner = null;
            if (((LinearLayout) view.findViewById(R.id.ll_space) != null))
                ((LinearLayout) view.findViewById(R.id.ll_space)).setVisibility(View.GONE);
            mediumbanneradmobloadingstatus = "show";
            preLoadMediumBannerAds(activity);
        } else {
            if (!mediumbanneradmobloadingstatus.equals("show") && !mediumbanneradmobloadingstatus.equals("fail")) {
                Log.e("admobBannerMedium", "admobBannerMedium return");
                if (app_customAdStatus == 1 && app_customNativeBannerStatus == 1) {
                    showMyCustomNative(activity, nativeAdContainer, type);
                } else if (nativespacebox == 0) {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                return;
            }
            adViewPreloadMediumBanner = new AdView(activity);
            AdRequest adRequest = new AdRequest.Builder().build();
            Log.e("admobBannerMedium", "loading: 1st time");
            adViewPreloadMediumBanner.setAdUnitId(ADMOB_B[0]);
            adViewPreloadMediumBanner.setAdSize(AdSize.MEDIUM_RECTANGLE);

            adViewPreloadMediumBanner.loadAd(adRequest);
            adViewPreloadMediumBanner.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    // Code to be executed when an ad request fails.
                    Log.e("admobBannerMedium", "fail:  ");
                    mediumbanneradmobloadingstatus = "fail";
                    adViewPreloadMediumBanner = null;
                    if (app_customAdStatus == 1 && app_customNativeBannerStatus == 1) {
                        showMyCustomNative(activity, nativeAdContainer, type);
                    } else if (nativespacebox == 0) {
                        nativeAdContainer.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onAdImpression() {
                    // Code to be executed when an impression is recorded
                    // for an ad.
                }

                @Override
                public void onAdLoaded() {
                    Log.e("admobBannerMedium", "loaded:  ");
                    if (((LinearLayout) view.findViewById(R.id.ll_space) != null))
                        ((LinearLayout) view.findViewById(R.id.ll_space)).setVisibility(View.GONE);
                    mediumbanneradmobloadingstatus = "loaded";
                    loadTimeMediumBanner = (new Date()).getTime();
                    middum_banner_container.removeAllViews();
                    middum_banner_container.addView(adViewPreloadMediumBanner);
                    adViewPreloadMediumBanner = null;
                    Log.e("admobBannerMedium", "show:  ");
                    mediumbanneradmobloadingstatus = "show";
                    preLoadMediumBannerAds(activity);
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }
            });
        }

    }


    public void preLoadMediumBannerAds(Activity activity) {
        if (!wasMediumBannerLoadTimeLessThanNHoursAgo()) {
            Log.e("admobBannerMedium", "wasMediumBannerLoadTimeLessThanNHoursAgo : " + wasBannerLoadTimeLessThanNHoursAgo());
            mediumbanneradmobloadingstatus = "fail";
        }
        if (!mediumbanneradmobloadingstatus.equals("show") && !mediumbanneradmobloadingstatus.equals("fail")) {
            return;
        }
        adViewPreloadMediumBanner = new AdView(activity);
        AdRequest adRequest = new AdRequest.Builder().build();
        Log.e("admobBannerMedium", "loading:  ");
        mediumbanneradmobloadingstatus = "loading";
        adViewPreloadMediumBanner.setAdUnitId(ADMOB_B[0]);
        adViewPreloadMediumBanner.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adViewPreloadMediumBanner.loadAd(adRequest);
        adViewPreloadMediumBanner.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.e("admobBannerMedium", "fail:  ");
                mediumbanneradmobloadingstatus = "fail";
                adViewPreloadMediumBanner = null;
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                Log.e("admobBannerMedium", "loaded:  ");
                mediumbanneradmobloadingstatus = "loaded";
                loadTimeMediumBanner = (new Date()).getTime();

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });
    }

    //With Preload Medium Banner 2 native
    public void showMediumBanner2(final Activity activity, final ViewGroup nativeAdContainer, View view, int type) {
        final FrameLayout middum_banner_container = view.findViewById(R.id.middum_banner_container);
        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(view);
        adViewSecondAdMediumBanner = new AdView(activity);
        AdRequest adRequest = new AdRequest.Builder().build();
        Log.e("admobBannerMedium2", "loading: 1st time");
        adViewSecondAdMediumBanner.setAdUnitId(ADMOB_B[0]);
        adViewSecondAdMediumBanner.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adViewSecondAdMediumBanner.loadAd(adRequest);
        adViewSecondAdMediumBanner.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.e("admobBannerMedium2", "fail:  ");
                adViewSecondAdMediumBanner = null;
                if (app_customAdStatus == 1 && app_customNativeBannerStatus == 1) {
                    showMyCustomNative(activity, nativeAdContainer, type);
                } else if (nativespacebox == 0) {
                    nativeAdContainer.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                Log.e("admobBannerMedium2", "loaded:  ");
                if (((LinearLayout) view.findViewById(R.id.ll_space) != null))
                    ((LinearLayout) view.findViewById(R.id.ll_space)).setVisibility(View.GONE);
                middum_banner_container.removeAllViews();
                middum_banner_container.addView(adViewSecondAdMediumBanner);
                adViewSecondAdMediumBanner = null;
                Log.e("admobBannerMedium2", "show:  ");


            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });
    }

    //With Preload Smart Banner  native
    public void showSmartBanner(final Activity activity, final ViewGroup nativeAdContainer, View view, int type) {
        final FrameLayout smart_banner_container = view.findViewById(R.id.smart_banner_container);
        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(view);
        if (adViewPreloadSmartBanner != null) {
            smart_banner_container.removeAllViews();
            Log.e("admobBannerSmart", "show:  ");
            smart_banner_container.addView(adViewPreloadSmartBanner);
            adViewPreloadSmartBanner = null;
            if (((LinearLayout) view.findViewById(R.id.ll_space) != null))
                ((LinearLayout) view.findViewById(R.id.ll_space)).setVisibility(View.GONE);
            smartbanneradmobloadingstatus = "show";
            preLoadSmartBannerAds(activity);
        } else {
            if (!smartbanneradmobloadingstatus.equals("show") && !smartbanneradmobloadingstatus.equals("fail")) {
                Log.e("admobBannerSmart", "admobBannerSmart return");
                if (app_customAdStatus == 1 && app_customNativeBannerStatus == 1) {
                    showMyCustomNative(activity, nativeAdContainer, type);
                } else if (nativespacebox == 0) {
                    nativeAdContainer.setVisibility(View.GONE);
                }
                return;
            }
            adViewPreloadSmartBanner = new AdView(activity);
            AdRequest adRequest = new AdRequest.Builder().build();
            Log.e("admobBannerSmart", "loading:  ");
            adViewPreloadSmartBanner.setAdUnitId(ADMOB_B[0]);
            adViewPreloadSmartBanner.setAdSize(AdSize.SMART_BANNER);
            adViewPreloadSmartBanner.loadAd(adRequest);
            adViewPreloadSmartBanner.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    // Code to be executed when an ad request fails.
                    Log.e("admobBannerSmart", "fail:  ");
                    smartbanneradmobloadingstatus = "fail";
                    adViewPreloadSmartBanner = null;

                    if (app_customAdStatus == 1 && app_customNativeBannerStatus == 1) {
                        showMyCustomNative(activity, nativeAdContainer, type);
                    } else if (nativespacebox == 0) {
                        nativeAdContainer.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAdImpression() {
                    // Code to be executed when an impression is recorded
                    // for an ad.
                }

                @Override
                public void onAdLoaded() {
                    Log.e("admobBannerSmart", "loaded:  ");
                    if (((LinearLayout) view.findViewById(R.id.ll_space) != null))
                        ((LinearLayout) view.findViewById(R.id.ll_space)).setVisibility(View.GONE);
                    smartbanneradmobloadingstatus = "loaded";
                    loadTimeSmartBanner = (new Date()).getTime();
                    smart_banner_container.removeAllViews();
                    smart_banner_container.addView(adViewPreloadSmartBanner);
                    adViewPreloadSmartBanner = null;
                    Log.e("admobBannerSmart", "show:  ");
                    smartbanneradmobloadingstatus = "show";
                    preLoadSmartBannerAds(activity);
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }
            });
        }
    }

    public void preLoadSmartBannerAds(Activity activity) {
        if (!wasSmartBannerLoadTimeLessThanNHoursAgo()) {
            Log.e("admobBannerSmart", "wasSmartBannerLoadTimeLessThanNHoursAgo : " + wasBannerLoadTimeLessThanNHoursAgo());
            smartbanneradmobloadingstatus = "fail";
        }
        if (!smartbanneradmobloadingstatus.equals("show") && !smartbanneradmobloadingstatus.equals("fail")) {
            return;
        }
        adViewPreloadSmartBanner = new AdView(activity);
        AdRequest adRequest = new AdRequest.Builder().build();
        Log.e("admobBannerSmart", "loading:  ");
        smartbanneradmobloadingstatus = "loading";
        adViewPreloadSmartBanner.setAdUnitId(ADMOB_B[0]);
        adViewPreloadSmartBanner.setAdSize(AdSize.SMART_BANNER);
        adViewPreloadSmartBanner.loadAd(adRequest);
        adViewPreloadSmartBanner.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.e("admobBannerSmart", "fail:  ");
                smartbanneradmobloadingstatus = "fail";
                adViewPreloadSmartBanner = null;
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                Log.e("admobBannerSmart", "loaded:  ");
                smartbanneradmobloadingstatus = "loaded";
                loadTimeSmartBanner = (new Date()).getTime();

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });
    }

    //With Preload Second Smart Banner  native
    public void showSmartBanner2(final Activity activity, final ViewGroup nativeAdContainer, View view, int type) {
        final FrameLayout smart_banner_container = view.findViewById(R.id.smart_banner_container);
        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(view);
        adViewSecondSmartBanner = new AdView(activity);
        AdRequest adRequest = new AdRequest.Builder().build();
        Log.e("admobBannerSmart2", "loading:  ");
        adViewSecondSmartBanner.setAdUnitId(ADMOB_B[0]);
        adViewSecondSmartBanner.setAdSize(AdSize.SMART_BANNER);
        adViewSecondSmartBanner.loadAd(adRequest);
        adViewSecondSmartBanner.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.e("admobBannerSmart2", "fail:  ");
                adViewSecondSmartBanner = null;
                if (app_customAdStatus == 1 && app_customNativeBannerStatus == 1) {
                    showMyCustomNative(activity, nativeAdContainer, type);
                } else if (nativespacebox == 0) {
                    nativeAdContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                Log.e("admobBannerSmart2", "loaded:  ");
                if (((LinearLayout) view.findViewById(R.id.ll_space) != null))
                    ((LinearLayout) view.findViewById(R.id.ll_space)).setVisibility(View.GONE);
                smart_banner_container.removeAllViews();
                smart_banner_container.addView(adViewSecondSmartBanner);
                adViewSecondSmartBanner = null;
                Log.e("admobBannerSmart2", "show:  ");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });
    }

    //With Preload Banner native
    public void showBannerAds(final Activity activity, final ViewGroup nativeAdContainer) {
        final int type = 3;
        final View adView = getView(activity, nativeAdContainer, type);
        manageBannerSpaceBox(adView, nativeAdContainer);

        if (app_adShowStatus == 0 || app_nativeBanner == 3) {
            if (bannerspacebox == 0)
                nativeAdContainer.setVisibility(View.GONE);
            return;
        }

        if (app_nativeBanner == 2) {
            showBanner(activity, nativeAdContainer, adView);
            return;
        }

        if (nativePreloadBannerAd != null) {
            Log.e("admobBanner", "Native Banner : Banner preload display" + type);
            populateNativeBannerAdView(nativeAdContainer, nativePreloadBannerAd, adView, type);

            nativePreloadBannerAd = null;
            Log.e("admobBanner", "show:  ");
            banneradmobloadingstatus = "show";
            preLoadNativeBannerAds(activity);
        } else {
            AdLoader.Builder builder = new AdLoader.Builder(activity, ADMOB_N[0]);
            builder.forNativeAd(
                    new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            boolean isDestroyed = false;

                            isDestroyed = activity.isDestroyed();
                            if (isDestroyed || activity.isFinishing() || activity.isChangingConfigurations()) {
                                nativeAd.destroy();
                                return;
                            }
                            if (nativePreloadBannerAd != null) {
                                nativePreloadBannerAd.destroy();
                            }
                            nativePreloadBannerAd = nativeAd;
                            Log.e("admobBanner", "loaded:  ");
                            banneradmobloadingstatus = "loaded";
                            Log.e("admobBanner", " banner : Display" + type);
                            populateNativeBannerAdView(nativeAdContainer, nativePreloadBannerAd, adView, type);

                            nativePreloadBannerAd = null;
                            Log.e("admobBanner", "show:  ");
                            banneradmobloadingstatus = "show";
                            loadTimeNativeBanner = (new Date()).getTime();

                            preLoadNativeBannerAds(activity);
                        }
                    });
            LoadBannerAd(builder, nativeAdContainer, type);

        }
    }

    //For Preload Ad Method Banner native
    public void preLoadNativeBannerAds(Activity activity) {
        if (app_adShowStatus == 1 && app_nativeBanner != 3) {
            if (!wasNativeBannerLoadTimeLessThanNHoursAgo()) {
                Log.e("admobBanner", "wasNativeBannerLoadTimeLessThanNHoursAgo : " + wasNativeBannerLoadTimeLessThanNHoursAgo());
                banneradmobloadingstatus = "fail";
            }
            if (!banneradmobloadingstatus.equals("show") && !banneradmobloadingstatus.equals("fail")) {
                return;
            }

            AdLoader.Builder builder = new AdLoader.Builder(activity, ADMOB_N[0]);
            builder.forNativeAd(
                    new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            nativePreloadBannerAd = nativeAd;
                            Log.e("admobBanner", "loaded:  ");
                            banneradmobloadingstatus = "loaded";
                            loadTimeNativeBanner = (new Date()).getTime();
                        }
                    });
            finalPreLoadNativeBannerAd(builder);
        }

    }

    private void finalPreLoadNativeBannerAd(AdLoader.Builder builder) {
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setAdChoicesPlacement(ADCHOICES_TOP_RIGHT).build();
        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(
                new AdListener() {
                    @Override
                    public void onAdImpression() {

                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e("admobBanner", "fail:  ");
                        banneradmobloadingstatus = "fail";
                    }
                }).build();
        Log.e("admobBanner", "loading:  ");
        banneradmobloadingstatus = "loading";
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void populateNativeAdView(ViewGroup nativeAdContainer, NativeAd nativeAd, View adView, int type) {

        ((Button) adView.findViewById(R.id.ad_call_to_action)).setBackgroundColor(Color.parseColor("#" + nativebutton));
        ((TextView) adView.findViewById(R.id.adText)).setBackgroundColor(Color.parseColor("#" + nativebutton));
        ((TextView) adView.findViewById(R.id.ad_headline)).setTextColor(Color.parseColor("#" + nativetitle));
        ((TextView) adView.findViewById(R.id.ad_body)).setTextColor(Color.parseColor("#" + nativedescription));
        if (((LinearLayout) adView.findViewById(R.id.ll_space) != null))
            ((CardView) adView.findViewById(R.id.ll_space_card)).setCardBackgroundColor(Color.parseColor("#" + nativespaceboxColor));
        ((CardView) adView.findViewById(R.id.adview_card)).setCardBackgroundColor(Color.parseColor("#" + nativebg));


        final NativeAdView adView1 = adView.findViewById(R.id.adview);
        adView1.setVisibility(View.VISIBLE);


        // Set other ad assets.
        adView1.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView1.setBodyView(adView.findViewById(R.id.ad_body));
        adView1.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView1.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView1.setPriceView(adView.findViewById(R.id.ad_price));
        adView1.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView1.setStoreView(adView.findViewById(R.id.ad_store));
        adView1.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // Set the media view.
        if (type == 1) {
            adView1.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
            adView1.getMediaView().setMediaContent(nativeAd.getMediaContent());
            // Get the video controller for the ad. One will always be provided, even if the ad doesn't
            // have a video asset.
            VideoController vc = nativeAd.getMediaContent().getVideoController();
            // Updates the UI to say whether or not this ad has a video asset.
            if (vc.hasVideoContent()) {
                // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
                // VideoController will call methods on this object when events occur in the video
                // lifecycle.
                vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                    @Override
                    public void onVideoEnd() {
                        super.onVideoEnd();
                    }
                });
            }
        }

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView1.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView1.getBodyView().setVisibility(View.GONE);
        } else {
            adView1.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView1.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView1.getCallToActionView().setVisibility(View.GONE);
        } else {
            adView1.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView1.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView1.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView1.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            if (type != 2) {
                adView1.getIconView().setVisibility(View.VISIBLE);
            }

        }

        if (nativeAd.getPrice() == null) {
            adView1.getPriceView().setVisibility(View.GONE);
        } else {
            adView1.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView1.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView1.getStoreView().setVisibility(View.GONE);
        } else {
            adView1.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView1.getStoreView()).setText(nativeAd.getStore());
        }

        if (type == 2 || type == 3) {
            if (type == 2) {
                if (nativeAd.getStarRating() == null) {
                    adView1.getStarRatingView().setVisibility(View.GONE);
                } else {
                    ((RatingBar) adView1.getStarRatingView())
                            .setRating(nativeAd.getStarRating().floatValue());
                    adView1.getStarRatingView().setVisibility(View.VISIBLE);
                }
            } else {
                adView1.getStarRatingView().setVisibility(View.GONE);
            }
        } else {
            if (nativeAd.getStarRating() == null) {
                adView1.getStarRatingView().setVisibility(View.GONE);
            } else {
                ((RatingBar) adView1.getStarRatingView())
                        .setRating(nativeAd.getStarRating().floatValue());
                adView1.getStarRatingView().setVisibility(View.VISIBLE);
            }
        }

        if (nativeAd.getAdvertiser() == null) {
            adView1.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) adView1.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView1.getAdvertiserView().setVisibility(View.GONE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView1.setNativeAd(nativeAd);

        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(adView);
    }

    //POO
    private void populateNativeBannerAdView(ViewGroup nativeAdContainer, NativeAd nativeAd, View adView, int type) {

        ((Button) adView.findViewById(R.id.ad_call_to_action)).setBackgroundColor(Color.parseColor("#" + nativebutton));
        ((TextView) adView.findViewById(R.id.adText)).setBackgroundColor(Color.parseColor("#" + nativebutton));
        ((TextView) adView.findViewById(R.id.ad_headline)).setTextColor(Color.parseColor("#" + nativetitle));
        ((TextView) adView.findViewById(R.id.ad_body)).setTextColor(Color.parseColor("#" + nativedescription));
        if (((LinearLayout) adView.findViewById(R.id.ll_space) != null))
            ((CardView) adView.findViewById(R.id.ll_space_card)).setCardBackgroundColor(Color.parseColor("#" + nativespaceboxColor));
        ((CardView) adView.findViewById(R.id.adview_card)).setCardBackgroundColor(Color.parseColor("#" + nativebg));


        final NativeAdView adView1 = adView.findViewById(R.id.adview);
        adView1.setVisibility(View.VISIBLE);


        // Set other ad assets.
        adView1.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView1.setBodyView(adView.findViewById(R.id.ad_body));
        adView1.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView1.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView1.setPriceView(adView.findViewById(R.id.ad_price));
        adView1.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView1.setStoreView(adView.findViewById(R.id.ad_store));
        adView1.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));


        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView1.getHeadlineView()).setText(nativeAd.getHeadline());


        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView1.getBodyView().setVisibility(View.GONE);
        } else {
            adView1.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView1.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView1.getCallToActionView().setVisibility(View.GONE);
        } else {
            adView1.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView1.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView1.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView1.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            if (type != 2) {
                adView1.getIconView().setVisibility(View.VISIBLE);
            }

        }

        if (nativeAd.getPrice() == null) {
            adView1.getPriceView().setVisibility(View.GONE);
        } else {
            adView1.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView1.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView1.getStoreView().setVisibility(View.GONE);
        } else {
            adView1.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView1.getStoreView()).setText(nativeAd.getStore());
        }

        if (type == 2 || type == 3) {
            adView1.getStarRatingView().setVisibility(View.GONE);
        } else {
            if (nativeAd.getStarRating() == null) {
                adView1.getStarRatingView().setVisibility(View.GONE);
            } else {
                ((RatingBar) adView1.getStarRatingView())
                        .setRating(nativeAd.getStarRating().floatValue());
                adView1.getStarRatingView().setVisibility(View.VISIBLE);
            }
        }


        if (nativeAd.getAdvertiser() == null) {
            adView1.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) adView1.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView1.getAdvertiserView().setVisibility(View.GONE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView1.setNativeAd(nativeAd);

        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(adView);
    }

    private void manageSpaceBox(View adView, ViewGroup nativeAdContainer) {
        if (app_adShowStatus == 1 && nativespacebox == 1) {
            if (((LinearLayout) adView.findViewById(R.id.ll_space) != null)) {
                ((LinearLayout) adView.findViewById(R.id.ll_space)).setVisibility(View.VISIBLE);
                ((CardView) adView.findViewById(R.id.ll_space_card)).setCardBackgroundColor(Color.parseColor("#" + nativespaceboxColor));
            }

            nativeAdContainer.removeAllViews();
            nativeAdContainer.addView(adView);
        } else {
            if (app_adShowStatus == 0)
                nativeAdContainer.setVisibility(View.GONE);
            if (((LinearLayout) adView.findViewById(R.id.ll_space) != null))
                ((LinearLayout) adView.findViewById(R.id.ll_space)).setVisibility(View.GONE);
        }
    }

    private void manageBannerSpaceBox(View adView, ViewGroup nativeAdContainer) {
        if (app_adShowStatus == 1 && bannerspacebox == 1) {
            if (((LinearLayout) adView.findViewById(R.id.ll_space) != null)) {
                ((LinearLayout) adView.findViewById(R.id.ll_space)).setVisibility(View.VISIBLE);
                ((CardView) adView.findViewById(R.id.ll_space_card)).setCardBackgroundColor(Color.parseColor("#" + nativespaceboxColor));
            }

            nativeAdContainer.removeAllViews();
            nativeAdContainer.addView(adView);

        } else {
            if (app_adShowStatus == 0)
                nativeAdContainer.setVisibility(View.GONE);
            if (((LinearLayout) adView.findViewById(R.id.ll_space) != null))
                ((LinearLayout) adView.findViewById(R.id.ll_space)).setVisibility(View.GONE);
        }
    }

    private View getView(Activity activity, ViewGroup nativeAdContainer, int type) {
        View adView;
        switch (type) {

            case 1:
                if (app_nativeBig == 1)
                    adView = activity.getLayoutInflater().inflate(R.layout.admob_med, null);
                else
                    adView = activity.getLayoutInflater().inflate(R.layout.admob_medium_bannerad, null);
                break;

            case 2:
                if (app_nativeBig == 1)
                    adView = activity.getLayoutInflater().inflate(R.layout.admob_small, null);
                else
                    adView = activity.getLayoutInflater().inflate(R.layout.admob_smart_bannerad, null);
                break;

            case 3:
                if (app_nativeBanner == 1)
                    adView = activity.getLayoutInflater().inflate(R.layout.admob_small_banner, null);
                else
                    adView = activity.getLayoutInflater().inflate(R.layout.admob_bannerad, null);
                break;

            default:
                adView = activity.getLayoutInflater().inflate(R.layout.admob_med, null);
                setViewHeight(activity, nativeAdContainer, type);
                break;

        }
        return adView;
    }

    private void setViewHeight(Activity activity, ViewGroup view, int type) {
        switch (type) {
            case 1:
                if (app_nativeBig == 1 || app_nativeBig == 3) {
                    Log.e("app_nativeBig", "setViewHeight: " + (activity.getResources().getDisplayMetrics().heightPixels * 40) / 100);
                    view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (activity.getResources().getDisplayMetrics().heightPixels * 40) / 100));
                } else if (app_nativeBig == 2) {
                    view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (activity.getResources().getDisplayMetrics().heightPixels * 35) / 100));
                }
                break;
        }
    }

    private void LoadAd(AdLoader.Builder builder, final ViewGroup nativeAdContainer, final int type) {
        VideoOptions videoOptions = new VideoOptions.Builder().build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).setAdChoicesPlacement(ADCHOICES_TOP_RIGHT).build();
        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(
                new AdListener() {
                    @Override
                    public void onAdImpression() {

                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e("admobNative_LoadAd", "onAdFailedToLoad:  ");
                        admobnativeloadingstatus = "fail";
                        if (app_customAdStatus == 1 && app_customNativeBannerStatus == 1) {
                            showMyCustomNative(activity, nativeAdContainer, type);
                        } else if (nativespacebox == 0) {
                            nativeAdContainer.setVisibility(View.GONE);
                        }
                    }

                }).build();

        Log.e("admobNative_LoadAd", "loading:");
        admobnativeloadingstatus = "loading";
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void LoadAd2(AdLoader.Builder builder, final ViewGroup nativeAdContainer, final int type) {
        VideoOptions videoOptions = new VideoOptions.Builder().build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).setAdChoicesPlacement(ADCHOICES_TOP_RIGHT).build();
        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(
                new AdListener() {
                    @Override
                    public void onAdImpression() {

                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e("admobNative", "onAdFailedToLoad:  ");

                        if (app_customAdStatus == 1 && app_customNativeBannerStatus == 1) {
                            showMyCustomNative(activity, nativeAdContainer, type);
                        } else if (nativespacebox == 0) {
                            nativeAdContainer.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        Log.e("admobNative", "onAdLoaded native : load" + type);

                    }
                }).build();

        Log.e("admobNative", "onAdLoaded native : AdRequest" + type);
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void LoadAd3(AdLoader.Builder builder, final ViewGroup nativeAdContainer, final int type) {
        VideoOptions videoOptions = new VideoOptions.Builder().build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).setAdChoicesPlacement(ADCHOICES_TOP_RIGHT).build();
        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(
                new AdListener() {
                    @Override
                    public void onAdImpression() {

                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e("admobNative", "onAdFailedToLoad:  ");
                        if (app_customAdStatus == 1 && app_customNativeBannerStatus == 1) {
                            showMyCustomNative(activity, nativeAdContainer, type);
                        } else if (nativespacebox == 0) {
                            nativeAdContainer.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        Log.e("admobNative", "onAdLoaded native : load" + type);

                    }
                }).build();

        Log.e("admobNative", "onAdLoaded native : AdRequest" + type);
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void LoadAd4(AdLoader.Builder builder, final ViewGroup nativeAdContainer, final int type) {
        VideoOptions videoOptions = new VideoOptions.Builder().build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).setAdChoicesPlacement(ADCHOICES_TOP_RIGHT).build();
        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(
                new AdListener() {
                    @Override
                    public void onAdImpression() {

                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e("admobNative", "onAdFailedToLoad:  ");

                        if (app_customAdStatus == 1 && app_customNativeBannerStatus == 1) {
                            showMyCustomNative(activity, nativeAdContainer, type);
                        } else if (nativespacebox == 0) {
                            nativeAdContainer.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        Log.e("admobNative", "onAdLoaded native : load" + type);

                    }
                }).build();

        Log.e("admobNative", "onAdLoaded native : AdRequest" + type);
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    //POO
    private void LoadBannerAd(AdLoader.Builder builder, final ViewGroup nativeAdContainer, final int type) {
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setAdChoicesPlacement(ADCHOICES_TOP_RIGHT).build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(
                new AdListener() {
                    @Override
                    public void onAdImpression() {

                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e("admobBanner", "onAdFailedToLoad:  ");
                        banneradmobloadingstatus = "fail";
                        if (bannerspacebox == 0) {
                            nativeAdContainer.setVisibility(View.GONE);
                        }


                    }


                }).build();
        Log.e("admobBanner", "loading:  ");
        banneradmobloadingstatus = "loading";
        Log.e("admobBanner", "onAdLoaded banner : AdRequest" + type);
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void preloadBannerAds(Activity activity) {
        if (!wasBannerLoadTimeLessThanNHoursAgo()) {
            Log.e("admobBanner", "wasBannerLoadTimeLessThanNHoursAgo : " + wasBannerLoadTimeLessThanNHoursAgo());
            banneradmobloadingstatus = "fail";
        }
        if (!banneradmobloadingstatus.equals("show") && !banneradmobloadingstatus.equals("fail")) {
            return;
        }
        adViewPreload = new AdView(activity);
        AdRequest adRequest = new AdRequest.Builder().build();
        Log.e("admobBanner", "loading:  ");
        banneradmobloadingstatus = "loading";
        adViewPreload.setAdUnitId(ADMOB_B[0]);
        AdSize adSize = getAdSize(activity);
        adViewPreload.setAdSize(adSize);
        adViewPreload.loadAd(adRequest);
        adViewPreload.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.e("admobBanner", "fail:  ");
                banneradmobloadingstatus = "fail";

                adViewPreload = null;
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                Log.e("admobBanner", "loaded:  ");
                banneradmobloadingstatus = "loaded";
                loadTimeBanner = (new Date()).getTime();

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });
    }

    //POO
    public void showBanner(final Activity activity, final ViewGroup nativeAdContainer, View view) {
        if (app_adShowStatus == 0) {
            view.setVisibility(View.GONE);
            return;
        }

        final FrameLayout banner_container = view.findViewById(R.id.banner_container);
        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(view);
        if (adViewPreload != null) {
            banner_container.removeAllViews();
            banner_container.addView(adViewPreload);
            adViewPreload = null;
            Log.e("admobBanner", "show:  ");
            if (((LinearLayout) view.findViewById(R.id.ll_space) != null))
                ((LinearLayout) view.findViewById(R.id.ll_space)).setVisibility(View.GONE);
            banneradmobloadingstatus = "show";
            preloadBannerAds(activity);
        } else {
            adViewPreload = new AdView(activity);
            AdRequest adRequest = new AdRequest.Builder().build();
            adViewPreload.setAdUnitId(ADMOB_B[0]);
            AdSize adSize = getAdSize(activity);
            adViewPreload.setAdSize(adSize);
            adViewPreload.loadAd(adRequest);
            adViewPreload.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    // Code to be executed when an ad request fails.
                    Log.e("admobBanner", "fail:  ");
                    banneradmobloadingstatus = "fail";
                    nativeAdContainer.setVisibility(View.GONE);
                }

                @Override
                public void onAdImpression() {
                    // Code to be executed when an impression is recorded
                    // for an ad.
                }

                @Override
                public void onAdLoaded() {
                    Log.e("admobBanner", "loaded:  ");
                    if (((LinearLayout) view.findViewById(R.id.ll_space) != null))
                        ((LinearLayout) view.findViewById(R.id.ll_space)).setVisibility(View.GONE);
                    banneradmobloadingstatus = "loaded";
                    loadTimeBanner = (new Date()).getTime();
                    banner_container.removeAllViews();
                    banner_container.addView(adViewPreload);
                    adViewPreload = null;
                    Log.e("admobBanner", "show:  ");
                    banneradmobloadingstatus = "show";
                    preloadBannerAds(activity);
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }
            });
        }

    }

    private AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);

        /**
         * FOR NATIVE TYPE BANNER VIEW ADS (ALWAYS INTEGRATE IT IN SCROLLVIEW)
         **/
        //return AdSize.getLandscapeInlineAdaptiveBannerAdSize(activity, adWidth);
    }

    public CastTvCustomModel getMyCustomAd() {
        CastTvCustomModel customModel = null;
        if (myAppMarketingList.size() > 0 && totalAdInc == myAppMarketingList.size()) {
            totalAdInc = 0;
        }
        customModel = myAppMarketingList.get(totalAdInc);

        totalAdInc++;
        return customModel;
    }

    public void SetCustomAdData(JsonArray array) {
        if (array != null && array.size() != 0) {
            if (myAppMarketingList != null) {
                myAppMarketingList.clear();
            }

            for (int i = 0; i < array.size(); i++) {
                JsonObject object = (JsonObject) array.get(i);
                CastTvCustomModel customModel = new CastTvCustomModel();
                customModel.setId(object.get("QZXIT3_ska_appCstm_id").getAsInt());
                customModel.setName(object.get("QZXIT3_ska_appCstm_name").getAsString());
                customModel.setUrl(object.get("QZXIT3_ska_appCstm_url").getAsString());
                customModel.setAppopen_image(object.get("QZXIT3_ska_appcstm_AO_IMG").getAsString());
                customModel.setInterstitial_image(object.get("QZXIT3_ska_appCstm_INTER_IMG").getAsString());
                customModel.setSmall_native_image(object.get("QZXIT3_ska_appCstm_SNTV_IMG").getAsString());
                customModel.setNative_image(object.get("QZXIT3_ska_appCstm_NTV_IMG").getAsString());

                saveCache(customModel.getAppopen_image());
                saveCache(customModel.getInterstitial_image());
                saveCache(customModel.getSmall_native_image());
                saveCache(customModel.getNative_image());

                myAppMarketingList.add(customModel);
            }
        }
    }

    private void saveCache(String url) {
        Glide.with(activity)
                .load(url)
                .preload();
    }

    private void showMyCustomNative(final Activity activity, final ViewGroup nativeAdContainer, int type) {
        if (myAppMarketingList.size() != 0) {
            final CastTvCustomModel appModal = getMyCustomAd();
            if (appModal != null) {

                View inflate = getCustomView(activity, nativeAdContainer, type);

                if (app_adShowStatus == 0 || app_nativeBig == 3) {
                    if (nativespacebox == 0)
                        nativeAdContainer.setVisibility(View.GONE);
                    return;
                }

                if (type == 1) {
                    Glide.with(activity)
                            .load(appModal.native_image)
                            .into((ImageView) inflate.findViewById(R.id.custNative));
                } else {
                    Glide.with(activity)
                            .load(appModal.small_native_image)
                            .into((ImageView) inflate.findViewById(R.id.custNative));

                }
                inflate.findViewById(R.id.custNative).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (CastTvAppManager.isNetworkAvailable(activity)) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appModal.url));
                            activity.startActivity(browserIntent);
                        } else {
                            Toast.makeText(activity, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                nativeAdContainer.removeAllViews();
                nativeAdContainer.addView(inflate);
            } else {
                nativeAdContainer.setVisibility(View.GONE);
            }
        } else {
            nativeAdContainer.setVisibility(View.GONE);
        }
    }

    private View getCustomView(Activity activity, ViewGroup nativeAdContainer, int type) {
        View adView;
        adView = activity.getLayoutInflater().inflate(R.layout.custom_native, null);
        if (type == 1)
            nativeAdContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (activity.getResources().getDisplayMetrics().heightPixels * 34) / 100));
        else if (type == 2)
            nativeAdContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (activity.getResources().getDisplayMetrics().heightPixels * 20) / 100));
        return adView;
    }

    public void showRedirectDialog(final String url, final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        View view = activity.getLayoutInflater().inflate(R.layout.update_dialog_view, null);
        dialog.setContentView(view);
        ImageView update = view.findViewById(R.id.update);
        ImageView update_redirect_text = view.findViewById(R.id.update_redirect_text);
        update_redirect_text.setImageResource(R.drawable.sdk_redirect_text);
        update.setImageResource(R.drawable.sdk_redirect_btn);
        ImageView image_dialog = view.findViewById(R.id.image_dialog);
        image_dialog.setImageResource(R.drawable.sdk_image_redirect);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getLatestRedirectUrl(activity, url);
                } catch (ActivityNotFoundException ignored1) {
                    try {
                        Uri marketUri = Uri.parse(url);
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                        activity.startActivity(marketIntent);
                    } catch (ActivityNotFoundException e) {
                    }
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public void showUpdateDialog(final String url, final Activity context) {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        View view = context.getLayoutInflater().inflate(R.layout.update_dialog_view, null);
        dialog.setContentView(view);
        ImageView update = view.findViewById(R.id.update);
        ImageView update_redirect_text = view.findViewById(R.id.update_redirect_text);
        update_redirect_text.setImageResource(R.drawable.sdk_update_text);
        update.setImageResource(R.drawable.sdk_update_now_btn);
        ImageView image_dialog = view.findViewById(R.id.image_dialog);
        image_dialog.setImageResource(R.drawable.sdk_image_update);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri marketUri = Uri.parse(url);
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    context.startActivity(marketIntent);
                } catch (ActivityNotFoundException ignored1) {
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void getLatestRedirectUrl(final Activity activity, String url) {
        Uri marketUri = Uri.parse(url);
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        activity.startActivity(marketIntent);
    }

    public static void openExitDialog(final Activity activity) {
        final BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.Ad2_CustomBottomSheetDialogTheme);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.windowAnimations = R.style.Ad2_DialogAnimation;
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        dialog.setContentView(R.layout.exit_dialog_view);

        if (CastTvAppManager.nativeExit == 1) {
            CastTvAppManager.getInstance(activity).showNativeAds(activity, (
                    ViewGroup) dialog.findViewById(R.id.native_container), (ImageView) dialog.findViewById(R.id.native_space_img), 1);
        }

        ImageView btn_exit = (ImageView) dialog.findViewById(R.id.btn_exit);
        ImageView btn_no = (ImageView) dialog.findViewById(R.id.no);


        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (app_vpnEnable == 1) {
                    try {
                        new BaseAdActivity().disconnectFromVnp();
                    } catch (Exception e) {

                    }
                }
                activity.finishAffinity();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager manager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }


}
