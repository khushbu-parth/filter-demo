package com.lib.screenrecorder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.text.format.DateUtils;

public class Notifications extends ContextWrapper {
    public static final String ACTION_STOP = "Com.lib.screenrecorder.action.STOP";
    protected Notification.Builder mBuilder;
    protected long mLastFiredTime;
    protected NotificationManager mManager;
    protected Notification.Action mStopAction;

    public Notifications(Context context) {
        super(context);
        this.mLastFiredTime = 0L;
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
        }
    }

    protected String getChannelId() {
        return "Recording";
    }

    protected String getChannelName() {
        return "Screen Recorder Notifications";
    }

    protected int getId() {
        return 8191;
    }

    public void recording(long j) {
        if (SystemClock.elapsedRealtime() - this.mLastFiredTime < 1000) {
            return;
        }
        Notification.Builder builder = getBuilder();
        getNotificationManager().notify(getId(), builder.setContentText("Length: " + DateUtils.formatElapsedTime(j / 1000)).build());
        this.mLastFiredTime = SystemClock.elapsedRealtime();
    }

    protected Notification.Builder getBuilder() {
        if (this.mBuilder == null) {
            Notification.Builder when = new Notification.Builder(this).setContentTitle("Recording...").setOngoing(true).setLocalOnly(true).setOnlyAlertOnce(true).addAction(stopAction()).setWhen(System.currentTimeMillis());
            if (Build.VERSION.SDK_INT >= 26) {
                when.setChannelId(getChannelId()).setUsesChronometer(true);
            }
            this.mBuilder = when;
        }
        return this.mBuilder;
    }

    protected void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(getChannelId(), getChannelName(), 2);
        notificationChannel.setShowBadge(false);
        getNotificationManager().createNotificationChannel(notificationChannel);
    }

    protected Notification.Action stopAction() {
        if (this.mStopAction == null) {
            this.mStopAction = new Notification.Action(17301539, "Stop", PendingIntent.getBroadcast(this, 1, new Intent(ACTION_STOP), 1073741824));
        }
        return this.mStopAction;
    }

    public void clear() {
        this.mLastFiredTime = 0L;
        this.mBuilder = null;
        this.mStopAction = null;
        getNotificationManager().cancelAll();
    }

    protected NotificationManager getNotificationManager() {
        if (this.mManager == null) {
            this.mManager = (NotificationManager) getSystemService("notification");
        }
        return this.mManager;
    }
}
