package com.adsdemo.vdapps.adsload;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.adsdemo.vdapps.R;


public class Ad_Globals {

    public static void setPrefBoolean(Context context, String key, boolean value) {
        String PREF_NAME = context.getString(R.string.app_name);
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Boolean getPrefBoolean(Context context, String key) {
        if (context != null) {
            String PREF_NAME = context.getString(R.string.app_name);
            return context.getSharedPreferences(PREF_NAME, 0).getBoolean(key, false);
        } else {
            return false;
        }
    }

    //PrivacyPolicy
    public static void saveBoolean(Activity activity, boolean isagree, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, isagree);
        editor.apply();
    }

    public static boolean isBoolean(Activity activity, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean bool = prefs.getBoolean(key, false);
        return bool;
    }

    //RateClick
    public static void setRateClick(Activity activity, int i, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, i);
        editor.apply();
    }

    public static int getRateClick(Activity activity, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getInt(key, 0);
    }
}