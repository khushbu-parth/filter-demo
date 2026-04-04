package com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.PhoneModel;

import java.util.ArrayList;
import java.util.List;


public class ContactModel extends BaseModel {
    private String firstLetter;
    private boolean hasNumber;
    private String id;
    private String imgUri;
    private String name;
    private List<PhoneModel> numbers;
    private Bitmap userImage;
    private String displayNumber = "";
    private int colorCode = -1;
    private boolean userImagePresentChecked = false;
    private String searchAblePhoneNumbers = "";
    private boolean isSelected = false;

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public int getColorCode() {
        return this.colorCode;
    }

    public String getDisplayNumber() {
        return this.displayNumber;
    }

    public String getFirstLetter() {
        return this.firstLetter.toUpperCase();
    }

    public String getId() {
        return this.id;
    }

    public String getImgUri() {
        return this.imgUri;
    }

    public String getName() {
        return this.name;
    }

    public List<PhoneModel> getNumbers() {
        List<PhoneModel> list = this.numbers;
        if (list == null || list.isEmpty()) {
            Cursor query = BoloApplication.getApplication().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"data1", "data2"}, "contact_id = ?", new String[]{getId()}, null);
            while (query.moveToNext()) {
                String replace = query.getString(query.getColumnIndex("data1")).replace(" ", "");
                query.getString(query.getColumnIndex("data2")).replace(" ", "");
                if (this.numbers == null) {
                    this.numbers = new ArrayList();
                }
                if (!this.numbers.contains(replace)) {
                    PhoneModel phoneModel = new PhoneModel();
                    phoneModel.setCallNumber(replace);
                    this.numbers.add(phoneModel);
                }
            }
        }
        return this.numbers;
    }

    public String getSearchAblePhoneNumbers() {
        return this.searchAblePhoneNumbers;
    }

    public Bitmap getUserImage() {
        return this.userImage;
    }

    public boolean hasNumber() {
        return this.hasNumber;
    }

    public boolean isUserImagePresentChecked() {
        return this.userImagePresentChecked;
    }

    public void setColorCode(int i) {
        this.colorCode = i;
    }

    public void setDisplayNumber(String str) {
        this.displayNumber = str;
    }

    public void setFirstLetter(String str) {
        this.firstLetter = str;
    }

    public void setHasNumber(boolean z) {
        this.hasNumber = z;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setImgUri(String str) {
        this.imgUri = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setNumbers(List<PhoneModel> list) {
        this.numbers = list;
    }

    public void setSearchAblePhoneNumbers(String str) {
        this.searchAblePhoneNumbers = str;
    }

    public void setUserImage(Bitmap bitmap) {
        this.userImage = bitmap;
    }

    public void setUserImagePresentChecked(boolean z) {
        this.userImagePresentChecked = z;
    }
}
