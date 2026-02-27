package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.os.Handler;
import android.os.HandlerThread;
import com.pu.casttotv.tvcast.screenmirror.tvremote.MyApplication;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.service.ForegroundService;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import org.greenrobot.eventbus.EventBus;

/* loaded from: classes4.dex */
public final class ImageGenerator {
    private ImageReader mImageReader;
    private HandlerThread mImageThread;
    private Bitmap mReusableBitmap;
    private VirtualDisplay mVirtualDisplay;
    private final Object mLock = new Object();
    private final AtomicBoolean isPrepared = new AtomicBoolean();

    /* loaded from: classes4.dex */
    private class ImageAvailableListener implements ImageReader.OnImageAvailableListener {
        private final ByteArrayOutputStream mJpegOutputStream = new ByteArrayOutputStream();
        private final Matrix mMatrix;

        ImageAvailableListener(Matrix matrix) {
            this.mMatrix = matrix;
        }

        @Override // android.media.ImageReader.OnImageAvailableListener
        public void onImageAvailable(ImageReader imageReader) {
            Bitmap bitmap;
            synchronized (ImageGenerator.this.mLock) {
                if (!ImageGenerator.this.isPrepared.get()) {
                    return;
                }
                try {
                    Image acquireLatestImage = imageReader.acquireLatestImage();
                    if (acquireLatestImage == null) {
                        return;
                    }
                    Image.Plane plane = acquireLatestImage.getPlanes()[0];
                    int rowStride = plane.getRowStride() / plane.getPixelStride();
                    if (rowStride > acquireLatestImage.getWidth()) {
                        if (ImageGenerator.this.mReusableBitmap == null) {
                            ImageGenerator.this.mReusableBitmap = Bitmap.createBitmap(rowStride, acquireLatestImage.getHeight(), Bitmap.Config.ARGB_8888);
                        }
                        ImageGenerator.this.mReusableBitmap.copyPixelsFromBuffer(plane.getBuffer());
                        bitmap = Bitmap.createBitmap(ImageGenerator.this.mReusableBitmap, 0, 0, acquireLatestImage.getWidth(), acquireLatestImage.getHeight());
                    } else {
                        Bitmap createBitmap = Bitmap.createBitmap(acquireLatestImage.getWidth(), acquireLatestImage.getHeight(), Bitmap.Config.ARGB_8888);
                        createBitmap.copyPixelsFromBuffer(plane.getBuffer());
                        bitmap = createBitmap;
                    }
                    if (!this.mMatrix.isIdentity()) {
                        Bitmap createBitmap2 = Bitmap.createBitmap(bitmap, 0, 0, acquireLatestImage.getWidth(), acquireLatestImage.getHeight(), this.mMatrix, false);
                        bitmap.recycle();
                        bitmap = createBitmap2;
                    }
                    acquireLatestImage.close();
                    this.mJpegOutputStream.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, MyApplication.getAppPreference().getJpegQuality(), this.mJpegOutputStream);
                    bitmap.recycle();
                    byte[] byteArray = this.mJpegOutputStream.toByteArray();
                    if (byteArray != null) {
                        if (MyApplication.getAppData().getImageQueue().size() > 3) {
                            MyApplication.getAppData().getImageQueue().pollLast();
                        }
                        MyApplication.getAppData().getImageQueue().add(byteArray);
                    }
                } catch (UnsupportedOperationException unused) {
                    EventBus.getDefault().postSticky(new BusMessages("MESSAGE_STATUS_IMAGE_GENERATOR_ERROR"));
                }
            }
        }
    }

    public void start() throws IllegalStateException {
        synchronized (this.mLock) {
            StringBuilder sb = new StringBuilder();
            sb.append("ImageGenerator start: ");
            sb.append(Thread.currentThread().getName());
            if (this.isPrepared.get()) {
                throw new IllegalStateException("ImageGenerator is already running");
            }
            MediaProjection mediaProjection = ForegroundService.getMediaProjection();
            if (mediaProjection == null) {
                throw new IllegalStateException("MediaProjection is null");
            }
            Matrix matrix = new Matrix();
            if (MyApplication.getAppPreference().getResizeFactor() != 10) {
                float resizeFactor = MyApplication.getAppPreference().getResizeFactor() / 10.0f;
                matrix.postScale(resizeFactor, resizeFactor);
            }
            HandlerThread handlerThread = new HandlerThread(ImageGenerator.class.getSimpleName(), -1);
            this.mImageThread = handlerThread;
            handlerThread.start();
            @SuppressLint("WrongConstant") ImageReader newInstance = ImageReader.newInstance(MyApplication.getAppData().getScreenSize().x, MyApplication.getAppData().getScreenSize().y, 1, 2);
            this.mImageReader = newInstance;
            newInstance.setOnImageAvailableListener(new ImageAvailableListener(matrix), new Handler(this.mImageThread.getLooper()));
            try {
                this.mVirtualDisplay = mediaProjection.createVirtualDisplay("ScreenStreamVirtualDisplay", MyApplication.getAppData().getScreenSize().x, MyApplication.getAppData().getScreenSize().y, MyApplication.getAppData().getScreenDensity(), 16, this.mImageReader.getSurface(), null, null);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            this.isPrepared.set(true);
        }
    }

    public void stop() throws IllegalStateException {
        synchronized (this.mLock) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("ImageGenerator stop: ");
                sb.append(Thread.currentThread().getName());
                try {
                } catch (IllegalStateException e2) {
                    e2.printStackTrace();
                }
            } catch (IllegalStateException e3) {
                e3.printStackTrace();
            }
            if (!this.isPrepared.get()) {
                throw new IllegalStateException("ImageGenerator is not running");
            }
            this.isPrepared.set(false);
            VirtualDisplay virtualDisplay = this.mVirtualDisplay;
            if (virtualDisplay != null) {
                virtualDisplay.release();
                this.mVirtualDisplay = null;
            }
            ImageReader imageReader = this.mImageReader;
            if (imageReader != null) {
                imageReader.setOnImageAvailableListener(null, null);
                this.mImageReader.close();
                this.mImageReader = null;
            }
            HandlerThread handlerThread = this.mImageThread;
            if (handlerThread != null) {
                handlerThread.quit();
                this.mImageThread = null;
            }
            Bitmap bitmap = this.mReusableBitmap;
            if (bitmap != null) {
                bitmap.recycle();
                this.mReusableBitmap = null;
            }
        }
    }
}
