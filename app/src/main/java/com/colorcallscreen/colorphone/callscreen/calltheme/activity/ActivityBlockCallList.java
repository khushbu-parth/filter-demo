package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.control.ads.bannerAds.AperoBannerAdView;
import com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallBlockListAdapter;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ContactModel_Favorites;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.block.BlockHelper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;

import java.util.ArrayList;
import java.util.Iterator;


public class ActivityBlockCallList extends AppCompatActivity {
    public static String IS_ENABLE_BLOCKER = "_call_blocker";
    private CallBlockListAdapter callBlockListAdapter;
    AppCompatImageView ivAdd;
    AppCompatImageView ivBack;
    AppCompatImageView ivDelete;
    RecyclerView recyclerView;
    public TextView textView;
    ArrayList<ContactModel_Favorites> blockedArray = new ArrayList<>();
    public ArrayList<ContactModel_Favorites> selectedContacts = new ArrayList<>();
    public boolean isSelected = false;
    AperoBannerAdView androBannerAdView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_call_block_list);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        androBannerAdView = findViewById(R.id.bannerView);
        androBannerAdView.loadBanner(this, getString(R.string.admob_banner_id));
        AppCompatImageView appCompatImageView = (AppCompatImageView) findViewById(R.id.ivBack);
        this.ivBack = appCompatImageView;
        appCompatImageView.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ActivityBlockCallList.this.onBackPressed();
            }
        });
        AppCompatImageView appCompatImageView2 = (AppCompatImageView) findViewById(R.id.ivAdd);
        this.ivAdd = appCompatImageView2;
        appCompatImageView2.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ActivityBlockCallList.this.startActivityForResult(new Intent(ActivityBlockCallList.this, ActivityContactList.class), 1001);
            }
        });
        AppCompatImageView appCompatImageView3 = (AppCompatImageView) findViewById(R.id.ivDelete);
        this.ivDelete = appCompatImageView3;
        appCompatImageView3.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ActivityBlockCallList activityBlockCallList = ActivityBlockCallList.this;
                activityBlockCallList.showUnblockAllConfirmation(activityBlockCallList.selectedContacts);
            }
        });
        this.textView = (TextView) findViewById(R.id.unblock_text);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcv);
        this.recyclerView = recyclerView;
        recyclerView.setHasFixedSize(true);
        getBlockedContact();
    }


    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1001) {
            getBlockedContact();
        }
    }

    public static boolean isCallBlockEnabled() {
        return PreferenceUtils.getInstance().getBoolean(IS_ENABLE_BLOCKER, true);
    }

    private void showUnblockConfirmation(final String str, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.this_number_will_be_removed);
        builder.setPositiveButton(R.string.unblock, new DialogInterface.OnClickListener() {
            @Override 
            public void onClick(DialogInterface dialogInterface, int i2) {
                BlockHelper.removeFromBlockList(str, ActivityBlockCallList.this);
                ActivityBlockCallList.this.getBlockedContact();
            }
        });
        builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
        builder.show();
    }

    
    public void showUnblockAllConfirmation(ArrayList<ContactModel_Favorites> arrayList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to removed all selected numbers from Block List");
        builder.setPositiveButton(R.string.unblock, new AnonymousClass5(arrayList));
        builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
        builder.show();
    }
    
    public class AnonymousClass5 implements DialogInterface.OnClickListener {
        final ArrayList val$arrayList;

        AnonymousClass5(ArrayList arrayList) {
            this.val$arrayList = arrayList;
        }

        @Override 
        public void onClick(DialogInterface dialogInterface, int i) {
            Iterator it = this.val$arrayList.iterator();
            while (it.hasNext()) {
                final ContactModel_Favorites contactModel_Favorites = (ContactModel_Favorites) it.next();
                new Thread(new Runnable() {
                    @Override 
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override 
                            public void run() {
                                BlockHelper.removeFromBlockList(contactModel_Favorites.getNumber(), ActivityBlockCallList.this);
                                ActivityBlockCallList.this.selectedContacts.remove(contactModel_Favorites);
                                if (ActivityBlockCallList.this.selectedContacts.size() == 0) {
                                    ActivityBlockCallList.this.isSelected = false;
                                    ActivityBlockCallList.this.selectedContacts = new ArrayList<>();
                                    ActivityBlockCallList.this.onContactSelected(ActivityBlockCallList.this.selectedContacts);
                                    ActivityBlockCallList.this.getBlockedContact();
                                }
                            }
                        });
                    }
                }).start();
            }
        }
    }

    public void onContactSelected(ArrayList<ContactModel_Favorites> arrayList) {
        this.selectedContacts = arrayList;
        if (arrayList.size() > 0) {
            this.ivDelete.setEnabled(true);
            this.ivDelete.setVisibility(View.VISIBLE);
            this.ivAdd.setVisibility(View.GONE);
            return;
        }
        this.ivDelete.setEnabled(false);
        this.ivDelete.setVisibility(View.GONE);
        this.ivAdd.setVisibility(View.VISIBLE);
    }

    
    public void getBlockedContact() {
        ArrayList<ContactModel_Favorites> arrayList = new ArrayList<>();
        this.blockedArray = arrayList;
        arrayList.addAll(BlockHelper.listOfAllBlockedList(this));
        if (this.blockedArray.size() == 0) {
            this.textView.setVisibility(View.VISIBLE);
        } else {
            this.textView.setVisibility(View.GONE);
        }
        CallBlockListAdapter callBlockListAdapter = new CallBlockListAdapter(this, this.blockedArray);
        this.callBlockListAdapter = callBlockListAdapter;
        this.recyclerView.setAdapter(callBlockListAdapter);
    }

    public void unblockNumber(String str, int i) {
        showUnblockConfirmation(str, i);
    }

    @Override 
    public void onBackPressed() {
        if (this.ivDelete.getVisibility() == View.VISIBLE) {
            this.isSelected = false;
            ArrayList<ContactModel_Favorites> arrayList = new ArrayList<>();
            this.selectedContacts = arrayList;
            onContactSelected(arrayList);
            getBlockedContact();
            return;
        }
        finish();
    }
}
