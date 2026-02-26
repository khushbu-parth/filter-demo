package com.lib.screening.bean;

import org.fourthline.cling.model.meta.Device;

import java.io.Serializable;

public class DeviceInfo implements Serializable {
    private boolean connected;
    private Device device;
    private boolean isSelect;
    private String mediaID;
    private String name;
    private String oldMediaID;
    private int state = -1;

    public DeviceInfo(Device device, String str) {
        this.device = device;
        this.name = str;
    }

    public DeviceInfo() {
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getMediaID() {
        return this.mediaID;
    }

    public void setMediaID(String str) {
        this.mediaID = str;
    }

    public String getOldMediaID() {
        return this.oldMediaID;
    }

    public void setOldMediaID(String str) {
        this.oldMediaID = str;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int i) {
        this.state = i;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void setConnected(boolean z) {
        this.connected = z;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean z) {
        this.isSelect = z;
    }
}
