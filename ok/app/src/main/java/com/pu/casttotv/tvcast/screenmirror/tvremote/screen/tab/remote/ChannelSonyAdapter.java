package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.remote;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.sony.TVApp;
import java.util.ArrayList;

/* loaded from: classes4.dex */
public class ChannelSonyAdapter extends RecyclerView.Adapter<ChannelSonyAdapter.ViewHolder> {
    private Context context;
    private OnClickChannelListener onClickChannelListener;
    private ArrayList<TVApp> tvApps;

    /* loaded from: classes4.dex */
    public interface OnClickChannelListener {
        void onItemClick(TVApp tVApp);
    }

    public ChannelSonyAdapter(Context context, ArrayList<TVApp> arrayList) {
        this.context = context;
        this.tvApps = arrayList;
    }

    public void setListener(OnClickChannelListener onClickChannelListener) {
        this.onClickChannelListener = onClickChannelListener;
    }

    public void setData(ArrayList<TVApp> arrayList) {
        this.tvApps.clear();
        this.tvApps.addAll(arrayList);
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this, LayoutInflater.from(this.context).inflate(R.layout.item_channel_sony, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final TVApp tVApp = this.tvApps.get(i);
        viewHolder.tv_name.setText(tVApp.getName());
        StringBuilder sb = new StringBuilder();
        sb.append("onBindViewHolder: ");
        sb.append(tVApp.getIcon());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.ChannelSonyAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ChannelSonyAdapter.this.onClickChannelListener != null) {
                    ChannelSonyAdapter.this.onClickChannelListener.onItemClick(tVApp);
                }
            }
        });
        Glide.with(this.context).load(tVApp.getIcon()).placeholder(R.drawable.imv_default).listener(new RequestListener<Drawable>() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.ChannelSonyAdapter.1
            @Override // com.bumptech.glide.request.RequestListener
            public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                return false;
            }

            @Override // com.bumptech.glide.request.RequestListener
            public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                return false;
            }
        }).into(viewHolder.icon_layout);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.tvApps.size();
    }

    /* loaded from: classes4.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon_layout;
        private TextView tv_name;

        public ViewHolder(ChannelSonyAdapter channelSonyAdapter, View view) {
            super(view);
            this.icon_layout = (ImageView) view.findViewById(R.id.icon_layout);
            ImageView imageView = (ImageView) view.findViewById(R.id.icon_color);
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
