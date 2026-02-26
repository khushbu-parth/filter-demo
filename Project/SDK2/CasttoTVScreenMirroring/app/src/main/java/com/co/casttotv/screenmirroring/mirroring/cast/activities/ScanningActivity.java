package com.co.casttotv.screenmirroring.mirroring.cast.activities;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ads.sdk.SdkManager;
import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.adapters.DeviceAdapter;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.Config;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.DeviceManager;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ActivityScanningBinding;
import com.co.casttotv.screenmirroring.mirroring.cast.models.ConnectModel;
import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.device.ConnectableDeviceListener;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.discovery.DiscoveryManagerListener;
import com.connectsdk.service.DeviceService;
import com.connectsdk.service.command.ServiceCommandError;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class ScanningActivity extends BaseActivity implements DeviceAdapter.ConnectAdapterCallback, DiscoveryManagerListener {
    private static final String TAG = ScanningActivity.class.getName();
    ActivityScanningBinding binding;
    DeviceAdapter adapter;
    private final ConnectableDeviceListener connectableDeviceListener = new DeviceCallBackListener();
    private ArrayList<ConnectableDevice> mListDevices = new ArrayList<>();

    @Override
    protected void onViewCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scanning);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
        SdkManager.loadNativeBanner(ScanningActivity.this, binding.frameNativeBanner);
        Log.e(TAG, "onViewCreate: ");
        Iterator<ConnectModel> it = Config.mDeviceShow.iterator();
        while (it.hasNext()) {
            it.next().setSelected(false);
        }

        if (adapter == null) {
            adapter = new DeviceAdapter(ScanningActivity.this, Config.mDeviceShow);
            adapter.setAdapterListener(this);
            binding.setAdapter(adapter);
        }
    }

    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
        DeviceManager.getInstance().startScanningDevice(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
        DeviceManager.getInstance().stopScanningDevice(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClickItem(ArrayList<ConnectModel> connectModels, ConnectModel connectModel) {
        DeviceManager.getInstance().setCurrentDevice(connectModel.getDevices());
        DeviceManager.getInstance().connectToDevice(connectableDeviceListener);
    }

    @Override
    public void onDeviceAdded(DiscoveryManager manager, ConnectableDevice device) {
        Log.e(TAG, "onDeviceAdded: ");
        setConnectableDevice(device, manager);
    }

    public void setConnectableDevice(ConnectableDevice connectableDevice, DiscoveryManager discoveryManager) {
        Log.e(TAG, "setConnectableDevice: ");
        if (connectableDevice.getServices() == null || connectableDevice.getServices().size() == 0) {
            return;
        }
        int i = 0;
        while (true) {
            if (i >= mListDevices.size()) {
                i = -1;
                break;
            }
            ConnectableDevice connectableDevice2 = mListDevices.get(i);
            String friendlyName = connectableDevice.getFriendlyName();
            String friendlyName2 = connectableDevice2.getFriendlyName();
            if (friendlyName == null) {
                friendlyName = connectableDevice.getModelName();
            }
            if (friendlyName2 == null) {
                friendlyName2 = connectableDevice2.getModelName();
            }
            if (connectableDevice2.getIpAddress().equals(connectableDevice.getIpAddress()) && connectableDevice2.getFriendlyName().equals(connectableDevice.getFriendlyName()) && !discoveryManager.isServiceIntegrationEnabled() && connectableDevice2.getServiceId().equals(connectableDevice.getServiceId())) {
                mListDevices.remove(connectableDevice2);
                mListDevices.add(i, connectableDevice);
                return;
            } else if (friendlyName.compareToIgnoreCase(friendlyName2) < 0) {
                mListDevices.add(i, connectableDevice);
                break;
            } else {
                i++;
            }
        }
        if (i == -1) {
            mListDevices.add(connectableDevice);
        }
        updateRecycler(mListDevices);
    }

    private void updateRecycler(ArrayList<ConnectableDevice> arrayList) {
        boolean z;
        ArrayList arrayList2 = new ArrayList(Config.mDeviceShow);
        if (arrayList.size() == 0) {
            arrayList2.removeIf(new Predicate() {
                @Override
                public boolean test(Object o) {
                    return false;
                }
            });
            Config.mDeviceShow.clear();
            Config.mDeviceShow.addAll(arrayList2);
            if (adapter != null) {
                adapter.updateData(Config.mDeviceShow);
                return;
            }
            return;
        }
        Iterator it = new ArrayList(arrayList).iterator();
        while (it.hasNext()) {
            ConnectableDevice connectableDevice = (ConnectableDevice) it.next();
            Iterator it2 = arrayList2.iterator();
            boolean z2 = false;
            while (true) {
                z = true;
                if (!it2.hasNext()) {
                    break;
                }
                ConnectModel connectModel = (ConnectModel) it2.next();
                if (connectModel.getDevices() != null) {
                    if (!connectModel.getDevices().contains(connectableDevice)) {
                        Iterator<ConnectableDevice> it3 = connectModel.getDevices().iterator();
                        while (true) {
                            if (!it3.hasNext()) {
                                break;
                            } else if (it3.next().getId().equals(connectableDevice.getId())) {
                                z2 = true;
                                break;
                            }
                        }
                    } else {
                        z2 = true;
                        break;
                    }
                }
            }
            if (!z2) {
                Iterator it4 = arrayList2.iterator();
                while (true) {
                    if (!it4.hasNext()) {
                        z = false;
                        break;
                    }
                    ConnectModel connectModel2 = (ConnectModel) it4.next();
                    if (connectModel2.getDevices() != null && connectModel2.getDevices().get(0).getLastKnownIPAddress().equals(connectableDevice.getLastKnownIPAddress())) {
                        connectModel2.getDevices().add(connectableDevice);
                        break;
                    }
                }
                if (!z) {
                    ArrayList arrayList3 = new ArrayList();
                    arrayList3.add(connectableDevice);
                    arrayList2.add(0, new ConnectModel(arrayList3));
                }
            }
        }
        Config.mDeviceShow.clear();
        Config.mDeviceShow.addAll(arrayList2);
        if (adapter != null) {
            adapter.updateData(Config.mDeviceShow);
        }
    }

    @Override
    public void onDeviceUpdated(DiscoveryManager manager, ConnectableDevice device) {
        Log.e(TAG, "onDeviceUpdated: ");
        updateRecycler(mListDevices);
    }

    @Override
    public void onDeviceRemoved(DiscoveryManager manager, ConnectableDevice device) {
        Log.e(TAG, "onDeviceRemoved: ");
        mListDevices.removeIf(new Predicate() {
            @Override
            public boolean test(Object obj) {
                boolean equals;
                equals = ((ConnectableDevice) obj).getId().equals(device.getId());
                return equals;
            }
        });
        updateRecycler(mListDevices);
    }

    @Override
    public void onDiscoveryFailed(DiscoveryManager manager, ServiceCommandError error) {
        Log.e(TAG, "onDiscoveryFailed: " + error.toString());
        mListDevices.clear();
        updateRecycler(mListDevices);
    }

    public void updateConnectedDevice(ConnectableDevice connectableDevice) {
        Log.e(TAG, "updateConnectedDevice: ");
        Iterator<ConnectModel> it = Config.mDeviceShow.iterator();
        while (it.hasNext()) {
            ConnectModel next = it.next();
            next.setConnected(false);
            next.setSelected(false);
            if (next.getDevices() != null && next.getDevices().contains(connectableDevice)) {
                next.setConnected(true);
            }
        }
        if (adapter != null) {
            adapter.updateData(Config.mDeviceShow);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public final void run() {
                finish();
            }
        }, 200L);
    }

    private class DeviceCallBackListener implements ConnectableDeviceListener {
        @Override
        public void onDeviceReady(ConnectableDevice device) {
            Log.e(TAG, "DeviceCallBackListener: onDeviceReady");
            updateConnectedDevice(device);
        }

        @Override
        public void onDeviceDisconnected(ConnectableDevice device) {
            Log.e(TAG, "DeviceCallBackListener: onDeviceDisconnected");
            Iterator<ConnectModel> it = Config.mDeviceShow.iterator();
            while (it.hasNext()) {
                ConnectModel next = it.next();
                next.setSelected(false);
                next.setConnected(false);
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public final void run() {
                  finish();
                }
            }, 200L);
        }

        @Override
        public void onPairingRequired(ConnectableDevice device, DeviceService service, DeviceService.PairingType pairingType) {
            Log.e(TAG, "DeviceCallBackListener: onPairingRequired");
        }

        @Override
        public void onCapabilityUpdated(ConnectableDevice device, List<String> added, List<String> removed) {
            Log.e(TAG, "DeviceCallBackListener: onCapabilityUpdated");
        }

        @Override
        public void onConnectionFailed(ConnectableDevice device, ServiceCommandError error) {
            Log.e(TAG, "DeviceCallBackListener: onConnectionFailed");
            Toast.makeText(ScanningActivity.this, "Failed to connect!", Toast.LENGTH_SHORT).show();
        }
    }

}