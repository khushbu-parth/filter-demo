package com.pu.casttotv.tvcast.screenmirror.tvremote.utils;

import android.content.Context;
import android.os.Bundle;

public class Tracking {
    public static String activity_splash_goto_main = "activity_splash_goto_main";
    public static String main_activity_oncreate = "main_activity_oncreate";
    public static String play_cast_error = "play_total_cast_error";
    public static String play_cast_success = "play_total_cast_success";
    public static String play_oncreate = "play_oncreate";
    public static String play_show_audio_error = "play_show_audio_error";
    public static String play_show_audio_success = "play_show_audio_success";
    public static String play_show_photo_offline_error = "play_show_photo_offline_error";
    public static String play_show_photo_offline_success = "play_show_photo_offline_success";
    public static String play_show_photo_online_error = "play_show_photo_online_error";
    public static String play_show_photo_online_success = "play_show_photo_online_success";
    public static String play_show_video_offline_error = "play_show_video_offline_error";
    public static String play_show_video_offline_success = "play_show_video_offline_success";
    public static String play_show_youtube_error = "play_show_youtube_error";
    public static String play_show_youtube_success = "play_show_youtube_success";

    public static void connect(Context context, String str, String str2, String str3) {
        try {
            Boolean bool = Boolean.FALSE;
            Bundle bundle = new Bundle();
            bundle.putString(str2, str2);
            bundle.putString(str, str);
            String[] strArr = NameDevice.list;
            int length = strArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String str4 = strArr[i];
                if (str3.contains(str4)) {
                    bool = Boolean.TRUE;
                    bundle.putString("name_default", str4);
                    bundle.putString(str3, str3.replace(" ", "_"));
                    break;
                }
                i++;
            }
            if (!bool.booleanValue()) {
                bundle.putString("name_default", "other");
                bundle.putString(str3, str3.replace(" ", "_"));
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void trackCast(Context context, String str, String str2, String str3, String str4) {
        try {
            Boolean bool = Boolean.FALSE;
            Bundle bundle = new Bundle();
            bundle.putString(str, str);
            bundle.putString(str2, str2);
            String[] strArr = NameDevice.list;
            int length = strArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String str5 = strArr[i];
                if (str3.contains(str5)) {
                    bool = Boolean.TRUE;
                    str3.replace(" ", "_");
                    bundle.putString("name_default", str5);
                    bundle.putString(str3, str3);
                    break;
                }
                i++;
            }
            if (!bool.booleanValue()) {
                bundle.putString("name_default", "other");
                bundle.putString(str3, str3);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
