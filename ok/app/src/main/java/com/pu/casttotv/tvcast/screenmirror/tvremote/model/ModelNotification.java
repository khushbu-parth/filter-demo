package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import com.google.gson.annotations.SerializedName;

public class ModelNotification {
    @SerializedName("des")
    private String des;
    @SerializedName("title")
    private String title;

    public String getTitle() {
        return this.title;
    }

    public String getDes() {
        return this.des;
    }
}
