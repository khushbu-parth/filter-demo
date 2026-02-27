package com.adsdemo.vdapps.adsload;

import android.content.Context;
import android.content.SharedPreferences;

public class AdsTimer {
    private Context myActivity;

    public AdsTimer(Context context) {
        this.myActivity = context;
    }

    public int getTimeVar(String str) {
        try {
            Context context = this.myActivity;
            SharedPreferences sharedPreferences = context.getSharedPreferences("var_" + this.myActivity.getPackageName(), 0);
            return sharedPreferences.getInt(str, 0);
        } catch (Exception unused) {
            unused.printStackTrace();
            return 0;
        }
    }

    public void setTimeVar(String str, int i) {
        try {
            Context context = this.myActivity;
            SharedPreferences sharedPreferences = context.getSharedPreferences("var_" + this.myActivity.getPackageName(), 0);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putInt(str, i);
            edit.commit();
        } catch (Exception unused) {
            unused.printStackTrace();
        }
    }

    public int getTimeNativeVar(String str) {
        try {
            Context context = this.myActivity;
            SharedPreferences sharedPreferences = context.getSharedPreferences("var_" + this.myActivity.getPackageName(), 0);
            return sharedPreferences.getInt(str, 0);
        } catch (Exception unused) {
            unused.printStackTrace();
            return 0;
        }
    }

    public void setTimeNativeVar(String str, int i) {
        try {
            Context context = this.myActivity;
            SharedPreferences sharedPreferences = context.getSharedPreferences("var_" + this.myActivity.getPackageName(), 0);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putInt(str, i);
            edit.commit();
        } catch (Exception unused) {
            unused.printStackTrace();
        }
    }
}