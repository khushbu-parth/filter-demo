package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;

@Keep
public class IdModelTube {
    @SerializedName("kind")
    private String kind;
    @SerializedName("videoId")
    private String videoId;

    public IdModelTube() {
    }

    public IdModelTube(String str, String str2) {
        this.kind = str;
        this.videoId = str2;
    }

    public String getKind() {
        return this.kind;
    }

    public void setKind(String str) {
        this.kind = str;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(String str) {
        this.videoId = str;
    }
}
