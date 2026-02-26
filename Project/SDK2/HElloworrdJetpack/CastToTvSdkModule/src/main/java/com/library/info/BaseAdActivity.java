package com.library.info;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anchorfree.partner.api.ClientInfo;
import com.anchorfree.partner.api.auth.AuthMethod;
import com.anchorfree.partner.api.response.User;
import com.anchorfree.reporting.TrackingConstants;
import com.anchorfree.sdk.HydraTransportConfig;
import com.anchorfree.sdk.NotificationConfig;
import com.anchorfree.sdk.SessionConfig;
import com.anchorfree.sdk.TransportConfig;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.sdk.UnifiedSDKConfig;
import com.anchorfree.sdk.rules.TrafficRule;
import com.anchorfree.vpnsdk.callbacks.CompletableCallback;
import com.anchorfree.vpnsdk.exceptions.NetworkRelatedException;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.anchorfree.vpnsdk.exceptions.VpnPermissionDeniedException;
import com.anchorfree.vpnsdk.exceptions.VpnPermissionRevokedException;
import com.anchorfree.vpnsdk.transporthydra.HydraTransport;
import com.anchorfree.vpnsdk.transporthydra.HydraVpnTransportException;
import com.anchorfree.vpnsdk.vpnservice.VPNState;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.northghost.caketube.CaketubeTransport;
import com.northghost.caketube.OpenVpnTransportConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

import static com.library.info.CastTvAppManager.ADMOB_AO;
import static com.library.info.CastTvAppManager.ADMOB_B;
import static com.library.info.CastTvAppManager.ADMOB_I;
import static com.library.info.CastTvAppManager.ADMOB_N;
import static com.library.info.CastTvAppManager.InterclickCountry;
import static com.library.info.CastTvAppManager.VPNCompulsory;
import static com.library.info.CastTvAppManager.app_adShowStatus;
import static com.library.info.CastTvAppManager.app_backwardClickAd;
import static com.library.info.CastTvAppManager.app_backwardInterstitial;
import static com.library.info.CastTvAppManager.app_customAdStatus;
import static com.library.info.CastTvAppManager.app_customAppOpenStatus;
import static com.library.info.CastTvAppManager.app_customInterstitialStatus;
import static com.library.info.CastTvAppManager.app_customNativeBannerStatus;
import static com.library.info.CastTvAppManager.app_forwardClickAd;
import static com.library.info.CastTvAppManager.app_forwardInterstitial;
import static com.library.info.CastTvAppManager.app_launchCountry;
import static com.library.info.CastTvAppManager.app_nativeBanner;
import static com.library.info.CastTvAppManager.app_nativeBig;
import static com.library.info.CastTvAppManager.app_privacyPolicyLink;
import static com.library.info.CastTvAppManager.app_swipCountry;
import static com.library.info.CastTvAppManager.app_swipMain;
import static com.library.info.CastTvAppManager.app_updateAppDialogStatus;
import static com.library.info.CastTvAppManager.app_versionCode;
import static com.library.info.CastTvAppManager.app_vpnBaseUrl;
import static com.library.info.CastTvAppManager.app_vpnCarrierId;
import static com.library.info.CastTvAppManager.app_vpnCountry;
import static com.library.info.CastTvAppManager.app_vpnEnable;
import static com.library.info.CastTvAppManager.bannerspacebox;
import static com.library.info.CastTvAppManager.developerDialog;
import static com.library.info.CastTvAppManager.exitScreen;
import static com.library.info.CastTvAppManager.internetCompulsory;
import static com.library.info.CastTvAppManager.isBlockedVPN;
import static com.library.info.CastTvAppManager.isNetworkAvailable;
import static com.library.info.CastTvAppManager.myAppMarketingList;
import static com.library.info.CastTvAppManager.nativeExit;
import static com.library.info.CastTvAppManager.nativebg;
import static com.library.info.CastTvAppManager.nativebutton;
import static com.library.info.CastTvAppManager.nativedescription;
import static com.library.info.CastTvAppManager.nativespacebox;
import static com.library.info.CastTvAppManager.nativespaceboxColor;
import static com.library.info.CastTvAppManager.nativetitle;
import static com.library.info.CastTvAppManager.secondsRemaining;
import static com.library.info.CastTvAppManager.showDialogBeforeAd;
import static com.library.info.CastTvAppManager.startScreen;
import static com.library.info.BuildConfig.SHARED_PREFS;
import static com.library.info.CustomAppOpenAdCastTv.isactivitystart;
import static com.library.info.MyApplication.admobAppOpenLoadingStatus;
import static com.library.info.MyApplication.appOpenAdManager;

