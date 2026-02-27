package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.MyApplication;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data.BusMessages;
import java.util.Random;
import org.greenrobot.eventbus.EventBus;

/* loaded from: classes4.dex */
public final class PreferencesHelper {
    private boolean mAutoChangePin;
    private volatile int mClientTimeout;
    private final Context mContext;
    private String mCurrentPin;
    private boolean mDisableMJPEGCheck;
    private boolean mEnablePin;
    private int mHTMLBackColor;
    private boolean mHidePinOnStart;
    private volatile int mJpegQuality;
    private boolean mMinimizeOnStream;
    private boolean mNewPinOnAppStart;
    private volatile int mResizeFactor;
    private volatile int mSeverPort;
    private final SharedPreferences mSharedPreferences;
    private boolean mStopOnSleep;

    public PreferencesHelper(Context context) {
        this.mContext = context;
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        readSettings();
        if ("NOPIN".equals(this.mCurrentPin) || (this.mEnablePin && this.mNewPinOnAppStart)) {
            generateAndSaveNewPin();
        }
    }

    private void readSettings() {
        this.mMinimizeOnStream = this.mSharedPreferences.getBoolean(this.mContext.getString(R.string.pref_key_minimize_on_stream), true);
        this.mStopOnSleep = this.mSharedPreferences.getBoolean(this.mContext.getString(R.string.pref_key_stop_on_sleep), false);
        this.mDisableMJPEGCheck = this.mSharedPreferences.getBoolean(this.mContext.getString(R.string.pref_key_mjpeg_check), false);
        this.mHTMLBackColor = this.mSharedPreferences.getInt(this.mContext.getString(R.string.pref_key_html_back_color), 0);
        this.mJpegQuality = Integer.parseInt(this.mSharedPreferences.getString(this.mContext.getString(R.string.pref_key_jpeg_quality), "80"));
        this.mResizeFactor = this.mSharedPreferences.getInt(this.mContext.getString(R.string.pref_key_resize_factor), 10);
        this.mEnablePin = this.mSharedPreferences.getBoolean(this.mContext.getString(R.string.pref_key_enable_pin), false);
        this.mHidePinOnStart = this.mSharedPreferences.getBoolean(this.mContext.getString(R.string.pref_key_hide_pin_on_start), true);
        this.mNewPinOnAppStart = this.mSharedPreferences.getBoolean(this.mContext.getString(R.string.pref_key_new_pin_on_app_start), true);
        this.mAutoChangePin = this.mSharedPreferences.getBoolean(this.mContext.getString(R.string.pref_key_auto_change_pin), false);
        this.mCurrentPin = this.mSharedPreferences.getString(this.mContext.getString(R.string.pref_key_set_pin), "NOPIN");
        this.mSeverPort = Integer.parseInt(this.mSharedPreferences.getString(this.mContext.getString(R.string.pref_key_server_port), "8668"));
        this.mClientTimeout = Integer.parseInt(this.mSharedPreferences.getString(this.mContext.getString(R.string.pref_key_client_con_timeout), "3000"));
    }

    public void updatePreference() {
        boolean z = this.mDisableMJPEGCheck;
        int i = this.mHTMLBackColor;
        int i2 = this.mSeverPort;
        boolean z2 = this.mEnablePin;
        String str = this.mCurrentPin;
        readSettings();
        MyApplication.getScreenMirrorViewModel().setResizeFactor(this.mResizeFactor);
        MyApplication.getScreenMirrorViewModel().setPinEnabled(this.mEnablePin);
        MyApplication.getScreenMirrorViewModel().setPinAutoHide(this.mHidePinOnStart);
        MyApplication.getScreenMirrorViewModel().setStreamPin(this.mCurrentPin);
        if (z != this.mDisableMJPEGCheck || i != this.mHTMLBackColor) {
            MyApplication.getAppData().initIndexHtmlPage(this.mContext);
        }
        if (i2 != this.mSeverPort) {
            MyApplication.getScreenMirrorViewModel().setServerAddress(MyApplication.getAppData().getServerAddress());
            EventBus.getDefault().post(new BusMessages("MESSAGE_ACTION_HTTP_RESTART"));
        } else if (z2 == this.mEnablePin && str.equals(this.mCurrentPin)) {
        } else {
            EventBus.getDefault().post(new BusMessages("MESSAGE_ACTION_PIN_UPDATE"));
        }
    }

    public void generateAndSaveNewPin() {
        this.mCurrentPin = getRandomPin();
        SharedPreferences.Editor edit = this.mSharedPreferences.edit();
        edit.putString(this.mContext.getString(R.string.pref_key_set_pin), this.mCurrentPin);
        edit.apply();
    }

    public boolean isMinimizeOnStream() {
        return this.mMinimizeOnStream;
    }

    public boolean isStopOnSleep() {
        return this.mStopOnSleep;
    }

    public boolean isDisableMJPEGCheck() {
        return this.mDisableMJPEGCheck;
    }

    public int getHTMLBackColor() {
        return this.mHTMLBackColor;
    }

    public int getResizeFactor() {
        return this.mResizeFactor;
    }

    public void setResizeFactor(int i) {
        this.mResizeFactor = i;
        this.mSharedPreferences.edit().putInt(this.mContext.getString(R.string.pref_key_resize_factor), this.mResizeFactor).apply();
    }

    public int getJpegQuality() {
        return this.mJpegQuality;
    }

    public boolean isEnablePin() {
        return this.mEnablePin;
    }

    public boolean isAutoChangePin() {
        return this.mAutoChangePin;
    }

    public String getCurrentPin() {
        return this.mCurrentPin;
    }

    public int getSeverPort() {
        return this.mSeverPort;
    }

    public int getClientTimeout() {
        return this.mClientTimeout;
    }

    private static String getRandomPin() {
        Random random = new Random(System.currentTimeMillis());
        return "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
    }
}
