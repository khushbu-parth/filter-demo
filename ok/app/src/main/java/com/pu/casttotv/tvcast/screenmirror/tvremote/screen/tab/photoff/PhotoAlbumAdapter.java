package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.photoff;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.PhotoAlbum;
import java.io.File;
import java.util.ArrayList;

/* loaded from: classes4.dex */
public class PhotoAlbumAdapter extends RecyclerView.Adapter<PhotoAlbumAdapter.AlbumHolder> {
    private ArrayList<PhotoAlbum> arrAlbums;
    private Context context;
    private OnItemClick onItemClick;

    /* loaded from: classes4.dex */
    public interface OnItemClick {
        void onItemClick(int i, PhotoAlbum photoAlbum);
    }

    public PhotoAlbumAdapter(Context context, ArrayList<PhotoAlbum> arrayList) {
        this.context = context;
        this.arrAlbums = arrayList;
    }

    public void setData(ArrayList<PhotoAlbum> arrayList) {
        this.arrAlbums.clear();
        this.arrAlbums.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void setItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public AlbumHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AlbumHolder(this, LayoutInflater.from(this.context).inflate(R.layout.item_album_photo, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(AlbumHolder albumHolder, final int i) {
        albumHolder.textViewNameAlbum.setText(this.arrAlbums.get(i).getName());
        Glide.with(this.context).asBitmap().load(Uri.fromFile(new File(this.arrAlbums.get(i).getCoverUri()))).centerCrop().into(albumHolder.imageView);
        albumHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PhotoAlbumAdapter.this.onItemClick.onItemClick(i, (PhotoAlbum) PhotoAlbumAdapter.this.arrAlbums.get(i));
            }
        });
        TextView textView = albumHolder.tvNumberAlbum;
        textView.setText("(" + this.arrAlbums.get(i).getAlbumPhotos().size() + ")");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.arrAlbums.size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public class AlbumHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textViewNameAlbum;
        private TextView tvNumberAlbum;

        public AlbumHolder(PhotoAlbumAdapter photoAlbumAdapter, View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.imvAlbum);
            this.textViewNameAlbum = (TextView) view.findViewById(R.id.tvNameAlbum);
            this.tvNumberAlbum = (TextView) view.findViewById(R.id.tvNumberAlbum);
        }
    }
}
