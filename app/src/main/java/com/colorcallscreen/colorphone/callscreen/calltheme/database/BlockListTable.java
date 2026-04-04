package com.colorcallscreen.colorphone.callscreen.calltheme.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

@DatabaseTable(tableName = "BlockListTable")

public class BlockListTable {
    @DatabaseField(columnName = "id", generatedId = true)
    private int blockId;
    @DatabaseField(columnName = "createdOn", index = true)
    private Date createdOn;
    @DatabaseField(columnName = "name")
    private String name = "";
    @DatabaseField(columnName = "phnNumber", index = true)
    private String phnNumber;

    public int getBlockId() {
        return this.blockId;
    }

    public Date getCreatedOn() {
        return this.createdOn;
    }

    public String getName() {
        return this.name;
    }

    public String getPhnNumber() {
        return this.phnNumber;
    }

    public void setBlockId(int i) {
        this.blockId = i;
    }

    public void setCreatedOn(Date date) {
        this.createdOn = date;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setPhnNumber(String str) {
        this.phnNumber = str;
    }
}
