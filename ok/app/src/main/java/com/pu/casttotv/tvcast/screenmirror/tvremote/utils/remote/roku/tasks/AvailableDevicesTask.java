package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks;

import android.content.Context;
import com.jaku.api.DeviceRequests;
import com.jaku.api.QueryRequests;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.model.ClientScanResult;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.model.Device;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.DBUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.WifiApManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

public class AvailableDevicesTask implements Callable {
    private Context context;
    private boolean filterPairedDevices;

    public AvailableDevicesTask(Context context2, boolean z) {
        this.context = context2;
        this.filterPairedDevices = z;
    }

    @Override // java.util.concurrent.Callable
    public List<Device> call() {
        List arrayList = new ArrayList();
        if (this.filterPairedDevices) {
            arrayList = DBUtils.getAllDevices(this.context);
        }
        ArrayList arrayList2 = new ArrayList();
        try {
            ArrayList<Device> arrayList3 = new ArrayList();
            if (new WifiApManager(this.context).isWifiApEnabled()) {
                arrayList3.addAll(scanAccessPointForDevices());
            } else {
                for (com.jaku.model.Device device : DeviceRequests.discoverDevices()) {
                    arrayList3.add(Device.Companion.fromDevice(device));
                }
            }
            for (Device device2 : arrayList3) {
                boolean z = false;
                int i = 0;
                while (true) {
                    if (i >= arrayList.size()) {
                        break;
                    } else if (((Device) arrayList.get(i)).getSerialNumber().equals(device2.getSerialNumber())) {
                        z = true;
                        break;
                    } else {
                        i++;
                    }
                }
                if (!z) {
                    arrayList2.add(device2);
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return arrayList2;
    }

    private ArrayList<Device> scanAccessPointForDevices() throws Throwable {
        ArrayList<ClientScanResult> clientList;
        ArrayList<Device> arrayList = new ArrayList<>();
        WifiApManager wifiApManager = new WifiApManager(this.context);
        if (wifiApManager.isWifiApEnabled() && (clientList = wifiApManager.getClientList(false, 3000)) != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Found ");
            sb.append(clientList.size());
            sb.append(" connected devices.");
            Iterator<ClientScanResult> it = clientList.iterator();
            while (it.hasNext()) {
                ClientScanResult next = it.next();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Device: ");
                sb2.append(next.getDevice());
                sb2.append(" HW Address: ");
                sb2.append(next.getHWAddr());
                sb2.append(" IP Address:  ");
                sb2.append(next.getIpAddr());
                try {
                    Device.Companion companion = Device.Companion;
                    Device fromDevice = companion.fromDevice(QueryRequests.queryDeviceInfo("http://" + next.getIpAddr() + ":8060"));
                    fromDevice.setHost("http://" + next.getIpAddr() + ":8060");
                    arrayList.add(fromDevice);
                } catch (IOException e2) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Invalid device: ");
                    sb3.append(e2.getMessage());
                }
            }
        }
        return arrayList;
    }
}
