package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models;

public class SettingsItem {
    private int engine = 0;
    private boolean isDownloadLimit = false;
    private boolean isDownloadingNotification = true;
    private boolean isMobile = true;
    private int maximumDownload = 6;

    public boolean isRetryDownload() {
        return false;
    }

    public int getEngine() {
        return this.engine;
    }

    public int getMaximumDownload() {
        return this.maximumDownload;
    }

    public boolean isDownloadLimit() {
        return this.isDownloadLimit;
    }

    public boolean isDownloadingNotification() {
        return this.isDownloadingNotification;
    }

    public boolean isMobile() {
        return this.isMobile;
    }

    public void setDownloadLimit(boolean z) {
        this.isDownloadLimit = z;
    }

    public void setDownloadingNotification(boolean z) {
        this.isDownloadingNotification = z;
    }

    public void setEngine(int i) {
        this.engine = i;
    }

    public void setMaximumDownload(int i) {
        this.maximumDownload = i;
    }

    public void setMobile(boolean z) {
        this.isMobile = z;
    }
}
