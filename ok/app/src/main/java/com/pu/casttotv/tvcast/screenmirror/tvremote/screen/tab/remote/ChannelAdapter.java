package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.remote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.jaku.model.Channel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.CommandHelper;
import java.util.List;

/* loaded from: classes4.dex */
public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelHolder> {
    private List<Channel> mChannels;
    private final Context mContext;
    private IItemClick mOnClickItemRecycleview;

    /* loaded from: classes4.dex */
    public interface IItemClick {
        void clickItem(int i);
    }

    public ChannelAdapter(Context context, List<Channel> list, IItemClick iItemClick) {
        this.mContext = context;
        this.mChannels = list;
        this.mOnClickItemRecycleview = iItemClick;
    }

    public void setData(List<Channel> list) {
        this.mChannels.clear();
        this.mChannels.addAll(list);
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mChannels.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ChannelHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ChannelHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_channel_lg, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ChannelHolder channelHolder, int i) {
        Glide.with(this.mContext).load(CommandHelper.getIconURL(this.mContext, this.mChannels.get(i).getId())).into(channelHolder.mImageView);
    }

    public class ChannelHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView tv_name;

        @SuppressLint("WrongConstant")
        public ChannelHolder(View view) {
            super(view);
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
            ImageView imageView = (ImageView) view.findViewById(R.id.icon_layout);
            this.mImageView = imageView;
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            this.mImageView.setClipToOutline(true);
            this.tv_name.setVisibility(8);
            view.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.ChannelAdapter.ChannelHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    ChannelAdapter.this.mOnClickItemRecycleview.clickItem(ChannelHolder.this.getLayoutPosition());
                }
            });
        }
    }
}
