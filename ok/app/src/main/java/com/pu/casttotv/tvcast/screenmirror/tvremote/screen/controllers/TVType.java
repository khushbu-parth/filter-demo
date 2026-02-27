package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.config.ServiceDescription;
import java.util.Locale;

public class TVType {
    public static String FireTV = "FireTV";
    public static String LG = "LG";
    public static String RokuTV = "RokuTV";
    public static String SamsungTV = "SamsungTV";
    public static String SonyTV = "SonyTV";
    public static int TypeFireTV = 4;
    public static int TypeLG = 2;
    public static int TypeRoku = 1;
    public static int TypeSamsung = 3;
    public static int TypeSony = 5;

    public static boolean isFireTV(ConnectableDevice connectableDevice) {
        String str;
        if (connectableDevice == null) {
            return false;
        }
        String str2 = "";
        if (connectableDevice.getConnectedServiceNames() != null) {
            str = connectableDevice.getConnectedServiceNames().toLowerCase(Locale.getDefault());
        } else {
            str = str2;
        }
        if (!str.contains("firetv") && !str.contains("fire tv")) {
            if (connectableDevice.getFriendlyName() != null) {
                str2 = connectableDevice.getFriendlyName().toLowerCase(Locale.getDefault());
            }
            if (str2.contains("fire tv") || str2.contains("firetv")) {
                return true;
            }
            return false;
        }
        return true;
    }

    public static boolean isLGTV(ConnectableDevice connectableDevice) {
        String manufacturer;
        if (connectableDevice == null) {
            return false;
        }
        String str = "";
        String lowerCase = connectableDevice.getConnectedServiceNames() != null ? connectableDevice.getConnectedServiceNames().toLowerCase(Locale.getDefault()) : str;
        if (lowerCase.contains("webos")) {
            return true;
        }
        ServiceDescription serviceDescription = connectableDevice.getServiceDescription();
        if (!(serviceDescription == null || (manufacturer = serviceDescription.getManufacturer()) == null)) {
            lowerCase = manufacturer.toLowerCase(Locale.getDefault());
        }
        if (lowerCase.contains("lg")) {
            return true;
        }
        if (connectableDevice.getFriendlyName() != null) {
            str = connectableDevice.getFriendlyName().toLowerCase(Locale.getDefault());
        }
        if (str.contains("web os") || str.contains("lg") || str.contains("webos")) {
            return true;
        }
        return false;
    }

    public static boolean isRokuTV(ConnectableDevice connectableDevice) {
        String manufacturer;
        if (connectableDevice == null) {
            return false;
        }
        String str = "";
        String lowerCase = connectableDevice.getConnectedServiceNames() != null ? connectableDevice.getConnectedServiceNames().toLowerCase(Locale.getDefault()) : str;
        ServiceDescription serviceDescription = connectableDevice.getServiceDescription();
        if (!(serviceDescription == null || (manufacturer = serviceDescription.getManufacturer()) == null)) {
            lowerCase = manufacturer.toLowerCase(Locale.getDefault());
        }
        if (lowerCase.contains("roku")) {
            return true;
        }
        if (connectableDevice.getFriendlyName() != null) {
            str = connectableDevice.getFriendlyName().toLowerCase(Locale.getDefault());
        }
        if (!str.contains("roku")) {
            return false;
        }
        return true;
    }

    public static boolean isSamsungTV(ConnectableDevice connectableDevice) {
        String manufacturer;
        if (connectableDevice == null) {
            return false;
        }
        String str = "";
        String lowerCase = connectableDevice.getConnectedServiceNames() != null ? connectableDevice.getConnectedServiceNames().toLowerCase(Locale.getDefault()) : str;
        ServiceDescription serviceDescription = connectableDevice.getServiceDescription();
        if (!(serviceDescription == null || (manufacturer = serviceDescription.getManufacturer()) == null)) {
            lowerCase = manufacturer.toLowerCase(Locale.getDefault());
        }
        if (lowerCase.contains("samsung")) {
            return true;
        }
        if (connectableDevice.getFriendlyName() != null) {
            str = connectableDevice.getFriendlyName().toLowerCase(Locale.getDefault());
        }
        if (!str.contains("samsung")) {
            return false;
        }
        return true;
    }

    public static boolean isSonyTV(ConnectableDevice connectableDevice) {
        String manufacturer;
        if (connectableDevice == null) {
            return false;
        }
        String str = "";
        String lowerCase = connectableDevice.getConnectedServiceNames() != null ? connectableDevice.getConnectedServiceNames().toLowerCase(Locale.getDefault()) : str;
        ServiceDescription serviceDescription = connectableDevice.getServiceDescription();
        if (!(serviceDescription == null || (manufacturer = serviceDescription.getManufacturer()) == null)) {
            lowerCase = manufacturer.toLowerCase(Locale.getDefault());
        }
        if (!lowerCase.contains("sony") && !lowerCase.contains("bravia")) {
            if (connectableDevice.getFriendlyName() != null) {
                str = connectableDevice.getFriendlyName().toLowerCase(Locale.getDefault());
            }
            if (str.contains("bravia") || str.contains("sony")) {
                return true;
            }
            return false;
        }
        return true;
    }

    public static String getTVType(ConnectableDevice connectableDevice) {
        if (isFireTV(connectableDevice)) {
            return FireTV;
        }
        if (isSamsungTV(connectableDevice)) {
            return SamsungTV;
        }
        if (isSonyTV(connectableDevice)) {
            return SonyTV;
        }
        if (isRokuTV(connectableDevice)) {
            return RokuTV;
        }
        return isLGTV(connectableDevice) ? LG : "null";
    }
}
