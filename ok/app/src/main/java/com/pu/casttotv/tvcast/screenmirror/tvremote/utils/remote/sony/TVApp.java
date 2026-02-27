package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.sony;

import java.io.Serializable;

public class TVApp implements Serializable {
    private String icon;
    private String name;
    private String uri;

    public TVApp(String str, String str2, String str3) {
        this.name = str;
        this.uri = str2;
        this.icon = str3;
    }

    public String getIcon() {
        return this.icon;
    }

    public String getName() {
        return this.name;
    }

    public String getUri() {
        return this.uri;
    }
}
