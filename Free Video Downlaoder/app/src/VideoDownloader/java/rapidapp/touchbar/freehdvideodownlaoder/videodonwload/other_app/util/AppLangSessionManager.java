package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util;

import android.content.Context;
import android.content.SharedPreferences;

public class AppLangSessionManager {
    public static final String KEY_APP_LANGUAGE = "en";
    private static final String PREFER_NAME = "AppLangPref";
    int PRIVATE_MODE = 0;
    Context _context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    public AppLangSessionManager(Context context) {
        this._context = context;
        this.pref = this._context.getSharedPreferences(PREFER_NAME, this.PRIVATE_MODE);
        this.editor = this.pref.edit();
    }

    public String getLanguage() {
        return this.pref.getString(KEY_APP_LANGUAGE, "");
    }

    public void setLanguage(String str) {
        this.editor.putString(KEY_APP_LANGUAGE, str);
        this.editor.commit();
    }
}