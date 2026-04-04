package com.colorcallscreen.colorphone.callscreen.calltheme.Splash;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.MainActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PermissionCenter;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.BuildConfig;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.ReqPermissionActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

public class StartActivity extends AppCompatActivity {
    LinearLayout adsCard;

    private String idInter = "";
    private ApInterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);

        configMediationProvider();
        loadAdInterstitial();

        adsCard = findViewById(R.id.adsCard);

        adsCard.setVisibility(View.VISIBLE);
        FrameLayout flPlaceHolder = findViewById(com.ads.control.R.id.fl_adplaceholder);
        ShimmerFrameLayout shimmerFrameLayout = findViewById(com.ads.control.R.id.shimmer_container_native);
        AperoAd.getInstance().loadNativeAd(StartActivity.this, getResources().getString(R.string.admob_native), com.ads.control.R.layout.custom_native_admob_free_size, flPlaceHolder, shimmerFrameLayout);

        findViewById(R.id.s_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isReady()) {

                    AperoAd.getInstance().showInterstitialAdByTimes(StartActivity.this, mInterstitialAd, new AperoAdCallback() {
                        @Override
                        public void onNextAction() {
                            callNext();
                        }

                        @Override
                        public void onAdFailedToShow(@Nullable ApAdError adError) {
                            super.onAdFailedToShow(adError);
                        }

                        @Override
                        public void onInterstitialShow() {
                            super.onInterstitialShow();
                        }
                    }, true);
                } else {
                    callNext();
                }


            }
        });
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });
        findViewById(R.id.privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=pub%3ASakshat%20Infotech&c=apps&hl=en_US&gl=US")));

            }
        });
    }

    private void callNext() {
        if (!getSharedPreferences(getPackageName(), MODE_PRIVATE).getBoolean("apppermi", false)) {
            Log.e("===", "onCreate: Api Call ");
            startActivity(new Intent(StartActivity.this, PermissionActivity.class));
        } else {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
        }
//        BoloPermission boloPermission = new BoloPermission(StartActivity.this);
//        if (Build.VERSION.SDK_INT >= 26 || boloPermission.isAllPermissionGranted(Utility.isAppDefaultSet(StartActivity.this)) || PermissionCenter.isAccessibilityEnabled(StartActivity.this) || PermissionCenter.isOverlayPermissionEnabled(StartActivity.this)) {
//            Log.e("===", "if callNext: " );
//            startActivity(new Intent(StartActivity.this, ReqPermissionActivity.class));
//        }else {
//            Log.e("===", "else callNext: " );
//            Intent intent = new Intent(StartActivity.this, MainActivity.class);
//            startActivity(intent);
//        }

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
        startActivity(new Intent(StartActivity.this, BackActivity.class));
    }
}