package com.colorcallscreen.colorphone.callscreen.calltheme.utils;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import java.io.File;


public class Constants {
    public static final String APPLIED_THEME_NAME = "applied_theme_name";
    public static final String APPLIED_THEME_TYPE = "applied_theme_type";
    public static final String APP_INIT = "app_init";
    public static final String CALLER_NAME = "caller_name_should_speak";
    public static final String CALLER_NAME_INFO_TOAST = "CALLER_NAME_INFO_TOAST";
    public static final String CallAudioRouteChangedBroadcast = "CallAudioRouteChanged";
    public static final String CallDisconnected = "CallDisconnected";
    public static final String DEFAULT_THEME_KEY = "default_theme";
    public static final String ENABLE_BOLO = "enable_bolo";
    public static final String IMG_MAIN = "imgkey";
    public static final String IMG_MAIN_URL = "IMG_MAIN_URL";
    public static final String IMG_THUMBNAIL = "imgthumb";
    public static final String IS_WELCOME_SCREEN_SHOWED = "welcome_screen";
    public static final String InternetStatusChanged = "InternetStatusChanged";
    public static final String LED_FLASH = "led_flash";
    public static final String PhoneInMuteBroadcast = "PhoneInMuteBroadcast";
    public static final int Speech_Recognizer_Not_Avilable = 1404;
    public static final String TAG = "bolo_log";
    public static final String THEME_KEY = "theme";
    public static final String THEME_TYPE = "theme_type";
    public static final String VOCIE_RECOGITION = "voice_recog";
    public static final String acceptTag = "acceptTag";
    public static final String autoReplyTag = "autoReply";
    public static final String blockTag = "blockTag";
    public static final String declineTag = "declineTag";
    public static final String muteTag = "muteTag";
    public static final String speakerTag = "speakerTag";
    public static final String whoTag = "whoTag";
    public static final File THEME_DIRECTORY = BoloApplication.getApplication().getExternalFilesDir(".bolo");
    public static int CUSTOM_POSITION = -1;
    public static String PUT_CALL_ON_SPEAKER_TASK = "PUT_CALL_ON_SPEAKER_TASK";
    public static String PUT_CALL_ON_UMUTE_TASK = "PUT_CALL_ON_UMUTE_TASK";
    public static File CallRecordingFolder = BoloApplication.getApplication().getExternalFilesDir("Vani_Call_Rec");
    public static String CallTypeIncoming = "CallIn";
    public static String CallTypeOutgoing = "CallOut";
    public static String OnNewCallRecordingAdded = "OnNewCallRecordingAdded";
    public static String CallRecordingTandCApproved = "CallRecordingTandCApproved";
    public static String RecordCallAutomatic = "RecordCallAutomatic";
    public static String SIMCategory = "SIM";
    public static String PermissionCategory = "Permission";
    public static String SettingCategory = "Setting";
    public static String ThemeCategory = "Theme";
    public static String CallCategory = "Call";
    public static String KEY_RANDOM_THEME = "random_theme";
    public static String LAST_APPLIED_THEME = "last_theme";
    public static String INVERT_CALL_PICK_UP = "INVERT_CALL_PICK_UP";
}
