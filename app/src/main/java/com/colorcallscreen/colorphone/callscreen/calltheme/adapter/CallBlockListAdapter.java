package com.colorcallscreen.colorphone.callscreen.calltheme.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityBlockCallList;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ContactModel_Favorites;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.images.UsersImageHandler;
import java.util.ArrayList;
import java.util.Random;


public class CallBlockListAdapter extends RecyclerView.Adapter<CallBlockListAdapter.MyViewHolder> {
    private Context context;
    public ArrayList<ContactModel_Favorites> numberList;
    private ArrayList<ContactModel_Favorites> selectedContact;

    
    public class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView drawableTextView;
        CheckBox rdSelect;
        AppCompatTextView txtName;
        AppCompatTextView txtNumber;
        AppCompatImageView user_img;

        public MyViewHolder(View view) {
            super(view);
            this.user_img = (AppCompatImageView) view.findViewById(R.id.user_img);
            this.drawableTextView = (AppCompatTextView) view.findViewById(R.id.drawableTextView);
            this.txtNumber = (AppCompatTextView) view.findViewById(R.id.txtNumber);
            this.txtName = (AppCompatTextView) view.findViewById(R.id.txtName);
            this.rdSelect = (CheckBox) view.findViewById(R.id.rdSelect);
        }
    }

    public CallBlockListAdapter(Context context, ArrayList<ContactModel_Favorites> arrayList) {
        this.selectedContact = new ArrayList<>();
        this.context = context;
        this.numberList = arrayList;
        this.selectedContact = new ArrayList<>();
    }

    public void updateList(ArrayList<ContactModel_Favorites> arrayList) {
        this.numberList = arrayList;
        notifyDataSetChanged();
    }

    @Override 
    public int getItemCount() {
        return this.numberList.size();
    }

    @Override 
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        String number;
        String str;
        final ContactModel_Favorites contactModel_Favorites = this.numberList.get(i);
        if (contactModel_Favorites != null) {
            myViewHolder.txtNumber.setText(contactModel_Favorites.getNumber());
        }
        if (((ActivityBlockCallList) this.context).isSelected) {
            myViewHolder.rdSelect.setVisibility(0);
        }
        if (contactModel_Favorites.isSelected()) {
            myViewHolder.rdSelect.setChecked(true);
        } else {
            myViewHolder.rdSelect.setChecked(false);
        }
        if (contactModel_Favorites.getName() != null && !contactModel_Favorites.getName().equals("")) {
            number = contactModel_Favorites.getName();
        } else {
            number = contactModel_Favorites.getNumber();
        }
        myViewHolder.txtName.setText(number);
        Random random = new Random();
        int argb = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(argb);
        gradientDrawable.setShape(0);
        myViewHolder.user_img.setBackground(gradientDrawable);
        String[] split = number.split(" ");
        if (split.length > 1) {
            try {
                str = split[0].substring(0, 1) + split[1].substring(0, 1) + "";
            } catch (Exception unused) {
                str = split[0].substring(0, 1) + "";
            }
        } else {
            str = split[0].substring(0, 1) + "";
        }
        myViewHolder.drawableTextView.setText(str);
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallBlockListAdapter.1
            @Override 
            public void run() {
                final Bitmap userImageForPhoneNumber = UsersImageHandler.userImageForPhoneNumber(contactModel_Favorites.getNumber(), CallBlockListAdapter.this.context);
                if (userImageForPhoneNumber != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallBlockListAdapter.1.1
                        @Override 
                        public void run() {
                            myViewHolder.user_img.setImageBitmap(userImageForPhoneNumber);
                            myViewHolder.drawableTextView.setVisibility(8);
                        }
                    });
                }
            }
        }).start();
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallBlockListAdapter.2
            @Override 
            public void onClick(View view) {
                if (((ActivityBlockCallList) CallBlockListAdapter.this.context).isSelected) {
                    CallBlockListAdapter.this.selectItem(myViewHolder, contactModel_Favorites);
                } else {
                    ((ActivityBlockCallList) CallBlockListAdapter.this.context).unblockNumber(CallBlockListAdapter.this.numberList.get(i).getNumber(), i);
                }
            }
        });
        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallBlockListAdapter.3
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                CallBlockListAdapter.this.selectItem(myViewHolder, contactModel_Favorites);
                if (!((ActivityBlockCallList) CallBlockListAdapter.this.context).isSelected) {
                    ((ActivityBlockCallList) CallBlockListAdapter.this.context).isSelected = true;
                    CallBlockListAdapter.this.notifyDataSetChanged();
                }
                return true;
            }
        });
    }

    
    public void selectItem(MyViewHolder myViewHolder, ContactModel_Favorites contactModel_Favorites) {
        if (myViewHolder.rdSelect.isChecked()) {
            myViewHolder.rdSelect.setChecked(false);
            contactModel_Favorites.setSelected(false);
            this.selectedContact.remove(contactModel_Favorites);
        } else {
            myViewHolder.rdSelect.setChecked(true);
            contactModel_Favorites.setSelected(true);
            this.selectedContact.add(contactModel_Favorites);
        }
        ((ActivityBlockCallList) this.context).onContactSelected(this.selectedContact);
    }

    @Override 
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.row_call_block, viewGroup, false));
    }
}