public class BaseAdActivity extends AppCompatActivity {

    CountDownTimer mCountDownTimer;
    Handler mHandler = null;
    ActivityResultLauncher<Intent> mActivityResultLauncher;
    public Dialog mDialog;
    public Activity mActivity;
    public int mCurrentVersion;
    public SuccessListener mListener;
    ArrayList<String> mCountryArray;
    ArrayList<String> mCarrierArray;
    UnifiedSDK mSdk;
    String mPreferenceCarrierKey = "carrier_list";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            if (isNetworkAvailable(BaseAdActivity.this)) {
                                if (mDialog != null && mDialog.isShowing()) {
                                    mDialog.dismiss();
                                    LoadSplash(getPackageName(), mActivity, mCurrentVersion, mListener);
                                }
                            } else {
                                Toast.makeText(mActivity, "Check Again internet still not connected..", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void LoadSplash(String appName, final Activity activity, final int cversion, final SuccessListener successListener) {
        this.mActivity = activity;
        this.mCurrentVersion = cversion;
        this.mListener = successListener;
        final SharedPreferences preferences = activity.getSharedPreferences("ad_pref", 0);
        final SharedPreferences.Editor editor_AD_PREF = preferences.edit();
        internetCompulsory = preferences.getInt("internetCompulsory", internetCompulsory);
        developerDialog = preferences.getInt("developerDialog", developerDialog);

        if (isDeveloperModeEnabled(activity) && developerDialog == 1) {
            final Dialog dialogD = new Dialog(activity);
            dialogD.setCancelable(false);
            View view = getLayoutInflater().inflate(R.layout.developer_layout, null);
            dialogD.setContentView(view);
            final ImageView retry_buttton = view.findViewById(R.id.retry_buttton);
            dialogD.show();
            Window window = dialogD.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            retry_buttton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialogD != null && dialogD.isShowing()) {
                        dialogD.dismiss();
                    }
                    if (!isDeveloperModeEnabled(activity)) {
                        LoadSplash(getPackageName(), activity, cversion, successListener);
                    } else {
                        try {
                            startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                            finish();
                        } catch (Exception e) {
                            LoadSplash(getPackageName(), activity, cversion, successListener);
                        }
                    }
                }
            });
            return;
        }

        if (isNetworkAvailable(BaseAdActivity.this) && isVpnActive(activity) && developerDialog == 1) {
            try {
                new BaseAdActivity().disconnectFromStartVnp(new CastTvVCallbackListener() {
                    @Override
                    public void callbackSuccess() {
                        LoadSplash(getPackageName(), activity, cversion, successListener);
                    }

                    @Override
                    public void callbackFail() {
                        final Dialog dialogD = new Dialog(activity);
                        dialogD.setCancelable(false);
                        View view = getLayoutInflater().inflate(R.layout.vpn_off_layout, null);
                        dialogD.setContentView(view);
                        final ImageView retry_buttton = view.findViewById(R.id.retry_buttton);
                        dialogD.show();
                        Window window = dialogD.getWindow();
                        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                        retry_buttton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (dialogD != null && dialogD.isShowing()) {
                                    dialogD.dismiss();
                                }
                                finishAffinity();
                            }
                        });
                    }
                });
            } catch (Exception e) {
                final Dialog dialogD = new Dialog(activity);
                dialogD.setCancelable(false);
                View view = getLayoutInflater().inflate(R.layout.vpn_off_layout, null);
                dialogD.setContentView(view);
                final ImageView retry_buttton = view.findViewById(R.id.retry_buttton);
                dialogD.show();
                Window window = dialogD.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                retry_buttton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialogD != null && dialogD.isShowing()) {
                            dialogD.dismiss();
                        }
                        finishAffinity();
                    }
                });

            }
            return;
        }

        if (!isNetworkAvailable(BaseAdActivity.this) && internetCompulsory == 1) {
            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            View view = getLayoutInflater().inflate(R.layout.retry_layout, null);
            dialog.setContentView(view);
            final ImageView retry_buttton = view.findViewById(R.id.retry_buttton);
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            retry_buttton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isNetworkAvailable(BaseAdActivity.this)) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            LoadSplash(getPackageName(), activity, cversion, successListener);
                        }
                    } else {
                        isactivitystart = true;
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        mActivityResultLauncher.launch(intent);
                    }
                }
            });
            return;
        }

        MobileAds.initialize(activity, initializationStatus -> {});
        ZSecurity secure = new ZSecurity(activity, getResources().getString(R.string.wireshark));
        RetrofitInterface methods = null;
        try {

//            Log.e("@@@SplashActivity", "LoadSplash: "+HiddenTesla.ice_hound(this, "ROk9Ad8i7ufLZY+TQJPQUibtO8vDSyY1RnO5o3XH1EGscbHHvuRRjw==") );
//            Log.e("@@@SplashActivity", "LoadReverseSplash: "+HiddenTesla.reverse_ice_Hound(this, "https://androtechinfo.com/service/") );

            new CastTvPreferenceManager(BaseAdActivity.this).setInstallType();
            methods = CastTvRetrofitClient.getRetrofitInstance(secure.decrypt(secure.EncryptToFinalTransferText(HiddenTesla.ice_hound(this, "ROk9Ad8i7ufLZY+TQJPQUibtO8vDSyY1RnO5o3XH1EGscbHHvuRRjw=="))) + appName + "/").create(RetrofitInterface.class);
            Call<JsonObject> call = methods.getData("3", appName, new CastTvPreferenceManager(this).getInstallType(), BuildConfig.BUILD_TYPE);
            call.enqueue(new retrofit2.Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                    Log.e("Response", "onResponse: " + response.body());
                    if (response.body() != null && response.body().get("status").getAsBoolean()) {
                        try {
                            JsonObject asJsonObject = response.body().get("data").getAsJsonObject();

                            app_privacyPolicyLink = checkNotNull(asJsonObject, "QZXIT3_ska_appPcyLink", "");
                            app_updateAppDialogStatus = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appUpdateDiaST", "0"));
                            app_versionCode = checkNotNull(asJsonObject, "QZXIT3_ska_appvCod", "");
                            app_adShowStatus = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appAdShwST", "0"));

                            app_forwardClickAd = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appInBtnClick", "0"));
                            if (app_forwardClickAd != 0) app_forwardClickAd++;
                            app_backwardClickAd = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appOutBtnClick", "0"));
                            if (app_backwardClickAd != 0) app_backwardClickAd++;

                            app_vpnEnable = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appVON", "0"));
                            app_vpnCountry = checkNotNull(asJsonObject, "QZXIT3_ska_appVCtry", "");
                            app_vpnCarrierId = checkNotNull(asJsonObject, "QZXIT3_ska_appVCarId", "");
                            app_vpnBaseUrl = checkNotNull(asJsonObject, "QZXIT3_ska_appVBSUrl", "");

                            app_forwardInterstitial = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appFrwdInter", "0"));
                            app_backwardInterstitial = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appBkwardInter", "0"));

                            app_nativeBig = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appNtvBg", "0"));
                            app_nativeBanner = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appNtvBnn", "0"));
                            app_launchCountry = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appLaunchCtry", "0"));
                            app_swipCountry = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appSwpCtry", "0"));
                            app_swipMain = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appSwpMn", "0"));

                            developerDialog = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_developerDialog", "0"));
                            VPNCompulsory = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_VPNCompulsory", "0"));
                            secondsRemaining = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_secondsRemaining", "0"));

                            internetCompulsory = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_internetCompulsory", "1"));
                            showDialogBeforeAd = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_showDialogBeforeAd", "0"));

                            nativespaceboxColor = checkNotNull(asJsonObject, "QZXIT3_ska_nativespaceboxColor", "2B86C5");
                            nativebg = checkNotNull(asJsonObject, "QZXIT3_ska_nativebg", "FFF0D0");
                            nativetitle = checkNotNull(asJsonObject, "QZXIT3_ska_nativetitle", "FA709A");
                            nativebutton = checkNotNull(asJsonObject, "QZXIT3_ska_nativebutton", "FFBBEC");
                            nativedescription = checkNotNull(asJsonObject, "QZXIT3_ska_nativedescription", "784BA0");

                            nativespacebox = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_nativespacebox", "0"));
                            bannerspacebox = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_bannerspacebox", "0"));

                            nativeExit = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_nativeExit", "0"));
                            InterclickCountry = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_InterclickCountry", "0"));
                            if (InterclickCountry != 0) InterclickCountry++;
                            startScreen = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_startScreen", "0"));
                            exitScreen = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_exitScreen", "0"));

                            app_customAdStatus = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appCstmST", "0"));
                            app_customAppOpenStatus = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appCstmAOST", "0"));
                            app_customInterstitialStatus = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appCstmINTERST", "0"));
                            app_customNativeBannerStatus = Integer.parseInt(checkNotNull(asJsonObject, "QZXIT3_ska_appCstmNBST", "0"));

                            editor_AD_PREF.putInt("app_needInternet", internetCompulsory).apply();
                            editor_AD_PREF.putInt("developerDialog", developerDialog).apply();
                            editor_AD_PREF.commit();

                            try {
                                JsonArray Advertise_List = asJsonObject.getAsJsonArray("QZXIT3_ska_appCstmAdData");
                                CastTvAppManager.getInstance(activity).SetCustomAdData(Advertise_List);
                            } catch (Exception e) {

                            }

                            if (app_updateAppDialogStatus == 1 && isUpdateAvailable(cversion, app_versionCode)) {
                                CastTvAppManager.getInstance(BaseAdActivity.this).showUpdateDialog("https://play.google.com/store/apps/details?id=" + activity.getPackageName(), BaseAdActivity.this);
                                return;
                            }

                            checkProxyCountry(asJsonObject, successListener);
                        } catch (Exception e) {
                            Toast.makeText(activity, "Something Missing..", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("kp_log_1__", "onErrorResponse: Status fail" + t.getMessage());
                    new Handler(getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!isNetworkAvailable(BaseAdActivity.this) && internetCompulsory == 1) {
                                mDialog = new Dialog(activity);
                                mDialog.setCancelable(false);
                                View view = getLayoutInflater().inflate(R.layout.retry_layout, null);
                                mDialog.setContentView(view);
                                final ImageView retry_buttton = view.findViewById(R.id.retry_buttton);
                                mDialog.show();
                                Window window = mDialog.getWindow();
                                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                                retry_buttton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (isNetworkAvailable(BaseAdActivity.this)) {
                                            if (mDialog != null && mDialog.isShowing()) {
                                                mDialog.dismiss();
                                                LoadSplash(getPackageName(), activity, cversion, successListener);
                                            }
                                        } else {
                                            //Toast.makeText(activity, "Check Again internet still not connected..", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                            mActivityResultLauncher.launch(intent);
                                        }

                                    }
                                });
                                return;
                            } else {
                                successListener.onSuccess();
                            }
                        }
                    }, 1500);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String checkNotNull(JsonObject object, String key, String defaultValue) {
        try {
            return object.get(key).getAsString().trim();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean isDeveloperModeEnabled(Activity activity) {
        return Settings.Secure.getInt(activity.getApplicationContext().getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
    }

    public static boolean isVpnActive(Context context) {
        //this method doesn't work below API 21
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return false;

        boolean vpnInUse = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(activeNetwork);
            return caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
        }

        Network[] networks = connectivityManager.getAllNetworks();

        for (int i = 0; i < networks.length; i++) {
            NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(networks[i]);
            if (caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                vpnInUse = true;
                break;
            }
        }

        return vpnInUse;
    }

    private void loadSplashAd(JsonObject AdmobJsonObject, SuccessListener myCallback1) {
        try {
            mHandler = new Handler(getMainLooper());
            if (app_adShowStatus == 1) {
                ADMOB_B[0] = AdmobJsonObject.get("QZXIT3_ska_appBnnStr").getAsString().trim();
                ADMOB_N[0] = AdmobJsonObject.get("QZXIT3_ska_appNtvStr").getAsString().trim();
                ADMOB_I[0] = AdmobJsonObject.get("QZXIT3_ska_appInterStr").getAsString().trim();
                ADMOB_AO[0] = AdmobJsonObject.get("QZXIT3_ska_appAoStr").getAsString().trim();

                if (admobAppOpenLoadingStatus.equals("loaded")) {
                    if (MyApplication.isappbackground) {
                        return;
                    }
                    appOpenAdManager.showAdIfAvailable(BaseAdActivity.this, new MyApplication.OnShowAdCompleteListener() {
                        @Override
                        public void onShowAdComplete() {
                            myCallback1.onSuccess();
                        }

                        @Override
                        public void onShowAdFail() {
                            if (app_customAdStatus == 1 && app_customAppOpenStatus == 1 && myAppMarketingList.size() > 0) {
                                CastTvCustomModel customModel = CastTvAppManager.getInstance(BaseAdActivity.this).getMyCustomAd();
                                if (customModel != null)
                                    CustomAppOpenAdCastTv.newIntent(BaseAdActivity.this, myCallback1, customModel);
                                else {
                                    myCallback1.onSuccess();
                                }
                            } else {
                                myCallback1.onSuccess();
                            }
                        }
                    });
                } else {
                    initCountDownTimer(secondsRemaining, myCallback1);
                    appOpenAdManager.loadAdSplash(BaseAdActivity.this, new CastTvAppOpenCallBackListener() {
                        @Override
                        public void callbackCall(int i) {
                            mCountDownTimer.cancel();
                            if (i == 0) {
                                //On Ads Fail
                                new Handler(getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (app_customAdStatus == 1 && app_customAppOpenStatus == 1 && myAppMarketingList.size() > 0) {
                                            CastTvCustomModel customModel = CastTvAppManager.getInstance(BaseAdActivity.this).getMyCustomAd();
                                            if (customModel != null) {
                                                CustomAppOpenAdCastTv.newIntent(BaseAdActivity.this, myCallback1, customModel);
                                            } else {
                                                myCallback1.onSuccess();
                                            }
                                        } else {
                                            myCallback1.onSuccess();
                                        }
                                    }
                                }, 1500);
                            } else {
                                //On Ads Load
                                appOpenAdManager.showAdIfAvailable(BaseAdActivity.this, new MyApplication.OnShowAdCompleteListener() {
                                    @Override
                                    public void onShowAdComplete() {
                                        myCallback1.onSuccess();
                                    }

                                    @Override
                                    public void onShowAdFail() {
                                        if (app_customAdStatus == 1 && app_customAppOpenStatus == 1 && myAppMarketingList.size() > 0) {
                                            CastTvCustomModel customModel = CastTvAppManager.getInstance(BaseAdActivity.this).getMyCustomAd();
                                            if (customModel != null) {
                                                CustomAppOpenAdCastTv.newIntent(BaseAdActivity.this, myCallback1, customModel);
                                            } else {
                                                myCallback1.onSuccess();
                                            }
                                        } else {
                                            myCallback1.onSuccess();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }

                if (app_forwardInterstitial != 3 || app_backwardInterstitial != 3) {
                    CastTvAppManager.getInstance(BaseAdActivity.this).loadInterstitialAd(BaseAdActivity.this);
                }

                if (app_forwardInterstitial == 2 || app_backwardInterstitial == 2) {
                    CastTvAppManager.getInstance(BaseAdActivity.this).loadAppOpenAd(BaseAdActivity.this);
                }

                if (app_nativeBig != 3) {
                    if (app_nativeBig == 1) {
                        CastTvAppManager.getInstance(BaseAdActivity.this).preLoadFirstNativeAds(BaseAdActivity.this, 1);
                        CastTvAppManager.getInstance(BaseAdActivity.this).preLoadNativeAds(BaseAdActivity.this, 1);
                    } else {
                        CastTvAppManager.getInstance(BaseAdActivity.this).preLoadFirstMediumBannerAds(BaseAdActivity.this);
                        CastTvAppManager.getInstance(BaseAdActivity.this).preLoadMediumBannerAds(BaseAdActivity.this);
                    }
                }

                if (app_nativeBanner != 3) {
                    if (app_nativeBanner == 1) {
                        CastTvAppManager.getInstance(BaseAdActivity.this).preLoadNativeBannerAds(BaseAdActivity.this);
                    } else {
                        CastTvAppManager.getInstance(BaseAdActivity.this).preLoadSmartBannerAds(BaseAdActivity.this);
                    }
                }

            } else {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myCallback1.onSuccess();
                    }
                }, 2500);
            }
        } catch (Exception E) {

        }
    }

    @Override
    protected void onPause() {
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    private void initCountDownTimer(long seconds, final SuccessListener myCallback1) {
        mCountDownTimer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining = ((millisUntilFinished / 1000) + 1);
            }

            @Override
            public void onFinish() {
                secondsRemaining = 0;
                appOpenAdManager.loadAd(mActivity);
                myCallback1.onSuccess();

            }
        };
        mCountDownTimer.start();
    }

    private boolean isUpdateAvailable(int currentVersion, String updateVersionCode) {
        try {
            if (currentVersion < Integer.parseInt(updateVersionCode)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "VPN" + "";
            String description = "VPN" + "" + "notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getPackageName(), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public SharedPreferences getPrefs() {
        return getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public void initHydraSdk() {
        if (mSdk != null) mSdk = null;

        mCarrierArray = new ArrayList<String>(Arrays.asList(app_vpnCarrierId.split(",")));

        createNotificationChannel();
        ClientInfo clientInfo = ClientInfo.newBuilder()
                .addUrl(app_vpnBaseUrl)
                .carrierId(getCarrierFromList(mCarrierArray))
                .build();
        List<TransportConfig> transportConfigList = new ArrayList<>();
        transportConfigList.add(HydraTransportConfig.create());
        transportConfigList.add(OpenVpnTransportConfig.tcp());
        transportConfigList.add(OpenVpnTransportConfig.udp());
        UnifiedSDK.update(transportConfigList, CompletableCallback.EMPTY);
        UnifiedSDKConfig config = UnifiedSDKConfig.newBuilder().idfaEnabled(false).build();
        UnifiedSDK.clearInstances();
        mSdk = UnifiedSDK.getInstance(clientInfo, config);
        NotificationConfig notificationConfig = NotificationConfig.newBuilder()
                .title("VPN")
                .channelId(getPackageName())
                .build();
        UnifiedSDK.update(notificationConfig);
        UnifiedSDK.setLoggingLevel(Log.VERBOSE);
    }

    public void loginToVpn(JsonObject admobJsonObject, SuccessListener myCallback1) {
        AuthMethod authMethod = AuthMethod.anonymous();
        UnifiedSDK.getInstance().getBackend().login(authMethod, new com.anchorfree.vpnsdk.callbacks.Callback<User>() {
            @Override
            public void success(@NonNull User user) {
                isConnected(new com.anchorfree.vpnsdk.callbacks.Callback<Boolean>() {
                    @Override
                    public void success(@NonNull Boolean aBoolean) {
                        if (aBoolean) {
                            startService(new Intent(getBaseContext(), VService.class));
                            loadSplashAd(admobJsonObject, myCallback1);
                        } else {
                            connectToVpn(admobJsonObject, myCallback1);
                        }
                    }

                    @Override
                    public void failure(@NonNull VpnException e) {
                        SharedPreferences prefs = getPrefs();
                        prefs.edit().putInt(mPreferenceCarrierKey, prefs.getInt(mPreferenceCarrierKey, 0) + 1).apply();
                        try {
                            initHydraSdk();
                            loginToVpn(admobJsonObject, myCallback1);
                        } catch (Exception e2) {
                            loadSplashAd(admobJsonObject, myCallback1);
                        }
                        handleError(e, "loginToVpn isConnected failure");
                    }
                });
            }

            @Override
            public void failure(@NonNull VpnException e) {
                if (UnifiedSDK.getInstance().getBackend().isLoggedIn()) {
                    logOutFromVnp(admobJsonObject, myCallback1);
                } else {
                    SharedPreferences prefs = getPrefs();
                    if (prefs.getInt(mPreferenceCarrierKey, 0) == mCarrierArray.size() - 1 && e.getMessage().contains("carrier_id field is invalid")) {
                        loadSplashAd(admobJsonObject, myCallback1);
                        return;
                    }
                    prefs.edit().putInt(mPreferenceCarrierKey, prefs.getInt(mPreferenceCarrierKey, 0) + 1).apply();
                    try {
                        initHydraSdk();
                        loginToVpn(admobJsonObject, myCallback1);
                    } catch (Exception e2) {
                        loadSplashAd(admobJsonObject, myCallback1);
                    }
                    handleError(e, "loginToVpn failure else");
                }

            }
        });


    }

    private void logOutFromVnp(JsonObject admobJsonObject, SuccessListener myCallback1) {
        UnifiedSDK.getInstance().getBackend().logout(new CompletableCallback() {
            @Override
            public void complete() {
                loginToVpn(admobJsonObject, myCallback1);
            }

            @Override
            public void error(VpnException e) {
                UnifiedSDK.getVpnState(new com.anchorfree.vpnsdk.callbacks.Callback<VPNState>() {
                    @Override
                    public void success(@NonNull VPNState vpnState) {
                        if (vpnState == VPNState.CONNECTED) {
                            myCallback1.onSuccess();
                        } else {
                            connectToVpn(admobJsonObject, myCallback1);
                        }
                    }

                    @Override
                    public void failure(@NonNull VpnException e) {
                    }
                });
            }
        });
    }

    public void isConnected(com.anchorfree.vpnsdk.callbacks.Callback<Boolean> callback) {
        UnifiedSDK.getVpnState(new com.anchorfree.vpnsdk.callbacks.Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
                callback.success(vpnState == VPNState.CONNECTED);
            }

            @Override
            public void failure(@NonNull VpnException e) {
                callback.success(false);
            }
        });
    }

    public void connectToVpn(JsonObject admobJsonObject, SuccessListener myCallback1) {
        isLoggedIn(new com.anchorfree.vpnsdk.callbacks.Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    mCountryArray = new ArrayList<String>(Arrays.asList(app_vpnCountry.split(",")));
                    List<String> fallbackOrder = new ArrayList<>();
                    fallbackOrder.add(HydraTransport.TRANSPORT_ID);
                    fallbackOrder.add(CaketubeTransport.TRANSPORT_ID_TCP);
                    fallbackOrder.add(CaketubeTransport.TRANSPORT_ID_UDP);
                    List<String> bypassDomains = new LinkedList<>();
                    bypassDomains.add("*facebook.com");
                    bypassDomains.add("*wtfismyip.com");
                    UnifiedSDK.getInstance().getVPN().start(new SessionConfig.Builder()
                            .withReason(TrackingConstants.GprReasons.M_UI)
                            .withTransportFallback(fallbackOrder)
                            .withCountry(getRandomItem(mCountryArray).toString())
                            .withTransport(HydraTransport.TRANSPORT_ID)
                            .addDnsRule(TrafficRule.Builder.bypass().fromDomains(bypassDomains))
                            .build(), new CompletableCallback() {
                        @Override
                        public void complete() {
                            startService(new Intent(getBaseContext(), VService.class));
                            loadSplashAd(admobJsonObject, myCallback1);

                        }

                        @Override
                        public void error(@NonNull VpnException e) {
                            if (VPNCompulsory == 1) {
                                if (mCarrierArray.size() > 1) {
                                    SharedPreferences prefs = getPrefs();
                                    prefs.edit().putInt(mPreferenceCarrierKey, prefs.getInt(mPreferenceCarrierKey, 0) + 1).apply();
                                }
                                initHydraSdk();
                                loginToVpn(admobJsonObject, myCallback1);
                            } else {
                                loadSplashAd(admobJsonObject, myCallback1);
                            }
                        }
                    });
                } else {
                    loadSplashAd(admobJsonObject, myCallback1);
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
                SharedPreferences prefs = getPrefs();
                prefs.edit().putInt(mPreferenceCarrierKey, prefs.getInt(mPreferenceCarrierKey, 0) + 1).apply();
                try {
                    initHydraSdk();
                    loginToVpn(admobJsonObject, myCallback1);
                } catch (Exception e2) {
                    loadSplashAd(admobJsonObject, myCallback1);
                }
                handleError(e, "connectToVpn isLoggedIn failure");
            }
        });
    }


    public void isLoggedIn(com.anchorfree.vpnsdk.callbacks.Callback<Boolean> callback) {
        UnifiedSDK.getInstance().getBackend().isLoggedIn(callback);
    }

    private <T> T getRandomItem(List<T> list) {
        Random random = new Random();
        int listSize = list.size();
        int randomIndex = random.nextInt(listSize);
        return list.get(randomIndex);
    }


    private <T> T getCarrierFromList(List<T> list) {
        int listSize = list.size();
        SharedPreferences prefs = getPrefs();
        if (prefs.getInt(mPreferenceCarrierKey, 0) >= listSize) {
            prefs.edit().putInt(mPreferenceCarrierKey, 0).apply();
        }
        return list.get(prefs.getInt(mPreferenceCarrierKey, 0));
    }


    public void disconnectFromVnp() {
        UnifiedSDK.getInstance().getVPN().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
            @Override
            public void complete() {
            }

            @Override
            public void error(@NonNull VpnException e) {
            }
        });
    }

    public void disconnectFromStartVnp(CastTvVCallbackListener myCallback) {
        UnifiedSDK.getInstance().getVPN().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
            @Override
            public void complete() {
                myCallback.callbackSuccess();
            }

            @Override
            public void error(@NonNull VpnException e) {
                myCallback.callbackFail();
            }
        });
    }

    public void handleError(Throwable e, String msg) {

        if (e instanceof NetworkRelatedException) {
            showMessage("Check internet connection");
        } else if (e instanceof VpnException) {
            if (e instanceof VpnPermissionRevokedException) {
                showMessage("User revoked vpn permissions");
            } else if (e instanceof VpnPermissionDeniedException) {
                showMessage("User canceled to grant vpn permissions");
            } else if (e instanceof HydraVpnTransportException) {
                HydraVpnTransportException hydraVpnTransportException = (HydraVpnTransportException) e;
                if (hydraVpnTransportException.getCode() == HydraVpnTransportException.HYDRA_ERROR_BROKEN) {
                    showMessage("Connection with vpn server was lost");
                } else if (hydraVpnTransportException.getCode() == HydraVpnTransportException.HYDRA_DCN_BLOCKED_BW) {
                    showMessage("Client traffic exceeded");
                } else {
                    showMessage("Error in VPN transport");
                }
            } else {
            }
        }
    }

    public void showMessage(String msg) {
        Toast.makeText(BaseAdActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void checkProxyCountry(JsonObject admobJsonObject, SuccessListener myCallback1) {
        if (app_vpnEnable == 0) {
            isBlockedVPN = true;
            if (InterclickCountry == -1) {
                app_adShowStatus = 0;
            }
            loadSplashAd(admobJsonObject, myCallback1);
        } else {
            isBlockedVPN = false;
            startAppWithVPN(admobJsonObject, myCallback1);
        }
    }

    private void startAppWithVPN(JsonObject admobJsonObject, SuccessListener myCallback1) {
        SharedPreferences prefs = getPrefs();
        prefs.edit().putInt(mPreferenceCarrierKey, 0).apply();
        try {
            initHydraSdk();
            loginToVpn(admobJsonObject, myCallback1);
        } catch (Exception e) {
            loadSplashAd(admobJsonObject, myCallback1);
        }
    }

}