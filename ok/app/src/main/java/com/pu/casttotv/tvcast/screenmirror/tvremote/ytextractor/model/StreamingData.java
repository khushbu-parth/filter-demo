package com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.model;

public class StreamingData {
    private YTMedia[] adaptiveFormats;
    private long expiresInSeconds;
    private YTMedia[] formats;
    private String hlsManifestUrl;

    public void setFormats(YTMedia[] yTMediaArr) {
        this.formats = yTMediaArr;
    }

    public YTMedia[] getFormats() {
        return this.formats;
    }

    public void setAdaptiveFormats(YTMedia[] yTMediaArr) {
        this.adaptiveFormats = yTMediaArr;
    }

    public YTMedia[] getAdaptiveFormats() {
        return this.adaptiveFormats;
    }

    public void setExpiresInSeconds(long j) {
        this.expiresInSeconds = j;
    }

    public long getExpiresInSeconds() {
        return this.expiresInSeconds;
    }

    public void setHlsManifestUrl(String str) {
        this.hlsManifestUrl = str;
    }

    public String getHlsManifestUrl() {
        return this.hlsManifestUrl;
    }
}
