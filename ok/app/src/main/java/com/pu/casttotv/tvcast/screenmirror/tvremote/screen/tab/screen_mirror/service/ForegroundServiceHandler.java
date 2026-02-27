package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.service;

import android.media.projection.MediaProjection;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.pu.casttotv.tvcast.screenmirror.tvremote.MyApplication;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data.ImageGenerator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class ForegroundServiceHandler extends Handler {
    private boolean mCurrentOrientation;
    private final ImageGenerator mImageGenerator;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ForegroundServiceHandler(Looper looper) {
        super(looper);
        this.mImageGenerator = new ImageGenerator();
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 0) {
            if (MyApplication.getAppData().isStreamRunning()) {
                return;
            }
            removeMessages(20);
            this.mCurrentOrientation = getOrientation();
            this.mImageGenerator.start();
            sendMessageDelayed(obtainMessage(20), 250L);
            MyApplication.getAppData().setStreamRunning(true);
        } else if (i == 1) {
            try {
                if (!MyApplication.getAppData().isStreamRunning()) {
                    return;
                }
                removeMessages(20);
                removeMessages(1);
                ImageGenerator imageGenerator = this.mImageGenerator;
                if (imageGenerator != null) {
                    imageGenerator.stop();
                }
                MediaProjection mediaProjection = ForegroundService.getMediaProjection();
                if (mediaProjection != null) {
                    mediaProjection.stop();
                }
                MyApplication.getAppData().setStreamRunning(false);
            } catch (IllegalStateException e2) {
                e2.printStackTrace();
            }
        } else if (i == 10) {
            try {
                if (!MyApplication.getAppData().isStreamRunning()) {
                    return;
                }
                this.mImageGenerator.stop();
                sendMessageDelayed(obtainMessage(11), 250L);
            } catch (IllegalStateException e3) {
                e3.printStackTrace();
            }
        } else if (i == 11) {
            if (!MyApplication.getAppData().isStreamRunning()) {
                return;
            }
            this.mImageGenerator.start();
            sendMessageDelayed(obtainMessage(20), 250L);
        } else if (i != 20 || !MyApplication.getAppData().isStreamRunning()) {
        } else {
            boolean orientation = getOrientation();
            if (this.mCurrentOrientation != orientation) {
                this.mCurrentOrientation = orientation;
                obtainMessage(10).sendToTarget();
                return;
            }
            sendMessageDelayed(obtainMessage(20), 250L);
        }
    }

    private boolean getOrientation() {
        int rotation = MyApplication.getAppData().getWindowsManager().getDefaultDisplay().getRotation();
        return rotation == 0 || rotation == 2;
    }
}
