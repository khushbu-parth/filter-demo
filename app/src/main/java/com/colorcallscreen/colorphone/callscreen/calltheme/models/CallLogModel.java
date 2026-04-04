package com.colorcallscreen.colorphone.callscreen.calltheme.models;

import android.graphics.Bitmap;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.ContactsHandler;
import java.io.Serializable;


public class CallLogModel implements Serializable {
    private String callId;
    private int callType;
    private String date;
    private String displayName;
    private String duration;
    private String firstLetter;
    private String imgUri;
    private int isRead;
    private String name;
    private String number;
    private String simName;
    private Bitmap userImage;
    private int colorCode = -1;
    private boolean isDrawOpen = false;
    private boolean userImagePresentChecked = false;
    private int sameNumberCount = 1;

    public String getCallId() {
        return this.callId;
    }

    public void setCallId(String str) {
        this.callId = str;
    }

    public int getCallType() {
        return this.callType;
    }

    public int getColorCode() {
        return this.colorCode;
    }

    public String getDate() {
        return this.date;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getDuration() {
        return this.duration;
    }

    public String getFirstLetter() {
        String str = this.firstLetter;
        if (str != null) {
            return str;
        }
        if (getName() != null && getName().length() > 0) {
            setFirstLetter(getName().substring(0, 1));
        } else {
            setFirstLetter("#");
        }
        return this.firstLetter;
    }

    public String getImgUri() {
        return this.imgUri;
    }

    public int getIsRead() {
        return this.isRead;
    }

    public String getName() {
        return this.name;
    }

    public String getNumber() {
        return this.number;
    }

    public int getSameNumberCount() {
        return this.sameNumberCount;
    }

    public String getSimName() {
        return this.simName;
    }

    public Bitmap getUserImage() {
        return this.userImage;
    }

    public boolean isDrawOpen() {
        return this.isDrawOpen;
    }

    public boolean isUserImagePresentChecked() {
        return this.userImagePresentChecked;
    }

    public void searchForNameForNumber() {
        try {
            new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.models.CallLogModel.1
                @Override 
                public void run() {
                    try {
                        if (CallLogModel.this.number != null) {
                            if (CallLogModel.this.name == null || CallLogModel.this.name.isEmpty() || CallLogModel.this.name.equalsIgnoreCase(CallLogModel.this.number)) {
                                CallLogModel callLogModel = CallLogModel.this;
                                callLogModel.name = ContactsHandler.contactNameFromNumber(callLogModel.number, BoloApplication.getApplication());
                                CallLogModel callLogModel2 = CallLogModel.this;
                                callLogModel2.displayName = callLogModel2.displayName.replace(CallLogModel.this.number, CallLogModel.this.name);
                            }
                        }
                    } catch (Exception unused) {
                    }
                }
            }).start();
        } catch (Exception unused) {
        }
    }

    public void setCallType(String str) {
        this.callType = Integer.parseInt(str);
    }

    public void setColorCode(int i) {
        this.colorCode = i;
    }

    public void setDate(String str) {
        this.date = str;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
    }

    public void setDrawOpen(boolean z) {
        this.isDrawOpen = z;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public void setFirstLetter(String str) {
        this.firstLetter = str;
    }

    public void setImgUri(String str) {
        this.imgUri = str;
    }

    public void setIsRead(int i) {
        this.isRead = i;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setNumber(String str) {
        this.number = str;
    }

    public void setSameNumberCount(int i) {
        this.sameNumberCount = i;
    }

    public void setSimName(String str) {
        this.simName = str;
    }

    public void setUserImage(Bitmap bitmap) {
        this.userImage = bitmap;
    }

    public void setUserImagePresentChecked(boolean z) {
        this.userImagePresentChecked = z;
    }
}
