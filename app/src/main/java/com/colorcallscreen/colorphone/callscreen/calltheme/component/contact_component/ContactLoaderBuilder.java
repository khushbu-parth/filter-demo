package com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;

import com.colorcallscreen.colorphone.callscreen.calltheme.models.PhoneModel;
import com.j256.ormlite.field.FieldType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class ContactLoaderBuilder implements ContactPresenter {
    private static ContactLoaderBuilder instance;
    private ContactsView contactsView;
    private Context context;
    private List<ContactModel> dataList;
    private boolean isContactLoadInProcess = false;

    public ContactLoaderBuilder(Context context, ContactsView contactsView) {
        this.context = context;
        this.contactsView = contactsView;
    }

    public static ContactLoaderBuilder getInstance(Context context, ContactsView contactsView) {
        if (instance == null) {
            instance = new ContactLoaderBuilder(context, contactsView);
        }
        if (contactsView != null) {
            ContactLoaderBuilder contactLoaderBuilder = instance;
            contactLoaderBuilder.context = context;
            contactLoaderBuilder.contactsView = contactsView;
        }
        return instance;
    }

    public boolean isContactExistAnsSetSearchableContact(ContactModel contactModel, String str) {
        String replaceAll = str.replaceAll("[^0-9]", "");
        if (contactModel.getSearchAblePhoneNumbers().contains(replaceAll)) {
            return true;
        }
        contactModel.setSearchAblePhoneNumbers(contactModel.getSearchAblePhoneNumbers() + "," + replaceAll);
        return false;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactPresenter
    public void clearContacts() {
        this.dataList = null;
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactPresenter
    public void loadContacts(boolean z) {
        List<ContactModel> list = this.dataList;
        if (list != null) {
            this.contactsView.onContactLoaded(list);
        } else if (this.isContactLoadInProcess) {
        } else {
            this.isContactLoadInProcess = true;
            final HashSet hashSet = new HashSet();
            final HashMap hashMap = new HashMap();
            new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactLoaderBuilder.1
                @Override 
                public void run() {
                    final ArrayList arrayList = new ArrayList();
                    Cursor query = ContactLoaderBuilder.this.context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"display_name", "data1", "data2", "has_phone_number", FieldType.FOREIGN_ID_FIELD_SUFFIX, "contact_id"}, null, null, "upper(display_name)");
                    if (query == null || query.getCount() <= 0) {
                        return;
                    }
                    while (query.moveToNext()) {
                        String string = query.getString(query.getColumnIndex("contact_id"));
                        String string2 = query.getString(query.getColumnIndex("display_name"));
                        String string3 = query.getString(query.getColumnIndex("data1"));
                        query.getString(query.getColumnIndex("data2"));
                        if (string3 != null) {
                            if (hashMap.containsKey(string)) {
                                ContactModel contactModel = (ContactModel) hashMap.get(string);
                                if (!ContactLoaderBuilder.this.isContactExistAnsSetSearchableContact(contactModel, string3)) {
                                    PhoneModel phoneModel = new PhoneModel();
                                    phoneModel.setCallNumber(string3);
                                    contactModel.getNumbers().add(phoneModel);
                                    hashSet.add(string3.replaceAll("[^0-9]", ""));
                                }
                            } else {
                                HashSet hashSet2 = hashSet;
                                if (hashSet2 == null || !hashSet2.contains(string3.replaceAll("[^0-9]", ""))) {
                                    ContactModel contactModel2 = new ContactModel();
                                    contactModel2.setId(string);
                                    contactModel2.setName(string2);
                                    contactModel2.setDisplayNumber(string3);
                                    if (query.getInt(query.getColumnIndex("has_phone_number")) > 0) {
                                        contactModel2.setHasNumber(true);
                                    } else {
                                        contactModel2.setHasNumber(false);
                                    }
                                    ArrayList arrayList2 = new ArrayList();
                                    PhoneModel phoneModel2 = new PhoneModel();
                                    phoneModel2.setCallNumber(string3);
                                    arrayList2.add(phoneModel2);
                                    ContactLoaderBuilder.this.isContactExistAnsSetSearchableContact(contactModel2, string3);
                                    contactModel2.setNumbers(arrayList2);
                                    if (contactModel2.getName() != null) {
                                        contactModel2.setFirstLetter(contactModel2.getName().substring(0, 1));
                                        arrayList.add(contactModel2);
                                        hashSet.add(string3.replaceAll("[^0-9]", ""));
                                        hashMap.put(string, contactModel2);
                                    }
                                }
                            }
                        }
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactLoaderBuilder.1.1
                        @Override 
                        public void run() {
                            ContactLoaderBuilder.this.dataList = arrayList;
                            ContactLoaderBuilder.this.contactsView.onContactLoaded(arrayList);
                            ContactLoaderBuilder.this.isContactLoadInProcess = false;
                        }
                    });
                }
            }).start();
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactPresenter
    public void loadContactsWithNumber() {
        List<ContactModel> list = this.dataList;
        if (list != null) {
            this.contactsView.onContactLoaded(list);
        } else {
            loadContacts(false);
        }
    }
}
