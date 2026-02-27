package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.remote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseFragment;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.Channels;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.ModelChannelsSamsung;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVType;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.premium.IapUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.firetv.FireTVManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.other.SamSungRemoteController;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.other.SamsungRemoteManeger;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.other.ViewUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks.ChannelTask;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks.RequestCallback;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.CommandHelper;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.RokuRequestTypes;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.sony.AppsListener;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.sony.RemoteSonyManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.sony.SimpleNetworkListener;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.sony.TVApp;
import com.connectsdk.core.AppInfo;
import com.connectsdk.service.capability.Launcher;
import com.connectsdk.service.command.ServiceCommandError;
import com.google.gson.Gson;
import com.jaku.core.JakuRequest;
import com.jaku.model.Channel;
import com.jaku.request.LaunchAppRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;

@SuppressLint("WrongConstant")
public class ChannelFragment extends BaseFragment {
    public static ChannelSonyAdapter channelSonyAdapter;
    public static ArrayList<TVApp> channelSonyTVList = new ArrayList<>();
    private ChannelLGAdapter channelAdapter;
    private ChannelSamsungAdapter channelSamsungAdapter;
    private FrameLayout frl_native;
    private ProgressBar loading;
    private Activity mActivity;
    private ChannelAdapter mAdapter;
    private ViewGroup main_ads_native;
    private RecyclerView rcv_list_channel_lg;
    private RemoteActivity remoteActivity;
    private RelativeLayout rlBannerAds;
    private TextView tv_connect_tv;
    private List<Channel> channelsArray = new ArrayList();
    private CompositeDisposable bin = new CompositeDisposable();
    private ArrayList<AppInfo> channelInfos = new ArrayList<>();
    private ArrayList<Channels> arrChannels = new ArrayList<>();
    private BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.ChannelFragment.7
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            ChannelFragment.this.loadChannels();
        }
    };
    private int numberClick = 0;

    @Override
    // com.magicapps.casttotv.tv.base.BaseFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mActivity = getActivity();
        setHasOptionsMenu(true);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("wseemann.media.romote.UPDATE_DEVICE");
        this.mActivity.registerReceiver(this.mUpdateReceiver, intentFilter);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        StringBuilder sb = new StringBuilder();
        sb.append("event bus channel");
        sb.append(messageEvent.getMessage());
        try {
            loadView();
            if (!IapUtils.isPaymentMirror() && !IapUtils.isIapAll()) {
                return;
            }
            this.rlBannerAds.setVisibility(8);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public Unit callbackFail() {
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Unit callbackDone() {
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Unit actionCommon() {
        return Unit.INSTANCE;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_channel, viewGroup, false);
        this.tv_connect_tv = (TextView) inflate.findViewById(R.id.tv_connect_tv);
        this.loading = (ProgressBar) inflate.findViewById(R.id.loading);
        this.rlBannerAds = (RelativeLayout) inflate.findViewById(R.id.rlBannerAds);
        this.main_ads_native = (ViewGroup) inflate.findViewById(R.id.main_ads_native);
        this.frl_native = (FrameLayout) inflate.findViewById(R.id.frl_native);
        this.rcv_list_channel_lg = (RecyclerView) inflate.findViewById(R.id.rcv_list_channel_lg);
        ChannelFragment.this.callbackDone();
        loadView();
        return inflate;
    }

    private void loadView() {
        if (TVConnectUtils.getInstance().isConnected()) {
            this.tv_connect_tv.setVisibility(8);
            StringBuilder sb = new StringBuilder();
            sb.append("connected: ");
            sb.append(TVConnectUtils.getInstance().getDeviveName());
            if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("load roku ");
                sb2.append(TVConnectUtils.getInstance().getDeviveName());
                loadChannels();
                this.rcv_list_channel_lg.setVisibility(0);
                return;
            } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("load lg ");
                sb3.append(TVConnectUtils.getInstance().getDeviveName());
                loadChannelsLG();
                this.rcv_list_channel_lg.setVisibility(0);
                return;
            } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                this.rcv_list_channel_lg.setVisibility(0);
                loadChannelSamsung();
                return;
            } else if (TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                this.rcv_list_channel_lg.setVisibility(0);
                loadChannelSony();
                return;
            } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                this.rcv_list_channel_lg.setVisibility(0);
                loadChannelsFireTV();
                return;
            } else {
                this.tv_connect_tv.setText(getString(R.string.can_find_channel));
                this.tv_connect_tv.setVisibility(0);
                return;
            }
        }
        this.rcv_list_channel_lg.setVisibility(8);
        this.tv_connect_tv.setVisibility(0);
        this.tv_connect_tv.setText(getString(R.string.can_channel_not_connect));
    }

    private void loadChannelsFireTV() {
        if (RemoteFragment.fireTVManager != null) {
            this.loading.setVisibility(0);
            if (!RemoteFragment.fireTVManager.getChannelFireTVDtoArrayList().isEmpty()) {
                this.loading.setVisibility(8);
            }
            this.rcv_list_channel_lg.setAdapter(new ChannelFireTVAdapter(getContext(), RemoteFragment.fireTVManager.getChannelFireTVDtoArrayList(), new ChannelFireTVAdapter.IItemClick() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.ChannelFragment.1
                @Override
                // com.magicapps.casttotv.tv.screen.tab.remote.ChannelFireTVAdapter.IItemClick
                public void clickItem(int i) {
                    if (FireTVManager.myArrayList.toString().contains(RemoteFragment.fireTVManager.getChannelFireTVDtoArrayList().get(i).getPackageName())) {
                        FireTVManager fireTVManager = RemoteFragment.fireTVManager;
                        fireTVManager.launchChannelFromRV("am start -n " + RemoteFragment.fireTVManager.getChannelFireTVDtoArrayList().get(i).getActivityName());
                        Context context = ChannelFragment.this.getContext();
                        Toast.makeText(context, "Launching " + RemoteFragment.fireTVManager.getChannelFireTVDtoArrayList().get(i).getName(), 0).show();
                    } else if (RemoteFragment.fireTVManager.getChannelFireTVDtoArrayList().get(i).getPackageName().equals("com.amazon.cloud9")) {
                        FireTVManager fireTVManager2 = RemoteFragment.fireTVManager;
                        fireTVManager2.launchChannelFromRV("am start -n " + RemoteFragment.fireTVManager.getChannelFireTVDtoArrayList().get(i).getActivityName());
                        Context context2 = ChannelFragment.this.getContext();
                        Toast.makeText(context2, "Launching " + RemoteFragment.fireTVManager.getChannelFireTVDtoArrayList().get(i).getName(), 0).show();
                    } else {
                        FireTVManager fireTVManager3 = RemoteFragment.fireTVManager;
                        fireTVManager3.launchChannelFromRV("am start -d 'amzn://apps/android?p=" + RemoteFragment.fireTVManager.getChannelFireTVDtoArrayList().get(i).getPackageName() + "'");
                        Toast.makeText(ChannelFragment.this.getContext(), "Redirecting to Appstore", 0).show();
                    }
                    if (ChannelFragment.this.remoteActivity != null) {
                        ChannelFragment.this.remoteActivity.setCurrentPage(0, true);
                    }
                }
            }));
            this.rcv_list_channel_lg.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
    }

    private void loadChannelSony() {
        channelSonyAdapter = new ChannelSonyAdapter(getActivity(), channelSonyTVList);
        this.rcv_list_channel_lg.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        this.rcv_list_channel_lg.setAdapter(channelSonyAdapter);
        channelSonyAdapter.setListener(new ChannelSonyAdapter.OnClickChannelListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.ChannelFragment.2
            @Override
            // com.magicapps.casttotv.tv.screen.tab.remote.ChannelSonyAdapter.OnClickChannelListener
            public void onItemClick(TVApp tVApp) {
                if (ChannelFragment.this.getContext() != null) {
                    ViewUtils.provideHapticFeedback(ChannelFragment.this.getContext(), 100);
                }
                RemoteSonyManager.getInstance().openTVApp(tVApp, new SimpleNetworkListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.ChannelFragment.2.1
                    @Override
                    // com.magicapps.casttotv.tv.utils.remote.sony.SimpleNetworkListener
                    public void onError() {
                    }

                    @Override
                    // com.magicapps.casttotv.tv.utils.remote.sony.SimpleNetworkListener
                    public void onFinish(boolean z) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("onFinish: ");
                        sb.append(z);
                    }
                });
                if (ChannelFragment.this.remoteActivity == null) {
                    return;
                }
                ChannelFragment.this.remoteActivity.setCurrentPage(0, true);
            }
        });
        RemoteSonyManager.getInstance().getTVApps(new AppsListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.ChannelFragment.3
            @Override // com.magicapps.casttotv.tv.utils.remote.sony.AppsListener
            public void onError() {
            }

            @Override // com.magicapps.casttotv.tv.utils.remote.sony.AppsListener
            public void onAppsFetched(ArrayList<TVApp> arrayList) {
                ChannelFragment.channelSonyTVList.clear();
                ChannelFragment.channelSonyTVList.addAll(arrayList);
                ChannelFragment.channelSonyAdapter.setData(ChannelFragment.channelSonyTVList);
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.remoteActivity = (RemoteActivity) context;
    }

    private void loadChannelSamsung() {
        this.channelSamsungAdapter = new ChannelSamsungAdapter(getContext(), new ArrayList());
        this.rcv_list_channel_lg.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        this.rcv_list_channel_lg.setAdapter(this.channelSamsungAdapter);
        this.channelSamsungAdapter.setListener(new ChannelSamsungAdapter.OnClickChannelListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.ChannelFragment.4
            @Override
            // com.magicapps.casttotv.tv.screen.tab.remote.ChannelSamsungAdapter.OnClickChannelListener
            public void onItemClick(int i, Channels channels, ArrayList<Channels> arrayList) {
                if (ChannelFragment.this.getContext() != null) {
                    ViewUtils.provideHapticFeedback(ChannelFragment.this.getContext(), 100);
                }
                SamsungRemoteManeger samsungRemoteManeger = SamSungRemoteController.getInstance(ChannelFragment.this.getContext()).getSamsungRemoteManeger();
                Toast.makeText(ChannelFragment.this.getContext(), channels.getName(), 1).show();
                if (!samsungRemoteManeger.isConnected()) {
                    return;
                }
                RequestTask requestTask = new RequestTask(ChannelFragment.this);
                requestTask.execute("http://" + samsungRemoteManeger.getIP() + ":8001/api/v2/applications/" + channels.getAppId());
                ChannelFragment.this.getContext();
                if (ChannelFragment.this.remoteActivity == null) {
                    return;
                }
                ChannelFragment.this.remoteActivity.setCurrentPage(0, true);
            }
        });
        SamsungRemoteManeger samsungRemoteManeger = SamSungRemoteController.getInstance(getContext()).getSamsungRemoteManeger();
        if (samsungRemoteManeger != null) {
            samsungRemoteManeger.getAllChanel(new SamsungRemoteManeger.GetAllChannelListener() {
                @Override
                public void onSuccess(String str) {
                    if (str != null) {
                        try {
                            StringBuilder sb = new StringBuilder();
                            sb.append("refreshConnect: 4444 ");
                            sb.append(str);
                            final ModelChannelsSamsung channels = ChannelFragment.this.getChannels(str);
                            ChannelFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (channels != null) {
                                        ChannelFragment.this.arrChannels.clear();
                                        ChannelFragment.this.arrChannels.addAll(channels.getData().getDataChannels());
                                        ChannelFragment.this.channelSamsungAdapter.setData(ChannelFragment.this.arrChannels);
                                        ChannelFragment.this.tv_connect_tv.setVisibility(8);
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append("refreshConnect: 666666 ");
                                        sb2.append(ChannelFragment.this.arrChannels.size());
                                        return;
                                    }
                                    ChannelFragment.this.tv_connect_tv.setText(ChannelFragment.this.getString(R.string.empty));
                                    ChannelFragment.this.tv_connect_tv.setVisibility(0);
                                }
                            });
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }

                @Override
                // com.magicapps.casttotv.tv.utils.remote.other.SamsungRemoteManeger.GetAllChannelListener
                public void onFail(String str) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("refreshConnect: 5555 ");
                    sb.append(str);
                }
            });
        }
    }

    /* loaded from: classes4.dex */
    public class RequestTask extends AsyncTask<String, String, String> {
        public RequestTask(ChannelFragment channelFragment) {
        }

        @Override // android.os.AsyncTask
        public String doInBackground(String... strArr) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(strArr[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();
                httpURLConnection.getResponseCode();
                return null;
            } catch (Exception e2) {
                System.out.println(e2.getMessage());
                return null;
            }
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(String str) {
            super.onPostExecute(str);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:4:0x0008, code lost:
        if (r3.equals("") != false) goto L2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ModelChannelsSamsung getChannels(String str) {
        if (str != null) {
            try {
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
        str = getDefaultChannels();
        StringBuilder sb = new StringBuilder();
        sb.append("getChannels: data null ");
        sb.append(str);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getChannels: data ");
        sb2.append(str);
        return (ModelChannelsSamsung) new Gson().fromJson(str, ModelChannelsSamsung.class);
    }

    private String getDefaultChannels() {
        try {
            InputStream open = getActivity().getAssets().open("channels.json");
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr, "UTF-8");
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    @Override
    // com.magicapps.casttotv.tv.base.BaseFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override
    // com.magicapps.casttotv.tv.base.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        this.bin.dispose();
        this.mActivity.unregisterReceiver(this.mUpdateReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadChannels() {
        this.rcv_list_channel_lg.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        this.mAdapter = new ChannelAdapter(getContext(), this.channelsArray, new ChannelAdapter.IItemClick() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.ChannelFragment.6
            @Override
            // com.magicapps.casttotv.tv.screen.tab.remote.ChannelAdapter.IItemClick
            public void clickItem(int i) {
                try {
                    if (ChannelFragment.this.getContext() != null) {
                        ViewUtils.provideHapticFeedback(ChannelFragment.this.getContext(), 100);
                    }
                    ChannelFragment.this.performLaunch(((Channel) ChannelFragment.this.channelsArray.get(i)).getId());
                    ChannelFragment.this.mActivity.sendBroadcast(new Intent("wseemann.media.romote.UPDATE_DEVICE"));
                    if (ChannelFragment.this.remoteActivity == null) {
                        return;
                    }
                    ChannelFragment.this.remoteActivity.setCurrentPage(0, true);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        if (this.channelsArray.isEmpty()) {
            this.tv_connect_tv.setText(getString(R.string.empty));
            this.tv_connect_tv.setVisibility(0);
        }
        this.rcv_list_channel_lg.setAdapter(this.mAdapter);
        try {
            CompositeDisposable compositeDisposable = this.bin;
            if (compositeDisposable == null) {
                return;
            }
            compositeDisposable.add(Observable.fromCallable(new ChannelTask(getContext())).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.ChannelFragment$$ExternalSyntheticLambda0
                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    ChannelFragment.this.lambda$loadChannels$0(channelsArray);
                }
            }));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onLoadFinished */
    public void lambda$loadChannels$0(List<Channel> list) {
        try {
            ChannelAdapter channelAdapter = this.mAdapter;
            if (channelAdapter == null) {
                return;
            }
            channelAdapter.setData(list);
            if (list.isEmpty()) {
                this.tv_connect_tv.setText(getString(R.string.empty));
                this.tv_connect_tv.setVisibility(0);
            } else {
                this.tv_connect_tv.setVisibility(8);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(list.size());
            sb.append("");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performLaunch(String str) {
        new com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks.RequestTask(new JakuRequest(new LaunchAppRequest(CommandHelper.getDeviceURL(getActivity()), str), null), new RequestCallback() {
            @Override
            public void onErrorResponse(com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks.RequestTask.Result result) {

            }

            @Override
            public void requestResult(RokuRequestTypes rokuRequestTypes, com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks.RequestTask.Result result) {

            }
        }).execute(RokuRequestTypes.launch);
    }

    public static void add(ArrayList arrayList, String str, String str2, String str3, String str4) {
        arrayList.add(str);
        arrayList.add(str2);
        arrayList.add(str3);
        arrayList.add(str4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupUI(final List<AppInfo> list) {
        try {
            if (getActivity() == null) {
                return;
            }
            getActivity().runOnUiThread(new Runnable() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.ChannelFragment.9
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ChannelFragment.this.loading.setVisibility(8);
                        if (list == null) {
                            return;
                        }
                        ChannelFragment.this.channelInfos.clear();
                        ArrayList arrayList = new ArrayList();
                        for (int i = 0; i < list.size(); i++) {
                            AppInfo appInfo = (AppInfo) list.get(i);
                            String id = appInfo.getId();
                            if (!TextUtils.isEmpty(appInfo.getName())) {
                                ArrayList arrayList2 = new ArrayList();
                                arrayList2.add("com.webos.app.acrcard");
                                arrayList2.add("com.webos.app.acrcomponent");
                                arrayList2.add("com.webos.app.acrhdmi1");
                                arrayList2.add("com.webos.app.acrhdmi2");
                                arrayList2.add("com.webos.app.acrhdmi3");
                                ChannelFragment.add(arrayList2, "com.webos.app.acrhdmi4", "com.webos.app.acroverlay", "com.webos.app.alibabafull", "com.webos.app.brandshop");
                                ChannelFragment.add(arrayList2, "com.webos.app.cheeringtv", "com.webos.app.container", "com.webos.app.crb", "com.webos.app.discovery");
                                ChannelFragment.add(arrayList2, "com.webos.app.dvrpopup", "com.webos.app.eula", "com.webos.app.externalinput.av1", "com.webos.app.externalinput.av2");
                                ChannelFragment.add(arrayList2, "com.webos.app.externalinput.component", "com.webos.app.externalinput.scart", "com.webos.app.facebooklogin", "com.webos.app.factorywin");
                                ChannelFragment.add(arrayList2, "com.webos.app.favshows", "com.webos.app.googleassistant", "com.webos.app.hdmi1", "com.webos.app.hdmi2");
                                ChannelFragment.add(arrayList2, "com.webos.app.hdmi3", "com.webos.app.hdmi4", "com.webos.app.inputcommon", "com.webos.app.installation");
                                ChannelFragment.add(arrayList2, "com.webos.app.iot-thirdparty-login", "com.webos.app.livehbbtv", "com.webos.app.livemenuplayer-inav1", "com.webos.app.livemenuplayer-inav2");
                                ChannelFragment.add(arrayList2, "com.webos.app.livemenuplayer-incomponent", "com.webos.app.livemenuplayer-inhdmi1", "com.webos.app.livemenuplayer-inhdmi2", "com.webos.app.livemenuplayer-inhdmi3");
                                ChannelFragment.add(arrayList2, "com.webos.app.livemenuplayer-inhdmi4", "com.webos.app.livemenuplayer-inscart", "com.webos.app.livemenuplayer-intv", "com.webos.app.livezoom-inhdmi1");
                                ChannelFragment.add(arrayList2, "com.webos.app.livezoom-inhdmi2", "com.webos.app.livezoom-inhdmi3", "com.webos.app.livezoom-inhdmi4", "com.webos.app.livezoom-inphotovideo");
                                ChannelFragment.add(arrayList2, "com.webos.app.livezoom-inrecordings", "com.webos.app.livezoom-insmhl", "com.webos.app.livezoom-intv", "com.webos.app.magicnum");
                                ChannelFragment.add(arrayList2, "com.webos.app.miracast-overlay", "com.webos.app.mystarter", "com.webos.app.screensaver", "com.webos.app.softwareupdate");
                                ChannelFragment.add(arrayList2, "com.webos.app.systemmusic", "com.webos.app.tips", "com.webos.app.tvhotkey", "com.webos.app.tvsimpleviewer");
                                arrayList2.add("com.webos.app.voiceview");
                                arrayList2.add("com.webos.app.webapphost");
                                if (!arrayList2.contains(id)) {
                                    if ((id.equalsIgnoreCase("netflix") && appInfo.getName().equals("Netflix")) || ((id.equalsIgnoreCase("youtube.leanback.v4") && appInfo.getName().equals("YouTube")) || (id.equalsIgnoreCase("amazon") && appInfo.getName().equals("Amazon Prime Video")))) {
                                        ChannelFragment.this.channelInfos.add(appInfo);
                                    } else {
                                        arrayList.add(appInfo);
                                    }
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("onSuccesscc: ");
                                    sb.append(id);
                                }
                            }
                        }
                        ChannelFragment.this.channelInfos.addAll(arrayList);
                        ChannelFragment.this.channelAdapter.setData(ChannelFragment.this.channelInfos);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(ChannelFragment.this.channelInfos.size());
                        sb2.append(" ");
                        if (ChannelFragment.this.channelInfos.size() > 0) {
                            ChannelFragment.this.tv_connect_tv.setVisibility(8);
                            return;
                        }
                        ChannelFragment.this.tv_connect_tv.setVisibility(0);
                        if (TVConnectUtils.getInstance().isConnected()) {
                            ChannelFragment.this.tv_connect_tv.setText(ChannelFragment.this.getString(R.string.can_find_channel));
                        } else {
                            ChannelFragment.this.tv_connect_tv.setText(ChannelFragment.this.getString(R.string.can_channel_not_connect));
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            });
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void loadChannelsLG() {
        try {
            this.rcv_list_channel_lg.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            ChannelLGAdapter channelLGAdapter = new ChannelLGAdapter(getActivity(), new ArrayList());
            this.channelAdapter = channelLGAdapter;
            this.rcv_list_channel_lg.setAdapter(channelLGAdapter);
            this.channelAdapter.setListener(new ChannelLGAdapter.OnClickChannelListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.ChannelFragment.10
                @Override
                // com.magicapps.casttotv.tv.screen.tab.remote.ChannelLGAdapter.OnClickChannelListener
                public void onItemClick(int i, AppInfo appInfo, ArrayList<AppInfo> arrayList) {
                    try {
                        if (ChannelFragment.this.getContext() != null) {
                            ViewUtils.provideHapticFeedback(ChannelFragment.this.getContext(), 100);
                        }
                        ((Launcher) TVConnectUtils.getInstance().getConnectableDevice().getCapability(Launcher.class)).launchApp(appInfo.getId(), null);
                        if (ChannelFragment.this.remoteActivity == null) {
                            return;
                        }
                        ChannelFragment.this.remoteActivity.setCurrentPage(0, true);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            });
            this.loading.setVisibility(0);
            if (TVConnectUtils.getInstance().isConnected()) {
                ((Launcher) TVConnectUtils.getInstance().getConnectableDevice().getCapability(Launcher.class)).getAppList(new Launcher.AppListListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.ChannelFragment.11
                    @Override // com.connectsdk.service.capability.listeners.ResponseListener
                    public void onSuccess(List<AppInfo> list) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("onSuccess: ");
                        sb.append(list.size());
                        ChannelFragment.this.setupUI(list);
                    }

                    @Override // com.connectsdk.service.capability.listeners.ErrorListener
                    public void onError(ServiceCommandError serviceCommandError) {
                        ChannelFragment.this.loading.setVisibility(8);
                        StringBuilder sb = new StringBuilder();
                        sb.append("onErrror: ");
                        sb.append(serviceCommandError.toString());
                    }
                });
            } else {
                this.loading.setVisibility(8);
                this.channelInfos.clear();
                this.channelAdapter.setData(this.channelInfos);
                if (this.channelInfos.size() > 0) {
                    this.tv_connect_tv.setVisibility(8);
                } else {
                    this.tv_connect_tv.setVisibility(0);
                    if (TVConnectUtils.getInstance().isConnected()) {
                        this.tv_connect_tv.setText(getString(R.string.can_find_channel));
                    } else {
                        this.tv_connect_tv.setText(getString(R.string.can_channel_not_connect));
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
