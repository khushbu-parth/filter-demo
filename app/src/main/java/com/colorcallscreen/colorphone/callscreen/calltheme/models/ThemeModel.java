package com.colorcallscreen.colorphone.callscreen.calltheme.models;

import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;


public class ThemeModel extends BaseModel {
    public static final String BASE_IMAGE = "https://theme.vaniassistant.com/theme/";
    private String appliedTheme;
    private String category;
    private String contentType;
    private int defaultPerson;
    private String name;
    private int order;
    private String personImage;
    private String personName;
    private String personPhoneNumber;
    private String source;
    private String themeImage;
    private String thumbnail;
    private String gaCategory = Constants.ThemeCategory;
    private int defaultTheme = 0;
    private String color = "ffffff";
    private THEME_TYPE themeType = THEME_TYPE.ONLINE;

    
    public enum Source {
        ONLINE("online"),
        OFFLINE("offline");
        
        private String s;

        Source(String str) {
            this.s = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.s;
        }
    }

    
    public enum THEME_TYPE {
        DEFAULT("default"),
        CUSTOM("custom"),
        ONLINE("online");
        
        private String txt;

        THEME_TYPE(String str) {
            this.txt = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.txt;
        }
    }

    public String getAppliedTheme() {
        return this.appliedTheme;
    }

    public String getCategory() {
        return this.category;
    }

    public String getColor() {
        return this.color;
    }

    public String getContentType() {
        return this.contentType;
    }

    public int getDefaultPerson() {
        return this.defaultPerson;
    }

    public int getDefaultTheme() {
        return this.defaultTheme;
    }

    public String getGaCategory() {
        return this.gaCategory;
    }

    public String getName() {
        return this.name;
    }

    public int getOrder() {
        return this.order;
    }

    public String getPersonImage() {
        return this.personImage;
    }

    public String getPersonName() {
        return this.personName;
    }

    public String getPersonPhoneNumber() {
        return this.personPhoneNumber;
    }

    public String getSource() {
        return this.source;
    }

    public String getThemeImage() {
        return this.themeImage;
    }

    public THEME_TYPE getThemeType() {
        return this.themeType;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setAppliedTheme(String str) {
        this.appliedTheme = str;
    }

    public void setCategory(String str) {
        this.category = str;
    }

    public void setColor(String str) {
        this.color = str;
    }

    public void setContentType(String str) {
        this.contentType = str;
    }

    public void setDefaultPerson(int i) {
        this.defaultPerson = i;
    }

    public void setDefaultTheme(int i) {
        this.defaultTheme = i;
    }

    public void setGaCategory(String str) {
        this.gaCategory = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setOrder(int i) {
        this.order = i;
    }

    public void setPersonImage(String str) {
        this.personImage = str;
    }

    public void setPersonName(String str) {
        this.personName = str;
    }

    public void setPersonPhoneNumber(String str) {
        this.personPhoneNumber = str;
    }

    public void setSource(String str) {
        this.source = str;
    }

    public void setThemeImage(String str) {
        this.themeImage = str;
    }

    public void setThemeType(THEME_TYPE theme_type) {
        this.themeType = theme_type;
    }

    public void setThumbnail(String str) {
        this.thumbnail = str;
    }
}
