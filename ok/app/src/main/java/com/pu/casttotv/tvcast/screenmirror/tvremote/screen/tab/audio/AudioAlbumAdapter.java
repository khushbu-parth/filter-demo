package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.audio;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.AudioAlbumModel;

import java.util.List;

/* loaded from: classes4.dex */
public class AudioAlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<AudioAlbumModel> listMedia;
    private OnItemClickPhoto mListener;
    private int type;
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;

    /* loaded from: classes4.dex */
    public interface OnItemClickPhoto {
        void itemClick(AudioAlbumModel audioAlbumModel);
    }

    public void setClickItem(OnItemClickPhoto onItemClickPhoto) {
        this.mListener = onItemClickPhoto;
    }

    public AudioAlbumAdapter(List<AudioAlbumModel> list, Context context, int i) {
        this.type = 0;
        this.listMedia = list;
        this.context = context;
        this.type = i;
    }

    public void setData(List<AudioAlbumModel> list) {
        this.listMedia.clear();
        this.listMedia.addAll(list);
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (ITEM_VIEW_TYPE_HEADER == i) {
            return new AdViewHolder(LayoutInflater.from(context).inflate(R.layout.ad_native, viewGroup, false));
        } else {
            return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_album_audio, viewGroup, false));
        }

    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (isHeader(i)) {
            AdsManager.CallNativeAdLoad((Activity) context, ((AdViewHolder) viewHolder).frameLayout, AdsManager.NATIVE_MIDEUM);
            ((AdViewHolder) viewHolder).setIsRecyclable(false);
        } else {
            if (listMedia.get(i).getArrSong().size() != 0) {
                ((ViewHolder) viewHolder).binData(this.listMedia.get(i), i);
            }
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
        ImageView imageView_photo;
        TextView tvNameAlbum;
        TextView tvSizeSong;

        public ViewHolder(View view) {
            super(view);
            this.imageView_photo = (ImageView) view.findViewById(R.id.imageView_photo);
            this.tvSizeSong = (TextView) view.findViewById(R.id.tvSizeSong);
            this.tvNameAlbum = (TextView) view.findViewById(R.id.tvNameAlbum);
        }

        public void binData(final AudioAlbumModel audioAlbumModel, int i) {
            this.tvNameAlbum.setText(audioAlbumModel.getNameAlbum());
            if (AudioAlbumAdapter.this.type == 2) {
                this.imageView_photo.setImageResource(R.drawable.music);
            }
            if (audioAlbumModel.getArrSong() != null) {
                TextView textView = this.tvSizeSong;
                textView.setText(audioAlbumModel.getArrSong().size() + " " + AudioAlbumAdapter.this.context.getResources().getString(R.string.txt_song));
            }
            this.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.audio.AudioAlbumAdapter.ViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    AudioAlbumAdapter.this.mListener.itemClick(audioAlbumModel);
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
