package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;

@Keep
public class ListKeyTube {
    @SerializedName("id_ads")
    private String id_ads;
    @SerializedName("status")
    private boolean status;

    public boolean isStatus() {
        return this.status;
    }

    public void setStatus(boolean z) {
        this.status = z;
    }

    public String getId_ads() {
        return this.id_ads;
    }

    public void setId_ads(String str) {
        this.id_ads = str;
    }
}
