package com.lib.screening;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaCodec;
import android.media.projection.MediaProjection;
import android.os.IBinder;
import android.util.Base64;

import com.chillingvan.lib.muxer.IMuxer;
import com.chillingvan.lib.muxer.RTMPStreamMuxer;
import com.chillingvan.lib.publisher.StreamPublisher;
import com.lib.nginxserver.nginx.NginxHelper;
import com.lib.screening.bean.DeviceInfo;
import com.lib.screening.bean.MediaInfo;
import com.lib.screening.listener.DLNAControlCallback;
import com.lib.screening.listener.DLNADeviceConnectListener;
import com.lib.screening.listener.OnRequestMediaProjectionResultCallback;
import com.lib.screenrecorder.IRecorderCallback;
import com.lib.screenrecorder.IScreenRecorderService;
import com.lib.screenrecorder.Notifications;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.support.avtransport.callback.Play;
import org.fourthline.cling.support.avtransport.callback.SetAVTransportURI;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.ProtocolInfo;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.item.AudioItem;
import org.fourthline.cling.support.model.item.ImageItem;
import org.fourthline.cling.support.model.item.VideoItem;
import org.seamless.util.MimeType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DLNAPlayer {
    public static final int BUFFER = 4;
    private static final String DIDL_LITE_FOOTER = "</DIDL-Lite>";
    private static final String DIDL_LITE_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\">";
    private DLNADeviceConnectListener connectListener;
    private Context mContext;
    private Device mDevice;
    private DeviceInfo mDeviceInfo;
    private MediaInfo mMediaInfo;
    private DLNAControlCallback mMirrorControlCallback;
    private Notifications mNotifications;
    private IScreenRecorderService mScreenRecorderService;
    private ServiceConnection mServiceConnection;
    private IMuxer mStreamMuxer;
    private AndroidUpnpService mUpnpService;
    private int currentState = -1;
    private long mNotificationStartTime = 0;
    private OnRequestMediaProjectionResultCallback mRequestMediaProjectionResultCallback = new OnRequestMediaProjectionResultCallback() {
        @Override
        public void onMediaProjectionResult(MediaProjection mediaProjection) {
            if (mScreenRecorderService != null) {
                mScreenRecorderService.prepareAndStartRecorder(mediaProjection, null, null);
            } else if (mMirrorControlCallback == null) {
            } else {
                mMirrorControlCallback.onFailure(null, 7, "");
            }
        }
    };    private ServiceConnection mScreenRecorderServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mScreenRecorderService = (IScreenRecorderService) iBinder;
            prepareMediaProjection();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mScreenRecorderService = null;
            mScreenRecorderServiceConnection = null;
        }
    };
    private ServiceType AV_TRANSPORT_SERVICE = new UDAServiceType("AVTransport");    private IRecorderCallback mRecorderCallback = new IRecorderCallback() {
        @Override
        public void onPrepareRecord() {
            mStreamMuxer = new RTMPStreamMuxer();
            StreamPublisher.StreamPublisherParam createStreamPublisherParam = new StreamPublisher.StreamPublisherParam(mScreenRecorderService.getVideoEncodeConfig().getWidth(), mScreenRecorderService.getVideoEncodeConfig().getHeight());
            String[] split = mScreenRecorderService.getSavingFilePath().split("\\.");
            createStreamPublisherParam.outputFilePath = mScreenRecorderService.getSavingFilePath().replace(split[split.length - 1], "flv");
            createStreamPublisherParam.outputUrl = "rtmp://" + DLNAManager.getLocalIpStr(mContext) + NginxHelper.getRtmpLiveServerConfig() + "mirror";
            mStreamMuxer.open(createStreamPublisherParam);
        }

        @Override
        public void onStartRecord() {
            mContext.registerReceiver(mStopActionReceiver, new IntentFilter(Notifications.ACTION_STOP));
            String mediaPath = mStreamMuxer.toString();
            MediaInfo mediaInfo = new MediaInfo();
            mediaInfo.setMediaId(Base64.encodeToString(mediaPath.getBytes(), 2));
            mediaInfo.setMediaType(2);
            mediaInfo.setUri(mediaPath);
            setDataSource(mediaInfo);
            DLNAPlayer dLNAPlayer = DLNAPlayer.this;
            dLNAPlayer.start(dLNAPlayer.mMirrorControlCallback);
            if (mNotifications == null) {
                mNotifications = new Notifications(mContext);
            }
            mNotifications.recording(0L);
        }

        @Override
        public void onRecording(long j) {
            if (mNotificationStartTime <= 0) {
                mNotificationStartTime = j;
            }
            mNotifications.recording((j - mNotificationStartTime) / 1000);
        }

        @Override
        public void onStopRecord(Throwable th) {
            mNotificationStartTime = 0L;
            mNotifications.clear();
        }

        @Override
        public void onDestroyRecord() {
            mNotifications = null;
        }

        @Override
        public void onMuxAudio(byte[] bArr, int i, int i2, MediaCodec.BufferInfo bufferInfo) {
            if (mStreamMuxer != null) {
                mStreamMuxer.writeAudio(bArr, i, i2, bufferInfo);
            }
        }

        @Override
        public void onMuxVideo(byte[] bArr, int i, int i2, MediaCodec.BufferInfo bufferInfo) {
            if (mStreamMuxer != null) {
                mStreamMuxer.writeVideo(bArr, i, i2, bufferInfo);
            }
        }
    };
    public DLNAPlayer(Context context) {
        this.mContext = context;
        initConnection();
    }

    public void setConnectListener(DLNADeviceConnectListener dLNADeviceConnectListener) {
        this.connectListener = dLNADeviceConnectListener;
    }    private BroadcastReceiver mStopActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Notifications.ACTION_STOP.equals(intent.getAction())) {
                stopMirror();
            }
        }
    };

    private void initConnection() {
        this.mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mUpnpService = (AndroidUpnpService) iBinder;
                currentState = 0;
                if (mDeviceInfo != null) {
                    mDeviceInfo.setState(0);
                    mDeviceInfo.setConnected(true);
                }
                if (connectListener != null) {
                    connectListener.onConnect(mDeviceInfo, 100000);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                currentState = 6;
                if (mDeviceInfo != null) {
                    mDeviceInfo.setState(6);
                    mDeviceInfo.setConnected(false);
                }
                if (connectListener != null) {
                    connectListener.onDisconnect(mDeviceInfo, 1, DLNADeviceConnectListener.CONNECT_INFO_DISCONNECT_SUCCESS);
                }
                mUpnpService = null;
                connectListener = null;
                mDeviceInfo = null;
                mDevice = null;
                mMediaInfo = null;
                AV_TRANSPORT_SERVICE = null;
                mServiceConnection = null;
                mContext = null;
            }
        };
    }

    public void connect(DeviceInfo deviceInfo) {
        checkConfig();
        this.mDeviceInfo = deviceInfo;
        this.mDevice = deviceInfo.getDevice();
        if (this.mUpnpService != null) {
            this.currentState = 0;
            DLNADeviceConnectListener dLNADeviceConnectListener = this.connectListener;
            if (dLNADeviceConnectListener == null) {
                return;
            }
            dLNADeviceConnectListener.onConnect(this.mDeviceInfo, 100000);
            return;
        }
        this.mContext.bindService(new Intent(this.mContext, DLNABrowserService.class), this.mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void disconnect() {
        ServiceConnection serviceConnection;
        checkConfig();
        try {
            if (this.mUpnpService != null && (serviceConnection = this.mServiceConnection) != null) {
                this.mContext.unbindService(serviceConnection);
            }
            this.currentState = 6;
            DLNADeviceConnectListener dLNADeviceConnectListener = this.connectListener;
            if (dLNADeviceConnectListener == null) {
                return;
            }
            dLNADeviceConnectListener.onDisconnect(this.mDeviceInfo, 1, DLNADeviceConnectListener.CONNECT_INFO_DISCONNECT_SUCCESS);
        } catch (Exception e) {
            DLNAManager.logE("DLNAPlayer disconnect UPnpService error.", e);
        }
    }

    private void checkPrepared() {
        if (this.mUpnpService != null) {
            return;
        }
        throw new IllegalStateException("Invalid AndroidUPnpService");
    }

    private void checkConfig() {
        if (this.mContext != null) {
            return;
        }
        throw new IllegalStateException("Invalid context");
    }

    public void execute(ActionCallback actionCallback) {
        checkPrepared();
        this.mUpnpService.getControlPoint().execute(actionCallback);
    }

    public void execute(SubscriptionCallback subscriptionCallback) {
        checkPrepared();
        this.mUpnpService.getControlPoint().execute(subscriptionCallback);
    }

    public void play(final DLNAControlCallback dLNAControlCallback) {
        Service findService = this.mDevice.findService(this.AV_TRANSPORT_SERVICE);
        if (checkErrorBeforeExecute(1, findService, dLNAControlCallback)) {
            return;
        }
        execute(new Play(findService) {
            @Override
            public void success(ActionInvocation actionInvocation) {
                super.success(actionInvocation);
                currentState = 1;
                dLNAControlCallback.onSuccess(actionInvocation);
                mDeviceInfo.setState(1);
            }

            @Override
            public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String str) {
                DLNAManager.logE("play error:" + str);
                currentState = 5;
                dLNAControlCallback.onFailure(actionInvocation, 4, str);
                mDeviceInfo.setState(5);
            }
        });
    }

    public void setDataSource(MediaInfo mediaInfo) {
        this.mMediaInfo = mediaInfo;
        mediaInfo.setUri(DLNAManager.tryTransformLocalMediaAddressToLocalHttpServerAddress(this.mContext, mediaInfo.getUri()));
    }

    public void start(final DLNAControlCallback dLNAControlCallback) {
        if (this.mMediaInfo.getMediaType() == 4) {
            startMirror(dLNAControlCallback);
            return;
        }
        this.mDeviceInfo.setMediaID(this.mMediaInfo.getMediaId());
        String pushMediaToRender = pushMediaToRender(this.mMediaInfo);
        Service findService = this.mDevice.findService(this.AV_TRANSPORT_SERVICE);
        if (findService == null) {
            dLNAControlCallback.onFailure(null, 5, null);
        } else {
            execute(new SetAVTransportURI(findService, this.mMediaInfo.getUri(), pushMediaToRender) {
                @Override
                public void success(ActionInvocation actionInvocation) {
                    super.success(actionInvocation);
                    play(dLNAControlCallback);
                }

                @Override
                public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String str) {
                    DLNAManager.logE("play error:" + str);
                    currentState = 5;
                    mDeviceInfo.setState(5);
                    dLNAControlCallback.onFailure(actionInvocation, 4, str);
                }
            });
        }
    }

    private String pushMediaToRender(MediaInfo mediaInfo) {
        return pushMediaToRender(mediaInfo.getUri(), mediaInfo.getMediaId(), mediaInfo.getMediaName(), mediaInfo.getMediaType());
    }

    private String pushMediaToRender(String str, String str2, String str3, int i) {
        String createItemMetadata;
        Res res = new Res(new MimeType("*", "*"), (Long) 0L, str);
        if (i == 1) {
            createItemMetadata = createItemMetadata(new ImageItem(str2, "0", str3, "unknow", res));
        } else if (i == 2) {
            createItemMetadata = createItemMetadata(new VideoItem(str2, "0", str3, "unknow", res));
        } else if (i == 3) {
            createItemMetadata = createItemMetadata(new AudioItem(str2, "0", str3, "unknow", res));
        } else {
            throw new IllegalArgumentException("UNKNOWN MEDIA TYPE");
        }
        DLNAManager.logE("metadata: " + createItemMetadata);
        return createItemMetadata;
    }

    private String createItemMetadata(DIDLObject dIDLObject) {
        StringBuilder sb = new StringBuilder();
        sb.append(DIDL_LITE_HEADER);
        Object[] objArr = new Object[3];
        objArr[0] = dIDLObject.getId();
        objArr[1] = dIDLObject.getParentID();
        objArr[2] = dIDLObject.isRestricted() ? "1" : "0";
        sb.append(String.format("<item id=\"%s\" parentID=\"%s\" restricted=\"%s\">", objArr));
        sb.append(String.format("<dc:title>%s</dc:title>", dIDLObject.getTitle()));
        String creator = dIDLObject.getCreator();
        if (creator != null) {
            creator = creator.replaceAll("<", "_").replaceAll(">", "_");
        }
        sb.append(String.format("<upnp:artist>%s</upnp:artist>", creator));
        sb.append(String.format("<upnp:class>%s</upnp:class>", dIDLObject.getClazz().getValue()));
        sb.append(String.format("<dc:date>%s</dc:date>", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date())));
        Res firstResource = dIDLObject.getFirstResource();
        if (firstResource != null) {
            ProtocolInfo protocolInfo = firstResource.getProtocolInfo();
            String str = "";
            String format = protocolInfo != null ? String.format("protocolInfo=\"%s:%s:%s:%s\"", protocolInfo.getProtocol(), protocolInfo.getNetwork(), (Object) protocolInfo.getContentFormatMimeType(), protocolInfo.getAdditionalInfo()) : str;
            DLNAManager.logE("protocolinfo: " + format);
            String format2 = (firstResource.getResolution() == null || firstResource.getResolution().length() <= 0) ? str : String.format("resolution=\"%s\"", firstResource.getResolution());
            if (firstResource.getDuration() != null && firstResource.getDuration().length() > 0) {
                str = String.format("duration=\"%s\"", firstResource.getDuration());
            }
            sb.append(String.format("<res %s %s %s>", format, format2, str));
            sb.append(firstResource.getValue());
            sb.append("</res>");
        }
        sb.append("</item>");
        sb.append(DIDL_LITE_FOOTER);
        return sb.toString();
    }

    private boolean checkErrorBeforeExecute(int i, Service service, DLNAControlCallback dLNAControlCallback) {
        if (this.currentState == i) {
            dLNAControlCallback.onSuccess(null);
            return true;
        }
        return checkErrorBeforeExecute(service, dLNAControlCallback);
    }

    private boolean checkErrorBeforeExecute(Service service, DLNAControlCallback dLNAControlCallback) {
        if (this.currentState == -1) {
            dLNAControlCallback.onFailure(null, 6, null);
            return true;
        } else if (service != null) {
            return false;
        } else {
            dLNAControlCallback.onFailure(null, 5, null);
            return true;
        }
    }

    public void destroy() {
        ServiceConnection serviceConnection;
        checkConfig();
        stopMirror();
        try {
            if (this.mScreenRecorderService != null && (serviceConnection = this.mScreenRecorderServiceConnection) != null) {
                this.mContext.unbindService(serviceConnection);
            }
        } catch (Exception e) {
            DLNAManager.logE("DLNAPlayer disconnect RecorderService error.", e);
        }
        disconnect();
    }

    public void prepareMediaProjection() {
        IScreenRecorderService iScreenRecorderService = this.mScreenRecorderService;
        if (iScreenRecorderService != null) {
            iScreenRecorderService.registerRecorderCallback(this.mRecorderCallback);
            if (this.mScreenRecorderService.hasPrepared()) {
                this.mScreenRecorderService.startRecorder();
                return;
            }
            RequestMediaProjectionActivity.resultCallback = this.mRequestMediaProjectionResultCallback;
            RequestMediaProjectionActivity.start(this.mContext);
        }
    }

    private void startMirror(DLNAControlCallback dLNAControlCallback) {
        checkConfig();
        this.mMirrorControlCallback = dLNAControlCallback;
    }

    public void stopMirror() {
        this.mMirrorControlCallback = null;
        try {
            this.mContext.unregisterReceiver(this.mStopActionReceiver);
        } catch (Exception unused) {
        }
        IScreenRecorderService iScreenRecorderService = this.mScreenRecorderService;
        if (iScreenRecorderService != null) {
            iScreenRecorderService.stopRecorder();
        }
        IMuxer iMuxer = this.mStreamMuxer;
        if (iMuxer != null) {
            iMuxer.close();
            this.mStreamMuxer = null;
        }
    }






}
