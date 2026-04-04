package com.colorcallscreen.colorphone.callscreen.calltheme.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;

import com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityFullScreenView;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityBlockCallList;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.MainActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.ParentCallAcitvity;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.TranslucentActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.controller.VideoCallController;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ThemeModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.components.BoloSpeechRecognizer;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.BoloCallHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.AcceptDeclineCallManager;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.CallManager.CallRingingManager;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.windowview.CallOpreationHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Helper {
    public static final String OVERLAY_SETTING_NOT_FOUND = "_overlay_not_found";
    private static final String TAG = "Helper";
    public static PendingIntent contentIntent = null;
    public static ArrayList<String> downloadedThemes = null;
    public static boolean isCallScreeiningDone = false;

    public static void cleanUpHelper() {
    }

    public static boolean shouldUseMediaRecorder() {
        return true;
    }

    public static void acceptCall(CallRingingManager.CallResponseGesture callResponseGesture, boolean z, Context context, Call call) {
        CallOpreationHandler callOpreationHandler;
        CallHandler callHandler = CallHandler.sharedInstance;
        if (callHandler != null && (callOpreationHandler = callHandler.callOpreationHandler) != null) {
            callOpreationHandler.cleanUpEverything();
        }
        if (z) {
            BoloCallHandler.getInstance().addCallHookTasks(Constants.PUT_CALL_ON_SPEAKER_TASK);
        } else {
            BoloCallHandler.getInstance().addCallHookTasks(Constants.PUT_CALL_ON_UMUTE_TASK);
        }
        AcceptDeclineCallManager.acceptCall(z, context, call);
        try {
            String str = callResponseGesture == CallRingingManager.CallResponseGesture.Voice ? "Voice" : "Hand";
            if (BoloSpeechRecognizer.isUsingGooleOnlineSpeechToText) {
                String str2 = str + "- GCS";
            } else {
                String str3 = str + "- offline";
            }
        } catch (Exception unused) {
        }
    }

    public static void addToContact(Context context, String str, String str2) {
        Intent intent = new Intent("com.android.contacts.action.SHOW_OR_CREATE_CONTACT", Uri.parse("tel:" + str));
        if (str2 != null) {
            intent.putExtra("name", str2);
        }
        intent.setFlags(268435456);
        intent.putExtra("com.android.contacts.action.FORCE_CREATE", true);
        context.startActivity(intent);
    }

    public static void addToContactIfExist(Context context, String str) {
        Intent intent = new Intent("android.intent.action.INSERT_OR_EDIT");
        intent.setType("vnd.android.cursor.item/contact");
        intent.putExtra("phone", str);
        context.startActivity(intent);
    }

    public static void applyTheme(String str) {
        PreferenceUtils.getInstance().putPreference(Constants.THEME_KEY, str);
        PreferenceUtils.getInstance().removePreference(Constants.DEFAULT_THEME_KEY);
        enableRandomTheme(false, false);
    }

    public static void createDir() {
        File file = Constants.THEME_DIRECTORY;
        if (!file.exists()) {
            if (file.mkdirs()) {
                Log.d(Constants.TAG, "Directory Created");
                return;
            } else {
                Log.d(Constants.TAG, "Failed To Directory Created");
                return;
            }
        }
        Log.d(Constants.TAG, "Directory Exist");
    }

    public static void createDirIfNotExist(File file) {
        if (!file.exists()) {
            if (file.mkdirs()) {
                Log.d(Constants.TAG, "Directory Created");
                return;
            } else {
                Log.d(Constants.TAG, "Failed To Directory Created");
                return;
            }
        }
        Log.d(Constants.TAG, "Directory Exist");
    }

    public static void declineCall(CallRingingManager.CallResponseGesture callResponseGesture, Context context, Call call, String str) {
        CallOpreationHandler callOpreationHandler;
        CallHandler callHandler = CallHandler.sharedInstance;
        if (callHandler != null && (callOpreationHandler = callHandler.callOpreationHandler) != null) {
            callOpreationHandler.cleanUpEverything();
        }
        AcceptDeclineCallManager.declineCall(context, call, str);
        try {
            String str2 = callResponseGesture == CallRingingManager.CallResponseGesture.Voice ? "Voice" : "Hand";
            if (BoloSpeechRecognizer.isUsingGooleOnlineSpeechToText) {
                String str3 = str2 + "- GCS";
            } else {
                String str4 = str2 + "- offline";
            }
        } catch (Exception unused) {
        }
    }

    public static void defaultSetting() {
        PreferenceUtils.getInstance().putPreference(Constants.ENABLE_BOLO, true);
        PreferenceUtils.getInstance().putPreference(Constants.LED_FLASH, false);
        PreferenceUtils.getInstance().putPreference(Constants.CALLER_NAME, false);
        PreferenceUtils.getInstance().putPreference(Constants.VOCIE_RECOGITION, false);
        PreferenceUtils.getInstance().putPreference(ActivityBlockCallList.IS_ENABLE_BLOCKER, true);
        if (Utility.hasWhatsappInstalled(BoloApplication.getApplication())) {
            VideoCallController.Settings.saveApp(VideoCallController.VideoCallApps.WHATSAPP);
        } else if (Utility.checkAppIsInstalledOrNot(VideoCallController.AppPackage.DUO)) {
            VideoCallController.Settings.saveApp(VideoCallController.VideoCallApps.DUO);
        } else {
            VideoCallController.Settings.saveApp(VideoCallController.VideoCallApps.NONE);
        }
        enableRandomTheme(true, false);
        PreferenceUtils.getInstance().putPreference(Constants.INVERT_CALL_PICK_UP, false);
    }

    public static long downloadTheme(Context context, String str, String str2) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
        request.setVisibleInDownloadsUi(false);
        request.setNotificationVisibility(2);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalFilesDir(BoloApplication.getApplication(), ".bolo", str2);
        return ((DownloadManager) context.getSystemService("download")).enqueue(request);
    }

    public static void emptyFolder(File file) {
        if (file.exists() && file.isDirectory() && file.listFiles() != null) {
            for (File file2 : file.listFiles()) {
                file2.delete();
            }
        }
    }

    public static void enableRandomTheme(boolean z, boolean z2) {
        ThemeModel lastAppliedTheme;
        PreferenceUtils.getInstance().putPreference(Constants.KEY_RANDOM_THEME, z);
        if (z) {
            PreferenceUtils.getInstance().removeCurrentThemeAndSaveInCache();
        } else if (!z2 || (lastAppliedTheme = PreferenceUtils.getInstance().getLastAppliedTheme()) == null) {
        } else {
            PreferenceUtils.getInstance().setThemeModel(lastAppliedTheme);
            PreferenceUtils.getInstance().putPreference(Constants.THEME_KEY, lastAppliedTheme.getThemeImage());
            PreferenceUtils.getInstance().putPreference(Constants.APPLIED_THEME_NAME, lastAppliedTheme.getName());
        }
    }

    public static String getAppliedThemeType() {
        String string = PreferenceUtils.getInstance().getString(Constants.APPLIED_THEME_TYPE);
        return string == null ? ThemeModel.THEME_TYPE.DEFAULT.toString() : string;
    }

    public static int getDP(Context context, int i) {
        return (int) ((i * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static List<String> getDownloadedTheme() {
        File[] listFiles;
        downloadedThemes = new ArrayList<>();
        File file = Constants.THEME_DIRECTORY;
        if (file.isDirectory() && (listFiles = file.listFiles()) != null) {
            for (File file2 : listFiles) {
                if (!file2.isDirectory()) {
                    downloadedThemes.add(file2.getAbsolutePath());
                }
            }
        }
        downloadedThemes.add("android.resource://" + BoloApplication.getApplication().getPackageName() + "/" + R.raw.m_defalut_vid);
        return downloadedThemes;
    }

    public static Intent getParentCallIntent(boolean z) {
        Intent intent = new Intent(BoloApplication.getApplication(), ParentCallAcitvity.class);
        if (z) {
            intent.addFlags(65536);
        }
        return intent;
    }

    public static String getRealPathFromURI(Context context, String str) {
        Uri parse = Uri.parse(str);
        Cursor query = context.getContentResolver().query(parse, null, null, null, null);
        if (query == null) {
            return parse.getPath();
        }
        query.moveToFirst();
        return query.getString(query.getColumnIndex("_data"));
    }

    public static void goToHome(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    public static boolean isCallRecordIsSupportedByDevice() {
        String lowerCase = Utility.getCurrentCountryCode().toLowerCase();
        if (lowerCase == null || lowerCase.isEmpty()) {
            return false;
        }
        return lowerCase.equals("in") || lowerCase.equals("bd") || lowerCase.equals("pk");
    }

    public static boolean isCallRecordingTermAndConditionApproved() {
        return PreferenceUtils.getInstance().getBoolean(Constants.CallRecordingTandCApproved, false);
    }

    public static boolean isColorCodeCorrect(String str) {
        return str.length() == 6;
    }

    public static boolean isDefaultThemeApplied(int i) {
        int i2 = PreferenceUtils.getInstance().getInt(Constants.DEFAULT_THEME_KEY);
        return i2 != 0 && i == i2;
    }

    public static boolean isThemeApplied(String str) {
        String string = PreferenceUtils.getInstance().getString(Constants.THEME_KEY);
        return string != null && string.contains(str);
    }

    public static boolean isValidDownload(Context context, long j) {
        Log.d(TAG, "Checking download status for id: " + j);
        Cursor query = ((DownloadManager) context.getSystemService("download")).query(new DownloadManager.Query().setFilterById(j));
        if (query.moveToFirst()) {
            int i = query.getInt(query.getColumnIndex(NotificationCompat.CATEGORY_STATUS));
            if (i == 8) {
                return true;
            }
            Log.d(TAG, "Download not correct, status [" + i + "] reason [" + query.getInt(query.getColumnIndex("reason")) + "]");
        }
        return false;
    }

    public static void onCallRecordingTermAndConditionApproved() {
        PreferenceUtils.getInstance().putPreference(Constants.CallRecordingTandCApproved, true);
    }

    public static void openAccessibilitySetting(final Context context) {
        try {
            ((Activity) context).startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), 101);
            new Timer().schedule(new TimerTask() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper.1
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    context.startActivity(new Intent(context, TranslucentActivity.class));
                }
            }, 50L);
        } catch (Exception unused) {
            Utility.logEventNew(Constants.PermissionCategory, "Accessibility_not_found");
            PreferenceUtils.getInstance().putPreference("isAccesbilityPresnt", false);
        }
    }

    public static void openFullScreenView(Context context, ThemeModel themeModel, boolean z) {
        Intent intent = new Intent(context, ActivityFullScreenView.class);
        intent.putExtra("model", themeModel);
        if (z) {
            intent.putExtra(Constants.IMG_MAIN, themeModel.getDefaultTheme());
            intent.putExtra(Constants.THEME_TYPE, ThemeModel.THEME_TYPE.DEFAULT.toString());
        } else {
            intent.putExtra(Constants.IMG_MAIN, ThemeModel.BASE_IMAGE + themeModel.getThemeImage());
            intent.putExtra(Constants.IMG_THUMBNAIL, ThemeModel.BASE_IMAGE + themeModel.getThumbnail());
            intent.putExtra(Constants.THEME_TYPE, ThemeModel.THEME_TYPE.ONLINE.toString());
        }
        context.startActivity(intent);
    }

    public static void openOverlaySetting(final Context context) {
        try {
            ((Activity) context).startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + context.getPackageName())), 102);
            final Intent intent = new Intent(context, TranslucentActivity.class);
            intent.putExtra("autostart", context.getResources().getString(R.string.overlay_msg));
            new Timer().schedule(new TimerTask() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper.2
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    context.startActivity(intent);
                }
            }, 50L);
        } catch (Exception unused) {
            PreferenceUtils.getInstance().putPreference(OVERLAY_SETTING_NOT_FOUND, true);
        }
    }

    public static PendingIntent pendingIntentParentCall(boolean z, boolean z2) {
        Intent parentCallIntent = getParentCallIntent(z);
        if (z2) {
            parentCallIntent.putExtra("fromNotification", true);
        }
        parentCallIntent.setAction("android.intent.action.MAIN");
        parentCallIntent.addCategory("android.intent.category.LAUNCHER");
        parentCallIntent.setFlags(268435456);
        PendingIntent activity = PendingIntent.getActivity(BoloApplication.getApplication(), 0, parentCallIntent, 67108864);
        contentIntent = activity;
        return activity;
    }

    public static void recordCallAutomaticUpdate(boolean z) {
        PreferenceUtils.getInstance().putPreference(Constants.RecordCallAutomatic, z);
    }

    public static void sendSms(Context context, String str) {
        try {
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + str));
            intent.addFlags(268435456);
            context.startActivity(intent);
        } catch (Exception unused) {
        }
    }

    public static void sendWhatsAppMsg(Context context, String str, String str2) {
        String updatePhoneNumberWithISDCode = Utility.updatePhoneNumberWithISDCode(BoloApplication.getApplication(), str);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(268435456);
        if (str2 != null) {
            intent.setPackage(VideoCallController.AppPackage.WHATSAPP);
            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + updatePhoneNumberWithISDCode + "&text=" + str2));
        } else {
            intent.setPackage(VideoCallController.AppPackage.WHATSAPP);
            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + updatePhoneNumberWithISDCode));
        }
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Whatsapp  not installed in your phone", 0).show();
        }
    }

    public static void setDefaultTheme() {
        if (PreferenceUtils.getInstance().getString(Constants.APPLIED_THEME_TYPE) == null) {
            PreferenceUtils.getInstance().putPreference(Constants.APPLIED_THEME_TYPE, "offline");
            PreferenceUtils.getInstance().putPreference(Constants.THEME_KEY, "m_defalut_vid.mp4");
            PreferenceUtils.getInstance().putPreference(Constants.APPLIED_THEME_NAME, "Dj Dog");
            ThemeModel themeModel = new ThemeModel();
            themeModel.setThumbnail("m_default_thumb_vid");
            themeModel.setThemeImage("m_defalut_vid");
            themeModel.setName("Default_Video");
            themeModel.setSource("offline");
            themeModel.setContentType("video");
            themeModel.setColor("ffffff");
            themeModel.setPersonName("Jessica");
            themeModel.setPersonPhoneNumber("000 0000 000");
            themeModel.setPersonImage("p3");
            themeModel.setCategory(Constants.THEME_KEY);
            ActivityFullScreenView.setThemeModel(themeModel);
        }
    }

    public static boolean shouldRecordCalAutomatic() {
        return PreferenceUtils.getInstance().getBoolean(Constants.RecordCallAutomatic, false);
    }

    public static void startParentCallActivity(Context context, boolean z) {
        BoloApplication application = BoloApplication.getApplication();
        if (contentIntent == null) {
            Intent intent = new Intent(application, ParentCallAcitvity.class);
            if (z) {
                intent.addFlags(65536);
            }
            contentIntent = PendingIntent.getActivity(application, 0, intent, 67108864);
        }
        try {
            contentIntent.send();
        } catch (PendingIntent.CanceledException unused) {
            application.startActivity(new Intent(application, ParentCallAcitvity.class));
        }
    }

    public static void applyTheme(int i) {
        PreferenceUtils.getInstance().putPreference(Constants.DEFAULT_THEME_KEY, i);
        PreferenceUtils.getInstance().removePreference(Constants.THEME_KEY);
    }
}
