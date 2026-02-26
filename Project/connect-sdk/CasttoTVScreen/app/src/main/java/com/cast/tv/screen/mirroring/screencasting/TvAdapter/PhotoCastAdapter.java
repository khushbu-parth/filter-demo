package com.cast.tv.screen.mirroring.screencasting.TvAdapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class PhotoCastAdapter extends BaseQuickAdapter<FileModel, BaseViewHolder> {
    public PhotoCastAdapter(List<FileModel> list) {
        super(R.layout.item_photo_cast, list);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, FileModel fileModel) {
        ImageView imageView = (ImageView) baseViewHolder.getView(R.id.image_cover);
        if (fileModel.isSelect()) {
            imageView.setBackgroundResource(R.drawable.cast_photo_select_bg);
        } else {
            imageView.setBackground(null);
        }
        Glide.with(getContext()).load(fileModel.getPath()).into(imageView);
    }
}
