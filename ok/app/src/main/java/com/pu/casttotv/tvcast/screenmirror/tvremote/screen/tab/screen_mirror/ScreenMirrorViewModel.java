package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import androidx.databinding.BaseObservable;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data.BusMessages;

import org.greenrobot.eventbus.EventBus;

/* loaded from: classes4.dex */
public final class ScreenMirrorViewModel extends BaseObservable {
    private final Context mContext;
    private boolean mHttpServerError;
    private boolean mIsStreaming;
    private String mServerAddress;
    private boolean mWiFiConnected;

    public ScreenMirrorViewModel(Context context) {
        this.mContext = context;
    }

    public void setServerAddress(String str) {
        this.mServerAddress = str;
        notifyPropertyChanged(9);
    }

    public void setPinEnabled(boolean z) {
        notifyPropertyChanged(4);
        notifyPropertyChanged(3);
        notifyPropertyChanged(5);
    }

    public void setPinAutoHide(boolean z) {
        notifyPropertyChanged(2);
    }

    public void setStreamPin(String str) {
        notifyPropertyChanged(2);
    }

    public void setStreaming(boolean z) {
        this.mIsStreaming = z;
        notifyPropertyChanged(10);
        notifyPropertyChanged(11);
        notifyPropertyChanged(2);
    }

    public void setWiFiConnected(boolean z) {
        this.mWiFiConnected = z;
        notifyPropertyChanged(9);
        notifyPropertyChanged(8);
    }

    public void setHttpServerError(boolean z) {
        this.mHttpServerError = z;
    }

    public void setClients(int i) {
        notifyPropertyChanged(1);
    }

    public void setResizeFactor(int i) {
        notifyPropertyChanged(6);
        notifyPropertyChanged(7);
    }

    public void setScreenSize(Point point) {
        notifyPropertyChanged(6);
    }

    public boolean isWiFiConnected() {
        return this.mWiFiConnected;
    }

    public String getServerAddressText() {
        if (this.mWiFiConnected) {
            return this.mServerAddress;
        }
        return this.mContext.getString(R.string.main_activity_no_wifi_connected);
    }

    public String getStartStop() {
        if (this.mIsStreaming) {
            return this.mContext.getString(R.string.stop_mirroring);
        }
        return this.mContext.getString(R.string.start_mirroring);
    }

    public boolean getToggleButtonEnabled() {
        return this.mWiFiConnected && !this.mHttpServerError;
    }

    public void onToggleButtonClick(View view) {
        try {
            if (this.mIsStreaming) {
                EventBus.getDefault().post(new BusMessages("MESSAGE_ACTION_STREAMING_STOP"));
            } else {
                EventBus.getDefault().post(new BusMessages("MESSAGE_ACTION_STREAMING_TRY_START"));
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
