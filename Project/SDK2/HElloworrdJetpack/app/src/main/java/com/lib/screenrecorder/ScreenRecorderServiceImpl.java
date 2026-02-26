package com.lib.screenrecorder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodecInfo;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScreenRecorderServiceImpl extends Service {
    private static final String TAG = "ScreenRecorderService";
    private ScreenRecorderServiceProxy mScreenRecorderServiceProxy;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mScreenRecorderServiceProxy = getScreenRecorderServiceProxy(this);
    }

    @Override
    public final IBinder onBind(Intent intent) {
        return this.mScreenRecorderServiceProxy;
    }

    @Override
    public void onDestroy() {
        ScreenRecorderServiceProxy screenRecorderServiceProxy = this.mScreenRecorderServiceProxy;
        if (screenRecorderServiceProxy != null) {
            screenRecorderServiceProxy.destroyRecorder();
        }
        super.onDestroy();
    }

    ScreenRecorderServiceProxy getScreenRecorderServiceProxy(Context context) {
        return new ScreenRecorderServiceProxy(context);
    }

    public static class ScreenRecorderService implements IScreenRecorderService {
        AudioEncodeConfig mAudioEncodeConfig;
        IRecorderCallback mCallback;
        Context mContext;
        WorkHandler mHandler;
        boolean mIsLandscape;
        MediaProjection mMediaProjection;
        MediaProjection.Callback mProjectionCallback = new MediaProjection.Callback() {
            @Override
            public void onStop() {
                ScreenRecorderService.this.stopRecorder();
            }
        };
        ScreenRecorder mScreenRecorder;
        VideoEncodeConfig mVideoEncodeConfig;
        VirtualDisplay mVirtualDisplay;

        ScreenRecorderService(Context context) {
            this.mContext = context;
            Utils.initMediaCodecInfo();
            HandlerThread handlerThread = new HandlerThread("ScreenRecorderService_" + System.currentTimeMillis());
            handlerThread.start();
            this.mHandler = new WorkHandler(this, handlerThread.getLooper());
        }

        @Override
        public void registerRecorderCallback(IRecorderCallback iRecorderCallback) {
            this.mCallback = iRecorderCallback;
        }

        @Override
        public void prepareAndStartRecorder(MediaProjection mediaProjection, VideoEncodeConfig videoEncodeConfig, AudioEncodeConfig audioEncodeConfig) {
            if (mediaProjection == null) {
                Log.e(ScreenRecorderServiceImpl.TAG, "prepareAndStartRecorder,media projection is null");
            } else {
                this.mHandler.obtainMessage(1, new PrepareRecorderParam(mediaProjection, audioEncodeConfig, videoEncodeConfig)).sendToTarget();
            }
        }

        void handlePrepareAndStartRecorder(PrepareRecorderParam prepareRecorderParam) {
            handleStopMediaProjection();
            MediaProjection mediaProjection = prepareRecorderParam.mediaProjection;
            this.mMediaProjection = mediaProjection;
            mediaProjection.registerCallback(this.mProjectionCallback, new Handler());
            handleStartRecorder(prepareRecorderParam);
        }

        @Override
        public void startRecorder(VideoEncodeConfig videoEncodeConfig, AudioEncodeConfig audioEncodeConfig) {
            this.mHandler.obtainMessage(2, new PrepareRecorderParam(this.mMediaProjection, audioEncodeConfig, videoEncodeConfig)).sendToTarget();
        }

        void handleStartRecorder(PrepareRecorderParam prepareRecorderParam) {
            handleStopRecorder(false);
            this.mAudioEncodeConfig = prepareRecorderParam.audioEncodeConfig;
            VideoEncodeConfig videoEncodeConfig = prepareRecorderParam.videoEncodeConfig;
            this.mVideoEncodeConfig = videoEncodeConfig;
            if (videoEncodeConfig == null) {
                this.mVideoEncodeConfig = VideoEncodeConfig.Builder.create().build();
            }
            if (this.mAudioEncodeConfig == null) {
                this.mAudioEncodeConfig = AudioEncodeConfig.Builder.create().build();
            }
            prepareScreenRecorder();
            if (this.mScreenRecorder == null) {
                Log.e(ScreenRecorderServiceImpl.TAG, "startRecorder,prepare ScreenRecorder failure");
                return;
            }
            IRecorderCallback iRecorderCallback = this.mCallback;
            if (iRecorderCallback != null) {
                iRecorderCallback.onPrepareRecord();
            }
            this.mScreenRecorder.start();
        }

        @Override
        public void startRecorder() {
            startRecorder(this.mVideoEncodeConfig, this.mAudioEncodeConfig);
        }

        @Override
        public void stopRecorder() {
            this.mHandler.sendEmptyMessage(3);
        }

        void handleStopMediaProjection() {
            VirtualDisplay virtualDisplay = this.mVirtualDisplay;
            if (virtualDisplay != null) {
                virtualDisplay.setSurface(null);
                this.mVirtualDisplay.release();
                this.mVirtualDisplay = null;
            }
            MediaProjection mediaProjection = this.mMediaProjection;
            if (mediaProjection != null) {
                mediaProjection.unregisterCallback(this.mProjectionCallback);
                this.mMediaProjection.stop();
                this.mMediaProjection = null;
            }
        }

        void handleStopRecorder(boolean z) {
            if (z) {
                handleStopMediaProjection();
            }
            ScreenRecorder screenRecorder = this.mScreenRecorder;
            if (screenRecorder == null) {
                Log.e(ScreenRecorderServiceImpl.TAG, "stopRecorder,ScreenRecorder has not been initialized yet");
                return;
            }
            screenRecorder.quit();
            this.mScreenRecorder = null;
        }

        @Override
        public void destroyRecorder() {
            this.mHandler.sendEmptyMessage(4);
        }

        void handleDestroyRecorder() {
            handleStopRecorder(true);
            WorkHandler workHandler = this.mHandler;
            if (workHandler != null) {
                workHandler.removeCallbacksAndMessages(null);
                this.mHandler.getLooper().quitSafely();
                this.mHandler = null;
            }
            this.mVideoEncodeConfig = null;
            this.mAudioEncodeConfig = null;
            this.mContext = null;
            IRecorderCallback iRecorderCallback = this.mCallback;
            if (iRecorderCallback != null) {
                iRecorderCallback.onDestroyRecord();
                this.mCallback = null;
            }
        }

        @Override
        public String getSavingFilePath() {
            ScreenRecorder screenRecorder = this.mScreenRecorder;
            if (screenRecorder == null) {
                Log.e(ScreenRecorderServiceImpl.TAG, "getSavingFilePath,ScreenRecorder has not been initialized yet");
                return "";
            }
            return screenRecorder.getSavedPath();
        }

        @Override
        public AudioEncodeConfig getAudioEncodeConfig() {
            ScreenRecorder screenRecorder = this.mScreenRecorder;
            if (screenRecorder == null) {
                Log.e(ScreenRecorderServiceImpl.TAG, "getAudioEncodeConfig,ScreenRecorder has not been initialized yet");
                return null;
            }
            return screenRecorder.getAudioEncoder().getConfig();
        }

        @Override
        public VideoEncodeConfig getVideoEncodeConfig() {
            ScreenRecorder screenRecorder = this.mScreenRecorder;
            if (screenRecorder == null) {
                Log.e(ScreenRecorderServiceImpl.TAG, "getVideoEncodeConfig,ScreenRecorder has not been initialized yet");
                return null;
            }
            return screenRecorder.getVideoEncoder().getConfig();
        }

        @Override
        public boolean hasPrepared() {
            return this.mMediaProjection != null;
        }

        void onConfigurationChanged(Configuration configuration) {
            this.mIsLandscape = configuration.orientation == 2;
        }

        void prepareScreenRecorder() {
            if (this.mMediaProjection == null) {
                Log.e(ScreenRecorderServiceImpl.TAG, "prepareScreenRecorder,media projection is null");
            } else if (hasPermissions()) {
                MediaCodecInfo.VideoCapabilities videoCapabilities = getVideoCodecInfo(this.mVideoEncodeConfig.codecName).getCapabilitiesForType("video/avc").getVideoCapabilities();
                if (!videoCapabilities.isSizeSupported(this.mVideoEncodeConfig.width, this.mVideoEncodeConfig.height)) {
                    Log.w(ScreenRecorderServiceImpl.TAG, "prepareScreenRecorder,unSupport size," + this.mVideoEncodeConfig.codecName + " height range: " + videoCapabilities.getSupportedHeights() + "\n width range: " + videoCapabilities.getSupportedHeights());
                    return;
                }
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "Screenshots");
                if (!file.exists() && !file.mkdirs()) {
                    throw new RuntimeException("create file failure");
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.CHINA);
                File file2 = new File(file, "ScreenRecord_" + simpleDateFormat.format(new Date()) + "_" + this.mVideoEncodeConfig.width + "x" + this.mVideoEncodeConfig.height + ".mp4");
                StringBuilder sb = new StringBuilder();
                sb.append("prepareScreenRecorder,Create recorder with :");
                sb.append(this.mVideoEncodeConfig);
                sb.append(" \n ");
                sb.append(this.mAudioEncodeConfig);
                sb.append("\n ");
                sb.append(file2);
                Log.d(ScreenRecorderServiceImpl.TAG, sb.toString());
                this.mScreenRecorder = newRecorder(this.mMediaProjection, this.mVideoEncodeConfig, this.mAudioEncodeConfig, file2);
            } else {
                throw new RuntimeException("Permission denied! Screen recorder is cancel.");
            }
        }

        ScreenRecorder newRecorder(MediaProjection mediaProjection, VideoEncodeConfig videoEncodeConfig, AudioEncodeConfig audioEncodeConfig, File file) {
            ScreenRecorder screenRecorder = new ScreenRecorder(videoEncodeConfig, audioEncodeConfig, getOrCreateVirtualDisplay(mediaProjection, videoEncodeConfig), file.getAbsolutePath());
            screenRecorder.setCallback(this.mCallback);
            return screenRecorder;
        }

        VirtualDisplay getOrCreateVirtualDisplay(MediaProjection mediaProjection, VideoEncodeConfig videoEncodeConfig) {
            if (this.mVirtualDisplay == null) {
                this.mVirtualDisplay = mediaProjection.createVirtualDisplay("ScreenRecorder-display0", videoEncodeConfig.width, videoEncodeConfig.height, 1, 1, null, null, null);
            } else {
                Point point = new Point();
                this.mVirtualDisplay.getDisplay().getSize(point);
                if (point.x != videoEncodeConfig.width || point.y != videoEncodeConfig.height) {
                    this.mVirtualDisplay.resize(videoEncodeConfig.width, videoEncodeConfig.height, 1);
                }
            }
            return this.mVirtualDisplay;
        }

        boolean hasPermissions() {
            PackageManager packageManager = this.mContext.getPackageManager();
            String packageName = this.mContext.getPackageName();
            return (packageManager.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", packageName) | packageManager.checkPermission("android.permission.RECORD_AUDIO", packageName)) == 0;
        }

        MediaCodecInfo getVideoCodecInfo(String str) {
            MediaCodecInfo[] mediaCodecInfoArr;
            if (str == null) {
                return null;
            }
            for (MediaCodecInfo mediaCodecInfo : Utils.getmAvcCodecInfos()) {
                if (mediaCodecInfo.getName().equals(str)) {
                    return mediaCodecInfo;
                }
            }
            return null;
        }

        public static class WorkHandler extends Handler {
            private ScreenRecorderService mService;

            WorkHandler(ScreenRecorderService screenRecorderService, Looper looper) {
                super(looper);
                this.mService = screenRecorderService;
            }

            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                int i = message.what;
                if (i == 1) {
                    this.mService.handlePrepareAndStartRecorder((PrepareRecorderParam) message.obj);
                } else if (i == 2) {
                    this.mService.handleStartRecorder((PrepareRecorderParam) message.obj);
                } else if (i == 3) {
                    this.mService.handleStopRecorder(true);
                } else if (i != 4) {
                } else {
                    this.mService.handleDestroyRecorder();
                }
            }
        }

        public static class PrepareRecorderParam {
            private AudioEncodeConfig audioEncodeConfig;
            private MediaProjection mediaProjection;
            private VideoEncodeConfig videoEncodeConfig;

            private PrepareRecorderParam(MediaProjection mediaProjection, AudioEncodeConfig audioEncodeConfig, VideoEncodeConfig videoEncodeConfig) {
                this.mediaProjection = mediaProjection;
                this.audioEncodeConfig = audioEncodeConfig;
                this.videoEncodeConfig = videoEncodeConfig;
            }
        }
    }

    public static class ScreenRecorderServiceProxy extends Binder implements IScreenRecorderServiceProxy {
        IScreenRecorderService mScreenRecorderService;

        ScreenRecorderServiceProxy(Context context) {
            this.mScreenRecorderService = getScreenRecorderService(context);
        }

        IScreenRecorderService getScreenRecorderService(Context context) {
            return new ScreenRecorderService(context);
        }

        @Override
        public IScreenRecorderService get() {
            return this.mScreenRecorderService;
        }

        @Override
        public void registerRecorderCallback(IRecorderCallback iRecorderCallback) {
            this.mScreenRecorderService.registerRecorderCallback(iRecorderCallback);
        }

        @Override
        public void prepareAndStartRecorder(MediaProjection mediaProjection, VideoEncodeConfig videoEncodeConfig, AudioEncodeConfig audioEncodeConfig) {
            this.mScreenRecorderService.prepareAndStartRecorder(mediaProjection, videoEncodeConfig, audioEncodeConfig);
        }

        @Override
        public void startRecorder(VideoEncodeConfig videoEncodeConfig, AudioEncodeConfig audioEncodeConfig) {
            this.mScreenRecorderService.startRecorder(videoEncodeConfig, audioEncodeConfig);
        }

        @Override
        public void startRecorder() {
            this.mScreenRecorderService.startRecorder();
        }

        @Override
        public void stopRecorder() {
            this.mScreenRecorderService.stopRecorder();
        }

        @Override
        public void destroyRecorder() {
            this.mScreenRecorderService.destroyRecorder();
        }

        @Override
        public String getSavingFilePath() {
            return this.mScreenRecorderService.getSavingFilePath();
        }

        @Override
        public AudioEncodeConfig getAudioEncodeConfig() {
            return this.mScreenRecorderService.getAudioEncodeConfig();
        }

        @Override
        public VideoEncodeConfig getVideoEncodeConfig() {
            return this.mScreenRecorderService.getVideoEncodeConfig();
        }

        @Override
        public boolean hasPrepared() {
            return this.mScreenRecorderService.hasPrepared();
        }
    }
}
