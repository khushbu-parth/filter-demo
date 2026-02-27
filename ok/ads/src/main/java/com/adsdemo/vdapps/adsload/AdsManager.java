package com.adsdemo.vdapps.adsload;

import static com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.adsdemo.vdapps.R;
import com.adsdemo.vdapps.adsload.activity.Ad_CustomAppopenActivity;
import com.adsdemo.vdapps.adsload.activity.Ad_CustomInterstitialActivity;
import com.adsdemo.vdapps.adsload.interfaces.MyCallback;
import com.adsdemo.vdapps.adsload.models.CustomAdModel;
import com.adsdemo.vdapps.adsload.models.MoreAppModel;
import com.facebook.ads.Ad;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
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
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AdsManager {

    //Ads stutas
    public static int customAds = 0;
    public static int BannerClick = 0;
    public static int checkBannerClick = 0;
    public static int NativeAdStatus = 0;
    public static int NativeClick = 0;
    public static int checkNativeClick = 0;
    public static int NativeTimer = 0;
    public static int AllAsdStutas = 0;
    public static int AdsStutas = 0;
    public static int googleAdsStutas = 0;
    public static int facebookAdsStuts = 0;
    public static int Launch = 0;
    public static int InterstitialStutas = 0;
    public static int showAdsClick = 0;
    public static int AsdTimerStutas = 0;
    public static int NativeStutas = 0;
    public static int BannerStutas = 0;
    public static int BackPressAdStatus = 0;
    public static int showBackAdsClick = 0;
    public static int SpaceBoxStutas = 0;
    public static int app_dialogBeforeAdShow = 0;
    public static int app_updateAppDialogStatus = 0;
    public static int app_needInternet = 0;
    public static int app_redirectOtherAppStatus = 0;
    public static int app_versionCode = 0;
    public static int chackAdsClick = 0;
    public static int chackBackAdsClick = 0;

    //Extra activity
    public static int PrivacyPolicyScreen = 0;
    public static int PermissionScreen = 0;
    public static int CountryScreen = 0;
    public static int LanguageScreen = 0;
    public static int SwipeScreen = 0;
    public static int AppPurchaseScreen = 0;
    public static int ExitScreen = 0;
    public static int RateDialog = 0;
    public static int backTimer = 0;

    //
    public static boolean status1 = false;
    public static boolean getAllOnlineData = false;
    public static boolean chackInterBool = false;
    //    public static boolean chackInterBoolBack = false;
    public static boolean chackBannerBool = false;
    public static boolean chackNativeBool = false;
    public static boolean chakGoogleNativeId = false;

    //Ads Ids
    public static String AppID = "";

    public static int MoreAds = 0;
    public static String app_newPackageName = "";
    public static String googleBanner = "";
    public static String googleInterstitial = "";
    public static String googleInterstitial2 = "";
    public static String googleNative = "";
    public static String googleNative2 = "";
    public static String googleAppOpen = "";
    public static String googleAppOpen2 = "";
    public static String fbBanner = "";
    public static String fbInterstitial = "";
    public static String fbNative = "";
    public static String fbNativeBanner = "";
    public static String customBanner = "";
    public static String customInterstitial = "";
    public static String customNative = "";
    public static String customAppOpen = "";
    public static String app_backPressAdType = "";

    //native Bg
    public static String NativeBg = "";
    public static String NativeTitle = "";
    public static String NativeDesc = "";
    public static String NativeButton = "";
    public static String NativeButtonText = "";

    //customads
    private static int totalAdInc = 0;

    //more apps and custom ads list
    public static ArrayList<MoreAppModel> moreAppsList;
    public static ArrayList<MoreAppModel> moreGamesList;
    public static ArrayList<MoreAppModel> moreAllList;

    public static int NATIVE_SMALL = 3;
    public static int NATIVE_MIDEUM = 2;
    public static int NATIVE_BIG = 1;

    //stativ var for preload ads
    public static NativeAd mNativeAdG = null;
    public static com.facebook.ads.NativeAd mNativeAd = null;
    public static AdManagerInterstitialAd googleInterAds = null;
    public static InterstitialAd fbInterAds = null;
    //app_id for payment gateway
    public static String app_id = "";
    public static boolean isSplash = false;

    //Exta var
    public static Dialog dialog2;
    public static AdsTimer adsTimer;
    public static AdsTimer adsTimer2;
    public static AdsTimer adsTimerNative;

    public static boolean firstGoogleInter = true;
    public static boolean firstFbInter = true;
    public static boolean firstGoogleNative = false;
    public static boolean firstFbNative = false;

    public static SharedPreferences pref;
    public static int finalAppCouner;
    public static int totalAppCouner;
    public static ArrayList<String> Headercountlist;
    public static List<CustomAdModel> myAppMarketingList = new ArrayList<>();

    //color
    public static String nativebutton = "000000";
    public static String nativebg = "00ffffff";
    public static String nativespaceboxColor = "00ffffff";
    public static String nativetitle = "000000";
    public static String nativedescription = "000000";

    //ads
    public static AppOpenAd appOpenAd1 = null;
    public static AppOpenAd.AppOpenAdLoadCallback loadCallback;
    public static MyCallback myCallback = null;

    public static Activity activity;
    public static AdsManager mInstance;
    public static int mtype;


    public static AdsManager getInstance(Activity activity) {
        AdsManager.activity = activity;
        if (mInstance == null) {
            mInstance = new AdsManager(activity);
        }
        return mInstance;
    }

    public AdsManager(Activity activity) {
        AdsManager.activity = activity;
    }

    //    Extra
    public static boolean isInternetAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getKeyHash(Activity activity) {
        PackageInfo info;
        try {
            info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = (Base64.encodeToString(md.digest(), Base64.NO_WRAP));
                return something.replace("+", "*");
            }
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();

        } catch (NoSuchAlgorithmException e) {

        } catch (Exception e) {

        }
        return null;
    }

    public static String decryptRes(Activity activity, String data) {
        if (data.contains("APP_SETTINGS")) {
            return data;
        } else {
            try {
                String key = AdsManager.getKeyHash(activity) + activity.getPackageName();
                key = key.substring(0, 16);
                String CIPHER_NAME = "AES/CBC/PKCS5PADDING";
                int CIPHER_KEY_LEN = 16; //128 bits
                if (key.length() < CIPHER_KEY_LEN) {
                    int numPad = CIPHER_KEY_LEN - key.length();

                    for (int i = 0; i < numPad; i++) {
                        key += "0";
                    }

                } else if (key.length() > CIPHER_KEY_LEN) {
                    key = key.substring(0, CIPHER_KEY_LEN);
                }

                String[] parts = data.split(":");

                IvParameterSpec iv = new IvParameterSpec(Base64.decode(parts[1], Base64.DEFAULT));
                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("ISO-8859-1"), "AES");

                Cipher cipher = Cipher.getInstance(CIPHER_NAME);
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

                byte[] decodedEncryptedData = Base64.decode(parts[0], Base64.DEFAULT);

                byte[] original = cipher.doFinal(decodedEncryptedData);

                return new String(original);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static void show_gift_header(final Activity activity, FrameLayout iv_adtop, TextView tv_counttop, int visibility) {
        if (AdsManager.moreAllList != null && AdsManager.moreAllList.size() > 0) {
            iv_adtop.setVisibility(View.VISIBLE);
            pref = activity.getSharedPreferences("counter", Context.MODE_PRIVATE);
            finalAppCouner = pref.getInt("lang_us", 0);
            if (finalAppCouner > 0) {
                finalAppCouner--;
                tv_counttop.setText(String.valueOf(finalAppCouner));
            } else {
                tv_counttop.setText(String.valueOf(totalAppCouner - 1));
            }
        } else {
            iv_adtop.setVisibility(visibility);
        }
    }


    //AppOpen
    public static void displaySplashAd(Activity activity, MyCallback myCallbackmain) {
        myCallback = myCallbackmain;
        Log.e("TAG", "displaySplashAd: " + AdsManager.fbBanner);
        if (AdsManager.Launch == 1) {
            if (AdsManager.googleAdsStutas == 1) {
                callAppOpen(activity, AdsManager.googleAppOpen);
            } else if (AdsManager.googleAdsStutas == 1) {
                callGoogleInter(activity, AdsManager.googleInterstitial);
            } else if (AdsManager.facebookAdsStuts == 1) {
                callFbInter(activity, AdsManager.fbInterstitial);
            } else if (AdsManager.customAds == 1 || AdsManager.customAds == 2) {
                CustomAdModel customAdModel = getMyCustomAd();
                Ad_CustomAppopenActivity.newIntent(activity, myCallback, customAdModel);
            } else {
                if (dialog2 != null && dialog2.isShowing()) {
                    dialog2.dismiss();
                }
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }

            }
        } else if (AdsManager.Launch == 2) {
            if (AdsManager.googleAdsStutas == 1) {
                callGoogleInter(activity, AdsManager.googleInterstitial);
            } else if (AdsManager.facebookAdsStuts == 1) {
                callFbInter(activity, AdsManager.fbInterstitial);
            } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3) {
                if (dialog2 != null && dialog2.isShowing()) {
                    dialog2.dismiss();
                }
                CustomAdModel customAdModel = getMyCustomAd();
                Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, -1);
            } else {
                if (dialog2 != null && dialog2.isShowing()) {
                    dialog2.dismiss();
                }
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }
        } else if (AdsManager.Launch == 3) {
            if (AdsManager.facebookAdsStuts == 1) {
                callFbInter(activity, AdsManager.fbInterstitial);
            } else if (AdsManager.googleAdsStutas == 1) {
                callGoogleInter(activity, AdsManager.googleInterstitial);
            } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3) {
                if (dialog2 != null && dialog2.isShowing()) {
                    dialog2.dismiss();
                }
                CustomAdModel customAdModel = getMyCustomAd();
                Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, -1);
            } else {
                if (dialog2 != null && dialog2.isShowing()) {
                    dialog2.dismiss();
                }
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }
        } else {
            if (dialog2 != null && dialog2.isShowing()) {
                dialog2.dismiss();
            }
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
        }
    }

    public static boolean isAdAvailable() {
        return appOpenAd1 != null;
    }

    public static void showAdIfAvailable(Activity activity) {
        appOpenAd1.setFullScreenContentCallback(new FullScreenContentCallback() {

            @Override
            public void onAdDismissedFullScreenContent() {
                if (dialog2 != null && dialog2.isShowing()) {
                    dialog2.dismiss();
                }
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {

                if (AdsManager.googleAdsStutas == 1) {
                    callGoogleInter(activity, AdsManager.googleInterstitial);
                } else if (AdsManager.facebookAdsStuts == 1) {
                    callFbInter(activity, AdsManager.fbInterstitial);
                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3) {
                    if (dialog2 != null && dialog2.isShowing()) {
                        dialog2.dismiss();
                    }
                    CustomAdModel customAdModel = getMyCustomAd();
                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, -1);
                } else {
                    if (dialog2 != null && dialog2.isShowing()) {
                        dialog2.dismiss();
                    }
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }
            }

            @Override
            public void onAdShowedFullScreenContent() {
                Log.e("TAG", "onAdShowedFullScreenContent: 78978997899789987");
            }
        });

        if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            appOpenAd1.show(activity);
        }
    }

    private static void callAppOpen(Activity activity, String adsId) {
        if (!isAdAvailable()) {
            loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    if (AdsManager.googleAdsStutas == 1) {
                        callGoogleInter(activity, AdsManager.googleInterstitial);
                    } else if (AdsManager.facebookAdsStuts == 1) {
                        callFbInter(activity, AdsManager.fbInterstitial);
                    } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3) {
                        if (dialog2 != null && dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        CustomAdModel customAdModel = getMyCustomAd();
                        Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, -1);
                    } else {
                        if (dialog2 != null && dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }
                    }
                }

                public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                    super.onAdLoaded(appOpenAd);
                    appOpenAd1 = appOpenAd;
                    showAdIfAvailable(activity);
                }
            };

            AppOpenAd.load(activity, adsId, getAdRequest(), 1, loadCallback);
        }
    }


    //Custom ad
    public void SetCustomAdData(JSONArray array) {
        try {
            if (myAppMarketingList != null) {
                myAppMarketingList.clear();
            }
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                CustomAdModel customAdModel = new CustomAdModel();
                customAdModel.setAd_id(object.getInt("ad_id"));
                customAdModel.setApp_name(object.getString("app_name"));
                customAdModel.setApp_packageName(object.getString("app_packageName"));
                customAdModel.setApp_logo(object.getString("app_logo"));
                customAdModel.setApp_banner(object.getString("app_banner"));
                customAdModel.setApp_shortDecription(object.getString("app_shortDecription"));
                customAdModel.setApp_rating(object.getString("app_rating"));
                customAdModel.setApp_download(object.getString("app_download"));
                customAdModel.setApp_AdFormat(object.getString("app_AdFormat"));
                myAppMarketingList.add(customAdModel);
            }
        } catch (JSONException e) {
            Log.e("kp_log_1111", "onResponse: " + e.toString());
        }
    }

    private static void showMyCustomNative(final Activity activity, final FrameLayout nativeAdContainer, int type) {
        if (myAppMarketingList.size() != 0) {
            final CustomAdModel appModal = getMyCustomAd();
            if (appModal != null) {

                View inflate = getCustomView(activity, nativeAdContainer, type);
                ((TextView) inflate.findViewById(R.id.btn_install)).setBackgroundColor(Color.parseColor("#" + nativebutton));
                ((TextView) inflate.findViewById(R.id.btn_install)).setBackgroundColor(Color.parseColor("#" + AdsManager.NativeButton));
                ((TextView) inflate.findViewById(R.id.btn_install)).setTextColor(Color.parseColor("#" + AdsManager.NativeButtonText));

                ((TextView) inflate.findViewById(R.id.adText)).setBackgroundColor(Color.parseColor("#" + AdsManager.NativeButton));
                ((TextView) inflate.findViewById(R.id.adText)).setTextColor(Color.parseColor("#" + AdsManager.NativeButtonText));
                ((TextView) inflate.findViewById(R.id.tv_appname)).setTextColor(Color.parseColor("#" + nativetitle));
                ((TextView) inflate.findViewById(R.id.tv_desc)).setTextColor(Color.parseColor("#" + nativedescription));
                ((FrameLayout) inflate.findViewById(R.id.adview)).setBackgroundColor(Color.parseColor("#" + nativebg));
                ImageView iv_banner = (ImageView) inflate.findViewById(R.id.iv_banner);
                ImageView iv_logo = (ImageView) inflate.findViewById(R.id.iv_logo);
                TextView tv_appname = (TextView) inflate.findViewById(R.id.tv_appname);
                LinearLayout ll_app_panel = (LinearLayout) inflate.findViewById(R.id.ll_app_panel);
                RatingBar ad_stars = (RatingBar) inflate.findViewById(R.id.ad_stars);
                TextView tv_rating = (TextView) inflate.findViewById(R.id.tv_rating);
                TextView tv_download = (TextView) inflate.findViewById(R.id.tv_download);
                TextView tv_desc = (TextView) inflate.findViewById(R.id.tv_desc);
                TextView btn_install = (TextView) inflate.findViewById(R.id.btn_install);
                Picasso.get().load(appModal.getApp_banner()).into(iv_banner);
                Picasso.get().load(appModal.getApp_logo()).into(iv_logo);

                if (appModal.getAppPackageName().contains("http")) {
                    tv_appname.setText("Play & AWin Coins Daily.");
                    btn_install.setText("Play Now");
                } else {
                    tv_appname.setText(appModal.getAppName().split("/")[0].trim());
                    btn_install.setText("Install");
                }
                ad_stars.setRating(Float.parseFloat(appModal.getApp_rating()));
                tv_rating.setText("(" + appModal.getApp_rating() + ")");
                tv_download.setText(appModal.getApp_download() + " +");
                tv_desc.setText(appModal.getApp_shortDecription().trim());

                btn_install.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        String action_str = appModal.getAppPackageName();
                        if (action_str.contains("http")) {
                            openChromeCustomTabUrl(activity, action_str);
                        } else {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getPlayStoreUrl(action_str))));
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

    public static CustomAdModel getMyCustomAd() {
        CustomAdModel customAdModel = null;
        if (totalAdInc == myAppMarketingList.size()) {
            totalAdInc = 0;
        }
        customAdModel = myAppMarketingList.get(totalAdInc);
        totalAdInc++;
        return customAdModel;
    }

    private static View getCustomView(Activity activity, FrameLayout nativeAdContainer, int type) {
        View adView;
        switch (type) {

            case 1:
                adView = activity.getLayoutInflater().inflate(R.layout.ad_cust_big_native, null);
                setViewHeight(activity, nativeAdContainer, type);
                break;
            case 2:
                adView = activity.getLayoutInflater().inflate(R.layout.ad_cust_med_native, null);
                break;
            case 3:
                adView = activity.getLayoutInflater().inflate(R.layout.ad_cust_small_native, null);
                break;

            default:
                adView = activity.getLayoutInflater().inflate(R.layout.ad_cust_big_native, null);
                setViewHeight(activity, nativeAdContainer, type);
                break;

        }
        return adView;
    }

    private static void setViewHeight(Activity activity, FrameLayout view, int type) {
        switch (type) {
            case 1:
                view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (activity.getResources().getDisplayMetrics().heightPixels * 40) / 100));
                break;
        }
    }

    public static void openChromeCustomTabUrl(final Context context, String webUrl) {
        try {
            if (isAppInstalled(context, "com.android.chrome")) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                int coolorInt = Color.parseColor("#66bb6a");
                builder.setToolbarColor(coolorInt);
                builder.setStartAnimations(context, R.anim.ad_slide_in_right, R.anim.ad_slide_out_left);
                builder.setExitAnimations(context, R.anim.ad_slide_in_left, R.anim.ad_slide_out_right);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.intent.setPackage("com.android.chrome");
                customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                customTabsIntent.launchUrl(context, Uri.parse(webUrl));
            } else {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                int coolorInt = Color.parseColor("#66bb6a");
                builder.setToolbarColor(coolorInt);
                builder.setStartAnimations(context, R.anim.ad_slide_in_right, R.anim.ad_slide_out_left);
                builder.setExitAnimations(context, R.anim.ad_slide_in_left, R.anim.ad_slide_out_right);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                customTabsIntent.launchUrl(context, Uri.parse(webUrl));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getPlayStoreUrl(String app_packageName) {
        return "https://play.google.com/store/apps/details?id=" + app_packageName;
    }


    //Google ADS
    private static void callGoogleInter(Activity activity, String adsId) {
        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        AdManagerInterstitialAd.load(activity, adsId, adRequest, new AdManagerInterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                interstitialAd.show(activity);
                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        if (dialog2 != null && dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        Log.e("TAG", "FFFFFFFF");
                        if (AdsManager.facebookAdsStuts == 1) {
                            callFbInter(activity, AdsManager.fbInterstitial);
                        } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3) {
                            if (dialog2 != null && dialog2.isShowing()) {
                                dialog2.dismiss();
                            }
                            CustomAdModel customAdModel = getMyCustomAd();
                            Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, -1);
                        } else {
                            if (dialog2 != null && dialog2.isShowing()) {
                                dialog2.dismiss();
                            }
                            if (myCallback != null) {
                                myCallback.callbackCall();
                                myCallback = null;
                            }
                        }


                    }

                    @Override
                    public void onAdShowedFullScreenContent() {

                    }

                    @Override
                    public void onAdImpression() {
                        Log.e("TAG", "Ad recorded an impression.");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.i("TAG", loadAdError.getMessage());
                if (dialog2 != null && dialog2.isShowing()) {
                    dialog2.dismiss();
                }
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
                String error = String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                Log.e("TAG", "onAdFailedToLoad 00: " + error);
            }
        });
    }

    public static void GooglePreloadInter(Activity activity, String adid, boolean isshow) {

        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        AdManagerInterstitialAd.load(activity, adid, adRequest, new AdManagerInterstitialAdLoadCallback() {

            @Override
            public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                if (isshow) {
                    interstitialAd.show(activity);
                }
                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {

                        if (dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        chackInterBool = true;
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }
                        if (mtype == 0) {
                            if (adsTimer != null) {
                                adsTimer.setTimeVar("mytime", (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                            }
                        } else {
                            if (adsTimer2 != null) {
                                adsTimer2.setTimeVar("backmytime", (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                            }
                        }

                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        if (dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        chackInterBool = false;
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        googleInterAds = null;
                        firstGoogleInter = false;
                        GooglePreloadInter(activity, adid, false);
                    }

                    @Override
                    public void onAdImpression() {
                    }
                });
                googleInterAds = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                googleInterAds = null;
                firstGoogleInter = false;
                GooglePreloadInter(activity, adid, false);
                if (dialog2.isShowing()) {
                    dialog2.dismiss();
                }
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }
        });
    }

    private static AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }


    //Facebook ADS
    private static void callFbInter(Activity activity, String adsId) {
        InterstitialAd interstitialAd = new InterstitialAd(activity, adsId);
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                Log.e("TAG", "onInterstitialDisplayed: ");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                Log.e("TAG", "onInterstitialDismissed: ");
                if (dialog2 != null && dialog2.isShowing()) {
                    dialog2.dismiss();
                }
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                Log.e("TAG", "onError: " + adError.getErrorCode() + " MSG " + adError.getErrorMessage());
                if (AdsManager.customAds == 1 || AdsManager.customAds == 3) {
                    if (dialog2 != null && dialog2.isShowing()) {
                        dialog2.dismiss();
                    }
                    CustomAdModel customAdModel = getMyCustomAd();
                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, -1);
                } else {
                    if (dialog2 != null && dialog2.isShowing()) {
                        dialog2.dismiss();
                    }
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                interstitialAd.show();
                Log.e("TAG", "onAdLoaded: ");
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.e("TAG", "onAdClicked: ");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.e("TAG", "onLoggingImpression: ");
            }
        };
        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
    }

    public static void FbPreloadInter(Activity activity, String adid, boolean isshow) {

        InterstitialAd interstitialAd = new InterstitialAd(activity, adid);
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                AdsManager.firstFbInter = false;
                fbInterAds = null;
                FbPreloadInter(activity, adid, false);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

                if (dialog2.isShowing()) {
                    dialog2.dismiss();
                }
                chackInterBool = false;
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
                if (mtype == 0) {
                    if (adsTimer != null) {
                        adsTimer.setTimeVar("mytime", (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                    }
                } else {
                    if (adsTimer2 != null) {
                        adsTimer2.setTimeVar("backmytime", (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                    }
                }
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                AdsManager.firstFbInter = false;
                if (firstGoogleInter) {
                    if (googleInterAds != null) {
                        chackInterBool = true;
                        googleInterAds.show(activity);
                    }
                } else {
                    if (googleInterAds != null) {
                        chackInterBool = true;
                        googleInterAds.show(activity);
                    } else {
                        if (dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }
                    }
                }
                Log.e("TAG", "onError: fbPreLoad " + adError.getErrorCode() + " MSG : fbPreLoad " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                fbInterAds = interstitialAd;
                if (isshow) {
                    fbInterAds.show();
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.e("TAG", "onAdClicked: fbPreLoad ");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.e("TAG", "onLoggingImpression: fbPreLoad ");
            }
        };
        interstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build();
        interstitialAd.loadAd();
    }


    //Interstitial Ad
    public static void CallInterstitialAdLoad(Activity activity, int type, MyCallback myCallbackmain) {
        myCallback = myCallbackmain;
        mtype = type;
        if (AdsManager.AllAsdStutas == 1) {

            if (type == 0) {
                adsTimer = new AdsTimer(activity);

                if (app_needInternet == 1) {
                    if (AdsManager.isInternetAvailable(activity)) {
                        if (AdsManager.InterstitialStutas == 1) {
                            dialog2 = new Dialog(activity);
                            dialog2.requestWindowFeature(1);
                            dialog2.setCancelable(false);
                            dialog2.getWindow().setGravity(Gravity.CENTER);
                            dialog2.setContentView(R.layout.ad_load_dialog);
                            ((TextView) dialog2.findViewById(R.id.text_description)).setText("Please Wait...");
                            if (app_dialogBeforeAdShow == 1) {
                                if (!dialog2.isShowing()) {
                                    dialog2.show();
                                }
                            }

                            if (AdsStutas == 0) {
                                if (chackAdsClick == showAdsClick) {
                                    chackAdsClick = 0;

                                    if (!chackInterBool) {
                                        if (googleAdsStutas == 1) {
                                            if (!AdsManager.googleInterstitial.equals("")) {
                                                DisplayInterstitialAd(activity, "Google", 0);
                                            } else {
                                                if (facebookAdsStuts == 1) {
                                                    if (!AdsManager.fbInterstitial.equals("")) {
                                                        DisplayInterstitialAd(activity, "Fb", 0);
                                                    } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                        if (dialog2 != null && dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        CustomAdModel customAdModel = getMyCustomAd();
                                                        Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                    } else {
                                                        if (dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        if (myCallback != null) {
                                                            myCallback.callbackCall();
                                                            myCallback = null;
                                                        }
                                                    }
                                                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                    if (dialog2 != null && dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    CustomAdModel customAdModel = getMyCustomAd();
                                                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                } else {
                                                    if (dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    if (myCallback != null) {
                                                        myCallback.callbackCall();
                                                        myCallback = null;
                                                    }
                                                }
                                            }
                                        } else if (facebookAdsStuts == 1) {
                                            if (!AdsManager.fbInterstitial.equals("")) {
                                                DisplayInterstitialAd(activity, "Fb", 0);
                                            } else {
                                                if (googleAdsStutas == 1) {
                                                    if (!AdsManager.googleInterstitial.equals("")) {
                                                        DisplayInterstitialAd(activity, "Google", 0);
                                                    } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                        if (dialog2 != null && dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        CustomAdModel customAdModel = getMyCustomAd();
                                                        Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                    } else {
                                                        if (dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        if (myCallback != null) {
                                                            myCallback.callbackCall();
                                                            myCallback = null;
                                                        }
                                                    }
                                                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                    if (dialog2 != null && dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    CustomAdModel customAdModel = getMyCustomAd();
                                                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                } else {
                                                    if (dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    if (myCallback != null) {
                                                        myCallback.callbackCall();
                                                        myCallback = null;
                                                    }
                                                }
                                            }
                                        } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                            if (dialog2 != null && dialog2.isShowing()) {
                                                dialog2.dismiss();
                                            }
                                            CustomAdModel customAdModel = getMyCustomAd();
                                            Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                        } else {
                                            if (dialog2.isShowing()) {
                                                dialog2.dismiss();
                                            }
                                            if (myCallback != null) {
                                                myCallback.callbackCall();
                                                myCallback = null;
                                            }
                                        }
                                    } else {
                                        if (facebookAdsStuts == 1) {
                                            if (!AdsManager.fbInterstitial.equals("")) {
                                                DisplayInterstitialAd(activity, "Fb", 0);
                                            } else {
                                                if (googleAdsStutas == 1) {
                                                    if (!AdsManager.googleInterstitial.equals("")) {
                                                        DisplayInterstitialAd(activity, "Google", 0);
                                                    } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                        if (dialog2 != null && dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        CustomAdModel customAdModel = getMyCustomAd();
                                                        Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                    } else {
                                                        if (dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        if (myCallback != null) {
                                                            myCallback.callbackCall();
                                                            myCallback = null;
                                                        }
                                                    }
                                                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                    if (dialog2 != null && dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    CustomAdModel customAdModel = getMyCustomAd();
                                                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                } else {
                                                    if (dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    if (myCallback != null) {
                                                        myCallback.callbackCall();
                                                        myCallback = null;
                                                    }
                                                }
                                            }
                                        } else if (googleAdsStutas == 1) {
                                            if (!AdsManager.googleInterstitial.equals("")) {
                                                DisplayInterstitialAd(activity, "Google", 0);
                                            } else {
                                                if (facebookAdsStuts == 1) {
                                                    if (!AdsManager.fbInterstitial.equals("")) {
                                                        DisplayInterstitialAd(activity, "Fb", 0);
                                                    } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                        if (dialog2 != null && dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        CustomAdModel customAdModel = getMyCustomAd();
                                                        Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                    } else {
                                                        if (dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        if (myCallback != null) {
                                                            myCallback.callbackCall();
                                                            myCallback = null;
                                                        }
                                                    }
                                                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                    if (dialog2 != null && dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    CustomAdModel customAdModel = getMyCustomAd();
                                                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                } else {
                                                    if (dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    if (myCallback != null) {
                                                        myCallback.callbackCall();
                                                        myCallback = null;
                                                    }
                                                }
                                            }
                                        } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                            if (dialog2 != null && dialog2.isShowing()) {
                                                dialog2.dismiss();
                                            }
                                            CustomAdModel customAdModel = getMyCustomAd();
                                            Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                        } else {
                                            if (dialog2.isShowing()) {
                                                dialog2.dismiss();
                                            }
                                            if (myCallback != null) {
                                                myCallback.callbackCall();
                                                myCallback = null;
                                            }
                                        }
                                    }
                                } else {
                                    chackAdsClick++;
                                    if (dialog2.isShowing()) {
                                        dialog2.dismiss();
                                    }
                                    if (myCallback != null) {
                                        myCallback.callbackCall();
                                        myCallback = null;
                                    }
                                }
                            } else if (AdsStutas == 1) {

                                if ((((int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())) - adsTimer.getTimeVar("mytime")) >= AsdTimerStutas) {
                                    if (!chackInterBool) {
                                        if (googleAdsStutas == 1) {
                                            if (!AdsManager.googleInterstitial.equals("")) {
                                                DisplayInterstitialAd(activity, "Google", 0);
                                            } else {
                                                if (facebookAdsStuts == 1) {
                                                    if (!AdsManager.fbInterstitial.equals("")) {
                                                        DisplayInterstitialAd(activity, "Fb", 0);
                                                    } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                        if (dialog2 != null && dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        CustomAdModel customAdModel = getMyCustomAd();
                                                        Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                    } else {
                                                        if (dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        if (myCallback != null) {
                                                            myCallback.callbackCall();
                                                            myCallback = null;
                                                        }
                                                    }
                                                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                    if (dialog2 != null && dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    CustomAdModel customAdModel = getMyCustomAd();
                                                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                } else {
                                                    if (dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    if (myCallback != null) {
                                                        myCallback.callbackCall();
                                                        myCallback = null;
                                                    }
                                                }
                                            }
                                        } else if (facebookAdsStuts == 1) {
                                            if (!AdsManager.fbInterstitial.equals("")) {
                                                DisplayInterstitialAd(activity, "Fb", 0);
                                            } else {
                                                if (googleAdsStutas == 1) {
                                                    if (!AdsManager.googleInterstitial.equals("")) {
                                                        DisplayInterstitialAd(activity, "Google", 0);
                                                    } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                        if (dialog2 != null && dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        CustomAdModel customAdModel = getMyCustomAd();
                                                        Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                    } else {
                                                        if (dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        if (myCallback != null) {
                                                            myCallback.callbackCall();
                                                            myCallback = null;
                                                        }
                                                    }

                                                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                    if (dialog2 != null && dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    CustomAdModel customAdModel = getMyCustomAd();
                                                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                } else {
                                                    if (dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    if (myCallback != null) {
                                                        myCallback.callbackCall();
                                                        myCallback = null;
                                                    }
                                                }
                                            }
                                        } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                            if (dialog2 != null && dialog2.isShowing()) {
                                                dialog2.dismiss();
                                            }
                                            CustomAdModel customAdModel = getMyCustomAd();
                                            Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                        } else {
                                            if (dialog2.isShowing()) {
                                                dialog2.dismiss();
                                            }
                                            if (myCallback != null) {
                                                myCallback.callbackCall();
                                                myCallback = null;
                                            }
                                        }
                                    } else {
                                        if (facebookAdsStuts == 1) {
                                            if (!AdsManager.fbInterstitial.equals("")) {
                                                DisplayInterstitialAd(activity, "Fb", 0);
                                            } else {
                                                if (googleAdsStutas == 1) {
                                                    if (!AdsManager.googleInterstitial.equals("")) {
                                                        DisplayInterstitialAd(activity, "Google", 0);
                                                    } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                        if (dialog2 != null && dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        CustomAdModel customAdModel = getMyCustomAd();
                                                        Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                    } else {
                                                        if (dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        if (myCallback != null) {
                                                            myCallback.callbackCall();
                                                            myCallback = null;
                                                        }
                                                    }

                                                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                    if (dialog2 != null && dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    CustomAdModel customAdModel = getMyCustomAd();
                                                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                } else {
                                                    if (dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    if (myCallback != null) {
                                                        myCallback.callbackCall();
                                                        myCallback = null;
                                                    }
                                                }
                                            }
                                        } else if (googleAdsStutas == 1) {
                                            if (!AdsManager.googleInterstitial.equals("")) {
                                                DisplayInterstitialAd(activity, "Google", 0);
                                            } else {
                                                if (facebookAdsStuts == 1) {
                                                    if (!AdsManager.fbInterstitial.equals("")) {
                                                        DisplayInterstitialAd(activity, "Fb", 0);
                                                    } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                        if (dialog2 != null && dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        CustomAdModel customAdModel = getMyCustomAd();
                                                        Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                    } else {
                                                        if (dialog2.isShowing()) {
                                                            dialog2.dismiss();
                                                        }
                                                        if (myCallback != null) {
                                                            myCallback.callbackCall();
                                                            myCallback = null;
                                                        }
                                                    }
                                                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                    if (dialog2 != null && dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    CustomAdModel customAdModel = getMyCustomAd();
                                                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                                } else {
                                                    if (dialog2.isShowing()) {
                                                        dialog2.dismiss();
                                                    }
                                                    if (myCallback != null) {
                                                        myCallback.callbackCall();
                                                        myCallback = null;
                                                    }
                                                }
                                            }
                                        } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                            if (dialog2 != null && dialog2.isShowing()) {
                                                dialog2.dismiss();
                                            }
                                            CustomAdModel customAdModel = getMyCustomAd();
                                            Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                        } else {
                                            if (dialog2.isShowing()) {
                                                dialog2.dismiss();
                                            }
                                            if (myCallback != null) {
                                                myCallback.callbackCall();
                                                myCallback = null;
                                            }
                                        }
                                    }
                                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                    if (dialog2 != null && dialog2.isShowing()) {
                                        dialog2.dismiss();
                                    }
                                    CustomAdModel customAdModel = getMyCustomAd();
                                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                } else {
                                    if (dialog2.isShowing()) {
                                        dialog2.dismiss();
                                    }
                                    if (myCallback != null) {
                                        myCallback.callbackCall();
                                        myCallback = null;
                                    }
                                }
                            } else {
                                if (dialog2 != null && dialog2.isShowing()) {
                                    dialog2.dismiss();
                                }
                                if (myCallback != null) {
                                    myCallback.callbackCall();
                                    myCallback = null;
                                }
                            }
                        } else {
                            if (dialog2 != null && dialog2.isShowing()) {
                                dialog2.dismiss();
                            }
                            if (myCallback != null) {
                                myCallback.callbackCall();
                                myCallback = null;
                            }
                        }
                    } else {
                        Toast.makeText(activity, "Please Chack Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (AdsManager.InterstitialStutas == 1) {
                        dialog2 = new Dialog(activity, R.style.MyAlertDialogTheme);
                        dialog2.requestWindowFeature(1);
                        dialog2.setCancelable(false);
                        dialog2.getWindow().setGravity(Gravity.CENTER);
                        dialog2.setContentView(R.layout.ad_load_dialog);
                        ((TextView) dialog2.findViewById(R.id.text_description)).setText("Please Wait...");
                        if (app_dialogBeforeAdShow == 1) {
                            if (!dialog2.isShowing()) {
                                dialog2.show();
                            }
                        }

                        if (chackAdsClick == showAdsClick) {
                            chackAdsClick = 0;

                            if (!chackInterBool) {
                                if (googleAdsStutas == 1) {
                                    if (!AdsManager.googleInterstitial.equals("")) {
                                        DisplayInterstitialAd(activity, "Google", 0);
                                    } else {
                                        if (facebookAdsStuts == 1) {
                                            if (!AdsManager.fbInterstitial.equals("")) {
                                                DisplayInterstitialAd(activity, "Fb", 0);
                                            } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                if (dialog2 != null && dialog2.isShowing()) {
                                                    dialog2.dismiss();
                                                }
                                                CustomAdModel customAdModel = getMyCustomAd();
                                                Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                            } else {
                                                if (dialog2.isShowing()) {
                                                    dialog2.dismiss();
                                                }
                                                if (myCallback != null) {
                                                    myCallback.callbackCall();
                                                    myCallback = null;
                                                }
                                            }
                                        } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                            if (dialog2 != null && dialog2.isShowing()) {
                                                dialog2.dismiss();
                                            }
                                            CustomAdModel customAdModel = getMyCustomAd();
                                            Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                        } else {
                                            if (dialog2.isShowing()) {
                                                dialog2.dismiss();
                                            }
                                            if (myCallback != null) {
                                                myCallback.callbackCall();
                                                myCallback = null;
                                            }
                                        }
                                    }
                                } else if (facebookAdsStuts == 1) {
                                    if (!AdsManager.fbInterstitial.equals("")) {
                                        DisplayInterstitialAd(activity, "Fb", 0);
                                    } else {
                                        if (googleAdsStutas == 1) {
                                            if (!AdsManager.googleInterstitial.equals("")) {
                                                DisplayInterstitialAd(activity, "Google", 0);
                                            } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                                if (dialog2 != null && dialog2.isShowing()) {
                                                    dialog2.dismiss();
                                                }
                                                CustomAdModel customAdModel = getMyCustomAd();
                                                Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                            } else {
                                                if (dialog2.isShowing()) {
                                                    dialog2.dismiss();
                                                }
                                                if (myCallback != null) {
                                                    myCallback.callbackCall();
                                                    myCallback = null;
                                                }
                                            }
                                        } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                            if (dialog2 != null && dialog2.isShowing()) {
                                                dialog2.dismiss();
                                            }
                                            CustomAdModel customAdModel = getMyCustomAd();
                                            Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                        } else {
                                            if (dialog2.isShowing()) {
                                                dialog2.dismiss();
                                            }
                                            if (myCallback != null) {
                                                myCallback.callbackCall();
                                                myCallback = null;
                                            }
                                        }
                                    }
                                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                    if (dialog2 != null && dialog2.isShowing()) {
                                        dialog2.dismiss();
                                    }
                                    CustomAdModel customAdModel = getMyCustomAd();
                                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                } else {
                                    if (dialog2.isShowing()) {
                                        dialog2.dismiss();
                                    }
                                    if (myCallback != null) {
                                        myCallback.callbackCall();
                                        myCallback = null;
                                    }
                                }
                            } else {
                                if (facebookAdsStuts == 1) {
                                    DisplayInterstitialAd(activity, "Fb", 0);
                                } else if (googleAdsStutas == 1) {
                                    DisplayInterstitialAd(activity, "Google", 0);
                                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                                    if (dialog2 != null && dialog2.isShowing()) {
                                        dialog2.dismiss();
                                    }
                                    CustomAdModel customAdModel = getMyCustomAd();
                                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                } else {
                                    if (dialog2.isShowing()) {
                                        dialog2.dismiss();
                                    }
                                    if (myCallback != null) {
                                        myCallback.callbackCall();
                                        myCallback = null;
                                    }
                                }
                            }
                        } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31) {
                            if (dialog2 != null && dialog2.isShowing()) {
                                dialog2.dismiss();
                            }
                            CustomAdModel customAdModel = getMyCustomAd();
                            Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                        } else {
                            chackAdsClick++;
                            if (dialog2.isShowing()) {
                                dialog2.dismiss();
                            }
                            if (myCallback != null) {
                                myCallback.callbackCall();
                                myCallback = null;
                            }
                        }
                    } else {
                        if (dialog2 != null && dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }
                    }
                }
            } else {
                if (AdsManager.BackPressAdStatus == 1) {

                    dialog2 = new Dialog(activity);
                    dialog2.requestWindowFeature(1);
                    dialog2.setCancelable(false);
                    dialog2.getWindow().setGravity(Gravity.CENTER);
                    dialog2.setContentView(R.layout.ad_load_dialog);
                    ((TextView) dialog2.findViewById(R.id.text_description)).setText("Please Wait...");

                    if (app_dialogBeforeAdShow == 1) {
                        if (!dialog2.isShowing()) {
                            dialog2.show();
                        }
                    }
                    adsTimer2 = new AdsTimer(activity);
                    Log.e("TAG", "backTimer == 0  : " + backTimer);
                    if (backTimer == 0) {
                        Log.e("TAG", "CallInterstitialBackAdLoad -------->>> : " + chackInterBool);

                        Log.e("TAG", "backTimer == 0ff : ");
                        if (chackBackAdsClick == showBackAdsClick) {
                            chackBackAdsClick = 0;
                            if (!chackInterBool) {
                                if (googleAdsStutas == 1) {
                                    chackInterBool = true;
                                    DisplayInterstitialAd(activity, "Google", 1);
                                } else if (facebookAdsStuts == 1) {
                                    chackInterBool = false;
                                    DisplayInterstitialAd(activity, "Fb", 1);
                                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 32) {
                                    if (dialog2 != null && dialog2.isShowing()) {
                                        dialog2.dismiss();
                                    }
                                    CustomAdModel customAdModel = getMyCustomAd();
                                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                } else {
                                    if (dialog2.isShowing()) {
                                        dialog2.dismiss();
                                    }
                                    if (myCallback != null) {
                                        myCallback.callbackCall();
                                        myCallback = null;
                                    }
                                }
                            } else {
                                if (facebookAdsStuts == 1) {
                                    chackInterBool = true;
                                    DisplayInterstitialAd(activity, "Fb", 1);
                                } else if (googleAdsStutas == 1) {
                                    chackInterBool = false;
                                    DisplayInterstitialAd(activity, "Google", 1);
                                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 32) {
                                    if (dialog2 != null && dialog2.isShowing()) {
                                        dialog2.dismiss();
                                    }
                                    CustomAdModel customAdModel = getMyCustomAd();
                                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                } else {
                                    if (dialog2.isShowing()) {
                                        dialog2.dismiss();
                                    }
                                    if (myCallback != null) {
                                        myCallback.callbackCall();
                                        myCallback = null;
                                    }
                                }
                            }
                        } else {
                            chackBackAdsClick++;
                            if (dialog2.isShowing()) {
                                dialog2.dismiss();
                            }
                            if (myCallback != null) {
                                myCallback.callbackCall();
                                myCallback = null;
                            }
                        }
                    } else {
                        Log.e("TAG", "backTimer == 0n : 1 :-->> " + (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                        Log.e("TAG", "backTimer == 0n : 2 :-->> " + adsTimer2.getTimeVar("backmytime"));
                        Log.e("TAG", "backTimer == 0n : 3 :-->> " + backTimer);
                        int dif = ((int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())) - (int) (adsTimer2.getTimeVar("backmytime"));
                        Log.e("TAG", "backTimer == 0n : 4 :-->> " + dif);
                        if (dif >= backTimer) {

                            if (!chackInterBool) {
                                if (googleAdsStutas == 1) {
                                    chackInterBool = true;
                                    DisplayInterstitialAd(activity, "Google", 1);
                                } else if (facebookAdsStuts == 1) {
                                    chackInterBool = false;
                                    DisplayInterstitialAd(activity, "Fb", 1);
                                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 32) {
                                    if (dialog2 != null && dialog2.isShowing()) {
                                        dialog2.dismiss();
                                    }
                                    CustomAdModel customAdModel = getMyCustomAd();
                                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                } else {
                                    if (dialog2.isShowing()) {
                                        dialog2.dismiss();
                                    }
                                    if (myCallback != null) {
                                        myCallback.callbackCall();
                                        myCallback = null;
                                    }
                                }
                            } else {
                                if (facebookAdsStuts == 1) {
                                    chackInterBool = true;
                                    DisplayInterstitialAd(activity, "Fb", 1);
                                } else if (googleAdsStutas == 1) {
                                    chackInterBool = false;
                                    DisplayInterstitialAd(activity, "Google", 1);
                                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 32) {
                                    if (dialog2 != null && dialog2.isShowing()) {
                                        dialog2.dismiss();
                                    }
                                    CustomAdModel customAdModel = getMyCustomAd();
                                    Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                                } else {
                                    if (dialog2.isShowing()) {
                                        dialog2.dismiss();
                                    }
                                    if (myCallback != null) {
                                        myCallback.callbackCall();
                                        myCallback = null;
                                    }
                                }
                            }

                        } else {
                            if (dialog2.isShowing()) {
                                dialog2.dismiss();
                            }
                            if (myCallback != null) {
                                myCallback.callbackCall();
                                myCallback = null;
                            }
                        }
                    }
                } else {
                    if (dialog2 != null && dialog2.isShowing()) {
                        dialog2.dismiss();
                    }
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }
            }
        } else {
            if (dialog2 != null && dialog2.isShowing()) {
                dialog2.dismiss();
            }
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
        }
    }

    public static void DisplayInterstitialAd(Activity activity, String adtype, int type) {
        if (adtype.equals("Google")) {
            String adid;
            if (type == 0) {
                adid = AdsManager.googleInterstitial;
            } else {
                adid = AdsManager.googleInterstitial2;
            }

            if (firstGoogleInter) {
                if (googleInterAds != null) {
                    chackInterBool = true;
                    googleInterAds.show(activity);
                }
            } else {
                if (googleInterAds != null) {
                    chackInterBool = true;
                    googleInterAds.show(activity);
                } else if (facebookAdsStuts == 1) {
                    if (fbInterAds != null && fbInterAds.isAdLoaded()) {
                        chackInterBool = false;
                        fbInterAds.show();
                    } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31 || AdsManager.customAds == 32) {
                        if (type == 0 && AdsManager.customAds == 31) {
                            if (dialog2 != null && dialog2.isShowing()) {
                                dialog2.dismiss();
                            }
                            CustomAdModel customAdModel = getMyCustomAd();
                            Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                        } else if (type == 1 && AdsManager.customAds == 32) {
                            if (dialog2 != null && dialog2.isShowing()) {
                                dialog2.dismiss();
                            }
                            CustomAdModel customAdModel = getMyCustomAd();
                            Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                        } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3) {
                            if (dialog2 != null && dialog2.isShowing()) {
                                dialog2.dismiss();
                            }
                            CustomAdModel customAdModel = getMyCustomAd();
                            Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                        }
                    } else {
                        if (dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }
                    }
                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31 || AdsManager.customAds == 32) {
                    if (type == 0 && AdsManager.customAds == 31) {
                        if (dialog2 != null && dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        CustomAdModel customAdModel = getMyCustomAd();
                        Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                    } else if (type == 1 && AdsManager.customAds == 32) {
                        if (dialog2 != null && dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        CustomAdModel customAdModel = getMyCustomAd();
                        Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                    } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3) {
                        if (dialog2 != null && dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        CustomAdModel customAdModel = getMyCustomAd();
                        Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                    }
                } else {
                    if (dialog2.isShowing()) {
                        dialog2.dismiss();
                    }
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }
            }
        } else {
            String adid = AdsManager.fbInterstitial;

            if (firstFbInter) {
                if (fbInterAds != null && fbInterAds.isAdLoaded()) {
                    chackInterBool = false;
                    fbInterAds.show();
                }
            } else {
                if (fbInterAds != null && fbInterAds.isAdLoaded()) {
                    chackInterBool = false;
                    fbInterAds.show();
                } else if (googleAdsStutas == 1) {
                    if (googleInterAds != null) {
                        chackInterBool = true;
                        googleInterAds.show(activity);
                    } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31 || AdsManager.customAds == 32) {
                        if (type == 0 && AdsManager.customAds == 31) {
                            if (dialog2 != null && dialog2.isShowing()) {
                                dialog2.dismiss();
                            }
                            CustomAdModel customAdModel = getMyCustomAd();
                            Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                        } else if (type == 1 && AdsManager.customAds == 32) {
                            if (dialog2 != null && dialog2.isShowing()) {
                                dialog2.dismiss();
                            }
                            CustomAdModel customAdModel = getMyCustomAd();
                            Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                        } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3) {
                            if (dialog2 != null && dialog2.isShowing()) {
                                dialog2.dismiss();
                            }
                            CustomAdModel customAdModel = getMyCustomAd();
                            Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                        }
                    } else {
                        if (dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }
                    }
                } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3 || AdsManager.customAds == 31 || AdsManager.customAds == 32) {
                    if (type == 0 && AdsManager.customAds == 31) {
                        if (dialog2 != null && dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        CustomAdModel customAdModel = getMyCustomAd();
                        Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                    } else if (type == 1 && AdsManager.customAds == 32) {
                        if (dialog2 != null && dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        CustomAdModel customAdModel = getMyCustomAd();
                        Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                    } else if (AdsManager.customAds == 1 || AdsManager.customAds == 3) {
                        if (dialog2 != null && dialog2.isShowing()) {
                            dialog2.dismiss();
                        }
                        CustomAdModel customAdModel = getMyCustomAd();
                        Ad_CustomInterstitialActivity.newIntent(activity, myCallback, customAdModel, adsTimer, adsTimer2, type);
                    }
                } else {
                    if (dialog2.isShowing()) {
                        dialog2.dismiss();
                    }
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }
            }
        }
    }

    //Banner All
    public static void CallBannerAds(Activity activity, FrameLayout adView) {

        if (AdsManager.AllAsdStutas == 1) {

            if (AdsManager.BannerStutas != 0) {

                if (AdsManager.checkBannerClick == AdsManager.BannerClick) {
                    AdsManager.checkBannerClick = 0;
                    if (AdsManager.BannerStutas == 3 || AdsManager.BannerStutas == 1) {
                        getBanner(activity, adView);
                    } else if (AdsManager.BannerStutas == 2) {
                        AdsManager.CallNativeAdLoad(activity, adView, AdsManager.NATIVE_SMALL);
                    } else {
                        if (SpaceBoxStutas == 2) {
                            RelativeLayout adViewbanner = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.ad_spacebox_banner, null);
                            adView.removeAllViews();
                            adView.addView(adViewbanner);
                        } else if (SpaceBoxStutas == 3) {
                            RelativeLayout adViewbanner = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.ad_spacebox_banner, null);
                            adView.removeAllViews();
                            adView.addView(adViewbanner);
                        }
                    }

                } else {
                    AdsManager.checkBannerClick++;
                    if (AdsManager.BannerStutas == 3) {
                        AdsManager.CallNativeAdLoad(activity, adView, AdsManager.NATIVE_SMALL);
                    }
                }
            } else {
                if (SpaceBoxStutas == 2) {
                    RelativeLayout adViewbanner = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.ad_spacebox_banner, null);
                    adView.removeAllViews();
                    adView.addView(adViewbanner);
                } else if (SpaceBoxStutas == 3) {
                    RelativeLayout adViewbanner = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.ad_spacebox_banner, null);
                    adView.removeAllViews();
                    adView.addView(adViewbanner);
                }
            }

        }
    }

    private static void getBanner(Activity activity, FrameLayout adView) {
        if (!chackBannerBool) {
            if (AdsManager.BannerStutas != 0) {
                if (googleAdsStutas == 1) {
                    GoogleBanner(activity, adView);
                } else if (facebookAdsStuts == 1) {
                    FacebookBanner(activity, adView);
                } else {
                    AdsManager.CallNativeAdLoad(activity, adView, AdsManager.NATIVE_SMALL);
                }
            } else {
                if (SpaceBoxStutas == 2) {
                    RelativeLayout adViewbanner = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.ad_spacebox_banner, null);
                    adView.removeAllViews();
                    adView.addView(adViewbanner);
                } else if (SpaceBoxStutas == 3) {
                    RelativeLayout adViewbanner = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.ad_spacebox_banner, null);
                    adView.removeAllViews();
                    adView.addView(adViewbanner);
                }
            }
        } else {
            if (AdsManager.BannerStutas != 0) {
                if (facebookAdsStuts == 1) {
                    FacebookBanner(activity, adView);
                } else if (googleAdsStutas == 1) {
                    GoogleBanner(activity, adView);
                } else {
                    AdsManager.CallNativeAdLoad(activity, adView, AdsManager.NATIVE_SMALL);
                }
            } else {
                if (SpaceBoxStutas == 2) {
                    RelativeLayout adViewbanner = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.ad_spacebox_banner, null);
                    adView.removeAllViews();
                    adView.addView(adViewbanner);
                } else if (SpaceBoxStutas == 3) {
                    RelativeLayout adViewbanner = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.ad_spacebox_banner, null);
                    adView.removeAllViews();
                    adView.addView(adViewbanner);
                }
            }
        }
    }

    public static void GoogleBanner(Activity activity, FrameLayout adView) {

        AdRequest adRequest = new AdRequest.Builder().build();
        AdView mAdView = new AdView(activity);

        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(googleBanner);

        adView.addView(mAdView);
        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.e("TAG", "onAdFailedToLoad 111: ");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                chackBannerBool = true;
                Log.e("TAG", "onAdLoaded: B_G");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.e("TAG", "onAdFailedToLoad 222: ");
                chackBannerBool = false;
                if (facebookAdsStuts == 1) {
                    if (!AdsManager.fbBanner.equals("")) {
                        FacebookBannerFail(activity, adView);
                    }
                }
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);
    }

    public static void FacebookBanner(Activity activity, FrameLayout adView) {

        com.facebook.ads.AdView fbView = new com.facebook.ads.AdView(activity, fbBanner, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        adView.addView(fbView);

        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                Log.e("TAG", "onError: " + adError.getErrorMessage() + " code" + adError.getErrorCode());
                chackBannerBool = true;
                if (googleAdsStutas == 1) {
                    if (!AdsManager.googleBanner.equals("")) {
                        GoogleBannerFail(activity, adView);
                    }
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                chackBannerBool = false;
                Log.e("TAG", "onAdLoaded: ");
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.e("TAG", "onAdClicked: ");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.e("TAG", "onLoggingImpression: ");
            }
        };
        fbView.loadAd(fbView.buildLoadAdConfig().withAdListener(adListener).build());
    }

    public static void GoogleBannerFail(Activity activity, FrameLayout adView) {

        AdRequest adRequest = new AdRequest.Builder().build();
        AdView mAdView = new AdView(activity);

        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(googleBanner);

        adView.addView(mAdView);
        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.e("TAG", "onAdFailedToLoad 111: ");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                chackBannerBool = true;
                Log.e("TAG", "onAdLoaded: B_G");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.e("TAG", "onAdFailedToLoad 222: ");
                chackBannerBool = false;
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);
    }

    public static void FacebookBannerFail(Activity activity, FrameLayout adView) {

        com.facebook.ads.AdView fbView = new com.facebook.ads.AdView(activity, fbBanner, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        adView.addView(fbView);

        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                Log.e("TAG", "onError: " + adError.getErrorMessage() + " code" + adError.getErrorCode());
                chackBannerBool = true;
                AdsManager.CallNativeAdLoad(activity, adView, AdsManager.NATIVE_SMALL);

            }

            @Override
            public void onAdLoaded(Ad ad) {
                chackBannerBool = false;
                Log.e("TAG", "onAdLoaded: ");
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.e("TAG", "onAdClicked: ");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.e("TAG", "onLoggingImpression: ");
            }
        };
        fbView.loadAd(fbView.buildLoadAdConfig().withAdListener(adListener).build());
    }

    //Native All
    public static void CallNativeAdLoad(Activity activity, FrameLayout frameLayout, int type) {
        if (AdsManager.AllAsdStutas == 1) {
            adsTimerNative = new AdsTimer(activity);
            if (NativeAdStatus == 0) {
                if (NativeClick == 0) {
                    shownative(activity, frameLayout, type);
                    return;
                }
                if (checkNativeClick == (NativeClick)) {
                    checkNativeClick = 0;
                    shownative(activity, frameLayout, type);
                } else {
                    checkNativeClick++;
                }
            } else {
                if (((int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())) - adsTimerNative.getTimeNativeVar("mytimenative") >= NativeTimer) {
                    shownative(activity, frameLayout, type);
                }
            }
        }
    }

    private static void shownative(Activity activity, FrameLayout frameLayout, int type) {
        Log.e("TAG", "CallNativeAdLoad ---------------->>>> : " + NativeStutas);
        if (!chackNativeBool) {
            chackNativeBool = true;
            if (AdsManager.NativeStutas == 1 || AdsManager.NativeStutas == 2 || AdsManager.NativeStutas == 3 || AdsManager.NativeStutas == 4 || AdsManager.NativeStutas == 23 || AdsManager.NativeStutas == 32 || AdsManager.NativeStutas == 34 || AdsManager.NativeStutas == 43 || AdsManager.NativeStutas == 24 || AdsManager.NativeStutas == 42) {
                if (googleAdsStutas == 1) {
                    DisplayGoogleNative(activity, frameLayout, type);
                } else if (facebookAdsStuts == 1) {
                    DisplayFbNative(activity, frameLayout, type);
                } else if (customAds == 1 || customAds == 4 || customAds == 41 || customAds == 42 || customAds == 43) {
                    if (customAds == 1 || customAds == 4) {
                        custonNativeAd(activity, frameLayout, type);
                    } else if (type == NATIVE_BIG && customAds == 41) {
                        custonNativeAd(activity, frameLayout, type);
                    } else if (type == NATIVE_MIDEUM && customAds == 42) {
                        custonNativeAd(activity, frameLayout, type);
                    } else if (type == NATIVE_SMALL && customAds == 43) {
                        custonNativeAd(activity, frameLayout, type);
                    }
                } else {
                    if (SpaceBoxStutas == 1 || SpaceBoxStutas == 3) {
                        RelativeLayout adView = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.ad_spacebox_native, null);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                        LinearLayout llSpaceBig = adView.findViewById(R.id.llBig);
                        LinearLayout llSpaceMid = adView.findViewById(R.id.llMid);
                        LinearLayout llSpaceSml = adView.findViewById(R.id.llSml);

                        if (type == 1) {
                            llSpaceMid.setVisibility(View.GONE);
                            llSpaceSml.setVisibility(View.GONE);
                            llSpaceBig.setVisibility(View.VISIBLE);
                        } else if (type == 2) {
                            llSpaceBig.setVisibility(View.GONE);
                            llSpaceSml.setVisibility(View.GONE);
                            llSpaceMid.setVisibility(View.VISIBLE);
                        } else if (type == 3) {
                            llSpaceBig.setVisibility(View.GONE);
                            llSpaceMid.setVisibility(View.GONE);
                            llSpaceSml.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } else if (customAds == 1 || customAds == 4 || customAds == 41 || customAds == 42 || customAds == 43) {
                if (customAds == 1 || customAds == 4) {
                    custonNativeAd(activity, frameLayout, type);
                } else if (type == NATIVE_BIG && customAds == 41) {
                    custonNativeAd(activity, frameLayout, type);
                } else if (type == NATIVE_MIDEUM && customAds == 42) {
                    custonNativeAd(activity, frameLayout, type);
                } else if (type == NATIVE_SMALL && customAds == 43) {
                    custonNativeAd(activity, frameLayout, type);
                }
            } else {
                if (SpaceBoxStutas == 1 || SpaceBoxStutas == 3) {
                    RelativeLayout adView = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.ad_spacebox_native, null);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                    LinearLayout llSpaceBig = adView.findViewById(R.id.llBig);
                    LinearLayout llSpaceMid = adView.findViewById(R.id.llMid);
                    LinearLayout llSpaceSml = adView.findViewById(R.id.llSml);

                    if (type == 1) {
                        llSpaceMid.setVisibility(View.GONE);
                        llSpaceSml.setVisibility(View.GONE);
                        llSpaceBig.setVisibility(View.VISIBLE);
                    } else if (type == 2) {
                        llSpaceBig.setVisibility(View.GONE);
                        llSpaceSml.setVisibility(View.GONE);
                        llSpaceMid.setVisibility(View.VISIBLE);
                    } else if (type == 3) {
                        llSpaceBig.setVisibility(View.GONE);
                        llSpaceMid.setVisibility(View.GONE);
                        llSpaceSml.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            chackNativeBool = false;
            if (AdsManager.NativeStutas == 1 || AdsManager.NativeStutas == 2 || AdsManager.NativeStutas == 3 || AdsManager.NativeStutas == 4 || AdsManager.NativeStutas == 23 || AdsManager.NativeStutas == 32 || AdsManager.NativeStutas == 34 || AdsManager.NativeStutas == 43 || AdsManager.NativeStutas == 24 || AdsManager.NativeStutas == 42) {
                if (facebookAdsStuts == 1) {
                    DisplayFbNative(activity, frameLayout, type);
                } else if (googleAdsStutas == 1) {
                    DisplayGoogleNative(activity, frameLayout, type);
                } else if (customAds == 1 || customAds == 4 || customAds == 41 || customAds == 42 || customAds == 43) {
                    if (customAds == 1 || customAds == 4) {
                        custonNativeAd(activity, frameLayout, type);
                    } else if (type == NATIVE_BIG && customAds == 41) {
                        custonNativeAd(activity, frameLayout, type);
                    } else if (type == NATIVE_MIDEUM && customAds == 42) {
                        custonNativeAd(activity, frameLayout, type);
                    } else if (type == NATIVE_SMALL && customAds == 43) {
                        custonNativeAd(activity, frameLayout, type);
                    }
                } else {
                    if (SpaceBoxStutas == 1 || SpaceBoxStutas == 3) {
                        RelativeLayout adView = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.ad_spacebox_native, null);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                        LinearLayout llSpaceBig = adView.findViewById(R.id.llBig);
                        LinearLayout llSpaceMid = adView.findViewById(R.id.llMid);
                        LinearLayout llSpaceSml = adView.findViewById(R.id.llSml);

                        if (type == 1) {
                            llSpaceMid.setVisibility(View.GONE);
                            llSpaceSml.setVisibility(View.GONE);
                            llSpaceBig.setVisibility(View.VISIBLE);
                        } else if (type == 2) {
                            llSpaceBig.setVisibility(View.GONE);
                            llSpaceSml.setVisibility(View.GONE);
                            llSpaceMid.setVisibility(View.VISIBLE);
                        } else if (type == 3) {
                            llSpaceBig.setVisibility(View.GONE);
                            llSpaceMid.setVisibility(View.GONE);
                            llSpaceSml.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } else if (customAds == 1 || customAds == 4 || customAds == 41 || customAds == 42 || customAds == 43) {
                if (customAds == 1 || customAds == 4) {
                    custonNativeAd(activity, frameLayout, type);
                } else if (type == NATIVE_BIG && customAds == 41) {
                    custonNativeAd(activity, frameLayout, type);
                } else if (type == NATIVE_MIDEUM && customAds == 42) {
                    custonNativeAd(activity, frameLayout, type);
                } else if (type == NATIVE_SMALL && customAds == 43) {
                    custonNativeAd(activity, frameLayout, type);
                }
            } else {
                if (SpaceBoxStutas == 1 || SpaceBoxStutas == 3) {
                    RelativeLayout adView = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.ad_spacebox_native, null);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                    LinearLayout llSpaceBig = adView.findViewById(R.id.llBig);
                    LinearLayout llSpaceMid = adView.findViewById(R.id.llMid);
                    LinearLayout llSpaceSml = adView.findViewById(R.id.llSml);

                    if (type == 1) {
                        llSpaceMid.setVisibility(View.GONE);
                        llSpaceSml.setVisibility(View.GONE);
                        llSpaceBig.setVisibility(View.VISIBLE);
                    } else if (type == 2) {
                        llSpaceBig.setVisibility(View.GONE);
                        llSpaceSml.setVisibility(View.GONE);
                        llSpaceMid.setVisibility(View.VISIBLE);
                    } else if (type == 3) {
                        llSpaceBig.setVisibility(View.GONE);
                        llSpaceMid.setVisibility(View.GONE);
                        llSpaceSml.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private static void custonNativeAd(Activity activity, FrameLayout frameLayout, int type) {
        if (AdsManager.NativeStutas == 1) {
            showMyCustomNative(activity, frameLayout, type);
        }
        if (AdsManager.NativeStutas == 2 || AdsManager.NativeStutas == 23 || AdsManager.NativeStutas == 32 || AdsManager.NativeStutas == 42 || AdsManager.NativeStutas == 24) {
            if (type == AdsManager.NATIVE_BIG) {
                showMyCustomNative(activity, frameLayout, type);
            }
        }
        if (AdsManager.NativeStutas == 3 || AdsManager.NativeStutas == 23 || AdsManager.NativeStutas == 32 || AdsManager.NativeStutas == 43 || AdsManager.NativeStutas == 34) {
            if (type == AdsManager.NATIVE_MIDEUM) {
                showMyCustomNative(activity, frameLayout, type);
            }
        }
        if (AdsManager.NativeStutas == 4 || AdsManager.NativeStutas == 43 || AdsManager.NativeStutas == 34 || AdsManager.NativeStutas == 42 || AdsManager.NativeStutas == 24) {
            if (type == AdsManager.NATIVE_SMALL) {
                showMyCustomNative(activity, frameLayout, type);
            }
        }
    }

    public static void DisplayGoogleNative(Activity activity, FrameLayout frameLayout, int type) {
        if (AdsManager.NativeStutas == 1) {
            if (type == 1 || type == 2 || type == 3) {
                if (!firstGoogleNative) {
                    Log.e("TAG", "displayGoogleNativeAd: Sajid First Native Load n Show");
                    FirstGoogleNative(activity, frameLayout, type);
                } else {
                    Log.e("TAG", "displayGoogleNativeAd: Sajid First Native PreLoad");
                    if (mNativeAdG != null) {
                        PopulatGooglePreLoadNative(frameLayout, mNativeAdG, activity, type);
                    } else {
                        if (!firstFbNative) {
                            FacebookNativeFirst(activity, frameLayout, type);
                        } else {
                            fbNativeBig(activity, frameLayout, type);
                        }
                    }
                }

            } else {

                if (!firstGoogleNative) {
                    Log.e("TAG", "displayGoogleNativeAd: Sajid First Native Load n Show");
                    FirstGoogleNative(activity, frameLayout, type);
                } else {
                    if (mNativeAdG != null) {
                        PopulatGooglePreLoadNative(frameLayout, mNativeAdG, activity, type);
                    } else {
                        if (!firstFbNative) {
                            FacebookNativeFirst(activity, frameLayout, type);
                        } else {
                            fbNativeBig(activity, frameLayout, type);
                        }
                    }
                }

            }
        }
        if (AdsManager.NativeStutas == 2 || AdsManager.NativeStutas == 23 || AdsManager.NativeStutas == 32 || AdsManager.NativeStutas == 42 || AdsManager.NativeStutas == 24) {
            if (type == 1) {
                if (!firstGoogleNative) {
                    Log.e("TAG", "displayGoogleNativeAd: Sajid First Native Load n Show");
                    FirstGoogleNative(activity, frameLayout, type);
                } else {
                    if (mNativeAdG != null) {
                        PopulatGooglePreLoadNative(frameLayout, mNativeAdG, activity, type);
                    } else {
                        if (!firstFbNative) {
                            FacebookNativeFirst(activity, frameLayout, type);
                        } else {
                            fbNativeBig(activity, frameLayout, type);
                        }
                    }
                }
            }
        }
        if (AdsManager.NativeStutas == 3 || AdsManager.NativeStutas == 23 || AdsManager.NativeStutas == 32 || AdsManager.NativeStutas == 43 || AdsManager.NativeStutas == 34) {
            if (type == 2) {

                if (!firstGoogleNative) {
                    Log.e("TAG", "displayGoogleNativeAd: Sajid First Native Load n Show");
                    FirstGoogleNative(activity, frameLayout, type);
                } else {
                    if (mNativeAdG != null) {
                        PopulatGooglePreLoadNative(frameLayout, mNativeAdG, activity, type);
                    } else {
                        if (!firstFbNative) {
                            FacebookNativeFirst(activity, frameLayout, type);
                        } else {
                            fbNativeBig(activity, frameLayout, type);
                        }
                    }
                }
            }
        }
        if (AdsManager.NativeStutas == 4 || AdsManager.NativeStutas == 43 || AdsManager.NativeStutas == 34 || AdsManager.NativeStutas == 42 || AdsManager.NativeStutas == 24) {
            if (type == 3) {

                if (!firstGoogleNative) {
                    Log.e("TAG", "displayGoogleNativeAd: Sajid First Native Load n Show");
                    FirstGoogleNative(activity, frameLayout, type);
                } else {
                    if (mNativeAdG != null) {
                        PopulatGooglePreLoadNative(frameLayout, mNativeAdG, activity, type);
                    } else {
                        if (!firstFbNative) {
                            FacebookNativeFirst(activity, frameLayout, type);
                        } else {
                            fbNativeBig(activity, frameLayout, type);
                        }
                    }
                }
            }
        }
    }

    public static void DisplayFbNative(Activity activity, FrameLayout frameLayout, int type) {
        if (AdsManager.NativeStutas == 1) {
            if (type == 1 || type == 2 || type == 3) {

                if (!firstFbNative) {
                    Log.e("TAG", "displayFbNativeAd: FacebookNativeFirst ");
                    FacebookNativeFirst(activity, frameLayout, type);
                } else {
                    if (mNativeAd != null) {
                        Log.e("TAG", "displayFbNativeAd: fbNativeBig Preload ");
                        fbNativeBig(activity, frameLayout, type);
                    } else {
                        if (!firstGoogleNative) {
                            Log.e("TAG", "displayGoogleNativeAd: Sajid First Native Load n Show");
                            FirstGoogleNative(activity, frameLayout, type);
                        } else {
                            PopulatGooglePreLoadNative(frameLayout, mNativeAdG, activity, type);
                        }
                    }
                }

            } else {
                if (!firstFbNative) {
                    Log.e("TAG", "displayFbNativeAd: FacebookNativeFirst ");
                    FacebookNativeFirst(activity, frameLayout, type);
                } else {
                    if (mNativeAd != null) {
                        Log.e("TAG", "displayFbNativeAd: fbNativeBig Preload ");
                        fbNativeBig(activity, frameLayout, type);
                    } else {
                        if (!firstGoogleNative) {
                            Log.e("TAG", "displayGoogleNativeAd: Sajid First Native Load n Show");
                            FirstGoogleNative(activity, frameLayout, type);
                        } else {
                            PopulatGooglePreLoadNative(frameLayout, mNativeAdG, activity, type);
                        }
                    }
                }
            }
        }
        if (AdsManager.NativeStutas == 2 || AdsManager.NativeStutas == 23 || AdsManager.NativeStutas == 32 || AdsManager.NativeStutas == 42 || AdsManager.NativeStutas == 24) {
            if (type == 1) {
                if (!firstFbNative) {
                    Log.e("TAG", "displayFbNativeAd: FacebookNativeFirst ");
                    FacebookNativeFirst(activity, frameLayout, type);
                } else {
                    if (mNativeAd != null) {
                        Log.e("TAG", "displayFbNativeAd: fbNativeBig Preload ");
                        fbNativeBig(activity, frameLayout, type);
                    } else {
                        if (!firstGoogleNative) {
                            Log.e("TAG", "displayGoogleNativeAd: Sajid First Native Load n Show");
                            FirstGoogleNative(activity, frameLayout, type);
                        } else {
                            PopulatGooglePreLoadNative(frameLayout, mNativeAdG, activity, type);
                        }
                    }
                }
            }
        }
        if (AdsManager.NativeStutas == 3 || AdsManager.NativeStutas == 23 || AdsManager.NativeStutas == 32 || AdsManager.NativeStutas == 43 || AdsManager.NativeStutas == 34) {
            if (type == 2) {
                if (!firstFbNative) {
                    Log.e("TAG", "displayFbNativeAd: FacebookNativeFirst ");
                    FacebookNativeFirst(activity, frameLayout, type);
                } else {
                    if (mNativeAd != null) {
                        Log.e("TAG", "displayFbNativeAd: fbNativeBig Preload ");
                        fbNativeBig(activity, frameLayout, type);
                    } else {
                        if (!firstGoogleNative) {
                            Log.e("TAG", "displayGoogleNativeAd: Sajid First Native Load n Show");
                            FirstGoogleNative(activity, frameLayout, type);
                        } else {
                            PopulatGooglePreLoadNative(frameLayout, mNativeAdG, activity, type);
                        }
                    }
                }
            }
        }
        if (AdsManager.NativeStutas == 4 || AdsManager.NativeStutas == 43 || AdsManager.NativeStutas == 34 || AdsManager.NativeStutas == 42 || AdsManager.NativeStutas == 24) {
            if (type == 3) {
                if (!firstFbNative) {
                    Log.e("TAG", "displayFbNativeAd: FacebookNativeFirst ");
                    FacebookNativeFirst(activity, frameLayout, type);
                } else {
                    if (mNativeAd != null) {
                        Log.e("TAG", "displayFbNativeAd: fbNativeBig Preload ");
                        fbNativeBig(activity, frameLayout, type);
                    } else {
                        if (!firstGoogleNative) {
                            Log.e("TAG", "displayGoogleNativeAd: Sajid First Native Load n Show");
                            FirstGoogleNative(activity, frameLayout, type);
                        } else {
                            PopulatGooglePreLoadNative(frameLayout, mNativeAdG, activity, type);
                        }
                    }
                }
            }
        }
    }

    public static void FirstGoogleNative(Activity activity, FrameLayout frameLayout, int type) {
        Log.e("TAG", "FirstGoogleNative: Sajid Load n Show");

        AdLoader.Builder builder;
        if (!chakGoogleNativeId) {
            chakGoogleNativeId = true;
            Log.e("TAGSAJID", "FirstGoogleNative: sajid Main1");
            if (!googleNative.equals("")) {
                Log.e("TAGSAJID", "FirstGoogleNative: sajid Main1 googleNative");
                builder = new AdLoader.Builder(activity, googleNative);
            } else {
                Log.e("TAGSAJID", "FirstGoogleNative: sajid Main1 googleNative2");
                builder = new AdLoader.Builder(activity, googleNative2);
            }
        } else {
            chakGoogleNativeId = false;
            Log.e("TAGSAJID", "FirstGoogleNative: sajid Main2");
            if (!googleNative2.equals("")) {
                Log.e("TAGSAJID", "FirstGoogleNative: sajid Main2 googleNative2");
                builder = new AdLoader.Builder(activity, googleNative2);
            } else {
                Log.e("TAGSAJID", "FirstGoogleNative: sajid Main2 googleNative");
                builder = new AdLoader.Builder(activity, googleNative);
            }
        }
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {

                Log.e("TAG", "onNativeAdLoaded: Sajid Load n Show");
                boolean isDestroyed = false;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    isDestroyed = activity.isDestroyed();
                }
                if (isDestroyed || activity.isFinishing() || activity.isChangingConfigurations()) {
                    nativeAd.destroy();
                    return;
                }
                PopulatGoogleFirstNative(frameLayout, nativeAd, type, activity);
                firstGoogleNative = true;
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder().build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).setAdChoicesPlacement(ADCHOICES_TOP_RIGHT).build();
        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(
                new AdListener() {
                    @Override
                    public void onAdImpression() {
                        adsTimerNative.setTimeNativeVar("mytimenative", (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

                        Log.d("TAG", "onAdImpression: Native show");
                        Log.e("TAG", "onAdImpression: Sajid Load n Show");
                        PreloadGoogleNative(activity, frameLayout, type);
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e("TAG", "onAdFailedToLoad: Sajid Load n Show");

                        PreloadGoogleNative(activity, frameLayout, type);

                    }
                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public static void PopulatGoogleFirstNative(FrameLayout nativeAdContainer, NativeAd nativeAd, int type, Activity activity) {
        final View adView;

        Log.e("TAG", "populateNativeAdViewFirst type: " + type);
        if (type == 1) {
            adView = activity.getLayoutInflater().inflate(R.layout.ad_big_google_native_layout, null);
            nativeAdContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (activity.getResources().getDisplayMetrics().heightPixels * 40) / 100));
        } else if (type == 2) {
            adView = activity.getLayoutInflater().inflate(R.layout.ad_mid_google_native_layout, null);
        } else if (type == 3) {
            adView = activity.getLayoutInflater().inflate(R.layout.ad_sml_google_native_layout, null);
        } else {
            adView = activity.getLayoutInflater().inflate(R.layout.ad_big_google_native_layout, null);
            nativeAdContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (activity.getResources().getDisplayMetrics().heightPixels * 40) / 100));
        }

        Log.e("TAG", "populateNativeAdViewFirst: Sajid Load n Show");
        ((TextView) adView.findViewById(R.id.ad_call_to_action)).setBackgroundColor(Color.parseColor("#" + AdsManager.NativeButton));
        ((TextView) adView.findViewById(R.id.ad_call_to_action)).setTextColor(Color.parseColor("#" + AdsManager.NativeButtonText));
        ((TextView) adView.findViewById(R.id.adText)).setBackgroundColor(Color.parseColor("#" + AdsManager.NativeButton));
        ((TextView) adView.findViewById(R.id.adText)).setTextColor(Color.parseColor("#" + AdsManager.NativeButtonText));
        ((TextView) adView.findViewById(R.id.ad_headline)).setTextColor(Color.parseColor("#" + AdsManager.NativeTitle));
        ((TextView) adView.findViewById(R.id.ad_body)).setTextColor(Color.parseColor("#" + AdsManager.NativeDesc));
        ((CardView) adView.findViewById(R.id.adview_card)).setCardBackgroundColor(Color.parseColor("#" + AdsManager.NativeBg));

        final NativeAdView adView1 = adView.findViewById(R.id.adview);
        adView1.setVisibility(View.VISIBLE);

        adView1.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView1.setBodyView(adView.findViewById(R.id.ad_body));
        adView1.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView1.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView1.setPriceView(adView.findViewById(R.id.ad_price));
        adView1.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView1.setStoreView(adView.findViewById(R.id.ad_store));
        adView1.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        if (type == 1) {
            adView1.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
            adView1.getMediaView().setMediaContent(nativeAd.getMediaContent());
            VideoController vc = nativeAd.getMediaContent().getVideoController();
            if (vc.hasVideoContent()) {
                vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                    @Override
                    public void onVideoEnd() {
                        super.onVideoEnd();
                    }
                });
            }
        }

        ((TextView) adView1.getHeadlineView()).setText(nativeAd.getHeadline());

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
            ((TextView) adView1.getCallToActionView()).setText(nativeAd.getCallToAction());
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

        adView1.setNativeAd(nativeAd);

        nativeAdContainer.removeAllViews();
        nativeAdContainer.addView(adView);
    }

    public static void FacebookNativeFirst(Activity activity, FrameLayout frameLayout, int type) {
        Log.e("TAG", "FacebookNativeFirst: sajid fb first load n show");
        com.facebook.ads.NativeAd nativeAd = new com.facebook.ads.NativeAd(activity, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");

        NativeAdListener nativeAdListener = new NativeAdListener() {

            @Override
            public void onMediaDownloaded(Ad ad) {
                Log.e("TAG", "onMediaDownloaded: sajid fb first load n show");
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                Log.e("TAG", "onError: sajid fb first load n show");
                PreloadFacebookNative(activity, frameLayout, type);

            }

            @Override
            public void onAdLoaded(Ad ad) {

                if (nativeAd == null || nativeAd != ad) {
                    if (customAds == 1 || customAds == 4 || customAds == 41 || customAds == 42 || customAds == 43) {
                        if (customAds == 1 || customAds == 4) {
                            custonNativeAd(activity, frameLayout, type);
                        } else if (type == NATIVE_BIG && customAds == 41) {
                            custonNativeAd(activity, frameLayout, type);
                        } else if (type == NATIVE_MIDEUM && customAds == 42) {
                            custonNativeAd(activity, frameLayout, type);
                        } else if (type == NATIVE_SMALL && customAds == 43) {
                            custonNativeAd(activity, frameLayout, type);
                        }
                    }
                    return;
                }
                PopulatFbFirstNative(activity, nativeAd, frameLayout, type);
                PreloadFacebookNative(activity, frameLayout, type);
                Log.e("TAG", "onAdLoaded: sajid fb first load n show");
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.e("TAG", "onAdClicked: sajid fb first load n show");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.e("TAG", "onLoggingImpression: sajid fb first load n show");
                adsTimerNative.setTimeNativeVar("mytimenative", (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

            }

        };

        nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());
    }

    public static void PopulatFbFirstNative(Activity activity, com.facebook.ads.NativeAd nativeAd, FrameLayout frameLayout, int type) {
        Log.e("TAG", "fbNativeAdViewFirst: sajid fb first load n show");
        nativeAd.unregisterView();
        LayoutInflater inflater = LayoutInflater.from(activity);
        RelativeLayout adView;
        if (type == 1) {
            adView = (RelativeLayout) inflater.inflate(R.layout.ad_big_facebook_native_layout, frameLayout, false);
            frameLayout.addView(adView);
        } else if (type == 2) {
            adView = (RelativeLayout) inflater.inflate(R.layout.ad_mid_facebook_native_layout, frameLayout, false);
            frameLayout.addView(adView);
        } else if (type == 3) {
            adView = (RelativeLayout) inflater.inflate(R.layout.ad_sml_facebook_native_layout, frameLayout, false);
            frameLayout.addView(adView);
        } else {
            adView = (RelativeLayout) inflater.inflate(R.layout.ad_big_facebook_native_layout, frameLayout, false);
            frameLayout.addView(adView);
        }

        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        NativeAdLayout nativeAdLayout = adView.findViewById(R.id.fb_native);
        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, nativeAdLayout);
        RelativeLayout mainLayout = adView.findViewById(R.id.mainLayout);
        TextView adText = adView.findViewById(R.id.adText);
        adText.setBackgroundColor(Color.parseColor("#" + AdsManager.NativeButton));
        adText.setTextColor(Color.parseColor("#" + AdsManager.NativeButtonText));

        mainLayout.setBackgroundColor(Color.parseColor("#" + AdsManager.NativeBg));
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        com.facebook.ads.MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        nativeAdTitle.setTextColor(Color.parseColor("#" + AdsManager.NativeTitle));

        com.facebook.ads.MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        nativeAdSocialContext.setTextColor(Color.parseColor("#" + AdsManager.NativeDesc));

        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        nativeAdBody.setTextColor(Color.parseColor("#" + AdsManager.NativeDesc));

        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);

        TextView nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
        nativeAdCallToAction.setBackgroundColor(Color.parseColor("#" + AdsManager.NativeButton));
        nativeAdCallToAction.setTextColor(Color.parseColor("#" + AdsManager.NativeButtonText));
        nativeAdTitle.setText(nativeAd.getAdvertiserName());

        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());

        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
        List<View> clickableViews = new ArrayList<>();

        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        nativeAd.registerViewForInteraction(adView, nativeAdMedia, nativeAdIcon, clickableViews);

    }

    public static void PreloadGoogleNative(Activity activity, FrameLayout frameLayout, int type) {
        AdLoader.Builder builder;
        if (!chakGoogleNativeId) {
            chakGoogleNativeId = true;
            builder = new AdLoader.Builder(activity, googleNative);
        } else {
            chakGoogleNativeId = false;
            if (!googleNative2.equals("1") || googleNative2.equals("")) {
                builder = new AdLoader.Builder(activity, googleNative2);
            } else {
                builder = new AdLoader.Builder(activity, googleNative);
            }
        }

        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {

                mNativeAdG = nativeAd;
                Log.e("parth", "onNativeAdLoaded: " + nativeAd);
                boolean isDestroyed = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    isDestroyed = activity.isDestroyed();
                }
                if (isDestroyed || activity.isFinishing() || activity.isChangingConfigurations()) {
                    nativeAd.destroy();
                    return;
                }
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder().build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).setAdChoicesPlacement(ADCHOICES_TOP_RIGHT).build();
        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdImpression() {
                adsTimerNative.setTimeNativeVar("mytimenative", (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

                Log.d("TAG", "onAdImpression: Native show");
                Log.e("TAG", "onNativeAdLoaded ************ 22: ");

            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                if (facebookAdsStuts == 1) {
                    DisplayFbNative(activity, frameLayout, type);
                } else if (customAds == 1 || customAds == 4 || customAds == 41 || customAds == 42 || customAds == 43) {
                    if (customAds == 1 || customAds == 4) {
                        custonNativeAd(activity, frameLayout, type);
                    } else if (type == NATIVE_BIG && customAds == 41) {
                        custonNativeAd(activity, frameLayout, type);
                    } else if (type == NATIVE_MIDEUM && customAds == 42) {
                        custonNativeAd(activity, frameLayout, type);
                    } else if (type == NATIVE_SMALL && customAds == 43) {
                        custonNativeAd(activity, frameLayout, type);
                    }
                }
                Log.e("TAG", "onNativeAdLoaded ************ 33: ");
                Log.e("kp_log_1_native", "onAdFailedToLoad:  ");
                Log.e("admobNative", "onAdFailedToLoad:  ");
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
        firstGoogleNative = true;
    }

    public static void PopulatGooglePreLoadNative(FrameLayout frameLayout, NativeAd nativeAd, Activity activity, int type) {
        try {
            final View adView;

            Log.e("TAG", "populateNativeAdViewFirst type: " + type);
            if (type == 1) {
                adView = activity.getLayoutInflater().inflate(R.layout.ad_big_google_native_layout, null);
                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (activity.getResources().getDisplayMetrics().heightPixels * 40) / 100));
            } else if (type == 2) {
                adView = activity.getLayoutInflater().inflate(R.layout.ad_mid_google_native_layout, null);
            } else if (type == 3) {
                adView = activity.getLayoutInflater().inflate(R.layout.ad_sml_google_native_layout, null);
            } else {
                adView = activity.getLayoutInflater().inflate(R.layout.ad_big_google_native_layout, null);
                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (activity.getResources().getDisplayMetrics().heightPixels * 40) / 100));
            }
            ((TextView) adView.findViewById(R.id.ad_call_to_action)).setBackgroundColor(Color.parseColor("#" + AdsManager.NativeButton));
            ((TextView) adView.findViewById(R.id.ad_call_to_action)).setTextColor(Color.parseColor("#" + AdsManager.NativeButtonText));
            ((TextView) adView.findViewById(R.id.adText)).setBackgroundColor(Color.parseColor("#" + AdsManager.NativeButton));
            ((TextView) adView.findViewById(R.id.adText)).setTextColor(Color.parseColor("#" + AdsManager.NativeButtonText));
            ((TextView) adView.findViewById(R.id.ad_headline)).setTextColor(Color.parseColor("#" + AdsManager.NativeTitle));
            ((TextView) adView.findViewById(R.id.ad_body)).setTextColor(Color.parseColor("#" + AdsManager.NativeDesc));
            ((CardView) adView.findViewById(R.id.adview_card)).setCardBackgroundColor(Color.parseColor("#" + AdsManager.NativeBg));

            final NativeAdView adView1 = adView.findViewById(R.id.adview);
            adView1.setVisibility(View.VISIBLE);
            adView1.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView1.setBodyView(adView.findViewById(R.id.ad_body));
            adView1.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView1.setIconView(adView.findViewById(R.id.ad_app_icon));
            adView1.setPriceView(adView.findViewById(R.id.ad_price));
            adView1.setStarRatingView(adView.findViewById(R.id.ad_stars));
            adView1.setStoreView(adView.findViewById(R.id.ad_store));
            adView1.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

            if (type == 1) {
                adView1.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
                adView1.getMediaView().setMediaContent(nativeAd.getMediaContent());
                VideoController vc = nativeAd.getMediaContent().getVideoController();
                if (vc.hasVideoContent()) {
                    vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                        @Override
                        public void onVideoEnd() {
                            super.onVideoEnd();
                        }
                    });
                }
            }

            ((TextView) adView1.getHeadlineView()).setText(nativeAd.getHeadline());

            if (nativeAd.getBody() == null) {
                Log.e("tag", "populateNativeAdView: parth");
                adView1.getBodyView().setVisibility(View.GONE);
            } else {
                adView1.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView1.getBodyView()).setText(nativeAd.getBody());
            }

            if (nativeAd.getCallToAction() == null) {
                adView1.getCallToActionView().setVisibility(View.GONE);
            } else {
                adView1.getCallToActionView().setVisibility(View.VISIBLE);
                ((TextView) adView1.getCallToActionView()).setText(nativeAd.getCallToAction());
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

            adView1.setNativeAd(nativeAd);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
            PreloadGoogleNative(activity, frameLayout, type);
        } catch (Exception e) {
            Log.e("TAG", "populateNativeAdView: " + e.getMessage());
        }
    }

    public static void PreloadFacebookNative(Activity activity, FrameLayout frameLayout, int type) {
        com.facebook.ads.NativeAd nativeAd = new com.facebook.ads.NativeAd(activity, fbNative);

        NativeAdListener nativeAdListener = new NativeAdListener() {

            @Override
            public void onMediaDownloaded(Ad ad) {
                Log.e("TAG", "onMediaDownloaded: ");
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                if (customAds == 1 || customAds == 4 || customAds == 41 || customAds == 42 || customAds == 43) {
                    if (customAds == 1 || customAds == 4) {
                        custonNativeAd(activity, frameLayout, type);
                    } else if (type == NATIVE_BIG && customAds == 41) {
                        custonNativeAd(activity, frameLayout, type);
                    } else if (type == NATIVE_MIDEUM && customAds == 42) {
                        custonNativeAd(activity, frameLayout, type);
                    } else if (type == NATIVE_SMALL && customAds == 43) {
                        custonNativeAd(activity, frameLayout, type);
                    }
                }
                Log.e("TAG", "onError: Code" + adError.getErrorCode() + " MSG " + adError.getErrorCode());
            }

            @Override
            public void onAdLoaded(Ad ad) {

                if (nativeAd == null || nativeAd != ad) {
                    if (customAds == 1 || customAds == 4 || customAds == 41 || customAds == 42 || customAds == 43) {
                        if (customAds == 1 || customAds == 4) {
                            custonNativeAd(activity, frameLayout, type);
                        } else if (type == NATIVE_BIG && customAds == 41) {
                            custonNativeAd(activity, frameLayout, type);
                        } else if (type == NATIVE_MIDEUM && customAds == 42) {
                            custonNativeAd(activity, frameLayout, type);
                        } else if (type == NATIVE_SMALL && customAds == 43) {
                            custonNativeAd(activity, frameLayout, type);
                        }
                    }
                    return;
                }
                Log.e("TAG", "onAdLoaded: ");
                mNativeAd = nativeAd;

            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.e("TAG", "onAdClicked: ");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.e("TAG", "onLoggingImpression: ");
                adsTimerNative.setTimeNativeVar("mytimenative", (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

            }

        };
        nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build();
        nativeAd.loadAd();
        firstFbNative = true;
    }

    public static void fbNativeBig(Activity activity, FrameLayout frameLayout, int type) {

        try {
            mNativeAd.unregisterView();
            LayoutInflater inflater = LayoutInflater.from(activity);
            RelativeLayout adView;
            if (type == 1) {
                adView = (RelativeLayout) inflater.inflate(R.layout.ad_big_facebook_native_layout, frameLayout, false);
                frameLayout.addView(adView);
            } else if (type == 2) {
                adView = (RelativeLayout) inflater.inflate(R.layout.ad_mid_facebook_native_layout, frameLayout, false);
                frameLayout.addView(adView);
            } else if (type == 3) {
                adView = (RelativeLayout) inflater.inflate(R.layout.ad_sml_facebook_native_layout, frameLayout, false);
                frameLayout.addView(adView);
            } else {
                adView = (RelativeLayout) inflater.inflate(R.layout.ad_big_facebook_native_layout, frameLayout, false);
                frameLayout.addView(adView);
            }

            LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
            NativeAdLayout fb_native = adView.findViewById(R.id.fb_native);
            AdOptionsView adOptionsView = new AdOptionsView(activity, mNativeAd, fb_native);
            RelativeLayout mainLayout = adView.findViewById(R.id.mainLayout);
            mainLayout.setBackgroundColor(Color.parseColor("#" + AdsManager.NativeBg));
            TextView adText = adView.findViewById(R.id.adText);
            adText.setBackgroundColor(Color.parseColor("#" + AdsManager.NativeButton));
            adText.setTextColor(Color.parseColor("#" + AdsManager.NativeButtonText));
            adChoicesContainer.removeAllViews();
            adChoicesContainer.addView(adOptionsView, 0);

            com.facebook.ads.MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
            TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
            nativeAdTitle.setTextColor(Color.parseColor("#" + AdsManager.NativeTitle));

            com.facebook.ads.MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
            TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
            nativeAdSocialContext.setTextColor(Color.parseColor("#" + AdsManager.NativeDesc));

            TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
            nativeAdBody.setTextColor(Color.parseColor("#" + AdsManager.NativeDesc));

            TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);

            TextView nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
            nativeAdCallToAction.setBackgroundColor(Color.parseColor("#" + AdsManager.NativeButton));
            nativeAdCallToAction.setTextColor(Color.parseColor("#" + AdsManager.NativeButtonText));
            nativeAdTitle.setText(mNativeAd.getAdvertiserName());

            nativeAdBody.setText(mNativeAd.getAdBodyText());
            nativeAdSocialContext.setText(mNativeAd.getAdSocialContext());

            nativeAdCallToAction.setVisibility(mNativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            nativeAdCallToAction.setText(mNativeAd.getAdCallToAction());

            sponsoredLabel.setText(mNativeAd.getSponsoredTranslation());
            List<View> clickableViews = new ArrayList<>();

            clickableViews.add(nativeAdTitle);
            clickableViews.add(nativeAdCallToAction);

            mNativeAd.registerViewForInteraction(adView, nativeAdMedia, nativeAdIcon, clickableViews);
            PreloadFacebookNative(activity, frameLayout, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
