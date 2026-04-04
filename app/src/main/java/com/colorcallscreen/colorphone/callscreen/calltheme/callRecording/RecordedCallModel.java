package com.colorcallscreen.colorphone.callscreen.calltheme.callRecording;

import java.io.File;


public class RecordedCallModel {
    private String callType;
    private long creationTime = 0;
    private long duration;
    private File file;
    private String fileName;
    private String firstLetter;
    private boolean isFav;
    private String name;
    private String number;

    public String getCallType() {
        return this.callType;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public long getDuration() {
        return this.duration;
    }

    public File getFile() {
        return this.file;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFirstLetter() {
        String str = this.firstLetter;
        if (str != null) {
            return str;
        }
        if (getName() != null && getName().length() > 0) {
            this.firstLetter = getName().substring(0, 1);
        } else {
            this.firstLetter = "#";
        }
        return this.firstLetter;
    }

    public String getName() {
        return this.name;
    }

    public String getNumber() {
        return this.number;
    }

    public boolean isFav() {
        return this.isFav;
    }

    public void setCallType(String str) {
        this.callType = str;
    }

    public void setCreationTime(long j) {
        this.creationTime = j;
    }

    public void setDuration(long j) {
        this.duration = j;
    }

    public void setFav(boolean z) {
        this.isFav = z;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setNumber(String str) {
        this.number = str;
    }
}
