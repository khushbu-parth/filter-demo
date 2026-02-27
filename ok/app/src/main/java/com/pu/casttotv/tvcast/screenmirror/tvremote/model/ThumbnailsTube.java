package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;

@Keep
public class ThumbnailsTube {
    @SerializedName("medium")
    private DefaultThumb medium;

    public ThumbnailsTube() {
    }

    public ThumbnailsTube(DefaultThumb defaultThumb) {
        this.medium = defaultThumb;
    }

    public DefaultThumb getMedium() {
        return this.medium;
    }

    public void setMedium(DefaultThumb defaultThumb) {
        this.medium = defaultThumb;
    }
}
