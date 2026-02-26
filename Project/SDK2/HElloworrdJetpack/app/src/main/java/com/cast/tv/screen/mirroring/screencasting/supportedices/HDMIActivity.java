package com.cast.tv.screen.mirroring.screencasting.supportedices;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.library.info.CastTvAppManager;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.UI.main.MainCastActivity;


public class HDMIActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.iuc_hdmi);

        CastTvAppManager.getInstance(this).showNativeAds(this, findViewById(R.id.fl_native_banner), findViewById(R.id.native_space_img), 2);
        CastTvAppManager.getInstance(this).showBannerAds(this, findViewById(R.id.fl_banner));
    }

    @Override
    public void onBackPressed() {

        CastTvAppManager.getInstance(this).showInterstitialBackAd(this, () -> {
            startActivity(new Intent(HDMIActivity.this, MainCastActivity.class));
        });
    }
}
