package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.firetv;

public class ChannelFireTVDto {
    private String activityName;
    private int img;
    private String name;
    private String packageName;

    public ChannelFireTVDto() {
    }

    public String getActivityName() {
        return this.activityName;
    }

    public String getName() {
        return this.name;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public int getImg() {
        return this.img;
    }

    public ChannelFireTVDto(String str, String str2, String str3, int i) {
        this.activityName = str;
        this.name = str2;
        this.packageName = str3;
        this.img = i;
    }
}
