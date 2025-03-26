package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;

public class Utils {
    public static String RootDirectoryFacebook = "/StatusSaver/Facebook/";
    public static File RootDirectoryFacebookShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Facebook");
    public static String RootDirectoryInsta = "/StatusSaver/Insta/";
    public static File RootDirectoryInstaShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Insta");
    public static String RootDirectoryTwitter = "/StatusSaver/Twitter/";
    public static File RootDirectoryTwitterShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Twitter");
    public static File RootDirectoryWhatsappShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Whatsapp");
    public static String RootDirectoryVimeo = "/StatusSaver/Vimeo/";
    public static File RootDirectoryVimeoShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Vimeo");
    public static String TikTokUrl = "http://codingdunia.com/ccprojects/tiktok/api/getContent/";
    public static Dialog customDialog;
    private static Context context;

    public Utils(Context context2) {
        context = context2;
    }

    public static void setToast(Context context2, String str) {
        Toast makeText = Toast.makeText(context2, str, Toast.LENGTH_SHORT);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }

    public static void createFileFolder() {
        if (!RootDirectoryFacebookShow.exists()) {
            RootDirectoryFacebookShow.mkdirs();
        }
        if (!RootDirectoryInstaShow.exists()) {
            RootDirectoryInstaShow.mkdirs();
        }
        if (!RootDirectoryTwitterShow.exists()) {
            RootDirectoryTwitterShow.mkdirs();
        }
        if (!RootDirectoryWhatsappShow.exists()) {
            RootDirectoryWhatsappShow.mkdirs();
        }
        if (!RootDirectoryVimeoShow.exists()) {
            RootDirectoryVimeoShow.mkdirs();
        }
    }

    public static void showProgressDialog(Activity activity) {
        System.out.println("Show");
        Dialog dialog = customDialog;
        if (dialog != null) {
            dialog.dismiss();
            customDialog = null;
        }
        customDialog = new Dialog(activity);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, (ViewGroup) null);
        customDialog.setCancelable(false);
        customDialog.setContentView(inflate);
        if (!customDialog.isShowing() && !activity.isFinishing()) {
            customDialog.show();
        }
    }

    public static void hideProgressDialog(Activity activity) {
        System.out.println("Hide");
        Dialog dialog = customDialog;
        if (dialog != null && dialog.isShowing()) {
            customDialog.dismiss();
        }
    }

    public static void startDownload(String str, String str2, Context context2, String str3) {
        setToast(context2, context2.getResources().getString(R.string.download_started));
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
        request.setAllowedNetworkTypes(3);
        request.setNotificationVisibility(1);
        request.setTitle(str3 + "");
        request.setVisibleInDownloadsUi(true);
        String str4 = Environment.DIRECTORY_DOWNLOADS;
        request.setDestinationInExternalPublicDir(str4, str2 + str3);
        ((DownloadManager) context2.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                MediaScannerConnection.scanFile(context2, new String[]{new File(Environment.DIRECTORY_DOWNLOADS + "/" + str2 + str3).getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String str, Uri uri) {
                    }
                });
                return;
            }
            context2.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File(Environment.DIRECTORY_DOWNLOADS + "/" + str2 + str3))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareImage(Context context2, String str) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.TEXT", context2.getResources().getString(R.string.share_txt));
            intent.putExtra("android.intent.extra.STREAM", Uri.parse(MediaStore.Images.Media.insertImage(context2.getContentResolver(), str, "", (String) null)));
            intent.setType("image/*");
            context2.startActivity(Intent.createChooser(intent, context2.getResources().getString(R.string.share_image_via)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public static void shareImageVideoOnWhatsapp(Context context2, String str, boolean z) {
        Uri parse = Uri.parse(str);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setPackage("com.whatsapp");
        intent.putExtra("android.intent.extra.TEXT", "");
        intent.putExtra("android.intent.extra.STREAM", parse);
        if (z) {
            intent.setType("video/*");
        } else {
            intent.setType("image/*");
        }
        intent.addFlags(1);
        try {
            context2.startActivity(intent);
        } catch (Exception unused) {
            setToast(context2, context2.getResources().getString(R.string.whatsapp_not_installed));
        }
    }

    @SuppressLint("WrongConstant")
    public static void shareVideo(Context context2, String str) {

        Uri parse = Uri.parse(str);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("video/*");
        intent.putExtra("android.intent.extra.STREAM", parse);
        intent.addFlags(1);
        try {
            context2.startActivity(Intent.createChooser(intent, "Share Video using"));
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(context2, context2.getResources().getString(R.string.no_app_installed), Toast.LENGTH_LONG).show();
        }
    }

    public static void OpenApp(Context context2, String str) {
        Intent launchIntentForPackage = context2.getPackageManager().getLaunchIntentForPackage(str);
        if (launchIntentForPackage != null) {
            context2.startActivity(launchIntentForPackage);
        } else {
            setToast(context2, context2.getResources().getString(R.string.app_not_available));
        }
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.equalsIgnoreCase("0");
    }

    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}