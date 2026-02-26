package com.cast.tv.screen.mirroring.screencasting.supportedices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cast.tv.screen.mirroring.screencasting.R;


public class TVName_adapter extends RecyclerView.Adapter<TVName_adapter.Dict_viewHolder> {
    String brand;
    Context context;
    OnClickAdd onClickAdd;
    String[] str_TV_Name;

    public TVName_adapter(String[] strArr, Context context2) {
        this.str_TV_Name = strArr;
        this.context = context2;
        this.onClickAdd = (OnClickAdd) context2;
    }

    @Override
    public Dict_viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Dict_viewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.iuc_item_tvname, viewGroup, false));
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(Dict_viewHolder dict_viewHolder, int i) {
        this.brand = this.str_TV_Name[i];
        dict_viewHolder.atv_tvname.setText(this.str_TV_Name[i]);
        dict_viewHolder.atv_tvname.setTextColor(R.color.colorPrimary);
    }

    @Override
    public int getItemCount() {
        return this.str_TV_Name.length;
    }

    public class Dict_viewHolder extends RecyclerView.ViewHolder {
        TextView atv_tvname;

        public Dict_viewHolder(View view) {
            super(view);
            this.atv_tvname = (TextView) view.findViewById(R.id.atv_tvname);
            view.setOnClickListener(view1 -> TVName_adapter.this.onClickAdd.ItemClick(Dict_viewHolder.this.getAdapterPosition()));
        }
    }
}
