package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models;

import java.io.Serializable;

public class DownloadingItem implements Serializable {
    private double currentSize = 0.0d;
    private int downloadId = 0;
    public int fileId = 0;
    private String icon = "";
    private int isInPause = 0;
    private String lastModification = "";
    private String localFilePath = "";
    private String name = "";
    private int percent = 0;
    private int progress = 0;
    private int speed = 0;
    private double totalSize = 0.0d;
    private String url = "";

    public DownloadingItem() {
    }

    public DownloadingItem(int i, String str, String str2, String str3, int i2, double d, double d2, int i3, int i4, int i5, int i6, String str4, String str5) {
        this.fileId = i;
        this.name = str;
        this.icon = str2;
        this.url = str3;
        this.downloadId = i2;
        this.totalSize = d;
        this.currentSize = d2;
        this.percent = i3;
        this.progress = i4;
        this.speed = i5;
        this.isInPause = i6;
        this.localFilePath = str4;
        this.lastModification = str5;
    }

    public double getCurrentSize() {
        return this.currentSize;
    }

    public int getDownloadId() {
        return this.downloadId;
    }

    public int getFileId() {
        return this.fileId;
    }

    public String getIcon() {
        return this.icon;
    }

    public int getIsInPause() {
        return this.isInPause;
    }

    public String getLastModification() {
        return this.lastModification;
    }

    public String getLocalFilePath() {
        return this.localFilePath;
    }

    public String getName() {
        return this.name;
    }

    public int getPercent() {
        return this.percent;
    }

    public int getProgress() {
        return this.progress;
    }

    public int getSpeed() {
        return this.speed;
    }

    public double getTotalSize() {
        return this.totalSize;
    }

    public String getUrl() {
        return this.url;
    }

    public void setCurrentSize(double d) {
        this.currentSize = d;
    }

    public void setDownloadId(int i) {
        this.downloadId = i;
    }

    public void setIsInPause(int i) {
        this.isInPause = i;
    }

    public void setLastModification(String str) {
        this.lastModification = str;
    }

    public void setLocalFilePath(String str) {
        this.localFilePath = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setPercent(int i) {
        this.percent = i;
    }

    public void setProgress(int i) {
        this.progress = i;
    }

    public void setSpeed(int i) {
        this.speed = i;
    }

    public void setTotalSize(double d) {
        this.totalSize = d;
    }
}
