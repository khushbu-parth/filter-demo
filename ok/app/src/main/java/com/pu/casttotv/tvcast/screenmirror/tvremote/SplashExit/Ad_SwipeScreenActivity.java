package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.adsdemo.vdapps.adsload.interfaces.MyCallback;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.MainActivity;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class Ad_SwipeScreenActivity extends AppCompatActivity {

    private ViewPager innerguidePager;
    private Ad_SwipeAdapter mViewPagerAdapter;

    int[] images = {R.drawable.imv_intro_1, R.drawable.imv_intro_2, R.drawable.imv_intro_3};
    String[] name;
    String[] name1;
    private DotsIndicator dotsIndicator;
    private TextView tv_continue, tv_startapp;
    private int currentPos = 0;
    private TextView tv_skip;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_swipe_screen);
        name = new String[]{getString(R.string.cast_to_tv_intro), getString(R.string.detail_mirror), getString(R.string.detail_remote)};
        name1 = new String[]{getString(R.string.cast_to_tv), getString(R.string.screen_mirror), getString(R.string.remote_to_tv)};
        AdsManager.CallBannerAds(this, findViewById(R.id.ad_view));
        AdsManager.CallNativeAdLoad(this, findViewById(R.id.native_container), AdsManager.NATIVE_MIDEUM);

        innerguidePager = (ViewPager) findViewById(R.id.innerguidePager);
        dotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);
        tv_skip = (TextView) findViewById(R.id.tv_skip);
        tv_continue = (TextView) findViewById(R.id.tv_continue);
        tv_continue.setText("Next");
        tv_startapp = (TextView) findViewById(R.id.tv_startapp);

        mViewPagerAdapter = new Ad_SwipeAdapter(Ad_SwipeScreenActivity.this, images, name, name1);
        innerguidePager.setAdapter(mViewPagerAdapter);
        innerguidePager.setCurrentItem(0);
        dotsIndicator.setViewPager(innerguidePager);
        innerguidePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                currentPos = position;
                if (currentPos == 0) {
                    tv_continue.setText("Next");
                    tv_continue.setVisibility(View.VISIBLE);
                    tv_startapp.setVisibility(View.GONE);
                } else if (currentPos == 1) {
                    tv_continue.setText("Continue");
                    tv_continue.setVisibility(View.VISIBLE);
                    tv_startapp.setVisibility(View.GONE);
                } else if (currentPos == 2) {
                    tv_continue.setVisibility(View.GONE);
                    tv_startapp.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*empty*/}
        });

        tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPos == 0) {
                    innerguidePager.setCurrentItem(1);
                } else if (currentPos == 1) {
                    innerguidePager.setCurrentItem(2);
                } else if (currentPos == 2) {

                }
            }
        });
        tv_startapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsManager.CallInterstitialAdLoad(Ad_SwipeScreenActivity.this, 0, new MyCallback() {
                    @Override
                    public void callbackCall() {
                        if (AdsManager.AppPurchaseScreen == 1) {
                            Intent intent = new Intent(Ad_SwipeScreenActivity.this, Ad_AppPurchaseActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Ad_SwipeScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });


            }
        });
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdsManager.CallInterstitialAdLoad(Ad_SwipeScreenActivity.this, 0, new MyCallback() {
                    @Override
                    public void callbackCall() {
                        if (AdsManager.AppPurchaseScreen == 1) {
                            Intent intent = new Intent(Ad_SwipeScreenActivity.this, Ad_AppPurchaseActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Ad_SwipeScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        AdsManager.CallInterstitialAdLoad(this, 1, new MyCallback() {
            @Override
            public void callbackCall() {
                finish();
            }
        });
    }
}