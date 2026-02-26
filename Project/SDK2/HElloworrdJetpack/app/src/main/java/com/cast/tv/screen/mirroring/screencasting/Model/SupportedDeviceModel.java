package com.cast.tv.screen.mirroring.screencasting.Model;

import com.cast.tv.screen.mirroring.screencasting.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SupportedDeviceModel implements Serializable {
    private static final long serialVersionUID = 1;
    private int coverResId;
    private String describe;
    private String deviceName;
    private List<String> supportWays;

    public static List<SupportedDeviceModel> obtain() {
        ArrayList arrayList = new ArrayList();
        SupportedDeviceModel supportedDeviceModel = new SupportedDeviceModel();
        supportedDeviceModel.coverResId = R.mipmap.pic_support_device1;
        supportedDeviceModel.deviceName = "Smart TV";
        supportedDeviceModel.describe = "- The may be a confirmation on the TV screen when you connect,click \"Allow\".\n- Stay on the corresponding video source page.";
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("Wi-Fi");
        arrayList2.add("MIRACAST");
        supportedDeviceModel.supportWays = arrayList2;
        arrayList.add(supportedDeviceModel);
        SupportedDeviceModel supportedDeviceModel2 = new SupportedDeviceModel();
        supportedDeviceModel2.coverResId = R.mipmap.pic_support_device2;
        supportedDeviceModel2.deviceName = "Google Chromecast";
        supportedDeviceModel2.describe = "- Stay on the corresponding video source page.";
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add("WIRELESS ADAPTER");
        supportedDeviceModel2.supportWays = arrayList3;
        arrayList.add(supportedDeviceModel2);
        SupportedDeviceModel supportedDeviceModel3 = new SupportedDeviceModel();
        supportedDeviceModel3.coverResId = R.mipmap.pic_support_device3;
        supportedDeviceModel3.deviceName = "Amazon Fire Stick";
        supportedDeviceModel3.describe = "- Open setting and find the \"Display and sound\" option.\n- Click \"Enable display mirroring\" and stay on that page.";
        ArrayList arrayList4 = new ArrayList();
        arrayList4.add("WIRELESS ADAPTER");
        supportedDeviceModel3.supportWays = arrayList4;
        arrayList.add(supportedDeviceModel3);
        SupportedDeviceModel supportedDeviceModel4 = new SupportedDeviceModel();
        supportedDeviceModel4.coverResId = R.mipmap.pic_support_device4;
        supportedDeviceModel4.deviceName = "Roku";
        supportedDeviceModel4.describe = "- The may be a confirmation on the TV screen when you connect,click \"Allow\".\n- Stay on the corresponding video source page.";
        ArrayList arrayList5 = new ArrayList();
        arrayList5.add("WIRELESS ADAPTER");
        supportedDeviceModel4.supportWays = arrayList5;
        arrayList.add(supportedDeviceModel4);
        SupportedDeviceModel supportedDeviceModel5 = new SupportedDeviceModel();
        supportedDeviceModel5.coverResId = R.mipmap.pic_support_device5;
        supportedDeviceModel5.deviceName = "Anycast";
        supportedDeviceModel5.describe = "- The may be several devices appearing on the list,you need to try each one to check which source can be used.\n- Stay on the corresponding video source page.";
        ArrayList arrayList6 = new ArrayList();
        arrayList6.add("WIRELESS ADAPTER");
        supportedDeviceModel5.supportWays = arrayList6;
        arrayList.add(supportedDeviceModel5);
        return arrayList;
    }

    public int getCoverResId() {
        return this.coverResId;
    }

    public void setCoverResId(int i) {
        this.coverResId = i;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String str) {
        this.deviceName = str;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String str) {
        this.describe = str;
    }

    public List<String> getSupportWays() {
        return this.supportWays;
    }

    public void setSupportWays(List<String> list) {
        this.supportWays = list;
    }
}
