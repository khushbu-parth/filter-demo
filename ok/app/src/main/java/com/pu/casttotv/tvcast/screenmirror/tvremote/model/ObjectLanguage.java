package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

public class ObjectLanguage {
    private String key;
    private String name;

    public ObjectLanguage() {
    }

    public ObjectLanguage(String str, String str2) {
        this.name = str;
        this.key = str2;
    }

    public String getName() {
        return this.name;
    }

    public String getKey() {
        return this.key;
    }
}
