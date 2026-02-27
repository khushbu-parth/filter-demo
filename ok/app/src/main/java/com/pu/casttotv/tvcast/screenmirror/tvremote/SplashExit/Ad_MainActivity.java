package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.pu.casttotv.tvcast.screenmirror.tvremote.BuildConfig;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.Ad_Dialogs;
import com.adsdemo.vdapps.adsload.Ad_Globals;

import com.adsdemo.vdapps.adsload.AdsManager;
import com.adsdemo.vdapps.adsload.MoreApps.Ad_PlayStoreActivity;
import com.adsdemo.vdapps.adsload.interfaces.MyCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.Globle.Ad_Dialog;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.Globle.Ad_MainLoad;

public class Ad_MainActivity extends Ad_MainLoad {
    private DrawerLayout mDrawerLayout;
    LinearLayout llApplayout, llSliderad;
    ImageView ivFloat, ivAppicon;
    int count = 0;
    TextView tvAppname;

    private FrameLayout iv_adtop;
    private ImageView ivInAppPurchase;
    private Thread thread;
    private TextView txtVersion;
    private TextView tv_countslider;
    SharedPreferences.Editor myEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_main);
        AdsManager.CallBannerAds(this, findViewById(R.id.ad_view));
        AdsManager.CallNativeAdLoad(this, findViewById(R.id.native_container), AdsManager.NATIVE_SMALL);

        ADCommanMethod();
        Ad_Dialogs.setRateDialog(this);
    }

    private void ADCommanMethod() {
        ivInAppPurchase = findViewById(R.id.ivInAppPurchase);
        iv_adtop = findViewById(R.id.iv_adtop);
        ivFloat = findViewById(R.id.ivFloat);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        tv_countslider = findViewById(R.id.tv_countslider);
        txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText("Version " + BuildConfig.VERSION_NAME + "");
        tvAppname = findViewById(R.id.tv_appname);
        ivAppicon = findViewById(R.id.iv_appicon);
        llApplayout = findViewById(R.id.ll_applayout);
        llSliderad = findViewById(R.id.ll_sliderad);

        if (AdsManager.moreAllList != null && AdsManager.moreAllList.size() != 0) {
            iv_adtop.setVisibility(View.VISIBLE);
            llApplayout.setVisibility(View.VISIBLE);
            llSliderad.setVisibility(View.VISIBLE);
        } else {
            iv_adtop.setVisibility(View.GONE);
            llApplayout.setVisibility(View.GONE);
            llSliderad.setVisibility(View.GONE);
        }
        if (AdsManager.AppPurchaseScreen == 1) {
            ivInAppPurchase.setVisibility(View.VISIBLE);
        } else {
            ivInAppPurchase.setVisibility(View.GONE);
        }

        iv_adtop.setOnClickListener(v -> {
            AdsManager.CallInterstitialAdLoad(Ad_MainActivity.this, 0, new MyCallback() {
                @Override
                public void callbackCall() {
                    startActivity(new Intent(Ad_MainActivity.this, Ad_PlayStoreActivity.class));
                }
            });
        });

        ivInAppPurchase.setOnClickListener(v -> {
            AdsManager.CallInterstitialAdLoad(Ad_MainActivity.this, 0, new MyCallback() {
                @Override
                public void callbackCall() {
                    startActivity(new Intent(Ad_MainActivity.this, Ad_AppPurchaseActivity.class));
                }
            });
        });
        if (AdsManager.moreAllList == null || AdsManager.moreAllList.size() <= 0) {
            llApplayout.setVisibility(View.GONE);
            llSliderad.setVisibility(View.GONE);
        } else {
            llApplayout.setVisibility(View.VISIBLE);
            llSliderad.setVisibility(View.VISIBLE);
        }

        ivFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        llSliderad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Ad_MainActivity.this, Ad_PlayStoreActivity.class));
            }
        });
        llApplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + AdsManager.moreAllList.get(count).app_packageName);
                    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(myAppLinkToMarket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

                try {
                    thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                while (!thread.isInterrupted()) {
                                    Thread.sleep(2000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (AdsManager.moreAllList != null) {
                                                count++;
                                                if (count == AdsManager.moreAllList.size()) {
                                                    count = 0;
                                                }
                                            }

                                            if (AdsManager.moreAllList.size() > 0) {
                                                ((TextView) findViewById(R.id.tv_appname)).setText(AdsManager.moreAllList.get(count).app_name);
                                                Glide.with(Ad_MainActivity.this)
                                                        .load(AdsManager.moreAllList.get(count).app_logo)
                                                        .apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher))
                                                        .into((ImageView) findViewById(R.id.iv_appicon));
                                            }
                                        }
                                    });
                                }
                            } catch (InterruptedException e) {
                            }
                        }
                    };
                    thread.start();


                    if (AdsManager.moreAllList == null || AdsManager.moreAllList.size() == 0) {
                        tvAppname.setText(getResources().getString(R.string.app_name));
                        Glide.with(Ad_MainActivity.this)
                                .load(R.mipmap.ic_launcher)
                                .apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher))
                                .into((ImageView) findViewById(R.id.iv_appicon));
                    } else {
                        tvAppname.setText(AdsManager.moreAllList.get(count).app_name);
                        Glide.with(Ad_MainActivity.this)
                                .load(AdsManager.moreAllList.get(count).app_logo)
                                .apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher))
                                .into((ImageView) findViewById(R.id.iv_appicon));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                try {
                    thread.interrupt();
                    if (AdsManager.moreAllList != null) {
                        count++;
                        if (count == AdsManager.moreAllList.size()) {
                            count = 0;

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        boolean a = sharedPreferences.getBoolean("chack", false);
        Log.e("sssss", "ssss: " + a);

        if (!a) {
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putBoolean("chack", true);
            myEdit.commit();
        }

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            int i = Ad_Globals.getRateClick(this, "checkRateClick");
            if (i == 0) {
                Ad_Dialog.setExitRateDialog(this);
                int newi = (i + 1);
                Ad_Globals.setRateClick(this, newi, "checkRateClick");
            } else {
                if (AdsManager.ExitScreen == 1) {
                    startActivity(new Intent(Ad_MainActivity.this, Ad_ExitActivity.class));
                } else {
                    Ad_Dialogs.setExitDialog(this);
                }
                if (i == 3) {
                    Ad_Globals.setRateClick(this, 0, "checkRateClick");
                    return;
                }
                int newi = (i + 1);
                Ad_Globals.setRateClick(this, newi, "checkRateClick");
            }


        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        AdsManager.pref = getSharedPreferences("counter", Context.MODE_PRIVATE);
        AdsManager.finalAppCouner = AdsManager.pref.getInt("lang_us", 0);
        if (AdsManager.finalAppCouner > 0) {
            AdsManager.finalAppCouner--;
            tv_countslider.setText(String.valueOf(AdsManager.finalAppCouner));
        } else {
            tv_countslider.setText(String.valueOf(AdsManager.totalAppCouner - 1));
        }
        AdsManager.show_gift_header(this, (FrameLayout) findViewById(R.id.iv_adtop), (TextView) findViewById(R.id.tv_counttop), View.INVISIBLE);
    }
}