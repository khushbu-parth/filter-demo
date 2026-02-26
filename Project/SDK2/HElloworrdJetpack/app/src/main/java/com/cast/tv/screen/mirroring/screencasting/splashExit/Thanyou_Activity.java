package com.cast.tv.screen.mirroring.screencasting.splashExit;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.library.info.CastTvAppManager;
import com.library.info.BaseAdActivity;
import com.cast.tv.screen.mirroring.screencasting.R;


public class Thanyou_Activity extends AppCompatActivity {

    ImageView exit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fcom_exitactivity);
        getWindow().setFlags(1024, 1024);
        exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (CastTvAppManager.app_vpnEnable == 1) {
            try {
                new BaseAdActivity().disconnectFromVnp();
            } catch (Exception e) {
            }
        }
        finishAffinity();
    }

}
