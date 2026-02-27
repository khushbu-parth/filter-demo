package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.youtube_browser;

/* loaded from: classes4.dex */
public class YoutubeDto {
    private long duration;
    private String format;
    private String name;
    private String quality;
    private String thumbnail;
    private String url;

    public YoutubeDto(String str, String str2, String str3, String str4, String str5, long j) {
        this.name = str;
        this.thumbnail = str2;
        this.url = str3;
        this.quality = str4;
        this.format = str5;
        this.duration = j;
    }

    public long getDuration() {
        return this.duration;
    }

    public String getName() {
        return this.name;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public String getUrl() {
        return this.url;
    }

    public String getQuality() {
        return this.quality;
    }

    public String getFormat() {
        return this.format;
    }
}
