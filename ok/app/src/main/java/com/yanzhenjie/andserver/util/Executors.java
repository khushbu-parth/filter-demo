package com.yanzhenjie.andserver.util;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public final class Executors {
    private static Executors instance;
    private static Handler mHandler;
    private final ExecutorService mService = java.util.concurrent.Executors.newCachedThreadPool();

    public static Executors getInstance() {
        if (instance == null) {
            synchronized (Executors.class) {
                if (instance == null) {
                    instance = new Executors();
                }
            }
        }
        return instance;
    }

    private Executors() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void execute(Runnable runnable) {
        this.mService.execute(runnable);
    }

    public Future<?> submit(Runnable runnable) {
        return this.mService.submit(runnable);
    }

    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }
}
