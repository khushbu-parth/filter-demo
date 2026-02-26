package com.library.info;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CastTvPreferenceManager {
    SharedPreferences preferences;
    private static final String PREFERENCE_NAME = "sdk_kp";
    private static final String KEY_INSTALL_TYPE = "install_type";

    public CastTvPreferenceManager(Activity activity) {
        preferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public String getInstallType() {
        return preferences.getString(KEY_INSTALL_TYPE, "1");
    }

    public void setInstallType() {
        if (getInstallType().equals("1"))
            preferences.edit().putString(KEY_INSTALL_TYPE, "2").apply();
    }

}
