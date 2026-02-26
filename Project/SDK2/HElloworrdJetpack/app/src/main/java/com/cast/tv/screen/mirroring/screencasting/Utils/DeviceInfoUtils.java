package com.cast.tv.screen.mirroring.screencasting.Utils;

import android.os.Build;

import com.cast.tv.screen.mirroring.screencasting.BuildConfig;
import com.cast.tv.screen.mirroring.screencasting.CastApp;

public class DeviceInfoUtils {
    public static String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getDeviceProduct() {
        return Build.PRODUCT;
    }

    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getDeviceId() {
        return Build.ID;
    }

    public static String getDeviceAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getAppVersionCode() {
        return String.valueOf(20);
    }

    public static String getSystemLanguage() {
        return CastApp.mContext.getResources().getConfiguration().locale.getLanguage();
    }
}
