package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;

@Keep
public class YoutubeModel implements Parcelable {
    public static final Creator<YoutubeModel> CREATOR = new Creator<YoutubeModel>() {
        /* class com.magicapps.casttotv.tv.model.YoutubeModel.AnonymousClass1 */

        @Override // android.os.Parcelable.Creator
        public YoutubeModel createFromParcel(Parcel parcel) {
            return new YoutubeModel(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public YoutubeModel[] newArray(int i) {
            return new YoutubeModel[i];
        }
    };
    @SerializedName("channelTitle")
    private String channelTitle;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;
    @SerializedName("videoId")
    private String videoId;

    public int describeContents() {
        return 0;
    }

    public YoutubeModel() {
    }

    public YoutubeModel(String str, String str2, String str3, String str4) {
        this.videoId = str;
        this.channelTitle = str2;
        this.title = str3;
        this.url = str4;
    }

    protected YoutubeModel(Parcel parcel) {
        this.videoId = parcel.readString();
        this.channelTitle = parcel.readString();
        this.title = parcel.readString();
        this.url = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.videoId);
        parcel.writeString(this.channelTitle);
        parcel.writeString(this.title);
        parcel.writeString(this.url);
    }

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(String str) {
        this.videoId = str;
    }

    public String getChannelTitle() {
        return this.channelTitle;
    }

    public void setChannelTitle(String str) {
        this.channelTitle = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }
}
