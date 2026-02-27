package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.connectsdk.core.Util;
import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.device.ConnectableDeviceListener;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.discovery.DiscoveryManagerListener;
import com.connectsdk.service.DIALService;
import com.connectsdk.service.DeviceService;
import com.connectsdk.service.FireTVService;
import com.connectsdk.service.RokuService;
import com.connectsdk.service.WebOSTVService;
import com.connectsdk.service.command.ServiceCommandError;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.PairingAlertDialog;
import com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.PairingCodeDialog;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.TVObject;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVType;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.browser.FileBrowserActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.howto.HowToYouActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Tracking;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.other.SamSungRemoteController;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.other.SamsungRemoteManeger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

@SuppressLint("WrongConstant")
public class ConnectActivity extends BaseActivity implements DiscoveryManagerListener, View.OnClickListener {
    public static String IpAddressFireTV = "";
    private Button btn_web_browser;
    private DeviceAdapter deviceAdapter;
    private LinearLayout llBack;
    private LinearLayout llConnect;
    private LinearLayout llHelp;
    private LinearLayout llListConnect;
    private LinearLayout ll_not_device;
    private PairingAlertDialog pairingAlertDialog;
    private PairingCodeDialog pairingCodeDialog;
    private RecyclerView rcv_list_device;
    private RelativeLayout rlHeader;
    private TextView tvTitleTab;
    private String TAG = "ConnectActivityzzz";
    private ArrayList<ConnectableDevice> connectableDevices = new ArrayList<>();
    private boolean isConnected = false;
    private ConnectableDeviceListener connectableDeviceListener = new ConnectableDeviceListener() {
        @Override
        public void onDeviceReady(ConnectableDevice connectableDevice) {
            if (connectableDevice.getFriendlyName() != null) {
                connectableDevice.getFriendlyName();
            } else {
                connectableDevice.getModelName();
            }
            if (!ConnectActivity.this.isConnected) {
                ConnectActivity.this.connectDeviceReady(connectableDevice);
                ConnectActivity.this.isConnected = true;
            }
        }

        @Override
        public void onDeviceDisconnected(ConnectableDevice connectableDevice) {
            String unused = ConnectActivity.this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onDeviceDisconnected: ");
            sb.append(connectableDevice.getModelName());
            try {
                if (connectableDevice.getServiceDescription() != null && connectableDevice.getServiceDescription().getManufacturer() != null) {
                    Tracking.connect(ConnectActivity.this, "fail", connectableDevice.getId(), connectableDevice.getServiceDescription().getManufacturer());
                } else {
                    Tracking.connect(ConnectActivity.this, "fail", connectableDevice.getId(), TVType.getTVType(connectableDevice));
                }
            } catch (Exception e2) {
                Tracking.connect(ConnectActivity.this, "fail", connectableDevice.getId(), "");
                e2.printStackTrace();
            }
            if (connectableDevice.getFriendlyName() != null) {
                connectableDevice.getFriendlyName();
            } else {
                connectableDevice.getModelName();
            }
        }

        @Override
        public void onPairingRequired(ConnectableDevice connectableDevice, DeviceService deviceService, DeviceService.PairingType pairingType) {
            String unused = ConnectActivity.this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPairingRequired: ");
            sb.append(connectableDevice.getModelName());
            if (connectableDevice.getFriendlyName() != null) {
                connectableDevice.getFriendlyName();
            } else {
                connectableDevice.getModelName();
            }
            int i = AnonymousClass16.$SwitchMap$com$connectsdk$service$DeviceService$PairingType[pairingType.ordinal()];
            if (i == 1) {
                try {
                    if (ConnectActivity.this.pairingAlertDialog == null) {
                        return;
                    }
                    ConnectActivity.this.pairingAlertDialog.show();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else if (i != 2 && i != 3) {
            } else {
                try {
                    if (ConnectActivity.this.pairingCodeDialog == null) {
                        return;
                    }
                    ConnectActivity.this.pairingCodeDialog.show();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }

        @Override // com.connectsdk.device.ConnectableDeviceListener
        public void onCapabilityUpdated(ConnectableDevice connectableDevice, List<String> list, List<String> list2) {
            String unused = ConnectActivity.this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onCapabilityUpdated: ");
            sb.append(connectableDevice.getModelName());
            if (connectableDevice.getFriendlyName() != null) {
                connectableDevice.getFriendlyName();
            } else {
                connectableDevice.getModelName();
            }
        }

        @Override // com.connectsdk.device.ConnectableDeviceListener
        public void onConnectionFailed(ConnectableDevice connectableDevice, ServiceCommandError serviceCommandError) {
            String unused = ConnectActivity.this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onConnectionFailed: ");
            sb.append(connectableDevice.getModelName());
            try {
                if (connectableDevice.getServiceDescription() != null && connectableDevice.getServiceDescription().getManufacturer() != null) {
                    Tracking.connect(ConnectActivity.this, "fail", connectableDevice.getId(), connectableDevice.getServiceDescription().getManufacturer());
                } else {
                    Tracking.connect(ConnectActivity.this, "fail", connectableDevice.getId(), TVType.getTVType(connectableDevice));
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            ConnectActivity.this.connectFailed(connectableDevice);
            if (connectableDevice.getFriendlyName() != null) {
                connectableDevice.getFriendlyName();
            } else {
                connectableDevice.getModelName();
            }
        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() { // from class: com.magicapps.casttotv.tv.screen.tab.connect.ConnectActivity.15
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.net.wifi.STATE_CHANGE".equals(intent.getAction())) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            }
        }
    };

    /* loaded from: classes4.dex */
    public interface DeviceItemClick {
        void onItemClick(ConnectableDevice connectableDevice);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.fragment_connect);


        initView();
        initData();
    }

    private void initView() {
        AdsManager.CallNativeAdLoad(this, findViewById(R.id.native_container), AdsManager.NATIVE_MIDEUM);
        DiscoveryManager.getInstance().addListener(this);
        this.tvTitleTab = (TextView) findViewById(R.id.tvTitleTab);
        this.rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        this.llBack = (LinearLayout) findViewById(R.id.llBack);
        this.btn_web_browser = (Button) findViewById(R.id.btn_web_browser);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llConnect);
        this.llConnect = linearLayout;
        linearLayout.setVisibility(8);
        this.llListConnect = (LinearLayout) findViewById(R.id.llListConnect);
        this.rcv_list_device = (RecyclerView) findViewById(R.id.rcvList);
        this.ll_not_device = (LinearLayout) findViewById(R.id.llNotConnect);
        this.llBack.setOnClickListener(this);
        this.btn_web_browser.setOnClickListener(this);
        this.tvTitleTab.setText(getString(R.string.connecting));
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.llHelp);
        this.llHelp = linearLayout2;
        linearLayout2.setVisibility(0);
        this.llHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdsManager.CallInterstitialAdLoad(ConnectActivity.this, 0, () -> {
                    Intent intent = new Intent(ConnectActivity.this, HowToYouActivity.class);
                    intent.putExtra("TYPE_HTY", 1);
                    ConnectActivity.this.startActivity(intent);
                    Utils.nextScreen(ConnectActivity.this);
                });

            }
        });
        this.rcv_list_device.setLayoutManager(new LinearLayoutManager(this));
        DeviceAdapter deviceAdapter = new DeviceAdapter(new ArrayList(), this, new DeviceItemClick() {
            @Override
            public void onItemClick(ConnectableDevice connectableDevice) {
                try {
                    if (TVType.isSamsungTV(connectableDevice)) {
                        ConnectActivity.this.setDataSamsung(connectableDevice);
                    } else {
                        ConnectActivity.this.connectToDevice(connectableDevice);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        this.deviceAdapter = deviceAdapter;
        this.rcv_list_device.setAdapter(deviceAdapter);
        registerReceiverWifi();
        ConnectActivity.this.callbackDone();
    }

    public Unit callbackFail() {
        return Unit.INSTANCE;
    }

    public Unit callbackDone() {
        return Unit.INSTANCE;
    }

    public void setDataSamsung(final ConnectableDevice connectableDevice) {
        final SamSungRemoteController samSungRemoteController = SamSungRemoteController.getInstance(this);
        samSungRemoteController.connect(connectableDevice.getIpAddress(), 8002, "", new SamsungRemoteManeger.SamsungConnectListener() { // from class: com.magicapps.casttotv.tv.screen.tab.connect.ConnectActivity.3
            @Override
            public void onFailure(String str) {
                StringBuilder sb = new StringBuilder();
                sb.append("onFailure:port1 ");
                sb.append(str);
                samSungRemoteController.connect(connectableDevice.getIpAddress(), 8001, "", new SamsungRemoteManeger.SamsungConnectListener() { // from class: com.magicapps.casttotv.tv.screen.tab.connect.ConnectActivity.3.1
                    @Override
                    public void onFailure(String str2) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("onFailure:port2 ");
                        sb2.append(str2);
                    }

                    @Override
                    public void onSuccess() {
                        try {
                            ConnectActivity.this.connectToDevice(connectableDevice);
                            ConnectActivity.this.connectDeviceReady(connectableDevice);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onSuccess() {
                try {
                    ConnectActivity.this.connectToDevice(connectableDevice);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    private void initData() {
        this.connectableDevices.clear();
        this.connectableDevices.addAll(DiscoveryManager.getInstance().getCompatibleDevices().values());
        this.deviceAdapter.setData(getArrayTV(this.connectableDevices));
        if (DiscoveryManager.getInstance().getCompatibleDevices().values().size() > 0) {
            this.ll_not_device.setVisibility(8);
            this.llListConnect.setVisibility(0);
            return;
        }
        this.ll_not_device.setVisibility(0);
        this.llListConnect.setVisibility(8);
    }

    @Override
    public void onDeviceAdded(DiscoveryManager discoveryManager, ConnectableDevice connectableDevice) {
        StringBuilder sb = new StringBuilder();
        sb.append("onDeviceAdded: ");
        sb.append(connectableDevice.getId());
        Util.runOnUI(new Runnable() {
            @Override // java.lang.Runnable
            public void run() {
                ConnectActivity.this.connectableDevices.clear();
                ConnectActivity.this.connectableDevices.addAll(DiscoveryManager.getInstance().getCompatibleDevices().values());
                DeviceAdapter deviceAdapter = ConnectActivity.this.deviceAdapter;
                ConnectActivity connectActivity = ConnectActivity.this;
                deviceAdapter.setData(connectActivity.getArrayTV(connectActivity.connectableDevices));
                if (DiscoveryManager.getInstance().getCompatibleDevices().values().size() > 0) {
                    ConnectActivity.this.ll_not_device.setVisibility(8);
                    ConnectActivity.this.llListConnect.setVisibility(0);
                } else {
                    ConnectActivity.this.ll_not_device.setVisibility(0);
                    ConnectActivity.this.llListConnect.setVisibility(8);
                }
                String unused = ConnectActivity.this.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("onDeviceAdded: 1");
                sb2.append(ConnectActivity.this.connectableDevices.size());
            }
        });
        try {
            String tVType = TVType.getTVType(connectableDevice);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public void onDeviceUpdated(DiscoveryManager discoveryManager, ConnectableDevice connectableDevice) {
        StringBuilder sb = new StringBuilder();
        sb.append("onDeviceUpdated: ");
        sb.append(connectableDevice.getId());
        Util.runOnUI(new Runnable() {
            @Override // java.lang.Runnable
            public void run() {
                ConnectActivity.this.connectableDevices.clear();
                ConnectActivity.this.connectableDevices.addAll(DiscoveryManager.getInstance().getCompatibleDevices().values());
                if (DiscoveryManager.getInstance().getCompatibleDevices().values().size() > 0) {
                    ConnectActivity.this.ll_not_device.setVisibility(8);
                    ConnectActivity.this.llListConnect.setVisibility(0);
                } else {
                    ConnectActivity.this.ll_not_device.setVisibility(0);
                    ConnectActivity.this.llListConnect.setVisibility(8);
                }
                DeviceAdapter deviceAdapter = ConnectActivity.this.deviceAdapter;
                ConnectActivity connectActivity = ConnectActivity.this;
                deviceAdapter.setData(connectActivity.getArrayTV(connectActivity.connectableDevices));
            }
        });
    }

    @Override
    public void onDeviceRemoved(DiscoveryManager discoveryManager, ConnectableDevice connectableDevice) {
        StringBuilder sb = new StringBuilder();
        sb.append("onDeviceRemoved: ");
        sb.append(connectableDevice.getId());
        Util.runOnUI(new Runnable() {
            @Override // java.lang.Runnable
            public void run() {
                ConnectActivity.this.connectableDevices.clear();
                if (DiscoveryManager.getInstance().getCompatibleDevices().values().size() > 0) {
                    ConnectActivity.this.ll_not_device.setVisibility(8);
                    ConnectActivity.this.llListConnect.setVisibility(0);
                } else {
                    ConnectActivity.this.ll_not_device.setVisibility(0);
                    ConnectActivity.this.llListConnect.setVisibility(8);
                }
                DeviceAdapter deviceAdapter = ConnectActivity.this.deviceAdapter;
                ConnectActivity connectActivity = ConnectActivity.this;
                deviceAdapter.setData(connectActivity.getArrayTV(connectActivity.connectableDevices));
            }
        });
    }

    @Override
    public void onDiscoveryFailed(DiscoveryManager discoveryManager, ServiceCommandError serviceCommandError) {
        StringBuilder sb = new StringBuilder();
        sb.append("onDiscoveryFailed: ");
        sb.append(serviceCommandError.getMessage());
        Util.runOnUI(new Runnable() {
            @Override
            public void run() {
                ConnectActivity.this.connectableDevices.clear();
                if (DiscoveryManager.getInstance().getCompatibleDevices().values().size() > 0) {
                    ConnectActivity.this.ll_not_device.setVisibility(8);
                    ConnectActivity.this.llListConnect.setVisibility(0);
                } else {
                    ConnectActivity.this.ll_not_device.setVisibility(0);
                    ConnectActivity.this.llListConnect.setVisibility(8);
                }
                DeviceAdapter deviceAdapter = ConnectActivity.this.deviceAdapter;
                ConnectActivity connectActivity = ConnectActivity.this;
                deviceAdapter.setData(connectActivity.getArrayTV(connectActivity.connectableDevices));
            }
        });
    }

    public void connectToDevice(final ConnectableDevice connectableDevice) {
        runOnUiThread(new Runnable() {
            @Override // java.lang.Runnable
            public void run() {
                String unused = ConnectActivity.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("connectToDevice: ");
                sb.append(connectableDevice.getModelName());
                connectableDevice.addListener(ConnectActivity.this.connectableDeviceListener);
                connectableDevice.setPairingType(DeviceService.PairingType.PIN_CODE);
                connectableDevice.connect();
            }
        });
        try {
            Tracking.connect(this, "start", connectableDevice.getId(), TVType.getTVType(connectableDevice));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.pairingAlertDialog = new PairingAlertDialog(this, new PairingAlertDialog.PairingListener() {
            @Override
            public void onClick() {
                ConnectActivity.this.hConnectToggle();
            }
        });
        this.pairingCodeDialog = new PairingCodeDialog(this, new PairingCodeDialog.DialogListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onOk(String str) {
                try {
                    connectableDevice.sendPairingKey(str);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        });
    }

    public void hConnectToggle() {
        if (TVConnectUtils.getInstance().getConnectableDevice() != null) {
            if (TVConnectUtils.getInstance().getConnectableDevice().isConnected()) {
                TVConnectUtils.getInstance().getConnectableDevice().disconnect();
            }
            TVConnectUtils.getInstance().getConnectableDevice().removeListener(this.connectableDeviceListener);
            TVConnectUtils.getInstance().getConnectableDevice().disconnect();
        }
    }

    public void connectDeviceReady(final ConnectableDevice connectableDevice) {
        runOnUiThread(new Runnable() {
            @Override // java.lang.Runnable
            public void run() {
                String unused = ConnectActivity.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onDeviceReady: ");
                sb.append(connectableDevice.getConnectedServiceNames());
                Toast.makeText(ConnectActivity.this, "Connect success", 0).show();
                TVConnectUtils.getInstance().setConnectableDevice(connectableDevice);
                EventBus.getDefault().post(new MessageEvent("KEY_CONNECT"));
                ConnectActivity.this.isConnected = true;
            }
        });
    }

    static class AnonymousClass16 {
        static final int[] $SwitchMap$com$connectsdk$service$DeviceService$PairingType;

        static {
            int[] iArr = new int[DeviceService.PairingType.values().length];
            $SwitchMap$com$connectsdk$service$DeviceService$PairingType = iArr;
            try {
                iArr[DeviceService.PairingType.FIRST_SCREEN.ordinal()] = 1;
                $SwitchMap$com$connectsdk$service$DeviceService$PairingType[DeviceService.PairingType.PIN_CODE.ordinal()] = 2;
                $SwitchMap$com$connectsdk$service$DeviceService$PairingType[DeviceService.PairingType.MIXED.ordinal()] = 3;
                $SwitchMap$com$connectsdk$service$DeviceService$PairingType[DeviceService.PairingType.NONE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    void connectFailed(ConnectableDevice connectableDevice) {
        if (connectableDevice != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to connect to ");
            sb.append(connectableDevice.getIpAddress());
        }
        if (TVConnectUtils.getInstance().getConnectableDevice() != null) {
            TVConnectUtils.getInstance().getConnectableDevice().removeListener(this.connectableDeviceListener);
            TVConnectUtils.getInstance().getConnectableDevice().disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_web_browser) {
            startToBrowser();
        } else if (id != R.id.llBack) {
        } else {
            onBackPressed();
        }
    }

    private void startToBrowser() {
        startActivityForResult(new Intent(this, FileBrowserActivity.class), 1000);
        Utils.nextScreen(this);
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1000 && i2 == -1) {
            onBackPressed();
        }
    }

    public ArrayList<TVObject> getArrayTV(ArrayList<ConnectableDevice> arrayList) {
        String modelName;
        ArrayList<TVObject> arrayList2 = null;
        Exception e;
        try {
            ArrayList<TVObject> arrayList3 = new ArrayList<>();
            for (int i = 0; i < arrayList.size(); i++) {
                try {
                    ConnectableDevice connectableDevice = arrayList.get(i);
                    if (connectableDevice.getServiceId().equalsIgnoreCase(DIALService.ID)) {
                        if (connectableDevice.getFriendlyName() != null && connectableDevice.getFriendlyName().contains(FireTVService.ID)) {
                            IpAddressFireTV = connectableDevice.getIpAddress();
                        }
                    } else {
                        if (connectableDevice.getFriendlyName() != null) {
                            modelName = connectableDevice.getFriendlyName();
                        } else {
                            modelName = connectableDevice.getModelName();
                        }
                        ArrayList arrayList4 = new ArrayList();
                        arrayList4.add(connectableDevice);
                        if (arrayList3.size() == 0) {
                            arrayList3.add(new TVObject(modelName, arrayList4));
                        } else {
                            int i2 = 0;
                            boolean z = false;
                            while (true) {
                                if (i2 >= arrayList3.size()) {
                                    break;
                                } else if (modelName.equalsIgnoreCase(arrayList3.get(i2).getTvName())) {
                                    arrayList3.get(i2).getArrType().add(connectableDevice);
                                    z = true;
                                    break;
                                } else {
                                    i2++;
                                    z = true;
                                }
                            }
                            if (z) {
                                arrayList3.add(new TVObject(modelName, arrayList4));
                            }
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    arrayList2 = arrayList3;
                    e.printStackTrace();
                    return arrayList2;
                }
            }
            return arrayList3;
        } catch (Exception e3) {
            e = e3;
        }
        return arrayList2;
    }

    public class DeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<TVObject> connectableDevices;
        private Context context;
        private DeviceItemClick deviceItemClick;

        public DeviceAdapter(ArrayList<TVObject> arrayList, Context context, DeviceItemClick deviceItemClick) {
            this.connectableDevices = new ArrayList<>();
            this.connectableDevices = arrayList;
            this.context = context;
            this.deviceItemClick = deviceItemClick;
        }

        public void setData(List<TVObject> list) {
            this.connectableDevices.clear();
            this.connectableDevices.addAll(list);
            notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            try {
                TVObject tVObject = this.connectableDevices.get(i);
                String tvName = tVObject.getTvName();
                final ArrayList<ConnectableDevice> arrType = tVObject.getArrType();
                ((MyViewHolder) viewHolder).tvNameDevice.setText(tvName);
                int i2 = this.context.getApplicationInfo().flags & 2;
                DiscoveryManager.getInstance().getCapabilityFilters().size();
                StringBuilder sb = new StringBuilder();
                for (int i3 = 0; i3 < arrType.size(); i3++) {
                    if (i3 == tVObject.getArrType().size() - 1) {
                        sb.append(arrType.get(i3).getConnectedServiceNames());
                    } else {
                        sb.append(arrType.get(i3).getConnectedServiceNames());
                        sb.append(", ");
                    }
                }
                ((MyViewHolder) viewHolder).text_device_type.setText(sb.toString());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String unused = ConnectActivity.this.TAG;
                        if (DeviceAdapter.this.deviceItemClick != null) {
                            String unused2 = ConnectActivity.this.TAG;
                            boolean z = true;
                            if (arrType.size() == 1) {
                                String unused3 = ConnectActivity.this.TAG;
                                DeviceAdapter.this.deviceItemClick.onItemClick((ConnectableDevice) arrType.get(0));
                                return;
                            }
                            String unused4 = ConnectActivity.this.TAG;
                            boolean z2 = false;
                            for (int i4 = 0; i4 < arrType.size(); i4++) {
                                ConnectableDevice connectableDevice = (ConnectableDevice) arrType.get(i4);
                                if (connectableDevice.getConnectedServiceNames() != null) {
                                    if (connectableDevice.getConnectedServiceNames().equalsIgnoreCase(WebOSTVService.ID) || connectableDevice.getConnectedServiceNames().equalsIgnoreCase(FireTVService.ID) || connectableDevice.getConnectedServiceNames().equalsIgnoreCase(RokuService.ID)) {
                                        DeviceAdapter.this.deviceItemClick.onItemClick(connectableDevice);
                                        break;
                                    }
                                    z2 = true;
                                }
                            }
                            z = z2;
                            String unused5 = ConnectActivity.this.TAG;
                            if (!z) {
                                return;
                            }
                            DeviceAdapter.this.deviceItemClick.onItemClick((ConnectableDevice) arrType.get(0));
                        }
                    }
                });
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return this.connectableDevices.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MyViewHolder(ConnectActivity.this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device, viewGroup, false));
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView text_device_type;
        private TextView tvNameDevice;

        MyViewHolder(ConnectActivity connectActivity, View view) {
            super(view);
            try {
                this.tvNameDevice = (TextView) view.findViewById(R.id.tvNameDevice);
                this.text_device_type = (TextView) view.findViewById(R.id.text_device_type);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void registerReceiverWifi() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(this.receiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.receiver);
    }
}
