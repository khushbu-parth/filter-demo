package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/* loaded from: classes4.dex */
public class DownloadsActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;

    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_downloads);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        this.viewPager = (ViewPager2) findViewById(R.id.viewPager);
        TabLayout tabLayout = this.tabLayout;
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.Videos)));
        TabLayout tabLayout2 = this.tabLayout;
        tabLayout2.addTab(tabLayout2.newTab().setText(getString(R.string.Audios)));
        TabLayout tabLayout3 = this.tabLayout;
        tabLayout3.addTab(tabLayout3.newTab().setText(getString(R.string.Audios)));
        this.tabLayout.setTabGravity(0);
        this.viewPager.setAdapter(new TabAdapter(this));
        new TabLayoutMediator(this.tabLayout, this.viewPager, new TabLayoutMediator.TabConfigurationStrategy() { // from class: com.thntech.cast68.screen.tab.webcast.DownloadsActivity.1
            @Override // com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
            public void onConfigureTab(TabLayout.Tab tab, int i) {
                if (i == 0) {
                    tab.setText(DownloadsActivity.this.getString(R.string.Videos));
                } else if (i == 1) {
                    tab.setText(DownloadsActivity.this.getString(R.string.Audios));
                } else if (i != 2) {
                } else {
                    tab.setText(DownloadsActivity.this.getString(R.string.txt_photo));
                }
            }
        }).attach();
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
