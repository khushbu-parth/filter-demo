package com.cast.tv.screen.mirroring.screencasting.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.text.TextUtils;

import com.cast.tv.screen.mirroring.screencasting.CastApp;
import com.cast.tv.screen.mirroring.screencasting.Contract.Contracts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {
    private static int inSampleSize(int i) {
        int i2 = 1;
        for (int i3 = i; i3 > 1; i3 >>= 1) {
            i2 <<= 1;
        }
        return i2 != i ? i2 << 1 : i2;
    }

    public static String rotateImage(String str) {
        String matching = matching(str);
        if (TextUtils.isEmpty(matching) || !new File(matching).exists()) {
            Bitmap bitmapDecodeFile = getBitmapDecodeFile(str);
            if (bitmapDecodeFile == null) {
                return str;
            }
            Matrix matrix = new Matrix();
            matrix.setRotate(-90.0f);
            return saveToPath(Bitmap.createBitmap(bitmapDecodeFile, 0, 0, bitmapDecodeFile.getWidth(), bitmapDecodeFile.getHeight(), matrix, true), matching);
        }
        return matching;
    }

    private static String matching(String str) {
        String str2;
        if (str == null || TextUtils.isEmpty(str)) {
            return str;
        }
        String[] split = getNameByPath(str).split("\\.");
        if (split.length <= 1) {
            return str;
        }
        String str3 = split[0];
        if (str3.contains("#")) {
            try {
                String[] split2 = str3.split("#");
                int parseInt = Integer.parseInt(split2[split2.length - 1]);
                str2 = split2[0] + "#" + (parseInt >= 3 ? 0 : parseInt + 1);
            } catch (NumberFormatException unused) {
                str2 = str3 + "#1";
            }
        } else {
            str2 = str3 + "#1";
        }
        String str4 = CastApp.mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator + Contracts.PHOTO_ROTATE_TEMP + File.separator;
        File file = new File(str4);
        if (!file.exists()) {
            file.mkdir();
        }
        return str4 + str2 + "." + split[1];
    }

    public static Bitmap getBitmapDecodeFile(String str) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inJustDecodeBounds = true;
        decode(options, str);
        if (options.outWidth > 1024) {
            options.inSampleSize = inSampleSize(Math.round((options.outWidth * 1.0f) / 1024.0f));
        }
        if (options.outHeight > 1024) {
            options.inSampleSize = Math.max(options.inSampleSize, inSampleSize(Math.round((options.outHeight * 1.0f) / 1024.0f)));
        }
        options.inJustDecodeBounds = false;
        return decode(options, str);
    }

    private static Bitmap decode(BitmapFactory.Options options, String str) {
        Uri fromFile;
        File file = new File(str);
        if (file.exists() && (fromFile = Uri.fromFile(file)) != null) {
            String path = fromFile.getPath();
            if (!TextUtils.isEmpty(path)) {
                return BitmapFactory.decodeFile(path, options);
            }
            return null;
        }
        return null;
    }

    public static String saveToPath(Bitmap bitmap, String str) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
            L.d("XXX", file.getAbsolutePath());
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getNameByPath(String str) {
        String str2 = System.currentTimeMillis() + ".jpg";
        if (str == null || TextUtils.isEmpty(str)) {
            return str2;
        }
        File file = new File(str);
        return file.exists() ? file.getName() : str2;
    }
}
