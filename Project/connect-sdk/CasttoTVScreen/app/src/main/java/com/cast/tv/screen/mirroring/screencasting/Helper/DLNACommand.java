package com.cast.tv.screen.mirroring.screencasting.Helper;

import android.os.Handler;
import android.os.Looper;

import com.cast.tv.screen.mirroring.screencasting.Observer.DeviceVolume;
import com.cast.tv.screen.mirroring.screencasting.Utils.L;
import com.cast.tv.screen.mirroring.screencasting.Utils.TimeUtil;
import com.lib.screening.DLNAPlayer;
import com.lib.screening.bean.DeviceInfo;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.support.avtransport.callback.GetPositionInfo;
import org.fourthline.cling.support.avtransport.callback.GetTransportInfo;
import org.fourthline.cling.support.avtransport.callback.Pause;
import org.fourthline.cling.support.avtransport.callback.Play;
import org.fourthline.cling.support.avtransport.callback.Seek;
import org.fourthline.cling.support.avtransport.callback.Stop;
import org.fourthline.cling.support.model.PositionInfo;
import org.fourthline.cling.support.model.TransportInfo;
import org.fourthline.cling.support.renderingcontrol.callback.GetVolume;
import org.fourthline.cling.support.renderingcontrol.callback.SetVolume;


public class DLNACommand {
    private static DLNACommand mInstance;
    private final DeviceInfo mConnectDeviceInfo;
    private final DLNAPlayer mDLNAPlayer;
    private final String TAG = "DLNACommand";
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private Service mAvtService;
    private Service mUdaService;

    private DLNACommand(DeviceInfo deviceInfo, DLNAPlayer dLNAPlayer) {
        this.mConnectDeviceInfo = deviceInfo;
        this.mDLNAPlayer = dLNAPlayer;
    }

    public static DLNACommand getInstance(DeviceInfo deviceInfo, DLNAPlayer dLNAPlayer) {
        if (mInstance == null) {
            synchronized (DLNACommand.class) {
                if (mInstance == null) {
                    mInstance = new DLNACommand(deviceInfo, dLNAPlayer);
                }
            }
        }
        return mInstance;
    }

    private boolean checkDevice(DeviceInfo deviceInfo) {
        return deviceInfo != null;
    }

    private boolean checkService(Service service) {
        return service != null;
    }

    public void play() {
        if (!checkService(obtainAtvService())) {
            return;
        }
        this.mDLNAPlayer.execute(new Play(this.mAvtService) {
            @Override
            public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String str) {
            }
        });
    }

    public void pause() {
        if (!checkService(obtainAtvService())) {
            return;
        }
        this.mDLNAPlayer.execute(new Pause(this.mAvtService) {
            @Override
            public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String str) {
            }

            @Override
            public void success(ActionInvocation actionInvocation) {
                super.success(actionInvocation);
            }
        });
    }

    public void stop() {
        if (!checkService(obtainAtvService())) {
            return;
        }
        this.mDLNAPlayer.execute(new Stop(this.mAvtService) {
            @Override
            public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String str) {
            }

            @Override
            public void success(ActionInvocation actionInvocation) {
                super.success(actionInvocation);
            }
        });
    }

    public void seekTo(String str) {
        if (!checkService(obtainAtvService())) {
            return;
        }
        this.mDLNAPlayer.execute(new Seek(this.mAvtService, str) {
            @Override
            public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String str2) {
            }
        });
    }

    public void seekFastForward(String str) {
        seekTo(formatTime(str, 15));
    }

    public void seekBackOff(String str) {
        seekTo(formatTime(str, -15));
    }

    public void getTransportInfo() {
        if (!checkService(obtainAtvService())) {
            return;
        }
        this.mDLNAPlayer.execute(new AnonymousClass5(this.mAvtService));
    }

    public void getPositionInfo() {
        if (!checkService(obtainAtvService())) {
            return;
        }
        this.mDLNAPlayer.execute(new AnonymousClass6(this.mAvtService));
    }

    public void getVolume() {
        if (!checkService(obtainUdaService())) {
            return;
        }
        this.mDLNAPlayer.execute(new AnonymousClass7(this.mUdaService));
    }

    public void setVolume(int i) {
        if (!checkService(obtainUdaService())) {
            return;
        }
        this.mDLNAPlayer.execute(new SetVolume(this.mUdaService, i) {
            @Override
            public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String str) {
            }
        });
    }

    private String formatTime(String str, int i) {
        return TimeUtil.int2String(TimeUtil.string2Int(str) + i);
    }

    private Service obtainAtvService() {
        if (!checkDevice(this.mConnectDeviceInfo)) {
            return null;
        }
        if (this.mAvtService == null) {
            this.mAvtService = this.mConnectDeviceInfo.getDevice().findService(new UDAServiceType("AVTransport"));
        }
        return this.mAvtService;
    }

    private Service obtainUdaService() {
        if (!checkDevice(this.mConnectDeviceInfo)) {
            return null;
        }
        if (this.mUdaService == null) {
            this.mUdaService = this.mConnectDeviceInfo.getDevice().findService(new UDAServiceType("RenderingControl"));
        }
        return this.mAvtService;
    }

    public class AnonymousClass5 extends GetTransportInfo {
        AnonymousClass5(Service service) {
            super(service);
        }

        @Override
        public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String str) {
        }

        @Override
        public void received(ActionInvocation actionInvocation, final TransportInfo transportInfo) {
            L.i("XXX", "getTransportInfo: " + transportInfo.getCurrentTransportState().getValue());
            DLNACommand.this.mHandler.post(new Runnable() {
                @Override
                public final void run() {
                    DLNAHelper.mTransportState.setValue(transportInfo.getCurrentTransportState());
                }
            });
        }
    }

    public class AnonymousClass6 extends GetPositionInfo {
        AnonymousClass6(Service service) {
            super(service);
        }

        @Override
        public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String str) {
        }

        @Override
        public void success(ActionInvocation actionInvocation) {
            super.success(actionInvocation);
        }

        @Override
        public void received(ActionInvocation actionInvocation, final PositionInfo positionInfo) {
            L.i("DLNACommand", "PositionInfo1: " + positionInfo.getRelTime());
            L.i("DLNACommand", "PositionInfo2: " + positionInfo.getTrackDuration());
            DLNACommand.this.mHandler.post(new Runnable() {
                @Override
                public final void run() {
                    DLNAHelper.mPlayPosition.setValue(positionInfo);
                }
            });
        }
    }

    public class AnonymousClass7 extends GetVolume {
        AnonymousClass7(Service service) {
            super(service);
        }

        @Override
        public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String str) {
        }

        @Override
        public void received(ActionInvocation actionInvocation, final int i) {
            L.i("DLNACommand", "GetVolume: " + i);
            DLNACommand.this.mHandler.post(new Runnable() {
                @Override
                public final void run() {
                    $received$0(i);
                }
            });
        }

        public void $received$0(int i) {
            DeviceVolume deviceVolume = new DeviceVolume();
            deviceVolume.volume = i;
            DLNAHelper.mDeviceVolume.setValue(deviceVolume);
        }
    }
}
