package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TiktokDataModel implements Serializable {
    @SerializedName("mainvideo")
    String mainvideo;
    @SerializedName("thumbnail")
    String thumbnail;
    @SerializedName("userdetail")
    String userdetail;
    @SerializedName("videowithoutWaterMark")
    String videowithoutWaterMark;

    public String getMainvideo() {
        return this.mainvideo;
    }

    public void setMainvideo(String str) {
        this.mainvideo = str;
    }

    public String getVideowithoutWaterMark() {
        return this.videowithoutWaterMark;
    }

    public void setVideowithoutWaterMark(String str) {
        this.videowithoutWaterMark = str;
    }

    public String getUserdetail() {
        return this.userdetail;
    }

    public void setUserdetail(String str) {
        this.userdetail = str;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String str) {
        this.thumbnail = str;
    }
}