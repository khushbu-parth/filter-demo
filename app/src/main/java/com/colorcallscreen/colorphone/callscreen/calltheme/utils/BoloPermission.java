package com.colorcallscreen.colorphone.callscreen.calltheme.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;


public class BoloPermission {
    public static final String ANSWER_PHONE_CALLS = "android.permission.ANSWER_PHONE_CALLS";
    public static int DEVICE_SETTING_REQ = 200;
    public static int GROUP_PERMISSION = 9001;
    public static final String PHONE_CALLS = "android.permission.CALL_PHONE";
    public static final String READ_CALL_LOG = "android.permission.READ_CALL_LOG";
    public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    public static final String WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG";
    public static final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private Activity activity;
    private Context context;


    public static class Utils {
        public static boolean checkPermissionPreference(String str) {
            return PreferenceUtils.getInstance().getBoolean(str);
        }

        public static void updatePermissionPreference(String str) {
            PreferenceUtils.getInstance().putPreference(str, true);
        }
    }

    public BoloPermission(Context context) {
        this.context = context;
        this.activity = (Activity) context;
    }

    public static List<String> permissionList(boolean z) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(WRITE_EXTERNAL_STORAGE);
        if (!z) {
            arrayList.add(WRITE_CONTACTS);
            arrayList.add(READ_CONTACTS);
            arrayList.add(READ_PHONE_STATE);
            if (Build.VERSION.SDK_INT >= 26) {
                arrayList.add(ANSWER_PHONE_CALLS);
            }
        }
        return arrayList;
    }

    public List<String> getRequiredPermissionList(boolean z) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < permissionList(z).size(); i++) {
            String str = permissionList(z).get(i);
            if (ContextCompat.checkSelfPermission(this.context, str) != 0) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    public void groupPermission(boolean z) {
        List<String> requiredPermissionList = getRequiredPermissionList(z);
        if (requiredPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(this.activity, (String[]) requiredPermissionList.toArray(new String[requiredPermissionList.size()]), GROUP_PERMISSION);
        }
    }

    public boolean isAllPermissionGranted(boolean z) {
        return getRequiredPermissionList(z).size() <= 0;
    }

    public boolean isPermissionGranted(String str) {
        return ContextCompat.checkSelfPermission(this.context, str) == 0;
    }

    public void openApplicationSetting() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", this.context.getPackageName(), null));
        this.activity.startActivityForResult(intent, DEVICE_SETTING_REQ);
    }

    public boolean shouldRelational(String str) {
        return ActivityCompat.shouldShowRequestPermissionRationale(this.activity, str);
    }

    public static void openApplicationSetting(Fragment fragment) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", fragment.getContext().getPackageName(), null));
        fragment.startActivityForResult(intent, DEVICE_SETTING_REQ);
    }
}
