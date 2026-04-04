package com.colorcallscreen.colorphone.callscreen.calltheme.custom;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.VideoView;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;


public class VideoWallpaper extends VideoView implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private Context context;
    private boolean isForCallScreen;
    private String name;
    private OnErrorOnPlaying onErrorOnPlaying;
    private String path;
    private String theme;

    
    public interface OnErrorOnPlaying {
        void onErrorOnPlaying(MediaPlayer mediaPlayer);
    }

    public VideoWallpaper(Context context) {
        super(context);
        this.isForCallScreen = false;
        this.context = context;
    }

    public long download(String str) {
        String str2;
        if (!Utility.isInternetEnabled(this.context) || (str2 = this.theme) == null) {
            return -1L;
        }
        return Helper.downloadTheme(this.context, str, str2);
    }

    public String getName() {
        return this.name;
    }

    public String getTheme() {
        return this.theme;
    }

    public String getWallpaper() {
        return this.path;
    }

    public boolean isApplied() {
        return Helper.isThemeApplied(this.theme);
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        if (this.isForCallScreen) {
            setOfflineWallpaper("");
        }
        OnErrorOnPlaying onErrorOnPlaying = this.onErrorOnPlaying;
        if (onErrorOnPlaying != null) {
            onErrorOnPlaying.onErrorOnPlaying(mediaPlayer);
        }
        return true;
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.setLooping(true);
    }

    public void setForCallScreen(boolean z) {
        this.isForCallScreen = z;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setOfflineWallpaper(String str) {
        setVideoURI(Uri.parse("android.resource://" + this.context.getPackageName() + "/" + R.raw.m_defalut_vid));
        setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.VideoWallpaper.1
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                return true;
            }
        });
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.VideoWallpaper.2
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mediaPlayer) {
                VideoWallpaper.this.start();
                mediaPlayer.setLooping(true);
            }
        });
    }

    public void setOnErrorOnPlaying(OnErrorOnPlaying onErrorOnPlaying) {
        this.onErrorOnPlaying = onErrorOnPlaying;
    }

    public void setTheme(String str) {
        this.theme = str;
    }

    public void setWallpaper(String str) {
        this.path = str;
        if (str.contains("android.resource:")) {
            setVideoURI(Uri.parse(str));
        } else {
            setVideoPath(str);
        }
        requestFocus();
        setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.VideoWallpaper.3
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                return onError(mediaPlayer, i, i2);
            }
        });
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.custom.VideoWallpaper.4
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mediaPlayer) {
                VideoWallpaper.this.start();
                mediaPlayer.setLooping(true);
            }
        });
    }

    public VideoWallpaper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isForCallScreen = false;
        this.context = context;
    }
}
