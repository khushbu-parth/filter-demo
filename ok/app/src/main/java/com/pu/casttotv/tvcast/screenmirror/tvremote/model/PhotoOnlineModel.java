package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PhotoOnlineModel implements Parcelable {
    public static final Creator<PhotoOnlineModel> CREATOR = new Creator<PhotoOnlineModel>() {
        /* class com.magicapps.casttotv.tv.model.PhotoOnlineModel.AnonymousClass1 */

        @Override // android.os.Parcelable.Creator
        public PhotoOnlineModel createFromParcel(Parcel parcel) {
            return new PhotoOnlineModel(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public PhotoOnlineModel[] newArray(int i) {
            return new PhotoOnlineModel[i];
        }
    };
    private String imageName;
    private String imageURL;
    public boolean isSelected;
    private String thumbURL;

    public int describeContents() {
        return 0;
    }

    public PhotoOnlineModel() {
    }

    protected PhotoOnlineModel(Parcel parcel) {
        this.imageName = parcel.readString();
        this.imageURL = parcel.readString();
        this.thumbURL = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.imageName);
        parcel.writeString(this.imageURL);
        parcel.writeString(this.thumbURL);
    }

    public void setImageName(String str) {
        this.imageName = str;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public void setImageURL(String str) {
        this.imageURL = str;
    }

    public String getThumbURL() {
        return this.thumbURL;
    }

    public void setThumbURL(String str) {
        this.thumbURL = str;
    }
}
