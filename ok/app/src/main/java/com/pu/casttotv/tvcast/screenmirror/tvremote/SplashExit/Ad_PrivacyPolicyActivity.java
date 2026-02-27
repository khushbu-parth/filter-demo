package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.Globle.Ad_Permission;
import com.adsdemo.vdapps.adsload.Ad_Globals;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.adsdemo.vdapps.adsload.interfaces.MyCallback;
import com.adsdemo.vdapps.adsload.utils.MyWebView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.MainActivity;


public class Ad_PrivacyPolicyActivity extends AppCompatActivity {
    CheckBox cb_check1;
    MyWebView wvPrivacyPolicy;
    TextView tv_agree, tv_notagree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_privacy_policy);

        AdsManager.CallNativeAdLoad(this, findViewById(R.id.native_container), AdsManager.NATIVE_MIDEUM);

        initClick();
    }

    private void initClick() {
        cb_check1 = findViewById(R.id.cb_check1);
        tv_agree = findViewById(R.id.tv_agree);

        tv_notagree = findViewById(R.id.tv_notagree);
        wvPrivacyPolicy = findViewById(R.id.privacy_web);

        tv_agree.setVisibility(View.GONE);
        tv_notagree.setVisibility(View.VISIBLE);

        cb_check1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tv_agree.setVisibility(View.VISIBLE);
                tv_notagree.setVisibility(View.GONE);
            } else {
                tv_agree.setVisibility(View.GONE);
                tv_notagree.setVisibility(View.VISIBLE);
            }
        });

        wvPrivacyPolicy.loadUrl("file:///android_asset/PrivacyPolicy.html");
        wvPrivacyPolicy.setOnScrollChangeListener((view, i, i1, i2, i3) -> {
            int r1 = wvPrivacyPolicy.computeVerticalScrollRange();
            int r2 = wvPrivacyPolicy.computeVerticalScrollExtent();
            if (i1 == (r1 - r2)) {
                cb_check1.setChecked(true);
            }
        });

        tv_notagree.setOnClickListener(v -> {

        });

        tv_agree.setOnClickListener(v -> {
            Ad_Globals.saveBoolean(Ad_PrivacyPolicyActivity.this, cb_check1.isChecked(), "TermCondition");
            if (Ad_Globals.isBoolean(Ad_PrivacyPolicyActivity.this, "TermCondition")) {
                AdsManager.CallInterstitialAdLoad(Ad_PrivacyPolicyActivity.this, 0, new MyCallback() {
                    @Override
                    public void callbackCall() {
                        if (AdsManager.PermissionScreen == 1) {
                            if (!Ad_Permission.isPermissionGranted(Ad_PrivacyPolicyActivity.this)/*||!Ad2_Permission.isGloblePermissionGranted(PrivacyPolicyActivity.this)*/) {
                                Intent intent = new Intent(Ad_PrivacyPolicyActivity.this, Ad_PermissionActivity.class);
                                startActivity(intent);
                            } else if (AdsManager.CountryScreen == 1) {
                                Intent intent = new Intent(Ad_PrivacyPolicyActivity.this, Ad_CountryActivity.class);
                                startActivity(intent);
                            } else if (AdsManager.LanguageScreen == 1) {
                                Intent intent = new Intent(Ad_PrivacyPolicyActivity.this, Ad_LanguageActivity.class);
                                startActivity(intent);
                            } else if (AdsManager.SwipeScreen == 1) {
                                Intent intent = new Intent(Ad_PrivacyPolicyActivity.this, Ad_SwipeScreenActivity.class);
                                startActivity(intent);
                            } else if (AdsManager.AppPurchaseScreen == 1) {
                                Intent intent = new Intent(Ad_PrivacyPolicyActivity.this, Ad_AppPurchaseActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(Ad_PrivacyPolicyActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        } else if (AdsManager.CountryScreen == 1) {
                            Intent intent = new Intent(Ad_PrivacyPolicyActivity.this, Ad_CountryActivity.class);
                            startActivity(intent);
                        } else if (AdsManager.LanguageScreen == 1) {
                            Intent intent = new Intent(Ad_PrivacyPolicyActivity.this, Ad_LanguageActivity.class);
                            startActivity(intent);
                        } else if (AdsManager.SwipeScreen == 1) {
                            Intent intent = new Intent(Ad_PrivacyPolicyActivity.this, Ad_SwipeScreenActivity.class);
                            startActivity(intent);
                        } else if (AdsManager.AppPurchaseScreen == 1) {
                            Intent intent = new Intent(Ad_PrivacyPolicyActivity.this, Ad_AppPurchaseActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Ad_PrivacyPolicyActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });

            } else {
                if (!Ad_Globals.isBoolean(Ad_PrivacyPolicyActivity.this, "TermCondition")) {
                    Animation animShake = AnimationUtils.loadAnimation(Ad_PrivacyPolicyActivity.this, R.anim.ad_shake);
                    cb_check1.startAnimation(animShake);
                }

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return false;

    }

    @Override
    public void onBackPressed() {

    }
}