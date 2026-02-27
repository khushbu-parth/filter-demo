package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.youtube_browser;

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

import java.util.ArrayList;

/* loaded from: classes4.dex */
public class YoutubeListAdapter extends RecyclerView.Adapter<YoutubeListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<YoutubeDto> dtoArrayList;
    private OnItemClickListener onItemClickListener;

    /* loaded from: classes4.dex */
    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    public YoutubeListAdapter(Context context, ArrayList<YoutubeDto> arrayList, OnItemClickListener onItemClickListener) {
        this.dtoArrayList = new ArrayList<>();
        this.context = context;
        this.dtoArrayList = arrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(ArrayList<YoutubeDto> arrayList) {
        this.dtoArrayList.clear();
        this.dtoArrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this, LayoutInflater.from(this.context).inflate(R.layout.item_youtube, viewGroup, false));
    }

    @SuppressLint("ResourceType")
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Glide.with(this.context).load(this.dtoArrayList.get(i).getThumbnail()).placeholder(2131231059).centerCrop().into(viewHolder.imv_itemYoutubeThumbnail);
        viewHolder.tv_itemYoutubeFormat.setText(this.dtoArrayList.get(i).getFormat());
        viewHolder.tv_itemYoutubeQuality.setText(this.dtoArrayList.get(i).getQuality());
        viewHolder.tv_itemYoutubeName.setText(this.dtoArrayList.get(i).getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.thntech.cast68.screen.tab.youtube_browser.YoutubeListAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                YoutubeListAdapter.this.onItemClickListener.onItemClick(i);
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.dtoArrayList.size();
    }

    /* loaded from: classes4.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imv_itemYoutubeThumbnail;
        private TextView tv_itemYoutubeFormat;
        private TextView tv_itemYoutubeName;
        private TextView tv_itemYoutubeQuality;

        public ViewHolder(YoutubeListAdapter youtubeListAdapter, View view) {
            super(view);
            this.imv_itemYoutubeThumbnail = (ImageView) view.findViewById(R.id.imv_itemYoutubeThumbnail);
            this.tv_itemYoutubeQuality = (TextView) view.findViewById(R.id.tv_itemYoutubeQuality);
            this.tv_itemYoutubeName = (TextView) view.findViewById(R.id.tv_itemYoutubeName);
            this.tv_itemYoutubeFormat = (TextView) view.findViewById(R.id.tv_itemYoutubeFormat);
        }
    }
}
