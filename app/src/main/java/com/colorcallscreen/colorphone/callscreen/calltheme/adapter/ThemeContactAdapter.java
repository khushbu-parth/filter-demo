package com.colorcallscreen.colorphone.callscreen.calltheme.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.internal.view.SupportMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityThemeContactList;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ThemeContactModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.images.UsersImageHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.CallLogUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import java.util.ArrayList;
import java.util.List;


public class ThemeContactAdapter extends RecyclerView.Adapter<ThemeContactAdapter.ContactViewHolder> implements Filterable {
    private String contactForTheme;
    private Context context;
    private List<ThemeContactModel> filteredList;
    private List<ThemeContactModel> models;
    private String query;
    private boolean searchCleared;
    private List<ThemeContactModel> selectedContact = new ArrayList();
    Filter filter = new Filter() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ThemeContactAdapter.1
        @Override // android.widget.Filter
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            try {
                if (charSequence.length() == 0) {
                    arrayList.addAll(ThemeContactAdapter.this.filteredList);
                    ThemeContactAdapter.this.searchCleared = true;
                } else {
                    String trim = charSequence.toString().trim();
                    ThemeContactAdapter.this.query = trim;
                    for (ThemeContactModel themeContactModel : ThemeContactAdapter.this.filteredList) {
                        ThemeContactAdapter.this.searchCleared = false;
                        String lowerCase = themeContactModel.getName().toLowerCase();
                        if (lowerCase != null && lowerCase.contains(trim.toLowerCase())) {
                            arrayList.add(themeContactModel);
                        }
                    }
                }
            } catch (Exception unused) {
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        @Override // android.widget.Filter
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            List list = (List) filterResults.values;
            if (list != null) {
                ThemeContactAdapter.this.models.clear();
                ThemeContactAdapter.this.models.addAll(list);
                ThemeContactAdapter.this.notifyDataSetChanged();
            }
        }
    };

    
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        AppCompatImageView circleImageView;
        TextView themeName;
        TextView txtLetter;
        TextView txtName;
        TextView txtNumber;

        public ContactViewHolder(View view) {
            super(view);
            this.txtName = (TextView) view.findViewById(R.id.txtName);
            this.txtNumber = (TextView) view.findViewById(R.id.txtNumber);
            this.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            this.txtLetter = (TextView) view.findViewById(R.id.txtLetter);
            this.themeName = (TextView) view.findViewById(R.id.textView2);
            this.circleImageView = (AppCompatImageView) view.findViewById(R.id.user_img);
        }
    }

    public ThemeContactAdapter(Context context, List<ThemeContactModel> list, String str) {
        this.context = context;
        this.models = list;
        this.filteredList = new ArrayList(list);
        this.contactForTheme = str;
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        return this.filter;
    }

    @Override 
    public int getItemCount() {
        List<ThemeContactModel> list = this.models;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override 
    public void onBindViewHolder(final ContactViewHolder contactViewHolder, int i) {
        String str;
        final ThemeContactModel themeContactModel = this.models.get(i);
        if (themeContactModel.getColorCode() == -1) {
            int[] iArr = CallLogUtils.colors;
            themeContactModel.setColorCode(iArr[i % iArr.length]);
        }
        contactViewHolder.circleImageView.setImageResource(themeContactModel.getColorCode());
        contactViewHolder.txtName.setText(themeContactModel.getName());
        contactViewHolder.txtNumber.setText(themeContactModel.getNumber());
        if (themeContactModel.getName() != null) {
            String[] split = themeContactModel.getName().split(" ");
            if (split.length > 1) {
                try {
                    str = split[0].substring(0, 1) + split[1].substring(0, 1) + "";
                } catch (Exception unused) {
                    str = split[0].substring(0, 1) + "";
                }
            } else {
                str = split[0].substring(0, 1) + "";
            }
            contactViewHolder.txtLetter.setText(str);
        } else {
            contactViewHolder.txtLetter.setText("");
        }
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ThemeContactAdapter.2
            @Override 
            public void run() {
                Log.e("sac", themeContactModel.getName());
                final Bitmap userImageForPhoneNumber = UsersImageHandler.userImageForPhoneNumber(themeContactModel.getName(), ThemeContactAdapter.this.context);
                if (userImageForPhoneNumber != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ThemeContactAdapter.2.1
                        @Override 
                        public void run() {
                            contactViewHolder.circleImageView.setImageBitmap(userImageForPhoneNumber);
                            contactViewHolder.txtLetter.setVisibility(8);
                        }
                    });
                }
            }
        }).start();
        String string = PreferenceUtils.getInstance().getString(themeContactModel.getName());
        Log.d("___TE", "I :" + themeContactModel.getName() + ":::" + string);
        if (string != null) {
            contactViewHolder.themeName.setVisibility(0);
            contactViewHolder.themeName.setText(string);
            String str2 = this.contactForTheme;
            if (str2 != null && string.equals(str2)) {
                themeContactModel.setSelected(true);
                this.selectedContact.add(themeContactModel);
            }
        } else {
            contactViewHolder.themeName.setVisibility(8);
        }
        contactViewHolder.checkBox.setOnCheckedChangeListener(null);
        contactViewHolder.checkBox.setChecked(themeContactModel.isSelected());
        contactViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ThemeContactAdapter.3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (!z) {
                    ThemeContactAdapter.this.selectedContact.remove(themeContactModel);
                    themeContactModel.setSelected(false);
                    contactViewHolder.checkBox.setChecked(false);
                } else {
                    contactViewHolder.checkBox.setChecked(true);
                    ThemeContactAdapter.this.selectedContact.add(themeContactModel);
                    themeContactModel.setSelected(true);
                }
                ((ActivityThemeContactList) ThemeContactAdapter.this.context).onContactSelected(ThemeContactAdapter.this.selectedContact);
            }
        });
        contactViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ThemeContactAdapter.4
            @Override 
            public void onClick(View view) {
                if (contactViewHolder.checkBox.isChecked()) {
                    ThemeContactAdapter.this.selectedContact.remove(themeContactModel);
                    themeContactModel.setSelected(false);
                    contactViewHolder.checkBox.setChecked(false);
                } else {
                    contactViewHolder.checkBox.setChecked(true);
                    ThemeContactAdapter.this.selectedContact.add(themeContactModel);
                    themeContactModel.setSelected(true);
                }
                ((ActivityThemeContactList) ThemeContactAdapter.this.context).onContactSelected(ThemeContactAdapter.this.selectedContact);
            }
        });
        try {
            if (this.query != null && !this.searchCleared) {
                String lowerCase = themeContactModel.getName().toLowerCase();
                if (lowerCase.contains(this.query)) {
                    int indexOf = lowerCase.indexOf(this.query);
                    Spannable newSpannable = Spannable.Factory.getInstance().newSpannable(contactViewHolder.txtName.getText());
                    newSpannable.setSpan(new ForegroundColorSpan((int) SupportMenu.CATEGORY_MASK), indexOf, this.query.length() + indexOf, 33);
                    contactViewHolder.txtName.setText(newSpannable, TextView.BufferType.SPANNABLE);
                }
            }
        } catch (Exception unused2) {
        }
    }

    @Override 
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ContactViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_theme_contact, viewGroup, false));
    }
}
