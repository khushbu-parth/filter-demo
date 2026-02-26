package com.co.casttotv.screenmirroring.mirroring.cast.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.ads.sdk.SdkManager;
import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ActivityThanksBinding;

public class ThanksActivity extends AppCompatActivity {
    ActivityThanksBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_thanks);

        binding.btnOk.setOnClickListener(view -> {
            SdkManager.finalExit(ThanksActivity.this);
        });
    }

    @Override
    public void onBackPressed() {

    }
}