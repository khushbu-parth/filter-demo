package com.cast.tv.screen.mirroring.screencasting.TvAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.R;

import java.util.List;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.QueueViewHolder> {
private List<FileModel> mfileModelList;
private OnItemClickListener mListener;

public interface OnItemClickListener{
    void onItemClick(int position);
    void onDeleteClick(int position);
}

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

public static class QueueViewHolder extends RecyclerView.ViewHolder{

    public TextView text_name;

    public ImageView mDeleteImage;

    public QueueViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        text_name = itemView.findViewById(R.id.text_name);
        mDeleteImage = itemView.findViewById(R.id.image_delete_item);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            }
        });

        mDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onDeleteClick(position);
                    }
                }
            }
        });
    }
}

    public QueueAdapter(List<FileModel> mfileModelList) {
        mfileModelList = mfileModelList;
    }

    @NonNull
    @Override
    public QueueViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_play_queue, viewGroup, false);
        QueueViewHolder evh = new QueueViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull QueueViewHolder exampleViewHolder, int i) {
        FileModel currentItem = mfileModelList.get(i);
        exampleViewHolder.text_name.setText(currentItem.getDisplayName());
    }

    @Override
    public int getItemCount() {
        return mfileModelList.size();
    }
}
