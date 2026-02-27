package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.browser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.ironsource.mediationsdk.utils.IronSourceConstants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;

/* loaded from: classes4.dex */
public class FileBrowserActivity extends BaseActivity {
    private Button button1;
    private LinearLayout llBack;
    private LinearLayout llConnect;
    private String mRootUrl;
    private WebBroadCast mServerManager;
    private TextView tvTitleTab;
    private TextView tv_ip;

    public void onServerStop() {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_file_browser);
        initView();
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        EventBus.getDefault().register(this);
        WebBroadCast webBroadCast = new WebBroadCast(this);
        this.mServerManager = webBroadCast;
        webBroadCast.register();
        this.tvTitleTab = (TextView) findViewById(R.id.tvTitleTab);
        this.llConnect = (LinearLayout) findViewById(R.id.llConnect);
        this.llBack = (LinearLayout) findViewById(R.id.llBack);
        this.button1 = (Button) findViewById(R.id.button_1);
        this.tv_ip = (TextView) findViewById(R.id.tv_ip);
        this.tvTitleTab.setText(getString(R.string.title_web_cast));
        this.llConnect.setVisibility(8);
        this.llBack.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.browser.FileBrowserActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FileBrowserActivity.this.onBackPressed();
            }
        });
        this.button1.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.browser.FileBrowserActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RxBus.getDefault().post("{\"url\":\"https://cdn.searchenginejournal.com/wp-content/uploads/2019/07/the-essential-guide-to-using-images-legally-online-1520x800.png\",\"type\":3,\"height\":300,\"width\":300,\"orientation\":0}");
            }
        });
        this.mServerManager.startServer();
        TVConnectUtils.getInstance().mWebBroadCast = this.mServerManager;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("KEY_CONNECTED_WEB")) {
            TVConnectUtils.getInstance().isConnectWeb = true;
            EventBus.getDefault().post(new MessageEvent("KEY_CONNECT"));
            Intent intent = new Intent();
            intent.putExtra(IronSourceConstants.EVENTS_RESULT, IronSourceConstants.EVENTS_RESULT);
            setResult(-1, intent);
            onFinish();
        }
        if (messageEvent.getMessage().equals("KEY_DISCONNECTED_WEB")) {
            TVConnectUtils.getInstance().isConnectWeb = false;
        }
        messageEvent.getMessage().equals("KEY_CONNECTED_WEB");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onServerStart(String str, int i) {
        if (!TextUtils.isEmpty(str)) {
            LinkedList linkedList = new LinkedList();
            String str2 = "http://" + str + ":" + i;
            this.mRootUrl = str2;
            this.tv_ip.setText(str2);
            linkedList.add(this.mRootUrl);
            linkedList.add("http://" + str + ":8936/login.html");
            StringBuilder sb = new StringBuilder();
            sb.append("onServerStart1: ");
            sb.append(this.mRootUrl);
            return;
        }
        this.mRootUrl = null;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onServerStart2: ");
        sb2.append(this.mRootUrl);
    }

    public void onServerError(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("onServerError: ");
        sb.append(str);
        Log.e("##TAG", "onServerError: ");
    }

    public void open(View view) {
        if (!TextUtils.isEmpty(this.mRootUrl)) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(this.mRootUrl));
            startActivity(intent);
        }
    }
}
