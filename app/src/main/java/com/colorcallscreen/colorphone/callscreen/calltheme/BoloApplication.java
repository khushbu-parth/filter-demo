package com.colorcallscreen.colorphone.callscreen.calltheme;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.colorcallscreen.colorphone.callscreen.calltheme.Splash.BaseApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.BoloCallHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.singleton.BoloSingleTon;


public class BoloApplication extends BaseApplication {
    private static BoloApplication applicationInstance;
    public boolean isInitDone = false;
    String strpass = "manmanga:jiotum@456";

    public static BoloApplication getApplication() {
        return applicationInstance;
    }

    public void initSettings(boolean z) {
        if (this.isInitDone) {
            return;
        }
        this.isInitDone = true;
        if (!PreferenceUtils.getInstance().getBoolean(Constants.APP_INIT)) {
            Helper.defaultSetting();
        }
        BoloSingleTon.getInstance(this);
        PreferenceUtils.getInstance().putPreference(Constants.APP_INIT, true);
        BoloCallHandler.getInstance().startPhoneStateService(this);
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        applicationInstance = this;
        PreferenceUtils.getInstance().init(applicationInstance);
    }


    public static boolean isConnected(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
                return false;
            }
            return activeNetworkInfo.isConnected();
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
            return false;
        }
    }
}
