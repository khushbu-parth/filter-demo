package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.model;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Device.kt */
public final class Device extends com.jaku.model.Device {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private String customUserDeviceName;

    /* compiled from: Device.kt */
    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        public final Device fromDevice(@NotNull com.jaku.model.Device device) {
            Intrinsics.checkNotNullParameter(device, "jakuDevice");
            Device device2 = new Device();
            device2.setHost(device.getHost());
            device2.setUdn(device.getUdn());
            device2.setSerialNumber(device.getSerialNumber());
            device2.setDeviceId(device.getDeviceId());
            device2.setVendorName(device.getVendorName());
            device2.setModelNumber(device.getModelNumber());
            device2.setModelName(device.getModelName());
            device2.setWifiMac(device.getWifiMac());
            device2.setEthernetMac(device.getEthernetMac());
            device2.setNetworkType(device.getNetworkType());
            device2.setUserDeviceName(device.getUserDeviceName());
            device2.setSoftwareVersion(device.getSoftwareVersion());
            device2.setSoftwareBuild(device.getSoftwareBuild());
            device2.setSecureDevice(device.getSecureDevice());
            device2.setLanguage(device.getLanguage());
            device2.setCountry(device.getCountry());
            device2.setLocale(device.getLocale());
            device2.setTimeZone(device.getTimeZone());
            device2.setTimeZoneOffset(device.getTimeZoneOffset());
            device2.setPowerMode(device.getPowerMode());
            device2.setSupportsSuspend(device.getSupportsSuspend());
            device2.setSupportsFindRemote(device.getSupportsFindRemote());
            device2.setSupportsAudioGuide(device.getSupportsAudioGuide());
            device2.setDeveloperEnabled(device.getDeveloperEnabled());
            device2.setKeyedDeveloperId(device.getKeyedDeveloperId());
            device2.setSearchEnabled(device.getSearchEnabled());
            device2.setVoiceSearchEnabled(device.getVoiceSearchEnabled());
            device2.setNotificationsEnabled(device.getNotificationsEnabled());
            device2.setNotificationsFirstUse(device.getNotificationsFirstUse());
            device2.setSupportsPrivateListening(device.getSupportsPrivateListening());
            device2.setHeadphonesConnected(device.getHeadphonesConnected());
            device2.setIsTv(device.getIsTv());
            device2.setIsStick(device.getIsStick());
            return device2;
        }
    }

    @Nullable
    public final String getCustomUserDeviceName() {
        return this.customUserDeviceName;
    }

    public final void setCustomUserDeviceName(@Nullable String str) {
        this.customUserDeviceName = str;
    }
}
