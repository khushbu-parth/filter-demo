package com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.language;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.ObjectLanguage;
import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ObjectLanguage> languageList;
    private String languageOld;
    private IItemCountry mListener;

    public interface IItemCountry {
        void setOnclickItem(int i, ObjectLanguage objectLanguage);
    }

    public LanguageAdapter(Context context2, List<ObjectLanguage> list) {
        this.context = context2;
        this.languageList = list;
    }

    public void setListener(IItemCountry iItemCountry) {
        this.mListener = iItemCountry;
    }

    public void setData(List<ObjectLanguage> list, String str) {
        this.languageOld = "";
        this.languageOld = str;
        this.languageList.clear();
        this.languageList.addAll(list);
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new TypeTwoViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_language, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((TypeTwoViewHolder) viewHolder).binData(this.languageList.get(i), i);
    }

    public class TypeTwoViewHolder extends RecyclerView.ViewHolder {
        private RadioButton radioSelect;
        private TextView tvNameCountry;

        public TypeTwoViewHolder(View view) {
            super(view);
            this.tvNameCountry = (TextView) view.findViewById(R.id.tvName);
            this.radioSelect = (RadioButton) view.findViewById(R.id.rbChecked);
        }

        public void binData(final ObjectLanguage objectLanguage, final int i) {
            if (LanguageAdapter.this.languageOld.equals(objectLanguage.getKey())) {
                this.radioSelect.setChecked(true);
            } else {
                this.radioSelect.setChecked(false);
            }
            this.tvNameCountry.setText(objectLanguage.getName());
            this.itemView.setOnClickListener(new View.OnClickListener() {
                /* class com.magicapps.casttotv.tv.dialog.language.LanguageAdapter.TypeTwoViewHolder.AnonymousClass1 */

                public void onClick(View view) {
                    LanguageAdapter.this.mListener.setOnclickItem(i, objectLanguage);
                }
            });
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.languageList.size();
    }
}
