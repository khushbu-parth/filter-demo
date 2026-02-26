package com.lib.screenrecorder;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public abstract class BaseEncoder implements Encoder {
    private Callback mCallback;
    private MediaCodec.Callback mCodecCallback = new MediaCodec.Callback() {
        @Override
        public void onInputBufferAvailable(MediaCodec mediaCodec, int i) {
            BaseEncoder.this.mCallback.onInputBufferAvailable(BaseEncoder.this, i);
        }

        @Override
        public void onOutputBufferAvailable(MediaCodec mediaCodec, int i, MediaCodec.BufferInfo bufferInfo) {
            BaseEncoder.this.mCallback.onOutputBufferAvailable(BaseEncoder.this, i, bufferInfo);
        }

        @Override
        public void onError(MediaCodec mediaCodec, MediaCodec.CodecException codecException) {
            BaseEncoder.this.mCallback.onError(BaseEncoder.this, codecException);
        }

        @Override
        public void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat) {
            BaseEncoder.this.mCallback.onOutputFormatChanged(BaseEncoder.this, mediaFormat);
        }
    };
    private String mCodecName;
    private MediaCodec mEncoder;

    public BaseEncoder(String str) {
        this.mCodecName = str;
    }

    protected abstract MediaFormat createMediaFormat();

    protected void onEncoderConfigured(MediaCodec mediaCodec) {
    }

    @Override
    public void setCallback(Encoder.Callback callback) {
        if (!(callback instanceof Callback)) {
            throw new IllegalArgumentException();
        }
        setCallback((Callback) callback);
    }

    public void setCallback(Callback callback) {
        if (this.mEncoder != null) {
            throw new IllegalStateException("mEncoder is not null");
        }
        this.mCallback = callback;
    }

    @Override
    public void prepare() throws IOException {
        if (Looper.myLooper() == null || Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("should run in a HandlerThread");
        }
        if (this.mEncoder != null) {
            throw new IllegalStateException("prepared!");
        }
        MediaFormat createMediaFormat = createMediaFormat();
        Log.d("Encoder", "Create media format: " + createMediaFormat);
        MediaCodec createEncoder = createEncoder(createMediaFormat.getString("mime"));
        try {
            if (this.mCallback != null) {
                createEncoder.setCallback(this.mCodecCallback);
            }
            createEncoder.configure(createMediaFormat, (Surface) null, (MediaCrypto) null, 1);
            onEncoderConfigured(createEncoder);
            createEncoder.start();
            this.mEncoder = createEncoder;
        } catch (MediaCodec.CodecException e) {
            Log.e("Encoder", "Configure codec failure!\n  with format" + createMediaFormat, e);
            throw e;
        }
    }

    private MediaCodec createEncoder(String str) throws IOException {
        try {
            String str2 = this.mCodecName;
            if (str2 != null) {
                return MediaCodec.createByCodecName(str2);
            }
        } catch (IOException e) {
            Log.w("@@", "Create MediaCodec by name '" + this.mCodecName + "' failure!", e);
        }
        return MediaCodec.createEncoderByType(str);
    }

    public final MediaCodec getEncoder() {
        MediaCodec mediaCodec = this.mEncoder;
        Objects.requireNonNull(mediaCodec, "doesn't prepare()");
        return mediaCodec;
    }

    public final ByteBuffer getOutputBuffer(int i) {
        return getEncoder().getOutputBuffer(i);
    }

    public final ByteBuffer getInputBuffer(int i) {
        return getEncoder().getInputBuffer(i);
    }

    public final void queueInputBuffer(int i, int i2, int i3, long j, int i4) {
        getEncoder().queueInputBuffer(i, i2, i3, j, i4);
    }

    public final void releaseOutputBuffer(int i) {
        getEncoder().releaseOutputBuffer(i, false);
    }

    @Override
    public void stop() {
        MediaCodec mediaCodec = this.mEncoder;
        if (mediaCodec != null) {
            mediaCodec.stop();
        }
    }

    @Override
    public void release() {
        MediaCodec mediaCodec = this.mEncoder;
        if (mediaCodec != null) {
            mediaCodec.release();
            this.mEncoder = null;
        }
    }

    public static abstract class Callback implements Encoder.Callback {
        void onInputBufferAvailable(BaseEncoder baseEncoder, int i) {
        }

        public void onOutputBufferAvailable(BaseEncoder baseEncoder, int i, MediaCodec.BufferInfo bufferInfo) {
        }

        public void onOutputFormatChanged(BaseEncoder baseEncoder, MediaFormat mediaFormat) {
        }
    }
}
