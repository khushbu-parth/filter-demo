package com.colorcallscreen.colorphone.callscreen.calltheme.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.internal.view.SupportMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.MainActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.PhoneModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.images.UsersImageHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.CallLogUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private Context context;
    private List<ContactModel> list;
    private ClickModel listener;
    private ArrayList<ContactModel> selectedContact;
    public String query = "";
    public boolean hasSearch = false;
    public String pattern = "";

    
    public interface ClickModel {
        void onContactUserImageClicked(ContactModel contactModel, View view);
    }

    
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView circleImageView;
        private TextView divider;
        CheckBox rdSelect;
        TextView txtLetter;
        TextView txtName;
        TextView txtNumber;

        public ContactViewHolder(View view) {
            super(view);
            this.divider = (TextView) view.findViewById(R.id.divider);
            this.txtName = (TextView) view.findViewById(R.id.txtName);
            this.txtLetter = (TextView) view.findViewById(R.id.txtLetter);
            this.txtNumber = (TextView) view.findViewById(R.id.txtNumber);
            this.circleImageView = (AppCompatImageView) view.findViewById(R.id.user_img);
            this.rdSelect = (CheckBox) view.findViewById(R.id.rdSelect);
        }
    }

    public ContactAdapter(Context context, List<ContactModel> list) {
        this.list = new ArrayList();
        this.selectedContact = new ArrayList<>();
        this.context = context;
        this.list.clear();
        this.list = new ArrayList(list);
        this.selectedContact = new ArrayList<>();
    }

    @Override 
    public int getItemCount() {
        return this.list.size();
    }

    public void setListener(ClickModel clickModel) {
        this.listener = clickModel;
    }

    public void updateList(List<ContactModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override 
    public void onBindViewHolder(final ContactViewHolder contactViewHolder, int i) {
        String str;
        String lowerCase;
        CharSequence text;
        final ContactModel contactModel = this.list.get(i);
        boolean z = true;
        if (((MainActivity) this.context).isSelected) {
            contactViewHolder.rdSelect.setVisibility(0);
            if (contactModel.isSelected()) {
                contactViewHolder.rdSelect.setChecked(true);
            } else {
                contactViewHolder.rdSelect.setChecked(false);
            }
        }
        if (contactModel.getColorCode() == -1) {
            int[] iArr = CallLogUtils.colors;
            contactModel.setColorCode(iArr[i % iArr.length]);
        }
        if (!contactModel.getDisplayNumber().isEmpty()) {
            contactViewHolder.txtNumber.setText(contactModel.getDisplayNumber());
        } else {
            contactViewHolder.txtNumber.setText(contactModel.getDisplayNumber());
        }
        contactViewHolder.divider.setText(contactModel.getFirstLetter());
        String str2 = null;
        if (i > 0 && this.list.size() > i) {
            str2 = this.list.get(i - 1).getFirstLetter();
        }
        if (str2 == null) {
            contactViewHolder.divider.setVisibility(0);
        } else if (!str2.equals(contactModel.getFirstLetter()) || i == 0) {
            contactViewHolder.divider.setVisibility(0);
        } else {
            contactViewHolder.divider.setVisibility(8);
        }
        String str3 = this.query;
        if (str3 != null && !str3.equals("")) {
            Iterator<PhoneModel> it = contactModel.getNumbers().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                PhoneModel next = it.next();
                if (next.getCallNumber().contains(this.query)) {
                    contactViewHolder.txtNumber.setText(next.getCallNumber());
                    break;
                }
            }
        }
        contactViewHolder.txtLetter.setVisibility(0);
        contactViewHolder.circleImageView.setImageResource(contactModel.getColorCode());
        contactViewHolder.txtName.setText(contactModel.getName());
        String[] split = contactModel.getName().split(" ");
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
        if (!contactModel.isUserImagePresentChecked()) {
            new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ContactAdapter.1
                @Override 
                public void run() {
                    Bitmap userImageForPhoneNumber = UsersImageHandler.userImageForPhoneNumber(contactModel.getDisplayNumber(), ContactAdapter.this.context);
                    contactModel.setUserImagePresentChecked(true);
                    if (userImageForPhoneNumber != null) {
                        contactModel.setUserImage(userImageForPhoneNumber);
                        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ContactAdapter.1.1
                            @Override 
                            public void run() {
                                contactViewHolder.circleImageView.setImageBitmap(contactModel.getUserImage());
                                contactViewHolder.txtLetter.setVisibility(8);
                            }
                        });
                    }
                }
            }).start();
        } else if (contactModel.getUserImage() != null) {
            contactViewHolder.circleImageView.setImageBitmap(contactModel.getUserImage());
            contactViewHolder.txtLetter.setVisibility(8);
        }
        try {
            if (this.query != null) {
                if (this.hasSearch && !contactModel.getDisplayNumber().isEmpty() && contactModel.getDisplayNumber().contains(this.query)) {
                    lowerCase = contactModel.getDisplayNumber().toLowerCase();
                } else {
                    lowerCase = contactModel.getName().toLowerCase();
                    z = false;
                }
                if (lowerCase.contains(this.query)) {
                    int indexOf = lowerCase.indexOf(this.query);
                    int length = this.query.length() + indexOf;
                    if (z) {
                        text = contactViewHolder.txtNumber.getText();
                    } else {
                        text = contactViewHolder.txtName.getText();
                    }
                    Spannable newSpannable = Spannable.Factory.getInstance().newSpannable(text);
                    newSpannable.setSpan(new ForegroundColorSpan((int) SupportMenu.CATEGORY_MASK), indexOf, length, 33);
                    if (z) {
                        contactViewHolder.txtNumber.setText(newSpannable, TextView.BufferType.SPANNABLE);
                    } else {
                        contactViewHolder.txtName.setText(newSpannable, TextView.BufferType.SPANNABLE);
                    }
                }
            }
        } catch (Exception unused2) {
        }
        contactViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ContactAdapter.2
            @Override 
            public void onClick(View view) {
                if (!((MainActivity) ContactAdapter.this.context).isSelected) {
                    if (ContactAdapter.this.listener != null) {
                        ContactAdapter.this.listener.onContactUserImageClicked(contactModel, contactViewHolder.circleImageView);
                        return;
                    }
                    return;
                }
                ContactAdapter contactAdapter = ContactAdapter.this;
                contactAdapter.selectItem(contactAdapter.context, contactViewHolder, contactModel);
            }
        });
        contactViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ContactAdapter.3
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                ContactAdapter contactAdapter = ContactAdapter.this;
                contactAdapter.selectItem(contactAdapter.context, contactViewHolder, contactModel);
                if (!((MainActivity) ContactAdapter.this.context).isSelected) {
                    ((MainActivity) ContactAdapter.this.context).isSelected = true;
                    ContactAdapter.this.notifyDataSetChanged();
                }
                return true;
            }
        });
    }

    
    public void selectItem(Context context, ContactViewHolder contactViewHolder, ContactModel contactModel) {
        if (contactViewHolder.rdSelect.isChecked()) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.selectedCount--;
            mainActivity.txtSelect.setText(mainActivity.selectedCount + " selected");
            contactViewHolder.rdSelect.setChecked(false);
            contactModel.setSelected(false);
            this.selectedContact.remove(contactModel);
        } else {
            MainActivity mainActivity2 = (MainActivity) context;
            mainActivity2.selectedCount++;
            mainActivity2.txtSelect.setText(mainActivity2.selectedCount + " selected");
            contactViewHolder.rdSelect.setChecked(true);
            contactModel.setSelected(true);
            this.selectedContact.add(contactModel);
        }
        ((MainActivity) context).onContactSelected(this.selectedContact);
    }

    @Override 
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ContactViewHolder(LayoutInflater.from(this.context).inflate(R.layout.row_contact_list, viewGroup, false));
    }
}
