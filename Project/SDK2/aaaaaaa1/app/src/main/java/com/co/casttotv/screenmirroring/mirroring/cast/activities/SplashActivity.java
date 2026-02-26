package com.co.casttotv.screenmirroring.mirroring.cast.activities;

import com.co.casttotv.screenmirroring.mirroring.cast.BuildConfig;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.ads.sdk.SplashBaseActivity;
import com.co.casttotv.screenmirroring.mirroring.cast.R;

public class SplashActivity extends SplashBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_splash);

        loadSplash(BuildConfig.DEBUG, BuildConfig.VERSION_CODE, "1"); //"1" is your app ID
    }

    @Override
    public void onComplete() {
        startActivity(new Intent(SplashActivity.this, StartActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}