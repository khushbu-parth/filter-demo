package com.pesonal.adsdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;


public class AppManage {

    public static String ADMOB = "Admob";
    public static String FACEBOOK = "Facebookaudiencenetwork";

    public static int count_banner = -1;
    public static int count_native = -1;
    public static int count_click = -1;
    public static int count_click_for_alt = -1;
    public static String app_privacyPolicyLink = "";
    public static int app_needInternet = 0;
    public static int app_updateAppDialogStatus = 0;
    public static int app_dialogBeforeAdShow = 0;
    public static String app_versionCode = "";
    public static int app_redirectOtherAppStatus = 0;
    public static String app_newPackageName = "";
    public static int app_adShowStatus = 1;
    public static int app_howShowAd = 0;
    public static String app_adPlatformSequence = "";
    public static String app_alernateAdShow = "";
    public static int app_mainClickCntSwAd = 0;
    public static int app_innerClickCntSwAd = 0;

    public InterstitialAd interstitial1;
    public static String ADMOB_APPID = "";
    public static String ADMOB_I1 = "";
    public static String ADMOB_B1 = "";
    public static String ADMOB_N1 = "";

    private com.facebook.ads.InterstitialAd fbinterstitialAd1;
    public static String FACEBOOK_I1 = "";
    public static String FACEBOOK_B1 = "";
    public static String FACEBOOK_NB1 = "";
    public static String FACEBOOK_N1 = "";


    public static int admob_AdStatus = 0;
    public static int facebook_AdStatus = 0;

    public static SharedPreferences mysharedpreferences;
    public static int ad_dialog_time_in_second = 2;

    static Context activity;
    static MyCallback myCallback;
    private static AppManage mInstance;

    String admob_b, facebook_nb, facebook_b;
    String admob_n, facebook_n;

    ArrayList<String> banner_sequence = new ArrayList<>();
    ArrayList<String> native_sequence = new ArrayList<>();
    ArrayList<String> interstitial_sequence = new ArrayList<>();


    private Dialog dialog;

    public AppManage(Context activity) {
        AppManage.activity = activity;
        mysharedpreferences = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        getResponseFromPref();

    }

    public static AppManage getInstance(Context activity) {
        if (mInstance == null) {
            mInstance = new AppManage(activity);
        }
        return mInstance;
    }

