package com.cast.tv.screen.mirroring.screencasting.splashExit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.library.info.CastTvAppManager;


public class First_Activity extends AppCompatActivity {
    ImageView startImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        getWindow().setFlags(1024, 1024);

        CastTvAppManager.getInstance(this).showNativeAds(this, findViewById(R.id.fl_native_banner), findViewById(R.id.native_space_img), 1);

        startImage = findViewById(R.id.start_image);
        startImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CastTvAppManager.getInstance(First_Activity.this).showInterstitialAd(First_Activity.this, () -> {
                    startActivity(new Intent(First_Activity.this, Second_Activity.class));
                    finish();
                });


            }
        });
    }

    @Override
    public void onBackPressed() {
        CastTvAppManager.getInstance(this).showInterstitialBackAd(this, () -> {
            startActivity(new Intent(First_Activity.this, Exit_Activity.class));
            finish();
        });

    }
}