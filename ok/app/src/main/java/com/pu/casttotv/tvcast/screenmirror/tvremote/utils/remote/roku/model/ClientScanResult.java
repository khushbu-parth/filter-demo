package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.model;

public class ClientScanResult {
    private String Device;
    private String HWAddr;
    private String IpAddr;

    public ClientScanResult(String str, String str2, String str3, boolean z) {
        this.IpAddr = str;
        this.HWAddr = str2;
        this.Device = str3;
    }

    public String getIpAddr() {
        return this.IpAddr;
    }

    public String getHWAddr() {
        return this.HWAddr;
    }

    public String getDevice() {
        return this.Device;
    }
}
