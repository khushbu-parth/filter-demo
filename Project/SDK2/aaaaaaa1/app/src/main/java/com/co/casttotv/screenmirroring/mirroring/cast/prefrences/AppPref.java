package com.co.casttotv.screenmirroring.mirroring.cast.prefrences;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPref {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    private static final String KEY_HISTORY = "history";

    public static void init(Context context) {
        preferences = context.getSharedPreferences(AppPref.class.getSimpleName(), Context.MODE_PRIVATE);
        editor = preferences.edit();
    }


    public static String getBrowserHistory() {
        return preferences.getString(KEY_HISTORY, "");
    }

    public static void setBrowserHistory(String value) {
        editor.putString(KEY_HISTORY, value).apply();
    }
}
