package com.cast.tv.screen.mirroring.screencasting.TvAdapter;

import android.view.View;

import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.Utils.ListUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class AudioAdapter extends BaseQuickAdapter<FileModel, BaseViewHolder> {
    public AudioAdapter(List<FileModel> list) {
        super(R.layout.item_audio, list);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, FileModel fileModel) {
        baseViewHolder.setText(R.id.text_name, fileModel.getDisplayName());
        baseViewHolder.setText(R.id.text_count, String.valueOf(fileModel.getChildCount()));
        View view = baseViewHolder.getView(R.id.view_line);
        if (baseViewHolder.getAdapterPosition() == ListUtil.getSize(getData()) - 1) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }
}
