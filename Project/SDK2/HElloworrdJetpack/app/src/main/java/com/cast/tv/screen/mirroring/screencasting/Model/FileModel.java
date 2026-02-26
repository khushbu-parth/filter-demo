package com.cast.tv.screen.mirroring.screencasting.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.cast.tv.screen.mirroring.screencasting.Utils.L;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class FileModel implements Serializable, Cloneable {
    private static final long serialVersionUID = 1;
    private int childCount;
    private List<FileModel> childFiles;
    private String cover;
    private String displayName;
    private long duration;
    private boolean isSelect;
    private byte[] miniKind;
    private String path;
    private String subTitle;
    private String tempPath;

    public String getCover() {
        return this.cover;
    }

    public void setCover(String str) {
        this.cover = str;
    }

    public String getDisplayName() {
        String str = this.displayName;
        if (str == null || str.isEmpty()) {
            this.displayName = "";
        }
        return this.displayName;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(String str) {
        this.subTitle = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getTempPath() {
        String str = this.tempPath;
        if (str == null || TextUtils.isEmpty(str)) {
            this.tempPath = this.path;
        }
        return this.tempPath;
    }

    public void setTempPath(String str) {
        this.tempPath = str;
    }

    public int getChildCount() {
        List<FileModel> list = this.childFiles;
        if (list != null) {
            return list.size();
        }
        return this.childCount;
    }

    public void setChildCount(int i) {
        this.childCount = i;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean z) {
        this.isSelect = z;
    }

    public List<FileModel> getChildFiles() {
        return this.childFiles;
    }

    public void setChildFiles(List<FileModel> list) {
        this.childFiles = list;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long j) {
        this.duration = j;
    }

    public String getDurationStr() {
        String str;
        long j = this.duration;
        if (j <= 1000) {
            return "00:01";
        }
        long j2 = j / 1000;
        int i = (int) j2;
        int i2 = (i / 60) / 60;
        int i3 = ((int) (j2 - ((i2 * 60) * 60))) / 60;
        int i4 = i % 60;
        String str2 = "";
        if (i2 > 0 && i2 < 10) {
            str2 = (str2 + "0" + i2) + ":";
        } else if (i2 >= 10) {
            str2 = (str2 + String.valueOf(i2)) + ":";
        }
        if (i3 < 10) {
            str = str2 + "0" + i3;
        } else {
            str = str2 + i3;
        }
        String str3 = str + ":";
        if (i4 < 10) {
            return str3 + "0" + i4;
        }
        return str3 + i4;
    }

    public Bitmap getMiniKind() {
        byte[] bArr = this.miniKind;
        if (bArr != null) {
            return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
        }
        return null;
    }

    public void setMiniKind(Bitmap bitmap) {
        if (bitmap != null) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                this.miniKind = byteArrayOutputStream.toByteArray();
            } catch (Exception unused) {
                L.e("FileModel", "setMiniKind exception");
            }
        }
    }

    public byte[] getMiniKindByte() {
        return this.miniKind;
    }

    public int getFileType() {
        String str = this.path;
        if (str != null && !TextUtils.isEmpty(str)) {
            File file = new File(this.path);
            if (file.isDirectory()) {
                return FileType.FOLDER;
            }
            String name = file.getName();
            if (TextUtils.isEmpty(name)) {
                return FileType.OTHER;
            }
            if (checkFileIsPic(name)) {
                return FileType.IMAGE;
            }
            if (checkFileIsVideo(name)) {
                return 273;
            }
            if (checkFileIsAudio(name)) {
                return FileType.AUDIO;
            }
        }
        return FileType.OTHER;
    }

    private boolean checkFileIsPic(String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            return false;
        }
        return str.toLowerCase().endsWith("png") || str.toLowerCase().endsWith("jpg") || str.toLowerCase().endsWith("jpeg") || str.toLowerCase().endsWith("gif");
    }

    private boolean checkFileIsVideo(String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            return false;
        }
        return str.toLowerCase().endsWith("mp4") || str.toLowerCase().endsWith("flv") || str.toLowerCase().endsWith("3gp") || str.toLowerCase().endsWith("3gpp") || str.toLowerCase().endsWith("avi") || str.toLowerCase().endsWith("mp2ts") || str.toLowerCase().endsWith("webm");
    }

    private boolean checkFileIsAudio(String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            return false;
        }
        return str.toLowerCase().endsWith("mp3") || str.toLowerCase().endsWith("wav") || str.toLowerCase().endsWith("ogg") || str.toLowerCase().endsWith("wma") || str.toLowerCase().endsWith("amr") || str.toLowerCase().endsWith("aac") || str.toLowerCase().endsWith("flac") || str.toLowerCase().endsWith("m4a");
    }

    public FileModel m146clone() throws CloneNotSupportedException {
        return (FileModel) super.clone();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FileType {
        public static final int AUDIO = 274;
        public static final int FOLDER = 275;
        public static final int IMAGE = 272;
        public static final int OTHER = 276;
        public static final int VIDEO = 273;
    }
}
