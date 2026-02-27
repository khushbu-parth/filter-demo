package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.photoonl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.PhotoOnlineModel;
import java.util.List;

/* loaded from: classes4.dex */
public class PhotoOnlineAdapter extends RecyclerView.Adapter<PhotoOnlineAdapter.ViewHolder> {
    private Context context;
    private List<PhotoOnlineModel> listMedia;
    private OnItemClickPhoto mListener;

    /* loaded from: classes4.dex */
    public interface OnItemClickPhoto {
        void itemClick(List<PhotoOnlineModel> list, int i);
    }

    public void setClickItem(OnItemClickPhoto onItemClickPhoto) {
        this.mListener = onItemClickPhoto;
    }

    public PhotoOnlineAdapter(List<PhotoOnlineModel> list, Context context) {
        this.listMedia = list;
        this.context = context;
    }

    public void setData(List<PhotoOnlineModel> list) {
        this.listMedia.clear();
        this.listMedia.addAll(list);
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_photo, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.binData(this.listMedia.get(i), i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.listMedia.size();
    }

    /* loaded from: classes4.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_photo;

        public ViewHolder(View view) {
            super(view);
            this.imageView_photo = (ImageView) view.findViewById(R.id.imageView_photo);
        }

        public void binData(PhotoOnlineModel photoOnlineModel, final int i) {
            Glide.with(PhotoOnlineAdapter.this.context).load(photoOnlineModel.getThumbURL()).placeholder(R.drawable.bg_dialog).centerCrop().into(this.imageView_photo);
            this.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.photoonl.PhotoOnlineAdapter.ViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    PhotoOnlineAdapter.this.mListener.itemClick(PhotoOnlineAdapter.this.listMedia, i);
                }
            });
        }
    }
}
