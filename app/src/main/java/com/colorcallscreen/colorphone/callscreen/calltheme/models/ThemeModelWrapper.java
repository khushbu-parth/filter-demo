package com.colorcallscreen.colorphone.callscreen.calltheme.models;

import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseModel;


public class ThemeModelWrapper extends BaseModel {
    ThemesWrapper data;
    boolean status;

    public ThemesWrapper getData() {
        return this.data;
    }

    public boolean isStatus() {
        return this.status;
    }

    public void setData(ThemesWrapper themesWrapper) {
        this.data = themesWrapper;
    }

    public void setStatus(boolean z) {
        this.status = z;
    }
}
