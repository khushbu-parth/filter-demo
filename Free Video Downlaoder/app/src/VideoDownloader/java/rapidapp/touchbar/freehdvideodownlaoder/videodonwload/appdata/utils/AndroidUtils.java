package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;

import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.util.UUID;

public class AndroidUtils {
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private static String uniqueID;

    public static String Default_Root(String str) {
        return generateFilePath(FileDownloadUtils.getDefaultSaveRootPath(), str);
    }

    public static synchronized String firebase_id(Context context) {
        String str;
        Class cls = AndroidUtils.class;
        synchronized (cls) {
            synchronized (cls) {
                if (uniqueID == null) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_UNIQUE_ID, 0);
                    String string = sharedPreferences.getString(PREF_UNIQUE_ID, null);
                    uniqueID = string;
                    if (string == null) {
                        uniqueID = UUID.randomUUID().toString();
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString(PREF_UNIQUE_ID, uniqueID);
                        edit.apply();
                    }
                }
                str = uniqueID;
            }
        }
        return str;
    }


    private static String generateFilePath(String str, String str2) {
        if (str2 == null) {
            throw new IllegalStateException("can't generate real path, the file name is null");
        } else if (str != null) {
            return FileDownloadUtils.formatString("%s%s%s", str, File.separator, str2);
        } else {
            throw new IllegalStateException("can't generate real path, the directory is null");
        }
    }

    public static boolean isValidUrl(String str) {
        return Patterns.WEB_URL.matcher(str.toLowerCase()).matches();
    }

    public static void notifyMediaScannerService(Context context, String str) {
        MediaScannerConnection.scanFile(context, new String[]{str}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
            public final void onScanCompleted(String str, Uri uri) {
                Log.i("ExternalStorage", "Scanned " + str + ":");
                Log.i("ExternalStorage", "-> uri=".concat(String.valueOf(uri)));
            }
        });
    }
}
