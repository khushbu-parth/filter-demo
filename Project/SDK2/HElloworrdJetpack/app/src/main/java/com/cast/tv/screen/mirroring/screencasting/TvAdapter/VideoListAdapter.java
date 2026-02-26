package com.cast.tv.screen.mirroring.screencasting.TvAdapter;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.Utils.CornerTransform;
import com.cast.tv.screen.mirroring.screencasting.Utils.ScreenUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class VideoListAdapter extends BaseQuickAdapter<FileModel, BaseViewHolder> {
    public VideoListAdapter(List<FileModel> list) {
        super(R.layout.item_video_list, list);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, FileModel fileModel) {
        ImageView imageView = (ImageView) baseViewHolder.getView(R.id.image_cover);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        int screenWidth = (ScreenUtil.getScreenWidth(getContext()) - ScreenUtil.dip2px(getContext(), 50.0f)) / 2;
        layoutParams.width = screenWidth;
        layoutParams.height = screenWidth;
//        imageView.setLayoutParams(layoutParams);
        Glide.with(getContext()).asBitmap().load(fileModel.getMiniKindByte()).transform(new CornerTransform(getContext(), 6.6f)).error((int) R.drawable.video_default_bg).into(imageView);
        ((TextView) baseViewHolder.getView(R.id.text_time)).setText(fileModel.getDurationStr());
    }
}
