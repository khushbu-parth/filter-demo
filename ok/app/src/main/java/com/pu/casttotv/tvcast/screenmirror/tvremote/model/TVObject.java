package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import com.connectsdk.device.ConnectableDevice;
import java.util.ArrayList;

public class TVObject {
    private ArrayList<ConnectableDevice> arrType = new ArrayList<>();
    private String tvName;

    public TVObject(String str, ArrayList<ConnectableDevice> arrayList) {
        this.tvName = str;
        this.arrType = arrayList;
    }

    public String getTvName() {
        return this.tvName;
    }

    public ArrayList<ConnectableDevice> getArrType() {
        return this.arrType;
    }
}
