package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utility;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;

public final class IntentUtility {
    private IntentUtility() {
    }

    public static void Share_Url(Context context, String str, String str2) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", str);
            intent.putExtra("android.intent.extra.TEXT", str2);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void startCallActivity(Context context, String str) {
        try {
            context.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(str)));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void startEmailActivity(Context context, String str, String str2, String str3) {
        try {
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:".concat(String.valueOf(str))));
            intent.putExtra("android.intent.extra.SUBJECT", str2);
            intent.putExtra("android.intent.extra.TEXT", str3);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean startIntentActivity(Context context, String str) {
        if (str != null && str.startsWith("mailto:")) {
            MailTo parse = MailTo.parse(str);
            startEmailActivity(context, parse.getTo(), parse.getSubject(), parse.getBody());
            return true;
        } else if (str != null && str.startsWith("tel:")) {
            startCallActivity(context, str);
            return true;
        } else if (str != null && str.startsWith("sms:")) {
            startSmsActivity(context, str);
            return true;
        } else if (str == null || !str.startsWith("geo:")) {
            return false;
        } else {
            startMapSearchActivity(context, str);
            return true;
        }
    }

    private static void startMapSearchActivity(Context context, String str) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void startSmsActivity(Context context, String str) {
        try {
            context.startActivity(new Intent("android.intent.action.SENDTO", Uri.parse(str)));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
