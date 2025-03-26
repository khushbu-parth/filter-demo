package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.listener;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.BookmarkItem;


public class DiffCallbackBookmarks extends DiffUtil.Callback {
    private final List<BookmarkItem> newList;
    private final List<BookmarkItem> oldList;

    public DiffCallbackBookmarks(List<BookmarkItem> list, List<BookmarkItem> list2) {
        this.oldList = list;
        this.newList = list2;
    }

    public boolean areContentsTheSame(int i, int i2) {
        return this.oldList.size() == this.newList.size();
    }

    public boolean areItemsTheSame(int i, int i2) {
        return this.oldList.get(i).getId() == this.newList.get(i2).getId();
    }

    public Object getChangePayload(int i, int i2) {
        return super.getChangePayload(i, i2);
    }

    public int getNewListSize() {
        return this.newList.size();
    }

    public int getOldListSize() {
        return this.oldList.size();
    }
}
