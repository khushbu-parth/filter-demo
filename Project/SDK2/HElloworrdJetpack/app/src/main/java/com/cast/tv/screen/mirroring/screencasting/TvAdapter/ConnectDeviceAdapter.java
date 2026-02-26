package com.cast.tv.screen.mirroring.screencasting.TvAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.cast.tv.screen.mirroring.screencasting.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lib.screening.bean.DeviceInfo;

import java.util.List;

public class ConnectDeviceAdapter extends BaseQuickAdapter<DeviceInfo, BaseViewHolder> {
    public ConnectDeviceAdapter(List<DeviceInfo> list) {
        super(R.layout.item_connect_device, list);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, DeviceInfo deviceInfo) {
        ((TextView) baseViewHolder.getView(R.id.text_name)).setText(deviceInfo.getDevice().getDetails().getFriendlyName());
        ImageView imageView = (ImageView) baseViewHolder.getView(R.id.image_status);
        if (deviceInfo.isSelect()) {
            imageView.setImageResource(R.drawable.check);
        } else {
            imageView.setImageResource(R.drawable.circle_663ff5_bg);
        }
    }
}
