package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.internal.view.SupportMenu;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.databases.DBDownloadManager;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.DownloadingItem;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.receivers.NetworkChangeReceiver;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities.MainActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.AndroidUtils;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.NetworkUtil;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.StoreUserData;


public class DownloadService extends Service implements NetworkChangeReceiver.OnNetoworkChangeListener {
    private static final String CHANNEL_ID = "com.mt.player-001";
    private static final CharSequence CHANNEL_NAME = "Video Downloader";
    static final boolean a = (!DownloadService.class.desiredAssertionStatus());
    private NotificationManager Complete_manager;
    private NotificationCompat.Builder cBuilder;
    public Context context;
    private final IBinder downloadBind = new DownloadBinder();
    public FileDownloadSampleListener fileDownloadSampleListener;
    public StoreUserData storeUserData;
    private NotificationCompat.Builder vBuilder;
    public NotificationManager vnotificationManager;

    public class DownloadBinder extends Binder {
        public DownloadBinder() {
        }

        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    public class DownloadCompletedTask extends AsyncTask<DownloadingItem, Void, Void> {
        String a = "";
        int b = 0;

        DownloadCompletedTask() {
        }


        public Void doInBackground(DownloadingItem... downloadingItemArr) {
            DownloadingItem downloadingItem = downloadingItemArr[0];
            try {
                this.a = downloadingItem.getLocalFilePath();
                this.b = downloadingItem.getDownloadId();
                downloadingItem.setDownloadId(0);
                downloadingItem.setProgress(100);
                DBDownloadManager.getInstance(DownloadService.this.context).addUpdateDownloadData(this.b, downloadingItem, true);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            try {
                if (DownloadService.this.storeUserData.getSettings().isRetryDownload()) {
                    DownloadService.this.retryQuedDownloadData();
                }
                DownloadService.this.vnotificationManager.cancel(this.b);
                if (DownloadService.this.storeUserData.getSettings().isDownloadingNotification()) {
                    DownloadService.this.showDownloadCompletedNotification(this.b, this.a);
                }
                if (DBDownloadManager.getInstance(DownloadService.this.context).getCurrentDownloadingCount() <= 0) {
                    DownloadService.this.stopForeground(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class LoadDownloadingTask extends AsyncTask<Void, Void, List<DownloadingItem>> {
        private LoadDownloadingTask() {
        }

        LoadDownloadingTask(DownloadService downloadService, DownloadService downloadService2, byte b) {
            this();
        }


        public List<DownloadingItem> doInBackground(Void... voidArr) {
            try {
                return DBDownloadManager.getInstance(DownloadService.this.context).getDownloadingData();
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList();
            }
        }


        public void onPostExecute(List<DownloadingItem> list) {
            super.onPostExecute(list);
            if (list != null && list.size() > 0) {
                int i = 0;
                if (NetworkUtil.isNetworkAvailable(DownloadService.this.context)) {
                    while (i < list.size()) {
                        try {
                            DownloadService.this.resumeDownload(list.get(i));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        i++;
                    }
                    return;
                }
                while (i < list.size()) {
                    try {
                        list.get(i).setIsInPause(2);
                        DownloadService.this.pauseDownload(list.get(i));
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    i++;
                }
            }
        }
    }

    public class UpdateDatabase extends AsyncTask<DownloadingItem, Void, Void> {
        String a = "";
        double b = 0.0d;
        double c = 0.0d;
        int d = 0;
        int e = 0;

        UpdateDatabase() {
        }


        public Void doInBackground(DownloadingItem... downloadingItemArr) {
            try {
                DownloadingItem downloadingItem = downloadingItemArr[0];
                String name = downloadingItem.getName();
                this.a = name;
                if (name.equals("")) {
                    String substring = downloadingItem.getLocalFilePath().substring(downloadingItem.getLocalFilePath().lastIndexOf("/") + 1);
                    this.a = substring;
                    downloadingItem.setName(substring);
                }
                this.b = downloadingItem.getCurrentSize();
                this.c = downloadingItem.getTotalSize();
                this.d = downloadingItem.getDownloadId();
                this.e = downloadingItem.getIsInPause();
                downloadingItem.setProgress((int) ((downloadingItem.getCurrentSize() / downloadingItem.getTotalSize()) * 100.0d));
                DBDownloadManager.getInstance(DownloadService.this.context).addUpdateDownloadData(Integer.parseInt(String.valueOf(downloadingItem.getDownloadId())), downloadingItem, true);
                return null;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }


        public void onPreExecute() {
            super.onPreExecute();
            DownloadService.this.storeUserData.getSettings().isDownloadingNotification();
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (DownloadService.this.storeUserData.getSettings().isDownloadingNotification()) {
                DownloadService.this.showDownloadingNotification(this.a.replace("%20", " "), this.d, this.b, this.c, this.e);
            }
        }
    }

    private NotificationCompat.Builder Completed_Builder() {
        if (this.Complete_manager == null) {
            this.Complete_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        if (this.cBuilder == null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(67108864);
            intent.addFlags(536870912);
            this.cBuilder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher).setPriority(1).setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, 134217728)).setAutoCancel(false).setColor(getResources().getColor(R.color.purple_700));
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                if (a || this.vnotificationManager != null) {
                    this.cBuilder.setChannelId(CHANNEL_ID);
                    this.Complete_manager.createNotificationChannel(notificationChannel);
                } else {
                    throw new AssertionError();
                }
            }
            if (!a && this.Complete_manager == null) {
                throw new AssertionError();
            }
        }
        return this.cBuilder;
    }

    private void generateNotification(int i) {
        NotificationCompat.Builder vNotificationBuilder = getVNotificationBuilder();
        this.vBuilder = vNotificationBuilder;
        startForeground(i, vNotificationBuilder.build());
    }

    private NotificationCompat.Builder getVNotificationBuilder() {
        if (this.vnotificationManager == null) {
            this.vnotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        if (this.vBuilder == null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(67108864);
            intent.addFlags(536870912);
            this.vBuilder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher).setPriority(-1).setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, 134217728)).setAutoCancel(false).setColor(getResources().getColor(R.color.purple_700));
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, 2);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
                notificationChannel.enableVibration(false);
                notificationChannel.setShowBadge(false);
                if (a || this.vnotificationManager != null) {
                    this.vBuilder.setChannelId(CHANNEL_ID);
                    this.vnotificationManager.createNotificationChannel(notificationChannel);
                } else {
                    throw new AssertionError();
                }
            }
            if (!a && this.vnotificationManager == null) {
                throw new AssertionError();
            }
        }
        return this.vBuilder;
    }

    private void initService() {
        FileDownloadUtils.setDefaultSaveRootPath(FileDownloadUtils.generateFilePath(Environment.getExternalStorageDirectory().getAbsolutePath(), this.context.getResources().getString(R.string.app_name)));
        Log.e("Default Storage", FileDownloadUtils.getDefaultSaveRootPath());
        NetworkChangeReceiver.setOnNetoworkChangeListener(this);
    }

    public void retryQuedDownloadData() {
        for (DownloadingItem startDownload : DBDownloadManager.getInstance(this.context).getQuedDownloadData()) {
            startDownload(startDownload, true);
        }
    }

    public void showDownloadCompletedNotification(int i, String str) {
        this.cBuilder = Completed_Builder();
        if (this.Complete_manager == null) {
            this.Complete_manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        String trim = str.substring(str.lastIndexOf(47) + 1).trim();
        try {
            Bitmap createVideoThumbnail = ThumbnailUtils.createVideoThumbnail(str, 3);
            if (createVideoThumbnail != null) {
                this.cBuilder.setLargeIcon(createVideoThumbnail);
            }
            this.cBuilder.setContentTitle(URLDecoder.decode(trim, "UTF-8"));
            this.cBuilder.setContentText(this.context.getString(R.string.downloadcomp));
            this.cBuilder.setSmallIcon(R.mipmap.ic_launcher);
            this.cBuilder.setAutoCancel(true);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.Complete_manager.notify(i, this.cBuilder.build());
    }

    public void showDownloadingNotification(String str, int i, double d, double d2, int i2) {
        String str2;
        NotificationCompat.Builder builder;
        this.vBuilder = getVNotificationBuilder();
        if (this.vnotificationManager == null) {
            this.vnotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        try {
            if (this.storeUserData.getSettings().isDownloadingNotification()) {
                double d3 = (100.0d * d) / d2;
                DecimalFormat decimalFormat = new DecimalFormat("#");
                try {
                    this.vBuilder.setContentTitle(URLDecoder.decode(str, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (i2 == 0) {
                    builder = this.vBuilder;
                    str2 = this.context.getString(R.string.downloadprog) + decimalFormat.format(d3) + "%";
                } else {
                    builder = this.vBuilder;
                    str2 = this.context.getString(R.string.download_paused);
                }
                builder.setContentText(str2);
                try {
                    this.vBuilder.setProgress(Double.valueOf(d2).intValue(), Double.valueOf(d).intValue(), false);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                this.vnotificationManager.notify(i, this.vBuilder.build());
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    private void startDownload(DownloadingItem downloadingItem, boolean z) {
        int i;
        int i2 = DBDownloadManager.getInstance(this.context).getCurrentDownloadingCount() >= this.storeUserData.getSettings().getMaximumDownload() ? 4 : 0;
        if (i2 == 0) {
            i = FileDownloader.getImpl().create(downloadingItem.getUrl()).setPath(downloadingItem.getLocalFilePath(), false).setCallbackProgressTimes(600).setMinIntervalUpdateSpeed(1000).setAutoRetryTimes(2000).setForceReDownload(true).setListener(this.fileDownloadSampleListener).start();
            if (this.storeUserData.getSettings().isDownloadingNotification()) {
                generateNotification(i);
            }
        } else {
            i = FileDownloader.getImpl().create(downloadingItem.getUrl()).setPath(downloadingItem.getLocalFilePath(), false).setCallbackProgressTimes(600).setMinIntervalUpdateSpeed(1000).setAutoRetryTimes(2000).setForceReDownload(true).setListener(this.fileDownloadSampleListener).start();
        }
        downloadingItem.setName(downloadingItem.getName().replace("%20", " "));
        downloadingItem.setDownloadId(i);
        downloadingItem.setIsInPause(i2);
        downloadingItem.setLastModification(String.valueOf(System.currentTimeMillis()));
        DBDownloadManager.getInstance(this.context).addUpdateDownloadData(i, downloadingItem, z);
    }

    public void CancelNoti(int i) {
        try {
            if (this.vnotificationManager != null) {
                this.vnotificationManager.cancel(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDownloadData(String str, String str2, String str3, String str4) {
        int i;
        Context context2;
        MainActivity mainActivity;
        initService();
        if (!new File(str4).exists()) {
            DownloadingItem downloadingItem = new DownloadingItem(0, str3, str2, str, 0, 0.0d, 0.0d, 0, 0, 0, 0, str4, String.valueOf(System.currentTimeMillis()));
            if (!NetworkUtil.isConnected()) {
                mainActivity = MainActivity.getInstance();
                context2 = this.context;
                i = R.string.nointernet;
            } else if (!NetworkUtil.getMobileConnectivityStatus(this.context)) {
                startDownload(downloadingItem, false);
                return;
            } else if (this.storeUserData.getSettings().isMobile()) {
                startDownload(downloadingItem, false);
                return;
            } else {
                mainActivity = MainActivity.getInstance();
                context2 = this.context;
                i = R.string.disabled_mobile;
            }
        } else {
            mainActivity = MainActivity.getInstance();
            context2 = this.context;
            i = R.string.file_exits;
        }
        mainActivity.show_snack(context2.getString(i));
    }

    public String getRoot_Path(String str) {
        return AndroidUtils.Default_Root(str);
    }

    public IBinder onBind(Intent intent) {
        return this.downloadBind;
    }

    public void onConnected() {
        if (this.storeUserData.getSettings().isRetryDownload()) {
            new LoadDownloadingTask(this, this, (byte) 0).execute(new Void[0]);
        }
    }

    public void onDisConnected() {
        new LoadDownloadingTask(this, this, (byte) 0).execute(new Void[0]);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        this.context = this;
        this.storeUserData = new StoreUserData(this.context);
        initService();
        this.fileDownloadSampleListener = new FileDownloadSampleListener() {
            public final void connected(BaseDownloadTask baseDownloadTask, String str, boolean z, int i, int i2) {
            }

            public final void error(BaseDownloadTask baseDownloadTask, Throwable th) {
            }

            public final void paused(BaseDownloadTask baseDownloadTask, int i, int i2) {
            }

            public final void pending(BaseDownloadTask baseDownloadTask, int i, int i2) {
            }

            public final void warn(BaseDownloadTask baseDownloadTask) {
            }

            public final void completed(BaseDownloadTask baseDownloadTask) {
                super.completed(baseDownloadTask);
                try {
                    DownloadingItem downloadingDataById = DBDownloadManager.getInstance(DownloadService.this.context).getDownloadingDataById(baseDownloadTask.getId());
                    if (downloadingDataById != null) {
                        downloadingDataById.setLocalFilePath(baseDownloadTask.getPath());
                        new DownloadCompletedTask().execute(new DownloadingItem[]{downloadingDataById});
                    }
                    AndroidUtils.notifyMediaScannerService(DownloadService.this.context, baseDownloadTask.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public final void progress(BaseDownloadTask baseDownloadTask, int i, int i2) {
                super.progress(baseDownloadTask, i, i2);
                if (DownloadService.this.fileDownloadSampleListener != null) {
                    try {
                        DownloadingItem downloadingDataById = DBDownloadManager.getInstance(DownloadService.this.context).getDownloadingDataById(baseDownloadTask.getId());
                        if (downloadingDataById != null) {
                            downloadingDataById.setDownloadId(baseDownloadTask.getId());
                            int largeFileTotalBytes = i2 == -1 ? baseDownloadTask.getSmallFileTotalBytes() == -1 ? (int) baseDownloadTask.getLargeFileTotalBytes() : baseDownloadTask.getSmallFileTotalBytes() : i2;
                            downloadingDataById.setPercent((i / i2) * 100);
                            downloadingDataById.setProgress((i / i2) * 100);
                            downloadingDataById.setTotalSize(largeFileTotalBytes < 0 ? 0.0d : (double) largeFileTotalBytes);
                            downloadingDataById.setCurrentSize((double) i);
                            downloadingDataById.setSpeed(baseDownloadTask.getSpeed());
                            downloadingDataById.setLocalFilePath(baseDownloadTask.getPath());
                            new UpdateDatabase().execute(new DownloadingItem[]{downloadingDataById});
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        if (this.storeUserData.getSettings() == null || !this.storeUserData.getSettings().isRetryDownload()) {
            return START_STICKY;
        }
        retryQuedDownloadData();
        return START_STICKY;
    }

    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void pauseDownload(final DownloadingItem downloadingItem) {
        try {
            DBDownloadManager.getInstance(this.context).addUpdateDownloadData(Integer.parseInt(String.valueOf(downloadingItem.getDownloadId())), downloadingItem, true);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    FileDownloader.getImpl().pause(Integer.parseInt(String.valueOf(downloadingItem.getDownloadId())));
                }
            }, 500);
            if (this.storeUserData.getSettings().isDownloadingNotification()) {
                showDownloadingNotification(new File(downloadingItem.getLocalFilePath()).getName(), downloadingItem.getDownloadId(), downloadingItem.getCurrentSize(), downloadingItem.getTotalSize(), downloadingItem.getIsInPause());
            }
            if (DBDownloadManager.getInstance(this.context).getCurrentDownloadingCount() == 0) {
                stopForeground(true);
            } else if (this.storeUserData.getSettings().isRetryDownload()) {
                retryQuedDownloadData();
            }
            MainActivity.getInstance().show_snack(this.context.getString(R.string.sdownload_paused));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resumeDownload(DownloadingItem downloadingItem) {
        this.vBuilder = getVNotificationBuilder();
        try {
            if (DBDownloadManager.getInstance(this.context).getCurrentDownloadingCount() < this.storeUserData.getSettings().getMaximumDownload()) {
                downloadingItem.setIsInPause(0);
                downloadingItem.setLastModification(String.valueOf(System.currentTimeMillis()));
                downloadingItem.setProgress((int) ((downloadingItem.getCurrentSize() / downloadingItem.getTotalSize()) * 100.0d));
                if (!NetworkUtil.isConnected()) {
                    MainActivity.getInstance().show_snack(this.context.getString(R.string.nointernet));
                } else if (!NetworkUtil.getMobileConnectivityStatus(this.context)) {
                    DBDownloadManager.getInstance(this.context).addUpdateDownloadData(downloadingItem.getDownloadId(), downloadingItem, true);
                    FileDownloader.getImpl().create(downloadingItem.getUrl()).setPath(downloadingItem.getLocalFilePath(), false).setCallbackProgressTimes(600).setMinIntervalUpdateSpeed(1000).setAutoRetryTimes(2000).setListener(this.fileDownloadSampleListener).start();
                    if (this.storeUserData.getSettings().isDownloadingNotification()) {
                        showDownloadingNotification(new File(downloadingItem.getLocalFilePath()).getName(), downloadingItem.getDownloadId(), downloadingItem.getCurrentSize(), downloadingItem.getTotalSize(), downloadingItem.getIsInPause());
                        startForeground(downloadingItem.getDownloadId(), this.vBuilder.build());
                    }
                    MainActivity.getInstance().show_snack(this.context.getString(R.string.download_resume));
                } else if (this.storeUserData.getSettings().isMobile()) {
                    DBDownloadManager.getInstance(this.context).addUpdateDownloadData(downloadingItem.getDownloadId(), downloadingItem, true);
                    FileDownloader.getImpl().create(downloadingItem.getUrl()).setPath(downloadingItem.getLocalFilePath(), false).setCallbackProgressTimes(600).setMinIntervalUpdateSpeed(1000).setAutoRetryTimes(2000).setListener(this.fileDownloadSampleListener).start();
                    if (this.storeUserData.getSettings().isDownloadingNotification()) {
                        showDownloadingNotification(new File(downloadingItem.getLocalFilePath()).getName(), downloadingItem.getDownloadId(), downloadingItem.getCurrentSize(), downloadingItem.getTotalSize(), downloadingItem.getIsInPause());
                        startForeground(downloadingItem.getDownloadId(), this.vBuilder.build());
                    }
                    MainActivity.getInstance().show_snack(this.context.getString(R.string.download_resume));
                } else {
                    MainActivity.getInstance().show_snack(this.context.getString(R.string.disabled_mobile));
                }
            } else {
                MainActivity.getInstance().show_snack(this.context.getString(R.string.queuelimit));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
