package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models;

import java.io.Serializable;

public class BookmarkItem implements Serializable {
    private int delete = 1;
    private String favicon = "";
    private int id;
    private String name = "";
    private int type = 0;
    private String url = "";

    public BookmarkItem() {
    }

    public BookmarkItem(int i, String str, String str2, String str3, int i2, int i3) {
        this.id = i;
        this.name = str;
        this.url = str2;
        this.favicon = str3;
        this.type = i2;
        this.delete = i3;
    }

    public String getFavicon() {
        return this.favicon;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public int getdelete() {
        return this.delete;
    }
}
