package com.library.info;

import java.io.Serializable;


public class CastTvCustomModel implements Serializable {

    int id;
    String name;
    String url;
    String appopen_image;
    String interstitial_image;
    String small_native_image;
    String native_image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppopen_image() {
        return appopen_image;
    }

    public void setAppopen_image(String appopen_image) {
        this.appopen_image = appopen_image;
    }

    public String getInterstitial_image() {
        return interstitial_image;
    }

    public void setInterstitial_image(String interstitial_image) {
        this.interstitial_image = interstitial_image;
    }

    public String getSmall_native_image() {
        return small_native_image;
    }

    public void setSmall_native_image(String small_native_image) {
        this.small_native_image = small_native_image;
    }

    public String getNative_image() {
        return native_image;
    }

    public void setNative_image(String native_image) {
        this.native_image = native_image;
    }
}
