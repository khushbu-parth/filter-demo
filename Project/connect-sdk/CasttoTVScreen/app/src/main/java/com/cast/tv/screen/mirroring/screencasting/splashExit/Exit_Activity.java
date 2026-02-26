package com.cast.tv.screen.mirroring.screencasting.splashExit;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cast.tv.screen.mirroring.screencasting.R;
import com.library.info.CastTvAppManager;

public class Exit_Activity extends AppCompatActivity {

    ImageView no, yes, rate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fcom_backactivity);
        getWindow().setFlags(1024, 1024);

//        AppManager.getInstance(this).showNativeAds(this, findViewById(R.id.fl_native_banner), findViewById(R.id.native_space_img), 1);
        if (CastTvAppManager.nativeExit == 1) {
            CastTvAppManager.getInstance(this).showNativeAds(
                    this,
                    (ViewGroup) findViewById(R.id.fl_native_banner),
                    (ImageView) findViewById(R.id.native_space_img),
                    1
            );
        }


        no = findViewById(R.id.no);
        yes = findViewById(R.id.yes);
        rate = findViewById(R.id.rate);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CastTvAppManager.getInstance(Exit_Activity.this).showInterstitialAd(Exit_Activity.this, () -> {
                    startActivity(new Intent(Exit_Activity.this, Thanyou_Activity.class));
                    finish();
                });

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CastTvAppManager.getInstance(Exit_Activity.this).showInterstitialAd(Exit_Activity.this, () -> {
                    startActivity(new Intent(Exit_Activity.this, Second_Activity.class));
                    finish();
                });


            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoStore();
            }
        });
    }


    public void gotoStore() {
        Uri uri = Uri.parse(Glob.app_link);
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        myAppLinkToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
        }
    }

    public void onBackPressed() {
    }
}
