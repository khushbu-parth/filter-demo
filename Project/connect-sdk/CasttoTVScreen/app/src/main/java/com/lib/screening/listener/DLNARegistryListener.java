package com.lib.screening.listener;

import com.lib.screening.bean.DeviceInfo;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class DLNARegistryListener implements RegistryListener {
    private final DeviceType DMR_DEVICE_TYPE = new UDADeviceType("MediaRenderer");

    @Override
    public void afterShutdown() {
    }

    @Override
    public void beforeShutdown(Registry registry) {
    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice localDevice) {
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice localDevice) {
    }

    public void onDeviceAdded(Registry registry, Device device) {
    }

    public abstract void onDeviceChanged(List<DeviceInfo> list);

    public void onDeviceRemoved(Registry registry, Device device) {
    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice remoteDevice, Exception exc) {
    }

    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice remoteDevice) {
    }

    @Override
    public void remoteDeviceUpdated(Registry registry, RemoteDevice remoteDevice) {
    }

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice remoteDevice) {
        onDeviceChanged(build(registry.getDevices()));
        onDeviceAdded(registry, remoteDevice);
    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice remoteDevice) {
        onDeviceChanged(build(registry.getDevices()));
        onDeviceRemoved(registry, remoteDevice);
    }

    public void onDeviceChanged(Collection<Device> collection) {
        onDeviceChanged(build(collection));
    }

    private List<DeviceInfo> build(Collection<Device> collection) {
        ArrayList arrayList = new ArrayList();
        for (Device device : collection) {
            if (device.findDevices(this.DMR_DEVICE_TYPE) != null) {
                arrayList.add(new DeviceInfo(device, getDeviceName(device)));
            }
        }
        return arrayList;
    }

    private String getDeviceName(Device device) {
        if (device.getDetails() != null && device.getDetails().getFriendlyName() != null) {
            return device.getDetails().getFriendlyName();
        }
        return device.getDisplayString();
    }
}
