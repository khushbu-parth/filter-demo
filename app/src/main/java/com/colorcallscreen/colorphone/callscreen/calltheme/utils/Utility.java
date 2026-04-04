package com.colorcallscreen.colorphone.callscreen.calltheme.utils;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.load.Key;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityFullScreenView;
import com.colorcallscreen.colorphone.callscreen.calltheme.custom.GifWallpaper;
import com.colorcallscreen.colorphone.callscreen.calltheme.custom.VideoWallpaper;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ThemeModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.MainActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.controller.VideoCallController;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.images.UsersImageHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.notification.BoloNotificationListenerService;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.notification.NotificationWear;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.ContactsHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.singleton.BoloSingleTon;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Utility {
    public static final int DEFAULT_APP_REQ = 320;
    public static final String IS_DEFAULT_PACKAGE_NOT_FOUND = "default_dailer_failed";
    public static int PICK_CONTACT = 103;
    private static AnimatorSet animatorSet = null;
    private static int count = 0;
    public static boolean isConnectedToInternet = false;
    public static boolean isVoiceRecogzinerAvailable = true;
    private static ThemeModel model;
    private static String path;
    private static Thread thread;

    public static void logEventNew(String str, String str2) {
    }

    public static void resizeText(TextView textView, int i, int i2) {
    }

    public static String stripExtension(String str) {
        return (str == null || str.lastIndexOf(".") <= 0) ? str : str.substring(0, str.lastIndexOf("."));
    }

    static int access$012(int i) {
        int i2 = count + i;
        count = i2;
        return i2;
    }

    static int access$020(int i) {
        int i2 = count - i;
        count = i2;
        return i2;
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static void cancelBounce() {
        AnimatorSet animatorSet2 = animatorSet;
        if (animatorSet2 != null) {
            animatorSet2.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
        }
    }

    public static void checkAndUpdateVoiceRecognizerStatus(Context context) {
        String checkIfVoiceRecognizerEnabled = checkIfVoiceRecognizerEnabled(context);
        if (!checkIfVoiceRecognizerEnabled.equals("no_google_service") && !checkIfVoiceRecognizerEnabled.equals("no_service")) {
            isVoiceRecogzinerAvailable = true;
        } else {
            isVoiceRecogzinerAvailable = false;
        }
    }

    public static boolean checkAppIsInstalledOrNot(Context context, String str) {
        for (ApplicationInfo applicationInfo : BoloApplication.getApplication().getPackageManager().getInstalledApplications(0)) {
            if (applicationInfo.packageName.contains(str)) {
                return true;
            }
        }
        return false;
    }

    private static String checkIfVoiceRecognizerEnabled(Context context) {
        List<ResolveInfo> queryIntentServices = context.getPackageManager().queryIntentServices(new Intent("android.speech.RecognitionService"), 0);
        if (queryIntentServices.size() == 0) {
            return "no_service";
        }
        if (queryIntentServices.size() == 1) {
            return queryIntentServices.get(0).toString().indexOf("google") != -1 ? queryIntentServices.get(0).serviceInfo.packageName + "/" + queryIntentServices.get(0).serviceInfo.name : "no_google_service";
        }
        String str = "";
        for (ResolveInfo resolveInfo : queryIntentServices) {
            if (resolveInfo.toString().indexOf("google") != -1) {
                str = resolveInfo.serviceInfo.packageName + "/" + resolveInfo.serviceInfo.name;
                if ("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService".equals(str)) {
                    return "com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService";
                }
            }
        }
        return str.equals("") ? "no_google_service" : str;
    }

    public static void checkInternetSpeed(boolean z) {
        boolean isConnectedFast = isConnectedFast(BoloApplication.getApplication());
        isConnectedToInternet = isConnectedFast;
        if (isConnectedFast && getWifiStrength() > 1) {
            isConnectedToInternet = true;
        } else {
            isConnectedToInternet = false;
        }
    }

    public static void cleanUpInternetSpeedChecker() {
        isConnectedToInternet = false;
        Thread thread2 = thread;
        if (thread2 != null) {
            thread2.interrupt();
            thread = null;
        }
    }

    public static List<String> convertToLowerCase(List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(list.get(i).toLowerCase());
        }
        return arrayList;
    }

    public static String createThemeName(String str) {
        return str.replaceAll("[:/.]", "");
    }

    public static Date dateAfterTime(int i, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(i2, i);
        return calendar.getTime();
    }

    public static void fireTestingNotification(String str) {
        PendingIntent pendingIntentParentCall = Helper.pendingIntentParentCall(false, true);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(BoloApplication.getApplication(), "channel vani test");
        builder.setContentIntent(pendingIntentParentCall);
        NotificationManager notificationManager = (NotificationManager) BoloApplication.getApplication().getSystemService("notification");
        builder.setSmallIcon(R.drawable.notification_call);
        builder.setContentTitle("Vani Test - ");
        builder.setContentText(str);
        builder.setPriority(1);
        builder.setVisibility(1);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("channel vani test", "Vani Voice Assistanceqq", 3);
            notificationChannel.setSound(null, null);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(560, builder.build());
    }

    public static Typeface getBoldAppFont(Context context) {
        if (context == null) {
            context = BoloApplication.getApplication();
        }
        return Typeface.createFromAsset(context.getAssets(), "fonts/sofia_pro_regular.ttf");
    }

    private static String getContentType(String str) {
        return str.contains("mp4") ? "video" : str.contains("gif") ? "gif" : "image";
    }

    public static String getCountryZipCode(Context context) {
        try {
            String upperCase = ((TelephonyManager) context.getSystemService("phone")).getSimCountryIso().toUpperCase();
            for (String str : context.getResources().getStringArray(R.array.CountryCodes)) {
                String[] split = str.split(",");
                if (split[1].trim().equals(upperCase.trim())) {
                    return split[0];
                }
            }
            return "";
        } catch (Exception unused) {
            return "91";
        }
    }

    public static String getCurrentCountryCode() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) BoloApplication.getApplication().getSystemService("phone");
            String networkCountryIso = telephonyManager.getNetworkCountryIso();
            if (networkCountryIso == null || networkCountryIso.isEmpty()) {
                networkCountryIso = telephonyManager.getSimCountryIso();
            }
            if ((networkCountryIso == null || networkCountryIso.isEmpty()) && Build.VERSION.SDK_INT >= 24 && BoloApplication.getApplication().getResources().getConfiguration().getLocales() != null && !BoloApplication.getApplication().getResources().getConfiguration().getLocales().isEmpty()) {
                networkCountryIso = BoloApplication.getApplication().getResources().getConfiguration().getLocales().get(0).getCountry();
            }
            if (networkCountryIso != null) {
                if (networkCountryIso.isEmpty()) {
                    return null;
                }
                return networkCountryIso;
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getDeviceName() {
        return Build.MANUFACTURER + " " + Build.MODEL;
    }

    public static Bitmap getImageOfUserCall(CallModel callModel, Context context) {
        BoloApplication application = BoloApplication.getApplication();
        if (callModel == null) {
            return null;
        }
        if (callModel.getUserImg() == null && !callModel.isUserImgFetched()) {
            if (callModel.getPhnNumber() == null) {
                callModel.setPhnNumber(numberFromCall(callModel.getCallDetails()));
            }
            Bitmap userImageForPhoneNumber = UsersImageHandler.userImageForPhoneNumber(callModel.getPhnNumber(), application);
            callModel.setUserImgFetched(true);
            callModel.setUserImg(userImageForPhoneNumber);
            return callModel.getUserImg();
        }
        return callModel.getUserImg();
    }

    public static Locale getLanguageCode() {
        Locale locale = Locale.getDefault();
        return (locale.getLanguage().equals(new Locale("pt").getLanguage()) || locale.getLanguage().equals(new Locale("es").getLanguage()) || locale.getLanguage().equals(new Locale("bn").getLanguage())) ? locale : Locale.ENGLISH;
    }

    public static Typeface getMediumAppFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/ui_medium.otf");
    }

    public static String getNameFromCall(CallModel callModel, Context context) {
        BoloApplication application = BoloApplication.getApplication();
        String string = application.getString(R.string.unknown);
        if (callModel == null) {
            return string;
        }
        if (callModel.getName() != null && !callModel.getName().isEmpty()) {
            return callModel.getName();
        }
        if (callModel.getCall() != null && isConfranceCall(callModel.getCall())) {
            return BoloApplication.getApplication().getString(R.string.conference_call);
        }
        if (callModel.isNameFetched()) {
            return string;
        }
        if (callModel.getPhnNumber() == null) {
            callModel.setPhnNumber(numberFromCall(callModel.getCallDetails()));
        }
        if (ContextCompat.checkSelfPermission(application, BoloPermission.READ_CONTACTS) != 0) {
            return string;
        }
        String contactNameFromNumber = ContactsHandler.contactNameFromNumber(callModel.getPhnNumber(), application);
        callModel.setNameFetched(true);
        if (contactNameFromNumber == null || contactNameFromNumber.isEmpty()) {
            return string;
        }
        callModel.setName(contactNameFromNumber);
        return contactNameFromNumber;
    }

    public static Typeface getNormalAppFont(Context context) {
        if (context == null) {
            context = BoloApplication.getApplication();
        }
        return Typeface.createFromAsset(context.getAssets(), "fonts/ui_normal.otf");
    }

    public static String getPhoneNumber() {
        try {
            List<SubscriptionInfo> activeSubscriptionInfoList = ((SubscriptionManager) BoloApplication.getApplication().getSystemService(SubscriptionManager.class)).getActiveSubscriptionInfoList();
            for (int i = 0; i < activeSubscriptionInfoList.size(); i++) {
                SubscriptionInfo subscriptionInfo = activeSubscriptionInfoList.get(i);
                if (subscriptionInfo != null && subscriptionInfo.getNumber() != null && !subscriptionInfo.getNumber().isEmpty()) {
                    return subscriptionInfo.getNumber();
                }
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getPhoneNumberOfCall(CallModel callModel, Context context) {
        if (callModel == null) {
            return "";
        }
        if (callModel.getPhnNumber() == null) {
            if (callModel.getCallDetails() != null) {
                callModel.setPhnNumber(numberFromCall(callModel.getCallDetails()));
            } else {
                callModel.setPhnNumber(numberFromCall(callModel.getCall()));
            }
        }
        return callModel.getPhnNumber();
    }

    public static ThemeModel getRandomTheme() {
        ArrayList<String> arrayList = Helper.downloadedThemes;
        if (arrayList == null || arrayList.size() == 0) {
            Helper.getDownloadedTheme();
            arrayList = Helper.downloadedThemes;
        }
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }
        String str = arrayList.get(new Random().nextInt(arrayList.size()));
        ThemeModel themeModel = new ThemeModel();
        themeModel.setAppliedTheme(str);
        if (str.contains("android.resource:")) {
            themeModel.setContentType("video");
        } else {
            themeModel.setContentType(getContentType(str));
        }
        themeModel.setSource("Online");
        return themeModel;
    }

    public static int getResourceByName(Context context, String str, String str2) {
        return context.getResources().getIdentifier(str, str2, context.getPackageName());
    }

    public static String getStringFromStringFile(Context context, String str) {
        return context.getResources().getString(context.getResources().getIdentifier(str, "string", context.getPackageName()));
    }

    public static String getSystemDialer(Context context) {
        for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(new Intent("android.intent.action.DIAL"), 0)) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if ((activityInfo.applicationInfo.flags & 1) != 0) {
                Log.d("Dialer", "onStart: " + activityInfo.packageName);
                return activityInfo.packageName;
            }
        }
        return null;
    }

    public static String getUserName() {
        try {
            Cursor query = BoloApplication.getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
            query.moveToFirst();
            return query.getString(query.getColumnIndex("display_name"));
        } catch (Exception unused) {
            return null;
        }
    }

    public static int getWifiStrength() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) BoloApplication.getApplication().getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.getType() == 1 && activeNetworkInfo.isConnected()) {
                return WifiManager.calculateSignalLevel(((WifiManager) BoloApplication.getApplication().getApplicationContext().getSystemService("wifi")).getConnectionInfo().getRssi(), 5);
            }
            return 4;
        } catch (Exception unused) {
            return 4;
        }
    }

    public static boolean hasWhatsappInstalled(Context context) {
        return checkAppIsInstalledOrNot(context, VideoCallController.AppPackage.WHATSAPP) || checkAppIsInstalledOrNot(context, VideoCallController.AppPackage.WHATSAPP_BUSSINESS);
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity == null || activity.getCurrentFocus() == null || activity.getCurrentFocus().getWindowToken() == null) {
            return;
        }
        ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isAppDefaultSet(Context context) {
        String defaultDialerPackage;
        return Build.VERSION.SDK_INT < 23 || (defaultDialerPackage = ((TelecomManager) context.getSystemService("telecom")).getDefaultDialerPackage()) == null || defaultDialerPackage.equals(context.getPackageName());
    }

    public static boolean isConfranceCall(Call call) {
        return (Build.VERSION.SDK_INT < 23 || call == null || call.getChildren() == null || call.getChildren().isEmpty()) ? false : true;
    }

    public static boolean isConnected(boolean z) {
        if (z) {
            isConnectedToInternet = false;
        }
        boolean z2 = isConnectedToInternet;
        if (z2) {
            return z2;
        }
        checkInternetSpeed(z);
        return isConnectedToInternet;
    }

    public static boolean isConnectedFast(Context context) {
        NetworkInfo networkInfo = Connectivity.getNetworkInfo(context);
        return networkInfo != null && networkInfo.isConnected() && Connectivity.isConnectionFast(networkInfo.getType(), networkInfo.getSubtype());
    }

    public static boolean isFlashlightSupport(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.camera.flash");
    }

    public static boolean isInternetEnabled(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        return (connectivityManager == null || connectivityManager.getActiveNetworkInfo() == null || !connectivityManager.getActiveNetworkInfo().isConnected()) ? false : true;
    }

    public static void isOnline() {
        final boolean[] zArr = {false};
        final boolean[] zArr2 = {false};
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility.1
            @Override 
            public void run() {
                zArr[0] = true;
                if (zArr2[0]) {
                    return;
                }
                Utility.updateNewOfInternetStatus(false);
            }
        };
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility.2
            @Override 
            public void run() {
                handler.postDelayed(runnable, 500L);
                Runtime runtime = Runtime.getRuntime();
                try {
                    Log.e("time BEFORE", "");
                    int waitFor = runtime.exec("/system/bin/ping -c 1 8.8.8.8").waitFor();
                    Log.e("time AFTER", "");
                    boolean z = waitFor == 0;
                    zArr2[0] = true;
                    handler.removeCallbacks(runnable);
                    if (zArr[0]) {
                        return;
                    }
                    Utility.updateNewOfInternetStatus(z);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }).start();
    }

    public static boolean isPhoneLocked(Context context) {
        BoloApplication application = BoloApplication.getApplication();
        try {
            if (((KeyguardManager) application.getSystemService("keyguard")).isDeviceLocked()) {
                return true;
            }
            return !((PowerManager) application.getSystemService("power")).isInteractive();
        } catch (Exception unused) {
            return true;
        }
    }

    public static boolean isVirtualHomeButton() {
        return !ViewConfiguration.get(BoloApplication.getApplication()).hasPermanentMenuKey();
    }

    public static String loadJSONFromAsset(Context context, String str) {
        try {
            InputStream open = context.getAssets().open(str);
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr, Key.STRING_CHARSET_NAME);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void logUserProtiesAndSIMData() {
        String obj;
        try {
            List<SubscriptionInfo> activeSubscriptionInfoList = ((SubscriptionManager) BoloApplication.getApplication().getSystemService(SubscriptionManager.class)).getActiveSubscriptionInfoList();
            if (activeSubscriptionInfoList == null) {
                logEventNew(Constants.SIMCategory, "SIM_Info_Not_Found");
                return;
            }
            boolean z = false;
            for (int i = 0; i < activeSubscriptionInfoList.size(); i++) {
                SubscriptionInfo subscriptionInfo = activeSubscriptionInfoList.get(i);
                if (subscriptionInfo != null) {
                    try {
                        if (subscriptionInfo.getCarrierName() != null && (obj = subscriptionInfo.getCarrierName().toString()) != null) {
                            logEventNew(Constants.SIMCategory, "SIM_" + obj);
                        }
                        String number = subscriptionInfo.getNumber();
                        if (number != null && !number.isEmpty() && !z) {
                            z = true;
                        }
                    } catch (Exception unused) {
                    }
                }
            }
        } catch (Exception unused2) {
        }
    }

    public static void makeTextGradient(TextView textView) {
        textView.getPaint().setShader(new LinearGradient(0.0f, 0.0f, textView.getPaint().measureText(textView.getText().toString()), textView.getTextSize(), new int[]{Color.parseColor("#FF1070"), Color.parseColor("#FD4D1F")}, (float[]) null, Shader.TileMode.CLAMP));
    }

    public static boolean mergeCall(CallModel callModel) {
        List<CallModel> list;
        CallModel callModelForCall;
        if (callModel == null) {
            return false;
        }
        List<Call> conferenceableCalls = callModel.getCall().getConferenceableCalls();
        if (conferenceableCalls.size() > 0) {
            callModel.setPartOfConfressCall(true);
            for (int i = 0; i < conferenceableCalls.size(); i++) {
                Call call = conferenceableCalls.get(i);
                callModel.getCall().conference(call);
                CallHandler callHandler = CallHandler.sharedInstance;
                if (callHandler != null && (list = callHandler.calls) != null && (callModelForCall = CallModel.callModelForCall(list, call)) != null) {
                    callModelForCall.setPartOfConfressCall(true);
                }
            }
            return true;
        }
        return false;
    }

    public static String numberFromCall(Call call) {
        return (Build.VERSION.SDK_INT < 23 || call == null || call.getDetails() == null || call.getDetails().getHandle() == null) ? "" : call.getDetails().getHandle().getSchemeSpecificPart();
    }

    public static String numberFromCallDetails(Call.Details details) {
        return (Build.VERSION.SDK_INT < 23 || details == null || details.getHandle() == null) ? "" : details.getHandle().getSchemeSpecificPart();
    }

    public static void openContacts(Context context) {
        context.startActivity(new Intent("android.intent.action.VIEW", ContactsContract.Contacts.CONTENT_URI));
    }

    public static void openDefaultAppDialog(Context context) {
        try {
            int i = Build.VERSION.SDK_INT;
            if (i >= 23) {
                if (i >= 29) {
                    ((Activity) context).startActivityForResult(((RoleManager) ((Activity) context).getSystemService("role")).createRequestRoleIntent("android.app.role.DIALER"), DEFAULT_APP_REQ);
                } else {
                    Intent intent = new Intent();
                    intent.setAction("android.telecom.action.CHANGE_DEFAULT_DIALER");
                    intent.putExtra("android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME", context.getPackageName());
                    ((Activity) context).startActivityForResult(intent, DEFAULT_APP_REQ);
                }
            }
        } catch (Exception unused) {
            PreferenceUtils.getInstance().putPreference(IS_DEFAULT_PACKAGE_NOT_FOUND, true);
        }
    }

    public static void openDialer(Context context) {
        Intent intent;
        if (ContextCompat.checkSelfPermission(BoloApplication.getApplication(), BoloPermission.PHONE_CALLS) == 0 && ContextCompat.checkSelfPermission(BoloApplication.getApplication(), BoloPermission.READ_CALL_LOG) == 0) {
            intent = new Intent(BoloApplication.getApplication(), MainActivity.class);
            Log.e("sachin ", "Our");
        } else {
            intent = new Intent("android.intent.action.DIAL");
            intent.setPackage(getSystemDialer(context));
            Log.e("sachin ", "System");
        }
        intent.setFlags(268435456);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("sachin ", e.getLocalizedMessage());
        }
    }

    public static void pickContacts(Activity activity) {
        activity.startActivityForResult(new Intent("android.intent.action.PICK", ContactsContract.Contacts.CONTENT_URI), PICK_CONTACT);
    }

    public static void rotateView(final Context context, final View view) {
        count = 0;
        new Timer().schedule(new TimerTask() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility.3
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (Utility.count == 180) {
                    Utility.access$020(90);
                }
                ((Activity) context).runOnUiThread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility.3.1
                    @Override 
                    public void run() {
                        view.setRotation(Utility.count);
                    }
                });
                Utility.access$012(90);
            }
        }, 200L, 100L);
    }

    public static boolean sendAutoReplyWithSocialMedia(Call call, String str) {
        HashMap<String, NotificationWear> hashMap = null;
        try {
            HashMap<String, HashMap<String, NotificationWear>> wearableHashMap = BoloSingleTon.getInstance(BoloApplication.getApplication()).getWearableHashMap();
            for (String str2 : BoloNotificationListenerService.packageNamesToHandle) {
                HashMap<String, NotificationWear> hashMap2 = wearableHashMap.get(str2);
                hashMap = hashMap2;
                if (hashMap2 != null) {
                    break;
                }
            }
        } catch (Exception unused) {
        }
        if (hashMap != null) {
            NotificationWear notificationWear = hashMap.get(updatePhoneNumberWithISDCode(BoloApplication.getApplication(), numberFromCall(call)));
            if (notificationWear.getAction() != null && notificationWear.getRemoteInputs() != null) {
                try {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    for (RemoteInput remoteInput : notificationWear.getRemoteInputs()) {
                        bundle.putCharSequence(remoteInput.getResultKey(), str);
                    }
                    RemoteInput.addResultsToIntent(notificationWear.getRemoteInputs(), intent, bundle);
                    if (notificationWear.getAction() == null) {
                        return false;
                    }
                    notificationWear.getAction().actionIntent.send(BoloApplication.getApplication(), 0, intent);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (notificationWear.getActionExtra() != null && notificationWear.getRemoteInputsExtra() != null) {
                try {
                    Intent intent2 = new Intent();
                    Bundle bundle2 = new Bundle();
                    for (android.app.RemoteInput remoteInput2 : notificationWear.getRemoteInputsExtra()) {
                        bundle2.putCharSequence(remoteInput2.getResultKey(), str);
                    }
                    android.app.RemoteInput.addResultsToIntent(notificationWear.getRemoteInputsExtra(), intent2, bundle2);
                    if (notificationWear.getActionExtra() == null) {
                        return false;
                    }
                    notificationWear.getActionExtra().actionIntent.send(BoloApplication.getApplication(), 0, intent2);
                    return true;
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return false;
    }

    public static void setThemeBackground(Context context, String str, GifWallpaper gifWallpaper, final VideoWallpaper videoWallpaper) {
        ThemeModel themeModel;
        videoWallpaper.setVisibility(8);
        gifWallpaper.setVisibility(0);
        if ((context instanceof Activity) && ((Activity) context).isFinishing()) {
            return;
        }
        ThemeModel themeModel2 = ActivityFullScreenView.getThemeModel(updatePhoneNumberWithISDCode(context, str));
        model = themeModel2;
        if (themeModel2 == null) {
            model = ActivityFullScreenView.getThemeModel();
            path = ActivityFullScreenView.getAppliedTheme();
        } else {
            path = themeModel2.getAppliedTheme();
        }
        if (PreferenceUtils.getInstance().getBoolean(Constants.KEY_RANDOM_THEME)) {
            ThemeModel randomTheme = getRandomTheme();
            model = randomTheme;
            if (randomTheme != null) {
                path = randomTheme.getAppliedTheme();
            }
        }
        if (path != null && (themeModel = model) != null) {
            if (themeModel.getSource().equals("custom")) {
                gifWallpaper.setWallpaper(path);
                return;
            } else if (model.getSource().equals("offline")) {
                if (model.getContentType().equals("video")) {
                    videoWallpaper.setVisibility(0);
                    gifWallpaper.setVisibility(8);
                    gifWallpaper.setOfflineWallpaper(model.getThemeImage());
                    videoWallpaper.setOfflineWallpaper(path);
                    return;
                } else if (model.getContentType().equals("gif")) {
                    gifWallpaper.setGifWallpaper(path);
                    return;
                } else {
                    gifWallpaper.setOfflineWallpaper(path);
                    return;
                }
            } else if (model.getContentType().equals("video")) {
                videoWallpaper.setVisibility(0);
                gifWallpaper.setVisibility(8);
                gifWallpaper.setThumbnail(ThemeModel.BASE_IMAGE + model.getThumbnail());
                videoWallpaper.setOnErrorOnPlaying(new VideoWallpaper.OnErrorOnPlaying() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility.4
                    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.custom.VideoWallpaper.OnErrorOnPlaying
                    public void onErrorOnPlaying(MediaPlayer mediaPlayer) {
                        String str2 = "";
                        try {
                            File file = new File(Utility.path);
                            if (file.exists()) {
                                str2 = file.getName();
                                file.delete();
                            }
                        } catch (Exception unused) {
                        }
                        String themeImage = Utility.model.getThemeImage();
                        if (themeImage != null && !themeImage.isEmpty()) {
                            str2 = themeImage;
                        }
                        if (str2 == null || str2.isEmpty()) {
                            return;
                        }
                        videoWallpaper.setTheme(str2);
                        videoWallpaper.download(ThemeModel.BASE_IMAGE + str2);
                    }
                });
                videoWallpaper.setWallpaper(path);
                return;
            } else if (model.getContentType().equals("gif")) {
                gifWallpaper.setGifWallpaper(path);
                return;
            } else {
                gifWallpaper.setWallpaper(path);
                return;
            }
        }
        gifWallpaper.setVisibility(8);
        videoWallpaper.setVisibility(0);
        videoWallpaper.setOfflineWallpaper("m_defalut_vid.mp4");
    }

    public static void showDialogForApp(final Context context, String str, String str2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.allow_bolo_default);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.set_default, new DialogInterface.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility.5
            @Override 
            public void onClick(DialogInterface dialogInterface, int i) {
                Utility.openDefaultAppDialog(context);
            }
        });
        builder.show();
    }

    public static void updateNewOfInternetStatus(boolean z) {
        if (isConnectedToInternet != z) {
            isConnectedToInternet = z;
            LocalBroadcastManager.getInstance(BoloApplication.getApplication()).sendBroadcast(new Intent(Constants.InternetStatusChanged));
        }
    }

    public static String updatePhoneNumberWithISDCode(Context context, String str) {
        try {
            String replace = str.replace("?", "").replace("-", "").replace("(", "").replace(")", "").replace(" ", "").replace("/", "").replace(".", "");
            if (replace.startsWith("+")) {
                return replace.replaceAll("[\\D]", "");
            }
            if (replace.startsWith("0")) {
                return getCountryZipCode(context) + replace.replaceAll("[\\D]", "").substring(1);
            }
            return getCountryZipCode(context) + replace;
        } catch (Exception unused) {
            return str;
        }
    }

    public static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200L, -1));
        } else {
            vibrator.vibrate(200L);
        }
    }

    public static void vibrate1(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(100L, -1));
        } else {
            vibrator.vibrate(100L);
        }
    }

    public static boolean checkAppIsInstalledOrNot(String str) {
        for (ApplicationInfo applicationInfo : BoloApplication.getApplication().getPackageManager().getInstalledApplications(0)) {
            if (applicationInfo.packageName.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    public static String numberFromCall(Call.Details details) {
        return (Build.VERSION.SDK_INT < 23 || details == null || details.getHandle() == null) ? "" : details.getHandle().getSchemeSpecificPart();
    }
}
