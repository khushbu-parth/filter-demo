package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class IntroManager {
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String PREF_NAME = "HDV-First";
    private final SharedPreferences.Editor editor;
    private final SharedPreferences pref;

    public IntroManager(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        this.pref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        this.editor = edit;
        edit.apply();
    }

    public boolean isFirstTimeLaunch() {
        return this.pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean z) {
        this.editor.putBoolean(IS_FIRST_TIME_LAUNCH, z);
        this.editor.commit();
    }
}
