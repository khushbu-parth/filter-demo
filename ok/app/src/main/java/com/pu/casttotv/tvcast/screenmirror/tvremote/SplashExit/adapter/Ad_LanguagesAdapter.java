package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.ObjectLanguage;

import java.util.ArrayList;

public class Ad_LanguagesAdapter extends RecyclerView.Adapter<Ad_LanguagesAdapter.ViewHolder> {
    Context context;
    ArrayList<ObjectLanguage> list;
    getLanguage cGetLanguage;
    private int selectedPos = 0;

    public Ad_LanguagesAdapter(Context context, ArrayList<ObjectLanguage> list, getLanguage cGetLanguage) {
        this.context = context;
        this.list = list;
        this.cGetLanguage = cGetLanguage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.ad_item_languages, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv1.setText(list.get(position).getName());
        holder.tvcircle.setText(list.get(position).getKey().equals("")?"en":list.get(position).getKey());
        holder.tvcircle.setAllCaps(true);
        holder.iv1.setVisibility(selectedPos == position ? View.VISIBLE : View.GONE);
        holder.tv1.setTextColor(context.getResources().getColor(selectedPos == position ? R.color.color_main : R.color.white));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv1, tvcircle;
        ImageView iv1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tvcircle = itemView.findViewById(R.id.tvcircle);
            iv1 = itemView.findViewById(R.id.iv1);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cGetLanguage.onClick(getAdapterPosition(), list.get(getAdapterPosition()));
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        notifyItemChanged(selectedPos);
                        selectedPos = getAdapterPosition();
                        notifyItemChanged(selectedPos);
                    }
                }
            });
        }
    }
}
