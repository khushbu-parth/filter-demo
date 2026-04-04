package com.colorcallscreen.colorphone.callscreen.calltheme.models;

import android.graphics.Bitmap;
import android.net.Uri;


public final class ContactModel_Favorites {
    private String id;
    private String image;
    private String name;
    private String number;
    private Uri uri;
    private Bitmap userImage;
    private boolean isSelected = false;
    private int colorCode = -1;

    public Bitmap getUserImage() {
        return this.userImage;
    }

    public void setUserImage(Bitmap bitmap) {
        this.userImage = bitmap;
    }

    public int getColorCode() {
        return this.colorCode;
    }

    public void setColorCode(int i) {
        this.colorCode = i;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(String str) {
        this.name = str;
    }

    public final String getNumber() {
        return this.number;
    }

    public final void setNumber(String str) {
        this.number = str;
    }

    public final String getId() {
        return this.id;
    }

    public final void setId(String str) {
        this.id = str;
    }

    public final String getImage() {
        return this.image;
    }

    public final void setImage(String str) {
        this.image = str;
    }

    public final Uri getUri() {
        return this.uri;
    }

    public final void setUri(Uri uri) {
        this.uri = uri;
    }
}
