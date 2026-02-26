package com.cast.tv.screen.mirroring.screencasting.TvAdapter;

import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class PhotoAdapter extends BaseQuickAdapter<FileModel, BaseViewHolder> {
    public PhotoAdapter(List<FileModel> list) {
        super(R.layout.item_photo, list);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, FileModel fileModel) {
        baseViewHolder.setText(R.id.text_name, fileModel.getDisplayName());
        baseViewHolder.setText(R.id.text_count, fileModel.getChildCount() + " photos");
    }
}
