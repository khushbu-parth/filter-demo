package com.cast.tv.screen.mirroring.screencasting.Utils.net;

public enum NetworkType {
    NETWORK_WIFI("WiFi"),
    NETWORK_UNKNOWN("Unknown"),
    NETWORK_NO("NETWORK_NO"),
    NETWORK_4G("NETWORK_4G"),
    NETWORK_3G("NETWORK_3G"),
    NETWORK_2G("NETWORK_2G");

    public String desc;

    NetworkType(String str) {
        this.desc = str;
    }

    @Override
    public String toString() {
        return this.desc;
    }
}