    public static boolean hasActiveInternetConnection(Context context) {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void getResponseFromPref() {
        String response1 = mysharedpreferences.getString("response", "");
        Log.e("===", "getResponseFromPref: " + response1);
        if (!response1.isEmpty()) {
            try {
                JSONObject response = new JSONObject(response1);
                JSONObject settingsJsonObject = response.getJSONObject("APP_SETTINGS");

                app_privacyPolicyLink = settingsJsonObject.getString("app_privacyPolicyLink");
                app_needInternet = settingsJsonObject.getInt("app_needInternet");
                app_updateAppDialogStatus = settingsJsonObject.getInt("app_updateAppDialogStatus");
                app_versionCode = settingsJsonObject.getString("app_versionCode");
                app_redirectOtherAppStatus = settingsJsonObject.getInt("app_redirectOtherAppStatus");
                app_newPackageName = settingsJsonObject.getString("app_newPackageName");
                app_dialogBeforeAdShow = settingsJsonObject.getInt("app_dialogBeforeAdShow");
                app_adShowStatus = settingsJsonObject.getInt("app_adShowStatus");
                app_howShowAd = settingsJsonObject.getInt("app_howShowAd");
                app_adPlatformSequence = settingsJsonObject.getString("app_adPlatformSequence");
                app_alernateAdShow = settingsJsonObject.getString("app_alernateAdShow");
                app_mainClickCntSwAd = settingsJsonObject.getInt("app_mainClickCntSwAd");
                app_innerClickCntSwAd = settingsJsonObject.getInt("app_innerClickCntSwAd");
                boolean app_AppOpenAdStatus;
                if (settingsJsonObject.getInt("app_AppOpenAdStatus") == 1) {
                    app_AppOpenAdStatus = true;
                } else {
                    app_AppOpenAdStatus = false;
                }

                SharedPreferences.Editor editor = mysharedpreferences.edit();
                editor.putString("app_privacyPolicyLink", app_privacyPolicyLink);
                editor.putInt("app_needInternet", app_needInternet);
                editor.putInt("app_updateAppDialogStatus", app_updateAppDialogStatus);
                editor.putString("app_versionCode", app_versionCode);
                editor.putInt("app_redirectOtherAppStatus", app_redirectOtherAppStatus);
                editor.putString("app_newPackageName", app_newPackageName);
                editor.putInt("app_adShowStatus", app_adShowStatus);
                editor.putInt("app_howShowAd", app_howShowAd);
                editor.putString("app_adPlatformSequence", app_adPlatformSequence);
                editor.putString("app_alernateAdShow", app_alernateAdShow);
                editor.putInt("app_mainClickCntSwAd", app_mainClickCntSwAd);
                editor.putInt("app_innerClickCntSwAd", app_innerClickCntSwAd);
                editor.putBoolean("app_AppOpenAdStatus", app_AppOpenAdStatus);
                editor.commit();

                JSONObject AdmobJsonObject = response.getJSONObject("PLACEMENT").getJSONObject("Admob");
                admob_AdStatus = AdmobJsonObject.getInt("ad_showAdStatus");
                ADMOB_APPID = AdmobJsonObject.getString("AppID");
                ADMOB_B1 = AdmobJsonObject.getString("Banner1");
                ADMOB_I1 = AdmobJsonObject.getString("Interstitial1");
                ADMOB_N1 = AdmobJsonObject.getString("Native1");

                SharedPreferences.Editor editor1 = mysharedpreferences.edit();
                editor1.putString("AppOpenID", AdmobJsonObject.getString("AppOpen"));
//                editor1.putString("AppOpenID", "ca-app-pub-3940256099942544/3419835294");
                editor1.commit();

                JSONObject FBJsonObject = response.getJSONObject("PLACEMENT").getJSONObject("Facebookaudiencenetwork");
                facebook_AdStatus = FBJsonObject.getInt("ad_showAdStatus");
                FACEBOOK_B1 = FBJsonObject.getString("Banner1");
                FACEBOOK_NB1 = FBJsonObject.getString("NativeBanner1");
                FACEBOOK_I1 = FBJsonObject.getString("Interstitial1");
                FACEBOOK_N1 = FBJsonObject.getString("Native1");


            } catch (Exception e) {
                Log.e("rrrrr", e.getMessage());
            }

        }

    }

    public List<MoreApp_Data> get_MoreAppData() {
        List<MoreApp_Data> data = new ArrayList<>();
        SharedPreferences preferences = activity.getSharedPreferences("ad_pref", 0);
        try {

            JSONArray array = new JSONArray(preferences.getString("MORE_APP", ""));
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                data.add(new MoreApp_Data(object.getString("app_id"), object.getString("app_name"), object.getString("app_packageName"), object.getString("app_logo"), object.getString("app_status")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }


    private static void initAd() {
        if (admob_AdStatus == 1) {
            Log.e("====", "initAd: " + "2222");

            MobileAds.initialize(activity, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
        }

//        AudienceNetworkAds.initialize(activity);
//        AdSettings.addTestDevice(activity.getResources().getString(R.string.device_id));


        if (facebook_AdStatus == 1) {

            Log.e("====", "initAd: " + "1111");

            AudienceNetworkAds.initialize(activity);

            AdSettings.addTestDevice(activity.getResources().getString(R.string.device_id));
        } else {
            Log.e("====", "initAd: " + "0000");
        }


    }

    private static boolean checkUpdate(int cversion) {


        if (mysharedpreferences.getInt("app_updateAppDialogStatus", 0) == 1) {
            String versions = mysharedpreferences.getString("app_versionCode", "");
            String str[] = versions.split(",");

            try {


                if (Arrays.asList(str).contains(cversion + "")) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return false;
    }

    public void getResponseFromPref(getDataListner listner, int cversion) {
        String response1 = mysharedpreferences.getString("response", "");
        if (!response1.isEmpty()) {
            try {
                JSONObject response = new JSONObject(response1);
                JSONObject settingsJsonObject = response.getJSONObject("APP_SETTINGS");

                app_privacyPolicyLink = settingsJsonObject.getString("app_privacyPolicyLink");
                app_needInternet = settingsJsonObject.getInt("app_needInternet");
                app_updateAppDialogStatus = settingsJsonObject.getInt("app_updateAppDialogStatus");
                app_versionCode = settingsJsonObject.getString("app_versionCode");
                app_redirectOtherAppStatus = settingsJsonObject.getInt("app_redirectOtherAppStatus");
                app_newPackageName = settingsJsonObject.getString("app_newPackageName");
                app_dialogBeforeAdShow = settingsJsonObject.getInt("app_dialogBeforeAdShow");
                app_adShowStatus = settingsJsonObject.getInt("app_adShowStatus");
                app_howShowAd = settingsJsonObject.getInt("app_howShowAd");
                app_adPlatformSequence = settingsJsonObject.getString("app_adPlatformSequence");
                app_alernateAdShow = settingsJsonObject.getString("app_alernateAdShow");
                app_mainClickCntSwAd = settingsJsonObject.getInt("app_mainClickCntSwAd");
                app_innerClickCntSwAd = settingsJsonObject.getInt("app_innerClickCntSwAd");
                boolean app_AppOpenAdStatus;
                if (settingsJsonObject.getInt("app_AppOpenAdStatus") == 1) {
                    app_AppOpenAdStatus = true;
                } else {
                    app_AppOpenAdStatus = false;
                }

                SharedPreferences.Editor editor = mysharedpreferences.edit();
                editor.putString("app_privacyPolicyLink", app_privacyPolicyLink);
                editor.putInt("app_needInternet", app_needInternet);
                editor.putInt("app_updateAppDialogStatus", app_updateAppDialogStatus);
                editor.putString("app_versionCode", app_versionCode);
                editor.putInt("app_redirectOtherAppStatus", app_redirectOtherAppStatus);
                editor.putString("app_newPackageName", app_newPackageName);
                editor.putInt("app_adShowStatus", app_adShowStatus);
                editor.putInt("app_howShowAd", app_howShowAd);
                editor.putString("app_adPlatformSequence", app_adPlatformSequence);
                editor.putString("app_alernateAdShow", app_alernateAdShow);
                editor.putInt("app_mainClickCntSwAd", app_mainClickCntSwAd);
                editor.putInt("app_innerClickCntSwAd", app_innerClickCntSwAd);
                editor.putBoolean("app_AppOpenAdStatus", app_AppOpenAdStatus);
                editor.commit();

                JSONObject AdmobJsonObject = response.getJSONObject("PLACEMENT").getJSONObject("Admob");
                admob_AdStatus = AdmobJsonObject.getInt("ad_showAdStatus");
                ADMOB_APPID = AdmobJsonObject.getString("AppID");
                ADMOB_B1 = AdmobJsonObject.getString("Banner1");
                ADMOB_I1 = AdmobJsonObject.getString("Interstitial1");
                ADMOB_N1 = AdmobJsonObject.getString("Native1");

                SharedPreferences.Editor editor1 = mysharedpreferences.edit();
                editor1.putString("AppOpenID", AdmobJsonObject.getString("AppOpen"));
//                editor1.putString("AppOpenID", "ca-app-pub-3940256099942544/3419835294");
                editor1.commit();

                JSONObject FBJsonObject = response.getJSONObject("PLACEMENT").getJSONObject("Facebookaudiencenetwork");
                facebook_AdStatus = FBJsonObject.getInt("ad_showAdStatus");
                FACEBOOK_B1 = FBJsonObject.getString("Banner1");
                FACEBOOK_NB1 = FBJsonObject.getString("NativeBanner1");
                FACEBOOK_I1 = FBJsonObject.getString("Interstitial1");
                FACEBOOK_N1 = FBJsonObject.getString("Native1");


                listner.ongetExtradata(response.getJSONObject("EXTRA_DATA"));

            } catch (Exception e) {
                Log.e("error", e.getMessage() + "");
            }

            if (app_redirectOtherAppStatus == 1) {
                String redirectNewPackage = mysharedpreferences.getString("app_newPackageName", "");
                listner.onRedirect(redirectNewPackage);
            } else if (app_updateAppDialogStatus == 1 && checkUpdate(cversion)) {
                listner.onUpdate("https://play.google.com/store/apps/details?id=" + activity.getPackageName());
            } else {
                listner.onsuccess();
                initAd();
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }
        }
    }

    public void loadintertialads(Activity activity, String google_i, String facebook_i) {

        if (app_adShowStatus == 0) {
            return;
        }

        if (admob_AdStatus == 1 && !google_i.isEmpty()) {
            loadAdmobInterstitial(activity, google_i);
        }


        if (facebook_AdStatus == 1 && !google_i.isEmpty()) {
            loadFacebookInterstitial(activity, facebook_i);
        }


    }


    private void loadFacebookInterstitial(Activity activity, String facebook_i) {
        fbinterstitialAd1 = new com.facebook.ads.InterstitialAd(activity, facebook_i);
        fbinterstitialAd1.loadAd(fbinterstitialAd1.buildLoadAdConfig().withAdListener(new AbstractAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                super.onError(ad, error);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                super.onAdLoaded(ad);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                super.onInterstitialDismissed(ad);
                fbinterstitialAd1.loadAd();
                interstitialCallBack();
            }
        }).build());
    }

    private void loadAdmobInterstitial(Activity activity, String google_i) {
        this.interstitial1 = new InterstitialAd(activity);
        this.interstitial1.setAdUnitId(google_i);
        interstitial1.loadAd(new AdRequest.Builder().build());
        this.interstitial1.setAdListener(new AdListener() {
            public void onAdLoaded() {
            }

            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            public void onAdClosed() {
                super.onAdClosed();

                interstitial1.loadAd(new AdRequest.Builder().build());

                interstitialCallBack();

            }

            public void onAdOpened() {
                super.onAdOpened();
            }
        });
    }

    public void show_INTERSTIAL(Context context, MyCallback myCallback) {
        displayInterstitial(context, myCallback, 0, "");
    }

    public void show_INTERSTIAL(Context context, MyCallback myCallback, String specific_platform) {
        displayInterstitial(context, myCallback, 0, specific_platform);
    }

    public void show_INTERSTIAL(Context context, MyCallback myCallback, int how_many_clicks) {
        displayInterstitial(context, myCallback, how_many_clicks, "");
    }


    public void show_INTERSTIAL(Context context, MyCallback myCallback, int how_many_clicks, String specific_platform) {
        displayInterstitial(context, myCallback, how_many_clicks, specific_platform);
    }

    public void show_INTERSTIAL(Context context, MyCallback myCallback, String specific_platform, int how_many_clicks) {
        displayInterstitial(context, myCallback, how_many_clicks, specific_platform);
    }


    public void displayInterstitial(Context context, MyCallback myCallback2, int how_many_clicks, String specific_platform) {
        this.myCallback = myCallback2;

        count_click++;

        if (app_adShowStatus == 0) {
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
            return;
        }
        if (how_many_clicks != 0) {
            if (count_click % how_many_clicks != 0) {
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
                return;
            }
        }

        count_click_for_alt++;


        int app_howShowAd = mysharedpreferences.getInt("app_howShowAd", 0);
        String adPlatformSequence = mysharedpreferences.getString("app_adPlatformSequence", "");
        String alernateAdShow = mysharedpreferences.getString("app_alernateAdShow", "");

        if (!specific_platform.isEmpty()) {
            app_howShowAd = 0;
            adPlatformSequence = specific_platform;
        }


        interstitial_sequence = new ArrayList<String>();
        if (app_howShowAd == 0 && !adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");

            for (int i = 0; i < adSequence.length; i++) {
                interstitial_sequence.add(adSequence[i]);
            }

        } else if (app_howShowAd == 1 && !alernateAdShow.isEmpty()) {
            String alernateAd[] = alernateAdShow.split(",");

            int index = 0;
            for (int j = 0; j <= 10; j++) {
                if (count_click_for_alt % alernateAd.length == j) {
                    index = j;
                    interstitial_sequence.add(alernateAd[index]);
                }
            }

            String adSequence[] = adPlatformSequence.split(",");
            for (int j = 0; j < adSequence.length; j++) {
                if (interstitial_sequence.size() != 0) {
                    if (!interstitial_sequence.get(0).equals(adSequence[j])) {
                        interstitial_sequence.add(adSequence[j]);
                    }
                }

            }
        } else {
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
        }

        if (interstitial_sequence.size() != 0) {
            showInterstitialAd(interstitial_sequence.get(0), context);
        }


    }

    private void showInterstitialAd(String platform, final Context context) {

        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.ad_progress_dialog, null);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        if (platform.equals("Admob") && admob_AdStatus == 1 && interstitial1 != null) {
            if (interstitial1.isLoaded()) {
                if (app_dialogBeforeAdShow == 1) {
                    dialog.show();

                    final CircularProgressIndicator circular_progress = view.findViewById(R.id.circular_progress);
                    circular_progress.setProgress(0, 100);
                    new CountDownTimer(ad_dialog_time_in_second * 1000, 10) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            double time = (millisUntilFinished / 10) / ad_dialog_time_in_second;
                            circular_progress.setProgress(time, 100);
                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                            interstitial1.show();
                        }
                    }.start();

                } else {
                    interstitial1.show();
                }
            } else {
                interstitial1.loadAd(new AdRequest.Builder().build());
                nextInterstitialPlatform(context);
            }
        } else if (platform.equals("Facebookaudiencenetwork") && facebook_AdStatus == 1 && fbinterstitialAd1 != null) {

            if (fbinterstitialAd1.isAdLoaded()) {
                if (app_dialogBeforeAdShow == 0) {

                    dialog.show();

                    final CircularProgressIndicator circular_progress = view.findViewById(R.id.circular_progress);
                    circular_progress.setProgress(0, 100);
                    new CountDownTimer(ad_dialog_time_in_second * 1000, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            double time = (millisUntilFinished / 10) / ad_dialog_time_in_second;
                            circular_progress.setProgress(time, 100);
                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                            fbinterstitialAd1.show();
                        }
                    }.start();

                } else {
                    fbinterstitialAd1.show();
                }
            } else {
                fbinterstitialAd1.loadAd();
                nextInterstitialPlatform(context);
            }

        } else {

            nextInterstitialPlatform(context);

        }
    }

    private void nextInterstitialPlatform(Context context) {

        if (interstitial_sequence.size() != 0) {
            interstitial_sequence.remove(0);

            if (interstitial_sequence.size() != 0) {
                showInterstitialAd(interstitial_sequence.get(0), context);
            } else {
                interstitialCallBack();
            }

        } else {
            interstitialCallBack();

        }
    }

    public void interstitialCallBack() {

        if (myCallback != null) {
            myCallback.callbackCall();
            AppManage.this.myCallback = null;
        }
    }

    public void show_BANNER(ViewGroup banner_container, String admob_b, String facebook_b) {
        this.admob_b = admob_b;
        this.facebook_b = facebook_b;


        if (!hasActiveInternetConnection(activity)) {
            return;
        }

        if (app_adShowStatus == 0) {
            return;
        }

        count_banner++;


        int app_howShowAd = mysharedpreferences.getInt("app_howShowAd", 0);
        String adPlatformSequence = mysharedpreferences.getString("app_adPlatformSequence", "");
        String alernateAdShow = mysharedpreferences.getString("app_alernateAdShow", "");


        banner_sequence = new ArrayList<String>();
        if (app_howShowAd == 0 && !adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");
            for (int i = 0; i < adSequence.length; i++) {
                banner_sequence.add(adSequence[i]);
            }

        } else if (app_howShowAd == 1 && !alernateAdShow.isEmpty()) {
            String alernateAd[] = alernateAdShow.split(",");

            int index = 0;
            for (int j = 0; j <= 10; j++) {
                if (count_banner % alernateAd.length == j) {
                    index = j;
                    banner_sequence.add(alernateAd[index]);
                }
            }

            String adSequence[] = adPlatformSequence.split(",");
            for (int j = 0; j < adSequence.length; j++) {
                if (banner_sequence.size() != 0) {
                    if (!banner_sequence.get(0).equals(adSequence[j])) {
                        banner_sequence.add(adSequence[j]);
                    }
                }
            }
        }


        if (banner_sequence.size() != 0) {
            showBanner(banner_sequence.get(0), banner_container);
        }


    }

    public void showBanner(String platform, ViewGroup banner_container) {
        if (platform.equals("Admob") && admob_AdStatus == 1) {
            showAdmobBanner(banner_container);
        } else if (platform.equals("Facebookaudiencenetwork") && facebook_AdStatus == 1) {
            showFacebookBanner(banner_container);
        } else {
            nextBannerPlatform(banner_container);
        }
    }

    private void nextBannerPlatform(ViewGroup banner_container) {
        if (banner_sequence.size() != 0) {
            banner_sequence.remove(0);
            if (banner_sequence.size() != 0) {
                showBanner(banner_sequence.get(0), banner_container);
            }
        }
    }


    private void showFacebookBanner(final ViewGroup banner_container) {
        if (facebook_b.isEmpty() || facebook_AdStatus == 0) {
            nextBannerPlatform(banner_container);
            return;
        }

        final com.facebook.ads.AdView adView = new com.facebook.ads.AdView(activity, facebook_b, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                nextBannerPlatform(banner_container);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
                banner_container.removeAllViews();
                banner_container.addView(adView);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        };

        // Request an ad
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());


    }

