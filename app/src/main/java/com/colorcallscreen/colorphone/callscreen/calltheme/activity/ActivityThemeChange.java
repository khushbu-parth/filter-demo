package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SwitchCompat;

import com.colorcallscreen.colorphone.callscreen.calltheme.service.ThemeWebService;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;


public class ActivityThemeChange extends AppCompatActivity {
    FrameLayout btnCustom;
    FrameLayout btnLiveWallpaper;
    FrameLayout btnThemes;
    FrameLayout btnWallpaper;
    CompoundButton.OnCheckedChangeListener randomThemeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            int i;
            Helper.enableRandomTheme(z, true);
            if (!z || (i = PreferenceUtils.getInstance().getInt("random_theme_toast_count")) > 3) {
                return;
            }
            Toast.makeText(ActivityThemeChange.this, (int) R.string.random_theme_toast, Toast.LENGTH_SHORT).show();
            PreferenceUtils.getInstance().putPreference("random_theme_toast_count", i + 1);
        }
    };
    SwitchCompat sw_random_theme;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_theme_change);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        ((AppCompatImageView) findViewById(R.id.ivBack)).setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ActivityThemeChange.this.onBackPressed();
            }
        });
        this.sw_random_theme = (SwitchCompat) findViewById(R.id.sw_random_theme);
        this.btnWallpaper = (FrameLayout) findViewById(R.id.btnWallpaper);
        this.btnLiveWallpaper = (FrameLayout) findViewById(R.id.btnLiveWallpaper);
        this.btnThemes = (FrameLayout) findViewById(R.id.btnThemes);
        this.btnCustom = (FrameLayout) findViewById(R.id.btnCustom);
        this.btnWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ActivityThemeChange.this.startActivity(new Intent(ActivityThemeChange.this, ActivityAllWallpapers.class).putExtra("type", ThemeWebService.CALLER_IMAGES).putExtra("name", "Abstract"));
            }
        });
        this.btnLiveWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ActivityThemeChange.this.startActivity(new Intent(ActivityThemeChange.this, ActivityAllWallpapers.class).putExtra("type", ThemeWebService.CALLER_LIVEWALLPAPER).putExtra("name", "LiveWallpaper"));
            }
        });
        this.btnThemes.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ActivityThemeChange.this.startActivity(new Intent(ActivityThemeChange.this, ActivityAllWallpapers.class).putExtra("type", ThemeWebService.CALLER_THEME).putExtra("name", "Theme"));
            }
        });
        this.btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                        ActivityThemeChange.this.startActivity(new Intent(ActivityThemeChange.this, ActivityAllWallpapers.class).putExtra("type", ThemeWebService.CUSTOM).putExtra("name", "Custom"));
            }
        });
        this.sw_random_theme.setChecked(PreferenceUtils.getInstance().getBoolean(Constants.KEY_RANDOM_THEME));
        this.sw_random_theme.setOnCheckedChangeListener(this.randomThemeListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.sw_random_theme.setOnCheckedChangeListener(null);
        this.sw_random_theme.setChecked(PreferenceUtils.getInstance().getBoolean(Constants.KEY_RANDOM_THEME));
        this.sw_random_theme.setOnCheckedChangeListener(this.randomThemeListener);
    }
}
