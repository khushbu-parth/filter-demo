package com.library.info;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class CustomAppOpenAdCastTv extends AppCompatActivity {
    public static CastTvCustomModel customModel;
    public static SuccessListener myCallback;
    public static MyApplication.OnShowAdCompleteListener myCallbackResume;
    public static int adcounter = 0;
    public static boolean isactivitystart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_app_open_view);
        isactivitystart = true;
        findViewById(R.id.ll_continue_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAd();
            }
        });
        if (customModel != null) {
            Glide.with(this)
                    .load(customModel.appopen_image)
                    .into((ImageView) findViewById(R.id.custAppopen));

            findViewById(R.id.custAppopen).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (CastTvAppManager.isNetworkAvailable(CustomAppOpenAdCastTv.this)) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(customModel.url));
                        startActivity(browserIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            closeAd();
        }
    }

    private void closeAd() {
        finish();
        isactivitystart = false;
        overridePendingTransition(0, 0);
        if (adcounter == 1) {

            if (myCallback != null) {
                myCallback.onSuccess();
                myCallback = null;
            }
        } else {
            if (myCallbackResume != null) {
                myCallbackResume.onShowAdComplete();
                myCallbackResume = null;
            }
        }

    }

    @Override
    public void onBackPressed() {

    }

    public static void newIntent(Activity activit, SuccessListener Callback, CastTvCustomModel AdModel) {
        adcounter = 1;
        customModel = AdModel;
        myCallback = Callback;
        activit.startActivity(new Intent(activit, CustomAppOpenAdCastTv.class));
    }

    public static void newIntentResume(Activity activit, MyApplication.OnShowAdCompleteListener Callback, CastTvCustomModel AdModel) {
        if (!isactivitystart) {
            adcounter = 2;
            customModel = AdModel;
            myCallbackResume = Callback;
            activit.startActivity(new Intent(activit, CustomAppOpenAdCastTv.class));
        }
    }
}