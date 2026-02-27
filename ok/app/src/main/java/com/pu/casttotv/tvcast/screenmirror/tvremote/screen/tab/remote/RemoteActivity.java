package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.remote;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.customview.CustomViewPager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.DialogNotSupport;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVType;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.ConnectActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.DialogDisconnect;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.howto.HowToYouActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.firetv.FireTVManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

public class RemoteActivity extends BaseActivity implements View.OnClickListener {
    private DialogNotSupport dialogNotSupport;
    private ImageView imvChannel;
    private ImageView imvConnect;
    private ImageView imvRemote;
    private LinearLayout llBack;
    private LinearLayout llConnect;
    private LinearLayout llHelp;
    private PagerHomeAdapter pagerHomeAdapter;
    private RelativeLayout rlChannel;
    private RelativeLayout rlHeader;
    private RelativeLayout rlRemote;
    private TextView tv_title;
    private CustomViewPager vpHome;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_remote);

        initView();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DialogNotSupport dialogNotSupport = this.dialogNotSupport;
        if (dialogNotSupport != null) {
            dialogNotSupport.clear();
        }
        try {
            FireTVManager fireTVManager = RemoteFragment.fireTVManager;
            if (fireTVManager != null) {
                fireTVManager.disconnectTelevision();
                try {
                    RemoteFragment.fireTVManager.getAdbConnection().close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                    StringBuilder sb = new StringBuilder();
                    sb.append("disconnect: ");
                    sb.append(e2.getMessage());
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("KEY_CONNECT")) {
            if (TVConnectUtils.getInstance().isConnected()) {
                loadPage();
            }
            setStatusConnect();
        }
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        this.rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        this.tv_title = (TextView) findViewById(R.id.tvTitleTab);
        this.llBack = (LinearLayout) findViewById(R.id.llBack);
        this.llConnect = (LinearLayout) findViewById(R.id.llConnect);
        this.imvConnect = (ImageView) findViewById(R.id.imvConnect);
        this.vpHome = (CustomViewPager) findViewById(R.id.vpHome);
        this.rlChannel = (RelativeLayout) findViewById(R.id.rlChannel);
        this.rlRemote = (RelativeLayout) findViewById(R.id.rlRemote);
        this.imvRemote = (ImageView) findViewById(R.id.imvRemote);
        this.imvChannel = (ImageView) findViewById(R.id.imvChannel);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llHelp);
        this.llHelp = linearLayout;
        linearLayout.setVisibility(0);
        this.llHelp.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.remote.RemoteActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent(RemoteActivity.this, HowToYouActivity.class);
                intent.putExtra("TYPE_HTY", 2);
                RemoteActivity.this.startActivity(intent);
                Utils.nextScreen(RemoteActivity.this);
            }
        });
        this.llBack.setOnClickListener(this);
        this.vpHome.setOnClickListener(this);
        this.rlChannel.setOnClickListener(this);
        this.rlRemote.setOnClickListener(this);
        this.llConnect.setOnClickListener(this);
        this.dialogNotSupport = DialogNotSupport.getInstance(this);
        this.rlHeader.setBackgroundColor(getResources().getColor(R.color.color_main_remote));
        Window window = getWindow();
        window.clearFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_txt));
        loadPage();
    }

    private void LoadTitle(boolean z) {
        if (z) {
            if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                setFragmentRoku();
                return;
            } else if (TVType.isLGTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                setFragmentLG();
                return;
            } else if (TVType.isSamsungTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                setFragmentSamSung();
                return;
            } else if (TVType.isFireTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                setFragmentFireTV();
                return;
            } else if (TVType.isSonyTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                setFragmentSony();
                return;
            } else {
                this.tv_title.setText("Remote TV");
                try {
                    if (this.dialogNotSupport != null && TVConnectUtils.getInstance().isConnectWeb) {
                        this.dialogNotSupport.showInstance();
                        this.dialogNotSupport.setMessage(getString(R.string.title_connect_web_remote));
                        return;
                    } else if (this.dialogNotSupport == null || !TVConnectUtils.getInstance().isConnected()) {
                        return;
                    } else {
                        this.dialogNotSupport.showInstance();
                        this.dialogNotSupport.setMessage(getString(R.string.no_support_remote));
                        return;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            }
        }
        this.tv_title.setText("Channel");
    }

    private void setFragmentFireTV() {
        this.tv_title.setText("Remote FireTV");
    }

    private void setFragmentSony() {
        this.tv_title.setText("Remote Sony");
    }

    private void setFragmentSamSung() {
        this.tv_title.setText("Remote SamSung");
    }

    private void setFragmentRoku() {
        this.tv_title.setText("Remote Roku");
    }

    private void setFragmentLG() {
        this.tv_title.setText("Remote LG");
    }

    private void loadPage() {
        setStatusConnect();
        LoadTitle(true);
        PagerHomeAdapter pagerHomeAdapter = new PagerHomeAdapter(getSupportFragmentManager());
        this.pagerHomeAdapter = pagerHomeAdapter;
        this.vpHome.setAdapter(pagerHomeAdapter);
        this.vpHome.setOffscreenPageLimit(2);
        this.vpHome.setPagingEnabled(false);
        setCurrentPage(0, true);
        this.vpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                RemoteActivity.this.setCurrentPage(i, false);
            }
        });
    }

    public void setCurrentPage(int i, boolean z) {
        if (z) {
            this.vpHome.setCurrentItem(i);
        }
        if (i == 0) {
            this.imvRemote.setSelected(true);
            this.imvChannel.setSelected(false);
            LoadTitle(true);
        } else if (i != 1) {
        } else {
            this.imvRemote.setSelected(false);
            this.imvChannel.setSelected(true);
            LoadTitle(false);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setStatusConnect() {
        if (this.imvConnect != null) {
            boolean z = TVConnectUtils.getInstance().isConnectWeb;
            int i = R.drawable.ic_not_connect;
            if (z) {
                this.imvConnect.setImageResource(R.drawable.ic_not_connect);
                return;
            }
            ImageView imageView = this.imvConnect;
            if (TVConnectUtils.getInstance().isConnected()) {
                i = R.drawable.hover_screen;
            }
            imageView.setImageResource(i);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                onBackPressed();
                return;
            case R.id.llConnect:
                if (TVConnectUtils.getInstance().isConnected() && !TVConnectUtils.getInstance().isConnectWeb) {
                    new DialogDisconnect(this).show();
                    return;
                }
                startActivity(new Intent(this, ConnectActivity.class));
                Utils.nextScreen(this);
                return;
            case R.id.rlChannel:
                setCurrentPage(1, true);
                return;
            case R.id.rlRemote:
                setCurrentPage(0, true);
                return;
            default:
                return;
        }
    }
}
