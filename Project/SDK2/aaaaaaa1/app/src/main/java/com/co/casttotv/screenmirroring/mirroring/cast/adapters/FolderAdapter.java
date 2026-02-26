package com.co.casttotv.screenmirroring.mirroring.cast.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.MediaData;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ItemFoldersBinding;
import com.co.casttotv.screenmirroring.mirroring.cast.models.MediaModel;

import java.io.File;
import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    Context context;
    ArrayList<String> modelList;
    Boolean isVideo = false;
    OnItemListener listener;
    
    public interface OnItemListener {
        void onItemClick(String folder);
    }

    public FolderAdapter(Context context, ArrayList<String> modelList, Boolean isVideo, OnItemListener onItemListener) {
        this.context = context;
        this.modelList = modelList;
        this.isVideo = isVideo;
        this.listener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoldersBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_folders, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String folder = modelList.get(position);
        ArrayList<MediaModel> list = isVideo ? MediaData.getVideosFrom(context, folder) : MediaData.getImagesFrom(context, folder);
        String s;
        if (isVideo) {
            s = list.size() == 1 ? " Video" : " Videos";
        } else {
            s = list.size() == 1 ? " Photo" : " Photos";
        }

        holder.binding.textItemCount.setText(String.valueOf(list.size()) + s);
        holder.binding.textName.setText(folder);
        Glide.with(context)
                .load(new File(list.get(0).getPath()))
                .placeholder(R.color.white)
                .into(holder.binding.imageThumbnail);

        holder.itemView.setOnClickListener(view -> {
            listener.onItemClick(folder);
        });
    }

    @Override
    public int getItemCount() {
        if (modelList != null) return modelList.size();
        return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ItemFoldersBinding binding;

        public ViewHolder(@NonNull ItemFoldersBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

}
