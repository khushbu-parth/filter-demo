package com.colorcallscreen.colorphone.callscreen.calltheme.custom;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.squareup.picasso.Picasso;
import java.io.File;
import pl.droidsonroids.gif.GifImageView;


public class GifWallpaper extends GifImageView {
    private Context context;
    private String name;
    private String path;
    private String theme;

    public GifWallpaper(Context context) {
        super(context);
        this.context = context;
    }

    public long download(String str) {
        if (Utility.isInternetEnabled(this.context)) {
            return Helper.downloadTheme(this.context, str, this.theme);
        }
        return -1L;
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

    public void setGifWallpaper(String str) {
        Context context = this.context;
        if (context != null) {
            this.path = str;
            Glide.with(context.getApplicationContext()).asGif().load(new File(str)).apply((BaseRequestOptions<?>) RequestOptions.placeholderOf((int) R.drawable.placeholder)).into(this);
        }
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setOfflineWallpaper(final String str) {
        new Handler(this.context.getMainLooper()).post(new Runnable() { // from class: com.contactsfree.idailerapp.custom.GifWallpaper.1
            @Override 
            public void run() {
                int resourceByName = Utility.getResourceByName(GifWallpaper.this.context, str, "raw");
                if (resourceByName != -1) {
                    GifWallpaper.this.setImageResource(resourceByName);
                } else {
                    GifWallpaper.this.setImageResource(R.drawable.placeholder);
                }
            }
        });
    }

    public void setTheme(String str) {
        this.theme = str;
    }

    public void setThumbnail(String str) {
        Picasso.get().load(str).placeholder(R.drawable.placeholder).into(this);
    }

    public void setWallpaper(String str) {
        if (this.context != null) {
            this.path = str;
            Picasso.get().load(new File(str)).placeholder(R.drawable.placeholder).into(this);
        }
    }

    public GifWallpaper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
    }
}