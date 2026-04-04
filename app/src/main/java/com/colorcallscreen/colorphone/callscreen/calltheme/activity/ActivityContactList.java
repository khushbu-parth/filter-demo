package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.colorcallscreen.colorphone.callscreen.calltheme.adapter.BlockContactAdapter;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactLoaderBuilder;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactPresenter;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactsView;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.PhoneModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.block.BlockHelper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ActivityContactList extends AppCompatActivity implements ContactsView, BlockContactAdapter.ClickModel {
    private BlockContactAdapter contactAdapter;
    private ContactPresenter contactPresenter;
    private Thread filterListThread;
    AppCompatImageView ivBack;
    AppCompatImageView ivBlock;
    AppCompatImageView ivClose;
    AppCompatImageView ivSearch;
    private RecyclerView rVContacts;
    private TextView tvNoPermission;
    AppCompatEditText tvSearch;
    List<ContactModel> contactList = new ArrayList();
    private String queryStr = "";
    public boolean isSelected = false;
    private ArrayList<ContactModel> selectedContact = new ArrayList<>();
    List<ContactModel> allContactList = new ArrayList();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_contactlist);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        this.ivSearch = (AppCompatImageView) findViewById(R.id.ivSearch);
        this.ivClose = (AppCompatImageView) findViewById(R.id.ivClose);
        AppCompatImageView appCompatImageView = (AppCompatImageView) findViewById(R.id.ivBack);
        this.ivBack = appCompatImageView;
        appCompatImageView.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ActivityContactList.this.onBackPressed();
            }
        });
        AppCompatImageView appCompatImageView2 = (AppCompatImageView) findViewById(R.id.ivAddBlock);
        this.ivBlock = appCompatImageView2;
        appCompatImageView2.setOnClickListener(new AnonymousClass2());
        AppCompatEditText appCompatEditText = (AppCompatEditText) findViewById(R.id.tvSearch);
        this.tvSearch = appCompatEditText;
        appCompatEditText.addTextChangedListener(new TextWatcher() {
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (editable.length() <= 0) {
                    ActivityContactList.this.queryStr = "";
                    ActivityContactList.this.contactAdapter.hasSearch = false;
                    ActivityContactList.this.contactAdapter.query = null;
                    ActivityContactList activityContactList = ActivityContactList.this;
                    activityContactList.searchContactsForQuery(activityContactList.queryStr, ActivityContactList.this.allContactList);
                    ActivityContactList.this.ivClose.setVisibility(View.GONE);
                    Utility.hideSoftKeyboard(ActivityContactList.this);
                    ActivityContactList.this.tvSearch.clearFocus();
                    return;
                }
                ActivityContactList.this.queryStr = editable.toString();
                if (ActivityContactList.this.contactAdapter != null) {
                    ActivityContactList.this.contactAdapter.query = ActivityContactList.this.queryStr;
                    ActivityContactList.this.contactAdapter.hasSearch = true;
                    ActivityContactList activityContactList2 = ActivityContactList.this;
                    activityContactList2.searchContactsForQuery(activityContactList2.queryStr, ActivityContactList.this.allContactList);
                }
                ActivityContactList.this.ivClose.setVisibility(View.VISIBLE);
            }
        });
        this.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ActivityContactList.this.tvSearch.setText("");
                Utility.hideSoftKeyboard(ActivityContactList.this);
                ActivityContactList.this.ivClose.setVisibility(View.GONE);
                ActivityContactList.this.tvSearch.clearFocus();
            }
        });
        this.tvNoPermission = (TextView) findViewById(R.id.tv_no_permissiom);
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService("input_method");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rVContacts);
        this.rVContacts = recyclerView;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityContactList.5
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView2, int i) {
                super.onScrollStateChanged(recyclerView2, i);
                if (inputMethodManager.isActive()) {
                    Utility.hideSoftKeyboard(ActivityContactList.this);
                }
            }
        });
        if (ContextCompat.checkSelfPermission(this, BoloPermission.READ_CONTACTS) != 0 && ContextCompat.checkSelfPermission(this, BoloPermission.WRITE_CONTACTS) != 0) {
            ActivityCompat.requestPermissions(this, new String[]{BoloPermission.READ_CONTACTS, BoloPermission.WRITE_CONTACTS}, 104);
            this.tvNoPermission.setVisibility(View.VISIBLE);
            return;
        }
        loadContactFresh();
    }
    
    class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override 
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityContactList.this, R.style.MyAlertDialogTheme2);
            builder.setTitle(ActivityContactList.this.getString(R.string.block_this_caller));
            builder.setMessage(ActivityContactList.this.getString(R.string.block_info));
            builder.setPositiveButton(ActivityContactList.this.getString(R.string.block), new AnonymousClass1());
            builder.setNegativeButton(ActivityContactList.this.getString(R.string.cancel), (DialogInterface.OnClickListener) null);
            builder.show();
        }
        
        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }
            
            class RunnableC00121 implements Runnable {
                final /* synthetic */ ContactModel val$model;

                RunnableC00121(ContactModel contactModel) {
                    this.val$model = contactModel;
                }

                @Override 
                public void run() {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override 
                        public void run() {
                            ActivityContactList.this.addBlockList(RunnableC00121.this.val$model);
                            ActivityContactList.this.selectedContact.remove(RunnableC00121.this.val$model);
                            if (ActivityContactList.this.selectedContact.size() == 0) {
                                        ActivityContactList.this.refreshAllContactList();
                            }
                        }
                    });
                }
            }

            @Override 
            public void onClick(DialogInterface dialogInterface, int i) {
                Iterator it = ActivityContactList.this.selectedContact.iterator();
                while (it.hasNext()) {
                    new Thread(new RunnableC00121((ContactModel) it.next())).start();
                }
            }
        }
    }

    @Override
    public void onContactUserImageClicked(final ContactModel contactModel, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme2);
        builder.setTitle(getString(R.string.block_this_caller));
        builder.setMessage(getString(R.string.block_info));
        builder.setPositiveButton(getString(R.string.block), new DialogInterface.OnClickListener() {
            @Override 
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityContactList.this.addBlockList(contactModel);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), (DialogInterface.OnClickListener) null);
        builder.show();
    }

    
    public void addBlockList(ContactModel contactModel) {
        if (contactModel.getNumbers().size() > 0) {
            for (PhoneModel phoneModel : contactModel.getNumbers()) {
                BlockHelper.addToBlockList(phoneModel.getCallNumber(), this);
            }
            return;
        }
        BlockHelper.addToBlockList(contactModel.getDisplayNumber(), this);
    }

    public void loadContactFresh() {
        this.contactAdapter = null;
        ContactLoaderBuilder contactLoaderBuilder = ContactLoaderBuilder.getInstance(this, this);
        this.contactPresenter = contactLoaderBuilder;
        contactLoaderBuilder.loadContacts(true);
    }

    public void onFilterCompleted(List<ContactModel> list) {
        this.contactList.clear();
        this.contactList.addAll(list);
        BlockContactAdapter blockContactAdapter = this.contactAdapter;
        if (blockContactAdapter == null) {
            BlockContactAdapter blockContactAdapter2 = new BlockContactAdapter(this, this.contactList);
            this.contactAdapter = blockContactAdapter2;
            blockContactAdapter2.setListener(this);
            this.rVContacts.setAdapter(this.contactAdapter);
            return;
        }
        blockContactAdapter.updateList(this.contactList);
    }

    public void searchContactsForQuery(String str, final List<ContactModel> list) {
        final String lowerCase = str.toLowerCase();
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        final ArrayList arrayList3 = new ArrayList();
        if (!lowerCase.isEmpty() && !list.isEmpty()) {
            Thread thread = this.filterListThread;
            if (thread != null) {
                thread.interrupt();
            }
            Thread thread2 = new Thread(new Runnable() {
                @Override 
                public void run() {
                    for (ContactModel contactModel : list) {
                        if (ActivityContactList.this.filterListThread == null || ActivityContactList.this.filterListThread.isInterrupted()) {
                            return;
                        }
                        if (contactModel.getName().toLowerCase().contains(lowerCase)) {
                            if (contactModel.getName().toLowerCase().startsWith(lowerCase)) {
                                arrayList.add(contactModel);
                            } else {
                                arrayList2.add(contactModel);
                            }
                        } else if (contactModel.getSearchAblePhoneNumbers().toLowerCase().contains(lowerCase)) {
                            if (contactModel.getSearchAblePhoneNumbers().toLowerCase().startsWith(lowerCase)) {
                                arrayList.add(contactModel);
                            } else {
                                arrayList2.add(contactModel);
                            }
                        }
                    }
                    arrayList3.addAll(arrayList);
                    arrayList3.addAll(arrayList2);
                    if (ActivityContactList.this.filterListThread == null || ActivityContactList.this.filterListThread.isInterrupted()) {
                        return;
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override 
                        public void run() {
                            ActivityContactList.this.filterListThread = null;
                            if (arrayList3.isEmpty() && lowerCase.isEmpty()) {
                                arrayList3.addAll(list);
                            }
                            ActivityContactList.this.onFilterCompleted(arrayList3);
                        }
                    });
                }
            });
            this.filterListThread = thread2;
            thread2.start();
            return;
        }
        onFilterCompleted(list);
    }

    public void onContactSelected(ArrayList<ContactModel> arrayList) {
        this.selectedContact = arrayList;
        if (arrayList.size() > 0) {
            this.ivBlock.setVisibility(View.VISIBLE);
        } else {
            this.ivBlock.setVisibility(View.GONE);
        }
    }

    
    public void refreshAllContactList() {
        this.isSelected = false;
        ArrayList<ContactModel> arrayList = new ArrayList<>();
        this.selectedContact = arrayList;
        onContactSelected(arrayList);
        ContactLoaderBuilder.getInstance(this, null).clearContacts();
        loadContactFresh();
    }

    @Override 
    public void onBackPressed() {
        if (this.isSelected) {
            refreshAllContactList();
            return;
        }
        setResult(-1, new Intent());
        finish();
    }

    @Override
    public void onContactLoaded(List<ContactModel> list) {
        this.allContactList = list;
        searchContactsForQuery(this.queryStr, list);
    }
}
