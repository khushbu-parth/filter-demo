package com.yanzhenjie.andserver.util;

import android.content.res.AssetManager;
import android.text.TextUtils;
import java.io.File;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class AssetsReader {
    private AssetManager mAssetManager;

    public AssetsReader(AssetManager assetManager) {
        this.mAssetManager = assetManager;
    }

    public InputStream getInputStream(String str) {
        try {
            return this.mAssetManager.open(str);
        } catch (Throwable unused) {
            return null;
        }
    }

    public boolean isFile(String str) {
        return getInputStream(str) != null;
    }

    public String[] fileList(String str) {
        try {
            return this.mAssetManager.list(str);
        } catch (Throwable unused) {
            return null;
        }
    }

    public List<String> scanFile(String str) {
        LinkedList linkedList = new LinkedList();
        if (isFile(str)) {
            linkedList.add(str);
        } else {
            String[] fileList = fileList(str);
            if (fileList != null && fileList.length > 0) {
                for (String str2 : fileList) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(TextUtils.isEmpty(str) ? "" : str + File.separator);
                    sb.append(str2);
                    String sb2 = sb.toString();
                    if (isFile(sb2)) {
                        linkedList.add(sb2);
                    } else {
                        List<String> scanFile = scanFile(sb2);
                        if (scanFile.size() > 0) {
                            linkedList.addAll(scanFile);
                        }
                    }
                }
            }
        }
        return linkedList;
    }
}
