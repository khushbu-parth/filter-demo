package com.pu.casttotv.tvcast.screenmirror.tvremote;

import android.os.Parcel;
import android.os.Parcelable;
import com.connectsdk.service.airplay.PListParser;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: HistoryBrowser.kt */
public final class HistoryBrowser implements Parcelable {
    @NotNull
    public static final Parcelable.Creator<HistoryBrowser> CREATOR = new Creator();
    @NotNull
    private final String date;
    private final int id;
    @NotNull
    private final String linkUrl;
    @NotNull
    private final String title;
    private final int type;

    /* compiled from: HistoryBrowser.kt */
    public static final class Creator implements Parcelable.Creator<HistoryBrowser> {
        @Override // android.os.Parcelable.Creator
        @NotNull
        public final HistoryBrowser createFromParcel(@NotNull Parcel parcel) {
            Intrinsics.checkNotNullParameter(parcel, "parcel");
            return new HistoryBrowser(parcel.readInt(), parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readString());
        }

        @Override // android.os.Parcelable.Creator
        @NotNull
        public final HistoryBrowser[] newArray(int i) {
            return new HistoryBrowser[i];
        }
    }

    public static HistoryBrowser copy$default(HistoryBrowser historyBrowser, int i, String str, String str2, int i2, String str3, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = historyBrowser.id;
        }
        if ((i3 & 2) != 0) {
            str = historyBrowser.title;
        }
        if ((i3 & 4) != 0) {
            str2 = historyBrowser.linkUrl;
        }
        if ((i3 & 8) != 0) {
            i2 = historyBrowser.type;
        }
        if ((i3 & 16) != 0) {
            str3 = historyBrowser.date;
        }
        return historyBrowser.copy(i, str, str2, i2, str3);
    }

    public final int component1() {
        return this.id;
    }

    @NotNull
    public final String component2() {
        return this.title;
    }

    @NotNull
    public final String component3() {
        return this.linkUrl;
    }

    public final int component4() {
        return this.type;
    }

    @NotNull
    public final String component5() {
        return this.date;
    }

    @NotNull
    public final HistoryBrowser copy(int i, @NotNull String str, @NotNull String str2, int i2, @NotNull String str3) {
        Intrinsics.checkNotNullParameter(str, "title");
        Intrinsics.checkNotNullParameter(str2, "linkUrl");
        Intrinsics.checkNotNullParameter(str3, PListParser.TAG_DATE);
        return new HistoryBrowser(i, str, str2, i2, str3);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HistoryBrowser)) {
            return false;
        }
        HistoryBrowser historyBrowser = (HistoryBrowser) obj;
        return this.id == historyBrowser.id && Intrinsics.areEqual(this.title, historyBrowser.title) && Intrinsics.areEqual(this.linkUrl, historyBrowser.linkUrl) && this.type == historyBrowser.type && Intrinsics.areEqual(this.date, historyBrowser.date);
    }

    public int hashCode() {
        return (((((((this.id * 31) + this.title.hashCode()) * 31) + this.linkUrl.hashCode()) * 31) + this.type) * 31) + this.date.hashCode();
    }

    @NotNull
    public String toString() {
        return "HistoryBrowser(id=" + this.id + ", title=" + this.title + ", linkUrl=" + this.linkUrl + ", type=" + this.type + ", date=" + this.date + ')';
    }

    public void writeToParcel(@NotNull Parcel parcel, int i) {
        Intrinsics.checkNotNullParameter(parcel, "out");
        parcel.writeInt(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.linkUrl);
        parcel.writeInt(this.type);
        parcel.writeString(this.date);
    }

    public HistoryBrowser(int i, @NotNull String str, @NotNull String str2, int i2, @NotNull String str3) {
        Intrinsics.checkNotNullParameter(str, "title");
        Intrinsics.checkNotNullParameter(str2, "linkUrl");
        Intrinsics.checkNotNullParameter(str3, PListParser.TAG_DATE);
        this.id = i;
        this.title = str;
        this.linkUrl = str2;
        this.type = i2;
        this.date = str3;
    }

    public final int getId() {
        return this.id;
    }

    @NotNull
    public final String getTitle() {
        return this.title;
    }

    @NotNull
    public final String getLinkUrl() {
        return this.linkUrl;
    }

    public final int getType() {
        return this.type;
    }

    @NotNull
    public final String getDate() {
        return this.date;
    }
}
