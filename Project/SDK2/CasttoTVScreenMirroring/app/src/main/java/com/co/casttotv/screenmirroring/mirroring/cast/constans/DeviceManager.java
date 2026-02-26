package com.co.casttotv.screenmirroring.mirroring.cast.constans;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.device.ConnectableDeviceListener;
import com.connectsdk.discovery.CapabilityFilter;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.discovery.DiscoveryManagerListener;
import com.connectsdk.service.DIALService;
import com.connectsdk.service.DLNAService;
import com.connectsdk.service.DeviceService;
import com.connectsdk.service.NetcastTVService;
import com.connectsdk.service.RokuService;
import com.connectsdk.service.WebOSTVService;
import com.connectsdk.service.capability.MediaControl;
import com.connectsdk.service.capability.MediaPlayer;
import com.connectsdk.service.capability.VolumeControl;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeviceManager {
    private static DeviceManager instance;
    private DLNAService mDLNAService;
    private ConnectableDevice mDevice;
    private DiscoveryManager mDiscoveryManager;
//    private ArrayList<ConnectableDevice> mListDevices = new ArrayList<>();
    private MediaControl mMediaControl;
    private WebOSTVService mWebOSTVService;

    public static DeviceManager getInstance() {
        if (instance == null) {
            instance = new DeviceManager();
        }
        return instance;
    }

    public DiscoveryManager getDiscoveryManager() {
        return mDiscoveryManager;
    }

    public ConnectableDevice getDevice() {
        return mDevice;
    }

    public ConnectableDevice getDeviceAlready() {
        if (mDevice.isConnected()) {
            return mDevice;
        }
        connectToDevice(null);
        return mDevice;
    }

    public Boolean isConnected(){
        return mDevice != null && mDevice.isConnected();
    }

    public String YoutubeAppID(){
        String appId = null;
        if (mDevice != null && mDevice.isConnected()){
            if (mDevice.getServiceByName(WebOSTVService.ID) != null)
                appId = "youtube.leanback.v4";
            else if (mDevice.getServiceByName(NetcastTVService.ID) != null)
                appId = "0000000000017498";
            else if (mDevice.getServiceByName(RokuService.ID) != null)
                appId = "837";
            else if (mDevice.getServiceByName(DIALService.ID) != null)
                appId = "YouTube";
        }
        return appId;
    }

    public void setMediaControl(MediaControl control) {
        mMediaControl = control;
    }

    public MediaControl getMediaControl(){
        return mMediaControl;
    }

    public void setCurrentDevice(ArrayList<ConnectableDevice> arrayList) {
//        mListDevices = arrayList;
        Iterator<ConnectableDevice> it = arrayList.iterator();
        while (it.hasNext()) {
            ConnectableDevice next = it.next();
            if (next.isConnectable() && next.getServiceByName(WebOSTVService.ID) != null) {
                mDevice = next;
                mWebOSTVService = (WebOSTVService) next.getServiceByName(WebOSTVService.ID);
                return;
            }
        }

        Iterator<ConnectableDevice> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            ConnectableDevice next2 = it2.next();
            if (next2.isConnectable() && next2.getServiceByName(DLNAService.ID) != null) {
                mDevice = next2;
                mDLNAService = (DLNAService) next2.getServiceByName(WebOSTVService.ID);
                return;
            }
        }
    }

    public void startScanningDevice(DiscoveryManagerListener discoveryManagerListener) {
        CapabilityFilter filter = new CapabilityFilter(
                MediaPlayer.Display_Video,
                MediaPlayer.Display_Audio,
                MediaPlayer.Display_Image,
                MediaControl.Any,
                VolumeControl.Volume_Up_Down
        );
        DiscoveryManager.getInstance().setCapabilityFilters(filter);
        mDiscoveryManager = DiscoveryManager.getInstance();
        mDiscoveryManager.addListener(discoveryManagerListener);
        mDiscoveryManager.start();
    }

    public void stopScanningDevice(DiscoveryManagerListener discoveryManagerListener) {
        if (mDiscoveryManager != null) {
            mDiscoveryManager.removeListener(discoveryManagerListener);
            mDiscoveryManager.stop();
        }
    }

    public void connectToDevice(ConnectableDeviceListener connectableDeviceListener) {
        if (mDevice == null || !mDevice.isConnectable()) {
            return;
        }
        mDevice.addListener(connectableDeviceListener);
        mDevice.connect();
    }

    public void disconnectToDevice(ConnectableDeviceListener connectableDeviceListener) {
        if (mDevice == null) {
            return;
        }
        if (mMediaControl != null) {
            mMediaControl.stop(null);
        }
        mDevice.disconnect();
        mDevice.removeListener(connectableDeviceListener);
    }

    public void getVolume(VolumeControl.VolumeListener volumeListener) {
        mWebOSTVService = (WebOSTVService) mDevice.getServiceByName(WebOSTVService.ID);
        if (mWebOSTVService != null) {
            mWebOSTVService.getVolume(volumeListener);
            return;
        }
        mDLNAService = (DLNAService) mDevice.getServiceByName(DLNAService.ID);
        if (mDLNAService != null) {
            mDLNAService.getVolume(volumeListener);
        }
    }

    public void setVolume(float volume) {
        mWebOSTVService = (WebOSTVService) mDevice.getServiceByName(WebOSTVService.ID);
        if (mWebOSTVService != null) {
            mWebOSTVService.setVolume(volume, new ResponseListener<Object>() {
                @Override
                public void onSuccess(Object obj) {
                }

                @Override
                public void onError(ServiceCommandError serviceCommandError) {
                }
            });
            return;
        }
        mDLNAService = (DLNAService) mDevice.getServiceByName(DLNAService.ID);
        if (mDLNAService != null) {
            mDLNAService.setVolume(volume, new ResponseListener<Object>() {
                @Override
                public void onError(ServiceCommandError serviceCommandError) {
                }

                @Override
                public void onSuccess(Object obj) {
                }
            });
        }
    }

    public static void showDeviceDialog(Context mContext) {
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_connected_device);

        Button buttonDisconnect = dialog.findViewById(R.id.button_disconnect);
        TextView textView = dialog.findViewById(R.id.text_title);
        textView.setText(DeviceManager.getInstance().getDevice().getFriendlyName());

        buttonDisconnect.setOnClickListener(view -> {
            DeviceManager.getInstance().disconnectToDevice(new ConnectableDeviceListener() {
                @Override
                public void onDeviceReady(ConnectableDevice device) {
                }

                @Override
                public void onDeviceDisconnected(ConnectableDevice device) {
                }

                @Override
                public void onPairingRequired(ConnectableDevice device, DeviceService service, DeviceService.PairingType pairingType) {
                }

                @Override
                public void onCapabilityUpdated(ConnectableDevice device, List<String> added, List<String> removed) {
                }

                @Override
                public void onConnectionFailed(ConnectableDevice device, ServiceCommandError error) {
                }
            });
            dialog.dismiss();
            Toast.makeText(mContext, "Device disconnected!", Toast.LENGTH_SHORT).show();
        });

        try {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
        } catch (Exception e) {
        }
    }
}
