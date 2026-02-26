package com.co.casttotv.screenmirroring.mirroring.cast.models;

import com.connectsdk.device.ConnectableDevice;

import java.util.ArrayList;

public class ConnectModel {
    ConnectableDevice device;
    ArrayList<ConnectableDevice> devices;
    boolean isConnected;
    boolean isSelected = false;
    String title;

    public ConnectModel() {
    }

    public ConnectModel(ArrayList<ConnectableDevice> arrayList) {
        this.devices = arrayList;
    }

    public ArrayList<ConnectableDevice> getDevices() {
        return this.devices;
    }

    public void setDevices(ArrayList<ConnectableDevice> arrayList) {
        this.devices = arrayList;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public void setConnected(boolean z) {
        this.isConnected = z;
    }

}
