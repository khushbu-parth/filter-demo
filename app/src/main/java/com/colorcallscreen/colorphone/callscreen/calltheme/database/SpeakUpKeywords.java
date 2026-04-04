package com.colorcallscreen.colorphone.callscreen.calltheme.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "SpeakUpKeywords")

public class SpeakUpKeywords {
    @DatabaseField(columnName = "id", generatedId = true)
    private int keywordsId;
    @DatabaseField(columnName = "tag", index = true)
    private String tag;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "words")
    private String words;

    public int getKeywordsId() {
        return this.keywordsId;
    }

    public String getTag() {
        return this.tag;
    }

    public String getTitle() {
        return this.title;
    }

    public String getWords() {
        return this.words;
    }

    public void setKeywordsId(int i) {
        this.keywordsId = i;
    }

    public void setTag(String str) {
        this.tag = str;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public void setWords(String str) {
        this.words = str;
    }
}
