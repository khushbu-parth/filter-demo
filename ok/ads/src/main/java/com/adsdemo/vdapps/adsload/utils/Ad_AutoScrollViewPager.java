package com.adsdemo.vdapps.adsload.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class Ad_AutoScrollViewPager extends ViewPager {
    private double autoScrollFactor = 1.0d;
    private int direction = 1;
    private Handler handler;
    private long interval = 2L * 1000L;
    private boolean isBorderAnimation = true;
    private boolean isCycle = true;
    private Ad_DurationScroller scroller;
    private int slideBorderMode = 0;
    private boolean stopScrollWhenTouch = true;
    private double swipeScrollFactor = 1.0d;

    public Ad_AutoScrollViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        this.handler = new MyHandler(this);
        setViewPagerScroller();
    }

    public void startAutoScroll() {
        Ad_DurationScroller durationScroller = this.scroller;
        if (durationScroller != null) {
            sendScrollMessage((long) (((double) this.interval) + ((((double) durationScroller.getDuration()) / this.autoScrollFactor) * this.swipeScrollFactor)));
        }
    }

    public void stopAutoScroll() {
        this.handler.removeMessages(0);
    }

    public void setSwipeScrollDurationFactor(double d) {
        this.swipeScrollFactor = d;
    }

    public void setAutoScrollDurationFactor(double d) {
        this.autoScrollFactor = d;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void sendScrollMessage(long j) {
        this.handler.removeMessages(0);
        this.handler.sendEmptyMessageDelayed(0, j);
    }

    private void setViewPagerScroller() {
        try {
            Field declaredField = ViewPager.class.getDeclaredField("mScroller");
            declaredField.setAccessible(true);
            Field declaredField2 = ViewPager.class.getDeclaredField("sInterpolator");
            declaredField2.setAccessible(true);
            Ad_DurationScroller durationScroller = new Ad_DurationScroller(getContext(), (Interpolator) declaredField2.get(null));
            this.scroller = durationScroller;
            declaredField.set(this, durationScroller);
        } catch (IllegalAccessException | NoSuchFieldException unused) {
        }
    }

    public void scrollOnce() {
        PagerAdapter adapter = getAdapter();
        int currentItem = getCurrentItem();
        int count = adapter != null ? adapter.getCount() : -100;
        if (adapter != null && count > 1) {
            int i = this.direction == 0 ? -currentItem : currentItem + 1;
            if (i < 0) {
                if (this.isCycle) {
                    setCurrentItem(count - 1, this.isBorderAnimation);
                }
            } else if (i != count) {
                setCurrentItem(i, true);
            } else if (this.isCycle) {
                setCurrentItem(getActualPosition(i));
            }
        }
    }

    private int getActualPosition(int i) {
        PagerAdapter adapter = getAdapter();
        getCurrentItem();
        int count = adapter != null ? adapter.getCount() : -100;
        return i >= count ? i % count : i;
    }

    public long getInterval() {
        return this.interval;
    }

    public void setInterval(long j) {
        this.interval = j;
    }

    public int getDirection() {
        return this.direction == 0 ? 0 : 1;
    }

    public void setDirection(int i) {
        this.direction = i;
    }

    public void setCycle(boolean z) {
        this.isCycle = z;
    }

    public void setStopScrollWhenTouch(boolean z) {
        this.stopScrollWhenTouch = z;
    }

    public int getSlideBorderMode() {
        return this.slideBorderMode;
    }

    public void setSlideBorderMode(int i) {
        this.slideBorderMode = i;
    }

    public void setBorderAnimation(boolean z) {
        this.isBorderAnimation = z;
    }

    /* access modifiers changed from: private */
    public static class MyHandler extends Handler {
        private final WeakReference<Ad_AutoScrollViewPager> autoScrollViewPager;

        public MyHandler(Ad_AutoScrollViewPager autoScrollViewPager2) {
            this.autoScrollViewPager = new WeakReference<>(autoScrollViewPager2);
        }

        public void handleMessage(Message message) {
            Ad_AutoScrollViewPager autoScrollViewPager2;
            super.handleMessage(message);
            if (message.what == 0 && (autoScrollViewPager2 = this.autoScrollViewPager.get()) != null && autoScrollViewPager2.scroller != null) {
                autoScrollViewPager2.scroller.setScrollDurationFactor(autoScrollViewPager2.autoScrollFactor);
                autoScrollViewPager2.scrollOnce();
                autoScrollViewPager2.scroller.setScrollDurationFactor(autoScrollViewPager2.swipeScrollFactor);
                autoScrollViewPager2.sendScrollMessage(autoScrollViewPager2.interval + ((long) autoScrollViewPager2.scroller.getDuration()));
            }
        }
    }
}
