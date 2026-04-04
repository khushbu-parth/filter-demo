package com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.telecom.Call;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import androidx.core.content.ContextCompat;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.internal.telephony.ITelephony;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.notification.BoloNotificationListenerService;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.BoloCallHandler;

import java.lang.reflect.Method;
import java.util.Locale;


public class AcceptDeclineCallManager {
    public static void acceptCall(boolean z, Context context, Call call) {
        try {
            if (Build.VERSION.SDK_INT >= 23 && call != null) {
                call.answer(0);
                return;
            }
        } catch (Exception unused) {
        }
        Context applicationContext = context.getApplicationContext();
        try {
            if (tryAcceptingCall(context)) {
                return;
            }
            AudioManager audioManager = (AudioManager) applicationContext.getSystemService("audio");
            if (BoloNotificationListenerService.checkAccessibility(context)) {
                for (MediaController mediaController : ((MediaSessionManager) context.getSystemService("media_session")).getActiveSessions(new ComponentName(context, BoloNotificationListenerService.class))) {
                    mediaController.dispatchMediaButtonEvent(new KeyEvent(1, 79));
                }
            } else {
                KeyEvent keyEvent = new KeyEvent(0, 79);
                KeyEvent keyEvent2 = new KeyEvent(1, 79);
                audioManager.dispatchMediaKeyEvent(keyEvent);
                audioManager.dispatchMediaKeyEvent(keyEvent2);
            }
            if (BoloCallHandler.getInstance().getNotificationActions() != null) {
                for (int i = 0; i < BoloCallHandler.getInstance().getNotificationActions().length; i++) {
                    Notification.Action action = BoloCallHandler.getInstance().getNotificationActions()[i];
                    if (action.title.toString().equalsIgnoreCase("answer") || i == BoloCallHandler.getInstance().getNotificationActions().length - 1) {
                        action.actionIntent.send(context, 0, new Intent());
                    }
                }
            }
        } catch (Exception unused2) {
            Intent putExtra = new Intent("android.intent.action.MEDIA_BUTTON").putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(0, 79));
            Intent putExtra2 = new Intent("android.intent.action.MEDIA_BUTTON").putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(1, 79));
            applicationContext.sendOrderedBroadcast(putExtra, "android.permission.CALL_PRIVILEGED");
            applicationContext.sendOrderedBroadcast(putExtra2, "android.permission.CALL_PRIVILEGED");
        } catch (Throwable unused3) {
        }
    }

    private static void broadcastHeadsetConnected(boolean z, Context context) {
        Intent intent = new Intent("android.intent.action.HEADSET_PLUG");
        intent.addFlags(1073741824);
        intent.putExtra("state", z ? 1 : 0);
        intent.putExtra("name", "mysms");
        try {
            context.sendOrderedBroadcast(intent, null);
        } catch (Exception unused) {
        }
    }

    public static void declineCall(Context context, Call call, String str) {
        if (context == null) {
            return;
        }
        try {
            try {
                if (Build.VERSION.SDK_INT >= 23 && call != null) {
                    if (str == null) {
                        call.reject(false, null);
                        return;
                    } else {
                        call.reject(true, str);
                        return;
                    }
                }
            } catch (Exception unused) {
                Log.e("", "");
            }
            if (tryDecliningCall(context)) {
                return;
            }
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                Method declaredMethod = Class.forName(telephonyManager.getClass().getName()).getDeclaredMethod("getITelephony", new Class[0]);
                declaredMethod.setAccessible(true);
                ((ITelephony) declaredMethod.invoke(telephonyManager, new Object[0])).endCall();
                endCallFromNotification(context);
            } catch (Exception unused2) {
                endCallFromNotification(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void endCallFromNotification(Context context) {
        if (BoloCallHandler.getInstance().getNotificationActions() != null) {
            Notification.Action action = null;
            for (int i = 0; i < BoloCallHandler.getInstance().getNotificationActions().length; i++) {
                action = BoloCallHandler.getInstance().getNotificationActions()[i];
                if (action.title.toString().equalsIgnoreCase("dismiss")) {
                    break;
                }
            }
            if (action == null && BoloCallHandler.getInstance().getNotificationActions().length > 0) {
                action = BoloCallHandler.getInstance().getNotificationActions()[0];
            }
            if (action != null) {
                try {
                    action.actionIntent.send(context, 0, new Intent());
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            final AudioManager audioManager = (AudioManager) BoloApplication.getApplication().getSystemService("audio");
            if (BoloNotificationListenerService.checkAccessibility(context)) {
                for (final MediaController mediaController : ((MediaSessionManager) context.getSystemService("media_session")).getActiveSessions(new ComponentName(context, BoloNotificationListenerService.class))) {
                    mediaController.dispatchMediaButtonEvent(new KeyEvent(1, 79));
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.AcceptDeclineCallManager.1
                        @Override 
                        public void run() {
                            new Handler().postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.AcceptDeclineCallManager.1.1
                                @Override 
                                public void run() {
                                    mediaController.dispatchMediaButtonEvent(new KeyEvent(1, 79));
                                }
                            }, 400L);
                        }
                    });
                }
                return;
            }
            KeyEvent keyEvent = new KeyEvent(0, 79);
            KeyEvent keyEvent2 = new KeyEvent(1, 79);
            audioManager.dispatchMediaKeyEvent(keyEvent);
            audioManager.dispatchMediaKeyEvent(keyEvent2);
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.AcceptDeclineCallManager.2
                @Override 
                public void run() {
                    new Handler().postDelayed(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.AcceptDeclineCallManager.2.1
                        @Override 
                        public void run() {
                            KeyEvent keyEvent3 = new KeyEvent(0, 79);
                            KeyEvent keyEvent4 = new KeyEvent(1, 79);
                            audioManager.dispatchMediaKeyEvent(keyEvent3);
                            audioManager.dispatchMediaKeyEvent(keyEvent4);
                        }
                    }, 400L);
                }
            });
        } catch (Exception unused) {
        }
    }

    private static boolean tryAcceptingCall(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            TelecomManager telecomManager = (TelecomManager) context.getSystemService("telecom");
            if (ContextCompat.checkSelfPermission(context, BoloPermission.ANSWER_PHONE_CALLS) == 0 || ContextCompat.checkSelfPermission(context, "android.permission.MODIFY_PHONE_STATE") == 0) {
                telecomManager.acceptRingingCall();
                return true;
            }
            return false;
        }
        boolean z = "htc" == Build.MANUFACTURER.toLowerCase(Locale.US) && !((AudioManager) context.getSystemService("audio")).isWiredHeadsetOn();
        if (z) {
            broadcastHeadsetConnected(false, context);
        }
        try {
            try {
                Runtime.getRuntime().exec("input keyevent " + Integer.toString(79));
            } catch (Exception unused) {
                Intent putExtra = new Intent("android.intent.action.MEDIA_BUTTON").putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(0, 79));
                Intent putExtra2 = new Intent("android.intent.action.MEDIA_BUTTON").putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(1, 79));
                context.sendOrderedBroadcast(putExtra, "android.permission.CALL_PRIVILEGED");
                context.sendOrderedBroadcast(putExtra2, "android.permission.CALL_PRIVILEGED");
            }
            return false;
        } finally {
            if (z) {
                broadcastHeadsetConnected(false, context);
            }
        }
    }

    private static boolean tryDecliningCall(Context context) {
        TelecomManager telecomManager;
        if (Build.VERSION.SDK_INT < 28 || (telecomManager = (TelecomManager) context.getSystemService("telecom")) == null || ContextCompat.checkSelfPermission(context, BoloPermission.ANSWER_PHONE_CALLS) != 0) {
            return false;
        }
        telecomManager.endCall();
        return true;
    }
}
