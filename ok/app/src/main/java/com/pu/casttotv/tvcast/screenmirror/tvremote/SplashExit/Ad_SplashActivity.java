package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.Globle.Ad_Permission;
import com.adsdemo.vdapps.adsload.Ad_SplashLoad;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.adsdemo.vdapps.adsload.interfaces.MyCallback;
import com.adsdemo.vdapps.adsload.interfaces.getDataListner;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.MainActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.SharedPrefsUtil;

import java.io.File;
import java.util.Locale;


public class Ad_SplashActivity extends Ad_SplashLoad {
    SharedPreferences.Editor myEdit;
    boolean jumpool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        language();
        setContentView(R.layout.ad_activity_splash);

        ADSinit(this, 0, new getDataListner() {
            @Override
            public void onSuccess() {
                AdsManager.displaySplashAd(Ad_SplashActivity.this, new MyCallback() {
                    @Override
                    public void callbackCall() {
                        AdsManager.isSplash=false;
                       gotoHome();
                    }
                });

            }
        });
    }

    public final void language() {
        try {
            String str = (String) SharedPrefsUtil.getInstance().get("KEY_LANGUAGE_SAVE", String.class);
            Locale locale = new Locale(str);
            Resources resources = getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, displayMetrics);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void gotoHome() {
        File sub = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Fake Video Call");
        if (!sub.exists()) {
            sub.mkdirs();
        }
        SharedPreferences sharedPreferences2 = getSharedPreferences("MySharedPref2", MODE_PRIVATE);
        int a = sharedPreferences2.getInt("chackRate", 0);
        Log.e("TAG1", "chackRatechackRatechackRatechackRate: " + a);

        if (a < 3) {
            SharedPreferences.Editor myEdit = sharedPreferences2.edit();
            myEdit.putInt("chackRate", a + 1);
            myEdit.commit();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        jumpool = sharedPreferences.getBoolean("chack", false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!jumpool) {
                   if (AdsManager.PrivacyPolicyScreen == 1) {
                        startActivity(new Intent(Ad_SplashActivity.this, Ad_PrivacyPolicyActivity.class));
                        finish();
                    } else if (AdsManager.PermissionScreen == 1) {
                       if (!Ad_Permission.isPermissionGranted(Ad_SplashActivity.this)/*||!Ad2_Permission.isGloblePermissionGranted(SplashActivity.this)*/) {
                           startActivity(new Intent(Ad_SplashActivity.this, Ad_PermissionActivity.class));
                           finish();
                       }else if (AdsManager.CountryScreen == 1) {
                           startActivity(new Intent(Ad_SplashActivity.this, Ad_CountryActivity.class));
                           finish();
                       } else if (AdsManager.LanguageScreen == 1) {
                           startActivity(new Intent(Ad_SplashActivity.this, Ad_LanguageActivity.class));
                           finish();
                       } else if (AdsManager.SwipeScreen == 1) {
                           startActivity(new Intent(Ad_SplashActivity.this, Ad_SwipeScreenActivity.class));
                           finish();
                       } else if (AdsManager.AppPurchaseScreen == 1) {
                           startActivity(new Intent(Ad_SplashActivity.this, Ad_AppPurchaseActivity.class));
                           finish();
                       } else {
                           startActivity(new Intent(Ad_SplashActivity.this, MainActivity.class));
                           finish();
                       }
                    } else if (AdsManager.CountryScreen == 1) {
                        startActivity(new Intent(Ad_SplashActivity.this, Ad_CountryActivity.class));
                        finish();
                    } else if (AdsManager.LanguageScreen == 1) {
                        startActivity(new Intent(Ad_SplashActivity.this, Ad_LanguageActivity.class));
                        finish();
                    } else if (AdsManager.SwipeScreen == 1) {
                        startActivity(new Intent(Ad_SplashActivity.this, Ad_SwipeScreenActivity.class));
                        finish();
                    } else if (AdsManager.AppPurchaseScreen == 1) {
                        startActivity(new Intent(Ad_SplashActivity.this, Ad_AppPurchaseActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(Ad_SplashActivity.this, MainActivity.class));
                        finish();
                    }
                } else {
                        startActivity(new Intent(Ad_SplashActivity.this, MainActivity.class));
                        finish();
                }
            }
        }, 10);
    }

    @Override
    public void onBackPressed() {

    }
}