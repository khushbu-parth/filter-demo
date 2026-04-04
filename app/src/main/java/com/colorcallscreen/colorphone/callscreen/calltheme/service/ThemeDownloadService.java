package com.colorcallscreen.colorphone.callscreen.calltheme.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.base.ResponseInterface;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ThemeModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ThemeModelWrapper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.DownloadCompleteReceiver;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;


public class ThemeDownloadService extends Service implements DownloadCompleteReceiver.DownloadListener {
    private static final String TAG = "ThemeDownloadService";
    private static final File THEME_CACHE_FOLDER_PATH = BoloApplication.getApplication().getExternalFilesDir("/.bolo/.cacheTheme");
    public static boolean isServiceRunning = false;
    private List<ThemeModel> themeModels;
    private ThemeWebService themeWebService;
    List<String> downloadedThemes = Helper.getDownloadedTheme();
    private long downloadID = -1;
    private int currentThemeIndex = -1;
    private String lastDownloadingFileName = "";
    private int currentThemeRequest = 1;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.ThemeDownloadService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            ThemeDownloadService.this.onDownloadComplete(intent);
        }
    };

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    static int access$104(ThemeDownloadService themeDownloadService) {
        int i = themeDownloadService.currentThemeIndex + 1;
        themeDownloadService.currentThemeIndex = i;
        return i;
    }

    private void downloadIfNotExist(List<ThemeModel> list) {
        for (ThemeModel themeModel : list) {
            if (!isThemeDownloaded(themeModel) && checkSelfPermission(BoloPermission.WRITE_EXTERNAL_STORAGE) == 0) {
                Helper.downloadTheme(this, ThemeModel.BASE_IMAGE + themeModel.getThemeImage(), themeModel.getThemeImage());
            }
        }
    }

    public static long downloadTheme(Context context, String str, String str2) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
        request.setVisibleInDownloadsUi(false);
        request.setNotificationVisibility(2);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalFilesDir(BoloApplication.getApplication(), "/.bolo", "/.cacheTheme/" + str2);
        return ((DownloadManager) context.getSystemService("download")).enqueue(request);
    }

    private boolean isThemeDownloaded(ThemeModel themeModel) {
        for (String str : this.downloadedThemes) {
            if (themeModel != null && str.contains(themeModel.getThemeImage())) {
                return true;
            }
        }
        return false;
    }

    private void moveFile(String str, String str2, String str3) {
        try {
            File file = new File(str3);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileInputStream fileInputStream = new FileInputStream(str + str2);
            FileOutputStream fileOutputStream = new FileOutputStream(str3 + str2);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    new File(str + str2).delete();
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e2) {
            Log.e(TAG, e2.getMessage());
        }
    }

    private void prepareForDownloadTheme(int i) {
        if (i == 1) {
            requestServer(ThemeWebService.CALLER_IMAGES);
        } else if (i == 2) {
            requestServer(ThemeWebService.CALLER_LIVEWALLPAPER);
        } else if (i != 3) {
            stopSelf();
        } else {
            requestServer(ThemeWebService.CALLER_THEME);
        }
    }

    private void requestServer(String str) {
        this.themeWebService.getThemes(this, str, new ResponseInterface() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.ThemeDownloadService.2
            @Override // com.colorcallscreen.colorphone.callscreen.calltheme.base.ResponseInterface
            public void onResponse(BaseModel baseModel, String str2) {
                if (baseModel != null) {
                    ThemeDownloadService.this.themeModels = ((ThemeModelWrapper) baseModel).getData().getThemes();
                    ThemeDownloadService themeDownloadService = ThemeDownloadService.this;
                    themeDownloadService.downloadIfNotExist(themeDownloadService.themeModels, ThemeDownloadService.access$104(ThemeDownloadService.this));
                }
            }
        });
    }

    private void unRegisterBroadcast() {
        try {
            if (this.broadcastReceiver != null) {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(this.broadcastReceiver);
                this.broadcastReceiver = null;
            }
        } catch (Exception unused) {
        }
    }

    private boolean validDownload(long j) {
        return Helper.isValidDownload(this, j);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        File file = THEME_CACHE_FOLDER_PATH;
        Helper.emptyFolder(file);
        Helper.createDirIfNotExist(file);
        this.themeWebService = new ThemeWebService();
        try {
            if (this.broadcastReceiver != null) {
                LocalBroadcastManager.getInstance(this).registerReceiver(this.broadcastReceiver, new IntentFilter(DownloadCompleteReceiver.LOCAL_DOWNLOAD_ACTION));
            }
        } catch (Exception unused) {
        }
        prepareForDownloadTheme(this.currentThemeRequest);
        isServiceRunning = true;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        isServiceRunning = false;
        unRegisterBroadcast();
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.utils.DownloadCompleteReceiver.DownloadListener
    public void onDownloadComplete(Intent intent) {
        if (this.downloadID != -1) {
            long longExtra = intent.getLongExtra("extra_download_id", -1L);
            long j = this.downloadID;
            if (j != longExtra) {
                return;
            }
            if (validDownload(j)) {
                if (this.themeModels != null) {
                    Log.i(TAG, "onDownloadComplete: File Downloaded:  " + (this.currentThemeIndex + 1) + "/" + this.themeModels.size());
                }
                moveFile(THEME_CACHE_FOLDER_PATH.getAbsolutePath() + "/", this.lastDownloadingFileName, Constants.THEME_DIRECTORY + "/");
                Helper.getDownloadedTheme();
            }
        }
        List<ThemeModel> list = this.themeModels;
        int i = this.currentThemeIndex + 1;
        this.currentThemeIndex = i;
        downloadIfNotExist(list, i);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean stopService(Intent intent) {
        unRegisterBroadcast();
        return super.stopService(intent);
    }

    public void downloadIfNotExist(List<ThemeModel> list, int i) {
        if (list == null) {
            Log.e(TAG, "downloadIfNotExist: Themes are null");
        } else if (i <= 5 && i < list.size()) {
            ThemeModel themeModel = list.get(i);
            this.lastDownloadingFileName = themeModel.getThemeImage();
            if (!themeModel.getThemeImage().equals("Dog.mp4") && !isThemeDownloaded(themeModel) && checkSelfPermission(BoloPermission.WRITE_EXTERNAL_STORAGE) == 0) {
                this.downloadID = downloadTheme(this, ThemeModel.BASE_IMAGE + themeModel.getThemeImage(), themeModel.getThemeImage());
                return;
            }
            int i2 = this.currentThemeIndex + 1;
            this.currentThemeIndex = i2;
            downloadIfNotExist(list, i2);
        } else {
            Log.d(TAG, "downloadIfNotExist: All themes are downloaded now");
            this.currentThemeIndex = -1;
            int i3 = this.currentThemeRequest + 1;
            this.currentThemeRequest = i3;
            prepareForDownloadTheme(i3);
        }
    }
}
