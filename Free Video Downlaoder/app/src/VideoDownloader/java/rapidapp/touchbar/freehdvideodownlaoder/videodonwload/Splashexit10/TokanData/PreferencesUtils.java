package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.TokanData;

import android.content.Context;
import android.content.SharedPreferences;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;

public class PreferencesUtils {
    public static String PREF_NAME;
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String SPLASH_1_JSON = "splash1_json";
    public static final String EXIT_JSON = "exit_json";
    public static final String TIME_OF_GET_APP_SPLASH = "time_of_get_app_splash";
    public static final String TIME_OF_GET_APP_EXIT = "time_of_get_app_EXIT";

    private static PreferencesUtils preferencesUtils;
    private static Context mContext;

    public static PreferencesUtils getInstance(Context context) {
        mContext = context;
        PREF_NAME = context.getString(R.string.app_name);
        if (preferencesUtils == null)
            preferencesUtils = new PreferencesUtils();
        return preferencesUtils;
    }

    public String getPrefString(String key) {
        if (mContext != null)
            return mContext.getSharedPreferences(PREF_NAME, 0).getString(key, "");
        else
            return "";
    }

    public void setPrefString(String key, String value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_NAME, 0).edit();
        editor.putString(key, value);
        editor.commit();
    }

}
