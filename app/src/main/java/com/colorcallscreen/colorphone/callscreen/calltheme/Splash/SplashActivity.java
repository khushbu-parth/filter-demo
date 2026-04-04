package com.colorcallscreen.colorphone.callscreen.calltheme.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ads.control.ads.AperoAd;
import com.ads.control.ads.AperoAdCallback;
import com.ads.control.ads.AperoInitCallback;
import com.ads.control.ads.wrapper.ApAdError;
import com.ads.control.ads.wrapper.ApNativeAd;
import com.ads.control.billing.AppPurchase;
import com.ads.control.config.AperoAdConfig;
import com.ads.control.funtion.BillingListener;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;


public class SplashActivity extends AppCompatActivity {
    private String idAdSplash;
    public String ad_interstitial_splash;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        ad_interstitial_splash = getResources().getString(R.string.admob_inter_id);

        if (AperoAd.getInstance().getMediationProvider() == AperoAdConfig.PROVIDER_ADMOB)
            idAdSplash = ad_interstitial_splash;
        else
            idAdSplash = "c630fe3686063741";
        AppPurchase.getInstance().setBillingListener(new BillingListener() {
            @Override
            public void onInitBillingFinished(int code) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadSplash();
                    }
                });
            }
        }, 3000);
    }

    private String TAG = "Splash_A" + "ctivity";
    AperoAdCallback adCallback = new AperoAdCallback() {
        @Override
        public void onAdFailedToLoad(@Nullable ApAdError i) {
            super.onAdFailedToLoad(i);
            Log.d(TAG, "onAdLoaded");
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            Log.d(TAG, "onAdLoaded");
        }

        @Override
        public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
            super.onNativeAdLoaded(nativeAd);
        }

        @Override
        public void onNextAction() {
            super.onNextAction();
            Log.d(TAG, "onNextAction");
            goto_act();
        }

        @Override
        public void onAdSplashReady() {
            super.onAdSplashReady();
        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            Log.d(TAG, "onAdClosed");
        }
    };

    private void loadSplash() {
        Log.d(TAG, "onCreate: show splash ads");
        AperoAd.getInstance().setInitCallback(new AperoInitCallback() {
            @Override
            public void initAdSuccess() {
                AperoAd.getInstance().loadSplashInterstitialAds(SplashActivity.this,
                        idAdSplash, 30000, 5000, true, adCallback);
            }
        });
    }

    private void goto_act() {
        startActivity(new Intent(SplashActivity.this, StartActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "Splash onPause: ");
        AperoAd.getInstance().onCheckShowSplashWhenFail(this, adCallback, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "Splash onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "Splash onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }
}
