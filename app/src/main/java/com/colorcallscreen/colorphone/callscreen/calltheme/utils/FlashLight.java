package com.colorcallscreen.colorphone.callscreen.calltheme.utils;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;


public class FlashLight {
    private static FlashLight get;
    private String cameraID;
    private CameraManager manager;
    private Timer timer;
    private boolean isFlashOn = false;
    int count = 0;

    public FlashLight(Context context) {
        if (context == null || Build.VERSION.SDK_INT < 23) {
            return;
        }
        CameraManager cameraManager = (CameraManager) context.getSystemService("camera");
        this.manager = cameraManager;
        try {
            this.cameraID = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (Exception unused) {
        }
    }

    public static FlashLight get(Context context) {
        if (get == null) {
            get = new FlashLight(context);
        }
        return get;
    }

    public void blink(int i, final int i2) {
        this.count = 0;
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
        }
        Timer timer2 = new Timer();
        this.timer = timer2;
        timer2.schedule(new TimerTask() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.FlashLight.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (FlashLight.this.isFlashOn) {
                    FlashLight.this.count++;
                    FlashLight.this.turn(false);
                } else {
                    FlashLight.this.turn(true);
                }
                if (FlashLight.this.count == i2) {
                    FlashLight.this.timer.cancel();
                }
            }
        }, 0L, i);
    }

    public String getCameraID() {
        if (this.cameraID == null) {
            try {
                this.cameraID = this.manager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                e.printStackTrace();
            } catch (Exception unused) {
            }
        }
        return this.cameraID;
    }

    public void stopBlinking() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            Log.d("flash", "Timer cancel");
        }
        if (this.isFlashOn) {
            turn(false);
        }
        turn(false);
        Log.d("flash", "turn off call");
        Log.d("flash", "stop blinking");
    }

    public void turn(boolean z) {
        Log.d("flash", "" + z);
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                String cameraID = getCameraID();
                if (cameraID != null && this.isFlashOn != z) {
                    this.manager.setTorchMode(cameraID, z);
                    this.isFlashOn = z;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void turnOffFlash() {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                this.manager.setTorchMode(this.cameraID, true);
                this.isFlashOn = false;
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void turnOnFlash() {
        if (Build.VERSION.SDK_INT < 23 || this.isFlashOn) {
            return;
        }
        try {
            this.manager.setTorchMode(getCameraID(), true);
            this.isFlashOn = true;
        } catch (CameraAccessException e) {
            e.printStackTrace();
            this.isFlashOn = false;
        }
    }
}
