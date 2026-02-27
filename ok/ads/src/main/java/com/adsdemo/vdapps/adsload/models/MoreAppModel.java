package com.adsdemo.vdapps.adsload.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MoreAppModel implements Serializable {

    @SerializedName("app_name")
    @Expose
    public String app_name;

    @SerializedName("app_packageName")
    @Expose
    public String app_packageName;

    @SerializedName("app_logo")
    @Expose
    public String app_logo;

    @Override
    public String toString() {
        return "MoreAppModel{" +
                "app_name='" + app_name + '\'' +
                ", app_packageName='" + app_packageName + '\'' +
                ", app_logo='" + app_logo + '\'' +
                '}';
    }
}
