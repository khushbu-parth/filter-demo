package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;

@Keep
public class DefaultThumb {
    @SerializedName("url")
    private String url;

    public DefaultThumb() {
    }

    public DefaultThumb(String str) {
        this.url = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }
}
