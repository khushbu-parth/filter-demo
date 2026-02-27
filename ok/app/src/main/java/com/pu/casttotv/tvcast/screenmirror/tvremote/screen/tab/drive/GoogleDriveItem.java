package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.drive;

import androidx.annotation.Keep;

@Keep
public class GoogleDriveItem {
    private String id;
    private boolean isImage;
    public boolean isSelected;
    private String mimeType;
    private String modifiedTime;
    private String name;
    private String parents;
    private String permissions;
    private Long size;
    private String thumbnailLink;
    private String version;
    private String webContentLink;
    private String webViewLink;

    public GoogleDriveItem() {
    }

    public GoogleDriveItem(String str, String str2, String str3, String str4, String str5, String str6, Long l, boolean z) {
        this.id = str;
        this.name = str2;
        this.mimeType = str3;
        this.thumbnailLink = str4;
        this.webViewLink = str5;
        this.webContentLink = str6;
        this.size = l;
        this.isImage = z;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getModifiedTime() {
        return this.modifiedTime;
    }

    public void setModifiedTime(String str) {
        this.modifiedTime = str;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String str) {
        this.mimeType = str;
    }

    public String getParents() {
        return this.parents;
    }

    public void setParents(String str) {
        this.parents = str;
    }

    public String getThumbnailLink() {
        return this.thumbnailLink;
    }

    public void setThumbnailLink(String str) {
        this.thumbnailLink = str;
    }

    public String getPermissions() {
        return this.permissions;
    }

    public void setPermissions(String str) {
        this.permissions = str;
    }

    public String getWebViewLink() {
        return this.webViewLink;
    }

    public void setWebViewLink(String str) {
        this.webViewLink = str;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String str) {
        this.version = str;
    }

    public String getWebContentLink() {
        return this.webContentLink;
    }

    public void setWebContentLink(String str) {
        this.webContentLink = str;
    }

    public void setSize(Long l) {
        this.size = l;
    }

    public Long getSize() {
        return this.size;
    }

    public boolean isImage() {
        return this.isImage;
    }

    public void setImage(boolean z) {
        this.isImage = z;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }
}
