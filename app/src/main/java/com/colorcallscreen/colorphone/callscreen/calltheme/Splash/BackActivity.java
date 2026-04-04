package com.colorcallscreen.colorphone.callscreen.calltheme.Splash;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ads.control.ads.AperoAd;
import com.ads.control.ads.AperoAdCallback;
import com.ads.control.ads.wrapper.ApAdError;
import com.ads.control.ads.wrapper.ApInterstitialAd;
import com.ads.control.config.AperoAdConfig;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.facebook.shimmer.ShimmerFrameLayout;

public class BackActivity extends AppCompatActivity {
    LinearLayout adsCard;
    private String idInter = "";
    private ApInterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);

        adsCard = findViewById(R.id.adsCard);

        adsCard.setVisibility(View.VISIBLE);
        FrameLayout flPlaceHolder = findViewById(com.ads.control.R.id.fl_adplaceholder);
        ShimmerFrameLayout shimmerFrameLayout = findViewById(com.ads.control.R.id.shimmer_container_native);
        AperoAd.getInstance().loadNativeAd(BackActivity.this, getResources().getString(R.string.admob_native), com.ads.control.R.layout.custom_native_admob_free_size, flPlaceHolder, shimmerFrameLayout);
        configMediationProvider();
        loadAdInterstitial();

        adsCard.setVisibility(View.VISIBLE);
        findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isReady()) {

                    AperoAd.getInstance().showInterstitialAdByTimes(BackActivity.this, mInterstitialAd, new AperoAdCallback() {
                        @Override
                        public void onNextAction() {
                            Log.i("TAG", "onNextAction: start content and finish main");
                            startActivity(new Intent(BackActivity.this, ExitActivity.class));
                        }

                        @Override
                        public void onAdFailedToShow(@Nullable ApAdError adError) {
                            super.onAdFailedToShow(adError);
                            Log.i("TAG", "onAdFailedToShow:" + adError.getMessage());
                        }

                        @Override
                        public void onInterstitialShow() {
                            super.onInterstitialShow();
                            Log.d("TAG", "onInterstitialShow");
                        }
                    }, true);
                } else {
                    startActivity(new Intent(BackActivity.this, ExitActivity.class));
                }
            }
        });
        findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BackActivity.this, StartActivity.class));
            }
        });
    }
    private void configMediationProvider() {
        if (AperoAd.getInstance().getMediationProvider() == AperoAdConfig.PROVIDER_ADMOB) {
            idInter = getResources().getString(R.string.admob_inter_id);
        } else {
            idInter = "c630fe3686063741";
        }
    }

    private void loadAdInterstitial() {

        mInterstitialAd = AperoAd.getInstance().getInterstitialAds(this, idInter);
    }
    @Override
    public void onBackPressed() {
    }
}