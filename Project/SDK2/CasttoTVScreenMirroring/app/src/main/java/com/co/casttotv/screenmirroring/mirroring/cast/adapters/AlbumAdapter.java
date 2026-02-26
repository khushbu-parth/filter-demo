package com.co.casttotv.screenmirroring.mirroring.cast.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.co.casttotv.screenmirroring.mirroring.cast.BR;
import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.activities.AudioAlbumActivity;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ItemAlbumBinding;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    Context mContext;
    ArrayList<String> modelList;
    Boolean isAlbum = false;

    public AlbumAdapter(Context mContext, ArrayList<String> modelList, Boolean aBoolean) {
        this.mContext = mContext;
        this.modelList = modelList;
        this.isAlbum = aBoolean;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAlbumBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_album, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(modelList.get(position));
        holder.itemView.setOnClickListener(view -> {
            mContext.startActivity(new Intent(mContext, AudioAlbumActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("type", isAlbum)
                    .putExtra("root", modelList.get(position)));
        });
    }

    @Override
    public int getItemCount() {
        if (modelList != null) return modelList.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemAlbumBinding binding;

        public ViewHolder(@NonNull ItemAlbumBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(Object data) {
            binding.setVariable(BR.album, data);
            binding.executePendingBindings();
        }
    }

}
