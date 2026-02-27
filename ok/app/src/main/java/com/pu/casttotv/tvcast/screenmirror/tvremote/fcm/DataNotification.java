package com.pu.casttotv.tvcast.screenmirror.tvremote.fcm;

import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;

@Keep
public class DataNotification {
    @SerializedName("linkImage")
    private String linkImage;
    @SerializedName("linkStore")
    private String linkStore;
    @SerializedName("message")
    private String message;
    @SerializedName("title")
    private String title;
    @SerializedName("type")
    private String type;

    public DataNotification() {
    }

    public DataNotification(String str, String str2, String str3, String str4, String str5) {
        this.title = str;
        this.type = str2;
        this.message = str3;
        this.linkImage = str4;
        this.linkStore = str5;
    }

    public String getLinkStore() {
        return this.linkStore;
    }

    public void setLinkStore(String str) {
        this.linkStore = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public String getLinkImage() {
        return this.linkImage;
    }

    public void setLinkImage(String str) {
        this.linkImage = str;
    }
}
