package com.adsdemo.vdapps.adsload;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adsdemo.vdapps.BuildConfig;
import com.adsdemo.vdapps.adsload.interfaces.InternetStutas;
import com.adsdemo.vdapps.adsload.interfaces.getDataListner;
import com.adsdemo.vdapps.adsload.interfaces.getDialogListner;
import com.adsdemo.vdapps.adsload.models.MoreAppModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Ad_SplashLoad extends AppCompatActivity {

    public static String sdfsdf;
    public static SharedPreferences mysharedpreferences;
    public static int addfdsf123;
    String firstStatus;
    public Activity mactivity;
    public int mcversion;
    public getDataListner mmyCallback1;
    private ActivityResultLauncher<Intent> launchSomeActivity;
    private Dialog dialog2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdsManager.isSplash = true;
        launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            if (AdsManager.isInternetAvailable(Ad_SplashLoad.this)) {
                                if (dialog2 != null && dialog2.isShowing()) {
                                    dialog2.dismiss();
                                    ADSinit(mactivity, mcversion, mmyCallback1);
                                }
                            } else {
                                Toast.makeText(mactivity, "Check Again internet still not connected..", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                        }


                    }
                });
    }


    public void ADSinit(final Activity activity, final int cversion, final getDataListner myCallback1) {
        this.mactivity = activity;
        this.mcversion = cversion;
        this.mmyCallback1 = myCallback1;
        if (BuildConfig.DEBUG) {
            sdfsdf = "TRSOFTAG12789I";
        } else {
            sdfsdf = "TRSOFTAG82382I";
        }
        Calendar calender = Calendar.getInstance();
        calender.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        String currentDate = df.format(calender.getTime());

        mysharedpreferences = getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        firstStatus = mysharedpreferences.getString("firsttime", "true");
        final SharedPreferences.Editor editor = mysharedpreferences.edit();

        if (firstStatus.equals("true")) {
            editor.putString("date", currentDate).apply();
            editor.putString("firsttime", "false").apply();
            addfdsf123 = 13421;
        } else {
            String date = mysharedpreferences.getString("date", "");
            if (!currentDate.equals(date)) {
                editor.putString("date", currentDate).apply();
                addfdsf123 = 26894;

            } else {
                addfdsf123 = 87332;
            }
        }
        Ad_Dialogs.isDevMode(Ad_SplashLoad.this, 101, new getDialogListner() {
            @Override
            public void onCallBack() {
                if (AdsManager.isInternetAvailable(activity)) {
                    getAdsData(activity, myCallback1);
                } else {
                    dialog2 = Ad_Dialogs.gotoInternetDialog(activity, new InternetStutas() {
                        @Override
                        public void chackInternet(Boolean b) {
                            getAdsData(activity, myCallback1);
                        }
                    });
                }
            }
        });


    }

    public void getAdsData(Activity activity, getDataListner myCallback1) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest strRequest = new StringRequest(Request.Method.POST, "https://jktechsol.com/AppsManager/api/v2/get_app.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AdsManager.moreAppsList = new ArrayList<>();
                AdsManager.moreGamesList = new ArrayList<>();
                AdsManager.moreAllList = new ArrayList<>();
                try {
                    response = AdsManager.decryptRes(activity, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    AdsManager.status1 = jsonObject.getBoolean("STATUS");
                    JSONObject AdmobJsonObject = jsonObject.getJSONObject("PLACEMENT");
                    JSONObject SettingJsonObject = jsonObject.getJSONObject("APP_SETTINGS");
                    JSONArray Advertise_List = jsonObject.getJSONArray("Advertise_List");
                    AdsManager.getInstance(activity).SetCustomAdData(Advertise_List);
                    JSONArray MORE_APP_SPLASH = jsonObject.getJSONArray("MORE_APP_SPLASH");
                    JSONArray MORE_APP_EXIT = jsonObject.getJSONArray("MORE_APP_EXIT");
                    JSONObject admobObject = AdmobJsonObject.getJSONObject("Admob");
                    JSONObject fbObject = AdmobJsonObject.getJSONObject("Facebookaudiencenetwork");
                    JSONObject myCustomObject = AdmobJsonObject.getJSONObject("MyCustomAds");
                    AdsManager.customAds = Integer.parseInt(SettingJsonObject.getString("customAds").trim());
                    AdsManager.BannerClick = Integer.parseInt(SettingJsonObject.getString("BannerClick").trim());
                    AdsManager.checkBannerClick = AdsManager.BannerClick;
                    AdsManager.NativeAdStatus = Integer.parseInt(SettingJsonObject.getString("NativeAdStatus").trim());
                    AdsManager.NativeClick = Integer.parseInt(SettingJsonObject.getString("NativeClick").trim());
                    AdsManager.NativeTimer = Integer.parseInt(SettingJsonObject.getString("NativeTimer").trim());
                    AdsManager.checkNativeClick = AdsManager.NativeClick;
                    AdsManager.AllAsdStutas = Integer.parseInt(SettingJsonObject.getString("app_adShowStatus").trim());
                    AdsManager.Launch = Integer.parseInt(SettingJsonObject.getString("LaunchScreen").trim());
                    AdsManager.AdsStutas = Integer.parseInt(SettingJsonObject.getString("AdsStutas").trim());
                    AdsManager.googleAdsStutas = Integer.parseInt(admobObject.getString("ad_showAdStatus").trim());
                    AdsManager.facebookAdsStuts = Integer.parseInt(fbObject.getString("ad_showAdStatus").trim());
                    AdsManager.showAdsClick = Integer.parseInt(SettingJsonObject.getString("app_mainClickCntSwAd").trim());
                    AdsManager.AsdTimerStutas = Integer.parseInt(SettingJsonObject.getString("app_innerClickCntSwAd").trim());
                    AdsManager.showBackAdsClick = Integer.parseInt(SettingJsonObject.getString("app_backPressAdLimit").trim());
                    AdsManager.chackAdsClick = AdsManager.showAdsClick;
                    AdsManager.chackBackAdsClick = AdsManager.showBackAdsClick;
                    AdsManager.AppID = admobObject.getString("AppID");
                    AdsManager.MoreAds = Integer.parseInt(SettingJsonObject.getString("MoreAds"));
                    AdsManager.googleBanner = admobObject.getString("Banner1");
                    AdsManager.googleInterstitial = admobObject.getString("Interstitial1");
                    AdsManager.googleInterstitial2 = admobObject.getString("Interstitial2");
                    if (AdsManager.googleInterstitial2.equals("")) {
                        AdsManager.googleInterstitial2 = AdsManager.googleInterstitial;
                    } else if (AdsManager.googleInterstitial.equals("")) {
                        AdsManager.googleInterstitial = AdsManager.googleInterstitial2;
                    }

                    AdsManager.googleNative = admobObject.getString("Native1");
                    AdsManager.googleNative2 = admobObject.getString("Native2");
                    AdsManager.googleAppOpen = admobObject.getString("AppOpen1");
                    AdsManager.googleAppOpen2 = admobObject.getString("AppOpen2");
                    if (AdsManager.googleAppOpen2.equals("")) {
                        AdsManager.googleAppOpen2 = AdsManager.googleAppOpen;
                    } else if (AdsManager.googleAppOpen.equals("")) {
                        AdsManager.googleAppOpen = AdsManager.googleAppOpen2;
                    }
                    if (AdsManager.googleNative2.equals("")) {
                        AdsManager.googleNative2 = AdsManager.googleNative;
                    } else if (AdsManager.googleNative.equals("")) {
                        AdsManager.googleNative = AdsManager.googleNative2;
                    }
                    AdsManager.app_newPackageName = SettingJsonObject.getString("app_newPackageName");
                    AdsManager.NativeBg = SettingJsonObject.getString("NativeBg");
                    AdsManager.NativeTitle = SettingJsonObject.getString("NativeTitle");
                    AdsManager.NativeDesc = SettingJsonObject.getString("NativeDesc");
                    AdsManager.NativeButton = SettingJsonObject.getString("NativeButton");
                    AdsManager.NativeButtonText = SettingJsonObject.getString("NativeButtonText");
                    AdsManager.app_backPressAdType = SettingJsonObject.getString("app_backPressAdType");
                    AdsManager.fbBanner = fbObject.getString("Banner1");
                    AdsManager.fbInterstitial = fbObject.getString("Interstitial1");
                    AdsManager.fbNative = fbObject.getString("Native1");
                    AdsManager.fbNativeBanner = fbObject.getString("NativeBanner1");
                    AdsManager.customBanner = myCustomObject.getString("Banner1");
                    AdsManager.customInterstitial = myCustomObject.getString("Interstitial1");
                    AdsManager.customNative = myCustomObject.getString("NativeBanner1");
                    AdsManager.customAppOpen = myCustomObject.getString("AppOpen1");
                    AdsManager.PrivacyPolicyScreen = Integer.parseInt(SettingJsonObject.getString("PrivacyPolicyScreen").trim());
                    AdsManager.PermissionScreen = Integer.parseInt(SettingJsonObject.getString("PermissionScreen").trim());
                    AdsManager.CountryScreen = Integer.parseInt(SettingJsonObject.getString("CountryScreen").trim());
                    AdsManager.LanguageScreen = Integer.parseInt(SettingJsonObject.getString("LanguageScreen").trim());
                    AdsManager.SwipeScreen = Integer.parseInt(SettingJsonObject.getString("SwipeScreen").trim());
                    AdsManager.AppPurchaseScreen = Integer.parseInt(SettingJsonObject.getString("AppPurchaseScreen").trim());
                    AdsManager.ExitScreen = Integer.parseInt(SettingJsonObject.getString("ExitScreen").trim());
                    AdsManager.RateDialog = Integer.parseInt(SettingJsonObject.getString("RateDialog").trim().isEmpty() ? "0" : SettingJsonObject.getString("RateDialog").trim());
                    AdsManager.backTimer = Integer.parseInt(SettingJsonObject.getString("backTimer").trim());
                    AdsManager.InterstitialStutas = Integer.parseInt(SettingJsonObject.getString("InterstitialStutas").trim());
                    AdsManager.BackPressAdStatus = Integer.parseInt(SettingJsonObject.getString("app_backPressAdStatus").trim());
                    AdsManager.app_dialogBeforeAdShow = Integer.parseInt(SettingJsonObject.getString("app_dialogBeforeAdShow").trim());
                    AdsManager.app_updateAppDialogStatus = Integer.parseInt(SettingJsonObject.getString("app_updateAppDialogStatus").trim());
                    AdsManager.app_needInternet = Integer.parseInt(SettingJsonObject.getString("app_needInternet").trim());
                    AdsManager.app_redirectOtherAppStatus = Integer.parseInt(SettingJsonObject.getString("app_redirectOtherAppStatus").trim());
                    AdsManager.app_versionCode = Integer.parseInt(SettingJsonObject.getString("app_versionCode").trim().isEmpty() ? "0" : SettingJsonObject.getString("app_versionCode").trim());
                    AdsManager.NativeStutas = Integer.parseInt(SettingJsonObject.getString("NativeStutas").trim());
                    AdsManager.BannerStutas = Integer.parseInt(SettingJsonObject.getString("BannerStutas").trim());
                    AdsManager.SpaceBoxStutas = Integer.parseInt(SettingJsonObject.getString("SpaceBoxStutas").trim());
                    AdsManager.app_id = SettingJsonObject.getString("app_id");

                    if (AdsManager.NativeBg.equals("")) {
                        AdsManager.NativeBg = "#ffffff";
                    } else if (AdsManager.NativeTitle.equals("")) {
                        AdsManager.NativeTitle = "#000000";
                    } else if (AdsManager.NativeDesc.equals("")) {
                        AdsManager.NativeDesc = "#000000";
                    } else if (AdsManager.NativeButton.equals("")) {
                        AdsManager.NativeButton = "#0097E2";
                    } else if (AdsManager.NativeButtonText.equals("")) {
                        AdsManager.NativeButtonText = "#ffffff";
                    }

                    for (int i = 0; i < MORE_APP_SPLASH.length(); i++) {
                        JSONObject obj = MORE_APP_SPLASH.getJSONObject(i);
                        MoreAppModel moreAppModel = new MoreAppModel();
                        moreAppModel.app_logo = obj.getString("app_logo");
                        moreAppModel.app_name = obj.getString("app_name");
                        moreAppModel.app_packageName = obj.getString("app_packageName");
                        AdsManager.moreAppsList.add(moreAppModel);
                    }

                    for (int i = 0; i < MORE_APP_EXIT.length(); i++) {
                        JSONObject obj = MORE_APP_EXIT.getJSONObject(i);
                        MoreAppModel moreAppModel = new MoreAppModel();
                        moreAppModel.app_logo = obj.getString("app_logo");
                        moreAppModel.app_name = obj.getString("app_name");
                        moreAppModel.app_packageName = obj.getString("app_packageName");
                        AdsManager.moreGamesList.add(moreAppModel);
                    }

                    Log.e("TAG", "onResponse AdsManager.moreGamesList 44: " + AdsManager.moreGamesList.size());
                    Log.e("TAG", "onResponse AdsManager.moreAppsList 55: " + AdsManager.moreAppsList.size());

                    if (AdsManager.moreAppsList.size() != 0 && AdsManager.moreGamesList.size() != 0) {
                        int max = Math.max(AdsManager.moreAppsList.size(), AdsManager.moreGamesList.size());
                        String type;
                        if (max == AdsManager.moreAppsList.size()) {
                            type = "app";
                            AdsManager.moreAllList.addAll(AdsManager.moreAppsList);
                        } else {
                            type = "game";
                            AdsManager.moreAllList.addAll(AdsManager.moreGamesList);
                        }

                        int temp = 1;
                        for (MoreAppModel moreAppModel : type.equals("game") ? AdsManager.moreAppsList : AdsManager.moreGamesList) {
                            AdsManager.moreAllList.add(temp, moreAppModel);
                            temp = temp + 2;
                        }
                    } else {
                        if (AdsManager.moreAppsList.size() != 0) {
                            Log.e("TAG", "onResponse MORE_APP_SPLASH 11: " + AdsManager.moreAppsList.size());
                            AdsManager.moreAllList.addAll(AdsManager.moreAppsList);
                        } else if (AdsManager.moreGamesList.size() != 0) {
                            Log.e("TAG", "onResponse MORE_APP_SPLASH 22: " + AdsManager.moreGamesList.size());
                            AdsManager.moreAllList.addAll(AdsManager.moreGamesList);
                        }
                    }

                    Log.e("TAG", "onResponse moreGamesList 33: " + AdsManager.moreGamesList.size());
                    if (AdsManager.moreAllList.size() != 0 || AdsManager.moreAllList != null) {
                        AdsManager.totalAppCouner = AdsManager.moreAllList.size() + 1;
                    }

                    AdsManager.getAllOnlineData = true;

                    if (AdsManager.AllAsdStutas == 1) {

                        if (AdsManager.app_redirectOtherAppStatus == 1 && !AdsManager.app_newPackageName.equals("")) {
                            Ad_Dialogs.gotoRedirectDialog(activity);
                        } else if (AdsManager.app_updateAppDialogStatus == 1 && checkUpdate(getCurrentVersionCode(), AdsManager.app_versionCode)) {
                            Ad_Dialogs.showUpdateDialog("https://play.google.com/store/apps/details?id=" + activity.getPackageName(), activity);
                        } else {
                            myCallback1.onSuccess();
                        }

                    } else {
                        if (AdsManager.app_redirectOtherAppStatus == 1 && !AdsManager.app_newPackageName.equals("")) {
                            Ad_Dialogs.gotoRedirectDialog(activity);
                        } else if (AdsManager.app_updateAppDialogStatus == 1 && checkUpdate(getCurrentVersionCode(), AdsManager.app_versionCode)) {
                            Ad_Dialogs.showUpdateDialog("https://play.google.com/store/apps/details?id=" + activity.getPackageName(), activity);
                        } else {
                            myCallback1.onSuccess();
                        }
                    }
                    AdsManager.GooglePreloadInter(activity, AdsManager.googleInterstitial, false);
                    AdsManager.FbPreloadInter(activity, AdsManager.fbInterstitial, false);

                } catch (Exception e) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace();
                        Toast.makeText(activity, "JSON RESPONSE ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("TAG", "Exception onResponse --->>> : " + e.getMessage());
                    } else {
                        Log.e("TAG", "onResponse --->>> : " + e.getMessage());
                    }

                    if (AdsManager.app_redirectOtherAppStatus == 1 && !AdsManager.app_newPackageName.equals("")) {
                        Ad_Dialogs.gotoRedirectDialog(activity);
                    } else if (AdsManager.app_updateAppDialogStatus == 1 && checkUpdate(getCurrentVersionCode(), AdsManager.app_versionCode)) {
                        Ad_Dialogs.showUpdateDialog("https://play.google.com/store/apps/details?id=" + activity.getPackageName(), activity);
                    } else {
                        myCallback1.onSuccess();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("TAG", "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("PHSUGSG6783019KG", activity.getPackageName());
                params.put("AFHJNTGDGD563200K", AdsManager.getKeyHash(activity));
                params.put("DTNHGNH7843DFGHBSA", String.valueOf(addfdsf123));
                params.put("DBMNBXRY4500991G", sdfsdf);

                Log.d("TAG", "getParams: " + activity.getPackageName() + "\n" + AdsManager.getKeyHash(activity) + "\n" + String.valueOf(addfdsf123) + "\n" + sdfsdf);
                return params;
            }

        };
        strRequest.setShouldCache(false);
        requestQueue.add(strRequest);
    }

    public static boolean checkUpdate(int cversion, int app_versionCode) {
        try {
            //for multi version with saperate comma
            /*
            String str[] = app_versionCode.split(",");
            if (Arrays.asList(str).contains(cversion + "")) {
                return true;
            }*/

            //for all down version
            if (cversion < app_versionCode) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getCurrentVersionCode() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    getPackageName(), 0);
            return info.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Ad_Dialogs.isDevMode(Ad_SplashLoad.this, 101, new getDialogListner() {
                @Override
                public void onCallBack() {
                    if (AdsManager.isInternetAvailable(mactivity)) {
                        getAdsData(mactivity, mmyCallback1);
                    } else {
                        dialog2 = Ad_Dialogs.gotoInternetDialog(mactivity, new InternetStutas() {
                            @Override
                            public void chackInternet(Boolean b) {
                                getAdsData(mactivity, mmyCallback1);
                            }
                        });
                    }
                }
            });

        }
    }
}
