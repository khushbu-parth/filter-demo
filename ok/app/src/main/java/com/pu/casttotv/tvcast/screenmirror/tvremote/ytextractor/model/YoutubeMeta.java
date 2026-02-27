package com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.model;

import java.util.ArrayList;
import java.util.List;

public class YoutubeMeta {
    private boolean allowRatings;
    private String author;
    private Float averageRating;
    private String channelId;
    private boolean isCrawlable;
    private boolean isLive;
    private boolean isLiveContent;
    private boolean isLowLatencyLiveStream;
    private boolean isOwnerViewing;
    private boolean isPrivate;
    private boolean isUnpluggedCorpus;
    private String latencyClass;
    private String lengthSeconds;
    private String shortDescription;
    private Thumbnail thumbnail;
    private String title;
    private boolean useCipher;
    private String videoId;
    private String viewCount;

    public void setIsLive(boolean z) {
        this.isLive = z;
    }

    public boolean getisLive() {
        return this.isLive;
    }

    public void setUseChiper(boolean z) {
        this.useCipher = z;
    }

    public boolean getUseChiper() {
        return this.useCipher;
    }

    public boolean getAllowRatings() {
        return this.allowRatings;
    }

    public void setAllowRatings(boolean z) {
        this.allowRatings = z;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String str) {
        this.author = str;
    }

    public Float getAverageRating() {
        return this.averageRating;
    }

    public void setAverageRating(Float f) {
        this.averageRating = f;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setChannelId(String str) {
        this.channelId = str;
    }

    public boolean getIsCrawlable() {
        return this.isCrawlable;
    }

    public void setIsCrawlable(boolean z) {
        this.isCrawlable = z;
    }

    public boolean getIsLiveContent() {
        return this.isLiveContent;
    }

    public void setIsLiveContent(boolean z) {
        this.isLiveContent = z;
    }

    public boolean getIsLowLatencyLiveStream() {
        return this.isLowLatencyLiveStream;
    }

    public void setIsLowLatencyLiveStream(boolean z) {
        this.isLowLatencyLiveStream = z;
    }

    public boolean getIsOwnerViewing() {
        return this.isOwnerViewing;
    }

    public void setIsOwnerViewing(boolean z) {
        this.isOwnerViewing = z;
    }

    public boolean getIsPrivate() {
        return this.isPrivate;
    }

    public void setIsPrivate(boolean z) {
        this.isPrivate = z;
    }

    public boolean getIsUnpluggedCorpus() {
        return this.isUnpluggedCorpus;
    }

    public void setIsUnpluggedCorpus(boolean z) {
        this.isUnpluggedCorpus = z;
    }

    public String getLatencyClass() {
        return this.latencyClass;
    }

    public void setLatencyClass(String str) {
        this.latencyClass = str;
    }

    public String getLengthSeconds() {
        return this.lengthSeconds;
    }

    public void setLengthSeconds(String str) {
        this.lengthSeconds = str;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public void setShortDescription(String str) {
        this.shortDescription = str;
    }

    public Thumbnail getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail2) {
        this.thumbnail = thumbnail2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(String str) {
        this.videoId = str;
    }

    public String getViewCount() {
        return this.viewCount;
    }

    public void setViewCount(String str) {
        this.viewCount = str;
    }

    public class Thumbnail {
        private List<Thumbnail_> thumbnails = new ArrayList();

        public Thumbnail() {
        }

        public List<Thumbnail_> getThumbnails() {
            return this.thumbnails;
        }

        public void setThumbnails(List<Thumbnail_> list) {
            this.thumbnails = list;
        }
    }

    public class Thumbnail_ {
        private Integer height;
        private String url;
        private Integer width;

        public Thumbnail_() {
        }

        public Integer getHeight() {
            return this.height;
        }

        public void setHeight(Integer num) {
            this.height = num;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }

        public Integer getWidth() {
            return this.width;
        }

        public void setWidth(Integer num) {
            this.width = num;
        }
    }
}
