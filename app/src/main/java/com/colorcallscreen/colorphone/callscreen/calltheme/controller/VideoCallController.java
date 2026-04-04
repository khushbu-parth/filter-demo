package com.colorcallscreen.colorphone.callscreen.calltheme.controller;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.j256.ormlite.field.FieldType;


public class VideoCallController {
    public static final String DUO_VIDEO_CALL = "vnd.android.cursor.item/com.google.android.apps.tachyon.phone";
    public static final String WA_VIDEO_CALL = "vnd.android.cursor.item/vnd.com.whatsapp.video.call";
    private Context context;
    private String number;

    
    public static class AppPackage {
        public static final String DUO = "com.google.android.apps.tachyon";
        public static final String WHATSAPP = "com.whatsapp";
        public static final String WHATSAPP_BUSSINESS = "com.whatsapp.w4b";
    }

    
    public static class Settings {
        public static VideoCallApps getApp() {
            String string = PreferenceUtils.getInstance().getString("_v_app");
            if (string == null) {
                if (Utility.hasWhatsappInstalled(BoloApplication.getApplication())) {
                    return VideoCallApps.WHATSAPP;
                }
                if (Utility.checkAppIsInstalledOrNot(AppPackage.DUO)) {
                    return VideoCallApps.DUO;
                }
                return VideoCallApps.NONE;
            }
            return VideoCallApps.valueOf(string);
        }

        public static void saveApp(VideoCallApps videoCallApps) {
            PreferenceUtils.getInstance().putPreference("_v_app", videoCallApps.toString());
        }
    }

    
    public enum VideoCallApps {
        WHATSAPP(VideoCallController.WA_VIDEO_CALL),
        DUO(VideoCallController.DUO_VIDEO_CALL),
        NONE("none");
        
        private String s;

        VideoCallApps(String str) {
            this.s = str;
        }

        public String getName() {
            return this.s;
        }
    }

    public VideoCallController(Context context) {
        this.context = context;
    }

    public Long getVideoCallID(String str, String str2) {
        Cursor query = this.context.getApplicationContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, "display_name = ?", new String[]{str}, "display_name");
        if (query != null) {
            while (query.moveToNext()) {
                long longValue = Long.valueOf(query.getLong(query.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX))).longValue();
                String string = query.getString(query.getColumnIndex("display_name"));
                String string2 = query.getString(query.getColumnIndex("mimetype"));
                if (string.equals(str) && string2.equals(str2)) {
                    return Long.valueOf(longValue);
                }
            }
        }
        return null;
    }

    public Long getVideoCallID1(String str, String str2) {
        Cursor query = this.context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{FieldType.FOREIGN_ID_FIELD_SUFFIX, "display_name", "mimetype"}, null, null, "display_name");
        while (query.moveToNext()) {
            long j = query.getLong(query.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX));
            String string = query.getString(query.getColumnIndex("display_name"));
            String string2 = query.getString(query.getColumnIndex("mimetype"));
            Log.println(7, "dispName---", string + "===" + str + ":::" + str2);
            if (string2.equals("vnd.android.cursor.item/vnd.com.whatsapp.voip.call") || string2.equals(WA_VIDEO_CALL)) {
                if (str.equals(string)) {
                    if (string2.equals("vnd.android.cursor.item/vnd.com.whatsapp.voip.call")) {
                        Log.println(7, "voiceCallID---", Long.toString(j));
                        return Long.valueOf(j);
                    }
                    Log.println(7, "videoCallID---", Long.toString(j));
                    return Long.valueOf(j);
                }
            }
        }
        return null;
    }

    public Long getVideoCallIDV2(String str, String str2) {
        if (this.context == null) {
            this.context = BoloApplication.getApplication();
        }
        Context context = this.context;
        if (context == null) {
            return null;
        }
        try {
            Cursor query = context.getApplicationContext().getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), null, "mimetype ='" + str2 + "'", null, "display_name");
            if (query.moveToNext()) {
                return Long.valueOf(query.getLong(query.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX)));
            }
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
        return null;
    }

    public void makeVideoCall(Long l, VideoCallApps videoCallApps) {
        if (videoCallApps.getName().equals(DUO_VIDEO_CALL)) {
            duoVideoCallNewApproach2();
        } else if (Utility.checkAppIsInstalledOrNot(AppPackage.WHATSAPP)) {
            videoCall(l, AppPackage.WHATSAPP, WA_VIDEO_CALL);
        } else {
            videoCall(l, AppPackage.WHATSAPP_BUSSINESS, WA_VIDEO_CALL);
        }
    }

    public void setNumber(String str) {
        this.number = str;
    }

    private void duoVideoCallNewApproach2() {
        Intent intent = new Intent();
        intent.setFlags(268435456);
        intent.setPackage(AppPackage.DUO);
        intent.setAction("com.google.android.apps.tachyon.action.CALL");
        intent.setData(Uri.parse("tel:" + this.number));
        this.context.startActivity(intent);
    }

    public void videoCall(Long l, String str, String str2) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.setDataAndType(Uri.parse("content://com.android.contacts/data/" + l), str2);
        intent.setPackage(str);
        this.context.startActivity(intent);
    }
}
