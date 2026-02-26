package com.cast.tv.screen.mirroring.screencasting.supportedices;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.library.info.CastTvAppManager;
import com.cast.tv.screen.mirroring.screencasting.R;


public class AutoModeProcessActivity extends AppCompatActivity implements View.OnClickListener {
    public CheckBox chk1;
    public CheckBox chk2;
    public CheckBox chk3;
    public CheckBox chk4;
    public Handler handler = new Handler();
    public int progressStatus = 0;
    String BACK = "back";
    String action_name = "back";
    TextView atv_process;
    Intent intent;
    ImageView iv_done;
    ProgressBar progressBar;
    String[] str_TV_Name = {"Acer", "Admiral", "Aiwa", "Akai", "Apex", "Audiovox", "Bose", "Bush", "Changhong", "Coby", "Colby", "Condor", "Zenith", "Dura Brand", "Dynex", "Element", "Emerson", "Funai", "Haier", "Hisense", "Hitachi", "Hyundai", "Insignia", "Jensen", "Kenwood", "LG", "Logik", "Magnavox", "Mascom", "Medion", "Micromax", "Mitsubishi", "Mystery", "NEC", "Nexus", "Nikai", "Noblex", "Olevia", "Onida", "Orion", "Palsonic", "Panasonic", "Philco", "Philips", "Pioneer", "Polaroid", "Polytron", "Prima", "Promac", "Proscan", "Proton", "Rubin", "Samsung", "Samsung Smart", "Sansui", "Sanyo", "Scott", "Seiki", "Sharp", "Singer", "Sinotec", "Skyworth", "Sony", "Supra", "Swisstec", "Sylvania", "Symphonic", "TCL", "Technical", "Thomson", "Tokai", "Toshiba", "TurboX", "Upstar", "Venturer", "Veon", "Videocon", "Viore", "Vizio", "Voxson", "Westinghouse", "Multi TV", "SFR", "Start Times", "Total Play", "Trend", "Other"};
    TextView tv_brand;
    int tv_name;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.iuc_auto_mode_process);

        ConstantMethod.BottomNavigationColor(this);
        Intent intent2 = getIntent();
        this.intent = intent2;
        this.tv_name = intent2.getExtras().getInt("brand_name");
//        Log.d(L.TAG, "onClick: process--  " + this.tv_name);

        CastTvAppManager.getInstance(this).showBannerAds(this, findViewById(R.id.fl_banner));
        CastTvAppManager.getInstance(this).showNativeAds(this, findViewById(R.id.fl_native_banner), findViewById(R.id.native_space_img), 2);

        ImageView imageView2 = findViewById(R.id.iv_done);
        this.iv_done = imageView2;
        imageView2.setOnClickListener(this);
        this.atv_process = findViewById(R.id.atv_process);
        TextView appTextView = findViewById(R.id.tv_brand);
        this.tv_brand = appTextView;
        int i = this.tv_name;
        String[] strArr = this.str_TV_Name;
        if (i <= strArr.length) {
            appTextView.setText(strArr[i]);
        }
        this.chk1 = findViewById(R.id.checkBox1);
        this.chk2 = findViewById(R.id.checkBox2);
        this.chk3 = findViewById(R.id.checkBox3);
        this.chk4 = findViewById(R.id.checkBox4);
        this.progressBar = findViewById(R.id.progressBar1);
        this.progressStatus = 0;
        final ProgressBarAnimation progressBarAnimation = new ProgressBarAnimation(this.progressBar, 1000);
        new Thread(new Runnable() {
            public void run() {
                while (AutoModeProcessActivity.this.progressStatus < 100) {
                    AutoModeProcessActivity.this.progressStatus += 5;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AutoModeProcessActivity.this.handler.post(new Runnable() {

                        @SuppressLint("WrongConstant")
                        public void run() {
                            progressBarAnimation.setProgress(AutoModeProcessActivity.this.progressStatus);
                            if (AutoModeProcessActivity.this.progressStatus == 25) {
                                AutoModeProcessActivity.this.chk1.setButtonDrawable(R.drawable.iuc_check);
                            }
                            if (AutoModeProcessActivity.this.progressStatus == 50) {
                                AutoModeProcessActivity.this.chk2.setButtonDrawable(R.drawable.iuc_check);
                            }
                            if (AutoModeProcessActivity.this.progressStatus == 75) {
                                AutoModeProcessActivity.this.chk3.setButtonDrawable(R.drawable.iuc_check);
                            }
                            if (AutoModeProcessActivity.this.progressStatus == 95) {
                                AutoModeProcessActivity.this.chk4.setButtonDrawable(R.drawable.iuc_check);
                            }
                            if (AutoModeProcessActivity.this.progressStatus == 100) {
                                AutoModeProcessActivity.this.iv_done.setVisibility(0);
                                AutoModeProcessActivity.this.atv_process.setText("Process Complete...");
                            }
                            Log.e("showprog", "" + AutoModeProcessActivity.this.progressStatus);
                        }
                    });
                }
            }
        }).start();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_done) {
            enablingWiFiDisplay();
        }
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

    @SuppressLint("WrongConstant")
    public void Open_Setting() {
        try {
            startActivity(new Intent("android.settings.WIFI_DISPLAY_SETTINGS"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
//            startActivity(new Intent("com.samsung.wfd.LAUNCH_WFD_PICKER_DLG"));
            try {
                startActivity(new Intent("android.settings.CAST_SETTINGS"));
            } catch (Exception unused) {
                Toast.makeText(getApplicationContext(), "Device not supported", 1).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void NextScreen() {
        if (this.action_name.equalsIgnoreCase(this.BACK)) {
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        action_name = BACK;
        CastTvAppManager.getInstance(this).showInterstitialBackAd(this, () -> {
            NextScreen();

        });
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
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(AutoModeProcessActivity.this, R.anim.button_push));
                AutoModeProcessActivity.this.Open_Setting();
                AutoModeProcessActivity.this.dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(AutoModeProcessActivity.this, R.anim.button_push));
                AutoModeProcessActivity.this.dialog.dismiss();
            }
        });
        this.dialog.show();
    }

    public class ProgressBarAnimation extends Animation {
        private int mFrom;
        private ProgressBar mProgressBar;
        private long mStepDuration;
        private int mTo;

        public ProgressBarAnimation(ProgressBar progressBar, long j) {
            this.mProgressBar = progressBar;
            this.mStepDuration = j / ((long) progressBar.getMax());
        }

        public void setProgress(int i) {
            if (i < 0) {
                i = 0;
            }
            if (i > this.mProgressBar.getMax()) {
                i = this.mProgressBar.getMax();
            }
            this.mTo = i;
            int progress = this.mProgressBar.getProgress();
            this.mFrom = progress;
            setDuration(((long) Math.abs(this.mTo - progress)) * this.mStepDuration);
            this.mProgressBar.startAnimation(this);
        }

        public void applyTransformation(float f, Transformation transformation) {
            int i = this.mFrom;
            this.mProgressBar.setProgress((int) (((float) i) + (((float) (this.mTo - i)) * f)));
        }
    }
}