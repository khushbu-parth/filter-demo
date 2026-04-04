package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.ads.control.ads.AperoAd;
import com.ads.control.ads.AperoAdCallback;
import com.ads.control.ads.bannerAds.AperoBannerAdView;
import com.ads.control.ads.wrapper.ApAdError;
import com.ads.control.ads.wrapper.ApInterstitialAd;
import com.ads.control.config.AperoAdConfig;
import com.colorcallscreen.colorphone.callscreen.calltheme.Splash.StartActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.facebook.shimmer.ShimmerFrameLayout;


public class SettingsActivity extends AppCompatActivity {
    public static final String AUTO_REPLY = "auto_reply";
    private SwitchCompat automatic_call_rec;
    TextView btnChangeTheme;
    private SwitchCompat enable_callblocker;
    private SwitchCompat enable_caller_name;
    AppCompatImageView ivBack;
    private SwitchCompat led_flash;
    private String idInter = "";
    private ApInterstitialAd mInterstitialAd;

    LinearLayout adsCard;
    public static void enableShowDialerTab(boolean z) {
        PreferenceUtils.getInstance().putPreference("_dialer_tab", z);
    }

    public void setEnableDisabled(boolean z) {
        PreferenceUtils.getInstance().putPreference(ActivityBlockCallList.IS_ENABLE_BLOCKER, z);
    }

    public static boolean shouldShowDialer() {
        return PreferenceUtils.getInstance().getBoolean("_dialer_tab");
    }

