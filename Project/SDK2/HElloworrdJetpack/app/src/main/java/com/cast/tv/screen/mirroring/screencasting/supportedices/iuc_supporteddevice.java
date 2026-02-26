package com.cast.tv.screen.mirroring.screencasting.supportedices;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.library.info.CastTvAppManager;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.UI.main.MainCastActivity;


public class iuc_supporteddevice extends AppCompatActivity {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.iuc_supported_device);
        getWindow().setFlags(1024, 1024);

        CastTvAppManager.getInstance(this).showBannerAds(this, findViewById(R.id.fl_banner));
        CastTvAppManager.getInstance(this).showNativeAds(this, findViewById(R.id.fl_native_banner), findViewById(R.id.native_space_img), 2);

        findViewById(R.id.btnback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        CastTvAppManager.getInstance(this).showInterstitialBackAd(this, () -> {
            startActivity(new Intent(iuc_supporteddevice.this, MainCastActivity.class));
        });

    }
}
