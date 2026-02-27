package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.photoff;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MediaModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PhotoOfflineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<MediaModel> listMedia;
    private OnItemClickPhoto mListener;
    private int type;

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;

    public interface OnItemClickPhoto {
        void itemClick(List<MediaModel> list, int i);
    }

    public void setClickItem(OnItemClickPhoto onItemClickPhoto) {
        this.mListener = onItemClickPhoto;
    }

    public PhotoOfflineAdapter(List<MediaModel> list, Context context, int i) {
        this.type = 0;
        this.listMedia = list;
        this.context = context;
        this.type = i;
    }

    public void setData(List<MediaModel> list) {
        this.listMedia.clear();
        this.listMedia.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (ITEM_VIEW_TYPE_HEADER == i) {
            return new AdViewHolder(LayoutInflater.from(context).inflate(R.layout.ad_native, viewGroup, false));
        } else {
            return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_photo, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if (isHeader(i)) {
            AdsManager.CallNativeAdLoad((Activity) context, ((AdViewHolder) holder).frameLayout, AdsManager.NATIVE_MIDEUM);
            ((AdViewHolder) holder).setIsRecyclable(false);
        } else {
            ((ViewHolder) holder).binData(this.listMedia.get(i), i);
        }

    }

    @Override
    public int getItemCount() {
        return this.listMedia.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    public boolean isHeader(int position) {
        return listMedia.get(position) == null;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_photo, imageView;
        RelativeLayout rlVideo;
        TextView tvDuration;

        public ViewHolder(View view) {
            super(view);
            this.imageView_photo = (ImageView) view.findViewById(R.id.imageView_photo);
            imageView = (ImageView) view.findViewById(R.id.imvCam);
            this.tvDuration = (TextView) view.findViewById(R.id.tvDuration);
            this.rlVideo = (RelativeLayout) view.findViewById(R.id.rlVideo);
        }

        @SuppressLint("WrongConstant")
        public void binData(MediaModel mediaModel, final int i) {
            Glide.with(PhotoOfflineAdapter.this.context).load(mediaModel.getPhotoUri()).placeholder(R.drawable.ic_image_default).centerCrop().into(this.imageView_photo);
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    ArrayList<MediaModel> list = new ArrayList<>();
                    for (MediaModel mediaModel1 : listMedia) {
                        if (mediaModel1 != null) {
                            list.add(mediaModel1);
                        }
                    }
                    PhotoOfflineAdapter.this.mListener.itemClick(list, i);
                }
            });
            if (PhotoOfflineAdapter.this.type == 2) {
                imageView.setVisibility(View.VISIBLE);
                this.rlVideo.setVisibility(View.VISIBLE);
                this.tvDuration.setText(Utils.formatTime(mediaModel.getDuration()));
            } else {
                imageView.setVisibility(View.GONE);
                rlVideo.setVisibility(View.GONE);
            }
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
