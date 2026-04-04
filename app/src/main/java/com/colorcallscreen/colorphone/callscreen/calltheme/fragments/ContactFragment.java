package com.colorcallscreen.colorphone.callscreen.calltheme.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ContactAdapter;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactLoaderBuilder;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactPresenter;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactsView;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class ContactFragment extends Fragment implements ContactsView, ContactAdapter.ClickModel {
    private BroadcastReceiver callReceiver;
    private ContactAdapter contactAdapter;
    private ContactPresenter contactPresenter;
    private Thread filterListThread;
    AppCompatImageView ivClose;
    AppCompatImageView ivSearch;
    private RecyclerView rVContacts;
    private TextView tvNoPermission;
    AppCompatEditText tvSearch;
    List<ContactModel> contactList = new ArrayList();
    private String queryStr = "";
    List<ContactModel> allContactList = new ArrayList();

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == BoloPermission.DEVICE_SETTING_REQ && ContextCompat.checkSelfPermission(getContext(), BoloPermission.READ_CONTACTS) == 0 && ContextCompat.checkSelfPermission(getContext(), BoloPermission.WRITE_CONTACTS) == 0) {
            loadContactFresh();
            this.tvNoPermission.setVisibility(8);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        try {
            if (this.callReceiver != null) {
                LocalBroadcastManager.getInstance(BoloApplication.getApplication()).unregisterReceiver(this.callReceiver);
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ContactAdapter.ClickModel
    public void onContactUserImageClicked(final ContactModel contactModel, View view) {
        Intent intent = new Intent(ContactFragment.this.getContext(), CallLogDetailActivity.class);
        contactModel.setUserImage(null);
        contactModel.setUserImagePresentChecked(false);
        intent.putExtra("model", new Gson().toJson(contactModel));
        intent.putExtra("fromContact", true);
        ContactFragment.this.startActivity(intent);
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactsView
    public void onContactLoaded(List<ContactModel> list) {
        this.allContactList = list;
        searchContactsForQuery(this.queryStr, list);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_contact, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (this.callReceiver == null) {
            this.callReceiver = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.ContactFragment.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    String stringExtra;
                    if (intent.getAction().equalsIgnoreCase("isEdited")) {
                        ContactLoaderBuilder.getInstance(ContactFragment.this.requireContext(), null).clearContacts();
                        ContactFragment.this.loadContactFresh();
                    }
                    if (!intent.getAction().equalsIgnoreCase("isDeleted") || (stringExtra = intent.getStringExtra("phoneNumber")) == null || stringExtra.isEmpty()) {
                        return;
                    }
                    for (ContactModel contactModel : ContactFragment.this.allContactList) {
                        if (contactModel.getDisplayNumber().equals(stringExtra)) {
                            ContactFragment.this.allContactList.remove(contactModel);
                            ContactFragment contactFragment = ContactFragment.this;
                            contactFragment.searchContactsForQuery(contactFragment.queryStr, ContactFragment.this.allContactList);
                            return;
                        }
                    }
                }
            };
        }
        LocalBroadcastManager.getInstance(BoloApplication.getApplication()).registerReceiver(this.callReceiver, new IntentFilter("isEdited"));
        LocalBroadcastManager.getInstance(BoloApplication.getApplication()).registerReceiver(this.callReceiver, new IntentFilter("isDeleted"));
        this.ivSearch = (AppCompatImageView) view.findViewById(R.id.ivSearch);
        this.ivClose = (AppCompatImageView) view.findViewById(R.id.ivClose);
        AppCompatEditText appCompatEditText = (AppCompatEditText) view.findViewById(R.id.tvSearch);
        this.tvSearch = appCompatEditText;
        appCompatEditText.addTextChangedListener(new TextWatcher() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.ContactFragment.3
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (editable.length() <= 0) {
                    ContactFragment.this.queryStr = "";
                    ContactFragment.this.contactAdapter.hasSearch = false;
                    ContactFragment.this.contactAdapter.query = null;
                    ContactFragment contactFragment = ContactFragment.this;
                    contactFragment.searchContactsForQuery(contactFragment.queryStr, ContactFragment.this.allContactList);
                    ContactFragment.this.ivClose.setVisibility(8);
                    Utility.hideSoftKeyboard(ContactFragment.this.requireActivity());
                    ContactFragment.this.tvSearch.clearFocus();
                    return;
                }
                ContactFragment.this.queryStr = editable.toString();
                Log.println(7, "log===", ContactFragment.this.queryStr);
                if (ContactFragment.this.contactAdapter != null) {
                    ContactFragment.this.contactAdapter.query = ContactFragment.this.queryStr;
                    ContactFragment.this.contactAdapter.hasSearch = true;
                    ContactFragment contactFragment2 = ContactFragment.this;
                    contactFragment2.searchContactsForQuery(contactFragment2.queryStr, ContactFragment.this.allContactList);
                }
                ContactFragment.this.ivClose.setVisibility(0);
            }
        });
        this.ivClose.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.ContactFragment.4
            @Override 
            public void onClick(View view2) {
                ContactFragment.this.tvSearch.setText("");
                Utility.hideSoftKeyboard(ContactFragment.this.requireActivity());
                ContactFragment.this.ivClose.setVisibility(8);
                ContactFragment.this.tvSearch.clearFocus();
            }
        });
        this.tvNoPermission = (TextView) view.findViewById(R.id.tv_no_permissiom);
        final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService("input_method");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rVContacts);
        this.rVContacts = recyclerView;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.ContactFragment.5
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView2, int i) {
                super.onScrollStateChanged(recyclerView2, i);
                if (ContactFragment.this.getActivity() != null && inputMethodManager.isActive()) {
                    Utility.hideSoftKeyboard(ContactFragment.this.getActivity());
                }
            }
        });
        this.tvNoPermission.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.ContactFragment.6
            @Override 
            public void onClick(View view2) {
                BoloPermission.openApplicationSetting(ContactFragment.this);
            }
        });
        if (ContextCompat.checkSelfPermission(getContext(), BoloPermission.READ_CONTACTS) != 0 && ContextCompat.checkSelfPermission(getContext(), BoloPermission.WRITE_CONTACTS) != 0) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{BoloPermission.READ_CONTACTS, BoloPermission.WRITE_CONTACTS}, 104);
            this.tvNoPermission.setVisibility(0);
            return;
        }
        loadContactFresh();
    }

    public void loadContactFresh() {
        this.contactAdapter = null;
        ContactLoaderBuilder contactLoaderBuilder = ContactLoaderBuilder.getInstance(getContext(), this);
        this.contactPresenter = contactLoaderBuilder;
        contactLoaderBuilder.loadContacts(false);
    }

    public void onFilterCompleted(List<ContactModel> list) {
        this.contactList.clear();
        this.contactList.addAll(list);
        ContactAdapter contactAdapter = this.contactAdapter;
        if (contactAdapter == null) {
            ContactAdapter contactAdapter2 = new ContactAdapter(requireContext(), this.contactList);
            this.contactAdapter = contactAdapter2;
            contactAdapter2.setListener(this);
            this.rVContacts.setAdapter(this.contactAdapter);
            return;
        }
        contactAdapter.updateList(this.contactList);
    }

    @Override // androidx.fragment.app.Fragment
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (this.tvNoPermission == null) {
            return;
        }
        if (ContextCompat.checkSelfPermission(getContext(), BoloPermission.READ_CONTACTS) != 0) {
            this.tvNoPermission.setVisibility(0);
            return;
        }
        loadContactFresh();
        this.tvNoPermission.setVisibility(8);
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
            Thread thread2 = new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.ContactFragment.7
                @Override 
                public void run() {
                    for (ContactModel contactModel : list) {
                        if (ContactFragment.this.filterListThread == null || ContactFragment.this.filterListThread.isInterrupted()) {
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
                    if (ContactFragment.this.filterListThread == null || ContactFragment.this.filterListThread.isInterrupted()) {
                        return;
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.ContactFragment.7.1
                        @Override 
                        public void run() {
                            ContactFragment.this.filterListThread = null;
                            if (arrayList3.isEmpty() && lowerCase.isEmpty()) {
                                arrayList3.addAll(list);
                            }
                            ContactFragment.this.onFilterCompleted(arrayList3);
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
}
