package com.pu.casttotv.tvcast.screenmirror.tvremote.model;

import java.util.ArrayList;

public class PhotoAlbum {
    private ArrayList<MediaModel> albumPhotos;
    private String coverUri;
    private String name;

    public void setId(int i) {
    }

    public PhotoAlbum() {
    }

    public PhotoAlbum(int i, String str, String str2, ArrayList<MediaModel> arrayList) {
        this.name = str;
        this.coverUri = str2;
        this.albumPhotos = arrayList;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getCoverUri() {
        return this.coverUri;
    }

    public void setCoverUri(String str) {
        this.coverUri = str;
    }

    public ArrayList<MediaModel> getAlbumPhotos() {
        if (this.albumPhotos == null) {
            this.albumPhotos = new ArrayList<>();
        }
        return this.albumPhotos;
    }
}
