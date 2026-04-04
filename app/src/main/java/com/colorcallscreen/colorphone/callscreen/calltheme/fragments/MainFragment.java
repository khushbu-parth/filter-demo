package com.colorcallscreen.colorphone.callscreen.calltheme.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ads.control.ads.AperoAd;
import com.ads.control.ads.AperoAdCallback;
import com.ads.control.ads.wrapper.ApAdError;
import com.ads.control.ads.wrapper.ApInterstitialAd;
import com.ads.control.config.AperoAdConfig;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.ThemeWebService;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityAllWallpapers;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;

public class MainFragment extends Fragment {
    FrameLayout btnCustom;
    FrameLayout btnLiveWallpaper;
    FrameLayout btnThemes;
    FrameLayout btnWallpaper;
    private String idInter = "";
    private ApInterstitialAd mInterstitialAd;
    CompoundButton.OnCheckedChangeListener randomThemeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityThemeChange.6
        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            int i;
            Helper.enableRandomTheme(z, true);
            if (!z || (i = PreferenceUtils.getInstance().getInt("random_theme_toast_count")) > 3) {
                return;
            }
            Toast.makeText(getActivity(), (int) R.string.random_theme_toast, 0).show();
            PreferenceUtils.getInstance().putPreference("random_theme_toast_count", i + 1);
        }
    };
    SwitchCompat sw_random_theme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        configMediationProvider();
        loadAdInterstitial();
        this.sw_random_theme = (SwitchCompat) view.findViewById(R.id.sw_random_theme);
        this.btnWallpaper = (FrameLayout) view.findViewById(R.id.btnWallpaper);
        this.btnLiveWallpaper = (FrameLayout) view.findViewById(R.id.btnLiveWallpaper);
        this.btnThemes = (FrameLayout) view.findViewById(R.id.btnThemes);
        this.btnCustom = (FrameLayout) view.findViewById(R.id.btnCustom);
        this.btnWallpaper.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityThemeChange.2
            @Override 
            public void onClick(View view) {
                if (mInterstitialAd.isReady()) {

                    AperoAd.getInstance().showInterstitialAdByTimes(getActivity(), mInterstitialAd, new AperoAdCallback() {
                        @Override
                        public void onNextAction() {
                            Log.i("TAG", "onNextAction: start content and finish main");

                            getActivity().startActivity(new Intent(getActivity(), ActivityAllWallpapers.class)
                                    .putExtra("type", ThemeWebService.CALLER_IMAGES)
                                    .putExtra("name", "Abstract"));
                        }

                        @Override
                        public void onAdFailedToShow(@Nullable ApAdError adError) {
                            super.onAdFailedToShow(adError);
                            Log.i("TAG", "onAdFailedToShow:" + adError.getMessage());
                        }

                        @Override
                        public void onInterstitialShow() {
                            super.onInterstitialShow();
                            Log.d("TAG", "onInterstitialShow");
                        }
                    }, true);
                } else {
                    getActivity().startActivity(new Intent(getActivity(), ActivityAllWallpapers.class)
                            .putExtra("type", ThemeWebService.CALLER_IMAGES)
                            .putExtra("name", "Abstract"));
                }

            }
        });
        this.btnLiveWallpaper.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityThemeChange.3
            @Override 
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), ActivityAllWallpapers.class).putExtra("type", ThemeWebService.CALLER_LIVEWALLPAPER).putExtra("name", "LiveWallpaper"));
            }
        });
        this.btnThemes.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityThemeChange.4
            @Override 
            public void onClick(View view) {
                if (mInterstitialAd.isReady()) {

                    AperoAd.getInstance().showInterstitialAdByTimes(getActivity(), mInterstitialAd, new AperoAdCallback() {
                        @Override
                        public void onNextAction() {
                            Log.i("TAG", "onNextAction: start content and finish main");

                            getActivity().startActivity(new Intent(getActivity(), ActivityAllWallpapers.class).putExtra("type", ThemeWebService.CALLER_THEME).putExtra("name", "Theme"));

                        }

                        @Override
                        public void onAdFailedToShow(@Nullable ApAdError adError) {
                            super.onAdFailedToShow(adError);
                            Log.i("TAG", "onAdFailedToShow:" + adError.getMessage());
                        }

                        @Override
                        public void onInterstitialShow() {
                            super.onInterstitialShow();
                            Log.d("TAG", "onInterstitialShow");
                        }
                    }, true);
                } else {
                    getActivity().startActivity(new Intent(getActivity(), ActivityAllWallpapers.class).putExtra("type", ThemeWebService.CALLER_THEME).putExtra("name", "Theme"));

                }
            }
        });
        this.btnCustom.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityThemeChange.5
            @Override 
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), ActivityAllWallpapers.class).putExtra("type", ThemeWebService.CUSTOM).putExtra("name", "Custom"));
            }
        });
        this.sw_random_theme.setChecked(PreferenceUtils.getInstance().getBoolean(Constants.KEY_RANDOM_THEME));
        this.sw_random_theme.setOnCheckedChangeListener(this.randomThemeListener);
        return view;
    }
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.sw_random_theme.setOnCheckedChangeListener(null);
        this.sw_random_theme.setChecked(PreferenceUtils.getInstance().getBoolean(Constants.KEY_RANDOM_THEME));
        this.sw_random_theme.setOnCheckedChangeListener(this.randomThemeListener);
    }

    private void configMediationProvider() {
        if (AperoAd.getInstance().getMediationProvider() == AperoAdConfig.PROVIDER_ADMOB) {
            idInter = getResources().getString(R.string.admob_inter_id);
        } else {
            idInter = "c630fe3686063741";
        }
    }

    private void loadAdInterstitial() {

        mInterstitialAd = AperoAd.getInstance().getInterstitialAds(getActivity(), idInter);
    }
}