package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;

@Keep
public class SnippetTube {
    @SerializedName("channelId")
    private String channelId;
    @SerializedName("channelTitle")
    private String channelTitle;
    @SerializedName("thumbnails")
    private ThumbnailsTube thumbnails;
    @SerializedName("title")
    private String title;

    public SnippetTube() {
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setChannelId(String str) {
        this.channelId = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public SnippetTube(String str, String str2, ThumbnailsTube thumbnailsTube) {
        this.channelId = str;
        this.title = str2;
        this.thumbnails = thumbnailsTube;
    }

    public String getChannelTitle() {
        return this.channelTitle;
    }

    public void setChannelTitle(String str) {
        this.channelTitle = str;
    }

    public ThumbnailsTube getThumbnails() {
        return this.thumbnails;
    }

    public void setThumbnails(ThumbnailsTube thumbnailsTube) {
        this.thumbnails = thumbnailsTube;
    }
}
