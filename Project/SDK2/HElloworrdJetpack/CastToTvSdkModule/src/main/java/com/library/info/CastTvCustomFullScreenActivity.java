package com.library.info;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

public class CastTvCustomFullScreenActivity extends AppCompatActivity {
    public static AdCallbackListenerCastTv myCallback;
    public static CastTvCustomModel customModel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(CastTvCustomFullScreenActivity.this, R.color.white));// set status background white
        }
        setContentView(R.layout.custom_full_screen_view);
        ImageView cust_ad_media = (ImageView) findViewById(R.id.cust_ad_media);
        Glide.with(this)
                .load(customModel.interstitial_image)
                .into(cust_ad_media);

        findViewById(R.id.ImgClose).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                closeAd();
            }
        });
        cust_ad_media.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (CastTvAppManager.isNetworkAvailable(CastTvCustomFullScreenActivity.this)) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(customModel.url));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static void newIntent(Activity activity, AdCallbackListenerCastTv Callback, CastTvCustomModel AdModel) {
        customModel = AdModel;
        myCallback = Callback;
        activity.startActivity(new Intent(activity, CastTvCustomFullScreenActivity.class));
    }

    private void closeAd() {
        CastTvAppManager.getInstance(CastTvCustomFullScreenActivity.this).loadInterstitialAd(CastTvCustomFullScreenActivity.this);
        finish();
        if (myCallback != null) {
            myCallback.callbackCall();
            myCallback = null;
        }
    }

    @Override
    public void onBackPressed() {

    }

}