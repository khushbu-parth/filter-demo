package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

public class IntroModel {
    private int image;
    private String intro;
    private String name;

    public IntroModel() {
    }

    public IntroModel(String str, String str2, int i) {
        this.name = str;
        this.intro = str2;
        this.image = i;
    }

    public String getName() {
        return this.name;
    }

    public String getIntro() {
        return this.intro;
    }

    public int getImage() {
        return this.image;
    }
}
