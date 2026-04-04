package com.colorcallscreen.colorphone.callscreen.calltheme.base;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;


public class BaseActivity extends AppCompatActivity {
    public Toolbar toolbar;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        PreferenceUtils.getInstance().init(getApplicationContext());
        Helper.setDefaultTheme();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        try {
            super.onResume();
            BoloApplication.getApplication().initSettings(false);
        } catch (Exception unused) {
        }
        Helper.createDir();
    }
}
