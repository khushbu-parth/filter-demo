package com.cast.tv.screen.mirroring.screencasting.supportedices;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.library.info.CastTvAppManager;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.UI.main.MainCastActivity;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    static WifiManager wifi;
    String AUTO_MODE = "auto_mode";
    String BACK = "back";
    String HDMI_PART = "hdmi_part";
    String INFO = "iuc_info";
    String MANUAL_MODE = "iuc_manual_mode";
    String action_name = "back";
    public Activity home_activity = null;
    ImageView iv_auto_mode;
    ImageView iv_guide;
    ImageView iv_hdmi_guide;
    ImageView iv_manual_mode;

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.iuc_home);
        getWindow().setFlags(1024, 1024);

        CastTvAppManager.getInstance(this).showNativeAds(this, findViewById(R.id.fl_native_banner), findViewById(R.id.native_space_img), 2);
        CastTvAppManager.getInstance(this).showBannerAds(this, findViewById(R.id.fl_banner));


        ImageView imageView2 = (ImageView) findViewById(R.id.iv_auto_mode);
        this.iv_auto_mode = imageView2;
        imageView2.setOnClickListener(this);
        ImageView imageView3 = (ImageView) findViewById(R.id.iv_manual_mode);
        this.iv_manual_mode = imageView3;
        imageView3.setOnClickListener(this);
        ImageView imageView4 = (ImageView) findViewById(R.id.iv_guide);
        this.iv_guide = imageView4;
        imageView4.setOnClickListener(this);
        wifi = (WifiManager) getApplicationContext().getSystemService("wifi");
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_auto_mode:
                        action_name = AUTO_MODE;
                        Select_TV_Screen_From_Auto();
                return;
            case R.id.iv_guide:
                Guide_Screen();
                return;
            case R.id.iv_manual_mode:
                action_name = MANUAL_MODE;
                Select_TV_Screen_From_Manual();
                return;
            default:
                return;
        }
    }

    public void Select_TV_Screen_From_Auto() {
        CastTvAppManager.getInstance(this).showInterstitialAd(this, () -> {
            Intent intent = new Intent(this, SelectTVActivity.class);
            intent.putExtra("FromAuto", true);
            startActivity(intent);
        });

    }

    public void Select_TV_Screen_From_Manual() {

        CastTvAppManager.getInstance(this).showInterstitialAd(this, () -> {
            Intent intent = new Intent(this, SelectTVActivity.class);
            intent.putExtra("FromAuto", false);
            startActivity(intent);
        });


    }

    public void Guide_Screen() {
        CastTvAppManager.getInstance(this).showInterstitialAd(this, () -> {
            startActivity(new Intent(this, GuideActivity.class));
        });

    }


    @Override
    public void onResume() {
        super.onResume();

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
            startActivity(new Intent(HomeActivity.this, MainCastActivity.class));
        });

    }
}
