package com.colorcallscreen.colorphone.callscreen.calltheme.database;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

@DatabaseTable(tableName = "UserImages")

public class UserImageTable {
    @DatabaseField(columnName = "createdOn")
    public Date createdOn = new Date();
    @DatabaseField(columnName = "image", dataType = DataType.BYTE_ARRAY)
    public byte[] image;
    @DatabaseField(columnName = "imageId", generatedId = true)
    private int imageId;

    public Date getCreatedOn() {
        return this.createdOn;
    }

    public byte[] getImage() {
        return this.image;
    }

    public int getImageId() {
        return this.imageId;
    }

    public void setCreatedOn(Date date) {
        this.createdOn = date;
    }

    public void setImage(byte[] bArr) {
        this.image = bArr;
    }

    public void setImageId(int i) {
        this.imageId = i;
    }
}
