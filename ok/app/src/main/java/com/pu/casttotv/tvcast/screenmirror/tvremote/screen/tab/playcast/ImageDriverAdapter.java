package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.playcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.drive.GoogleDriveItem;
import java.util.List;

/* loaded from: classes4.dex */
public class ImageDriverAdapter extends RecyclerView.Adapter<ImageDriverAdapter.ChannelHolder> {
    private Context mContext;
    private IItemClick mListener;
    private List<GoogleDriveItem> objectList;

    /* loaded from: classes4.dex */
    public interface IItemClick {
        void clickItem(int i);
    }

    public ImageDriverAdapter(Context context, List<GoogleDriveItem> list) {
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

        public void binData(GoogleDriveItem googleDriveItem, final int i) {
            this.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.ImageDriverAdapter.ChannelHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    ImageDriverAdapter.this.mListener.clickItem(i);
                }
            });
            if (((GoogleDriveItem) ImageDriverAdapter.this.objectList.get(i)).isSelected) {
                this.imv.setBorderColor(ImageDriverAdapter.this.mContext.getResources().getColor(R.color.border_image));
            } else {
                this.imv.setBorderColor(ImageDriverAdapter.this.mContext.getResources().getColor(R.color.color_transparent));
            }
            Glide.with(ImageDriverAdapter.this.mContext).load(googleDriveItem.getThumbnailLink()).placeholder(R.drawable.ic_image_default).into(this.imv);
        }
    }

    public void setSelectedPosition(int i) {
        for (GoogleDriveItem googleDriveItem : this.objectList) {
            googleDriveItem.isSelected = false;
        }
        this.objectList.get(i).isSelected = true;
        notifyDataSetChanged();
    }

    public void clearItems() {
        this.objectList.clear();
    }

    public void addItems(List<GoogleDriveItem> list) {
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
