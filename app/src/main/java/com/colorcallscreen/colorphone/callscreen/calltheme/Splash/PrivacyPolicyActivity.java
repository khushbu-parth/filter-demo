package com.colorcallscreen.colorphone.callscreen.calltheme.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.colorcallscreen.colorphone.callscreen.calltheme.R;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private WebView webPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        webPrivacyPolicy = (WebView) findViewById(R.id.wvPrivacyPolicy);

        WebSettings webSettings = webPrivacyPolicy.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webPrivacyPolicy.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }


            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(PrivacyPolicyActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        });

        webPrivacyPolicy.loadUrl("https://sakshatinfotech50.blogspot.com/2023/09/color-caller-screen-phone-dialer.html");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}