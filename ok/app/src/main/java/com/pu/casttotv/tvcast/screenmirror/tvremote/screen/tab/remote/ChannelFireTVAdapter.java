package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.remote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.firetv.ChannelFireTVDto;
import java.util.List;

/* loaded from: classes4.dex */
public class ChannelFireTVAdapter extends RecyclerView.Adapter<ChannelFireTVAdapter.ChannelHolder> {
    private List<ChannelFireTVDto> mChannels;
    private final Context mContext;
    private IItemClick mOnClickItemRecycleview;

    /* loaded from: classes4.dex */
    public interface IItemClick {
        void clickItem(int i);
    }

    public ChannelFireTVAdapter(Context context, List<ChannelFireTVDto> list, IItemClick iItemClick) {
        this.mContext = context;
        this.mChannels = list;
        this.mOnClickItemRecycleview = iItemClick;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mChannels.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ChannelHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ChannelHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_channel_fire_tv, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ChannelHolder channelHolder, int i) {
        channelHolder.mImageView.setImageResource(this.mChannels.get(i).getImg());
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
            view.setOnClickListener(new View.OnClickListener() {
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    ChannelFireTVAdapter.this.mOnClickItemRecycleview.clickItem(ChannelHolder.this.getLayoutPosition());
                }
            });
        }
    }
}
