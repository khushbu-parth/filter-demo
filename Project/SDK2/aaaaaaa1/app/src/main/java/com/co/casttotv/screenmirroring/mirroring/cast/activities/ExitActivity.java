package com.co.casttotv.screenmirroring.mirroring.cast.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.ads.sdk.SdkManager;
import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ActivityExitBinding;

public class ExitActivity extends AppCompatActivity {
    ActivityExitBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exit);
        SdkManager.loadNative(ExitActivity.this, binding.frameNativeView);

        binding.btnNo.setOnClickListener(view -> {
            finish();
        });

        binding.btnYes.setOnClickListener(view -> {
            SdkManager.showInterstitialAd(ExitActivity.this, () -> {
                startActivity(new Intent(this, ThanksActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            });
        });
    }

    @Override
    public void onBackPressed() {

    }
}