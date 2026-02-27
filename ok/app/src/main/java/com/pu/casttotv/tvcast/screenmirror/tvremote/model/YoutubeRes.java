package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

@Keep
public class YoutubeRes {
    @SerializedName("items")
    private ArrayList<ItemYoutube> items;

    public YoutubeRes() {
    }

    public YoutubeRes(ArrayList<ItemYoutube> arrayList) {
        this.items = arrayList;
    }

    public ArrayList<ItemYoutube> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<ItemYoutube> arrayList) {
        this.items = arrayList;
    }
}
