package com.lib.screenrecorder;

import android.media.MediaCodecInfo;
import android.media.MediaFormat;


public class VideoEncodeConfig {
    final int bitrate;
    final String codecName;
    final int codecProfile;
    final int codecProfileLevel;
    final int framerate;
    final int height;
    final int iframeInterval;
    final String mimeType;
    final int width;

    private VideoEncodeConfig(Builder builder) {
        this(builder.width, builder.height, builder.bitrate, builder.framerate, builder.iframeInterval, builder.codecName, builder.mimeType, builder.codecProfile, builder.codecProfileLevel);
    }

    public VideoEncodeConfig(int i, int i2, int i3, int i4, int i5, String str, String str2, int i6, int i7) {
        this.width = i;
        this.height = i2;
        this.bitrate = i3;
        this.framerate = i4;
        this.iframeInterval = i5;
        this.codecName = str;
        this.mimeType = str2;
        this.codecProfile = i6;
        this.codecProfileLevel = i7;
    }

    public MediaFormat toFormat() {
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(this.mimeType, this.width, this.height);
        createVideoFormat.setInteger("color-format", 2130708361);
        createVideoFormat.setInteger("bitrate", this.bitrate);
        createVideoFormat.setInteger("frame-rate", this.framerate);
        createVideoFormat.setInteger("i-frame-interval", this.iframeInterval);
        int i = this.codecProfile;
        if (i != 0 && this.codecProfileLevel != 0) {
            createVideoFormat.setInteger("profile", i);
            createVideoFormat.setInteger("level", this.codecProfileLevel);
        }
        return createVideoFormat;
    }

    public String toString() {
        return "VideoEncodeConfig{width=" + this.width + ", height=" + this.height + ", bitrate=" + this.bitrate + ", framerate=" + this.framerate + ", iframeInterval=" + this.iframeInterval + ", codecName='" + this.codecName + "', mimeType='" + this.mimeType + "', codecProfileLevel=" + this.codecProfile + "-" + this.codecProfileLevel + '}';
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public static final class Builder {
        private String codecName;
        private int codecProfile;
        private int codecProfileLevel;
        private int width = 1080;
        private int height = 1920;
        private int bitrate = 25000000;
        private int framerate = 60;
        private int iframeInterval = 30;
        private String mimeType = "video/avc";

        public Builder() {
            MediaCodecInfo[] mediaCodecInfoArr = Utils.getmAvcCodecInfos();
            if (mediaCodecInfoArr == null || mediaCodecInfoArr.length <= 0) {
                return;
            }
            this.codecName = mediaCodecInfoArr[0].getName();
            MediaCodecInfo.CodecProfileLevel[] codecProfileLevelArr = mediaCodecInfoArr[0].getCapabilitiesForType("video/avc").profileLevels;
            if (codecProfileLevelArr == null || codecProfileLevelArr.length <= 0) {
                return;
            }
            this.codecProfile = codecProfileLevelArr[0].profile;
            this.codecProfileLevel = codecProfileLevelArr[0].level;
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder width(int i) {
            this.width = i;
            return this;
        }

        public Builder height(int i) {
            this.height = i;
            return this;
        }

        public Builder bitrate(int i) {
            this.bitrate = i;
            return this;
        }

        public Builder framerate(int i) {
            this.framerate = i;
            return this;
        }

        public Builder iframeInterval(int i) {
            this.iframeInterval = i;
            return this;
        }

        public Builder codecName(String str) {
            this.codecName = str;
            return this;
        }

        public Builder mimeType(String str) {
            this.mimeType = str;
            return this;
        }

        public Builder codecProfile(int i) {
            this.codecProfile = i;
            return this;
        }

        public Builder codecProfileLevel(int i) {
            this.codecProfileLevel = i;
            return this;
        }

        public Builder codecProfile(MediaCodecInfo.CodecProfileLevel codecProfileLevel) {
            this.codecProfile = codecProfileLevel.profile;
            this.codecProfileLevel = codecProfileLevel.level;
            return this;
        }

        public VideoEncodeConfig build() {
            return new VideoEncodeConfig(this);
        }
    }
}
