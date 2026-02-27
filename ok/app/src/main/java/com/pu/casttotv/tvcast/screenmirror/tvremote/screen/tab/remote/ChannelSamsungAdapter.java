package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.remote;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.Channels;
import java.util.ArrayList;
import java.util.Random;

/* loaded from: classes4.dex */
public class ChannelSamsungAdapter extends RecyclerView.Adapter<ChannelSamsungAdapter.ViewHolder> {
    private ArrayList<Channels> channelInfos;
    private String[] color = {"#D10143", "#2196F3", "#2CB4F1", "#D91BFA", "#B40039", "#FFC107", "#FF9800", "#FF5722", "#2FF837", "#14D5ED", "#87EC13"};
    private Context context;
    private OnClickChannelListener onClickChannelListener;

    /* loaded from: classes4.dex */
    public interface OnClickChannelListener {
        void onItemClick(int i, Channels channels, ArrayList<Channels> arrayList);
    }

    public ChannelSamsungAdapter(Context context, ArrayList<Channels> arrayList) {
        this.context = context;
        this.channelInfos = arrayList;
    }

    public void setListener(OnClickChannelListener onClickChannelListener) {
        this.onClickChannelListener = onClickChannelListener;
    }

    public void setData(ArrayList<Channels> arrayList) {
        this.channelInfos.clear();
        this.channelInfos.addAll(arrayList);
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this, LayoutInflater.from(this.context).inflate(R.layout.item_channel_lg, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final Channels channels = this.channelInfos.get(i);
        viewHolder.icon_color.setBackgroundColor(Color.parseColor(this.color[new Random().nextInt(this.color.length)]));
        StringBuilder sb = new StringBuilder();
        sb.append(" :");
        sb.append(channels.getName());
        sb.append(" -- ");
        sb.append(channels.getAppId());
        viewHolder.tv_name.setText(channels.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.ChannelSamsungAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ChannelSamsungAdapter.this.onClickChannelListener != null) {
                    ChannelSamsungAdapter.this.onClickChannelListener.onItemClick(i, channels, ChannelSamsungAdapter.this.channelInfos);
                }
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.channelInfos.size();
    }

    /* loaded from: classes4.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon_color;
        private TextView tv_name;

        public ViewHolder(ChannelSamsungAdapter channelSamsungAdapter, View view) {
            super(view);
            ImageView imageView = (ImageView) view.findViewById(R.id.icon_layout);
            this.icon_color = (ImageView) view.findViewById(R.id.icon_color);
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
