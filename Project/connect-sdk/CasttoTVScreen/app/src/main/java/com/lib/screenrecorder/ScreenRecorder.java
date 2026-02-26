package com.lib.screenrecorder;

import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScreenRecorder {
    private static final String TAG = "ScreenRecorder";
    private MicRecorder mAudioEncoder;
    private long mAudioPtsOffset;
    private IRecorderCallback mCallback;
    private String mDstPath;
    private CallbackHandler mHandler;
    private MediaMuxer mMuxer;
    private VideoEncoder mVideoEncoder;
    private long mVideoPtsOffset;
    private VirtualDisplay mVirtualDisplay;
    private HandlerThread mWorker;
    private MediaFormat mVideoOutputFormat = null;
    private MediaFormat mAudioOutputFormat = null;
    private int mVideoTrackIndex = -1;
    private int mAudioTrackIndex = -1;
    private boolean mMuxerStarted = false;
    private AtomicBoolean mForceQuit = new AtomicBoolean(false);
    private AtomicBoolean mIsRunning = new AtomicBoolean(false);
    private LinkedList<Integer> mPendingVideoEncoderBufferIndices = new LinkedList<>();
    private LinkedList<Integer> mPendingAudioEncoderBufferIndices = new LinkedList<>();
    private LinkedList<MediaCodec.BufferInfo> mPendingAudioEncoderBufferInfos = new LinkedList<>();
    private LinkedList<MediaCodec.BufferInfo> mPendingVideoEncoderBufferInfos = new LinkedList<>();

    public ScreenRecorder(VideoEncodeConfig videoEncodeConfig, AudioEncodeConfig audioEncodeConfig, VirtualDisplay virtualDisplay, String str) {
        MicRecorder micRecorder = null;
        this.mVirtualDisplay = virtualDisplay;
        this.mDstPath = str;
        this.mVideoEncoder = new VideoEncoder(videoEncodeConfig);
        this.mAudioEncoder = audioEncodeConfig != null ? new MicRecorder(audioEncodeConfig) : micRecorder;
    }

    public final void quit() {
        this.mForceQuit.set(true);
        if (!this.mIsRunning.get()) {
            release();
        } else {
            signalStop(false);
        }
    }

    public void start() {
        if (this.mWorker != null) {
            throw new IllegalStateException();
        }
        HandlerThread handlerThread = new HandlerThread(TAG);
        this.mWorker = handlerThread;
        handlerThread.start();
        CallbackHandler callbackHandler = new CallbackHandler(this.mWorker.getLooper());
        this.mHandler = callbackHandler;
        callbackHandler.sendEmptyMessage(0);
    }

    public void setCallback(IRecorderCallback iRecorderCallback) {
        this.mCallback = iRecorderCallback;
    }

    public String getSavedPath() {
        return this.mDstPath;
    }

    public void signalEndOfStream() {
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        ByteBuffer allocate = ByteBuffer.allocate(0);
        bufferInfo.set(0, 0, 0L, 4);
        Log.i(TAG, "Signal EOS to muxer ");
        int i = this.mVideoTrackIndex;
        if (i != -1) {
            writeSampleData(i, bufferInfo, allocate);
        }
        int i2 = this.mAudioTrackIndex;
        if (i2 != -1) {
            writeSampleData(i2, bufferInfo, allocate);
        }
        this.mVideoTrackIndex = -1;
        this.mAudioTrackIndex = -1;
    }

    public void record() {
        if (this.mIsRunning.get() || this.mForceQuit.get()) {
            throw new IllegalStateException();
        }
        if (this.mVirtualDisplay == null) {
            throw new IllegalStateException("maybe release");
        }
        this.mIsRunning.set(true);
        try {
            this.mMuxer = new MediaMuxer(this.mDstPath, 0);
            prepareVideoEncoder();
            prepareAudioEncoder();
            this.mVirtualDisplay.setSurface(this.mVideoEncoder.getInputSurface());
            Log.d(TAG, "set surface to display: " + this.mVirtualDisplay.getDisplay());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void muxVideo(int i, MediaCodec.BufferInfo bufferInfo) {
        if (!this.mIsRunning.get()) {
            Log.w(TAG, "muxVideo: Already stopped!");
        } else if (!this.mMuxerStarted || this.mVideoTrackIndex == -1) {
            this.mPendingVideoEncoderBufferIndices.add(Integer.valueOf(i));
            this.mPendingVideoEncoderBufferInfos.add(bufferInfo);
        } else {
            writeSampleData(this.mVideoTrackIndex, bufferInfo, this.mVideoEncoder.getOutputBuffer(i));
            this.mVideoEncoder.releaseOutputBuffer(i);
            if ((bufferInfo.flags & 4) == 0) {
                return;
            }
            Log.d(TAG, "Stop encoder and muxer, since the buffer has been marked with EOS");
            this.mVideoTrackIndex = -1;
            signalStop(true);
        }
    }

    public void muxAudio(int i, MediaCodec.BufferInfo bufferInfo) {
        if (!this.mIsRunning.get()) {
            Log.w(TAG, "muxAudio: Already stopped!");
        } else if (!this.mMuxerStarted || this.mAudioTrackIndex == -1) {
            this.mPendingAudioEncoderBufferIndices.add(Integer.valueOf(i));
            this.mPendingAudioEncoderBufferInfos.add(bufferInfo);
        } else {
            writeSampleData(this.mAudioTrackIndex, bufferInfo, this.mAudioEncoder.getOutputBuffer(i));
            this.mAudioEncoder.releaseOutputBuffer(i);
            if ((bufferInfo.flags & 4) == 0) {
                return;
            }
            Log.d(TAG, "Stop encoder and muxer, since the buffer has been marked with EOS");
            this.mAudioTrackIndex = -1;
            signalStop(true);
        }
    }

    private void writeSampleData(int i, MediaCodec.BufferInfo bufferInfo, ByteBuffer byteBuffer) {
        IRecorderCallback iRecorderCallback;
        boolean z = false;
        if ((bufferInfo.flags & 2) != 0) {
            Log.d(TAG, "Ignoring BUFFER_FLAG_CODEC_CONFIG");
            bufferInfo.size = 0;
        }
        if ((bufferInfo.flags & 4) != 0) {
            z = true;
        }
        if (bufferInfo.size == 0 && !z) {
            Log.d(TAG, "info.size == 0, drop it.");
            byteBuffer = null;
        } else {
            if (bufferInfo.presentationTimeUs != 0) {
                if (i == this.mVideoTrackIndex) {
                    resetVideoPts(bufferInfo);
                } else if (i == this.mAudioTrackIndex) {
                    resetAudioPts(bufferInfo);
                }
            }
            Log.d(TAG, "[" + Thread.currentThread().getId() + "] Got buffer, track=" + i + ", info: size=" + bufferInfo.size + ", presentationTimeUs=" + bufferInfo.presentationTimeUs);
            if (!z && (iRecorderCallback = this.mCallback) != null) {
                iRecorderCallback.onRecording(bufferInfo.presentationTimeUs);
            }
        }
        if (byteBuffer != null) {
            byteBuffer.position(bufferInfo.offset);
            byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
            try {
                if (this.mCallback != null && bufferInfo.size != 0 && byteBuffer.remaining() > 0) {
                    byte[] bArr = new byte[byteBuffer.limit() - byteBuffer.position()];
                    byteBuffer.get(bArr);
                    if (i == this.mVideoTrackIndex) {
                        this.mCallback.onMuxVideo(bArr, bufferInfo.offset, bufferInfo.size, bufferInfo);
                    } else if (i == this.mAudioTrackIndex) {
                        this.mCallback.onMuxAudio(bArr, bufferInfo.offset, bufferInfo.size, bufferInfo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mMuxer.writeSampleData(i, byteBuffer, bufferInfo);
            Log.i(TAG, "Sent " + bufferInfo.size + " bytes to MediaMuxer on track " + i);
        }
    }

    private void resetAudioPts(MediaCodec.BufferInfo bufferInfo) {
        if (this.mAudioPtsOffset == 0) {
            this.mAudioPtsOffset = bufferInfo.presentationTimeUs;
            bufferInfo.presentationTimeUs = 0L;
            return;
        }
        bufferInfo.presentationTimeUs -= this.mAudioPtsOffset;
    }

    private void resetVideoPts(MediaCodec.BufferInfo bufferInfo) {
        if (this.mVideoPtsOffset == 0) {
            this.mVideoPtsOffset = bufferInfo.presentationTimeUs;
            bufferInfo.presentationTimeUs = 0L;
            return;
        }
        bufferInfo.presentationTimeUs -= this.mVideoPtsOffset;
    }

    public void resetVideoOutputFormat(MediaFormat mediaFormat) {
        if (this.mVideoTrackIndex >= 0 || this.mMuxerStarted) {
            throw new IllegalStateException("output format already changed!");
        }
        Log.i(TAG, "Video output format changed.\n New format: " + mediaFormat.toString());
        this.mVideoOutputFormat = mediaFormat;
    }

    public void resetAudioOutputFormat(MediaFormat mediaFormat) {
        if (this.mAudioTrackIndex >= 0 || this.mMuxerStarted) {
            throw new IllegalStateException("output format already changed!");
        }
        Log.i(TAG, "Audio output format changed.\n New format: " + mediaFormat.toString());
        this.mAudioOutputFormat = mediaFormat;
    }

    public void startMuxerIfReady() {
        MediaFormat mediaFormat;
        if (this.mMuxerStarted || (mediaFormat = this.mVideoOutputFormat) == null) {
            return;
        }
        if (this.mAudioEncoder != null && this.mAudioOutputFormat == null) {
            return;
        }
        this.mVideoTrackIndex = this.mMuxer.addTrack(mediaFormat);
        this.mAudioTrackIndex = this.mAudioEncoder == null ? -1 : this.mMuxer.addTrack(this.mAudioOutputFormat);
        this.mMuxer.start();
        this.mMuxerStarted = true;
        Log.i(TAG, "Started media muxer, videoIndex=" + this.mVideoTrackIndex);
        if (this.mPendingVideoEncoderBufferIndices.isEmpty() && this.mPendingAudioEncoderBufferIndices.isEmpty()) {
            return;
        }
        Log.i(TAG, "Mux pending video output buffers...");
        while (true) {
            MediaCodec.BufferInfo poll = this.mPendingVideoEncoderBufferInfos.poll();
            if (poll == null) {
                break;
            }
            muxVideo(this.mPendingVideoEncoderBufferIndices.poll().intValue(), poll);
        }
        if (this.mAudioEncoder != null) {
            while (true) {
                MediaCodec.BufferInfo poll2 = this.mPendingAudioEncoderBufferInfos.poll();
                if (poll2 == null) {
                    break;
                }
                muxAudio(this.mPendingAudioEncoderBufferIndices.poll().intValue(), poll2);
            }
        }
        Log.i(TAG, "Mux pending video output buffers done.");
    }

    private void prepareVideoEncoder() throws IOException {
        this.mVideoEncoder.setCallback(new BaseEncoder.Callback() {
            boolean ranIntoError = false;

            @Override
            public void onOutputBufferAvailable(BaseEncoder baseEncoder, int i, MediaCodec.BufferInfo bufferInfo) {
                Log.i(ScreenRecorder.TAG, "VideoEncoder output buffer available: index=" + i);
                try {
                    ScreenRecorder.this.muxVideo(i, bufferInfo);
                } catch (Exception e) {
                    Log.e(ScreenRecorder.TAG, "Muxer encountered an error! ", e);
                    Message.obtain(ScreenRecorder.this.mHandler, 2, e).sendToTarget();
                }
            }

            @Override
            public void onError(Encoder encoder, Exception exc) {
                this.ranIntoError = true;
                Log.e(ScreenRecorder.TAG, "VideoEncoder ran into an error! ", exc);
                Message.obtain(ScreenRecorder.this.mHandler, 2, exc).sendToTarget();
            }

            @Override
            public void onOutputFormatChanged(BaseEncoder baseEncoder, MediaFormat mediaFormat) {
                ScreenRecorder.this.resetVideoOutputFormat(mediaFormat);
                ScreenRecorder.this.startMuxerIfReady();
            }
        });
        this.mVideoEncoder.prepare();
    }

    private void prepareAudioEncoder() throws IOException {
        MicRecorder micRecorder = this.mAudioEncoder;
        if (micRecorder == null) {
            return;
        }
        micRecorder.setCallback(new BaseEncoder.Callback() {
            boolean ranIntoError = false;

            @Override
            public void onOutputBufferAvailable(BaseEncoder baseEncoder, int i, MediaCodec.BufferInfo bufferInfo) {
                Log.i(ScreenRecorder.TAG, "[" + Thread.currentThread().getId() + "] AudioEncoder output buffer available: index=" + i);
                try {
                    ScreenRecorder.this.muxAudio(i, bufferInfo);
                } catch (Exception e) {
                    Log.e(ScreenRecorder.TAG, "Muxer encountered an error! ", e);
                    Message.obtain(ScreenRecorder.this.mHandler, 2, e).sendToTarget();
                }
            }

            @Override
            public void onOutputFormatChanged(BaseEncoder baseEncoder, MediaFormat mediaFormat) {
                Log.d(ScreenRecorder.TAG, "[" + Thread.currentThread().getId() + "] AudioEncoder returned new format " + mediaFormat);
                ScreenRecorder.this.resetAudioOutputFormat(mediaFormat);
                ScreenRecorder.this.startMuxerIfReady();
            }

            @Override
            public void onError(Encoder encoder, Exception exc) {
                this.ranIntoError = true;
                Log.e(ScreenRecorder.TAG, "MicRecorder ran into an error! ", exc);
                Message.obtain(ScreenRecorder.this.mHandler, 2, exc).sendToTarget();
            }
        });
        micRecorder.prepare();
    }

    private void signalStop(boolean z) {
        this.mHandler.sendMessageAtFrontOfQueue(Message.obtain(this.mHandler, 1, z ? 1 : 0, 0));
    }

    public void stopEncoders() {
        this.mIsRunning.set(false);
        this.mPendingAudioEncoderBufferInfos.clear();
        this.mPendingAudioEncoderBufferIndices.clear();
        this.mPendingVideoEncoderBufferInfos.clear();
        this.mPendingVideoEncoderBufferIndices.clear();
        try {
            VideoEncoder videoEncoder = this.mVideoEncoder;
            if (videoEncoder != null) {
                videoEncoder.stop();
            }
        } catch (IllegalStateException unused) {
        }
        try {
            MicRecorder micRecorder = this.mAudioEncoder;
            if (micRecorder == null) {
                return;
            }
            micRecorder.stop();
        } catch (IllegalStateException unused2) {
        }
    }

    public void release() {
        VirtualDisplay virtualDisplay = this.mVirtualDisplay;
        if (virtualDisplay != null) {
            virtualDisplay.setSurface(null);
            this.mVirtualDisplay.release();
            this.mVirtualDisplay = null;
        }
        this.mAudioOutputFormat = null;
        this.mVideoOutputFormat = null;
        this.mAudioTrackIndex = -1;
        this.mVideoTrackIndex = -1;
        this.mMuxerStarted = false;
        HandlerThread handlerThread = this.mWorker;
        if (handlerThread != null) {
            handlerThread.quitSafely();
            this.mWorker = null;
        }
        VideoEncoder videoEncoder = this.mVideoEncoder;
        if (videoEncoder != null) {
            videoEncoder.release();
            this.mVideoEncoder = null;
        }
        MicRecorder micRecorder = this.mAudioEncoder;
        if (micRecorder != null) {
            micRecorder.release();
            this.mAudioEncoder = null;
        }
        MediaMuxer mediaMuxer = this.mMuxer;
        if (mediaMuxer != null) {
            try {
                mediaMuxer.stop();
                this.mMuxer.release();
            } catch (Exception unused) {
            }
            this.mMuxer = null;
        }
        CallbackHandler callbackHandler = this.mHandler;
        if (callbackHandler != null) {
            callbackHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
    }

    public VideoEncoder getVideoEncoder() {
        return this.mVideoEncoder;
    }

    public MicRecorder getAudioEncoder() {
        return this.mAudioEncoder;
    }

    protected void finalize() throws Throwable {
        if (this.mVirtualDisplay != null) {
            Log.e(TAG, "release() not called!");
            release();
        }
    }

    public class CallbackHandler extends Handler {
        CallbackHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                try {
                    ScreenRecorder.this.record();
                    if (ScreenRecorder.this.mCallback == null) {
                        return;
                    }
                    ScreenRecorder.this.mCallback.onStartRecord();
                    return;
                } catch (Exception e) {
                    message.obj = e;
                }
            } else if (i != 1 && i != 2) {
                return;
            }
            ScreenRecorder.this.stopEncoders();
            if (message.arg1 != 1) {
                ScreenRecorder.this.signalEndOfStream();
            }
            if (ScreenRecorder.this.mCallback != null) {
                ScreenRecorder.this.mCallback.onStopRecord((Throwable) message.obj);
            }
            ScreenRecorder.this.release();
        }
    }
}
