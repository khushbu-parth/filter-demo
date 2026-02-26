package com.lib.screening.bean;

public class MediaInfo {
    public static final int TYPE_AUDIO = 3;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_MIRROR = 4;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_VIDEO = 2;
    private String bulbulName;
    private long duration;
    private String filePath;
    private int index;
    private String mediaId;
    private String mediaName;
    private int mediaType = 0;
    private String theAlbumName;
    private String uri;

    public String getMediaName() {
        return this.mediaName;
    }

    public void setMediaName(String str) {
        this.mediaName = str;
    }

    public String getMediaId() {
        return this.mediaId;
    }

    public void setMediaId(String str) {
        this.mediaId = str;
    }

    public int getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(int i) {
        this.mediaType = i;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String str) {
        this.uri = str;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long j) {
        this.duration = j;
    }

    public String getBulbulName() {
        return this.bulbulName;
    }

    public void setBulbulName(String str) {
        this.bulbulName = str;
    }

    public String getTheAlbumName() {
        return this.theAlbumName;
    }

    public void setTheAlbumName(String str) {
        this.theAlbumName = str;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }
}
