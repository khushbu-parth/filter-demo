package com.cast.tv.screen.mirroring.screencasting.TvAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.cast.tv.screen.mirroring.screencasting.Model.SupportedDeviceModel;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class SupportedDeviceAdapter extends BaseQuickAdapter<SupportedDeviceModel, BaseViewHolder> {
    public SupportedDeviceAdapter(List<SupportedDeviceModel> list) {
        super(R.layout.item_supported_device, list);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, SupportedDeviceModel supportedDeviceModel) {
        ((ImageView) baseViewHolder.getView(R.id.image_cover)).setImageResource(supportedDeviceModel.getCoverResId());
        baseViewHolder.setText(R.id.text_name, supportedDeviceModel.getDeviceName());
        baseViewHolder.setText(R.id.text_desc, supportedDeviceModel.getDescribe());
        List<String> supportWays = supportedDeviceModel.getSupportWays();
        baseViewHolder.setText(R.id.text_type1, supportWays.get(0));
        TextView textView = (TextView) baseViewHolder.getView(R.id.text_type2);
        if (supportWays.size() >= 2) {
            textView.setVisibility(0);
            textView.setText(supportWays.get(1));
            return;
        }
        textView.setVisibility(8);
    }
}
