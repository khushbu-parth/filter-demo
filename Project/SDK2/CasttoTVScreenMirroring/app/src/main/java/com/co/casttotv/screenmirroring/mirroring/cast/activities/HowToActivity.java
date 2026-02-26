package com.co.casttotv.screenmirroring.mirroring.cast.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ActivityHowToBinding;

public class HowToActivity extends AppCompatActivity {
    ActivityHowToBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_how_to);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());

        binding.buttonGotit.setOnClickListener(view -> onBackPressed());
    }
}