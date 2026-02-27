package com.adsdemo.vdapps.adsload.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.adsdemo.vdapps.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.adsdemo.vdapps.adsload.AdsTimer;
import com.adsdemo.vdapps.adsload.interfaces.MyCallback;
import com.adsdemo.vdapps.adsload.models.CustomAdModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.concurrent.TimeUnit;

public class Ad_CustomInterstitialActivity extends AppCompatActivity {
    private static AdsTimer myadsTimer;
    private static AdsTimer myadsTimer2;
    private static int myType;
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
    private LinearLayout adPersonalCloseBtn;
    public static int random = 0;
    private LinearLayout llPersonalAd, llPersonalAdCenter;
    private LinearLayout userCount, adPersonalLlPlayStore;
    private TextView querkaText;
    private String action_str;
    private boolean onClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (random == 2) {
            setContentView(R.layout.ad_cust_int2);
        } else if (random == 1) {
            setContentView(R.layout.ad_cust_int1);
        } else {
            setContentView(R.layout.ad_cust_int);
        }

        llPersonalAd = (LinearLayout) findViewById(R.id.llPersonalAd);
        llPersonalAdCenter = (LinearLayout) findViewById(R.id.llPersonalAdCenter);

        if (customAdModel != null) {
            try {
                ad_media_view = (ImageView) findViewById(R.id.native_ad_media);
                txt_title = (TextView) findViewById(R.id.native_ad_title);
                txt_body = (TextView) findViewById(R.id.native_ad_social_context);
                txt_rate = (TextView) findViewById(R.id.txt_rate);
                txt_download = (TextView) findViewById(R.id.txt_download);
                native_ad_call_to_action = (TextView) findViewById(R.id.native_ad_call_to_action);
                ((TextView) findViewById(R.id.native_ad_call_to_action)).setBackgroundColor(Color.parseColor("#" + AdsManager.nativebutton));
                ((TextView) findViewById(R.id.native_ad_call_to_action)).setBackgroundColor(Color.parseColor("#" + AdsManager.NativeButton));
                ((TextView) findViewById(R.id.native_ad_call_to_action)).setTextColor(Color.parseColor("#" + AdsManager.NativeButtonText));

                adPersonalCloseBtn = (LinearLayout) findViewById(R.id.adPersonalCloseBtn);
                userCount = (LinearLayout) findViewById(R.id.userCount);
                adPersonalLlPlayStore = (LinearLayout) findViewById(R.id.adPersonalLlPlayStore);
                querkaText = (TextView) findViewById(R.id.querkaText);
                ad_stars = (RatingBar) findViewById(R.id.ad_stars);
                int_bg = findViewById(R.id.int_bg);
                Glide.with(this)
                        .load(customAdModel.getApp_logo())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into((ImageView) findViewById(R.id.native_ad_icon));

                Glide.with(this)
                        .load(customAdModel.getApp_banner())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ad_media_view);

                txt_title.setText(customAdModel.getAppName().split("/")[0]);
                txt_body.setText(customAdModel.getApp_shortDecription());
                txt_rate.setText(customAdModel.getApp_rating());
                ad_stars.setRating(Float.parseFloat(customAdModel.getApp_rating()));
                txt_download.setText(customAdModel.getApp_download());


                action_str = customAdModel.getAppPackageName();
                if (action_str.contains("http")) {
                    userCount.setVisibility(View.GONE);

                    adPersonalCloseBtn.setVisibility(View.GONE);
                    adPersonalLlPlayStore.setVisibility(View.GONE);
                    if (random == 1) {
                        native_ad_call_to_action.setText("Play Game");
                    } else {
                        native_ad_call_to_action.setText("Play Now");
                    }

                } else {
                    userCount.setVisibility(View.VISIBLE);

                    adPersonalCloseBtn.setVisibility(View.VISIBLE);
                    adPersonalLlPlayStore.setVisibility(View.VISIBLE);
                    if (random == 1) {
                        native_ad_call_to_action.setText("Install");
                    } else {
                        native_ad_call_to_action.setText("Download");
                    }
                }
                if (random == 2) {
                    random = 0;
                    SlideToAbove30(findViewById(R.id.llcus3), 1000);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FadeIn(findViewById(R.id.llPersonalAd));
                            FadeIn(findViewById(R.id.main));
                            FadeIn(findViewById(R.id.aa));
                            action_str = customAdModel.getAppPackageName();
                            if (action_str.contains("http")) {
                                FadeIn(findViewById(R.id.querkaText));
                            } else {
                                FadeIn(findViewById(R.id.adPersonalLlPlayStore));
                            }

                            FadeIn(findViewById(R.id.adPersonalLlCloseInstallBtns));
                        }
                    }, 500);
                } else if (random == 1) {
                    random++;
                    SlideToAbove20(findViewById(R.id.native_ad_icon), 1000);
                    SlideToAbove30(findViewById(R.id.cvTopAd), 1000);


                    action_str = customAdModel.getAppPackageName();
                    if (action_str.contains("http")) {
                        findViewById(R.id.querkaText).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.querkaText).setVisibility(View.GONE);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FadeIn(findViewById(R.id.aa));
                            FadeIn(findViewById(R.id.adPersonalLlCloseInstallBtnsCenter));
                        }
                    }, 1100);
                } else {
                    random++;
                    SlideToTop(findViewById(R.id.native_ad_icon), 500);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SlideToAbove(findViewById(R.id.native_ad_title), 300);
                            SlideToAbove(findViewById(R.id.banner), 500);
                            SlideToAbove(findViewById(R.id.native_ad_social_context), 600);
                            action_str = customAdModel.getAppPackageName();
                            if (action_str.contains("http")) {
                                SlideToAbove(findViewById(R.id.querkaText), 450);
                            } else {
                                SlideToAbove(findViewById(R.id.userCount), 400);
                                SlideToAbove(findViewById(R.id.adPersonalLlPlayStore), 700);
                            }
                            SlideToAbove(findViewById(R.id.adPersonalLlCloseInstallBtns), 800);
                        }
                    }, 400);
                }

                findViewById(R.id.native_ad_call_to_action).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        action_str = customAdModel.getAppPackageName();
                        if (action_str.contains("http")) {
                            AdsManager.openChromeCustomTabUrl(Ad_CustomInterstitialActivity.this, customAdModel.getAppPackageName());
                        } else {
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + action_str)));
                        }
                    }
                });
                findViewById(R.id.ImgClose).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        closeAd();
                    }
                });
                adPersonalCloseBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        closeAd();
                    }
                });
            } catch (Exception e) {
                closeAd();
            }
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

    public void SlideToAbove30(final View view, int time) {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.5f, Animation.RELATIVE_TO_SELF, 0.0f);
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

    public void SlideToAbove20(final View view, int time) {
        Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.ad_zoom_in);
        zoomin.setFillAfter(true);
        view.startAnimation(zoomin);

        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        slide.setDuration(time);
        slide.setFillAfter(true);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animZoomin = AnimationUtils.loadAnimation(Ad_CustomInterstitialActivity.this, R.anim.ad_zoom_out);
                animZoomin.setFillAfter(true);
                view.startAnimation(animZoomin);
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

    public void FadeIn(final View view) {
        Animation aniFade = AnimationUtils.loadAnimation(this, R.anim.ad_fade_in);
        view.startAnimation(aniFade);
        aniFade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    public static void newIntent(Activity activity, MyCallback Callback, CustomAdModel AdModel, AdsTimer adsTimer, AdsTimer adsTimer2,int type) {
        customAdModel = AdModel;
        myCallback = Callback;
        myadsTimer = adsTimer;
        myadsTimer2 = adsTimer2;
        myType = type;
        activity.startActivity(new Intent(activity, Ad_CustomInterstitialActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (myType==0) {
            if (myadsTimer != null) {
                myadsTimer.setTimeVar("mytime", (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
            }
        }else if (myType==1) {
            if (myadsTimer2 != null) {
                myadsTimer2.setTimeVar("backmytime", (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}