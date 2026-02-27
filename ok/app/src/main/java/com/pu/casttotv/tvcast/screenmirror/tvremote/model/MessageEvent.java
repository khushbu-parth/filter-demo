package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

public class MessageEvent {
    private long duration;
    private String message;

    public MessageEvent(String str, long j) {
        this.message = str;
        this.duration = j;
    }

    public MessageEvent(String str) {
        this.message = str;
    }

    public long getDuration() {
        return this.duration;
    }

    public String getMessage() {
        return this.message;
    }
}
