package com.adsdemo.vdapps.adsload.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.adsdemo.vdapps.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.adsdemo.vdapps.adsload.interfaces.MyCallback;
import com.adsdemo.vdapps.adsload.models.CustomAdModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class Ad_CustomAppopenActivity extends AppCompatActivity {

    private ImageView ad_media_view;
    private RelativeLayout int_bg;
    private TextView txt_title;
    private TextView txt_body;
    private TextView txt_rate;
    private TextView txt_download;
    public static CustomAdModel customAdModel;
    public static MyCallback myCallback;
    private TextView native_ad_call_to_action;
    private RatingBar ad_stars;
    private LinearLayout ll_close;
    public static int random = 0;
    private LinearLayout userCount, adPersonalLlPlayStore;
    private String action_str;
    private boolean onClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_custom_appopen);


        if (customAdModel != null) {
//            try {
            ad_media_view = (ImageView) findViewById(R.id.native_ad_media);
            txt_title = (TextView) findViewById(R.id.native_ad_title);
            txt_body = (TextView) findViewById(R.id.native_ad_social_context);
            txt_rate = (TextView) findViewById(R.id.txt_rate);
            txt_download = (TextView) findViewById(R.id.txt_download);
            native_ad_call_to_action = (TextView) findViewById(R.id.native_ad_call_to_action);
            ((TextView) findViewById(R.id.native_ad_call_to_action)).setBackgroundColor(Color.parseColor("#" + AdsManager.nativebutton));
            ((TextView) findViewById(R.id.native_ad_call_to_action)).setBackgroundColor(Color.parseColor("#" + AdsManager.NativeButton));
            ((TextView) findViewById(R.id.native_ad_call_to_action)).setTextColor(Color.parseColor("#" + AdsManager.NativeButtonText));

            ll_close = (LinearLayout) findViewById(R.id.ll_close);
            userCount = (LinearLayout) findViewById(R.id.userCount);
            adPersonalLlPlayStore = (LinearLayout) findViewById(R.id.adPersonalLlPlayStore);
            ad_stars = (RatingBar) findViewById(R.id.ad_stars);
            int_bg = findViewById(R.id.int_bg);
            Glide.with(this).load(customAdModel.getApp_logo()).diskCacheStrategy(DiskCacheStrategy.ALL).into((ImageView) findViewById(R.id.native_ad_icon));


            txt_title.setText(customAdModel.getAppName().split("/")[0]);
            txt_body.setText(customAdModel.getApp_shortDecription());
            txt_rate.setText(customAdModel.getApp_rating());
            ad_stars.setRating(Float.parseFloat(customAdModel.getApp_rating()));
            txt_download.setText(customAdModel.getApp_download());


            action_str = customAdModel.getAppPackageName();
            if (action_str.contains("http")) {
                userCount.setVisibility(View.GONE);

                ll_close.setVisibility(View.GONE);
                adPersonalLlPlayStore.setVisibility(View.GONE);
                if (random == 1) {
                    native_ad_call_to_action.setText("Play Game");
                } else {
                    native_ad_call_to_action.setText("Play Now");
                }

            } else {
                userCount.setVisibility(View.VISIBLE);

                ll_close.setVisibility(View.VISIBLE);
                adPersonalLlPlayStore.setVisibility(View.VISIBLE);
                if (random == 1) {
                    native_ad_call_to_action.setText("Install");
                } else {
                    native_ad_call_to_action.setText("Download");
                }
            }
            SlideToTop(findViewById(R.id.native_ad_icon), 500);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SlideToAbove(findViewById(R.id.native_ad_title), 300);
                    SlideToAbove(findViewById(R.id.native_ad_social_context), 600);
                    action_str = customAdModel.getAppPackageName();
                    if (action_str.contains("http")) {
                        SlideToAbove(findViewById(R.id.querkaText), 450);
                    } else {
                        SlideToAbove(findViewById(R.id.userCount), 400);
                        SlideToAbove(findViewById(R.id.adPersonalLlPlayStore), 700);
                    }
                }
            }, 400);

            findViewById(R.id.native_ad_call_to_action).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    action_str = customAdModel.getAppPackageName();
                    if (action_str.contains("http")) {
                        AdsManager.openChromeCustomTabUrl(Ad_CustomAppopenActivity.this, customAdModel.getAppPackageName());
                    } else {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + action_str)));
                    }
                }
            });
            ll_close.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    closeAd();
                }
            });
        } else {
            closeAd();
        }
    }

    private void closeAd() {
        finish();
        if (myCallback != null) {
            myCallback.callbackCall();
            myCallback = null;
        }
    }

    public void SlideToAbove(final View view, int time) {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                5.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        slide.setDuration(time);
        slide.setFillAfter(true);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        slide.setFillEnabled(true);
        view.startAnimation(slide);
    }

    public void SlideToTop(final View view, int time) {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -2.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        slide.setDuration(time);
        slide.setFillAfter(true);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        slide.setFillEnabled(true);
        view.startAnimation(slide);
    }

    @Override
    public void onBackPressed() {

    }

    public static void newIntent(Activity activity, MyCallback Callback, CustomAdModel AdModel) {
        customAdModel = AdModel;
        myCallback = Callback;
        activity.startActivity(new Intent(activity, Ad_CustomAppopenActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
