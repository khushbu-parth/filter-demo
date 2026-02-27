package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks;

import android.content.Context;
import com.jaku.core.JakuRequest;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.model.Device;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.DBUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.PreferenceUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.RokuRequestTypes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Callable;

public class RxRequestTask implements Callable {
    private Context context;
    private JakuRequest request;
    private RokuRequestTypes rokuRequestType;

    public RxRequestTask(Context context2, JakuRequest jakuRequest, RokuRequestTypes rokuRequestTypes) {
        this.context = context2;
        this.request = jakuRequest;
        this.rokuRequestType = rokuRequestTypes;
    }

    public class Result {
        Object mResultValue;

        public Result(RxRequestTask rxRequestTask, Object obj) {
            this.mResultValue = obj;
        }

        public Result(RxRequestTask rxRequestTask, Exception exc) {
        }
    }

    @Override // java.util.concurrent.Callable
    public Result call() {
        try {
            if (this.rokuRequestType.equals(RokuRequestTypes.query_active_app)) {
                return new Result(this, (List) this.request.send().getResponseData());
            }
            if (this.rokuRequestType.equals(RokuRequestTypes.query_device_info)) {
                return new Result(this, (Device) this.request.send().getResponseData());
            }
            if (this.rokuRequestType.equals(RokuRequestTypes.query_icon)) {
                return new Result(this, ((ByteArrayOutputStream) this.request.send().getResponseData()).toByteArray());
            }
            return new Result(this, this.request.send().getResponseData());
        } catch (IOException e2) {
            if (e2 instanceof UnknownHostException) {
                handleUnknownHostException();
            }
            e2.printStackTrace();
            return new Result(this, (Exception) e2);
        }
    }

    private void handleUnknownHostException() {
        List<Device> call = new AvailableDevicesTask(this.context, false).call();
        try {
            Device connectedDevice = PreferenceUtils.getConnectedDevice(this.context);
            for (Device device : call) {
                if (device.getSerialNumber().equals(connectedDevice.getSerialNumber())) {
                    DBUtils.updateDevice(this.context, device);
                    return;
                }
            }
        } catch (Exception unused) {
        }
    }
}
