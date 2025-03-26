package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.databinding.ActivityLoginBinding;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.SharePrefs;

public class LoginActivity extends AppCompatActivity {
    LoginActivity activity;
    ActivityLoginBinding binding;
    private String cookies;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = (ActivityLoginBinding) DataBindingUtil.setContentView(this, R.layout.activity_login);  getWindow().setFlags(1024, 1024);
        this.activity = this;
        LoadPage();
        this.binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                LoginActivity.this.LoadPage();
            }
        });
    }

    public void LoadPage() {
        this.binding.webView.getSettings().setJavaScriptEnabled(true);
        this.binding.webView.clearCache(true);
        this.binding.webView.setWebViewClient(new MyBrowser());
        CookieSyncManager.createInstance(this.activity);
        CookieManager.getInstance().removeAllCookie();
        this.binding.webView.loadUrl("https://www.instagram.com/accounts/login/");
        this.binding.webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView webView, int i) {
                if (i == 100) {
                    LoginActivity.this.binding.swipeRefreshLayout.setRefreshing(false);
                } else {
                    LoginActivity.this.binding.swipeRefreshLayout.setRefreshing(true);
                }
            }
        });
    }

    public String getCookie(String str, String str2) {
        String cookie = CookieManager.getInstance().getCookie(str);
        if (cookie != null && !cookie.isEmpty()) {
            String[] split = cookie.split(";");
            for (String str3 : split) {
                if (str3.contains(str2)) {
                    return str3.split("=")[1];
                }
            }
        }
        return null;
    }

    public class MyBrowser extends WebViewClient {
        private MyBrowser() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            webView.loadUrl(str);
            return true;
        }

        public void onLoadResource(WebView webView, String str) {
            super.onLoadResource(webView, str);
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            LoginActivity.this.cookies = CookieManager.getInstance().getCookie(str);
            try {
                String cookie = LoginActivity.this.getCookie(str, "sessionid");
                String cookie2 = LoginActivity.this.getCookie(str, "csrftoken");
                String cookie3 = LoginActivity.this.getCookie(str, "ds_user_id");
                if (cookie != null && cookie2 != null && cookie3 != null) {
                    SharePrefs.getInstance(LoginActivity.this.activity).putString(SharePrefs.COOKIES, LoginActivity.this.cookies);
                    SharePrefs.getInstance(LoginActivity.this.activity).putString(SharePrefs.CSRF, cookie2);
                    SharePrefs.getInstance(LoginActivity.this.activity).putString(SharePrefs.SESSIONID, cookie);
                    SharePrefs.getInstance(LoginActivity.this.activity).putString(SharePrefs.USERID, cookie3);
                    SharePrefs.getInstance(LoginActivity.this.activity).putBoolean(SharePrefs.ISINSTALOGIN, true);
                    LoginActivity.this.binding.webView.destroy();
                    Intent intent = new Intent();
                    intent.putExtra("result", "result");
                    LoginActivity.this.setResult(-1, intent);
                    LoginActivity.this.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }
    }
}