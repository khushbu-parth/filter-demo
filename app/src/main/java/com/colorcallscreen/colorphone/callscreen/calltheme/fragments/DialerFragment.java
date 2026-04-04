package com.colorcallscreen.colorphone.callscreen.calltheme.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.CallLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.MainActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ContactAdapter;
import com.colorcallscreen.colorphone.callscreen.calltheme.module.AdLoad;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.CallLogUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactLoaderBuilder;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactPresenter;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactsView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;


public class DialerFragment extends Fragment implements View.OnClickListener, ContactsView, ContactAdapter.ClickModel {
    private static final int REQUEST_ADD_CONTACT = 197;
    AdLoad adLoad;
    BottomSheetDialog bottomSheetDialog;
    FrameLayout btnAddContact;
    FrameLayout btnBackRemove;
    FrameLayout btnCall;
    CardView btnEight;
    CardView btnFive;
    CardView btnFour;
    CardView btnHash;
    CardView btnNine;
    CardView btnOne;
    CardView btnSeven;
    CardView btnSix;
    CardView btnStar;
    CardView btnThree;
    CardView btnTwo;
    CardView btnZero;
    private ContactPresenter contactPresenter;
    private Thread filterListThread;
    private String number;
    TextView number_box;
    TextView txtNameContact;
    TextView viewMoreTxt;
    private List<ContactModel> contactModels = new ArrayList();
    private List<ContactModel> finalList = new ArrayList();

    
    public interface FilterListenr {
        void onListFiltered();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_dialer, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        TextView textView = (TextView) view.findViewById(R.id.number_box);
        this.number_box = textView;
        textView.setText("");
        this.txtNameContact = (TextView) view.findViewById(R.id.txtNameContact);
        this.viewMoreTxt = (TextView) view.findViewById(R.id.viewMoreTxt);
        CardView cardView = (CardView) view.findViewById(R.id.btnOne);
        this.btnOne = cardView;
        cardView.setOnClickListener(this);
        CardView cardView2 = (CardView) view.findViewById(R.id.btnTwo);
        this.btnTwo = cardView2;
        cardView2.setOnClickListener(this);
        CardView cardView3 = (CardView) view.findViewById(R.id.btnThree);
        this.btnThree = cardView3;
        cardView3.setOnClickListener(this);
        CardView cardView4 = (CardView) view.findViewById(R.id.btnFour);
        this.btnFour = cardView4;
        cardView4.setOnClickListener(this);
        CardView cardView5 = (CardView) view.findViewById(R.id.btnFive);
        this.btnFive = cardView5;
        cardView5.setOnClickListener(this);
        CardView cardView6 = (CardView) view.findViewById(R.id.btnSix);
        this.btnSix = cardView6;
        cardView6.setOnClickListener(this);
        CardView cardView7 = (CardView) view.findViewById(R.id.btnSeven);
        this.btnSeven = cardView7;
        cardView7.setOnClickListener(this);
        CardView cardView8 = (CardView) view.findViewById(R.id.btnEight);
        this.btnEight = cardView8;
        cardView8.setOnClickListener(this);
        CardView cardView9 = (CardView) view.findViewById(R.id.btnNine);
        this.btnNine = cardView9;
        cardView9.setOnClickListener(this);
        CardView cardView10 = (CardView) view.findViewById(R.id.btnStar);
        this.btnStar = cardView10;
        cardView10.setOnClickListener(this);
        CardView cardView11 = (CardView) view.findViewById(R.id.btnZero);
        this.btnZero = cardView11;
        cardView11.setOnClickListener(this);
        CardView cardView12 = (CardView) view.findViewById(R.id.btnHash);
        this.btnHash = cardView12;
        cardView12.setOnClickListener(this);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.btnAddContact);
        this.btnAddContact = frameLayout;
        frameLayout.setOnClickListener(this);
        FrameLayout frameLayout2 = (FrameLayout) view.findViewById(R.id.btnCall);
        this.btnCall = frameLayout2;
        frameLayout2.setOnClickListener(this);
        FrameLayout frameLayout3 = (FrameLayout) view.findViewById(R.id.btnBackRemove);
        this.btnBackRemove = frameLayout3;
        frameLayout3.setOnClickListener(this);
        contactbtnView();
        ContactLoaderBuilder contactLoaderBuilder = ContactLoaderBuilder.getInstance(getContext(), this);
        this.contactPresenter = contactLoaderBuilder;
        contactLoaderBuilder.loadContactsWithNumber();
        this.btnBackRemove.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.DialerFragment.1
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                DialerFragment.this.number_box.setText("");
                DialerFragment.this.contactbtnView();
                return true;
            }
        });
        this.btnZero.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.DialerFragment.2
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                DialerFragment.this.number_box.append("+");
                return true;
            }
        });
        this.txtNameContact.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.DialerFragment.3
            @Override 
            public void onClick(View view2) {
                if (DialerFragment.this.txtNameContact.getText().toString().length() > 0) {
                    DialerFragment.this.number_box.setText(((ContactModel) DialerFragment.this.contactModels.get(0)).getDisplayNumber());
                    DialerFragment.this.searchContacts();
                }
            }
        });
        this.viewMoreTxt.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.DialerFragment.4
            @Override 
            public void onClick(View view2) {
                DialerFragment.this.showBottomSheetDialogSearch();
            }
        });
    }

    
    public void showBottomSheetDialogSearch() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        this.bottomSheetDialog = bottomSheetDialog;
        bottomSheetDialog.setContentView(R.layout.bottom_dialog_callhistory);
        ((TextView) this.bottomSheetDialog.findViewById(R.id.txtTitleBottom)).setText("Contacts");
        ContactAdapter contactAdapter = new ContactAdapter(requireContext(), this.contactModels);
        contactAdapter.setListener(this);
        contactAdapter.query = this.number;
        contactAdapter.hasSearch = true;
        ((RecyclerView) this.bottomSheetDialog.findViewById(R.id.rvHistoryBottom)).setAdapter(contactAdapter);
        this.bottomSheetDialog.show();
    }

    @Override 
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnAddContact) { /* 2131361937 */
            Helper.addToContactIfExist(requireContext(), this.number_box.getText().toString().trim());
        } else if (id == R.id.btnBackRemove) { /* 2131361939 */
            if (this.number_box.getText().toString().length() > 0) {
                StringBuffer stringBuffer = new StringBuffer(this.number_box.getText().toString());
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                this.number_box.setText(stringBuffer);
                this.txtNameContact.setText("");
                this.viewMoreTxt.setText("");
            } else {
                this.txtNameContact.setText("");
                this.viewMoreTxt.setText("");
            }
        } else if (id == R.id.btnCall) { /* 2131361940 */
            if (this.number_box.getText().toString().trim().length() > 0) {
                CallLogUtils.makeCall(requireContext(), this.number_box.getText().toString().trim());
            } else {
                Cursor query = requireActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, "date DESC");
                int columnIndex = query.getColumnIndex("number");
                if (query.moveToFirst()) {
                    String string = query.getString(columnIndex);
                    Log.println(7, "phNum", string);
                    this.number_box.setText(string);
                }
                query.close();
            }
        } else if (id == R.id.btnEight) { /* 2131361943 */
            this.number_box.append("8");
        } else if (id == R.id.btnFive) { /* 2131361944 */
            this.number_box.append("5");
        } else if (id == R.id.btnFour) { /* 2131361945 */
            this.number_box.append("4");
        } else if (id == R.id.btnHash) { /* 2131361946 */
            this.number_box.append("#");
        } else if (id == R.id.btnNine) { /* 2131361948 */
            this.number_box.append("9");
        } else if (id == R.id.btnOne) { /* 2131361949 */
            this.number_box.append("1");
        } else if (id == R.id.btnSeven) { /* 2131361950 */
            this.number_box.append("7");
        } else if (id == R.id.btnSix) { /* 2131361951 */
            this.number_box.append("6");
        } else if (id == R.id.btnStar) { /* 2131361952 */
            this.number_box.append("*");
        } else if (id == R.id.btnThree) { /* 2131361954 */
            this.number_box.append("3");
        } else if (id == R.id.btnTwo) { /* 2131361955 */
            this.number_box.append("2");
        } else if (id == R.id.btnZero) { /* 2131361957 */
            this.number_box.append("0");
        }
        contactbtnView();
        searchContacts();
    }

    
    public void searchContacts() {
        String trim = this.number_box.getText().toString().trim();
        if (trim.isEmpty()) {
            return;
        }
        this.number = trim;
        loadContactsFromCache();
    }

    
    public void contactbtnView() {
        if (this.number_box.getText().toString().length() == 0) {
            this.btnAddContact.setVisibility(4);
        } else {
            this.btnAddContact.setVisibility(0);
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ContactAdapter.ClickModel
    public void onContactUserImageClicked(ContactModel contactModel, View view) {
        this.bottomSheetDialog.dismiss();
        Intent intent = new Intent(getContext(), CallLogDetailActivity.class);
        contactModel.setUserImage(null);
        contactModel.setUserImagePresentChecked(false);
        intent.putExtra("model", new Gson().toJson(contactModel));
        intent.putExtra("fromContact", true);
        startActivity(intent);
    }

    public void loadContactsFromCache() {
        try {
            if (ContextCompat.checkSelfPermission(getContext(), BoloPermission.READ_CONTACTS) != 0) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{BoloPermission.READ_CONTACTS}, 1);
                Log.e("Contact Permission", "Contact Permission Required for Searching");
                return;
            }
        } catch (Exception unused) {
        }
        if (this.finalList.size() == 0) {
            getContactPresenter().loadContactsWithNumber();
        }
        filterList(this.finalList, new FilterListenr() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.DialerFragment.5
            @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.DialerFragment.FilterListenr
            public void onListFiltered() {
                if (DialerFragment.this.contactModels.size() != 0) {
                    DialerFragment.this.txtNameContact.setText(((ContactModel) DialerFragment.this.contactModels.get(0)).getName());
                    if (DialerFragment.this.contactModels.size() > 1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("+");
                        sb.append(DialerFragment.this.contactModels.size() - 1);
                        sb.append(" more");
                        DialerFragment.this.viewMoreTxt.setText(sb);
                        return;
                    }
                    DialerFragment.this.viewMoreTxt.setText("");
                    return;
                }
                DialerFragment.this.txtNameContact.setText("");
                DialerFragment.this.viewMoreTxt.setText("");
            }
        });
    }

    private void filterList(final List<ContactModel> list, final FilterListenr filterListenr) {
        this.contactModels.clear();
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        Thread thread = this.filterListThread;
        if (thread != null) {
            thread.interrupt();
        }
        Thread thread2 = new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.DialerFragment.6
            @Override 
            public void run() {
                try {
                    for (ContactModel contactModel : list) {
                        if (DialerFragment.this.filterListThread != null && !DialerFragment.this.filterListThread.isInterrupted() && contactModel.getSearchAblePhoneNumbers().toLowerCase().contains(DialerFragment.this.number)) {
                            if (contactModel.getSearchAblePhoneNumbers().toLowerCase().startsWith(DialerFragment.this.number)) {
                                arrayList.add(contactModel);
                            } else {
                                arrayList2.add(contactModel);
                            }
                        }
                    }
                    DialerFragment.this.contactModels.addAll(arrayList);
                    DialerFragment.this.contactModels.addAll(arrayList2);
                } catch (Exception unused) {
                }
                if (DialerFragment.this.filterListThread == null || DialerFragment.this.filterListThread.isInterrupted()) {
                    return;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.DialerFragment.6.1
                    @Override 
                    public void run() {
                        filterListenr.onListFiltered();
                        DialerFragment.this.filterListThread = null;
                    }
                });
            }
        });
        this.filterListThread = thread2;
        thread2.start();
    }

    public ContactPresenter getContactPresenter() {
        if (this.contactPresenter == null) {
            this.contactPresenter = ContactLoaderBuilder.getInstance(getContext(), this);
        }
        return this.contactPresenter;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactsView
    public void onContactLoaded(List<ContactModel> list) {
        this.finalList.clear();
        this.finalList.addAll(list);
        filterList(list, new FilterListenr() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.fragments.DialerFragment.7
            @Override // com.colorcallscreen.colorphone.callscreen.calltheme.fragments.DialerFragment.FilterListenr
            public void onListFiltered() {
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == REQUEST_ADD_CONTACT && i2 == -1) {
            if (((MainActivity) requireActivity()) != null) {
                ((MainActivity) requireActivity()).refreshAllContactList();
            }
            this.txtNameContact.setText(intent.getStringExtra("dispName"));
            this.viewMoreTxt.setText("");
        }
    }
}