    public static String getAutoReplyMsg(Context context) {
        String string = PreferenceUtils.getInstance().getString(AUTO_REPLY);
        return string == null ? context.getResources().getString(R.string.auto_reply_txt) : string;
    }

    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_settings);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        adsCard = findViewById(R.id.adsCard);

        adsCard.setVisibility(View.VISIBLE);
        FrameLayout flPlaceHolder = findViewById(com.ads.control.R.id.fl_adplaceholder);
        ShimmerFrameLayout shimmerFrameLayout = findViewById(com.ads.control.R.id.shimmer_container_native);
        AperoAd.getInstance().loadNativeAd(SettingsActivity.this, getResources().getString(R.string.admob_native), com.ads.control.R.layout.custom_native_admob_free_size, flPlaceHolder, shimmerFrameLayout);
        configMediationProvider();
        loadAdInterstitial();

        AppCompatImageView appCompatImageView = (AppCompatImageView) findViewById(R.id.ivBack);
        this.ivBack = appCompatImageView;
        appCompatImageView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity.1
            @Override 
            public void onClick(View view) {
                SettingsActivity.this.onBackPressed();
            }
        });
        TextView textView = (TextView) findViewById(R.id.btnChangeTheme);
        this.btnChangeTheme = textView;
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity.2
            @Override 
            public void onClick(View view) {
                SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, ActivityThemeChange.class));
            }
        });
        findViewById(R.id.our_caller_id_layout).setVisibility(0);
        if (!Helper.isCallRecordIsSupportedByDevice()) {
            findViewById(R.id.call_recording_layout).setVisibility(8);
        } else {
            findViewById(R.id.list_call_recording).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity.3
                @Override 
                public void onClick(View view) {
                    SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, MyRecordingsActivity.class));
                }
            });
            SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.enable_automatic_call_recording);
            this.automatic_call_rec = switchCompat;
            switchCompat.setChecked(Helper.shouldRecordCalAutomatic());
            this.automatic_call_rec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity.4
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    if (z) {
                        if (Helper.isCallRecordingTermAndConditionApproved()) {
                            if (ContextCompat.checkSelfPermission(SettingsActivity.this, BoloPermission.RECORD_AUDIO) != 0) {
                                SettingsActivity.this.requestPermissions(new String[]{BoloPermission.RECORD_AUDIO}, 2000);
                                SettingsActivity.this.automatic_call_rec.setChecked(false);
                                return;
                            }
                            Helper.recordCallAutomaticUpdate(true);
                            return;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                        builder.setMessage(R.string.recording_tandc_msg).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity.4.2
                            @Override 
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (ContextCompat.checkSelfPermission(SettingsActivity.this, BoloPermission.RECORD_AUDIO) != 0) {
                                    SettingsActivity.this.requestPermissions(new String[]{BoloPermission.RECORD_AUDIO}, 2000);
                                    SettingsActivity.this.automatic_call_rec.setChecked(false);
                                    return;
                                }
                                Helper.onCallRecordingTermAndConditionApproved();
                                Helper.recordCallAutomaticUpdate(true);
                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity.4.1
                            @Override 
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SettingsActivity.this.automatic_call_rec.setChecked(false);
                            }
                        });
                        builder.create().show();
                        return;
                    }
                    Helper.recordCallAutomaticUpdate(false);
                }
            });
        }
        ((LinearLayout) findViewById(R.id.caller_name_layout)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity.5
            @Override 
            public void onClick(View view) {
                if (SettingsActivity.this.enable_caller_name.isChecked()) {
                    SettingsActivity.this.enable_caller_name.setChecked(false);
                } else {
                    SettingsActivity.this.enable_caller_name.setChecked(true);
                }
            }
        });
        SwitchCompat switchCompat2 = (SwitchCompat) findViewById(R.id.caller_name_switch);
        this.enable_caller_name = switchCompat2;
        switchCompat2.setChecked(PreferenceUtils.getInstance().getBoolean(Constants.CALLER_NAME));
        this.enable_caller_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity.6
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                PreferenceUtils.getInstance().putPreference(Constants.CALLER_NAME, z);
                if (!z || PreferenceUtils.getInstance().getBoolean(Constants.CALLER_NAME_INFO_TOAST, false)) {
                    return;
                }
                Toast.makeText(SettingsActivity.this, (int) R.string.caller_name_on_msg, 0).show();
                PreferenceUtils.getInstance().putPreference(Constants.CALLER_NAME_INFO_TOAST, true);
            }
        });
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.flash_layout);
        if (!Utility.isFlashlightSupport(this)) {
            linearLayout.setVisibility(8);
        }
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity.7
            @Override 
            public void onClick(View view) {
                if (SettingsActivity.this.led_flash.isChecked()) {
                    SettingsActivity.this.led_flash.setChecked(false);
                } else {
                    SettingsActivity.this.led_flash.setChecked(true);
                }
            }
        });
        SwitchCompat switchCompat3 = (SwitchCompat) findViewById(R.id.flash_switch);
        this.led_flash = switchCompat3;
        switchCompat3.setChecked(PreferenceUtils.getInstance().getBoolean(Constants.LED_FLASH));
        this.led_flash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity.8
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                PreferenceUtils.getInstance().putPreference(Constants.LED_FLASH, z);
            }
        });
        ((LinearLayout) findViewById(R.id.enable_block_layout)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity.9
            @Override 
            public void onClick(View view) {
                if (SettingsActivity.this.enable_callblocker.isChecked()) {
                    SettingsActivity.this.enable_callblocker.setChecked(false);
                } else {
                    SettingsActivity.this.enable_callblocker.setChecked(true);
                }
            }
        });
        SwitchCompat switchCompat4 = (SwitchCompat) findViewById(R.id.enable_block);
        this.enable_callblocker = switchCompat4;
        switchCompat4.setChecked(ActivityBlockCallList.isCallBlockEnabled());
        this.enable_callblocker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SettingsActivity.this.setEnableDisabled(z);
            }
        });
        ((LinearLayout) findViewById(R.id.call_block_layout)).setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, ActivityBlockCallList.class));
            }
        });
        final View findViewById = findViewById(R.id.view_red_green);
        findViewById.setRotationY(PreferenceUtils.getInstance().getBoolean(Constants.INVERT_CALL_PICK_UP) ? 180.0f : 0.0f);
        findViewById(R.id.call_invert_layout).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.SettingsActivity.16
            @Override 
            public void onClick(View view) {
                boolean z = !PreferenceUtils.getInstance().getBoolean(Constants.INVERT_CALL_PICK_UP);
                findViewById.setRotationY(z ? 180.0f : 0.0f);
                PreferenceUtils.getInstance().putPreference(Constants.INVERT_CALL_PICK_UP, z);
            }
        });
    }
    private void configMediationProvider() {
        if (AperoAd.getInstance().getMediationProvider() == AperoAdConfig.PROVIDER_ADMOB) {
            idInter = getResources().getString(R.string.admob_inter_id);
        } else {
            idInter = "c630fe3686063741";
        }
    }

    private void loadAdInterstitial() {

        mInterstitialAd = AperoAd.getInstance().getInterstitialAds(this, idInter);
    }

    @Override
    public void onBackPressed() {
        if (mInterstitialAd.isReady()) {

            AperoAd.getInstance().showInterstitialAdByTimes(SettingsActivity.this, mInterstitialAd, new AperoAdCallback() {
                @Override
                public void onNextAction() {
                    startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                }

                @Override
                public void onAdFailedToShow(@Nullable ApAdError adError) {
                    super.onAdFailedToShow(adError);
                }

                @Override
                public void onInterstitialShow() {
                    super.onInterstitialShow();
                }
            }, true);
        } else {
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        }


    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 2000 && ContextCompat.checkSelfPermission(this, BoloPermission.RECORD_AUDIO) == 0) {
            Helper.onCallRecordingTermAndConditionApproved();
            Helper.recordCallAutomaticUpdate(true);
            this.automatic_call_rec.setChecked(true);
        }
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void settingsItemClick(View view) {
        if (view.getId() != R.id.video_call) {
            return;
        }
        SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, VideoCallSettings.class));
    }
}