    private void showAdmobBanner(final ViewGroup banner_container) {
        if (admob_b.isEmpty() || admob_AdStatus == 0) {
            nextBannerPlatform(banner_container);
            return;
        }


        final AdView adView = new AdView(activity);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(admob_b);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                banner_container.removeAllViews();
                banner_container.addView(adView);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                banner_container.removeAllViews();

                nextBannerPlatform(banner_container);

            }
        });
    }


    public void show_NATIVEBANNER(ViewGroup banner_container, String admobB, String facebookNB) {
        this.admob_b = admobB;
        this.facebook_nb = facebookNB;
        if (app_adShowStatus == 0) {
            return;
        }

        count_banner++;
        int app_howShowAd = mysharedpreferences.getInt("app_howShowAd", 0);
        String adPlatformSequence = mysharedpreferences.getString("app_adPlatformSequence", "");
        String alernateAdShow = mysharedpreferences.getString("app_alernateAdShow", "");


        banner_sequence = new ArrayList<String>();
        if (app_howShowAd == 0 && !adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");
            for (int i = 0; i < adSequence.length; i++) {
                banner_sequence.add(adSequence[i]);
            }

        } else if (app_howShowAd == 1 && !alernateAdShow.isEmpty()) {
            String alernateAd[] = alernateAdShow.split(",");

            int index = 0;
            for (int j = 0; j <= 10; j++) {
                if (count_banner % alernateAd.length == j) {
                    index = j;
                    banner_sequence.add(alernateAd[index]);
                }
            }

            String adSequence[] = adPlatformSequence.split(",");
            for (int j = 0; j < adSequence.length; j++) {
                if (banner_sequence.size() != 0) {
                    if (!banner_sequence.get(0).equals(adSequence[j])) {
                        banner_sequence.add(adSequence[j]);
                    }
                }
            }
        }

        for (int i = 0; i < banner_sequence.size(); i++) {
            if (banner_sequence.get(i).equalsIgnoreCase("Unity")) {
                banner_sequence.remove(i);
            }
        }

        if (banner_sequence.size() != 0) {
            showNativeBanner(banner_sequence.get(0), banner_container);
        }
    }

    public void showNativeBanner(String platform, ViewGroup banner_container) {
        if (platform.equals("Admob") && admob_AdStatus == 1) {
            showNativeAdmobBanner(banner_container);
        } else if (platform.equals("Facebookaudiencenetwork") && facebook_AdStatus == 1) {
            showNativeFacebookBanner(banner_container);
        } else {
            nextNativeBannerPlatform(banner_container);
        }
    }

    private void nextNativeBannerPlatform(ViewGroup banner_container) {
        if (banner_sequence.size() != 0) {
            banner_sequence.remove(0);
            if (banner_sequence.size() != 0) {
                showNativeBanner(banner_sequence.get(0), banner_container);
            }
        }
    }


    private void showNativeFacebookBanner(final ViewGroup container) {
        if (facebook_nb.isEmpty() || facebook_AdStatus == 0) {
            nextNativeBannerPlatform(container);
            return;
        }

        final NativeBannerAd nativeAd1 = new NativeBannerAd(activity, facebook_nb);
        nativeAd1.loadAd(nativeAd1.buildLoadAdConfig().withAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                container.removeAllViews();
                container.setVisibility(View.VISIBLE);
                new Inflate_ADS(activity).inflate_NB_FB(nativeAd1, container);
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                nextNativeBannerPlatform(container);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAd1 == null || nativeAd1 != ad) {
                    return;
                }
                nativeAd1.downloadMedia();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        }).build());

    }

    private void showNativeAdmobBanner(final ViewGroup banner_container) {

        if (admob_b.isEmpty() || admob_AdStatus == 0) {
            nextNativeBannerPlatform(banner_container);
            return;
        }

        final AdView adView = new AdView(activity);
        adView.setAdUnitId(admob_b);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.setAdSize(AdSize.BANNER);
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                banner_container.removeAllViews();
                banner_container.addView(adView);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                banner_container.removeAllViews();

                nextNativeBannerPlatform(banner_container);

            }
        });
    }

    public void show_NATIVE(ViewGroup nativeAdContainer, String admobN1, String facebookN1) {
        this.admob_n = admobN1;
        this.facebook_n = facebookN1;
        if (app_adShowStatus == 0) {
            return;
        }

        count_native++;
        int app_howShowAd = mysharedpreferences.getInt("app_howShowAd", 0);
        String adPlatformSequence = mysharedpreferences.getString("app_adPlatformSequence", "");
        String alernateAdShow = mysharedpreferences.getString("app_alernateAdShow", "");


        native_sequence = new ArrayList<String>();
        if (app_howShowAd == 0 && !adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");
            for (int i = 0; i < adSequence.length; i++) {
                native_sequence.add(adSequence[i]);
            }

        } else if (app_howShowAd == 1 && !alernateAdShow.isEmpty()) {
            String alernateAd[] = alernateAdShow.split(",");

            int index = 0;
            for (int j = 0; j <= 10; j++) {
                if (count_native % alernateAd.length == j) {
                    index = j;
                    native_sequence.add(alernateAd[index]);
                }
            }

            String adSequence[] = adPlatformSequence.split(",");
            for (int j = 0; j < adSequence.length; j++) {
                if (native_sequence.size() != 0) {
                    if (!native_sequence.get(0).equals(adSequence[j])) {
                        native_sequence.add(adSequence[j]);
                    }
                }
            }
        }


        for (int i = 0; i < native_sequence.size(); i++) {
            if (native_sequence.get(i).equalsIgnoreCase("Unity")) {
                native_sequence.remove(i);
            }
        }

        if (native_sequence.size() != 0) {
            showNative(native_sequence.get(0), nativeAdContainer);
        }

    }

    private void showNative(String platform, ViewGroup nativeAdContainer) {
        if (platform.equals("Admob") && admob_AdStatus == 1) {
            showAdmobNative(nativeAdContainer);
        } else if (platform.equals("Facebookaudiencenetwork") && facebook_AdStatus == 1) {
            showFacebookNative(nativeAdContainer);
        } else {
            nextNativePlatform(nativeAdContainer);
        }
    }

    private void nextNativePlatform(ViewGroup nativeAdContainer) {
        if (native_sequence.size() != 0) {
            native_sequence.remove(0);
            if (native_sequence.size() != 0) {
                showNative(native_sequence.get(0), nativeAdContainer);
            }
        }
    }


    private void showFacebookNative(final ViewGroup nativeAdContainer) {
        if (facebook_n.isEmpty() || facebook_AdStatus == 0) {
            nextNativePlatform(nativeAdContainer);
            return;
        }

        final NativeAd nativeAd = new NativeAd(activity, facebook_n);

        nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                nextNativePlatform(nativeAdContainer);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                new Inflate_ADS(activity).inflate_NATIV_FB(nativeAd, nativeAdContainer);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        }).build());
    }

    private void showAdmobNative(final ViewGroup nativeAdContainer) {
        if (admob_n.isEmpty() || admob_AdStatus == 0) {
            nextNativePlatform(nativeAdContainer);
            return;
        }

        AdLoader.Builder builder = new AdLoader.Builder(activity, admob_n);

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                new Inflate_ADS(activity).inflate_NATIV_ADMOB(unifiedNativeAd, nativeAdContainer);
            }

        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Request an ad
                nextNativePlatform(nativeAdContainer);
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }


    public interface MyCallback {
        void callbackCall();
    }


}
