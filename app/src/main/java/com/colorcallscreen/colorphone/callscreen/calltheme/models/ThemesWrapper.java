package com.colorcallscreen.colorphone.callscreen.calltheme.models;

import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseModel;
import java.util.List;


public class ThemesWrapper extends BaseModel {
    List<ThemeModel> themes;

    public List<ThemeModel> getThemes() {
        return this.themes;
    }

    public void setThemes(List<ThemeModel> list) {
        this.themes = list;
    }
}
