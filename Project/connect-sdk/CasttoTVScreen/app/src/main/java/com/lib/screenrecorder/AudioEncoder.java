package com.lib.screenrecorder;

import android.media.MediaFormat;

public class AudioEncoder extends BaseEncoder {
    private final AudioEncodeConfig mConfig;

    public AudioEncoder(AudioEncodeConfig audioEncodeConfig) {
        super(audioEncodeConfig.codecName);
        this.mConfig = audioEncodeConfig;
    }

    @Override
    protected MediaFormat createMediaFormat() {
        return this.mConfig.toFormat();
    }

    public AudioEncodeConfig getConfig() {
        return this.mConfig;
    }
}
