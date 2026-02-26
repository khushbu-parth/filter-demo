package com.cast.tv.screen.mirroring.screencasting.supportedices;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.library.info.CastTvAppManager;
import com.cast.tv.screen.mirroring.screencasting.R;


public class SelectTVActivity extends AppCompatActivity implements View.OnClickListener, OnClickAdd {
    boolean FromAuto;
    ImageView iv_back;
    RecyclerView riv_TVName;
    Activity select_TV_activity = null;
    String[] str_TV_Name = {"Acer", "Admiral", "Aiwa", "Akai", "Apex", "Audiovox", "Bose", "Bush", "Changhong", "Coby", "Colby", "Condor", "Zenith", "Dura Brand", "Dynex", "Element", "Emerson", "Funai", "Haier", "Hisense", "Hitachi", "Hyundai", "Insignia", "Jensen", "Kenwood", "LG", "Logik", "Magnavox", "Mascom", "Medion", "Micromax", "Mitsubishi", "Mystery", "NEC", "Nexus", "Nikai", "Noblex", "Olevia", "Onida", "Orion", "Palsonic", "Panasonic", "Philco", "Philips", "Pioneer", "Polaroid", "Polytron", "Prima", "Promac", "Proscan", "Proton", "Rubin", "Samsung", "Samsung Smart", "Sansui", "Sanyo", "Scott", "Seiki", "Sharp", "Singer", "Sinotec", "Skyworth", "Sony", "Supra", "Swisstec", "Sylvania", "Symphonic", "TCL", "Technical", "Thomson", "Tokai", "Toshiba", "TurboX", "Upstar", "Venturer", "Veon", "Videocon", "Viore", "Vizio", "Voxson", "Westinghouse", "Multi TV", "SFR", "Start Times", "Total Play", "Trend", "Other"};
    TVName_adapter tvName_adapter;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.iuc_select_tv);
        boolean booleanExtra = getIntent().getBooleanExtra("FromAuto", false);
        this.FromAuto = booleanExtra;
        if (!booleanExtra) {
            setContentView(R.layout.iuc_select_tv_2);
        }

        CastTvAppManager.getInstance(this).showBannerAds(this, findViewById(R.id.fl_banner));
        CastTvAppManager.getInstance(this).showNativeAds(this, findViewById(R.id.fl_native_banner), findViewById(R.id.native_space_img), 2);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.riv_TVName);
        this.riv_TVName = recyclerView;
        recyclerView.setHasFixedSize(false);
        this.riv_TVName.setLayoutManager(new GridLayoutManager(this, 3));
        this.riv_TVName.setItemAnimator(new DefaultItemAnimator());
        TVName_adapter tVName_adapter = new TVName_adapter(this.str_TV_Name, this);
        this.tvName_adapter = tVName_adapter;
        this.riv_TVName.setAdapter(tVName_adapter);
    }

    public void onClick(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {

        CastTvAppManager.getInstance(this).showInterstitialBackAd(this, () -> {

            startActivity(new Intent(SelectTVActivity.this, HomeActivity.class));
            finish();
        });


    }

    @Override
    public void ItemClick(int i) {
        if (FromAuto) {
            CastTvAppManager.getInstance(this).showInterstitialAd(this, () -> {
                Intent intent = new Intent(SelectTVActivity.this, AutoModeProcessActivity.class);
                intent.putExtra("brand_name", i);
                startActivity(intent);
            });
            return;
        }
        Manual_Mode_Screen();
    }

    public void Manual_Mode_Screen() {
        enablingWiFiDisplay();
    }

    public void enablingWiFiDisplay() {
        if (HomeActivity.wifi.isWifiEnabled()) {
            wifidisplay();
            return;
        }
        HomeActivity.wifi.setWifiEnabled(true);
        wifidisplay();
    }

    public void wifidisplay() {
        ShowDialog();
    }

    public void Auto_Mode_Process_Screen() {
        startActivity(new Intent(this, AutoModeProcessActivity.class));
    }

    @Override
    public void onResume() {
        try {
            super.onResume();

        } catch (Exception e) {
            e.toString();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void ShowDialog() {
        Dialog dialog2 = new Dialog(this, R.style.TransparentBackground);
        this.dialog = dialog2;
        dialog2.setContentView(R.layout.iuc_confirm_dialog);
        Button button = (Button) this.dialog.findViewById(R.id.btn_ok);
        button.setTypeface(ConstantMethod.ChangeTypeFaceGOTHICB(this));
        Button button2 = (Button) this.dialog.findViewById(R.id.btn_cancel);
        button2.setTypeface(ConstantMethod.ChangeTypeFaceGOTHICB(this));
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(SelectTVActivity.this, R.anim.button_push));
                try {
                    SelectTVActivity.this.startActivity(new Intent("android.settings.WIFI_DISPLAY_SETTINGS"));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
//                    SelectTVActivity.this.startActivity(new Intent("com.samsung.wfd.LAUNCH_WFD_PICKER_DLG"));
                    try {
                        SelectTVActivity.this.startActivity(new Intent("android.settings.CAST_SETTINGS"));
                    } catch (Exception unused) {
                        Toast.makeText(SelectTVActivity.this.getApplicationContext(), "Device not supported", 1).show();
                    }
                }
                SelectTVActivity.this.dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(SelectTVActivity.this, R.anim.button_push));
                SelectTVActivity.this.dialog.dismiss();
            }
        });
        this.dialog.show();
    }
}
