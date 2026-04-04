package com.colorcallscreen.colorphone.callscreen.calltheme.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.colorcallscreen.colorphone.callscreen.calltheme.service.block.BlockHelper;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogAction;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogActionBuilder;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.fragments.FavoriteFragment;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ContactModel_Favorites;
import com.colorcallscreen.colorphone.callscreen.calltheme.module.AdLoad;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.images.UsersImageHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.CallLogUtils;
import com.google.gson.Gson;

import java.util.ArrayList;


public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.Viewholder> {
    AdLoad adLoad;
    private ArrayList<ContactModel_Favorites> arrayList;
    public CallLogAction callLogAction;
    private Activity context;
    FavoriteFragment favoriteFragment;
    PopupWindow mypopupWindow;

    public FavoriteAdapter(Activity activity, ArrayList<ContactModel_Favorites> arrayList, FavoriteFragment favoriteFragment) {
        this.context = activity;
        this.arrayList = arrayList;
        this.favoriteFragment = favoriteFragment;
        this.callLogAction = new CallLogActionBuilder(this.context);
    }

    public final ArrayList<ContactModel_Favorites> getArrayList() {
        return this.arrayList;
    }

    public final void setArrayList(ArrayList<ContactModel_Favorites> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override 
    public Viewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Viewholder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_favorites, viewGroup, false));
    }

    @Override 
    public void onBindViewHolder(final Viewholder viewholder, int i) {
        String str;
        viewholder.setIsRecyclable(false);
        final ContactModel_Favorites contactModel_Favorites = this.arrayList.get(i);
        viewholder.tvName.setText(contactModel_Favorites.getName());
        viewholder.txtNumber.setText(contactModel_Favorites.getNumber());
        if (contactModel_Favorites.getColorCode() == -1) {
            int[] iArr = CallLogUtils.colors;
            contactModel_Favorites.setColorCode(iArr[i % iArr.length]);
        }
        viewholder.user_img.setImageResource(contactModel_Favorites.getColorCode());
        String[] split = contactModel_Favorites.getName().split(" ");
        if (split.length > 1) {
            try {
                str = split[0].substring(0, 1) + split[1].substring(0, 1) + "";
            } catch (Exception unused) {
                str = split[0].substring(0, 1) + "";
            }
        } else {
            str = split[0].substring(0, 1) + "";
        }
        viewholder.drawableTextView.setText(str);
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.FavoriteAdapter.1
            @Override 
            public void run() {
                final Bitmap userImageForPhoneNumber = UsersImageHandler.userImageForPhoneNumber(contactModel_Favorites.getNumber(), FavoriteAdapter.this.context);
                if (userImageForPhoneNumber != null) {
                    contactModel_Favorites.setUserImage(userImageForPhoneNumber);
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.FavoriteAdapter.1.1
                        @Override 
                        public void run() {
                            viewholder.user_img.setImageBitmap(userImageForPhoneNumber);
                            viewholder.drawableTextView.setVisibility(8);
                        }
                    });
                }
            }
        }).start();
        viewholder.llItemFavLayout.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.FavoriteAdapter.2
            @Override 
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(FavoriteAdapter.this.context, BoloPermission.PHONE_CALLS) != 0) {
                    ActivityCompat.requestPermissions(FavoriteAdapter.this.context, new String[]{BoloPermission.PHONE_CALLS}, 111);
                }
                try {
                    FavoriteAdapter.this.callLogAction.onCall(contactModel_Favorites.getNumber());
                } catch (Exception unused2) {
                }
            }
        });
        viewholder.iv_favouritestar.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.FavoriteAdapter.3
            @Override 
            public void onClick(View view) {
                FavoriteAdapter.this.removeFavorites(contactModel_Favorites);
            }
        });
        viewholder.ivMore.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.FavoriteAdapter.4
            @Override 
            public void onClick(View view) {
                FavoriteAdapter.this.showPopup(contactModel_Favorites);
                FavoriteAdapter.this.mypopupWindow.showAsDropDown(view, -20, -30);
            }
        });
    }

    
    public void removeFavorites(final ContactModel_Favorites contactModel_Favorites) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setMessage(R.string.remove_favorite);
        builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.FavoriteAdapter.5
            @Override 
            public void onClick(DialogInterface dialogInterface, int i) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("starred", (Integer) 0);
                FavoriteAdapter.this.context.getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, contentValues, "_id=" + contactModel_Favorites.getId(), null);
                FavoriteAdapter.this.arrayList.remove(contactModel_Favorites);
                if (FavoriteAdapter.this.arrayList.size() > 0) {
                    FavoriteAdapter.this.favoriteFragment.tvNoFound.setVisibility(8);
                } else {
                    FavoriteAdapter.this.favoriteFragment.tvNoFound.setVisibility(0);
                }
                FavoriteAdapter.this.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
        builder.show();
    }

    public void showPopup(final ContactModel_Favorites contactModel_Favorites) {
        View inflate = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.menu_more, (ViewGroup) null);
        ((ImageView) inflate.findViewById(R.id.ivCall)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.FavoriteAdapter.6
            @Override 
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(FavoriteAdapter.this.context, BoloPermission.PHONE_CALLS) != 0) {
                    ActivityCompat.requestPermissions(FavoriteAdapter.this.context, new String[]{BoloPermission.PHONE_CALLS}, 111);
                }
                try {
                    FavoriteAdapter.this.callLogAction.onCall(contactModel_Favorites.getNumber());
                } catch (Exception unused) {
                }
                FavoriteAdapter.this.mypopupWindow.dismiss();
            }
        });
        ((ImageView) inflate.findViewById(R.id.ivMsg)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.FavoriteAdapter.7
            @Override 
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(FavoriteAdapter.this.context, BoloPermission.PHONE_CALLS) != 0) {
                    ActivityCompat.requestPermissions(FavoriteAdapter.this.context, new String[]{BoloPermission.PHONE_CALLS}, 111);
                }
                try {
                    FavoriteAdapter.this.callLogAction.onMessage(contactModel_Favorites.getNumber());
                } catch (Exception unused) {
                }
                FavoriteAdapter.this.mypopupWindow.dismiss();
            }
        });
        ((AppCompatTextView) inflate.findViewById(R.id.txtCallDetails)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.FavoriteAdapter.8
            @Override 
            public void onClick(View view) {
                ContactModel contactModel = new ContactModel();
                contactModel.setId(contactModel_Favorites.getId());
                contactModel.setName(contactModel_Favorites.getName());
                contactModel.setDisplayNumber(contactModel_Favorites.getNumber());
                contactModel.setUserImage(contactModel_Favorites.getUserImage());
                contactModel.setColorCode(contactModel_Favorites.getColorCode());
                final Intent intent = new Intent(FavoriteAdapter.this.context, CallLogDetailActivity.class);
                intent.putExtra("model", new Gson().toJson(contactModel));
                intent.putExtra("fromContact", true);
                FavoriteAdapter.this.context.startActivity(intent);
                FavoriteAdapter.this.mypopupWindow.dismiss();
            }
        });
        ((AppCompatTextView) inflate.findViewById(R.id.txtCopy)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.FavoriteAdapter.9
            @Override 
            public void onClick(View view) {
                ((ClipboardManager) FavoriteAdapter.this.context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", contactModel_Favorites.getNumber()));
                Toast.makeText(FavoriteAdapter.this.context, "Number copied.", 0).show();
                FavoriteAdapter.this.mypopupWindow.dismiss();
            }
        });
        ((AppCompatTextView) inflate.findViewById(R.id.txtDelete)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.FavoriteAdapter.10
            @Override 
            public void onClick(View view) {
                FavoriteAdapter.this.removeFavorites(contactModel_Favorites);
                FavoriteAdapter.this.mypopupWindow.dismiss();
            }
        });
        AppCompatTextView appCompatTextView = (AppCompatTextView) inflate.findViewById(R.id.txtBlock);
        if (BlockHelper.isPhoneNumberInBlockedList(contactModel_Favorites.getNumber(), this.context)) {
            appCompatTextView.setText(this.context.getResources().getString(R.string.unblock));
        } else {
            appCompatTextView.setText(this.context.getResources().getString(R.string.block));
        }
        appCompatTextView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.FavoriteAdapter.11
            @Override 
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(FavoriteAdapter.this.context, BoloPermission.PHONE_CALLS) != 0) {
                    ActivityCompat.requestPermissions(FavoriteAdapter.this.context, new String[]{BoloPermission.PHONE_CALLS}, 111);
                }
                if (BlockHelper.isPhoneNumberInBlockedList(contactModel_Favorites.getNumber(), FavoriteAdapter.this.context)) {
                    FavoriteAdapter.this.callLogAction.onUnblock(contactModel_Favorites.getNumber());
                } else {
                    FavoriteAdapter.this.callLogAction.onBlock(contactModel_Favorites.getNumber());
                }
                FavoriteAdapter.this.mypopupWindow.dismiss();
            }
        });
        this.mypopupWindow = new PopupWindow(inflate, -2, -2, true);
    }

    @Override 
    public int getItemCount() {
        return this.arrayList.size();
    }

    
    public final class Viewholder extends RecyclerView.ViewHolder {
        public AppCompatTextView drawableTextView;
        private ImageView ivMore;
        private ImageView iv_favouritestar;
        private LinearLayout llItemFavLayout;
        private AppCompatTextView tvName;
        private AppCompatTextView txtNumber;
        public AppCompatImageView user_img;

        public Viewholder(View view) {
            super(view);
            this.tvName = (AppCompatTextView) view.findViewById(R.id.txtFavName);
            this.txtNumber = (AppCompatTextView) view.findViewById(R.id.txtNumber);
            this.drawableTextView = (AppCompatTextView) view.findViewById(R.id.drawableTextView);
            this.user_img = (AppCompatImageView) view.findViewById(R.id.user_img);
            this.iv_favouritestar = (ImageView) view.findViewById(R.id.iv_favouritestar);
            this.ivMore = (ImageView) view.findViewById(R.id.ivMore);
            this.llItemFavLayout = (LinearLayout) view.findViewById(R.id.llItemFavLayout);
        }
    }
}
