package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.other;

import android.content.Context;
import android.content.SharedPreferences;

public class SamSungRemoteController {
    private static SamSungRemoteController samSungRemoteController;
    private Context context;
    private SamsungRemoteManeger samsungRemoteManeger;
    private SharedPreferences sharedPreferences;

    public static SamSungRemoteController getInstance(Context context2) {
        if (samSungRemoteController == null) {
            samSungRemoteController = new SamSungRemoteController(context2);
        }
        return samSungRemoteController;
    }

    public SamSungRemoteController(Context context2) {
        this.context = context2;
        this.sharedPreferences = context2.getSharedPreferences("SamsungATV", 0);
    }

    public SamsungRemoteManeger getSamsungRemoteManeger() {
        return this.samsungRemoteManeger;
    }

    public void setToken(String str) {
        this.sharedPreferences.edit().putString("TOKEN_SS_REMOTE", str).apply();
    }

    public void setIP(String str) {
        this.sharedPreferences.edit().putString("IP_SAMSUNG_REMOTE", str).apply();
    }

    public void connect(String str, int i, String str2, SamsungRemoteManeger.SamsungConnectListener samsungConnectListener) {
        setIP(str);
        setToken(str2);
        this.samsungRemoteManeger = null;
        SamsungRemoteManeger samsungRemoteManeger2 = new SamsungRemoteManeger(this.context, str, i, samsungConnectListener, this.sharedPreferences);
        this.samsungRemoteManeger = samsungRemoteManeger2;
        samsungRemoteManeger2.connect();
    }
}
