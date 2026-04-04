package com.colorcallscreen.colorphone.callscreen.calltheme.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users")

public class UserPhoneTable {
    @DatabaseField(columnName = "phoneNumber", index = true)
    private String phoneNumber;
    @DatabaseField(columnName = "userId", generatedId = true)
    private int userId;
    @DatabaseField(canBeNull = true, columnName = "userImage", foreign = true, foreignAutoRefresh = true)
    private UserImageTable userImage;

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public int getUserId() {
        return this.userId;
    }

    public UserImageTable getUserImage() {
        return this.userImage;
    }

    public void setPhoneNumber(String str) {
        this.phoneNumber = str;
    }

    public void setUserId(int i) {
        this.userId = i;
    }

    public void setUserImage(UserImageTable userImageTable) {
        this.userImage = userImageTable;
    }
}
