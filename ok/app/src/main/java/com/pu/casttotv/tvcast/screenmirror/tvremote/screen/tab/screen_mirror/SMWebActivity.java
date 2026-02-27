package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.pu.casttotv.tvcast.screenmirror.tvremote.MyApplication;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.databinding.FragmentSmWebBinding;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.ConnectActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.DialogDisconnect;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.data.BusMessages;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.service.ForegroundService;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.youtube_browser.YoutubeBrowserActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@SuppressLint("ResourceType")
public class SMWebActivity extends BaseActivity {
    private FragmentSmWebBinding fragmentSMWebBinding;
    private LinearLayout llBack;
    private LinearLayout llConnect;
    private ServiceConnection serverConnection = new ServiceConnection() {
        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SMWebActivity.this.serverService = ((ForegroundService.ServerServiceBinder) iBinder).getServerService();
        }
    };
    private Intent serverIntent;
    private ForegroundService serverService;
    private TextView tvTitleTab;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        fragmentSMWebBinding = (FragmentSmWebBinding) DataBindingUtil.setContentView(this, R.layout.fragment_sm_web);
        setContentView(fragmentSMWebBinding.getRoot());
        fragmentSMWebBinding.setViewModel(MyApplication.getScreenMirrorViewModel());


        initView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initView() {
        this.llBack = (LinearLayout) findViewById(R.id.llBack);
        this.llConnect = (LinearLayout) findViewById(R.id.llConnect);
        TextView textView = (TextView) findViewById(R.id.tvTitleTab);
        this.tvTitleTab = textView;
        textView.setText("Mirror Web Browser");
        this.llConnect.setVisibility(8);
        this.llBack.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SMWebActivity.this.onBackPressed();
            }
        });
        Intent intent = new Intent(this, ForegroundService.class);
        this.serverIntent = intent;
        bindService(intent, this.serverConnection, 1);

        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(ForegroundService.getStartIntent(this));
        }
        MyApplication.getScreenMirrorViewModel().setServerAddress(MyApplication.getAppData().getServerAddress());
        MyApplication.getScreenMirrorViewModel().setWiFiConnected(MyApplication.getAppData().isWiFiConnected());
        MyApplication.getScreenMirrorViewModel().setScreenSize(MyApplication.getAppData().getScreenSize());
        MyApplication.getScreenMirrorViewModel().setResizeFactor(MyApplication.getAppPreference().getResizeFactor());

        findViewById(R.id.btn_start_screen_mirroring).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TVConnectUtils.getInstance().isConnected()) {
                    new DialogDisconnect(SMWebActivity.this).show();
                    return;
                }
                gotoActivityMain(ConnectActivity.class);
            }
        });

    }

    private void gotoActivityMain(Class cls) {
        Intent intent = new Intent(this, cls);
        if (cls == YoutubeBrowserActivity.class) {
            intent.putExtra("browser_type", "youtube");
        }
        startActivity(intent);
        Utils.nextScreen(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BusMessages busMessages) {
        MediaProjectionManager projectionManager;
        String message = busMessages.getMessage();
        message.hashCode();
        char c = 65535;
        switch (message.hashCode()) {
            case -1927720706:
                if (message.equals("MESSAGE_STATUS_HTTP_OK")) {
                    c = 0;
                    break;
                }
                break;
            case -1789810909:
                if (message.equals("MESSAGE_STATUS_IMAGE_GENERATOR_ERROR")) {
                    c = 1;
                    break;
                }
                break;
            case 445057200:
                if (message.equals("MESSAGE_ACTION_STREAMING_TRY_START")) {
                    c = 2;
                    break;
                }
                break;
            case 1127767442:
                if (message.equals("MESSAGE_STATUS_HTTP_ERROR_PORT_IN_USE")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                EventBus.getDefault().removeStickyEvent(BusMessages.class);
                MyApplication.getScreenMirrorViewModel().setHttpServerError(false);
                return;
            case 1:
                EventBus.getDefault().removeStickyEvent(BusMessages.class);
                EventBus.getDefault().post(new BusMessages("MESSAGE_ACTION_STREAMING_STOP"));
                startActivity(getStartIntent(this).setFlags(131072));
                if (isFinishing()) {
                    return;
                }
                new AlertDialog.Builder(this).setTitle(getString(R.string.main_activity_error_title)).setMessage(getString(R.string.main_activity_error_msg_unknown_format)).setIcon(R.drawable.ic_main_activity_error_24dp).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
                return;
            case 2:
                EventBus.getDefault().removeStickyEvent(BusMessages.class);
                if (!MyApplication.getAppData().isWiFiConnected() || MyApplication.getAppData().isStreamRunning() || (projectionManager = ForegroundService.getProjectionManager()) == null) {
                    return;
                }
                startActivityForResult(projectionManager.createScreenCaptureIntent(), 1);
                return;
            case 3:
                MyApplication.getScreenMirrorViewModel().setHttpServerError(true);
                return;
            default:
                return;
        }
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SMWebActivity.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        MyApplication.getAppData().setActivityRunning(true);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        MyApplication.getAppData().setActivityRunning(false);
        super.onStop();
    }

    @Override
    public void onActivityResult(int i, final int i2, final Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 1) {
            if (i != 2) {
                return;
            }
            MyApplication.getAppPreference().updatePreference();
            return;
        }
        try {
            if (i2 != -1) {
                Toast.makeText(this, getString(R.string.main_activity_toast_cast_permission_deny), 0).show();
                return;
            }
            final MediaProjectionManager projectionManager = ForegroundService.getProjectionManager();
            if (projectionManager == null) {
                return;
            }
            new Handler().postDelayed(new Runnable() {
                @Override // java.lang.Runnable
                public void run() {
                    MediaProjection mediaProjection = projectionManager.getMediaProjection(i2, intent);
                    if (mediaProjection == null) {
                        return;
                    }
                    ForegroundService.setMediaProjection(mediaProjection);
                    EventBus.getDefault().post(new BusMessages("MESSAGE_ACTION_STREAMING_START"));
                    if (!MyApplication.getAppPreference().isMinimizeOnStream()) {
                        return;
                    }
                    SMWebActivity.this.startActivity(new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME").setFlags(268435456));
                }
            }, 500L);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
