package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.content.Context;
import android.view.ViewConfiguration;

/* loaded from: classes4.dex */
public abstract class SwipeDirectionDetector {
    private int touchSlop;

    /* loaded from: classes4.dex */
    public enum Direction {
        NOT_DETECTED,
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public SwipeDirectionDetector(Context context) {
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
}
