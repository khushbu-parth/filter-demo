package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Locale;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.databinding.ActivityWebviewBinding;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.AppLangSessionManager;

public class WebviewAcitivity extends AppCompatActivity {
    String IntentTitle = "";
    String IntentURL;
    AppLangSessionManager appLangSessionManager;
    ActivityWebviewBinding binding;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = (ActivityWebviewBinding) DataBindingUtil.setContentView(this, R.layout.activity_webview);
        getWindow().setFlags(1024, 1024);
        this.IntentURL = getIntent().getStringExtra("URL");
        this.IntentTitle = getIntent().getStringExtra("Title");
        this.binding.imBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                WebviewAcitivity.this.onBackPressed();
            }
        });
        this.binding.TVTitle.setText(this.IntentTitle);
        this.appLangSessionManager = new AppLangSessionManager(this);
        setLocale(this.appLangSessionManager.getLanguage());
        this.binding.swipeRefreshLayout.post(new Runnable() {

            public void run() {
                WebviewAcitivity.this.binding.swipeRefreshLayout.setRefreshing(true);
                WebviewAcitivity webviewAcitivity = WebviewAcitivity.this;
                webviewAcitivity.LoadPage(webviewAcitivity.IntentURL);
            }
        });
        this.binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                WebviewAcitivity webviewAcitivity = WebviewAcitivity.this;
                webviewAcitivity.LoadPage(webviewAcitivity.IntentURL);
            }
        });
    }

    public void LoadPage(String str) {
        this.binding.webView1.setWebViewClient(new MyBrowser());
        this.binding.webView1.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView webView, int i) {
                if (i == 100) {
                    WebviewAcitivity.this.binding.swipeRefreshLayout.setRefreshing(false);
                } else {
                    WebviewAcitivity.this.binding.swipeRefreshLayout.setRefreshing(true);
                }
            }
        });
        this.binding.webView1.getSettings().setLoadsImagesAutomatically(true);
        this.binding.webView1.getSettings().setJavaScriptEnabled(true);
        this.binding.webView1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        this.binding.webView1.loadUrl(str);
    }

    public void setLocale(String str) {
        Locale locale = new Locale(str);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public class MyBrowser extends WebViewClient {
        private MyBrowser() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            webView.loadUrl(str);
            return true;
        }
    }
}