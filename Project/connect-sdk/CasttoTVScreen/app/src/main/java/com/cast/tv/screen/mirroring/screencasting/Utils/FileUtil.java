package com.cast.tv.screen.mirroring.screencasting.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    public static boolean copyFile(String str, String str2) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                L.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!file.isFile()) {
                L.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            } else if (!file.canRead()) {
                L.e("--Method--", "copyFile:  oldFile cannot read.");
                return false;
            } else {
                File file2 = new File(str);
                if (!file.exists()) {
                    L.e("--Method--", "copyFile:  oldFile not exist.");
                    file2.createNewFile();
                }
                FileInputStream fileInputStream = new FileInputStream(str);
                FileOutputStream fileOutputStream = new FileOutputStream(str2);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (-1 != read) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        fileInputStream.close();
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static long getFileSize(File file) {
        long j = 0;
        try {
            if (file.exists()) {
                j = new FileInputStream(file).available();
            } else {
                L.d("FileUtil", "获取文件大小不存在!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return j;
    }

    public static long getFolderSize(File file) {
        File[] listFiles = new File[0];
        long j = 0;
        try {
            listFiles = file.listFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listFiles != null && listFiles.length > 0) {
            for (File file2 : listFiles) {
                j += file2.isDirectory() ? getFolderSize(file2) : file2.length();
            }
            return j;
        }
        return 0L;
    }

    public static void deleteDir(File file) {
        File[] listFiles;
        if (file == null || !file.exists() || !file.isDirectory()) {
            return;
        }
        for (File file2 : file.listFiles()) {
            if (file2.isFile()) {
                file2.delete();
            } else if (file2.isDirectory()) {
                deleteDir(file2);
            }
        }
    }
}
