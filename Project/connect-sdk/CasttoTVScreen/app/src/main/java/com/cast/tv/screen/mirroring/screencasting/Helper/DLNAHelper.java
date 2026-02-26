package com.cast.tv.screen.mirroring.screencasting.Helper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.cast.tv.screen.mirroring.screencasting.Callback.DLNADeviceChangeCallback;
import com.cast.tv.screen.mirroring.screencasting.Callback.SimpleSubscriptionCallback;
import com.cast.tv.screen.mirroring.screencasting.CastApp;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.Observer.ConnectStatus;
import com.cast.tv.screen.mirroring.screencasting.Observer.DeviceVolume;
import com.cast.tv.screen.mirroring.screencasting.Observer.SimpleObserver;
import com.cast.tv.screen.mirroring.screencasting.Service.PlayPhotoService;
import com.cast.tv.screen.mirroring.screencasting.Utils.FileUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.L;
import com.cast.tv.screen.mirroring.screencasting.Utils.MaxRewardUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.TimeUtil;
import com.lib.screening.DLNAManager;
import com.lib.screening.DLNAPlayer;
import com.lib.screening.bean.DeviceInfo;
import com.lib.screening.bean.MediaInfo;
import com.lib.screening.listener.DLNAControlCallback;
import com.lib.screening.listener.DLNADeviceConnectListener;
import com.lib.screening.listener.DLNARegistryListener;
import com.lib.screening.listener.DLNAStateCallback;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.support.model.PositionInfo;
import org.fourthline.cling.support.model.TransportState;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DLNAHelper {
    private static final String TAG = "DLNAHelper";
    public static MutableLiveData<ConnectStatus> mConnectStatus = new MutableLiveData<>();
    public static MutableLiveData<PositionInfo> mPlayPosition = new MutableLiveData<>();
    public static MutableLiveData<TransportState> mTransportState = new MutableLiveData<>();
    public static MutableLiveData<DeviceVolume> mDeviceVolume = new MutableLiveData<>();
    private static Service mAvtService;
    private static DeviceInfo mConnectDevice;
    private static DLNACommand mDLNACommand;
    private static DLNAPlayer mDLNAPlayer;
    private static DLNADeviceChangeCallback mDeviceCallback;
    private static List<DeviceInfo> mDevices;
    private static long mReceiveTime;
    private static String tempPath,pathName;
    private static final DLNARegistryListener mDLNARegistryListener = new DLNARegistryListener() {
        @Override
        public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice remoteDevice) {
            super.remoteDeviceDiscoveryStarted(registry, remoteDevice);
            L.d(DLNAHelper.TAG, "device: " + remoteDevice.getDetails().getFriendlyName());
        }

        @Override
        public void onDeviceChanged(List<DeviceInfo> list) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - DLNAHelper.mReceiveTime < 300) {
                return;
            }
            DLNAHelper.mReceiveTime = currentTimeMillis;
            if (DLNAHelper.mDevices != null) {
                DLNAHelper.mDevices.clear();
            }
            L.d(DLNAHelper.TAG, "--------------------------------- deviceInfo ---------------------------------");
            for (DeviceInfo deviceInfo : list) {
                Service findService = deviceInfo.getDevice().findService(new UDAServiceType("AVTransport"));
                L.d(DLNAHelper.TAG, "deviceInfo: " + deviceInfo.getName() + " service -> " + findService);
                if (findService != null) {
                    if (DLNAHelper.mDevices == null) {
                        DLNAHelper.mDevices = new ArrayList();
                    }
                    DLNAHelper.mDevices.add(deviceInfo);
                }
            }
            L.d(DLNAHelper.TAG, "--------------------------------- deviceInfo ---------------------------------");
            if (DLNAHelper.mDeviceCallback == null) {
                return;
            }
            DLNAHelper.mDeviceCallback.onDeviceChange(DLNAHelper.mDevices);
        }
    };
    private static boolean mPlaying = false;
    public static final DLNADeviceConnectListener mIDLNADeviceConnectListener = new DLNADeviceConnectListener() {
        @Override
        public void onConnect(DeviceInfo deviceInfo, int i) {
            DLNAHelper.mConnectDevice = deviceInfo;
            DLNAHelper.mAvtService = DLNAHelper.mConnectDevice.getDevice().findService(new UDAServiceType("AVTransport"));
            DLNAHelper.mDLNACommand = DLNACommand.getInstance(DLNAHelper.mConnectDevice, DLNAHelper.mDLNAPlayer);
            DLNAHelper.mConnectStatus.setValue(ConnectStatus.CONNECTED);
            DLNAHelper.setDeviceActionListener();
            if (AudioVisualHelper.isPlaySingle()) {
                FileModel value = AudioVisualHelper.mCastFileModel.getValue();
                if (value == null) {
                    return;
                }
                DLNAHelper.startPlay(value);
            } else {
                FileModel audioVisualList = AudioVisualHelper.getAudioVisualList();
                if (audioVisualList == null) {
                    return;
                }
                if (AudioVisualHelper.getSelectChildIndex() != -1) {
                    FileModel fileModel = audioVisualList.getChildFiles().get(AudioVisualHelper.getSelectChildIndex());
                    if (fileModel == null) {
                        return;
                    }
                    DLNAHelper.startPlay(fileModel);
                }
            }
            L.i(DLNAHelper.TAG, "DLNADeviceConnectListener onConnect");
        }

        @Override
        public void onDisconnect(DeviceInfo deviceInfo, int i, int i2) {
            DLNAHelper.mConnectDevice = null;
            DLNAHelper.mAvtService = null;
            DLNAHelper.mDLNACommand = null;
            AudioVisualHelper.setSelectChildIndex(-1);
            DLNAHelper.mConnectStatus.setValue(ConnectStatus.DISCONNECT);
            L.i(DLNAHelper.TAG, "DLNADeviceConnectListener onDisconnect");
        }
    };

    private DLNAHelper() {
    }

    private static void initDlnaService() {
        DLNAManager.getInstance().init(CastApp.mContext, new DLNAStateCallback() {
            @Override
            public void onConnected() {
                L.i(DLNAHelper.TAG, "dlna service connect");
                DLNAHelper.initDlna();
            }

            @Override
            public void onDisconnected() {
                L.i(DLNAHelper.TAG, "initDlnaService onDisconnected");
            }
        });
    }

    public static void initDlna() {
        DLNAPlayer dLNAPlayer = new DLNAPlayer(CastApp.mContext);
        mDLNAPlayer = dLNAPlayer;
        dLNAPlayer.setConnectListener(mIDLNADeviceConnectListener);
        DLNAManager.getInstance().registerListener(mDLNARegistryListener);
    }

    public static void stopBrowser() {
        mDeviceCallback = null;
    }

    public static void startBrowser(DLNADeviceChangeCallback dLNADeviceChangeCallback) {
        mDeviceCallback = dLNADeviceChangeCallback;
        if (mDLNAPlayer != null) {
            DLNAManager.getInstance().refreshDevice();
        } else {
            initDlnaService();
        }
    }

    public static void connectDevice(DeviceInfo deviceInfo) {
        DLNAPlayer dLNAPlayer = mDLNAPlayer;
        if (dLNAPlayer != null) {
            dLNAPlayer.connect(deviceInfo);
        }
    }

    public static void disconnectDevice() {
        if (mDLNAPlayer != null) {
            stop();
            mConnectStatus.setValue(ConnectStatus.DISCONNECT);
            mConnectDevice = null;
            mAvtService = null;
            mDLNACommand = null;
            List<DeviceInfo> list = mDevices;
            if (list != null) {
                list.clear();
                mDevices = null;
            }
            AudioVisualHelper.setSelectChildIndex(-1);
        }
    }

    public static void refresh() {
        DLNAManager.getInstance().refreshDevice();
    }

    public static DeviceInfo getConnectDevice() {
        return mConnectDevice;
    }

    public static void startPlay(FileModel fileModel) {
        if (MaxRewardUtil.obtainCastFileNum() <= 0 || mDLNAPlayer == null || mConnectDevice == null || fileModel == null) {
            return;
        }
        preparePlay(fileModel);
        MaxRewardUtil.reduceCastNum();
    }

    public static void startYoutubePlay(String url) {
        if (MaxRewardUtil.obtainCastFileNum() <= 0 || mDLNAPlayer == null || mConnectDevice == null || url == null) {
            return;
        }
        prepareYoutubePlay(url);
        MaxRewardUtil.reduceCastNum();
    }

    private static void preparePlay(final FileModel fileModel) {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public final void subscribe(ObservableEmitter observableEmitter) {
                DLNAHelper.$preparePlay$0(fileModel, observableEmitter);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SimpleObserver<Object>() {
            @Override
            public void onNext(Object obj) {
                int fileType = fileModel.getFileType();
                Log.d("FileType", String.valueOf(fileModel.getFileType()));
                try{
//                    if(fileModel.getPath().contains(".mp4")){
//                        SharedPreferences sharedPreferences =CastApp.mContext.getSharedPreferences("VideoCastSharedPreference", Activity.MODE_PRIVATE);
//                        tempPath = sharedPreferences.getString("VideoPath","");
//                        tempPath=tempPath.replace("[!@#$%^&*()-+={}?% ]*","");
//                        Log.d("tempPath mp4", tempPath);
//                    }
//                    else if(fileModel.getPath().contains(".wav") ) {
//                        SharedPreferences sharedPreferences =CastApp.mContext.getSharedPreferences("AudioCastSharedPreference", Activity.MODE_PRIVATE);
//                        tempPath = sharedPreferences.getString("AudioPath","");
//                        tempPath=tempPath.replace("[!@#$%^&*()-+={}?% ]*","");
//                        Log.d("tempPath wav", tempPath);
//                    }
                     if(fileModel.getPath().contains(".png")) {
                        SharedPreferences sharedPreferences =CastApp.mContext.getSharedPreferences("ImageCastSharedPreference", Activity.MODE_PRIVATE);
                        tempPath = sharedPreferences.getString("ImagePath","");
                        tempPath=tempPath.replace("[!@#$%^&*()-+={}?% ]*","");
                        Log.d("tempPath png", tempPath);
                    }else
                     {
                        tempPath = fileModel.getTempPath();
                        Log.d("tempPath", String.valueOf(fileModel.getPath()));
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }


                MediaInfo mediaInfo = new MediaInfo();
                int i = 2;
                if (!TextUtils.isEmpty(tempPath)) {
                    mediaInfo.setMediaId(Base64.encodeToString(tempPath.getBytes(), 2));
                    mediaInfo.setUri(tempPath);
                }
                if (fileType != 273) {
                    i = fileType == 274 ? 3 : 1;
                }
                mediaInfo.setMediaName(fileModel.getDisplayName());
                mediaInfo.setMediaType(i);
                DLNAHelper.mDLNAPlayer.setDataSource(mediaInfo);
                DLNAHelper.mDLNAPlayer.start(new DLNAControlCallback() {
                    @Override
                    public void onFailure(ActionInvocation actionInvocation, int i, String str) {
                        L.i(DLNAHelper.TAG, "TAG " + Thread.currentThread().getName());
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                DLNAHelper.mConnectStatus.setValue(ConnectStatus.FAIL);
                            }
                        });
                    }

                    @Override
                    public void onReceived(ActionInvocation actionInvocation, Object... objArr) {

                    }

                    @Override
                    public void onSuccess(ActionInvocation actionInvocation) {
                        L.i(DLNAHelper.TAG, "投屏成功");
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DLNAHelper.play();
                    }
                }, 700L);
            }
        });
    }

    public static void $preparePlay$0(FileModel fileModel, ObservableEmitter observableEmitter) {
        String path = fileModel.getPath();
        if (!path.contains("/storage/emulated/0")) {
            String str = CastApp.mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator + fileModel.getDisplayName();
            L.d("newPath", "newPath: " + str);
            if (!new File(str).exists()) {
                FileUtil.copyFile(path, str);
            }
            fileModel.setTempPath(str);
        }
        observableEmitter.onNext(fileModel);
    }


    private static void prepareYoutubePlay(final String url) {
        MediaInfo mediaInfo = new MediaInfo();
        int i = 2;
        //if (!TextUtils.isEmpty(tempPath)) {
        //     mediaInfo.setMediaId(Base64.encodeToString(tempPath.getBytes(), 2));
        mediaInfo.setUri(url);

//                if (fileType != 273) {
//                    i = fileType == 274 ? 3 : 1;
//                }
        mediaInfo.setMediaName(url);
        mediaInfo.setMediaType(i);
        DLNAHelper.mDLNAPlayer.setDataSource(mediaInfo);
        DLNAHelper.mDLNAPlayer.start(new DLNAControlCallback() {
            @Override
            public void onFailure(ActionInvocation actionInvocation, int i, String str) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        DLNAHelper.mConnectStatus.setValue(ConnectStatus.FAIL);
                    }
                });
            }

            @Override
            public void onReceived(ActionInvocation actionInvocation, Object... objArr) {

            }

            @Override
            public void onSuccess(ActionInvocation actionInvocation) {
                L.i(DLNAHelper.TAG, "投屏成功");
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DLNAHelper.play();
            }
        }, 700L);
    }


    public static boolean isConnectDevice() {
        return mConnectDevice != null;
    }

    public static void play() {
        if (mDLNACommand == null) {
            return;
        }
        mConnectStatus.setValue(ConnectStatus.PLAYING);
        mDLNACommand.play();
    }

    public static void stop() {
        if (mDLNACommand == null) {
            return;
        }
        mConnectStatus.setValue(ConnectStatus.STOP);
        mDLNACommand.stop();
//        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "CastFolder");
//        if (dir.isDirectory())
//        {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++)
//            {
//                new File(dir, children[i]).delete();
//            }
//        }
        File imagedir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CastFolder");
        if (imagedir.isDirectory())
        {
            String[] children = imagedir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(imagedir, children[i]).delete();
            }
        }

    }
    public static void deleteFiles(File file) {
        File file2 = new File(file.getPath());
        if (file2.exists()) {
            file2.delete();
        }
    }
    public static void pause() {
        DLNACommand dLNACommand = mDLNACommand;
        if (dLNACommand == null) {
            return;
        }
        dLNACommand.pause();
        mConnectStatus.setValue(ConnectStatus.PAUSE);
    }

    public static void seekTo(int i) {
        DLNACommand dLNACommand = mDLNACommand;
        if (dLNACommand == null) {
            return;
        }
        dLNACommand.seekTo(TimeUtil.int2String(i));
    }

    public static void seekFastForward(String str) {
        DLNACommand dLNACommand = mDLNACommand;
        if (dLNACommand == null) {
            return;
        }
        dLNACommand.seekFastForward(str);
    }

    public static void seekBackOff(String str) {
        DLNACommand dLNACommand = mDLNACommand;
        if (dLNACommand == null) {
            return;
        }
        dLNACommand.seekBackOff(str);
    }

    public static void getTransportInfo() {
        DLNACommand dLNACommand = mDLNACommand;
        if (dLNACommand == null) {
            return;
        }
        dLNACommand.getTransportInfo();
    }

    public static void getPositionInfo() {
        DLNACommand dLNACommand = mDLNACommand;
        if (dLNACommand == null) {
            return;
        }
        dLNACommand.getPositionInfo();
    }

    public static void getVolume() {
        DLNACommand dLNACommand = mDLNACommand;
        if (dLNACommand == null) {
            return;
        }
        dLNACommand.getVolume();
    }

    public static void setVolume(int i) {
        DLNACommand dLNACommand = mDLNACommand;
        if (dLNACommand == null) {
            return;
        }
        dLNACommand.setVolume(i);
    }

    public static void setDeviceActionListener() {
        mDLNAPlayer.execute(new SimpleSubscriptionCallback(mAvtService) {
            @Override
            protected void eventReceived(GENASubscription gENASubscription) {
                if (AudioVisualHelper.mCastFileModel.getValue() == null) {
                    return;
                }
                if (CastApp.DEBUG) {
                    for (Iterator iterator = gENASubscription.getCurrentValues().entrySet().iterator(); iterator.hasNext(); ) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        L.i(DLNAHelper.TAG, "key: " + ((String) entry.getKey()) + ", value: " + entry.getValue());
                    }
                }
                if (DLNAHelper.mDLNACommand == null) {
                    return;
                }
                DLNAHelper.mDLNACommand.getPositionInfo();
                DLNAHelper.mDLNACommand.getTransportInfo();
            }
        });
    }

    public static void startPlayPhotoService() {
        if (!mPlaying) {
            mPlaying = true;
            CastApp.mContext.startService(new Intent(CastApp.mContext, PlayPhotoService.class));
        }
    }

    public static void stopPlayPhotoService() {
        if (mPlaying) {
            CastApp.mContext.stopService(new Intent(CastApp.mContext, PlayPhotoService.class));
            mPlaying = false;
        }
    }

    public static void recycler() {
        List<DeviceInfo> list = mDevices;
        if (list != null) {
            list.clear();
            mDevices = null;
        }
        DLNAPlayer dLNAPlayer = mDLNAPlayer;
        if (dLNAPlayer != null) {
            dLNAPlayer.destroy();
            mDLNAPlayer = null;
        }
        mDeviceCallback = null;
        DLNAManager.getInstance().destroy();
    }
}
