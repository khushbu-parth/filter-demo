package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import cn.dreamtobe.threaddebugger.CommonThreadKey;
import cn.dreamtobe.threaddebugger.IThreadDebugger;
import cn.dreamtobe.threaddebugger.ThreadDebugger;
import cn.dreamtobe.threaddebugger.ThreadDebuggers;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.SettingsItem;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.services.DownloadService;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.AdBlocker;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.StoreUserData;


public class WebApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "FileDownloadApplication";
    private static WebApplication mInstance;

    public final ServiceConnection downloadConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            WebApplication.this.downloadService = ((DownloadService.DownloadBinder) iBinder).getService();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            WebApplication.this.downloadService.stopForeground(false);
        }
    };

    public Intent downloadIntent;
    public DownloadService downloadService;

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public static synchronized WebApplication getInstance() {
        WebApplication webApplication;
        Class cls = WebApplication.class;
        synchronized (cls) {
            synchronized (cls) {
                webApplication = mInstance;
            }
        }
        return webApplication;
    }



    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    public void onCreate() {
        super.onCreate();
        mInstance = this;
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        AdBlocker.init(getApplicationContext());
        if (new StoreUserData(mInstance.getApplicationContext()).getSettings() == null) {
            new StoreUserData(mInstance.getApplicationContext()).setSettings(new SettingsItem());
        }
        FileDownloader.setupOnApplicationOnCreate(this).connectionCreator(new FileDownloadUrlConnection.Creator(new FileDownloadUrlConnection.Configuration().connectTimeout(15000).readTimeout(15000))).commit();
        ThreadDebugger.install(ThreadDebuggers.create().add(CommonThreadKey.OpenSource.OKHTTP).add(CommonThreadKey.OpenSource.OKIO).add(CommonThreadKey.System.BINDER).add(FileDownloadUtils.getThreadPoolName("Network"), "Network").add(FileDownloadUtils.getThreadPoolName("Flow"), "FlowSingle").add(FileDownloadUtils.getThreadPoolName("EventPool"), "Event").add(FileDownloadUtils.getThreadPoolName("LauncherTask"), "LauncherTask").add(FileDownloadUtils.getThreadPoolName("ConnectionBlock"), "Connection").add(FileDownloadUtils.getThreadPoolName("RemitHandoverToDB"), "RemitHandoverToDB").add(FileDownloadUtils.getThreadPoolName("BlockCompleted"), "BlockCompleted"), 2000, new ThreadDebugger.ThreadChangedCallback() {
            public void onChanged(IThreadDebugger iThreadDebugger) {
                Log.d(WebApplication.TAG, iThreadDebugger.drawUpEachThreadInfoDiff());
                Log.d(WebApplication.TAG, iThreadDebugger.drawUpEachThreadSizeDiff());
                Log.d(WebApplication.TAG, iThreadDebugger.drawUpEachThreadSize());
            }
        });
        registerActivityLifecycleCallbacks(this);
    }


}
