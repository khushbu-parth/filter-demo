package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.audio;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.AudioModel;
import java.util.List;

/* loaded from: classes4.dex */
public class AudioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<AudioModel> listMedia;
    private OnItemClickPhoto mListener;

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;

    /* loaded from: classes4.dex */
    public interface OnItemClickPhoto {
        void itemClick(List<AudioModel> list, int i);
    }

    public void setClickItem(OnItemClickPhoto onItemClickPhoto) {
        this.mListener = onItemClickPhoto;
    }

    public AudioAdapter(List<AudioModel> list, Context context) {
        this.listMedia = list;
        this.context = context;
    }

    public void setData(List<AudioModel> list) {
        this.listMedia.clear();
        this.listMedia.addAll(list);
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (ITEM_VIEW_TYPE_HEADER == i) {
            return new AdViewHolder(LayoutInflater.from(context).inflate(R.layout.ad_native, viewGroup, false));
        } else {
            return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_song, viewGroup, false));
        }

    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (isHeader(i)) {
            AdsManager.CallNativeAdLoad((Activity) context, ((AdViewHolder) viewHolder).frameLayout, AdsManager.NATIVE_MIDEUM);
            ((AdViewHolder) viewHolder).setIsRecyclable(false);
        } else {
            ((ViewHolder) viewHolder).binData(this.listMedia.get(i), i);
        }

    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.listMedia.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    public boolean isHeader(int position) {
        return listMedia.get(position).header;
    }

    /* loaded from: classes4.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvArtist;
        TextView tvSongName;

        public ViewHolder(View view) {
            super(view);
            this.tvSongName = (TextView) view.findViewById(R.id.tvSongName);
            this.tvArtist = (TextView) view.findViewById(R.id.tvArtists);
        }

        public void binData(AudioModel audioModel, final int i) {
            this.tvSongName.setText(audioModel.getSongTitle());
            this.tvArtist.setText(audioModel.getSongArtist());
            this.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.audio.AudioAdapter.ViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    AudioAdapter.this.mListener.itemClick(AudioAdapter.this.listMedia, i);
                }
            });
        }
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameLayout;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.native_container);
        }
    }
}
