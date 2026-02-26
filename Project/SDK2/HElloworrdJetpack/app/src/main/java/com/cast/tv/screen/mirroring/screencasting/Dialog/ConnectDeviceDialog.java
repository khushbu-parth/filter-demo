package com.cast.tv.screen.mirroring.screencasting.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cast.tv.screen.mirroring.screencasting.Base.BaseDialogFragment;
import com.cast.tv.screen.mirroring.screencasting.Callback.DLNADeviceChangeCallback;
import com.cast.tv.screen.mirroring.screencasting.Contract.IntentContracts;
import com.cast.tv.screen.mirroring.screencasting.Helper.DLNAHelper;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.Observer.ConnectStatus;
import com.cast.tv.screen.mirroring.screencasting.Observer.RewardDialogEvent;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.ConnectDeviceAdapter;
import com.cast.tv.screen.mirroring.screencasting.UI.cast.AudioVideoCastActivity;
import com.cast.tv.screen.mirroring.screencasting.UI.cast.PhotoCastActivity;
import com.cast.tv.screen.mirroring.screencasting.UI.help.ConnectHelpActivity;
import com.cast.tv.screen.mirroring.screencasting.UI.main.MainActivity;
import com.cast.tv.screen.mirroring.screencasting.Utils.DeviceInfoUtils;
import com.cast.tv.screen.mirroring.screencasting.Utils.L;
import com.cast.tv.screen.mirroring.screencasting.Utils.MaxRewardUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.ScreenUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.T;
import com.cast.tv.screen.mirroring.screencasting.Utils.net.NetUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.net.NetworkType;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lib.screening.bean.DeviceInfo;

import java.util.ArrayList;
import java.util.List;

public class ConnectDeviceDialog extends BaseDialogFragment {
    private ConnectDeviceAdapter mAdapter;
    private FileModel mFileModel;
    private ImageView mImageRefresh;
    private int mViewType;

    public static void safedk_ConnectDeviceDialog_startActivity_fc04625b4f6b4f8d65393063d8a4eb00(ConnectDeviceDialog p0, Intent p1) {
        if (p1 == null) {
            return;
        }
        p0.startActivity(p1);
    }

