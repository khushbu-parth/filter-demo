package com.mancj.materialsearchbar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;


public class DefaultSuggestionsAdapter extends SuggestionsAdapter<String, DefaultSuggestionsAdapter.SuggestionHolder> {
    public SuggestionsAdapter.OnItemViewClickListener listener;

    public interface OnItemViewClickListener {
        void OnItemClickListener(int i, View view);

        void OnItemDeleteListener(int i, View view);
    }

    public int getSingleViewHeight() {
        return 50;
    }

    public DefaultSuggestionsAdapter(LayoutInflater layoutInflater) {
        super(layoutInflater);
    }

    public void setListener(SuggestionsAdapter.OnItemViewClickListener onItemViewClickListener) {
        this.listener = onItemViewClickListener;
    }

    public SuggestionHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SuggestionHolder(getLayoutInflater().inflate(R.layout.item_last_request, viewGroup, false));
    }

    public void onBindSuggestionHolder(String str, SuggestionHolder suggestionHolder, int i) {
        suggestionHolder.text.setText((CharSequence) getSuggestions().get(i));
    }

    class SuggestionHolder extends RecyclerView.ViewHolder {
        private ImageView iv_delete;
        public TextView text;

        public SuggestionHolder(View view) {
            super(view);
            this.text = (TextView) view.findViewById(R.id.text);
            this.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    view.setTag(DefaultSuggestionsAdapter.this.getSuggestions().get(SuggestionHolder.this.getAdapterPosition()));
                    DefaultSuggestionsAdapter.this.listener.OnItemClickListener(SuggestionHolder.this.getAdapterPosition(), view);
                }
            });
            this.iv_delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    view.setTag(DefaultSuggestionsAdapter.this.getSuggestions().get(SuggestionHolder.this.getAdapterPosition()));
                    DefaultSuggestionsAdapter.this.listener.OnItemDeleteListener(SuggestionHolder.this.getAdapterPosition(), view);
                }
            });
        }
    }
}
