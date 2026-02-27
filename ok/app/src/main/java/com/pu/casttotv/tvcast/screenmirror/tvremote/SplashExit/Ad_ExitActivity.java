package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.adsdemo.vdapps.adsload.interfaces.MyCallback;


public class Ad_ExitActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_exit);
        AdsManager.CallBannerAds(this, findViewById(R.id.ad_view));
        AdsManager.CallNativeAdLoad(this, findViewById(R.id.native_container), AdsManager.NATIVE_BIG);

        findViewById(R.id.textYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);
            }
        });

        findViewById(R.id.textNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
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