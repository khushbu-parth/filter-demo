package com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.model;

public class YTMedia {
    double approxDurationMs;
    int audioChannels;
    String audioQuality;
    int audioSampleRate;
    double averageBitrate;
    int bitrate;
    double contentLength;
    int fps;
    int height;
    int itag;
    double lastModified;
    String mimeType;
    String projectionType;
    String quality;
    String qualityLabel;
    String signatureCipher;
    String url;
    int width;

    public void setFps(int i) {
        this.fps = i;
    }

    public int getFps() {
        return this.fps;
    }

    public void setItag(int i) {
        this.itag = i;
    }

    public int getItag() {
        return this.itag;
    }

    public void setMimeType(String str) {
        this.mimeType = str;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setBitrate(int i) {
        this.bitrate = i;
    }

    public int getBitrate() {
        return this.bitrate;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public int getHeight() {
        return this.height;
    }

    public void setLastModified(double d) {
        this.lastModified = d;
    }

    public double getLastModified() {
        return this.lastModified;
    }

    public void setContentLength(double d) {
        this.contentLength = d;
    }

    public double getContentLength() {
        return this.contentLength;
    }

    public void setQuality(String str) {
        this.quality = str;
    }

    public String getQuality() {
        return this.quality;
    }

    public void setQualityLabel(String str) {
        this.qualityLabel = str;
    }

    public String getQualityLabel() {
        return this.qualityLabel;
    }

    public void setProjectionType(String str) {
        this.projectionType = str;
    }

    public String getProjectionType() {
        return this.projectionType;
    }

    public void setAverageBitrate(double d) {
        this.averageBitrate = d;
    }

    public double getAverageBitrate() {
        return this.averageBitrate;
    }

    public void setApproxDurationMs(double d) {
        this.approxDurationMs = d;
    }

    public double getApproxDurationMs() {
        return this.approxDurationMs;
    }

    public void setAudioChannels(int i) {
        this.audioChannels = i;
    }

    public int getAudioChannels() {
        return this.audioChannels;
    }

    public void setAudioSampleRate(int i) {
        this.audioSampleRate = i;
    }

    public int getAudioSampleRate() {
        return this.audioSampleRate;
    }

    public void setSignatureCipher(String str) {
        this.signatureCipher = str;
    }

    public String getSignatureCipher() {
        return this.signatureCipher;
    }

    public void setAudioQuality(String str) {
        this.audioQuality = str;
    }

    public String getAudioQuality() {
        return this.audioQuality;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean useCipher() {
        String str = this.signatureCipher;
        return str != null && str.contains("s=");
    }
}
