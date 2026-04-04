package com.colorcallscreen.colorphone.callscreen.calltheme.utils;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;


public class PausableChronometer extends Chronometer {
    private long timeWhenStopped;

    public PausableChronometer(Context context) {
        super(context);
        this.timeWhenStopped = 0L;
    }

    public long getCurrentTime() {
        return this.timeWhenStopped;
    }

    public void reset() {
        stop();
        setBase(SystemClock.elapsedRealtime());
        this.timeWhenStopped = 0L;
    }

    public void setCurrentTime(long j) {
        this.timeWhenStopped = j;
        setBase(SystemClock.elapsedRealtime() + this.timeWhenStopped);
    }

    @Override // android.widget.Chronometer
    public void start() {
        setBase(SystemClock.elapsedRealtime() + this.timeWhenStopped);
        super.start();
    }

    @Override // android.widget.Chronometer
    public void stop() {
        super.stop();
        this.timeWhenStopped = getBase() - SystemClock.elapsedRealtime();
    }

    public PausableChronometer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.timeWhenStopped = 0L;
    }

    public PausableChronometer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.timeWhenStopped = 0L;
    }
}
