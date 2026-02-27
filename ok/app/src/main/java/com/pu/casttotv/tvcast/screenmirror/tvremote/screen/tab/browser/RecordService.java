package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.browser;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.WindowManager;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint("WrongConstant")
public class RecordService extends Service {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ExecutorService executorService;
    private int height;
    private ImageReader imageReader;
    private MediaProjection mediaProjection;
    private RecordServiceListener recordServiceListener;
    private boolean running;
    private int threadCount;
    private VirtualDisplay virtualDisplay;
    private int width;

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        return 1;
    }

    /* loaded from: classes4.dex */
    private class ScreenHandler extends Handler {
        public ScreenHandler(RecordService recordService, Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return new RecordBinder(this);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        new HandlerThread("service_thread", 10).start();
        this.running = false;
        this.threadCount = Runtime.getRuntime().availableProcessors();
        StringBuilder sb = new StringBuilder();
        sb.append("onCreate: threadCount");
        sb.append(this.threadCount);
        this.executorService = Executors.newFixedThreadPool(this.threadCount);
        HandlerThread handlerThread = new HandlerThread("Screen Record");
        handlerThread.start();
        new ScreenHandler(this, handlerThread.getLooper());
        WindowManager windowManager = (WindowManager) getApplication().getSystemService("window");
        this.width = windowManager.getDefaultDisplay().getWidth();
        this.height = windowManager.getDefaultDisplay().getHeight();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onCreate: w is ");
        sb2.append(this.width);
        sb2.append(" h is ");
        sb2.append(this.height);
        this.imageReader = ImageReader.newInstance(this.width, this.height, 1, 2);
        Schedulers.from(this.executorService);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        stopRecord();
        this.compositeDisposable.dispose();
    }

    public boolean stopRecord() {
        if (!this.running) {
            return false;
        }
        this.running = false;
        VirtualDisplay virtualDisplay = this.virtualDisplay;
        if (virtualDisplay != null) {
            virtualDisplay.release();
            this.virtualDisplay = null;
        }
        MediaProjection mediaProjection = this.mediaProjection;
        if (mediaProjection != null) {
            mediaProjection.stop();
            this.mediaProjection = null;
        }
        ExecutorService executorService = this.executorService;
        if (executorService != null && !executorService.isShutdown()) {
            this.executorService.shutdown();
            this.executorService = null;
        }
        ImageReader imageReader = this.imageReader;
        if (imageReader != null) {
            imageReader.close();
        }
        RecordServiceListener recordServiceListener = this.recordServiceListener;
        if (recordServiceListener == null) {
            return true;
        }
        recordServiceListener.onRecorderStatusChanged(this.running);
        return true;
    }

    /* loaded from: classes4.dex */
    public class RecordBinder extends Binder {
        public RecordBinder(RecordService recordService) {
        }
    }
}
