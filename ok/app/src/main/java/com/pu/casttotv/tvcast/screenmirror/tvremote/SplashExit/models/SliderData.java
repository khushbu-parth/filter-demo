package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.models;

public class SliderData {
    private String des;
    private int image;
    private String title;

    public SliderData() {
    }

    public SliderData(String str, String str2, int i) {
        this.title = str;
        this.des = str2;
        this.image = i;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDes() {
        return this.des;
    }

    public int getImage() {
        return this.image;
    }
}
