package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.playcast;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MediaModel;
import java.io.File;
import java.util.List;

/* loaded from: classes4.dex */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ChannelHolder> {
    private Context mContext;
    private IItemClick mListener;
    private List<MediaModel> objectList;

    /* loaded from: classes4.dex */
    public interface IItemClick {
        void clickItem(int i);
    }

    public ImageAdapter(Context context, List<MediaModel> list) {
        this.mContext = context;
        this.objectList = list;
    }

    public void setOnclick(IItemClick iItemClick) {
        this.mListener = iItemClick;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.objectList.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ChannelHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ChannelHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_image, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ChannelHolder channelHolder, int i) {
        channelHolder.binData(this.objectList.get(i), i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public class ChannelHolder extends RecyclerView.ViewHolder {
        RoundedImageView imv;

        public ChannelHolder(View view) {
            super(view);
            this.imv = (RoundedImageView) view.findViewById(R.id.imv);
        }

        public void binData(MediaModel mediaModel, final int i) {
            this.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.ImageAdapter.ChannelHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    ImageAdapter.this.mListener.clickItem(i);
                }
            });
            if (((MediaModel) ImageAdapter.this.objectList.get(i)).isSelected) {
                this.imv.setBorderColor(ImageAdapter.this.mContext.getResources().getColor(R.color.border_image));
            } else {
                this.imv.setBorderColor(ImageAdapter.this.mContext.getResources().getColor(R.color.color_transparent));
            }
            Glide.with(ImageAdapter.this.mContext).load(Uri.fromFile(new File(mediaModel.getPhotoUri()))).into(this.imv);
        }
    }

    public void setSelectedPosition(int i) {
        for (MediaModel mediaModel : this.objectList) {
            mediaModel.isSelected = false;
        }
        this.objectList.get(i).isSelected = true;
        notifyDataSetChanged();
    }

    public void clearItems() {
        this.objectList.clear();
    }

    public void addItems(List<MediaModel> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        this.objectList.clear();
        this.objectList.addAll(list);
        if (list.size() > 0) {
            list.get(0).isSelected = true;
        }
        notifyDataSetChanged();
    }
}
