package com.cast.tv.screen.mirroring.screencasting.splashExit;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cast.tv.screen.mirroring.screencasting.R;
import com.library.info.CastTvAppManager;


public class Web_Activity extends AppCompatActivity {

    private WebView webPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        webPrivacyPolicy = (WebView) findViewById(R.id.wvPrivacyPolicy);

        WebSettings webSettings = webPrivacyPolicy.getSettings();
        webSettings.setJavaScriptEnabled(true);// enable javascript
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
                Toast.makeText(Web_Activity.this, description, Toast.LENGTH_SHORT).show();
            }
        });

        webPrivacyPolicy.loadUrl(Glob.privacy_link);
    }

    @Override
    public void onBackPressed() {
        CastTvAppManager.getInstance(this).showInterstitialBackAd(this, () -> {
            finish();
        });


    }

}
