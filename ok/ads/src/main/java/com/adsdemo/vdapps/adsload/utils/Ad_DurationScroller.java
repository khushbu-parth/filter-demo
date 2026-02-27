package com.adsdemo.vdapps.adsload.utils;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class Ad_DurationScroller extends Scroller {
    private double scrollFactor = 1.0d;

    public Ad_DurationScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public void setScrollDurationFactor(double d) {
        this.scrollFactor = d;
    }

    public void startScroll(int i, int i2, int i3, int i4, int i5) {
        super.startScroll(i, i2, i3, i4, (int) (((double) i5) * this.scrollFactor));
    }
}
