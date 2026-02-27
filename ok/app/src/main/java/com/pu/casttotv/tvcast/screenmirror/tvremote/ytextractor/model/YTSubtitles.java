package com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.model;

public class YTSubtitles {
    String baseUrl;
    boolean isTranslatable;
    String languageCode;
    SubtitleName name;

    public void setBaseUrl(String str) {
        this.baseUrl = str;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public void setName(SubtitleName subtitleName) {
        this.name = subtitleName;
    }

    public SubtitleName getName() {
        return this.name;
    }

    public void setLanguageCode(String str) {
        this.languageCode = str;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public void setIsTranslatable(boolean z) {
        this.isTranslatable = z;
    }

    public boolean isTranslatable() {
        return this.isTranslatable;
    }

    public class SubtitleName {
        String simpleText;

        public SubtitleName() {
        }

        public void setSimpleText(String str) {
            this.simpleText = str;
        }

        public String getSimpleText() {
            return this.simpleText;
        }
    }
}
