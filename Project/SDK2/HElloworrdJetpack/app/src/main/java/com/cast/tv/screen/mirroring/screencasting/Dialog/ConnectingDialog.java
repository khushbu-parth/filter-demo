package com.cast.tv.screen.mirroring.screencasting.Dialog;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseDialogFragment;
import com.cast.tv.screen.mirroring.screencasting.Contract.IntentContracts;
import com.cast.tv.screen.mirroring.screencasting.Helper.DLNAHelper;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.Utils.ScreenUtil;
import com.lib.screening.bean.DeviceInfo;

public class ConnectingDialog extends BaseDialogFragment {
    public static ConnectingDialog newInstance(DeviceInfo deviceInfo) {
        ConnectingDialog connectingDialog = new ConnectingDialog();
        if (deviceInfo != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentContracts.INTENT_CONNECT_DEVICE, deviceInfo);
            connectingDialog.setArguments(bundle);
        }
        return connectingDialog;
    }

    @Override
    protected int setDialogGravity() {
        return 17;
    }

    @Override
    protected int setDialogHeight() {
        return 0;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_connecting;
    }

    @Override
    protected int setDialogWidth() {
        return ScreenUtil.getScreenWidth(this.mContext) - ScreenUtil.dip2px(this.mContext, 64.0f);
    }

    @Override
    protected void initView(View view) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            final DeviceInfo deviceInfo = (DeviceInfo) arguments.getSerializable(IntentContracts.INTENT_CONNECT_DEVICE);
            if (deviceInfo == null) {
                dismiss();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public final void run() {
                        DLNAHelper.connectDevice(deviceInfo);
                    }
                }, 1500L);
            }
        }
    }
}
