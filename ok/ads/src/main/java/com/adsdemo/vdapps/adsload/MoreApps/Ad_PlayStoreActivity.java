package com.adsdemo.vdapps.adsload.MoreApps;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.adsdemo.vdapps.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.google.android.material.tabs.TabLayout;

public class Ad_PlayStoreActivity extends AppCompatActivity {

    private Ad_ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView noData;
    ImageView ivBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_play_store);

        noData = findViewById(R.id.noData);

        viewPager = findViewById(R.id.viewpager);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(v -> onBackPressed());

        viewPagerAdapter = new Ad_ViewPagerAdapter(getSupportFragmentManager());

        if (AdsManager.moreAppsList.size() != 0) {
            viewPagerAdapter.add(new Ad_MoreAppsFragment(), "Apps");
        }
        if (AdsManager.moreGamesList.size() != 0) {
            viewPagerAdapter.add(new Ad_MoreGamesFragment(), "Games");
        }
        if (AdsManager.moreGamesList.size() == 0 && AdsManager.moreAppsList.size() == 0) {
            noData.setVisibility(View.VISIBLE);
        }
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}