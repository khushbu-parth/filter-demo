package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Model;

public class SplashModel {
    private String AppName, AppLink, AppUrl;

    public SplashModel(String AppUrl, String AppName, String AppLink) {
        this.AppName = AppName;
        this.AppLink = AppLink;
        this.AppUrl = AppUrl;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String AppName) {
        this.AppName = AppName;
    }

    public String getAppLink() {
        return AppLink;
    }

    public void setAppLink(String AppIcon) {
        this.AppLink = AppIcon;
    }

    public String getAppUrl() {
        return AppUrl;
    }

    public void setAppUrl(String AppUrl) {
        this.AppUrl = AppUrl;
    }
}
