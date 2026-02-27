package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import androidx.annotation.Keep;
import com.connectsdk.service.NetcastTVService;
import com.google.gson.annotations.SerializedName;

@Keep
public class ModelChannelsSamsung {
    @SerializedName("data")
    private ChannelsData data;
    @SerializedName(NetcastTVService.UDAP_API_EVENT)
    private String event;
    @SerializedName("from")
    private String from;

    public ChannelsData getData() {
        return this.data;
    }

    public void setData(ChannelsData channelsData) {
        this.data = channelsData;
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String str) {
        this.event = str;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String str) {
        this.from = str;
    }
}
