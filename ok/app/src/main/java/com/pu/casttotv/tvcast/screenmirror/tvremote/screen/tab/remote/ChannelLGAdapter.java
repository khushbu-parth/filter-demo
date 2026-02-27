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
import com.connectsdk.core.AppInfo;
import java.util.ArrayList;
import java.util.Random;

/* loaded from: classes4.dex */
public class ChannelLGAdapter extends RecyclerView.Adapter<ChannelLGAdapter.ViewHolder> {
    private ArrayList<AppInfo> channelInfos;
    private String[] color = {"#D10143", "#2196F3", "#2CB4F1", "#D91BFA", "#B40039", "#FFC107", "#FF9800", "#FF5722", "#2FF837", "#14D5ED", "#87EC13"};
    private Context context;
    private OnClickChannelListener onClickChannelListener;

    /* loaded from: classes4.dex */
    public interface OnClickChannelListener {
        void onItemClick(int i, AppInfo appInfo, ArrayList<AppInfo> arrayList);
    }

    public ChannelLGAdapter(Context context, ArrayList<AppInfo> arrayList) {
        this.context = context;
        this.channelInfos = arrayList;
    }

    public void setListener(OnClickChannelListener onClickChannelListener) {
        this.onClickChannelListener = onClickChannelListener;
    }

    public void setData(ArrayList<AppInfo> arrayList) {
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
        final AppInfo appInfo = this.channelInfos.get(i);
        viewHolder.icon_color.setBackgroundColor(Color.parseColor(this.color[new Random().nextInt(this.color.length)]));
        StringBuilder sb = new StringBuilder();
        sb.append(" :");
        sb.append(appInfo.getName());
        sb.append(" -- ");
        sb.append(appInfo.getId());
        viewHolder.tv_name.setText(appInfo.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.thntech.cast68.screen.tab.remote.ChannelLGAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ChannelLGAdapter.this.onClickChannelListener != null) {
                    ChannelLGAdapter.this.onClickChannelListener.onItemClick(i, appInfo, ChannelLGAdapter.this.channelInfos);
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

        public ViewHolder(ChannelLGAdapter channelLGAdapter, View view) {
            super(view);
            ImageView imageView = (ImageView) view.findViewById(R.id.icon_layout);
            this.icon_color = (ImageView) view.findViewById(R.id.icon_color);
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
