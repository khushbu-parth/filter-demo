package com.pu.casttotv.tvcast.screenmirror.tvremote;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.adsdemo.vdapps.adsload.AppOpenManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.ObjectLanguage;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.ScreenMirrorViewModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data.AppData;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data.local.PreferencesHelper;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.SharedPrefsUtil;
import com.connectsdk.DeviceConnectService;
import com.connectsdk.discovery.DiscoveryManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import java.util.ArrayList;
import java.util.Locale;

public class MyApplication extends Application implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private static MyApplication application;
    private static RequestQueue requestQueue;
    private AppData mAppData;
    private PreferencesHelper mPreferencesHelper;
    private ScreenMirrorViewModel mScreenMirrorViewModel;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onAppPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onAppResume() {
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        application = this;
        Fresco.initialize(this);
        DiscoveryManager.init(getApplicationContext());
        DiscoveryManager.getInstance().start();
        DeviceConnectService.enqueueWork(this, new Intent());
        registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        initLanguage();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        this.mAppData = new AppData(this);
        this.mPreferencesHelper = new PreferencesHelper(this);
        this.mScreenMirrorViewModel = new ScreenMirrorViewModel(this);



    }

    public static AppData getAppData() {
        return application.mAppData;
    }

    public static PreferencesHelper getAppPreference() {
        return application.mPreferencesHelper;
    }

    public static ScreenMirrorViewModel getScreenMirrorViewModel() {
        return application.mScreenMirrorViewModel;
    }

    private void initLanguage() {
        if ((Boolean) SharedPrefsUtil.getInstance().get("KEY_INIT_LANGUAGE", Boolean.class)) {
            return;
        }
        SharedPrefsUtil.getInstance().put("KEY_INIT_LANGUAGE", Boolean.TRUE);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ObjectLanguage(getString(R.string.lag_english), ""));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_russian), "ru"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_french), "fr"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_spanish), "es"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_turkish), "tr"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_japan), "ja"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_kr), "kr"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_indonesian), "in"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_hindi), "hi"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_norwegian), "no"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_finnish), "fi"));
        arrayList.add(new ObjectLanguage(getString(R.string.lag_vn), "vi"));
        String language = Locale.getDefault().getLanguage();
        for (int i = 0; i < arrayList.size(); i++) {
            if (language.equals(((ObjectLanguage) arrayList.get(i)).getKey())) {
                SharedPrefsUtil.getInstance().put("KEY_LANGUAGE_SAVE", language);
            }
        }
    }

    public static MyApplication getInstance() {
        return application;
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
