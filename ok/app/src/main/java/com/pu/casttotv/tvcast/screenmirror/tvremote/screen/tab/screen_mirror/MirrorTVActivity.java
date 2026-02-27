package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;

import kotlin.Unit;

@SuppressLint("WrongConstant")
public class MirrorTVActivity extends BaseActivity {
    private Button btnStart;
    private ImageView imv_dot1;
    private ImageView imv_dot2;
    private ImageView imv_dot3;
    private ImageView imv_dot4;
    private LinearLayout llBack;
    private LinearLayout llConnect;
    private TextView tvTitle;
    private TextView tvTitleTab;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.fragment_screen_mirror);

        initView();
        MirrorTVActivity.this.callbackDone();
    }

    public Unit callbackFail() {
        return Unit.INSTANCE;
    }

    public Unit callbackDone() {
        return Unit.INSTANCE;
    }

    public Unit actionCommon() {
        gotoScreen();
        return Unit.INSTANCE;
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initView() {
        this.llConnect = (LinearLayout) findViewById(R.id.llConnect);
        this.llBack = (LinearLayout) findViewById(R.id.llBack);
        this.tvTitleTab = (TextView) findViewById(R.id.tvTitleTab);
        this.tvTitle = (TextView) findViewById(R.id.tvTitle);
        this.btnStart = (Button) findViewById(R.id.btnStart);
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        this.imv_dot1 = (ImageView) findViewById(R.id.imv_dot1);
        this.imv_dot2 = (ImageView) findViewById(R.id.imv_dot2);
        this.imv_dot3 = (ImageView) findViewById(R.id.imv_dot3);
        this.imv_dot4 = (ImageView) findViewById(R.id.imv_dot4);
        int[] iArr = {R.drawable.imv_sc1, R.drawable.imv_sc2, R.drawable.imv_sc3, R.drawable.imv_sc4};
        this.llConnect.setVisibility(8);
        this.llBack.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.MirrorTVActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MirrorTVActivity.this.finish();
                Utils.backScreen(MirrorTVActivity.this);
            }
        });
        this.tvTitleTab.setText("Screen Mirror");
        this.viewPager.setAdapter(new ViewPagerAdapter(this, iArr));
        setPosDot(0);
        this.tvTitle.setText("TV and Phone must be connected\nto the same wifi");
        this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                MirrorTVActivity.this.setPosDot(i);
                if (i == 0) {
                    MirrorTVActivity.this.tvTitle.setText("TV and Phone must be connected\nto the same wifi");
                } else if (i == 1) {
                    MirrorTVActivity.this.tvTitle.setText("Enable Cast Display on your TV");
                }
                if (i == 2) {
                    MirrorTVActivity.this.tvTitle.setText("Enable Wireless Display & Choose your TV");
                }
                if (i == 3) {
                    MirrorTVActivity.this.tvTitle.setText("Successfully Connect");
                }
            }
        });
        this.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
            }
        });
        this.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MirrorTVActivity.this.gotoScreen();
            }
        });
    }

    public void gotoScreen() {
        try {
            startActivity(new Intent("android.settings.WIFI_DISPLAY_SETTINGS"));
        } catch (ActivityNotFoundException e2) {
            e2.printStackTrace();
            try {
                startActivity(new Intent("android.settings.CAST_SETTINGS"));
            } catch (Exception unused) {
                Toast.makeText(this, "Device not supported", 1).show();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPosDot(int i) {
        this.imv_dot1.setImageResource(R.drawable.ic_dot_off);
        this.imv_dot2.setImageResource(R.drawable.ic_dot_off);
        this.imv_dot3.setImageResource(R.drawable.ic_dot_off);
        this.imv_dot4.setImageResource(R.drawable.ic_dot_off);
        if (i == 0) {
            this.imv_dot1.setImageResource(R.drawable.ic_dot_on);
        } else if (i == 1) {
            this.imv_dot2.setImageResource(R.drawable.ic_dot_on);
        } else if (i == 2) {
            this.imv_dot3.setImageResource(R.drawable.ic_dot_on);
        } else if (i != 3) {
        } else {
            this.imv_dot4.setImageResource(R.drawable.ic_dot_on);
        }
    }
}
