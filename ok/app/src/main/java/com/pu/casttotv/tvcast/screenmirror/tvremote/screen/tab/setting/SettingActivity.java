package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.setting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pu.casttotv.tvcast.screenmirror.tvremote.BuildConfig;
import com.pu.casttotv.tvcast.screenmirror.tvremote.Common;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.language.DialogLanguage;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.ObjectLanguage;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.PrivacyPolicyActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.splash.CastSplashActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.howto.HowToYouActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Const;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.SharedPrefsUtil;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.other.SharefUtils;

import java.util.ArrayList;

import kotlin.Unit;

@SuppressLint("WrongConstant")
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llBack;
    private LinearLayout llConnect;
    private RelativeLayout rl_hty1;
    private RelativeLayout rl_hty2;
    private RelativeLayout rl_hty3;
    private RelativeLayout rl_language;
    private RelativeLayout rl_privacy;
    private RelativeLayout rl_rate;
    private RelativeLayout rl_share;
    private ToggleButton tbVibrate;
    private TextView tvNameLanguage;
    private TextView tvTitleTab;
    private TextView tv_version;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_setting);
        AdsManager.CallNativeAdLoad(this, findViewById(R.id.native_container), AdsManager.NATIVE_MIDEUM);

        this.tvTitleTab = (TextView) findViewById(R.id.tvTitleTab);
        this.llConnect = (LinearLayout) findViewById(R.id.llConnect);
        this.tbVibrate = (ToggleButton) findViewById(R.id.tbVibrate);
        this.tv_version = (TextView) findViewById(R.id.tv_version);
        this.rl_language = (RelativeLayout) findViewById(R.id.rl_language);
        this.rl_hty1 = (RelativeLayout) findViewById(R.id.rl_hty1);
        this.rl_hty2 = (RelativeLayout) findViewById(R.id.rl_hty2);
        this.rl_hty3 = (RelativeLayout) findViewById(R.id.rl_hty3);
        this.rl_rate = (RelativeLayout) findViewById(R.id.rl_rate);
        this.rl_share = (RelativeLayout) findViewById(R.id.rl_share);
        this.tvNameLanguage = (TextView) findViewById(R.id.tvNameLanguage);
        this.rl_privacy = (RelativeLayout) findViewById(R.id.rl_privacy);
        this.llBack = (LinearLayout) findViewById(R.id.llBack);
        this.rl_language.setOnClickListener(this);
        this.rl_hty1.setOnClickListener(this);
        this.llBack.setOnClickListener(this);
        this.rl_hty2.setOnClickListener(this);
        this.rl_hty3.setOnClickListener(this);
        this.rl_rate.setOnClickListener(this);
        this.rl_share.setOnClickListener(this);
        this.rl_privacy.setOnClickListener(this);
        SettingActivity.this.callbackDone();
        this.llConnect.setVisibility(8);
        this.tvTitleTab.setText(getString(R.string.action_settings));
        boolean hapticFeedBack = SharefUtils.getInstance(this).getHapticFeedBack();
        try {
            if (((Boolean) SharedPrefsUtil.getInstance().get(Const.KEY_RATED, Boolean.class)).booleanValue()) {
                this.rl_rate.setVisibility(8);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.tbVibrate.setChecked(hapticFeedBack);
        this.tbVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SharefUtils.getInstance(SettingActivity.this).setHapticFeedBack(z);
            }
        });
        this.tv_version.setText(String.format(getString(R.string.about_version), BuildConfig.VERSION_NAME));
        getData();
    }

    private void getData() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ObjectLanguage(getString(R.string.lag_english), ""));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_russian), "ru"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_french), "fr"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_spanish), "es"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_turkish), "tr"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_japan), "ja"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_kr), "kr"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_indonesian), "in"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_hindi), "hi"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_norwegian), "no"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_finnish), "fi"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_vn), "vi"));
        String str = (String) SharedPrefsUtil.getInstance().get("KEY_LANGUAGE_SAVE", String.class);
        if (str == null || str.isEmpty()) {
            this.tvNameLanguage.setText(getString(R.string.language_en));
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (((ObjectLanguage) arrayList.get(i)).getKey().equalsIgnoreCase(str)) {
                this.tvNameLanguage.setText(((ObjectLanguage) arrayList.get(i)).getName());
                return;
            }
        }
    }

    public Unit callbackFail() {
        return Unit.INSTANCE;
    }

    public Unit callbackDone() {
        return Unit.INSTANCE;
    }

    private void gotoHowToYou(int i) {
        Intent intent = new Intent(this, HowToYouActivity.class);
        intent.putExtra("TYPE_HTY", i);
        startActivity(intent);
        Utils.nextScreen(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                onBackPressed();
                return;
            case R.id.rl_hty1:
                AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                    gotoHowToYou(3);
                });

                return;
            case R.id.rl_hty2:
                AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                    gotoHowToYou(2);
                });
                return;
            case R.id.rl_hty3:
                AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                    gotoHowToYou(1);
                });
                return;
            case R.id.rl_language:
                AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                    DialogLanguage dialogLanguage = new DialogLanguage(this);
                    dialogLanguage.setListener(new DialogLanguage.ClickLanguage() {
                        @Override
                        public void clickItem(ObjectLanguage objectLanguage) {
                            SettingActivity.this.gotoActivity(CastSplashActivity.class);
                            SettingActivity.this.finish();
                        }
                    });
                    dialogLanguage.show();
                });
                return;
            case R.id.rl_privacy:
                AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                    Intent intent = new Intent(this, PrivacyPolicyActivity.class);
                    startActivity(intent);
                    Utils.nextScreen(this);
                });
                return;
            case R.id.rl_rate:
                Common.rateUs(this);
                return;
            case R.id.rl_share:
                Common.shareApp(this);
            default:
                return;
        }
    }

    @Override
    public void onBackPressed() {
        AdsManager.CallInterstitialAdLoad(this, 1, () -> {
            super.onBackPressed();
        });
    }
}
