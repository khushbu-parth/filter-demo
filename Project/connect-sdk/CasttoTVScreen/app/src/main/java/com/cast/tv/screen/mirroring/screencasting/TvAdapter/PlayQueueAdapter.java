package com.cast.tv.screen.mirroring.screencasting.TvAdapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class PlayQueueAdapter extends BaseQuickAdapter<FileModel, BaseViewHolder> {
    private IDeleteItemCallback mDeleteCallback;


    public PlayQueueAdapter(List<FileModel> list) {
        super(R.layout.item_play_queue, list);

    }



    public void setDeleteItemCallback(IDeleteItemCallback iDeleteItemCallback) {
        this.mDeleteCallback = iDeleteItemCallback;
    }

    @Override
    public void convert(final BaseViewHolder baseViewHolder, FileModel fileModel) {
        TextView textView = (TextView) baseViewHolder.getView(R.id.text_name);
        ImageView imageView = (ImageView) baseViewHolder.getView(R.id.image_delete_item);
        textView.setText(fileModel.getDisplayName());
        if (fileModel.isSelect()) {
            textView.setTextColor(getContext().getResources().getColor(R.color.color_663FF5));
        } else {
            textView.setTextColor(getContext().getResources().getColor(R.color.color_222222));
        }
        baseViewHolder.getView(R.id.image_delete_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                IDeleteItemCallback iDeleteItemCallback = mDeleteCallback;
                if (iDeleteItemCallback != null) {
                    iDeleteItemCallback.onDeleteItem(getItemPosition(fileModel));
                }

            }
        });
    }

    public interface IDeleteItemCallback {
        void onDeleteItem(int i);
    }
}
