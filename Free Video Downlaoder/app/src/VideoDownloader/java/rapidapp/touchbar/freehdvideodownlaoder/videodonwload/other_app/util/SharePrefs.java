package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefs {
    public static String COOKIES = "Cookies";
    public static String CSRF = "csrf";
    public static String ISINSTALOGIN = "IsInstaLogin";
    public static String ISSHOWHOWTOCHINGARI = "IsShoHowToChingari";
    public static String ISSHOWHOWTOFB = "IsShoHowToFB";
    public static String ISSHOWHOWTOINSTA = "IsShoHowToInsta";
    public static String ISSHOWHOWTOJOSH = "IsShoHowToJosh";
    public static String ISSHOWHOWTOLIKEE = "IsShoHowToLikee";
    public static String ISSHOWHOWTOMITRON = "IsShoHowToMitron";
    public static String ISSHOWHOWTOROPOSO = "IsShoHowToRoposo";
    public static String ISSHOWHOWTOSHARECHAT = "IsShoHowToSharechat";
    public static String ISSHOWHOWTOSNACK = "IsShoHowToSnack";
    public static String ISSHOWHOWTOTT = "IsShoHowToTT";
    public static String ISSHOWHOWTOTWITTER = "IsShoHowToTwitter";
    public static String PREFERENCE = "AllInOneDownloader";
    public static String SESSIONID = "session_id";
    public static String USERID = "user_id";
    private static SharePrefs instance;
    private Context ctx;
    private SharedPreferences sharedPreferences;

    public SharePrefs(Context context) {
        this.ctx = context;
        this.sharedPreferences = context.getSharedPreferences(PREFERENCE, 0);
    }

    public static SharePrefs getInstance(Context context) {
        if (instance == null) {
            instance = new SharePrefs(context);
        }
        return instance;
    }

    public void putString(String str, String str2) {
        this.sharedPreferences.edit().putString(str, str2).apply();
    }

    public String getString(String str) {
        return this.sharedPreferences.getString(str, "");
    }

    public void putInt(String str, Integer num) {
        this.sharedPreferences.edit().putInt(str, num.intValue()).apply();
    }

    public void putBoolean(String str, Boolean bool) {
        this.sharedPreferences.edit().putBoolean(str, bool.booleanValue()).apply();
    }

    public Boolean getBoolean(String str) {
        return Boolean.valueOf(this.sharedPreferences.getBoolean(str, false));
    }

    public int getInt(String str) {
        return this.sharedPreferences.getInt(str, 0);
    }

    public void clearSharePrefs() {
        this.sharedPreferences.edit().clear().apply();
    }
}
