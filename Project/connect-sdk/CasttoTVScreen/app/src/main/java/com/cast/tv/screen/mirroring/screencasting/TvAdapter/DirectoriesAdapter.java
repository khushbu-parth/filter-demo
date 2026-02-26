package com.cast.tv.screen.mirroring.screencasting.TvAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cast.tv.screen.mirroring.screencasting.Callback.DirectoriesItemClick;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.holder.AudioHolder;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.holder.FolderHolder;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.holder.PhotoHolder;
import com.cast.tv.screen.mirroring.screencasting.TvAdapter.holder.VideoHolder;
import com.cast.tv.screen.mirroring.screencasting.Utils.CornerTransform;

import java.util.List;

public class DirectoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<FileModel> mList;
    private Context mContext;
    private DirectoriesItemClick mItemClick;

    public DirectoriesAdapter(List<FileModel> list) {
        this.mList = list;
    }

    public void setItemClick(DirectoriesItemClick directoriesItemClick) {
        this.mItemClick = directoriesItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.mContext = viewGroup.getContext();
        if (i == 274) {
            return new AudioHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_directories_audio, viewGroup, false));
        }
        if (i == 273) {
            return new VideoHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_directories_video, viewGroup, false));
        }
        if (i == 272) {
            return new PhotoHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_directories_photo, viewGroup, false));
        }
        return new FolderHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_directories_folder, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        if (viewHolder.getItemViewType() == 273) {
            VideoHolder videoHolder = (VideoHolder) viewHolder;
            videoHolder.textName.setText(this.mList.get(i).getDisplayName());
            videoHolder.textSubtitle.setText(this.mList.get(i).getDurationStr());
            Glide.with(this.mContext).asBitmap().load(this.mList.get(i).getMiniKindByte()).error((int) R.drawable.video_default_bg).transform(new CornerTransform(this.mContext, 6.6f)).into(videoHolder.image);
        } else if (viewHolder.getItemViewType() == 274) {
            AudioHolder audioHolder = (AudioHolder) viewHolder;
            audioHolder.textName.setText(this.mList.get(i).getDisplayName());
            audioHolder.textSubtitle.setText(this.mList.get(i).getSubTitle());
        } else if (viewHolder.getItemViewType() == 272) {
            PhotoHolder photoHolder = (PhotoHolder) viewHolder;
            photoHolder.textName.setText(this.mList.get(i).getDisplayName());
            Glide.with(this.mContext).asBitmap().load(this.mList.get(i).getPath()).placeholder((int) R.color.color_646464).error((int) R.color.color_646464).transform(new CornerTransform(this.mContext, 6.6f)).into(photoHolder.image);
        } else {
            FolderHolder folderHolder = (FolderHolder) viewHolder;
            folderHolder.textName.setText(this.mList.get(i).getDisplayName());
            folderHolder.textNum.setText(this.mList.get(i).getChildCount() + " item");
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                DirectoriesAdapter.this.$onBindViewHolder$0$DirectoriesAdapter(i, view);
            }
        });
    }

    public void $onBindViewHolder$0$DirectoriesAdapter(int i, View view) {
        DirectoriesItemClick directoriesItemClick = this.mItemClick;
        if (directoriesItemClick != null) {
            directoriesItemClick.onItemClick(this.mList.get(i));
        }
    }

    @Override
    public int getItemViewType(int i) {
        return this.mList.get(i).getFileType();
    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }
}
