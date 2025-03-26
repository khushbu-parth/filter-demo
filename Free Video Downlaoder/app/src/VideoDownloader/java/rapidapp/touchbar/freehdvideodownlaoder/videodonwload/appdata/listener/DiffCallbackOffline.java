package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.listener;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.DownloadingItem;


public class DiffCallbackOffline extends DiffUtil.Callback {
    private final List<DownloadingItem> newList;
    private final List<DownloadingItem> oldList;

    public DiffCallbackOffline(List<DownloadingItem> list, List<DownloadingItem> list2) {
        this.oldList = list;
        this.newList = list2;
    }

    public boolean areContentsTheSame(int i, int i2) {
        return this.oldList.size() == this.newList.size();
    }

    public boolean areItemsTheSame(int i, int i2) {
        return this.oldList.get(i).getFileId() == this.newList.get(i2).getFileId();
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
