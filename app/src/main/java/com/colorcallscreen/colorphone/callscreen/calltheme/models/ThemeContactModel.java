package com.colorcallscreen.colorphone.callscreen.calltheme.models;

import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseModel;


public class ThemeContactModel extends BaseModel {
    private int colorCode = -1;
    private boolean hasNumber;
    private String id;
    private boolean isSelected;
    private String name;
    private String number;

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getNumber() {
        return this.number;
    }

    public int getColorCode() {
        return this.colorCode;
    }

    public void setColorCode(int i) {
        this.colorCode = i;
    }

    public void setNumber(String str) {
        this.number = str;
    }

    public boolean hasNumber() {
        return this.hasNumber;
    }

    public boolean isHasNumber() {
        return this.hasNumber;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setHasNumber(boolean z) {
        this.hasNumber = z;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }
}
