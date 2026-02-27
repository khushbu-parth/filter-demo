package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers;

import com.connectsdk.device.ConnectableDevice;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.browser.WebBroadCast;
import org.greenrobot.eventbus.EventBus;

public class TVConnectUtils {
    private static TVConnectUtils sTVConnectUtils;
    private ConnectableDevice connectableDevice;
    public boolean isConnectWeb;
    public WebBroadCast mWebBroadCast;
    public float volume;

    public static TVConnectUtils getInstance() {
        if (sTVConnectUtils == null) {
            sTVConnectUtils = new TVConnectUtils();
        }
        return sTVConnectUtils;
    }

    public ConnectableDevice getConnectableDevice() {
        return this.connectableDevice;
    }

    public void setConnectableDevice(ConnectableDevice connectableDevice2) {
        this.connectableDevice = connectableDevice2;
    }

    public boolean isConnected() {
        ConnectableDevice connectableDevice2 = this.connectableDevice;
        if (connectableDevice2 != null) {
            return connectableDevice2.isConnected();
        }
        if (this.mWebBroadCast != null) {
            return this.isConnectWeb;
        }
        return false;
    }

    public void disconnect() {
        ConnectableDevice connectableDevice2 = this.connectableDevice;
        if (connectableDevice2 != null) {
            connectableDevice2.disconnect();
            this.connectableDevice = null;
        }
        WebBroadCast webBroadCast = this.mWebBroadCast;
        if (webBroadCast != null) {
            webBroadCast.stopServer();
            this.isConnectWeb = false;
        }
        EventBus.getDefault().post(new MessageEvent("KEY_CONNECT"));
    }

    public String getDeviveName() {
        ConnectableDevice connectableDevice2 = this.connectableDevice;
        if (connectableDevice2 == null) {
            return "no TV connection";
        }
        if (connectableDevice2.getFriendlyName() != null) {
            return this.connectableDevice.getFriendlyName();
        }
        return this.connectableDevice.getModelName();
    }
}
