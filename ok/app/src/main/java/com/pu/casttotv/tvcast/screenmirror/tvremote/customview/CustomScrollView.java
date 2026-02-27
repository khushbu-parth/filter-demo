package com.pu.casttotv.tvcast.screenmirror.tvremote.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {
    private boolean enableScrolling = true;

    public boolean isEnableScrolling() {
        return this.enableScrolling;
    }

    public void setEnableScrolling(boolean z) {
        this.enableScrolling = z;
    }

    public CustomScrollView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public CustomScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (isEnableScrolling()) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (isEnableScrolling()) {
            return super.onTouchEvent(motionEvent);
        }
        return false;
    }
}
