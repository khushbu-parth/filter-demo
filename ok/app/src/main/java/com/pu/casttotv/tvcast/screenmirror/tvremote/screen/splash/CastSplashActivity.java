package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.splash;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.MainActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.SharedPrefsUtil;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Locale;

public final class CastSplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        language();
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callHome();

            }
        },3000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public final void language() {
        try {
            String str = (String) SharedPrefsUtil.getInstance().get("KEY_LANGUAGE_SAVE", String.class);
            Locale locale = new Locale(str);
            Resources resources = getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, displayMetrics);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void callHome() {
        File sub = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Fake Video Call");
        if (!sub.exists()) {
            sub.mkdirs();
        }
        startActivity(new Intent(CastSplashActivity.this, MainActivity.class));
    }
}
