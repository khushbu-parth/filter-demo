package com.co.casttotv.screenmirroring.mirroring.cast.models;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import java.io.File;

public class MediaModel {

    String path;
    String displayName;
    String albumName;
    String artistName;
    Long duration;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String album) {
        this.albumName = album;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @BindingAdapter("setThumbnail")
    public static void image(ImageView view, String path){
        Glide.with(view.getContext())
                .load(new File(path))
                .placeholder(new ColorDrawable(Color.WHITE))
                .into(view);
    }
}
