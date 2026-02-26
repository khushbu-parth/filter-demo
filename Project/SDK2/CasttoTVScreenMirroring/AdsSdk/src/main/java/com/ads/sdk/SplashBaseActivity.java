package com.ads.sdk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.ads.sdk.adsClass.AppOpenAdManager;
import com.ads.sdk.adsClass.NativeLoader;
import com.ads.sdk.apiData.APIClient;
import com.ads.sdk.apiData.APIInterface;
import com.ads.sdk.configs.Config;
import com.ads.sdk.configs.PreferenceManager;
import com.ads.sdk.interfaces.OnShowAdCompleteListener;
import com.ads.sdk.services.VpnService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class SplashBaseActivity extends AppCompatActivity {
    private static final String TAG = SplashBaseActivity.class.getName();

    public abstract void onComplete();

    public void loadSplash(Boolean debug, int versionCode, String appId) {
        Config.DEBUG = debug;
        if (!Config.isNetworkAvailable(SplashBaseActivity.this)) {
            new AlertDialog.Builder(SplashBaseActivity.this)
                    .setTitle("No Internet")
                    .setCancelable(false)
                    .setMessage("Please check your internet connection and try again.")
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            loadSplash(debug, versionCode, appId);
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            SdkManager.finalExit(SplashBaseActivity.this);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            if (!Config.isServiceRunning(SplashBaseActivity.this, VpnService.class)) {
                startService(new Intent(SplashBaseActivity.this, VpnService.class));
            }

            Config.log(TAG, getApplicationContext().getPackageName());
            APIInterface apiInterface = APIClient.getClient(getApplicationContext().getPackageName()).create(APIInterface.class);
            Call<JsonObject> call = apiInterface.doCall(appId, getApplicationContext().getPackageName(), PreferenceManager.getInstallType(), Config.buildMode(debug));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null && response.body().get("status").getAsBoolean()) {
                        PreferenceManager.setAdsResponse(response.body().get("data").getAsJsonObject());
                        if (PreferenceManager.getInstallType().equals("1")) {
                            PreferenceManager.setInstallType();
                        }
                    }

                    if (PreferenceManager.getAppUpdateFlag() && PreferenceManager.getUpdateVersionCode() > versionCode) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashBaseActivity.this);
                        alertDialog.setTitle("New Version Available");
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage("It looks like you have an old version of the app, Please update the app and enjoy our latest features.");
                        alertDialog.setPositiveButton("Update now", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Config.rateUs(SplashBaseActivity.this);
                                SdkManager.finalExit(SplashBaseActivity.this);
                                dialog.dismiss();
                            }
                        });
                        alertDialog.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                loadAd();
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        loadAd();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Config.log(TAG, "loadSplash => onFailure: " + t.toString());
                    loadAd();
                }
            });
        }
    }

    private void loadAd() {
        if (Config.isNetworkAvailable(SplashBaseActivity.this) && PreferenceManager.getAdsFlag() && !PreferenceManager.getAppOpenID().isEmpty()) {
            SdkManager.loadInterstitialAd(SplashBaseActivity.this);
            NativeLoader loader = new NativeLoader();
            loader.loadBanner(SplashBaseActivity.this);
            loader.loadNative(SplashBaseActivity.this);
            loader.loadNativeBanner(SplashBaseActivity.this);
            new AppOpenAdManager().loadSplashAd(SplashBaseActivity.this, new OnShowAdCompleteListener() {
                @Override
                public void onShowAdComplete() {
                    onComplete();
                }
            });
        } else {
            onComplete();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
