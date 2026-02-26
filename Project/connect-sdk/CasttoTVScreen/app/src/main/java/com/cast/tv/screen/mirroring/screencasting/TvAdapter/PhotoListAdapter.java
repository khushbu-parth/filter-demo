package com.cast.tv.screen.mirroring.screencasting.TvAdapter;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.Utils.ScreenUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class PhotoListAdapter extends BaseQuickAdapter<FileModel, BaseViewHolder> {
    public PhotoListAdapter(List<FileModel> list) {
        super(R.layout.item_photo_list, list);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, FileModel fileModel) {
        ImageView imageView = (ImageView) baseViewHolder.getView(R.id.image);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.width = (ScreenUtil.getScreenWidth(getContext()) - ScreenUtil.dip2px(getContext(), 30.0f)) / 3;
        layoutParams.height = layoutParams.width;
        imageView.setLayoutParams(layoutParams);
        Glide.with(getContext()).load(fileModel.getPath()).placeholder((int) R.color.color_BFBFBF).error((int) R.color.color_BFBFBF).into(imageView);
    }
}
