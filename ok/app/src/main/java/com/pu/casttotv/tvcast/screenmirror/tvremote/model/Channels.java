package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;

@Keep
public class Channels {
    @SerializedName("appId")
    private String appId;
    @SerializedName("app_type")
    private String app_type;
    @SerializedName("icon")
    private String icon;
    @SerializedName("name")
    private String name;

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String str) {
        this.appId = str;
    }

    public String getApp_type() {
        return this.app_type;
    }

    public void setApp_type(String str) {
        this.app_type = str;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String str) {
        this.icon = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }
}
