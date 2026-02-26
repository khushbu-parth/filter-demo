package com.lib.screenrecorder;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;

import java.util.Objects;

class VideoEncoder extends BaseEncoder {
    private static final boolean VERBOSE = false;
    private VideoEncodeConfig mConfig;
    private Surface mSurface;

    public VideoEncoder(VideoEncodeConfig videoEncodeConfig) {
        super(videoEncodeConfig.codecName);
        this.mConfig = videoEncodeConfig;
    }

    @Override
    protected void onEncoderConfigured(MediaCodec mediaCodec) {
        this.mSurface = mediaCodec.createInputSurface();
    }

    @Override
    protected MediaFormat createMediaFormat() {
        return this.mConfig.toFormat();
    }

    public Surface getInputSurface() {
        Surface surface = this.mSurface;
        Objects.requireNonNull(surface, "doesn't prepare()");
        return surface;
    }

    @Override
    public void release() {
        Surface surface = this.mSurface;
        if (surface != null) {
            surface.release();
            this.mSurface = null;
        }
        super.release();
    }

    public VideoEncodeConfig getConfig() {
        return this.mConfig;
    }
}
