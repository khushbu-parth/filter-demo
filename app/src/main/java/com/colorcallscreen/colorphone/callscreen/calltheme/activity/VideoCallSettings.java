package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.control.ads.AperoAd;
import com.ads.control.ads.bannerAds.AperoBannerAdView;
import com.colorcallscreen.colorphone.callscreen.calltheme.Splash.StartActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.adapter.VideoCallSettingAdapter;
import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.controller.VideoCallController;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.VideoCallSettingModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;


public class VideoCallSettings extends BaseActivity implements VideoCallSettingAdapter.AppSelectedListener {
    private List<VideoCallSettingModel> installedApp = new ArrayList();
    AppCompatImageView ivBack;
    private List<VideoCallSettingModel> listApp;
    LinearLayout adsCard;
    @Override
    public void onAppSelected(VideoCallController.VideoCallApps videoCallApps) {
        VideoCallController.Settings.saveApp(videoCallApps);
        String str = Constants.SettingCategory;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.settings_video_call);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        adsCard = findViewById(R.id.adsCard);

        adsCard.setVisibility(View.VISIBLE);
        FrameLayout flPlaceHolder = findViewById(com.ads.control.R.id.fl_adplaceholder);
        ShimmerFrameLayout shimmerFrameLayout = findViewById(com.ads.control.R.id.shimmer_container_native);
        AperoAd.getInstance().loadNativeAd(VideoCallSettings.this, getResources().getString(R.string.admob_native), com.ads.control.R.layout.custom_native_admob_free_size, flPlaceHolder, shimmerFrameLayout);
        AppCompatImageView appCompatImageView = (AppCompatImageView) findViewById(R.id.ivBack);
        this.ivBack = appCompatImageView;
        appCompatImageView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.VideoCallSettings.1
            @Override 
            public void onClick(View view) {
                VideoCallSettings.this.onBackPressed();
            }
        });
        this.listApp = (List) new Gson().fromJson(Utility.loadJSONFromAsset(this, "videocallsettings.json"), new TypeToken<List<VideoCallSettingModel>>() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.VideoCallSettings.2
        }.getType());
        for (int i = 0; i < this.listApp.size(); i++) {
            if (Utility.checkAppIsInstalledOrNot(this, this.listApp.get(i).getAapPackage())) {
                this.installedApp.add(this.listApp.get(i));
            }
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvVideoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        VideoCallSettingAdapter videoCallSettingAdapter = new VideoCallSettingAdapter(this, this.installedApp);
        videoCallSettingAdapter.setAppSelectedListener(this);
        recyclerView.setAdapter(videoCallSettingAdapter);
    }
}
