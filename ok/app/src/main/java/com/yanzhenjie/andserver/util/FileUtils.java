package com.yanzhenjie.andserver.util;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

public class FileUtils {
    public static String getMimeType(String str) {
        if (!TextUtils.isEmpty(str)) {
            String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
            if (MimeTypeMap.getSingleton().hasExtension(fileExtensionFromUrl)) {
                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl);
            }
        }
        return "application/octet-stream";
    }
}
