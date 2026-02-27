package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.pu.casttotv.tvcast.screenmirror.tvremote.AppPrefsEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.IntroModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.MainActivity;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends BaseActivity {
    private Button btnNext;
    private int currentPos = 0;
    private DotsIndicator dotsIndicator;
    private LinearLayout llSkip;
    private ViewPager viewPagerMain;

    static int access$008(IntroActivity introActivity) {
        int i = introActivity.currentPos;
        introActivity.currentPos = i + 1;
        return i;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_intro);
        this.llSkip = (LinearLayout) findViewById(R.id.llSkip);
        this.viewPagerMain = (ViewPager) findViewById(R.id.viewPagerMain);
        this.btnNext = (Button) findViewById(R.id.btnNext);
        this.viewPagerMain.setAdapter(new ViewPagerAdapter(this, modelList()));
        DotsIndicator dotsIndicator2 = (DotsIndicator) findViewById(R.id.dotsIndicator);
        this.dotsIndicator = dotsIndicator2;
        dotsIndicator2.setViewPager(this.viewPagerMain);

        this.viewPagerMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i) {

            }

            @Override
            public void onPageScrolled(int i, float f, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                IntroActivity.this.currentPos = i;
                IntroActivity.this.setClick(i);
            }
        });

        this.btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (IntroActivity.this.currentPos < 2) {
                    IntroActivity.access$008(IntroActivity.this);
                    IntroActivity.this.viewPagerMain.setCurrentItem(IntroActivity.this.currentPos);
                } else {
                    IntroActivity.this.gotoMain();
                }
            }
        });

        this.llSkip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                IntroActivity.this.gotoMain();
            }
        });
    }

    private void gotoMain() {
        new AppPrefsEvent(this).setIsIntroFirstTime(false);
        startActivity(new Intent(this, MainActivity.class));
    }

    private void setClick(int i) {
        if (i == 2) {
            this.btnNext.setText(getString(R.string.get_start));
        } else {
            this.btnNext.setText(getString(R.string.next));
        }
    }

    private List<IntroModel> modelList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new IntroModel(getString(R.string.cast_to_tv), getString(R.string.cast_to_tv_intro), R.drawable.imv_intro_1));
        arrayList.add(new IntroModel(getString(R.string.screen_mirror), getString(R.string.detail_mirror), R.drawable.imv_intro_2));
        arrayList.add(new IntroModel(getString(R.string.remote_to_tv), getString(R.string.detail_remote), R.drawable.imv_intro_3));
        return arrayList;
    }
}