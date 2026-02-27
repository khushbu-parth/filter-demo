package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

/* loaded from: classes4.dex */
public class downloadable_resource_model {
    private String Title;
    private String URL;
    private String file_size;
    private file_type file_type;
    public boolean isSelected;

    public downloadable_resource_model() {
    }

    public downloadable_resource_model(String str, String str2, file_type file_typeVar, String str3) {
        this.Title = str;
        this.URL = str2;
        this.file_type = file_typeVar;
        this.file_size = str3;
    }

    public String getTitle() {
        return this.Title;
    }

    public void setTitle(String str) {
        this.Title = str;
    }

    public String getURL() {
        return this.URL;
    }

    public file_type getFile_type() {
        return this.file_type;
    }

    public String getFile_size() {
        String str = this.file_size;
        if (str == null || str.equals("")) {
            return this.file_size;
        }
        try {
            return Utils.formatFileSize(Long.parseLong(this.file_size));
        } catch (Exception unused) {
            return this.file_size;
        }
    }
}
