package com.pu.casttotv.tvcast.screenmirror.tvremote.utils;

import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: MyActivityObserver.kt */
public final class MyActivityObserver implements DefaultLifecycleObserver {
    @NotNull
    private final Function0<Unit> onStart;

    @Override // androidx.lifecycle.FullLifecycleObserver
    public void onCreate(LifecycleOwner lifecycleOwner) {
       onCreate(lifecycleOwner);
    }

    @Override // androidx.lifecycle.FullLifecycleObserver
    public void onDestroy(LifecycleOwner lifecycleOwner) {
        onDestroy(lifecycleOwner);
    }

    @Override // androidx.lifecycle.FullLifecycleObserver
    public void onPause(LifecycleOwner lifecycleOwner) {
        onPause(lifecycleOwner);
    }

    @Override // androidx.lifecycle.FullLifecycleObserver
    public void onResume(LifecycleOwner lifecycleOwner) {
        onResume(lifecycleOwner);
    }

    @Override // androidx.lifecycle.FullLifecycleObserver
    public void onStop(LifecycleOwner lifecycleOwner) {
        onStop(lifecycleOwner);
    }

    public MyActivityObserver(@NotNull Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "onStart");
        this.onStart = function0;
    }

    @Override // androidx.lifecycle.FullLifecycleObserver
    public void onStart(@NotNull LifecycleOwner lifecycleOwner) {
        Intrinsics.checkNotNullParameter(lifecycleOwner, "owner");
        onStart(lifecycleOwner);
        this.onStart.invoke();
    }
}
