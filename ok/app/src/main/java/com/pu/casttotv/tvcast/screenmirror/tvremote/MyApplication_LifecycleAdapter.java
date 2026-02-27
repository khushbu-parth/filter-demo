package com.pu.casttotv.tvcast.screenmirror.tvremote;

import android.annotation.SuppressLint;

import androidx.lifecycle.GeneratedAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MethodCallsLogger;

@SuppressLint("RestrictedApi")
public class MyApplication_LifecycleAdapter implements GeneratedAdapter {
    final MyApplication mReceiver;

    MyApplication_LifecycleAdapter(MyApplication myApplication) {
        this.mReceiver = myApplication;
    }

    @Override // androidx.lifecycle.GeneratedAdapter
    public void callMethods(LifecycleOwner lifecycleOwner, Lifecycle.Event event, boolean z, MethodCallsLogger methodCallsLogger) {
        boolean z2 = methodCallsLogger != null;
        if (!z) {
            if (event == Lifecycle.Event.ON_STOP) {
                if (!z2 || methodCallsLogger.approveCall("onAppBackgrounded", 1)) {
                    this.mReceiver.onAppBackgrounded();
                }
            } else if (event == Lifecycle.Event.ON_START) {
                if (!z2 || methodCallsLogger.approveCall("onAppForegrounded", 1)) {
                    this.mReceiver.onAppForegrounded();
                }
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                if (!z2 || methodCallsLogger.approveCall("onAppPause", 1)) {
                    this.mReceiver.onAppPause();
                }
            } else if (event != Lifecycle.Event.ON_RESUME) {
            } else {
                if (!z2 || methodCallsLogger.approveCall("onAppResume", 1)) {
                    this.mReceiver.onAppResume();
                }
            }
        }
    }
}
