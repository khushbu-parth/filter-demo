package com.adsdemo.vdapps.adsload.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CheckoutDetails implements Parcelable {
    String mid;
    String callbackUrl;
    String txnToken;
    String razorpayKey;
    int gatewayType;
    String orderId;

    public CheckoutDetails(String mid, String callbackUrl, String txnToken, String razorpayKey, int gatewayType,String orderId) {
        this.mid = mid;
        this.callbackUrl = callbackUrl;
        this.txnToken = txnToken;
        this.razorpayKey = razorpayKey;
        this.gatewayType = gatewayType;
        this.orderId = orderId;
    }

    protected CheckoutDetails(Parcel in) {
        mid = in.readString();
        callbackUrl = in.readString();
        txnToken = in.readString();
        razorpayKey = in.readString();
        orderId = in.readString();
        gatewayType = in.readInt();
    }

    public static final Creator<CheckoutDetails> CREATOR = new Creator<CheckoutDetails>() {
        @Override
        public CheckoutDetails createFromParcel(Parcel in) {
            return new CheckoutDetails(in);
        }

        @Override
        public CheckoutDetails[] newArray(int size) {
            return new CheckoutDetails[size];
        }
    };

    public String getMid() {
        return mid;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public String getTxnToken() {
        return txnToken;
    }

    public String getRazorpayKey() {
        return razorpayKey;
    }

    public int getGatewayType() {
        return gatewayType;
    }

    public String getOrderId() {
        return orderId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(mid);
        parcel.writeString(callbackUrl);
        parcel.writeString(txnToken);
        parcel.writeString(razorpayKey);
        parcel.writeString(orderId);
        parcel.writeInt(gatewayType);
    }
}
