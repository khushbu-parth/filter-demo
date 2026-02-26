package com.lib.screenrecorder;

import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseLongArray;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class MicRecorder implements Encoder {
    private static final String TAG = "MicRecorder";
    private final AudioEncoder mEncoder;
    private final HandlerThread mRecordThread;
    private BaseEncoder.Callback mCallback;
    private CallbackDelegate mCallbackDelegate;
    private int mChannelConfig;
    private int mChannelsSampleRate;
    private AudioRecord mMic;
    private RecordHandler mRecordHandler;
    private int mSampleRate;
    private int mFormat = 2;
    private AtomicBoolean mForceStop = new AtomicBoolean(false);
    private SparseLongArray mFramesUsCache = new SparseLongArray(2);

    public MicRecorder(AudioEncodeConfig audioEncodeConfig) {
        this.mEncoder = new AudioEncoder(audioEncodeConfig);
        int i = audioEncodeConfig.sampleRate;
        this.mSampleRate = i;
        this.mChannelsSampleRate = i * audioEncodeConfig.channelCount;
        this.mChannelConfig = audioEncodeConfig.channelCount == 2 ? 12 : 16;
        this.mRecordThread = new HandlerThread(TAG);
    }

    public static AudioRecord createAudioRecord(int i, int i2, int i3) {
        int minBufferSize = AudioRecord.getMinBufferSize(i, i2, i3);
        if (minBufferSize <= 0) {
            Log.e(TAG, String.format(Locale.US, "Bad arguments: getMinBufferSize(%d, %d, %d)", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3)));
            return null;
        }
        AudioRecord audioRecord = new AudioRecord(1, i, i2, i3, minBufferSize * 2);
        if (audioRecord.getState() != 0) {
            return audioRecord;
        }
        Log.e(TAG, String.format(Locale.US, "Bad arguments to new AudioRecord %d, %d, %d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3)));
        return null;
    }

    @Override
    public void setCallback(Callback callback) {
        this.mCallback = (BaseEncoder.Callback) callback;
    }

    public void setCallback(BaseEncoder.Callback callback) {
        this.mCallback = callback;
    }

    @Override
    public void prepare() throws IOException {
        Looper myLooper = Looper.myLooper();
        Objects.requireNonNull(myLooper, "Should prepare in HandlerThread");
        this.mCallbackDelegate = new CallbackDelegate(myLooper, this.mCallback);
        this.mRecordThread.start();
        RecordHandler recordHandler = new RecordHandler(this.mRecordThread.getLooper());
        this.mRecordHandler = recordHandler;
        recordHandler.sendEmptyMessage(0);
    }

    @Override
    public void stop() {
        CallbackDelegate callbackDelegate = this.mCallbackDelegate;
        if (callbackDelegate != null) {
            callbackDelegate.removeCallbacksAndMessages(null);
        }
        this.mForceStop.set(true);
        RecordHandler recordHandler = this.mRecordHandler;
        if (recordHandler != null) {
            recordHandler.sendEmptyMessage(4);
        }
    }

    @Override
    public void release() {
        RecordHandler recordHandler = this.mRecordHandler;
        if (recordHandler != null) {
            recordHandler.sendEmptyMessage(5);
        }
        this.mRecordThread.quitSafely();
    }

    public void releaseOutputBuffer(int i) {
        Message.obtain(this.mRecordHandler, 3, i, 0).sendToTarget();
    }

    public ByteBuffer getOutputBuffer(int i) {
        return this.mEncoder.getOutputBuffer(i);
    }

    public void feedAudioEncoder(int i) {
        int read;
        if (i < 0 || this.mForceStop.get()) {
            return;
        }
        AudioRecord audioRecord = this.mMic;
        Objects.requireNonNull(audioRecord, "maybe release");
        AudioRecord audioRecord2 = audioRecord;
        boolean z = audioRecord2.getRecordingState() == 1;
        ByteBuffer inputBuffer = this.mEncoder.getInputBuffer(i);
        int position = inputBuffer.position();
        int i2 = (z || (read = audioRecord2.read(inputBuffer, inputBuffer.limit())) < 0) ? 0 : read;
        this.mEncoder.queueInputBuffer(i, position, i2, calculateFrameTimestamp(i2 << 3), z ? 4 : 1);
    }

    private long calculateFrameTimestamp(int i) {
        int i2 = i >> 4;
        long j = this.mFramesUsCache.get(i2, -1L);
        if (j == -1) {
            j = (1000000 * i2) / this.mChannelsSampleRate;
            this.mFramesUsCache.put(i2, j);
        }
        long elapsedRealtimeNanos = (SystemClock.elapsedRealtimeNanos() / 1000) - j;
        long j2 = this.mFramesUsCache.get(-1, -1L);
        if (j2 == -1) {
            j2 = elapsedRealtimeNanos;
        }
        if (elapsedRealtimeNanos - j2 < (j << 1)) {
            elapsedRealtimeNanos = j2;
        }
        this.mFramesUsCache.put(-1, j + elapsedRealtimeNanos);
        return elapsedRealtimeNanos;
    }

    public AudioEncodeConfig getConfig() {
        return this.mEncoder.getConfig();
    }

    public static class CallbackDelegate extends Handler {
        private BaseEncoder.Callback mCallback;

        CallbackDelegate(Looper looper, BaseEncoder.Callback callback) {
            super(looper);
            this.mCallback = callback;
        }

        void onError(final Encoder encoder, final Exception exc) {
            Message.obtain(this, new Runnable() {
                @Override
                public final void run() {
                    CallbackDelegate.this.onError$0$MicRecorder$CallbackDelegate(encoder, exc);
                }
            }).sendToTarget();
        }

        public void onError$0$MicRecorder$CallbackDelegate(Encoder encoder, Exception exc) {
            BaseEncoder.Callback callback = this.mCallback;
            if (callback != null) {
                callback.onError(encoder, exc);
            }
        }

        void onOutputFormatChanged(final BaseEncoder baseEncoder, final MediaFormat mediaFormat) {
            Message.obtain(this, new Runnable() {
                @Override
                public final void run() {
                    CallbackDelegate.this.onOutputFormatChanged$1$MicRecorder$CallbackDelegate(baseEncoder, mediaFormat);
                }
            }).sendToTarget();
        }

        public void onOutputFormatChanged$1$MicRecorder$CallbackDelegate(BaseEncoder baseEncoder, MediaFormat mediaFormat) {
            BaseEncoder.Callback callback = this.mCallback;
            if (callback != null) {
                callback.onOutputFormatChanged(baseEncoder, mediaFormat);
            }
        }

        void onOutputBufferAvailable(final BaseEncoder baseEncoder, final int i, final MediaCodec.BufferInfo bufferInfo) {
            Message.obtain(this, new Runnable() {
                @Override
                public final void run() {
                    CallbackDelegate.this.onOutputBufferAvailable$2$MicRecorder$CallbackDelegate(baseEncoder, i, bufferInfo);
                }
            }).sendToTarget();
        }

        public void onOutputBufferAvailable$2$MicRecorder$CallbackDelegate(BaseEncoder baseEncoder, int i, MediaCodec.BufferInfo bufferInfo) {
            BaseEncoder.Callback callback = this.mCallback;
            if (callback != null) {
                callback.onOutputBufferAvailable(baseEncoder, i, bufferInfo);
            }
        }
    }

    private class RecordHandler extends Handler {
        private LinkedList<MediaCodec.BufferInfo> mCachedInfos;
        private LinkedList<Integer> mMuxingOutputBufferIndices;
        private int mPollRate;

        RecordHandler(Looper looper) {
            super(looper);
            this.mCachedInfos = new LinkedList<>();
            this.mMuxingOutputBufferIndices = new LinkedList<>();
            this.mPollRate = 2048000 / MicRecorder.this.mSampleRate;
        }

        @Override
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                AudioRecord createAudioRecord = MicRecorder.createAudioRecord(MicRecorder.this.mSampleRate, MicRecorder.this.mChannelConfig, MicRecorder.this.mFormat);
                if (createAudioRecord == null) {
                    Log.e(MicRecorder.TAG, "create audio record failure");
                    MicRecorder.this.mCallbackDelegate.onError(MicRecorder.this, new IllegalArgumentException());
                    return;
                }
                createAudioRecord.startRecording();
                MicRecorder.this.mMic = createAudioRecord;
                try {
                    MicRecorder.this.mEncoder.prepare();
                } catch (Exception e) {
                    MicRecorder.this.mCallbackDelegate.onError(MicRecorder.this, e);
                    return;
                }
            } else if (i != 1) {
                if (i == 2) {
                    offerOutput();
                    pollInputIfNeed();
                    return;
                } else if (i == 3) {
                    MicRecorder.this.mEncoder.releaseOutputBuffer(message.arg1);
                    this.mMuxingOutputBufferIndices.poll();
                    pollInputIfNeed();
                    return;
                } else if (i == 4) {
                    if (MicRecorder.this.mMic != null) {
                        MicRecorder.this.mMic.stop();
                    }
                    MicRecorder.this.mEncoder.stop();
                    return;
                } else if (i != 5) {
                    return;
                } else {
                    if (MicRecorder.this.mMic != null) {
                        MicRecorder.this.mMic.release();
                        MicRecorder.this.mMic = null;
                    }
                    MicRecorder.this.mEncoder.release();
                    return;
                }
            }
            if (!MicRecorder.this.mForceStop.get()) {
                int pollInput = pollInput();
                if (pollInput >= 0) {
                    MicRecorder.this.feedAudioEncoder(pollInput);
                    if (MicRecorder.this.mForceStop.get()) {
                        return;
                    }
                    sendEmptyMessage(2);
                    return;
                }
                sendEmptyMessageDelayed(1, this.mPollRate);
            }
        }

        private void offerOutput() {
            while (!MicRecorder.this.mForceStop.get()) {
                MediaCodec.BufferInfo poll = this.mCachedInfos.poll();
                if (poll == null) {
                    poll = new MediaCodec.BufferInfo();
                }
                int dequeueOutputBuffer = MicRecorder.this.mEncoder.getEncoder().dequeueOutputBuffer(poll, 1L);
                if (dequeueOutputBuffer == -2) {
                    MicRecorder.this.mCallbackDelegate.onOutputFormatChanged(MicRecorder.this.mEncoder, MicRecorder.this.mEncoder.getEncoder().getOutputFormat());
                }
                if (dequeueOutputBuffer < 0) {
                    poll.set(0, 0, 0L, 0);
                    this.mCachedInfos.offer(poll);
                    return;
                }
                this.mMuxingOutputBufferIndices.offer(Integer.valueOf(dequeueOutputBuffer));
                MicRecorder.this.mCallbackDelegate.onOutputBufferAvailable(MicRecorder.this.mEncoder, dequeueOutputBuffer, poll);
            }
        }

        private int pollInput() {
            return MicRecorder.this.mEncoder.getEncoder().dequeueInputBuffer(0L);
        }

        private void pollInputIfNeed() {
            if (this.mMuxingOutputBufferIndices.size() > 1 || MicRecorder.this.mForceStop.get()) {
                return;
            }
            removeMessages(1);
            sendEmptyMessageDelayed(1, 0L);
        }
    }
}
