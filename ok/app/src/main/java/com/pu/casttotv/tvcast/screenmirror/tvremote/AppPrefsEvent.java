package com.pu.casttotv.tvcast.screenmirror.tvremote;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefsEvent {

    public static final String USER_PREFS = "USER PREFS";
    public SharedPreferences appSharedPref;
    public SharedPreferences.Editor prefEditor;

    public String IsIntroFirstTime = "IsIntroFirstTime";

    public AppPrefsEvent(Context context) {
        this.appSharedPref = context.getSharedPreferences(USER_PREFS, Activity.MODE_PRIVATE);
        this.prefEditor = appSharedPref.edit();
    }

    public boolean getIsIntroFirstTime() {
        return appSharedPref.getBoolean(IsIntroFirstTime, true);
    }

    public void setIsIntroFirstTime(boolean first) {
        this.prefEditor.putBoolean(IsIntroFirstTime, first).commit();
    }

}
