package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.drive;

import androidx.annotation.Keep;

@Keep
/* loaded from: classes4.dex */
public class CloudAccountDto {
    private String authCode;
    private String displayName;
    private String email;
    private String firstName;
    private String googleIdToken2;
    private String lastName;
    private String personId;
    private String photoUrl;

    public CloudAccountDto() {
    }

    public CloudAccountDto(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
        this.email = str;
        this.personId = str2;
        this.firstName = str3;
        this.lastName = str4;
        this.displayName = str5;
        this.authCode = str6;
        this.googleIdToken2 = str7;
        this.photoUrl = str8;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getPersonId() {
        return this.personId;
    }

    public void setPersonId(String str) {
        this.personId = str;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String str) {
        this.firstName = str;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String str) {
        this.lastName = str;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
    }

    public String getAuthCode() {
        return this.authCode;
    }

    public void setAuthCode(String str) {
        this.authCode = str;
    }

    public String getGoogleIdToken2() {
        return this.googleIdToken2;
    }

    public void setGoogleIdToken2(String str) {
        this.googleIdToken2 = str;
    }

    public String getPhotoUrl() {
        return this.photoUrl;
    }

    public void setPhotoUrl(String str) {
        this.photoUrl = str;
    }
}
