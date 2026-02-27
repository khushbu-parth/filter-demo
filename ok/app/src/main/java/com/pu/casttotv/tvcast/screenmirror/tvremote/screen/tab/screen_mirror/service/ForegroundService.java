package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Binder;
import android.os.Build;
import android.os.HandlerThread;
import android.os.IBinder;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.MyApplication;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.SMWebActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data.BusMessages;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data.HttpServer;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data.NotifyImageGenerator;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@SuppressLint("WrongConstant")
public final class ForegroundService extends Service {
    private static ForegroundService sServiceInstance;
    private boolean isServicePrepared;
    private BroadcastReceiver mBroadcastReceiver;
    private ForegroundServiceHandler mForegroundServiceTaskHandler;
    private HandlerThread mHandlerThread;
    private HttpServer mHttpServer;
    private BroadcastReceiver mLocalNotificationReceiver;
    private MediaProjection mMediaProjection;
    private MediaProjectionManager mMediaProjectionManager;
    private NotifyImageGenerator mNotifyImageGenerator;
    private MediaProjection.Callback mProjectionCallback;
    public NotificationManager notificationManager;
    public NotificationManager notificationManagerStart;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ForegroundService.class).putExtra("EXTRA_SERVICE_MESSAGE", "SERVICE_MESSAGE_PREPARE_STREAMING");
    }

    public static void stopService() {
        sServiceInstance.stopSelf();
    }

    public static void setMediaProjection(MediaProjection mediaProjection) {
        sServiceInstance.mMediaProjection = mediaProjection;
    }

    public static MediaProjectionManager getProjectionManager() {
        ForegroundService foregroundService = sServiceInstance;
        if (foregroundService == null) {
            return null;
        }
        return foregroundService.mMediaProjectionManager;
    }

    public static MediaProjection getMediaProjection() {
        ForegroundService foregroundService = sServiceInstance;
        if (foregroundService == null) {
            return null;
        }
        return foregroundService.mMediaProjection;
    }

    @Override // android.app.Service
    public void onCreate() {
        sServiceInstance = this;
        MyApplication.getAppData().initIndexHtmlPage(this);
        this.mMediaProjectionManager = (MediaProjectionManager) getSystemService("media_projection");
        this.mProjectionCallback = new MediaProjection.Callback() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.service.ForegroundService.1
            @Override // android.media.projection.MediaProjection.Callback
            public void onStop() {
                ForegroundService.this.serviceStopStreaming();
            }
        };
        this.mHttpServer = new HttpServer();
        MyApplication.getAppData().getImageQueue().clear();
        NotifyImageGenerator notifyImageGenerator = new NotifyImageGenerator(getApplicationContext());
        this.mNotifyImageGenerator = notifyImageGenerator;
        notifyImageGenerator.addDefaultScreen();
        HandlerThread handlerThread = new HandlerThread(ForegroundService.class.getSimpleName(), -1);
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mForegroundServiceTaskHandler = new ForegroundServiceHandler(this.mHandlerThread.getLooper());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION_NOTIFY_START_STREAM");
        intentFilter.addAction("ACTION_NOTIFY_STOP_STREAM");
        intentFilter.addAction("ACTION_NOTIFY_CLOSE_APP");
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.service.ForegroundService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                action.hashCode();
                char c = 65535;
                switch (action.hashCode()) {
                    case -791631088:
                        if (action.equals("ACTION_NOTIFY_STOP_STREAM")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 408193994:
                        if (action.equals("ACTION_NOTIFY_START_STREAM")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 705235085:
                        if (action.equals("ACTION_NOTIFY_CLOSE_APP")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        ForegroundService.this.serviceStopStreaming();
                        return;
                    case 1:
                        if (!MyApplication.getAppData().isActivityRunning()) {
                            ForegroundService foregroundService = ForegroundService.this;
                            foregroundService.startActivity(SMWebActivity.getStartIntent(foregroundService.getApplicationContext()).addFlags(268435456));
                        }
                        BusMessages busMessages = (BusMessages) EventBus.getDefault().getStickyEvent(BusMessages.class);
                        if (busMessages != null && !"MESSAGE_STATUS_HTTP_OK".equals(busMessages.getMessage())) {
                            return;
                        }
                        EventBus.getDefault().postSticky(new BusMessages("MESSAGE_ACTION_STREAMING_TRY_START"));
                        return;
                    case 2:
                        ForegroundService.stopService();
                        EventBus.getDefault().post(new MessageEvent("KEY_EXIT_MIRROR_WEB"));
                        return;
                    default:
                        return;
                }
            }
        };
        this.mLocalNotificationReceiver = broadcastReceiver;
        registerReceiver(broadcastReceiver, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.SCREEN_OFF");
        intentFilter2.addAction("android.net.wifi.STATE_CHANGE");
        BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() { // from class: com.magicapps.casttotv.tv.screen.tab.screen_mirror.service.ForegroundService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                boolean isWiFiConnected;
                String action = intent.getAction();
                if ("android.intent.action.SCREEN_OFF".equals(action) && MyApplication.getAppPreference().isStopOnSleep() && MyApplication.getAppData().isStreamRunning()) {
                    ForegroundService.this.serviceStopStreaming();
                }
                if (!"android.net.wifi.STATE_CHANGE".equals(action) || MyApplication.getScreenMirrorViewModel().isWiFiConnected() == (isWiFiConnected = MyApplication.getAppData().isWiFiConnected())) {
                    return;
                }
                MyApplication.getScreenMirrorViewModel().setServerAddress(MyApplication.getAppData().getServerAddress());
                MyApplication.getScreenMirrorViewModel().setWiFiConnected(isWiFiConnected);
                if (MyApplication.getScreenMirrorViewModel().isWiFiConnected()) {
                    EventBus.getDefault().post(new BusMessages("MESSAGE_ACTION_HTTP_RESTART"));
                }
                if (MyApplication.getScreenMirrorViewModel().isWiFiConnected() || !MyApplication.getAppData().isStreamRunning()) {
                    return;
                }
                ForegroundService.this.serviceStopStreaming();
            }
        };
        this.mBroadcastReceiver = broadcastReceiver2;
        registerReceiver(broadcastReceiver2, intentFilter2);
        EventBus.getDefault().register(this);
        this.mHttpServer.start();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        if ("SERVICE_MESSAGE_PREPARE_STREAMING".equals(intent.getStringExtra("EXTRA_SERVICE_MESSAGE"))) {
            if (!this.isServicePrepared) {
                createNotificationStart();
            }
            this.isServicePrepared = true;
            return 2;
        }
        return 2;
    }

    @Override // android.app.Service
    public void onDestroy() {
        stopAll();
    }

    private void stopAll() {
        try {
            EventBus.getDefault().unregister(this);
            this.mHttpServer.stop(null);
            unregisterReceiver(this.mBroadcastReceiver);
            unregisterReceiver(this.mLocalNotificationReceiver);
            MediaProjection mediaProjection = this.mMediaProjection;
            if (mediaProjection != null) {
                mediaProjection.unregisterCallback(this.mProjectionCallback);
                this.mMediaProjection.stop();
            }
            this.mHandlerThread.quit();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Subscribe
    public void onMessageEvent(BusMessages busMessages) {
        String message = busMessages.getMessage();
        message.hashCode();
        char c = 65535;
        switch (message.hashCode()) {
            case -871847863:
                if (message.equals("MESSAGE_ACTION_HTTP_RESTART")) {
                    c = 0;
                    break;
                }
                break;
            case -482600620:
                if (message.equals("MESSAGE_ACTION_STREAMING_START")) {
                    c = 1;
                    break;
                }
                break;
            case 677169328:
                if (message.equals("MESSAGE_ACTION_STREAMING_STOP")) {
                    c = 2;
                    break;
                }
                break;
            case 1144669732:
                if (message.equals("MESSAGE_ACTION_PIN_UPDATE")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                MyApplication.getAppData().getImageQueue().clear();
                this.mHttpServer.stop(this.mNotifyImageGenerator.getClientNotifyImage("MESSAGE_ACTION_HTTP_RESTART"));
                this.mNotifyImageGenerator.addDefaultScreen();
                this.mHttpServer.start();
                return;
            case 1:
                serviceStartStreaming();
                return;
            case 2:
                serviceStopStreaming();
                return;
            case 3:
                MyApplication.getAppData().getImageQueue().clear();
                this.mHttpServer.stop(this.mNotifyImageGenerator.getClientNotifyImage("MESSAGE_ACTION_PIN_UPDATE"));
                this.mNotifyImageGenerator.addDefaultScreen();
                this.mHttpServer.start();
                return;
            default:
                return;
        }
    }

    private void serviceStartStreaming() {
        if (MyApplication.getAppData().isStreamRunning()) {
            return;
        }
        try {
            NotificationManager notificationManager = this.notificationManager;
            if (notificationManager != null) {
                notificationManager.cancel(111);
            }
            NotificationManager notificationManager2 = this.notificationManagerStart;
            if (notificationManager2 != null) {
                notificationManager2.cancel(110);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        createNotificationStop();
        this.mForegroundServiceTaskHandler.obtainMessage(0).sendToTarget();
        MediaProjection mediaProjection = this.mMediaProjection;
        if (mediaProjection == null) {
            return;
        }
        mediaProjection.registerCallback(this.mProjectionCallback, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void serviceStopStreaming() {
        if (!MyApplication.getAppData().isStreamRunning()) {
            return;
        }
        this.mForegroundServiceTaskHandler.obtainMessage(1).sendToTarget();
        MediaProjection mediaProjection = this.mMediaProjection;
        if (mediaProjection != null) {
            mediaProjection.unregisterCallback(this.mProjectionCallback);
            this.mMediaProjection.stop();
        }
        MyApplication.getAppData().getImageQueue().clear();
        this.mNotifyImageGenerator.addDefaultScreen();
        if (MyApplication.getAppPreference().isEnablePin() && MyApplication.getAppPreference().isAutoChangePin()) {
            MyApplication.getAppPreference().generateAndSaveNewPin();
            MyApplication.getScreenMirrorViewModel().setStreamPin(MyApplication.getAppPreference().getCurrentPin());
            EventBus.getDefault().post(new BusMessages("MESSAGE_ACTION_PIN_UPDATE"));
        }
        stopForeground(true);
        createNotificationStart();
    }

    private void createNotificationStart() {
        PendingIntent activity;
        PendingIntent broadcast;
        PendingIntent broadcast2;
        Intent intent = new Intent(this, SMWebActivity.class);
        intent.addFlags(268435456);
        int i = Build.VERSION.SDK_INT;
        if (i >= 31) {
            activity = PendingIntent.getActivity(getApplicationContext(), 0, intent, 67108864);
        } else {
            activity = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        }
        if (i >= 31) {
            broadcast = PendingIntent.getBroadcast(this, 0, new Intent("ACTION_NOTIFY_START_STREAM"), 67108864);
        } else {
            broadcast = PendingIntent.getBroadcast(this, 0, new Intent("ACTION_NOTIFY_START_STREAM"), 0);
        }
        if (i >= 31) {
            broadcast2 = PendingIntent.getBroadcast(this, 0, new Intent("ACTION_NOTIFY_CLOSE_APP"), 67108864);
        } else {
            broadcast2 = PendingIntent.getBroadcast(this, 0, new Intent("ACTION_NOTIFY_CLOSE_APP"), 0);
        }
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setContentIntent(activity).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(getString(R.string.service_ready_to_stream)).setContentText(getString(R.string.service_press_start)).setWhen(System.currentTimeMillis()).addAction(R.drawable.ic_service_start_24dp, getString(R.string.service_start).toUpperCase(), broadcast).addAction(R.drawable.ic_service_exit_24dp, getString(R.string.service_exit).toUpperCase(), broadcast2);
        if (i >= 26) {
            builder.setChannelId("NOTIFICATION_START_STREAMING");
        }
        if (i >= 26) {
            try {
                this.notificationManagerStart = (NotificationManager) getSystemService("notification");
                this.notificationManagerStart.createNotificationChannel(new NotificationChannel("NOTIFICATION_START_STREAMING", "NOTIFICATION_START_STREAMING", 2));
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        }
        Notification build = builder.build();
        build.defaults = 1;
        startForeground(110, build);
    }

    private void createNotificationStop() {
        PendingIntent activity;
        PendingIntent broadcast;
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        Intent intent = new Intent(this, SMWebActivity.class);
        intent.addFlags(268435456);
        intent.putExtra("ForegroundService", 1);
        int i = Build.VERSION.SDK_INT;
        if (i >= 31) {
            activity = PendingIntent.getActivity(getApplicationContext(), 0, intent, 67108864);
        } else {
            activity = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        }
        if (i >= 31) {
            broadcast = PendingIntent.getBroadcast(this, 0, new Intent("ACTION_NOTIFY_STOP_STREAM"), 67108864);
        } else {
            broadcast = PendingIntent.getBroadcast(this, 0, new Intent("ACTION_NOTIFY_STOP_STREAM"), 0);
        }
        Notification.Builder contentTitle = builder.setContentIntent(activity).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(getString(R.string.service_stream));
        contentTitle.setContentText(getString(R.string.service_go_to) + MyApplication.getAppData().getServerAddress()).setWhen(System.currentTimeMillis()).addAction(R.drawable.ic_service_stop_24dp, getString(R.string.service_stop).toUpperCase(), broadcast);
        if (i >= 26) {
            builder.setChannelId("NOTIFICATION_START_STREAMING");
        }
        if (i >= 26) {
            try {
                this.notificationManager = (NotificationManager) getSystemService("notification");
                this.notificationManager.createNotificationChannel(new NotificationChannel("NOTIFICATION_START_STREAMING", "NOTIFICATION_START_STREAMING", 2));
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        }
        Notification build = builder.build();
        build.defaults = 1;
        startForeground(111, build);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return new ServerServiceBinder();
    }

    /* loaded from: classes4.dex */
    public class ServerServiceBinder extends Binder {
        public ServerServiceBinder() {
        }

        public ForegroundService getServerService() {
            return ForegroundService.this;
        }
    }
}
