package com.pu.casttotv.tvcast.screenmirror.tvremote.screen;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.view.MyWebView;


public class PrivacyPolicyActivity extends AppCompatActivity {
    MyWebView wvPrivacyPolicy;
    LinearLayout llBack;
    boolean firstTime = false;
    CheckBox cbAgree;
    TextView btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        firstTime = getIntent().getBooleanExtra("firstTime", false);

        cbAgree = findViewById(R.id.cbAgree);
        btnNext = findViewById(R.id.btnNext);

        llBack = findViewById(R.id.llBack);
        llBack.setOnClickListener(v -> onBackPressed());

        wvPrivacyPolicy = findViewById(R.id.wvPrivacyPolicy);
        wvPrivacyPolicy.loadUrl("file:///android_asset/PrivacyPolicy.html");

        wvPrivacyPolicy.setOnScrollChangeListener((view, i, i1, i2, i3) -> {
            int r1 = wvPrivacyPolicy.computeVerticalScrollRange();
            int r2 = wvPrivacyPolicy.computeVerticalScrollExtent();
            if (i1 == (r1 - r2)) {
                cbAgree.setChecked(true);
            }
        });
        btnNext.setBackground(getResources().getDrawable(R.drawable.start_tv_bg_light));
        btnNext.setTextColor(getResources().getColor(R.color.white));
        btnNext.setEnabled(false);
        cbAgree.setOnCheckedChangeListener((compoundButton, b) -> {
            btnNext.setEnabled(b);
            if (b) {
                btnNext.setBackground(getResources().getDrawable(R.drawable.start_tv_bg_dark));
                btnNext.setTextColor(getResources().getColor(R.color.white));
            }else{
                btnNext.setBackground(getResources().getDrawable(R.drawable.start_tv_bg_light));
                btnNext.setTextColor(getResources().getColor(R.color.white));
            }
        });

        if (firstTime) {
            cbAgree.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cbAgree.isChecked()) {

                    } else {
                        Toast.makeText(PrivacyPolicyActivity.this, "First accept privacy policy.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            cbAgree.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        }
    }

}