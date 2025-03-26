package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.SettingsItem;


public class StoreUserData {
    private static String APP_KEY = null;
    private static final String KEY_SETTINGS = "KEY_SETTINGS";
    private final Context parentActivity;
    private SharedPreferences pref = null;

    public StoreUserData(Context context) {
        this.parentActivity = context;
        APP_KEY = "com.mt.player".replaceAll("\\.", "_").toLowerCase();
    }

    private String getString() {
        SharedPreferences sharedPreferences = this.parentActivity.getSharedPreferences(APP_KEY, 0);
        this.pref = sharedPreferences;
        return sharedPreferences.getString(KEY_SETTINGS, "");
    }

    private void setString(String str) {
        SharedPreferences sharedPreferences = this.parentActivity.getSharedPreferences(APP_KEY, 0);
        this.pref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(KEY_SETTINGS, str);
        edit.apply();
    }

    public SettingsItem getSettings() {
        return (SettingsItem) new Gson().fromJson(getString(), SettingsItem.class);
    }

    public void setSettings(SettingsItem settingsItem) {
        setString(new Gson().toJson((Object) settingsItem));
    }
}
