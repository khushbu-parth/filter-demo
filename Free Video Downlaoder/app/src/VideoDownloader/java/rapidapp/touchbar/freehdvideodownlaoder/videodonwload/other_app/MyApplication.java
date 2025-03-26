package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.AppLangSessionManager;

//import com.google.firebase.messaging.FirebaseMessaging;

public class MyApplication extends Application {
    AppLangSessionManager appLangSessionManager;

    public void onCreate() {
        super.onCreate();
//        FirebaseMessaging.getInstance().subscribeToTopic("all");
        this.appLangSessionManager = new AppLangSessionManager(getApplicationContext());
        setLocale(this.appLangSessionManager.getLanguage());
    }

    public void setLocale(String str) {
        if (str.equals("")) {
            str = AppLangSessionManager.KEY_APP_LANGUAGE;
        }
        Log.d("Support", str + "");
        Locale locale = new Locale(str);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
    }
}
