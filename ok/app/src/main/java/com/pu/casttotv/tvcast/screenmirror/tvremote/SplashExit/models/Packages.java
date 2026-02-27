package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Packages implements Parcelable {

    public static final Creator<Packages> CREATOR = new Creator<Packages>() {
        @Override
        public Packages createFromParcel(Parcel in) {
            return new Packages(in);
        }

        @Override
        public Packages[] newArray(int size) {
            return new Packages[size];
        }
    };
    @SerializedName("price")
    private String price;
    @SerializedName("description")
    private String description;
    @SerializedName("days")
    private String days;
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("app_id")
    private String appId;

    public Packages(String price, String description, String days, int id, String title, String appId) {
        this.price = price;
        this.description = description;
        this.days = days;
        this.id = id;
        this.title = title;
        this.appId = appId;
    }

    protected Packages(Parcel in) {
        price = in.readString();
        description = in.readString();
        days = in.readString();
        id = in.readInt();
        title = in.readString();
        appId = in.readString();
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getDays() {
        return days;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAppId() {
        return appId;
    }

    @Override
    public String toString() {
        return "Packages{" +
                "price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", days='" + days + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", appId=" + appId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(price);
        parcel.writeString(description);
        parcel.writeString(days);
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(appId);
    }
}