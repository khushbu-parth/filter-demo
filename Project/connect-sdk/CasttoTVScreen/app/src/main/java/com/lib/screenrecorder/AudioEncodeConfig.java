package com.lib.screenrecorder;

import android.media.MediaCodecInfo;
import android.media.MediaFormat;

import java.util.Objects;

public class AudioEncodeConfig {
    final int bitRate;
    final int channelCount;
    final String codecName;
    final String mimeType;
    final int profile;
    final int sampleRate;

    private AudioEncodeConfig(Builder builder) {
        this(builder.codecName, builder.mimeType, builder.bitrate, builder.simpleRate, builder.channelCount, builder.profile);
    }

    public AudioEncodeConfig(String str, String str2, int i, int i2, int i3, int i4) {
        this.codecName = str;
        Objects.requireNonNull(str2);
        this.mimeType = str2;
        this.bitRate = i;
        this.sampleRate = i2;
        this.channelCount = i3;
        this.profile = i4;
    }

    public MediaFormat toFormat() {
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat(this.mimeType, this.sampleRate, this.channelCount);
        createAudioFormat.setInteger("aac-profile", this.profile);
        createAudioFormat.setInteger("bitrate", this.bitRate);
        return createAudioFormat;
    }

    public String toString() {
        return "AudioEncodeConfig{codecName='" + this.codecName + "', mimeType='" + this.mimeType + "', bitRate=" + this.bitRate + ", sampleRate=" + this.sampleRate + ", channelCount=" + this.channelCount + ", profile=" + this.profile + '}';
    }

    public static final class Builder {
        private String codecName;
        private String mimeType = "audio/mp4a-latm";
        private int bitrate = 80000;
        private int simpleRate = 44100;
        private int channelCount = 2;
        private int profile = 1;

        public Builder() {
            MediaCodecInfo[] mediaCodecInfoArr = Utils.getmAacCodecInfos();
            if (mediaCodecInfoArr == null || mediaCodecInfoArr.length <= 0) {
                return;
            }
            this.codecName = mediaCodecInfoArr[0].getName();
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder codecName(String str) {
            this.codecName = str;
            return this;
        }

        public Builder mimeType(String str) {
            this.mimeType = str;
            return this;
        }

        public Builder bitrate(int i) {
            this.bitrate = i;
            return this;
        }

        public Builder simpleRate(int i) {
            this.simpleRate = i;
            return this;
        }

        public Builder channelCount(int i) {
            this.channelCount = i;
            return this;
        }

        public Builder profile(int i) {
            this.profile = i;
            return this;
        }

        public AudioEncodeConfig build() {
            return new AudioEncodeConfig(this);
        }
    }
}
