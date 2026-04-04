package com.colorcallscreen.colorphone.callscreen.calltheme.utils;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.controller.VideoCallController;
import com.j256.ormlite.field.FieldType;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CallLogUtils {
    public static int[] colors = {R.color.circle_bg_1, R.color.circle_bg_2, R.color.circle_bg_3, R.color.circle_bg_4, R.color.circle_bg_5, R.color.circle_bg_6, R.color.circle_bg_7, R.color.circle_bg_8, R.color.circle_bg_9, R.color.circle_bg_10, R.color.circle_bg_11};
    public static int incoming = 1;
    public static int outgoing = 2;
    public static int missed = 3;
    public static int rejected = 5;
    public static int blocked = 6;

    public static int getCallTypeIcon(int i) {
        return i != 1 ? i != 3 ? i != 5 ? i != 6 ? R.drawable.ic_outgoingcalls : R.drawable.block : R.drawable.ic_incomingcallsgreen : R.drawable.ic_missedcall : R.drawable.ic_incomingcallsgreen;
    }

    public static int getSimColor(int i) {
        return i == 0 ? R.color.sim_one : R.color.sim_two;
    }

    public static String calculateTiming(Date date) {
        Date currentFullDate = getCurrentFullDate();
        if (date.getMonth() == currentFullDate.getMonth()) {
            int date2 = currentFullDate.getDate() - date.getDate();
            String str = date2 + " " + BoloApplication.getApplication().getString(R.string.days_ago);
            if (date2 > 6) {
                return new SimpleDateFormat("dd MMM , HH:mm", Locale.getDefault()).format(date);
            }
            if (date2 == 1) {
                return BoloApplication.getApplication().getString(R.string.yesterday) + new SimpleDateFormat(" , HH:mm", Locale.getDefault()).format(date);
            }
            if (date2 > 1) {
                return new SimpleDateFormat("dd MMM , HH:mm", Locale.getDefault()).format(date);
            }
            long time = currentFullDate.getTime() - date.getTime();
            long j = time / 3600000;
            if (j > 0) {
                return j + " " + BoloApplication.getApplication().getString(R.string.hr_ago);
            }
            long j2 = time / 60000;
            if (j2 > 0) {
                return j2 + " " + BoloApplication.getApplication().getString(R.string.min_ago);
            }
            long j3 = time / 1000;
            return j3 > 0 ? j3 + " " + BoloApplication.getApplication().getString(R.string.sec_ago) : str;
        } else if (currentFullDate.getYear() == date.getYear()) {
            currentFullDate.getMonth();
            date.getMonth();
            return new SimpleDateFormat("dd MMM , HH:mm", Locale.getDefault()).format(date);
        } else {
            return new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(date);
        }
    }

    public static String formatDuration(String str) {
        int parseInt = Integer.parseInt(str);
        if (parseInt > 59) {
            return (parseInt / 60) + "m " + (parseInt % 60) + "s";
        }
        if (parseInt > 59) {
            return (parseInt / 0) + "h 0m " + parseInt + "s";
        }
        return str + "s";
    }

    public static String getCallTypeName(int i) {
        if (i != incoming) {
            if (i != outgoing) {
                if (i != missed) {
                    if (i != rejected) {
                        return i != blocked ? "" : BoloApplication.getApplication().getString(R.string.blocked_call);
                    }
                    return BoloApplication.getApplication().getString(R.string.rejected_call);
                }
                return BoloApplication.getApplication().getString(R.string.missed_call);
            }
            return BoloApplication.getApplication().getString(R.string.outgoing_call);
        }
        return BoloApplication.getApplication().getString(R.string.incoming_call);
    }

    public static long getLastDateFromToday(int i) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, -i);
        return calendar.getTimeInMillis();
    }

    public static long getNumberOfMonthOlderFromToday(int i) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(2, -i);
        return calendar.getTimeInMillis();
    }

    public static String getSimNameBySimID(Context context, String str) {
        SubscriptionManager subscriptionManager;
        if (ContextCompat.checkSelfPermission(context, BoloPermission.READ_PHONE_STATE) == 0 && (subscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service")) != null) {
            for (SubscriptionInfo subscriptionInfo : subscriptionManager.getActiveSubscriptionInfoList()) {
                if (subscriptionInfo.getIccId().equals(str)) {
                    return subscriptionInfo.getDisplayName().toString();
                }
            }
        }
        return "";
    }

    public static void call(Context context, String str) {
        Cursor query = context.getApplicationContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, null, null, "display_name");
        while (query.moveToNext()) {
            Long valueOf = Long.valueOf(query.getLong(query.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX)));
            String string = query.getString(query.getColumnIndex("display_name"));
            String string2 = query.getString(query.getColumnIndex("mimetype"));
            if (string.equals(str) && string2.equals("vnd.android.cursor.item/vnd.com.whatsapp.voip.call")) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setDataAndType(Uri.parse("content://com.android.contacts/data/" + valueOf), "vnd.android.cursor.item/vnd.com.whatsapp.voip.call");
                intent.setPackage(VideoCallController.AppPackage.WHATSAPP);
                context.startActivity(intent);
            }
        }
    }

    public static boolean cehckContactHasWhatsapp(Context context, String str, String str2, boolean z) {
        Cursor query = context.getApplicationContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, null, null, "display_name");
        while (query.moveToNext()) {
            Long valueOf = Long.valueOf(query.getLong(query.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX)));
            String string = query.getString(query.getColumnIndex("display_name"));
            String string2 = query.getString(query.getColumnIndex("mimetype"));
            if (string.equals(str) && string2.equals(str2)) {
                if (z) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setDataAndType(Uri.parse("content://com.android.contacts/data/" + valueOf), str2);
                    intent.setPackage(VideoCallController.AppPackage.WHATSAPP);
                    context.startActivity(intent);
                }
                return true;
            }
        }
        return false;
    }

    public static void findNumberOnWhatsapp(String str) {
        Cursor query = BoloApplication.getApplication().getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{"data3"}, "mimetype = ? AND raw_contact_id = ? ", new String[]{"vnd.android.cursor.item/vnd.com.whatsapp.profile", str}, "1 LIMIT 1");
        if (query.moveToNext()) {
            query.getString(0);
        }
    }

    public static boolean findWhatsAppPerson(Context context, String str) {
        Cursor query = context.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, new String[]{FieldType.FOREIGN_ID_FIELD_SUFFIX}, "contact_id = ? AND account_type IN (?)", new String[]{String.valueOf(getContactID(context, str)), VideoCallController.AppPackage.WHATSAPP}, null);
        if (query.moveToNext()) {
            query.getString(0);
        }
        return false;
    }

    public static Long getContactID(Context context, String str) {
        Cursor query = context.getApplicationContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, null, null, "display_name");
        while (query.moveToNext()) {
            Long valueOf = Long.valueOf(query.getLong(query.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX)));
            String string = query.getString(query.getColumnIndex("display_name"));
            String string2 = query.getString(query.getColumnIndex("mimetype"));
            if (string.equals(str)) {
                if (string2.equals("vnd.android.cursor.item/vnd.com.whatsapp.voip.call")) {
                    whatsAppCall(context, valueOf, "vnd.android.cursor.item/vnd.com.whatsapp.voip.call");
                }
                return valueOf;
            }
        }
        return 0L;
    }

    public static long getContactID(ContentResolver contentResolver, String str) {
        Cursor cursor = null;
        try {
            try {
                cursor = contentResolver.query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), new String[]{FieldType.FOREIGN_ID_FIELD_SUFFIX}, null, null, null);
                if (!cursor.moveToFirst()) {
                    if (cursor != null) {
                        cursor.close();
                    }
                    return -1L;
                }
                long j = cursor.getLong(cursor.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX));
                if (cursor != null) {
                    cursor.close();
                }
                return j;
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor != null) {
                    cursor.close();
                }
                return -1L;
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public static String hasWhatsapp(String str) {
        Cursor query = BoloApplication.getApplication().getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, new String[]{FieldType.FOREIGN_ID_FIELD_SUFFIX}, "contact_id = ? AND account_type IN (?)", new String[]{str, VideoCallController.AppPackage.WHATSAPP}, null);
        if (query != null) {
            String string = query.moveToNext() ? query.getString(0) : null;
            query.close();
            return string;
        }
        return "";
    }

    public static void makeCall(final Context context, final String str) {
        if (ContextCompat.checkSelfPermission(context, BoloPermission.PHONE_CALLS) != 0) {
            Dexter.withContext(context).withPermission(BoloPermission.PHONE_CALLS).withListener(new PermissionListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.utils.CallLogUtils.1
                @Override // com.karumi.dexter.listener.single.PermissionListener
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                }

                @Override // com.karumi.dexter.listener.single.PermissionListener
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    try {
                        ((TelecomManager) context.getSystemService("telecom")).placeCall(Uri.fromParts("tel", str, null), new Bundle());
                    } catch (Exception unused) {
                        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + str));
                        intent.addFlags(268435456);
                        try {
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException unused2) {
                            Toast.makeText(context, "Could not find an activity to place the call.", 0).show();
                        }
                    }
                }

                @Override // com.karumi.dexter.listener.single.PermissionListener
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).check();
            return;
        }
        try {
            ((TelecomManager) context.getSystemService("telecom")).placeCall(Uri.fromParts("tel", str, null), new Bundle());
        } catch (Exception unused) {
        }
    }

    public static void whatsAppCall(Context context, Long l, String str) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.parse("content://com.android.contacts/data/" + l), str);
        intent.setPackage(VideoCallController.AppPackage.WHATSAPP);
        context.startActivity(intent);
    }

    public static String timeForCalculateForCallDetails(Date date) {
        if (date == null) {
            return "";
        }
        Date currentFullDate = getCurrentFullDate();
        if (currentFullDate.getDate() == date.getDate()) {
            return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date);
        }
        if (currentFullDate.getYear() == date.getYear()) {
            return new SimpleDateFormat("dd MMM hh:mm a", Locale.getDefault()).format(date);
        }
        return new SimpleDateFormat("dd MMM YYYY hh:mm a", Locale.getDefault()).format(date);
    }

    public static long getCurrentDate() {
        return Calendar.getInstance().getTimeInMillis();
    }

    private static Date getCurrentFullDate() {
        return new Date(System.currentTimeMillis());
    }
}
