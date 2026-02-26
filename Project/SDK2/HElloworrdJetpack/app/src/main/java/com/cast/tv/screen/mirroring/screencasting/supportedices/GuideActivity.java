package com.cast.tv.screen.mirroring.screencasting.supportedices;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.library.info.CastTvAppManager;
import com.cast.tv.screen.mirroring.screencasting.R;


public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.iuc_guide);
        ConstantMethod.BottomNavigationColor(this);

        CastTvAppManager.getInstance(this).showNativeAds(this, findViewById(R.id.fl_native_banner), findViewById(R.id.native_space_img), 2);
        CastTvAppManager.getInstance(this).showBannerAds(this, findViewById(R.id.fl_banner));

    }

    public void onClick(View view) {
        onBackPressed();
    }

    @Override
    public void onResume() {
        try {
            super.onResume();
        } catch (Exception e) {
            e.toString();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        CastTvAppManager.getInstance(this).showInterstitialBackAd(this, () -> {
            startActivity(new Intent(GuideActivity.this, HomeActivity.class));
            finish();
        });


    }

}
