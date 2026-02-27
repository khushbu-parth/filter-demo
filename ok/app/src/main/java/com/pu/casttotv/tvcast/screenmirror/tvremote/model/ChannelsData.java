package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

@Keep
public class ChannelsData {
    @SerializedName("data")
    private ArrayList<Channels> dataChannels = new ArrayList<>();

    public ArrayList<Channels> getDataChannels() {
        return this.dataChannels;
    }

    public void setDataChannels(ArrayList<Channels> arrayList) {
        this.dataChannels = arrayList;
    }
}
