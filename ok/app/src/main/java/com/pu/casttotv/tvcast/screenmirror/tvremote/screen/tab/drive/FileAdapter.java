package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.drive;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;
import java.util.List;

@SuppressLint("WrongConstant")
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    private List<GoogleDriveItem> list;
    private IItemClick mListener;

    /* loaded from: classes4.dex */
    public interface IItemClick {
        void clickItem(GoogleDriveItem googleDriveItem, List<GoogleDriveItem> list);
    }

    public FileAdapter(List<GoogleDriveItem> list) {
        this.list = list;
    }

    public void setListener(IItemClick iItemClick) {
        this.mListener = iItemClick;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view, viewGroup, false));
    }

    public void setData(List<GoogleDriveItem> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.bindData(this.list.get(i));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size();
    }

    /* loaded from: classes4.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imvFolder;
        ImageView imvImage;
        TextView tvName;
        TextView tvSize;

        public ViewHolder(View view) {
            super(view);
            this.imvFolder = (ImageView) view.findViewById(R.id.imvFolder);
            this.tvSize = (TextView) view.findViewById(R.id.tvSize);
            this.imvImage = (ImageView) view.findViewById(R.id.imvImage);
            this.tvName = (TextView) view.findViewById(R.id.tvName);
        }

        public void bindData(final GoogleDriveItem googleDriveItem) {
            this.tvName.setText(googleDriveItem.getName());
            if (googleDriveItem.isImage()) {
                this.imvImage.setVisibility(0);
                this.imvFolder.setVisibility(8);
                if (googleDriveItem.getSize() != null) {
                    this.tvSize.setText(Utils.readableFileSize(googleDriveItem.getSize().longValue()));
                }
            } else {
                this.imvImage.setVisibility(8);
                this.imvFolder.setVisibility(0);
            }
            Glide.with(this.itemView.getContext()).load(googleDriveItem.getThumbnailLink()).centerCrop().placeholder(R.drawable.ic_folder).into(this.imvImage);
            this.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.thntech.cast68.screen.tab.drive.FileAdapter.ViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    FileAdapter.this.mListener.clickItem(googleDriveItem, FileAdapter.this.list);
                }
            });
        }
    }
}
