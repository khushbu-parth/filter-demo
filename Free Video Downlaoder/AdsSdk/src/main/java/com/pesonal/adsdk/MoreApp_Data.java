package com.pesonal.adsdk;

public class MoreApp_Data {
    String app_id,app_name,app_packageName,app_logo,app_status;

    public MoreApp_Data(String app_id, String app_name, String app_packageName, String app_logo, String app_status) {
        this.app_id = app_id;
        this.app_name = app_name;
        this.app_packageName = app_packageName;
        this.app_logo = app_logo;
        this.app_status = app_status;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_packageName() {
        return app_packageName;
    }

    public void setApp_packageName(String app_packageName) {
        this.app_packageName = app_packageName;
    }

    public String getApp_logo() {
        return app_logo;
    }

    public void setApp_logo(String app_logo) {
        this.app_logo = app_logo;
    }

    public String getApp_status() {
        return app_status;
    }

    public void setApp_status(String app_status) {
        this.app_status = app_status;
    }
}
