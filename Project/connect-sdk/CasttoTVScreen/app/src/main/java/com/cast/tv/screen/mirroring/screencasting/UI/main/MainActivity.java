package com.cast.tv.screen.mirroring.screencasting.UI.main;

import static android.view.View.GONE;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.cast.tv.screen.mirroring.screencasting.Base.BaseActivity;
import com.cast.tv.screen.mirroring.screencasting.Callback.MainTopTextClick;
import com.cast.tv.screen.mirroring.screencasting.Contract.Contracts;
import com.cast.tv.screen.mirroring.screencasting.Helper.AudioVisualHelper;
import com.cast.tv.screen.mirroring.screencasting.Helper.DLNAHelper;
import com.cast.tv.screen.mirroring.screencasting.Model.FileModel;
import com.cast.tv.screen.mirroring.screencasting.Observer.ConnectStatus;
import com.cast.tv.screen.mirroring.screencasting.Observer.RewardDialogEvent;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.Report.ReportUtil;
import com.cast.tv.screen.mirroring.screencasting.splashExit.Glob;
import com.cast.tv.screen.mirroring.screencasting.UI.audio_visual.AudioVisualFragment;
import com.cast.tv.screen.mirroring.screencasting.UI.audio_visual.DirectoriesFragment;
import com.cast.tv.screen.mirroring.screencasting.UI.cast.AudioVideoCastActivity;
import com.cast.tv.screen.mirroring.screencasting.UI.cast.PhotoCastActivity;
import com.cast.tv.screen.mirroring.screencasting.Utils.L;
import com.cast.tv.screen.mirroring.screencasting.Utils.ListUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.MaxRewardUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.T;
import com.cast.tv.screen.mirroring.screencasting.Utils.net.NetStateChangeReceiver;
import com.cast.tv.screen.mirroring.screencasting.Utils.net.NetUtil;
import com.cast.tv.screen.mirroring.screencasting.Utils.net.NetworkType;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.lib.screening.bean.DeviceInfo;
import com.library.info.CastTvAppManager;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import org.fourthline.cling.support.model.TransportState;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_CHECK_SETTINGS = 214;
    SettingsClient settingsClient;
    LocationSettingsRequest locationSettingsRequest;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private FrameLayout mFrameLayout;
    private ImageView mImageCover;
    private ImageView mImagePlay;
    private View mPlayBomView;
    private TextView mTvConnect;
    private TextView mTvPlayName;
    private int openPageType;
    private List<Fragment> mFragments = new ArrayList();
    private int mIndex = -1;
    private boolean isReportClickPhotoDir = false;
    private boolean isReportClickAudioDir = false;
    private boolean isReportClickVideoDir = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public static void safedk_MainActivity_startActivity_01e7c8c258bb36d69606c2f836bdf877(MainActivity p0, Intent p1) {
        if (p1 == null) {
            return;
        }
        p0.startActivity(p1);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle bundle) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        NetStateChangeReceiver.registerReceiver(this);
        super.onCreate(bundle);
        ReportUtil.loadHomePage();


    }

    @Override
    protected void init() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(100));
        builder.setAlwaysShow(true);
        CastTvAppManager.getInstance(this).showNativeAds(this, findViewById(R.id.fl_native_banner), findViewById(R.id.native_space_img), 2);
        CastTvAppManager.getInstance(this).showBannerAds(this, findViewById(R.id.fl_banner));
        this.locationSettingsRequest = builder.build();
        this.settingsClient = LocationServices.getSettingsClient((Activity) this);
        this.mFragmentManager = getSupportFragmentManager();
        this.mFrameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        findViewById(R.id.view_photo).setOnClickListener(this);
        findViewById(R.id.view_video).setOnClickListener(this);
        findViewById(R.id.view_audio).setOnClickListener(this);

        View findViewById = findViewById(R.id.rl_play_bom);
        this.mPlayBomView = findViewById;
        findViewById.setOnClickListener(this);
        ImageView imageView = (ImageView) findViewById(R.id.image_disconnect);
        this.mImagePlay = imageView;
        imageView.setOnClickListener(this);
        setNetworkChange();
        setConnectDlnaStatus();
    }

    private void setMainTopText() {
        String str = "Screen mirroring requires your phone and target device to connect to the same WIFI. Still do not understand?";
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new StyleSpan(1), 84, str.length(), 33);
        spannableString.setSpan(new MainTopTextClick(this.mContext, "Still do not understand?"), 84, str.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 84, str.length(), 33);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_disconnect:
                bomImageStop();
                return;
            case R.id.rl_play_bom:
                FileModel value = AudioVisualHelper.mCastFileModel.getValue();
                if (value != null) {
                    if (value.getFileType() == 272) {
                        try {
                            startActivity(PhotoCastActivity.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    } else {
                        try {
                            startActivity(AudioVideoCastActivity.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
                hideBomPlay();
                return;

            case R.id.view_audio:
                try {
                    CastTvAppManager.getInstance(this).showInterstitialAd(this, () -> {
                        Glob.idcheckads = 2;
                        ReportUtil.clickAudio();
                        setAdInterMainClick(Contracts.OPEN_VIEW_TYPE_AUDIO_LIST);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            case R.id.view_photo:
                try {
                    CastTvAppManager.getInstance(this).showInterstitialAd(this, () -> {
                        Glob.idcheckads = 2;
                        ReportUtil.clickPhoto();
                        setAdInterMainClick(Contracts.OPEN_VIEW_TYPE_IMAGE_LIST);
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;

            case R.id.view_video:
                try {

                    CastTvAppManager.getInstance(this).showInterstitialAd(this, () -> {
                        Glob.idcheckads = 2;
                        ReportUtil.clickVideo();
                        setAdInterMainClick(768);
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;

            default:
                return;
        }
    }

    private void showScreenMirror() {
        showClickScreenMirror();
    }

    public void startSetting() {
        try {
            ReportUtil.clickScreenMirror();
            safedk_MainActivity_startActivity_01e7c8c258bb36d69606c2f836bdf877(this, new Intent("android.settings.CAST_SETTINGS"));
        } catch (Exception unused) {
            T.showShort(this.mContext, "The device does not support wireless projection");
        }
    }

    private void showClickScreenMirror() {
//        startSetting();

        openmirrorsetting();
    }

    private void bomImageStop() {
        FileModel value = AudioVisualHelper.mCastFileModel.getValue();
        if (value == null) {
            DLNAHelper.stop();
        } else if (value.getFileType() == 272) {
            DLNAHelper.stop();
        } else if (DLNAHelper.mConnectStatus.getValue() == ConnectStatus.PLAYING) {
            DLNAHelper.pause();
            DLNAHelper.mConnectStatus.setValue(ConnectStatus.PAUSE);
        } else {
            DLNAHelper.play();
            DLNAHelper.mConnectStatus.setValue(ConnectStatus.PLAYING);
        }
        DLNAHelper.stopPlayPhotoService();
    }

    private void setAdInterMainClick(int i) {
        this.openPageType = i;
//        if (!SubscribeUtil.isLoadAd(4099)) {
//            openAudioVisual(i);
//        } else {
        showClickPhoto(i);
//        }
    }


    public void openmirrorsetting() {

        this.settingsClient.checkLocationSettings(this.locationSettingsRequest).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public final void onSuccess(Object obj) {
                setCasting$9$HSMainActivity((LocationSettingsResponse) obj);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public final void onFailure(Exception exc) {
                setCasting$10$HSMainActivity(exc);
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.e("GPS", "checkLocationSettings -> onCanceled");

            }
        });


    }

    public void setCasting$9$HSMainActivity(LocationSettingsResponse locationSettingsResponse) {
        Intent intent;
        try {
            if (Build.MANUFACTURER.equals("Xiaomi")) {
                intent = new Intent("android.settings.WIRELESS_SETTINGS");
            } else {
                intent = new Intent("android.settings.WIFI_DISPLAY_SETTINGS");
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                try {
                    startActivity(new Intent("com.samsung.wfd.LAUNCH_WFD_PICKER_DLG"));
                } catch (Exception unused) {
                    startActivity(new Intent("android.settings.CAST_SETTINGS"));
                }
            } catch (Exception unused2) {
                Toast.makeText(this, "Device Not Supported", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setCasting$10$HSMainActivity(Exception exc) {
        int statusCode = ((ApiException) exc).getStatusCode();
        if (statusCode == 6) {
            try {
                ((ResolvableApiException) exc).startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
            } catch (IntentSender.SendIntentException unused) {
                Log.e("===", "Unable to execute request.");
            }
        } else if (statusCode != 8502) {
            return;
        }
        Log.e("===", "Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
    }


    private void showClickPhoto(int i) {
        try {
            openAudioVisual(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openAudioVisual(final int i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionX.init(this).permissions("android.permission.ACCESS_MEDIA_LOCATION").request(new RequestCallback() {
                @Override
                public final void onResult(boolean z, List list, List list2) {
                    MainActivity.this.openAudioVisual$0$MainActivity(i, z, list, list2);
                }
            });
        } else {
            PermissionX.init(this).permissions("android.permission.READ_EXTERNAL_STORAGE").request(new RequestCallback() {
                @Override
                public final void onResult(boolean z, List list, List list2) {
                    MainActivity.this.openAudioVisual$0$MainActivity(i, z, list, list2);
                }
            });
        }
    }

    public void openAudioVisual$0$MainActivity(int i, boolean z, List list, List list2) {
        if (z) {
            if (i == 772) {
                if (!this.isReportClickPhotoDir) {
                    this.isReportClickPhotoDir = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ReportUtil.clickPhotoDir();
                        }
                    });

                }
            } else if (i == 769) {
                if (!this.isReportClickVideoDir) {
                    this.isReportClickVideoDir = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ReportUtil.clickVideoDir();
                        }
                    });
                }
            } else if (i == 771 && !this.isReportClickAudioDir) {
                this.isReportClickAudioDir = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ReportUtil.clickAudioDir();
                    }
                });
            }
            AudioVisualFragment newInstance = AudioVisualFragment.newInstance(i);
            this.mFragment = newInstance;
            addFragment(newInstance);
            return;
        }
        T.showShort(this.mContext, "You have denied the necessary permissions");
    }

    public void openDirectories(final String str) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionX.init(this).permissions("android.permission.ACCESS_MEDIA_LOCATION").request(new RequestCallback() {
                @Override
                public final void onResult(boolean z, List list, List list2) {
                    MainActivity.this.openDirectories$1$MainActivity(str, z, list, list2);
                }
            });
        } else {
            PermissionX.init(this).permissions("android.permission.READ_EXTERNAL_STORAGE").request(new RequestCallback() {
                @Override
                public final void onResult(boolean z, List list, List list2) {
                    MainActivity.this.openDirectories$1$MainActivity(str, z, list, list2);
                }
            });
        }
    }

    public void openDirectories$1$MainActivity(String str, boolean z, List list, List list2) {
        if (z) {
            DirectoriesFragment newInstance = DirectoriesFragment.newInstance(str);
            this.mFragment = newInstance;
            addFragment(newInstance);
            return;
        }
        T.showShort(this.mContext, "You have denied the necessary permissions");
    }

    private void addFragment(Fragment fragment) {
        findViewById(R.id.view_photo).setVisibility(GONE);
        findViewById(R.id.view_video).setVisibility(GONE);
        findViewById(R.id.view_audio).setVisibility(GONE);

        if (this.mIndex < 0) {
            this.mFragmentManager.beginTransaction().add(R.id.frame_layout, fragment).show(fragment).commitAllowingStateLoss();
        } else {
            this.mFragmentManager.beginTransaction().add(R.id.frame_layout, fragment).show(fragment).hide(this.mFragments.get(this.mIndex)).commitAllowingStateLoss();
        }
        this.mFragments.add(fragment);
        this.mIndex++;
        if (this.mFrameLayout.getVisibility() == GONE) {
            this.mFrameLayout.setVisibility(View.VISIBLE);
        }
    }

    private void onBackPress() {
        int size = ListUtil.getSize(this.mFragments);
        int i = this.mIndex;
        if (size > i && i >= 0) {
            if (i > 0) {
                this.mFragmentManager.beginTransaction().remove(this.mFragments.get(this.mIndex)).addToBackStack("freeholder").show(this.mFragments.get(this.mIndex - 1)).commitAllowingStateLoss();

            } else {
                this.mFragmentManager.beginTransaction().remove(this.mFragments.get(this.mIndex)).addToBackStack("filenames").commitAllowingStateLoss();
                this.mFrameLayout.setVisibility(GONE);
                int i2 = this.openPageType;
                if (i2 == 773) {
                    ReportUtil.backPhoto();
                    findViewById(R.id.view_photo).setVisibility(View.VISIBLE);
                    findViewById(R.id.view_video).setVisibility(View.VISIBLE);
                    findViewById(R.id.view_audio).setVisibility(View.VISIBLE);
                } else if (i2 == 768) {
                    ReportUtil.backVideo();
                    findViewById(R.id.view_photo).setVisibility(View.VISIBLE);
                    findViewById(R.id.view_video).setVisibility(View.VISIBLE);
                    findViewById(R.id.view_audio).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.view_photo).setVisibility(View.VISIBLE);
                    findViewById(R.id.view_video).setVisibility(View.VISIBLE);
                    findViewById(R.id.view_audio).setVisibility(View.VISIBLE);
                }
                this.isReportClickPhotoDir = false;
                this.isReportClickVideoDir = false;
                this.isReportClickAudioDir = false;
            }
            this.mFragments.remove(this.mIndex);
            this.mIndex--;
            return;
        }
        ReportUtil.quitApp();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }

    private void setNetworkChange() {
        PermissionX.init(this).permissions("android.permission.ACCESS_FINE_LOCATION").request(new RequestCallback() {
            @Override
            public final void onResult(boolean z, List list, List list2) {
                MainActivity.this.setNetworkChange$3$MainActivity(z, list, list2);
            }
        });
    }

    public void setNetworkChange$3$MainActivity(boolean z, List list, List list2) {
        NetUtil.mNetworkType.observeForever(new Observer() {
            @Override
            public final void onChanged(Object obj) {
                MainActivity.this.null$2$MainActivity((NetworkType) obj);
            }
        });
    }

    public void null$2$MainActivity(NetworkType networkType) {
        if (DLNAHelper.isConnectDevice()) {

        }
        L.i("Main", "networkType: " + networkType.desc);


    }

    private void setConnectDlnaStatus() {
        DLNAHelper.mConnectStatus.observeForever(new Observer() {
            @Override
            public final void onChanged(Object obj) {
                MainActivity.this.setConnectDlnaStatus$4$MainActivity((ConnectStatus) obj);
            }
        });
    }

    public void setConnectDlnaStatus$4$MainActivity(ConnectStatus connectStatus) {
        FileModel value = AudioVisualHelper.mCastFileModel.getValue();
        if (value == null) {
            hideBomPlay();
        } else if (connectStatus == ConnectStatus.PLAYING) {
            showBomPlay();
            if (value.getFileType() == 272) {
                this.mImagePlay.setImageResource(R.drawable.icon_stop_54);
            } else {
                this.mImagePlay.setImageResource(R.drawable.icon_pause_54);
            }
        } else if (connectStatus == ConnectStatus.PAUSE) {
            if (value.getFileType() == 272) {
                this.mImagePlay.setImageResource(R.drawable.icon_stop_54);
            } else {
                this.mImagePlay.setImageResource(R.drawable.icon_play_54);
            }
        } else if (connectStatus == ConnectStatus.FAIL) {
            L.d("Main", "cast fail");
        } else {
            hideBomPlay();
        }
    }

    private void showBomPlay() {
        View view = this.mPlayBomView;
        if (view != null) {
            view.setVisibility(View.VISIBLE);
            DeviceInfo connectDevice = DLNAHelper.getConnectDevice();
            if (connectDevice == null) {
                return;
            }
            if (this.mTvConnect == null) {
                this.mTvConnect = (TextView) findViewById(R.id.text_connect);
            }
            this.mTvConnect.setText(connectDevice.getName());
            setObserverFileModel();
        }
    }

    private void setObserverFileModel() {
        AudioVisualHelper.mCastFileModel.observe(this, new Observer() {
            @Override
            public final void onChanged(Object obj) {
                MainActivity.this.updateFileModel((FileModel) obj);
            }
        });
        DLNAHelper.mTransportState.observe(this, new Observer() {
            @Override
            public final void onChanged(Object obj) {
                MainActivity.this.setObserverFileModel$5$MainActivity((TransportState) obj);
            }
        });
    }

    public void setObserverFileModel$5$MainActivity(TransportState transportState) {
        try {
            FileModel value = AudioVisualHelper.mCastFileModel.getValue();
            if (value == null || value.getFileType() == 272) {
                return;
            }
            if (TransportState.valueOf(transportState.getValue()) == TransportState.PLAYING) {
                this.mImagePlay.setImageResource(R.drawable.icon_pause_54);
            } else {
                this.mImagePlay.setImageResource(R.drawable.icon_play_54);
            }
        } catch (Exception unused) {
            L.e("MainActivity", "");
        }
    }

    public void updateFileModel(FileModel fileModel) {
        if (fileModel == null) {
            return;
        }
        if (this.mImageCover == null) {
            this.mImageCover = (ImageView) findViewById(R.id.image_connect_cover);
        }
        int fileType = fileModel.getFileType();
        if (fileType == 272) {
            L.i("MainActivity", "path: " + fileModel.getPath());
            Glide.with(this.mContext).load(fileModel.getPath()).into(this.mImageCover);
            this.mImageCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else if (fileType == 273) {
            this.mImageCover.setImageBitmap(fileModel.getMiniKind());
            this.mImageCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            this.mImageCover.setImageResource(R.mipmap.icon_audio_cover);
            this.mImageCover.setScaleType(ImageView.ScaleType.CENTER);
            this.mImageCover.setBackgroundColor(getResources().getColor(R.color.color_BAB5B5));
        }
        if (this.mTvPlayName == null) {
            this.mTvPlayName = (TextView) findViewById(R.id.text_play_name);
        }
        this.mTvPlayName.setText(fileModel.getDisplayName());
    }

    private void hideBomPlay() {
        DLNAHelper.stopPlayPhotoService();
        AudioVisualHelper.mCastFileModel.removeObservers(this);
        DLNAHelper.mTransportState.removeObservers(this);
        View view = this.mPlayBomView;
        if (view != null) {
            view.setVisibility(GONE);
        }
        if (ListUtil.getSize(this.mFragments) <= 0) {
            AudioVisualHelper.recycler();
        }
    }


    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
        L.i("Main", "onDestroy");
        NetUtil.mNetworkType.removeObservers(this);
        DLNAHelper.mConnectStatus.removeObservers(this);
        NetStateChangeReceiver.unRegisterReceiver(this);
        if (this.mFragment != null) {
            this.mFragment = null;
        }
        List<Fragment> list = this.mFragments;
        if (list != null) {
            list.clear();
            this.mFragments = null;
            findViewById(R.id.view_photo).setVisibility(View.VISIBLE);
            findViewById(R.id.view_video).setVisibility(View.VISIBLE);
            findViewById(R.id.view_audio).setVisibility(View.VISIBLE);
        }
        if (this.mFragmentManager != null) {
            this.mFragmentManager = null;
        }
        if (this.mFrameLayout != null) {
            this.mFrameLayout = null;
        }
        if (this.mHandler != null) {
            this.mHandler = null;
        }
        DLNAHelper.recycler();
    }

    public void loadRewardAd() {
        MaxRewardUtil.rewardCastNum();
    }

    @Subscribe
    public void handlerRewardEvent(RewardDialogEvent rewardDialogEvent) {
        if (rewardDialogEvent.mViewType == 1) {
            showRewardDialog();
        }
    }

    private void showRewardDialog() {
        //  ShowRewardAdDialog.newInstance().show(getSupportFragmentManager(), "Reward");
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
//        try {
//            if (DLNAHelper.isConnectDevice()) {
//                Toast.makeText(mContext, "Please dissconnect the device", Toast.LENGTH_SHORT).show();
//            } else {
//                startActivity(new Intent(MainActivity.this, MainCastActivity.class));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        CastTvAppManager.getInstance(this).showInterstitialBackAd(this, () -> {
            startActivity(new Intent(MainActivity.this, MainCastActivity.class));
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
//        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "CastFolder");
//        if (dir.isDirectory())
//        {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++)
//            {
//                new File(dir, children[i]).delete();
//            }
//        }
//        File imagedir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CastFolder");
//        if (imagedir.isDirectory())
//        {
//            String[] children = imagedir.list();
//            for (int i = 0; i < children.length; i++)
//            {
//                new File(imagedir, children[i]).delete();
//            }
//        }
    }
}
