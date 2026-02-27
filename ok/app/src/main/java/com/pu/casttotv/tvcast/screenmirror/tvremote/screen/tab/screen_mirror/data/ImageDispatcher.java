package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data;

import com.pu.casttotv.tvcast.screenmirror.tvremote.MyApplication;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class ImageDispatcher {
    private volatile boolean isThreadRunning;
    private JpegStreamerThread mJpegStreamerThread;
    private final Object mLock = new Object();

    /* loaded from: classes4.dex */
    private class JpegStreamerThread extends Thread {
        private byte[] mCurrentJpeg;
        private byte[] mLastJpeg;
        private int mSleepCount;

        JpegStreamerThread() {
            super(JpegStreamerThread.class.getSimpleName());
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!isInterrupted() && ImageDispatcher.this.isThreadRunning) {
                byte[] poll = MyApplication.getAppData().getImageQueue().poll();
                this.mCurrentJpeg = poll;
                if (poll == null) {
                    try {
                        Thread.sleep(24L);
                        int i = this.mSleepCount + 1;
                        this.mSleepCount = i;
                        if (i >= 20) {
                            sendLastJPEGToClients();
                        }
                    } catch (InterruptedException unused) {
                    }
                } else {
                    this.mLastJpeg = poll;
                    sendLastJPEGToClients();
                }
            }
        }

        private void sendLastJPEGToClients() {
            this.mSleepCount = 0;
            synchronized (ImageDispatcher.this.mLock) {
                if (!ImageDispatcher.this.isThreadRunning) {
                    return;
                }
                Iterator<Client> it = MyApplication.getAppData().getClientQueue().iterator();
                while (it.hasNext()) {
                    it.next().sendClientData(2, this.mLastJpeg, false);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addClient(Socket socket) {
        synchronized (this.mLock) {
            if (!this.isThreadRunning) {
                return;
            }
            try {
                Client client = new Client(socket);
                client.sendClientData(1, null, false);
                MyApplication.getAppData().getClientQueue().add(client);
                MyApplication.getScreenMirrorViewModel().setClients(MyApplication.getAppData().getClientQueue().size());
            } catch (IOException unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void start() {
        synchronized (this.mLock) {
            if (this.isThreadRunning) {
                return;
            }
            JpegStreamerThread jpegStreamerThread = new JpegStreamerThread();
            this.mJpegStreamerThread = jpegStreamerThread;
            jpegStreamerThread.start();
            this.isThreadRunning = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stop(byte[] bArr) {
        synchronized (this.mLock) {
            if (!this.isThreadRunning) {
                return;
            }
            this.isThreadRunning = false;
            this.mJpegStreamerThread.interrupt();
            Iterator<Client> it = MyApplication.getAppData().getClientQueue().iterator();
            while (it.hasNext()) {
                it.next().sendClientData(2, bArr, true);
            }
            MyApplication.getAppData().getClientQueue().clear();
            MyApplication.getScreenMirrorViewModel().setClients(0);
        }
    }
}
