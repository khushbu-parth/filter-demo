package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ModelSaleAll {
    @SerializedName("hour")
    private int hour;
    @SerializedName("noti")
    private ArrayList<ModelNotification> listNotification;
    @SerializedName("minute")
    private int minute;
    @SerializedName("status")
    private boolean status;

    public int getHour() {
        return this.hour;
    }

    public boolean isStatus() {
        return this.status;
    }

    public int getMinute() {
        return this.minute;
    }

    public ArrayList<ModelNotification> getListNotification() {
        return this.listNotification;
    }
}
