package com.co.casttotv.screenmirroring.mirroring.cast.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.ads.sdk.SdkManager;
import com.co.casttotv.screenmirroring.mirroring.cast.BuildConfig;
import com.co.casttotv.screenmirroring.mirroring.cast.MainActivity;
import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ActivityStartBinding;

public class StartActivity extends AppCompatActivity {
    ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);

        SdkManager.loadNative(StartActivity.this, binding.frameNativeView);
        binding.btnStart.setOnClickListener(view -> {
            SdkManager.showInterstitialAd(StartActivity.this, () -> {
                startActivity(new Intent(this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            });
        });

        binding.btnShare.setOnClickListener(view -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
            }
        });

        binding.btnRate.setOnClickListener(view -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
            }
        });

        binding.btnPrivacy.setOnClickListener(view -> {
            startActivity(new Intent(StartActivity.this, PolicyActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ExitActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }


}