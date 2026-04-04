package com.colorcallscreen.colorphone.callscreen.calltheme.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ThemeModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;


public class PreferenceUtils {
    private static final String PREF_UTIL = "perfutilkey";
    private static PreferenceUtils mInstance;
    private Context context;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private boolean isInit = false;
    private SharedPreferences sharedPreferences;

    public static PreferenceUtils getInstance() {
        if (mInstance == null) {
            mInstance = new PreferenceUtils();
        }
        return mInstance;
    }

    public boolean getBoolean(String str) {
        return this.sharedPreferences.getBoolean(str, false);
    }

    public int getInt(String str) {
        return this.sharedPreferences.getInt(str, 0);
    }

    public ThemeModel getLastAppliedTheme() {
        return (ThemeModel) this.gson.fromJson(getString(Constants.LAST_APPLIED_THEME), new TypeToken<ThemeModel>() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils.1
        }.getType());
    }

    public <T> List<T> getList(String str, TypeToken typeToken) {
        return (List) this.gson.fromJson(getString(str), typeToken.getType());
    }

    public Long getLong(String str) {
        return Long.valueOf(getInstance().sharedPreferences.getLong(str, 0L));
    }

    public String getString(String str) {
        return this.sharedPreferences.getString(str, null);
    }

    public ThemeModel getThemeModel() {
        return (ThemeModel) this.gson.fromJson(getString("theme_model"), new TypeToken<ThemeModel>() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils.2
        }.getType());
    }

    public List<ThemeModel> getThemes(String str) {
        String string = getString(str);
        if (string == null || string.isEmpty()) {
            return null;
        }
        return (List) this.gson.fromJson(string, new TypeToken<List<ThemeModel>>() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils.3
        }.getType());
    }

    public void init(Context context) {
        if (this.isInit) {
            return;
        }
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_UTIL, 0);
        this.sharedPreferences = sharedPreferences;
        this.editor = sharedPreferences.edit();
        this.gson = new Gson();
        this.isInit = true;
    }

    public void putPreference(String str, String str2) {
        this.editor.putString(str, str2);
        this.editor.commit();
    }

    public void removeCurrentThemeAndSaveInCache() {
        putPreference(Constants.LAST_APPLIED_THEME, getString("theme_model"));
        getInstance().putPreference(Constants.APPLIED_THEME_NAME, "");
        getInstance().removePreference(Constants.THEME_KEY);
        getInstance().removePreference("theme_model");
    }

    public void removePreference(String str) {
        this.editor.remove(str);
        this.editor.commit();
    }

    public <T> void setList(String str, List<T> list) {
        putPreference(str, this.gson.toJson(list));
    }

    public void setThemeModel(ThemeModel themeModel) {
        putPreference("theme_model", this.gson.toJson(themeModel));
    }

    public void setThemes(List<ThemeModel> list, String str) {
        this.editor.putString(str, this.gson.toJson(list));
        this.editor.commit();
    }

    public boolean getBoolean(String str, boolean z) {
        return this.sharedPreferences.getBoolean(str, z);
    }

    public String getString(String str, String str2) {
        return this.sharedPreferences.getString(str, str2);
    }

    public void putPreference(String str, int i) {
        this.editor.putInt(str, i);
        this.editor.commit();
    }

    public void setThemeModel(ThemeModel themeModel, String str) {
        putPreference(str, this.gson.toJson(themeModel));
    }

    public ThemeModel getThemeModel(String str) {
        String string = getString(str);
        if (string == null) {
            return null;
        }
        return (ThemeModel) this.gson.fromJson(string, new TypeToken<ThemeModel>() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils.4
        }.getType());
    }

    public void putPreference(String str, Long l) {
        this.editor.putLong(str, l.longValue());
        this.editor.commit();
    }

    public void putPreference(String str, boolean z) {
        this.editor.putBoolean(str, z);
        this.editor.commit();
    }
}
