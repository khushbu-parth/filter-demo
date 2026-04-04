package com.colorcallscreen.colorphone.callscreen.calltheme.service.notification;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.colorcallscreen.colorphone.callscreen.calltheme.BuildConfig;
import com.colorcallscreen.colorphone.callscreen.calltheme.controller.VideoCallController;
import com.colorcallscreen.colorphone.callscreen.calltheme.singleton.BoloSingleTon;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.images.UsersImageHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.BoloCallHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.ContactsHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class BoloNotificationListenerService extends NotificationListenerService {
    public static List<String> packageNamesToHandle = Arrays.asList(VideoCallController.AppPackage.WHATSAPP, VideoCallController.AppPackage.WHATSAPP_BUSSINESS, "com.viber.voip", "com.imo.android.imoim", "org.telegram.messenger", "com.google.android.apps.fireball", "jp.naver.line.android");
    private HashMap<String, HashMap<String, NotificationWear>> wearableWrapper;

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return true;
    }

    public static boolean checkAccessibility(Context context) {
        String str;
        try {
            str = context.getApplicationContext().getPackageName();
        } catch (Exception unused) {
            str = null;
        }
        if (str == null) {
            str = BuildConfig.APPLICATION_ID;
        }
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            contentResolver = context.getContentResolver();
        }
        try {
            String string = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
            if (string != null) {
                return string.contains(str);
            }
            return true;
        } catch (Exception unused2) {
            return true;
        }
    }

    private boolean saveReply(StatusBarNotification statusBarNotification, String str, String str2) {
        if (str != null && str2 != null) {
            try {
                if (ContextCompat.checkSelfPermission(this, BoloPermission.READ_CONTACTS) != 0) {
                    return false;
                }
                for (String str3 : ContactsHandler.contactNumberFromName(str2, this)) {
                    try {
                        String updatePhoneNumberWithISDCode = Utility.updatePhoneNumberWithISDCode(this, str3);
                        this.wearableWrapper = BoloSingleTon.getInstance(this).getWearableHashMap();
                        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender(statusBarNotification.getNotification());
                        HashMap<String, NotificationWear> hashMap = this.wearableWrapper.get(str);
                        NotificationWear notificationWear = new NotificationWear(wearableExtender, updatePhoneNumberWithISDCode, statusBarNotification.getNotification(), str);
                        if ((notificationWear.getAction() == null && notificationWear.getActionExtra() == null) || str.equals("") || updatePhoneNumberWithISDCode.equals("")) {
                            break;
                        }
                        if (hashMap == null) {
                            hashMap = new HashMap<>();
                        }
                        hashMap.put(updatePhoneNumberWithISDCode, notificationWear);
                        this.wearableWrapper.put(str, hashMap);
                        return true;
                    } catch (Exception unused) {
                    }
                }
            } catch (Exception unused2) {
            }
        }
        return false;
    }

    @Override // android.service.notification.NotificationListenerService
    public StatusBarNotification[] getActiveNotifications() {
        StatusBarNotification[] statusBarNotificationArr = null;
        try {
            statusBarNotificationArr = super.getActiveNotifications();
        } catch (SecurityException unused) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarNotificationArr == null ? new StatusBarNotification[0] : statusBarNotificationArr;
    }

    @Override // android.service.notification.NotificationListenerService, android.app.Service
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override // android.service.notification.NotificationListenerService
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        String packageName = statusBarNotification.getPackageName();
        if (packageNamesToHandle.contains(packageName)) {
            String string = statusBarNotification.getNotification().extras.getString(NotificationCompat.EXTRA_TITLE);
            UsersImageHandler.handleImageForNameAndNotification(string, statusBarNotification, this);
            saveReply(statusBarNotification, packageName, string);
        }
        if ((packageName.contains("incallui") || packageName.contains("com.huawei.systemmanger") || packageName.equalsIgnoreCase("com.android.dialer")) && statusBarNotification.getNotification().actions != null && statusBarNotification.getNotification().actions.length > 1) {
            BoloCallHandler.getInstance().setNotificationActions(statusBarNotification.getNotification().actions);
        }
    }

    @Override // android.app.Service
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);
        return 1;
    }
}
