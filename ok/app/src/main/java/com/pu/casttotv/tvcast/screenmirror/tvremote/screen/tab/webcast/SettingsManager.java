package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.os.Environment;

/* loaded from: classes4.dex */
public class SettingsManager {
    public static final String DOWNLOAD_FOLDER;
    public static final String DOWNLOAD_FOLDER_AUDIO;
    public static final String DOWNLOAD_FOLDER_IMAGES;
    public static final String DOWNLOAD_FOLDER_VIDEO;

    static {
        String str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "DOWNLOAD_AIO_VIDEO" + "/";
        DOWNLOAD_FOLDER = str;
        DOWNLOAD_FOLDER_IMAGES = str + "images";
        DOWNLOAD_FOLDER_AUDIO = str + "audio";
        DOWNLOAD_FOLDER_VIDEO = str + "video";
    }
}
