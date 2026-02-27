package com.pu.casttotv.tvcast.screenmirror.tvremote.customview;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.MouseControl;
import java.util.Timer;
import java.util.TimerTask;

public final class TouchCustom implements View.OnTouchListener {
    private float DEFAULT_LINE_SPACING_ADD = 0.0f;
    private ConnectableDevice connectableDevice;
    public boolean f5629g;
    public boolean f5630k;
    public boolean f5631l;
    public float f5632m;
    public float f5633n;
    public float f5634o = Float.NaN;
    public float f5635p = Float.NaN;
    public IMoveLG mListener;
    public long s;
    public TimerTask u;

    public interface IMoveLG {
        void moveCancel();

        void moveDown();
    }

    public void setListener(IMoveLG iMoveLG) {
        this.mListener = iMoveLG;
    }

    public TouchCustom(ConnectableDevice connectableDevice2) {
        new Timer();
        this.connectableDevice = connectableDevice2;
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        float f;
        float f2;
        MouseControl mouseControl;
        MouseControl mouseControl2;
        IMoveLG iMoveLG;
        if (motionEvent.getAction() == 0) {
            IMoveLG iMoveLG2 = this.mListener;
            if (iMoveLG2 != null) {
                iMoveLG2.moveDown();
            }
        } else if (motionEvent.getAction() == 1 && (iMoveLG = this.mListener) != null) {
            iMoveLG.moveCancel();
        }
        boolean z = this.f5630k;
        boolean z2 = this.f5631l;
        this.f5631l = z2 || motionEvent.getPointerCount() > 1;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.f5629g = true;
            this.s = motionEvent.getEventTime();
            this.f5632m = motionEvent.getX();
            this.f5633n = motionEvent.getY();
        } else if (actionMasked == 1) {
            this.f5629g = false;
            this.f5630k = false;
            this.f5631l = false;
            this.f5634o = Float.NaN;
            this.f5635p = Float.NaN;
        }
        if (!Float.isNaN(this.f5634o) || !Float.isNaN(this.f5635p)) {
            float x = motionEvent.getX() - this.f5634o;
            if (!Float.isNaN(x)) {
                float round = (float) Math.round(x);
                float y = motionEvent.getY() - this.f5635p;
                if (!Float.isNaN(y)) {
                    float round2 = (float) Math.round(y);
                    f = round;
                    f2 = round2;
                } else {
                    throw new IllegalArgumentException("Cannot round NaN value.");
                }
            } else {
                throw new IllegalArgumentException("Cannot round NaN value.");
            }
        } else {
            f2 = this.DEFAULT_LINE_SPACING_ADD;
            f = f2;
        }
        this.f5634o = motionEvent.getX();
        this.f5635p = motionEvent.getY();
        float abs = Math.abs(motionEvent.getX() - this.f5632m);
        float abs2 = Math.abs(motionEvent.getY() - this.f5633n);
        boolean z3 = this.f5629g;
        if (z3 && !this.f5630k && abs > 10.0f && abs2 > 10.0f) {
            this.f5630k = true;
        }
        if (z3 && this.f5630k) {
            float f3 = this.DEFAULT_LINE_SPACING_ADD;
            if (!(f == f3 || f2 == f3)) {
                int i = -1;
                int i2 = f >= f3 ? 1 : -1;
                if (f2 >= f3) {
                    i = 1;
                }
                float p = ((float) i2) * ((float) getP(Math.pow((double) Math.abs(f), 1.1d)));
                float p2 = ((float) i) * ((float) getP(Math.pow((double) Math.abs(f2), 1.1d)));
                if (!this.f5631l) {
                    double d = (double) p;
                    double d2 = (double) p2;
                    ConnectableDevice connectableDevice2 = this.connectableDevice;
                    if (!(connectableDevice2 == null || (mouseControl2 = (MouseControl) connectableDevice2.getCapability(MouseControl.class)) == null)) {
                        mouseControl2.move(d, d2);
                    }
                } else {
                    SystemClock.uptimeMillis();
                    motionEvent.getX();
                    motionEvent.getY();
                }
            }
        } else if (!z3 && !z) {
            ConnectableDevice connectableDevice3 = this.connectableDevice;
            if (!(connectableDevice3 == null || (mouseControl = (MouseControl) connectableDevice3.getCapability(MouseControl.class)) == null)) {
                mouseControl.click();
            }
        } else if (!z3 && z && z2) {
            mouseScroll((double) (motionEvent.getX() - this.f5632m), (double) (motionEvent.getY() - this.f5633n));
        }
        if (this.f5629g) {
            return true;
        }
        this.f5630k = false;
        TimerTask timerTask = this.u;
        if (timerTask == null) {
            return true;
        }
        timerTask.cancel();
        return true;
    }

    private void mouseScroll(double d, double d2) {
        MouseControl mouseControl;
        ConnectableDevice connectableDevice2 = TVConnectUtils.getInstance().getConnectableDevice();
        if (connectableDevice2 != null && (mouseControl = (MouseControl) connectableDevice2.getCapability(MouseControl.class)) != null) {
            mouseControl.scroll(d, d2);
        }
    }

    private int getP(double d) {
        if (Double.isNaN(d)) {
            throw new IllegalArgumentException("Cannot round NaN value.");
        } else if (d > 2.147483647E9d) {
            return Integer.MAX_VALUE;
        } else {
            if (d < -2.147483648E9d) {
                return Integer.MIN_VALUE;
            }
            return (int) Math.round(d);
        }
    }
}
