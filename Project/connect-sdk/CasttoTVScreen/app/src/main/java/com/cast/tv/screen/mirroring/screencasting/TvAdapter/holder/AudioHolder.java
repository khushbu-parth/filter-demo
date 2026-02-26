package com.cast.tv.screen.mirroring.screencasting.TvAdapter.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cast.tv.screen.mirroring.screencasting.R;

public class AudioHolder extends RecyclerView.ViewHolder {
    public View itemView;
    public TextView textName;
    public TextView textSubtitle;

    public AudioHolder(View view) {
        super(view);
        this.itemView = view;
        this.textName = (TextView) view.findViewById(R.id.text_name);
        this.textSubtitle = (TextView) view.findViewById(R.id.text_subtitle);
    }
}
