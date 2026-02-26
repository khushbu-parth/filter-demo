package com.cast.tv.screen.mirroring.screencasting.supportedices;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Process;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.cast.tv.screen.mirroring.screencasting.R;

import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ConstantMethod {
    private static String TAG = "TestClass: ";
    public static String current_date;
    public static String current_date_time;
    public static Date current_datetime;
    public static String current_time;
    public static SimpleDateFormat df;
    public static boolean is_online;
    private static Dialog loading_dialog;
    private static TextView loading_dialog_message;
    private static Context mContext;
    public static SimpleDateFormat sdf;
    public static SimpleDateFormat sdt;

    public ConstantMethod(Context context) {
        mContext = context;
    }

    public static Typeface ChangeTypeFaceGOTHICB(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "AE01014D.TTF");
    }

    public static int dpToPx(Context context, int i) {
        return Math.round(((float) i) * (context.getResources().getDisplayMetrics().xdpi / 160.0f));
    }

    public static void BottomNavigationColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
            activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.colorPrimary));
        }
    }

    public static void BottomNavigationColor_Splash_Screen(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
            activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.black));
        }
    }


    public static String GetCurrentDateTime() {
        try {
            Calendar instance = Calendar.getInstance();
            PrintStream printStream = System.out;
            printStream.println("Current time => " + instance.getTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa");
            df = simpleDateFormat;
            String format = simpleDateFormat.format(instance.getTime());
            current_date_time = format;
            current_datetime = df.parse(format);
            sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdt = new SimpleDateFormat("hh:mm:ss");
            current_date = sdf.format(current_datetime);
            current_time = sdt.format(current_datetime);
            current_time = (String) DateFormat.format("hh:mm:ss aaa", Calendar.getInstance().getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return current_date_time;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting() || !connectivityManager.getActiveNetworkInfo().isAvailable() || !connectivityManager.getActiveNetworkInfo().isConnected()) {
            is_online = false;
            return false;
        }
        is_online = true;
        return true;
    }

    public static void ShowSuccessToast(Context context, String str) {
        Toast.makeText(context, str, 1).show();
    }

    public static void ShowInfoToast(Context context, String str) {
        Toast.makeText(context, str, 1).show();
    }

    public static void ShowWarningToast(Context context, String str) {
        Toast.makeText(context, str, 1).show();
    }

    @SuppressLint("WrongConstant")
    public static void ShowErrorToast(Context context, String str) {
        Toast.makeText(context, str, 1).show();
    }


    public static final String md5(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest) {
                String hexString = Integer.toHexString(b & 255);
                while (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                stringBuffer.append(hexString);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void LoadingDialog(Context context, String str) {
        Dialog dialog = new Dialog(context, R.style.TransparentBackground);
        loading_dialog = dialog;
        dialog.requestWindowFeature(1);
        loading_dialog.setContentView(R.layout.iuc_dialog_loading);
        loading_dialog_message = (TextView) loading_dialog.findViewById(R.id.dialog_loading_txt_message);
        loading_dialog_message.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf"));
        loading_dialog_message.setText(str);
        loading_dialog.show();
    }

    private static void ShowLoadingDialog(Context context) {
        LoadingDialog(context, "Fetching AdMob Consent");
    }

    private static void DismissLoadingDialog() {
        Dialog dialog = loading_dialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void ExitApp(Activity activity) {
        activity.finishAndRemoveTask();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    public static void FinishApp(Activity activity) {
        activity.finish();
    }

}
