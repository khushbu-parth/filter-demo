package com.co.casttotv.screenmirroring.mirroring.cast.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ActivityPolicyBinding;

public class PolicyActivity extends AppCompatActivity {
    ActivityPolicyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_policy);

        binding.webView.loadUrl("https://appsprivacypolicyfinder.blogspot.com/");
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setVisibility(View.INVISIBLE);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                binding.progress.setVisibility(View.GONE);
                binding.webView.setVisibility(View.VISIBLE);
            }
        });
    }
}