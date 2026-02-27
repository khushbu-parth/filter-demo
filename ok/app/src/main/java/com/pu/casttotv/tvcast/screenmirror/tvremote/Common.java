package com.pu.casttotv.tvcast.screenmirror.tvremote;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.ArrayList;

public class Common {

    public static ArrayList<String> videolist = new ArrayList<>();

    public static void shareApp(Context context) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            final String appName = context.getString(R.string.app_name);
            intent.setType("text/plain");
            StringBuilder sb = new StringBuilder();
            sb.append("\nLet me recommend you this application\n\n");
            sb.append("https://play.google.com/store/apps/details?id=");
            sb.append(BuildConfig.APPLICATION_ID);
            sb.append("\n\n");
            intent.putExtra(Intent.EXTRA_SUBJECT, appName);
            intent.putExtra("android.intent.extra.TEXT", sb.toString());
            context.startActivity(Intent.createChooser(intent, "choose one"));
        } catch (Exception e) {
            e.toString();
        }
    }

    public static void privacypolicy(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.privacy_policy);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        WebView webView = (WebView) dialog.findViewById(R.id.webview_privacy);
        webView.loadUrl("https://appprivacypolicy101.blogspot.com/2021/05/app-privacy-policy.html");
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                viewx.loadUrl(urlx);
                return false;
            }
        });
        ((TextView) dialog.findViewById(R.id.cancle)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void rateUs(Context context) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
        } catch (Exception unused) {
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
