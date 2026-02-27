package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils;

public enum RokuRequestTypes {
    query_active_app("query/active-app"),
    query_device_info("query/device-info"),
    launch("launch"),
    keypress("keypress"),
    query_icon("query/icon"),
    search("search/browse?");

    private RokuRequestTypes(String str) {
    }
}