    public static ConnectDeviceDialog newInstance(FileModel fileModel) {
        ConnectDeviceDialog connectDeviceDialog = new ConnectDeviceDialog();
        if (fileModel != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentContracts.INTENT_CAST_SCREEN_FILE, fileModel);
            connectDeviceDialog.setArguments(bundle);
        }
        return connectDeviceDialog;
    }

    @Override
    protected int setDialogGravity() {
        return 17;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_connect_device;
    }

    @Override
    protected int setDialogWidth() {
        return ScreenUtil.getScreenWidth(this.mContext) - ScreenUtil.dip2px(this.mContext, 32.0f);
    }

    @Override
    protected int setDialogHeight() {
        return ScreenUtil.getScreenHeight(this.mContext) - ScreenUtil.dip2px(this.mContext, 170.0f);
    }

    @Override
    protected void initView(View view) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (activity instanceof MainActivity) {
                this.mViewType = 1;
            } else {
                this.mViewType = 2;
            }
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mFileModel = (FileModel) arguments.getSerializable(IntentContracts.INTENT_CAST_SCREEN_FILE);
        }
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(false);
        }
        TextView textView = (TextView) view.findViewById(R.id.text_connect_ssid);
        textView.setText(NetUtil.getConnectWifiSsid());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                ConnectDeviceDialog.this.initView$0$ConnectDeviceDialog(view2);
            }
        });
        view.findViewById(R.id.image_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                ConnectDeviceDialog.this.initView$1$ConnectDeviceDialog(view2);
            }
        });
        ImageView imageView = (ImageView) view.findViewById(R.id.image_refresh);
        this.mImageRefresh = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                ConnectDeviceDialog.this.initView$2$ConnectDeviceDialog(view2);
            }
        });
        view.findViewById(R.id.text_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view2) {
                ConnectDeviceDialog.this.initView$4$ConnectDeviceDialog(view2);
            }
        });
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        ConnectDeviceAdapter connectDeviceAdapter = new ConnectDeviceAdapter(new ArrayList());
        this.mAdapter = connectDeviceAdapter;
        recyclerView.setAdapter(connectDeviceAdapter);
        this.mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public final void onItemClick(BaseQuickAdapter baseQuickAdapter, View view2, int i) {
                ConnectDeviceDialog.this.initView$5$ConnectDeviceDialog(baseQuickAdapter, view2, i);
            }
        });
        DLNAHelper.mConnectStatus.observe(this, new Observer() {
            @Override
            public final void onChanged(Object obj) {
                ConnectDeviceDialog.this.initView$6$ConnectDeviceDialog((ConnectStatus) obj);
            }
        });
        setNetworkChange(textView);
    }

    public void initView$0$ConnectDeviceDialog(View view) {
        safedk_ConnectDeviceDialog_startActivity_fc04625b4f6b4f8d65393063d8a4eb00(this, new Intent("android.settings.WIFI_SETTINGS"));
        dismiss();
    }

    public void initView$1$ConnectDeviceDialog(View view) {
        startToConnectHelp();
    }

    public void initView$2$ConnectDeviceDialog(View view) {
        startRotateAnim();
    }

    public void initView$3$ConnectDeviceDialog(View view) {
//        ShareUtil.sendEmailFeedback(this.mContext, "");
    }

    public void initView$4$ConnectDeviceDialog(View view) {
        dismiss();
    }

    public void initView$5$ConnectDeviceDialog(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        List<DeviceInfo> data = this.mAdapter.getData();
        for (DeviceInfo deviceInfo : data) {
            deviceInfo.setSelect(false);
        }
        DeviceInfo item = this.mAdapter.getItem(i);
        item.setSelect(true);
        ConnectingDialog.newInstance(item).show(getChildFragmentManager(), "Connecting");
        this.mAdapter.setList(data);
    }

    public void initView$6$ConnectDeviceDialog(ConnectStatus connectStatus) {
        if (connectStatus == ConnectStatus.CONNECTED) {
            if (this.mFileModel == null) {
                T.showShort(this.mContext, "Device is connected.");
            } else if (MaxRewardUtil.obtainCastFileNum() <= 0) {
                RewardDialogEvent.post(this.mViewType);
                dismiss();
                return;
            } else if (this.mFileModel.getFileType() == 272) {
               startActivity(new Intent(this.mContext, PhotoCastActivity.class));
            } else {
               startActivity(
                       new Intent(this.mContext, AudioVideoCastActivity.class));
            }
            dismiss();
        }
    }

    private void setNetworkChange(final TextView textView) {
        NetUtil.mNetworkType.observe(this, new Observer() {
            @Override
            public final void onChanged(Object obj) {
                ConnectDeviceDialog.this.setNetworkChange$8$ConnectDeviceDialog(textView, (NetworkType) obj);
            }
        });
    }

    public void setNetworkChange$8$ConnectDeviceDialog(TextView textView, NetworkType networkType) {
        if (networkType == NetworkType.NETWORK_WIFI) {
            textView.setText(NetUtil.getConnectWifiSsid());
        } else {
            textView.setText("WiFi not connected");
        }
        try {
            L.i("XXX", "DeviceModel: " + DeviceInfoUtils.getDeviceModel());
            L.i("XXX", "DeviceBrand: " + DeviceInfoUtils.getDeviceBrand());
            DLNAHelper.startBrowser(new DLNADeviceChangeCallback() {
                @Override
                public final void onDeviceChange(List list) {
                    ConnectDeviceDialog.this.null$7$ConnectDeviceDialog(list);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void null$7$ConnectDeviceDialog(List list) {
        this.mAdapter.setList(list);
    }

    private void startRotateAnim() {
        DLNAHelper.refresh();
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(1500L);
        rotateAnimation.setRepeatCount(1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        this.mImageRefresh.startAnimation(rotateAnimation);
    }

    private void startToConnectHelp() {
        safedk_ConnectDeviceDialog_startActivity_fc04625b4f6b4f8d65393063d8a4eb00(this, new Intent(this.mContext, ConnectHelpActivity.class));
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        DLNAHelper.stopBrowser();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ImageView imageView = this.mImageRefresh;
        if (imageView != null) {
            imageView.clearAnimation();
        }
        DLNAHelper.mConnectStatus.removeObservers(this);
        NetUtil.mNetworkType.removeObservers(this);
    }
}
