package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.playcast;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.AudioModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.ItemYoutube;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MediaModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.PhotoOnlineModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.ManagerDataPlay;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVType;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.browser.BrowserAction;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.ConnectActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.DialogDisconnect;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.drive.GoogleDriveItem;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.premium.IapUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.remote.RemoteActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.downloadable_resource_model;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.file_type;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.static_variables;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Const;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.ServeHTTPD2;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.SharedPrefsUtil;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Tracking;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks.RxRequestTask;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.CommandHelper;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.RokuRequestTypes;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.resize.CastPhotoOnlineError;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.resize.PhotoUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.connectsdk.core.MediaInfo;
import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.MediaControl;
import com.connectsdk.service.capability.MediaPlayer;
import com.connectsdk.service.capability.VolumeControl;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.sessions.LaunchSession;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jaku.core.JakuRequest;
import com.jaku.core.KeypressKeyValues;
import com.jaku.request.KeypressRequest;
import com.wonshinhyo.dragrecyclerview.DragRecyclerView;
import com.wonshinhyo.dragrecyclerview.SimpleDragListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;

@SuppressLint("WrongConstant")
public class PlayCastActivity extends BaseActivity implements View.OnClickListener {
    private final int REFRESH_INTERVAL_MS;
    private DragVideoAdapter allVideoAdapter;
    private DragAudioAdapter audioAdapter;
    private boolean closeRate;
    private RelativeLayout ctPhoto;
    private ConstraintLayout ctVideo;
    private int currentPos;
    private MediaControl.DurationListener durationListener;
    private File file;
    private ImageView imvConnect;
    private ImageView imvNextPhoto;
    private ImageView imvNextVideo5s;
    private ImageView imvPhoto;
    private ImageView imvPlayPhoto;
    private ImageView imvPlayVideo;
    private ImageView imvPreviousPhoto;
    private ImageView imvPreviousVideo5s;
    private LinearLayout imvRemote;
    private ImageView imvVideo;
    boolean isMute;
    private boolean isPlaying;
    private ArrayList<downloadable_resource_model> listAudioBrowser;
    private ArrayList<downloadable_resource_model> listPhotoBrowser;
    private List<ItemYoutube> listYoutube;
    private LinearLayout llBack;
    private LinearLayout llConnect;
    private LinearLayout ll_mute;
    private LinearLayout ll_show_list;
    private LinearLayout ll_stopVideo;
    private MediaControl mMediaControl;
    private ViewGroup main_ads_native;
    int numberCastSS;
    private int numberShowAds;
    public MediaControl.PlayStateListener playStateListener;
    int port;
    private MediaControl.PositionListener positionListener;
    private ProgressBar prLoading;
    private RecyclerView rcvImage;
    private Timer refreshTimer;
    private RelativeLayout rlBanner;
    private LinearLayout rlt_volume_down_media_player;
    private LinearLayout rlt_volume_up_media_player;
    private SeekBar seekbar_time;
    private ServeHTTPD2 serveHTTPD;
    String titleCurrent;
    public long totalTimeDuration;
    private TextView tvDuration;
    private TextView tvName;
    private TextView tvTime;
    private TextView tvTitleTab;
    private int typeCast;
    private ViewPager vpImage;
    private List<MediaModel> listMedia = new ArrayList();
    private List<AudioModel> listAudio = new ArrayList();
    private List<PhotoOnlineModel> listPhotoOnline = new ArrayList();
    private List<GoogleDriveItem> listDrive = new ArrayList();

    /* JADX INFO: Access modifiers changed from: private */
    public static void lambda$performRequest$0(Object obj) throws Exception {
    }

    public PlayCastActivity() {
        new ArrayList();
        this.currentPos = 0;
        this.port = 8093;
        this.typeCast = 0;
        this.isPlaying = false;
        this.totalTimeDuration = -1L;
        this.REFRESH_INTERVAL_MS = (int) TimeUnit.SECONDS.toMillis(1L);
        this.durationListener = new MediaControl.DurationListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.26
            @Override // com.connectsdk.service.capability.listeners.ErrorListener
            public void onError(ServiceCommandError serviceCommandError) {
                Log.e("####TAG", "durationListener onError: " + serviceCommandError.getMessage());
            }

            @Override // com.connectsdk.service.capability.listeners.ResponseListener
            public void onSuccess(Long l) {
                totalTimeDuration = l.longValue();
                StringBuilder sb = new StringBuilder();
                sb.append("onSuccess DurationListener ");
                sb.append(l);
                tvDuration.setText(Utils.formatTime(l.intValue()));
                seekbar_time.setMax((int) l.longValue());
            }
        };
        this.playStateListener = new MediaControl.PlayStateListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.27
            @Override // com.connectsdk.service.capability.listeners.ErrorListener
            public void onError(ServiceCommandError serviceCommandError) {
                Log.e("####TAG", "playStateListener onError: " + serviceCommandError.getMessage());
                StringBuilder sb = new StringBuilder();
                sb.append("Playstate Listener error = ");
                sb.append(serviceCommandError);
            }

            @Override // com.connectsdk.service.capability.listeners.ResponseListener
            public void onSuccess(MediaControl.PlayStateStatus playStateStatus) {
                StringBuilder sb = new StringBuilder();
                sb.append("Playstate changed | playState = ");
                sb.append(playStateStatus);
                int i = AnonymousClass38.$SwitchMap$com$connectsdk$service$capability$MediaControl$PlayStateStatus[playStateStatus.ordinal()];
                if (i != 1) {
                    if (i == 2) {
                        tvTime.setText("00:00");
                        tvDuration.setText("00:00");
                        seekbar_time.setProgress(0);
                    }
                    stopUpdating();
                    return;
                }
                startUpdating();
                if (mMediaControl == null || !TVConnectUtils.getInstance().getConnectableDevice().hasCapability(MediaControl.Duration)) {
                    return;
                }
                mMediaControl.getDuration(durationListener);
            }
        };
        this.positionListener = new MediaControl.PositionListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.29
            @Override // com.connectsdk.service.capability.listeners.ErrorListener
            public void onError(ServiceCommandError serviceCommandError) {
            }

            @Override // com.connectsdk.service.capability.listeners.ResponseListener
            public void onSuccess(Long l) {
                tvTime.setText(Utils.formatTime(l.intValue()));
                seekbar_time.setProgress(l.intValue());
            }
        };
        this.numberCastSS = 0;
        this.closeRate = false;
        this.numberShowAds = 0;
        this.isMute = false;
        this.titleCurrent = "";
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.fragment_play_cast);
        AdsManager.CallNativeAdLoad(this, findViewById(R.id.native_container), AdsManager.NATIVE_SMALL);
        initView();
        EventBus.getDefault().register(this);
        if (IapUtils.isIapAll() || IapUtils.isPaymentMirror()) {
            this.rlBanner.setVisibility(8);
            this.main_ads_native.setVisibility(8);
            return;
        }
        callbackDone();
    }

    public Unit callbackFail() {
        return Unit.INSTANCE;
    }

    public Unit callbackDone() {
        return Unit.INSTANCE;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.backScreen(this);
    }

    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("KEY_TIME_WEB")) {
            runOnUiThread(new Runnable() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.1
                @Override // java.lang.Runnable
                public void run() {
                    tvTime.setText(Utils.formatTime(messageEvent.getDuration() * 1000));
                    seekbar_time.setProgress(((int) messageEvent.getDuration()) * 1000);
                }
            });
            return;
        }
        if (TVConnectUtils.getInstance().isConnected()) {
            imvConnect.setImageResource(R.drawable.hover_screen);
        } else {
            imvConnect.setImageResource(R.drawable.screen);
        }
        if (!IapUtils.isIapAll() && !IapUtils.isPaymentMirror()) {
            return;
        }
        this.rlBanner.setVisibility(8);
        this.main_ads_native.setVisibility(8);
    }

    private void initView() {
        this.ll_stopVideo = (LinearLayout) findViewById(R.id.ll_stopVideo);
        this.ll_show_list = (LinearLayout) findViewById(R.id.ll_show_list);
        this.imvRemote = (LinearLayout) findViewById(R.id.imvRemote);
        this.rlt_volume_down_media_player = (LinearLayout) findViewById(R.id.rlt_volume_down_media_player);
        this.ll_mute = (LinearLayout) findViewById(R.id.ll_mute);
        this.rlt_volume_up_media_player = (LinearLayout) findViewById(R.id.rlt_volume_up_media_player);
        this.vpImage = (ViewPager) findViewById(R.id.vpImage);
        this.rcvImage = (RecyclerView) findViewById(R.id.rcvImage);
        this.rlBanner = (RelativeLayout) findViewById(R.id.rlBanner);
        this.main_ads_native = (ViewGroup) findViewById(R.id.main_ads_native);
        this.tvName = (TextView) findViewById(R.id.tvName);
        this.llBack = (LinearLayout) findViewById(R.id.llBack);
        this.ctVideo = (ConstraintLayout) findViewById(R.id.ctVideo);
        this.seekbar_time = (SeekBar) findViewById(R.id.seekbar_time);
        this.prLoading = (ProgressBar) findViewById(R.id.prLoading);
        this.tvTime = (TextView) findViewById(R.id.tvTime);
        this.tvDuration = (TextView) findViewById(R.id.tvDuration);
        this.imvNextVideo5s = (ImageView) findViewById(R.id.imvNextVideo5s);
        this.imvPlayVideo = (ImageView) findViewById(R.id.imvPlayVideo);
        this.imvPreviousVideo5s = (ImageView) findViewById(R.id.imvPreviousVideo5s);
        this.imvVideo = (ImageView) findViewById(R.id.imvVideo);
        this.tvTitleTab = (TextView) findViewById(R.id.tvTitleTab);
        this.ctPhoto =  findViewById(R.id.ctPhoto);
        this.imvPhoto = (ImageView) findViewById(R.id.imvPhoto);
        this.imvPlayPhoto = (ImageView) findViewById(R.id.imvPlayPhoto);
        this.imvPreviousPhoto = (ImageView) findViewById(R.id.imvPreviousPhoto);
        this.imvNextPhoto = (ImageView) findViewById(R.id.imvNextPhoto);
        this.imvConnect = (ImageView) findViewById(R.id.imvConnect);
        this.llConnect = (LinearLayout) findViewById(R.id.llConnect);
        this.imvPlayPhoto.setOnClickListener(this);
        this.llBack.setOnClickListener(this);
        this.imvPreviousPhoto.setOnClickListener(this);
        this.imvNextPhoto.setOnClickListener(this);
        this.imvPlayVideo.setOnClickListener(this);
        this.imvPreviousVideo5s.setOnClickListener(this);
        this.imvNextVideo5s.setOnClickListener(this);
        this.ll_stopVideo.setOnClickListener(this);
        this.imvRemote.setOnClickListener(this);
        this.ll_show_list.setOnClickListener(this);
        this.rlt_volume_down_media_player.setOnClickListener(this);
        this.ll_mute.setOnClickListener(this);
        this.rlt_volume_up_media_player.setOnClickListener(this);
        this.llConnect.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (TVConnectUtils.getInstance().isConnected()) {
                    new DialogDisconnect(PlayCastActivity.this).show();
                } else {
                    startActivity(new Intent(PlayCastActivity.this, ConnectActivity.class));
                }
            }
        });
        try {
            this.listMedia = ManagerDataPlay.getInstance().getListMedia();
            this.listAudio = ManagerDataPlay.getInstance().getListAudio();
            this.listPhotoOnline = ManagerDataPlay.getInstance().getListPhotoOnl();
            this.listDrive = ManagerDataPlay.getInstance().getListDriver();
            this.listYoutube = ManagerDataPlay.getInstance().getListYoutube();
            this.typeCast = ManagerDataPlay.getInstance().getTypePlay();
            this.currentPos = ManagerDataPlay.getInstance().getPosSelected();
            this.listPhotoBrowser = static_variables.get_downloadable_resource_model_By_Type(file_type.IMAGE);
            static_variables.get_downloadable_resource_model_By_Type(file_type.VIDEO);
            this.listAudioBrowser = static_variables.get_downloadable_resource_model_By_Type(file_type.AUDIO);
        } catch (Exception unused) {
        }
        if (TVConnectUtils.getInstance().isConnected()) {
            imvConnect.setImageResource(R.drawable.hover_screen);
        } else {
            imvConnect.setImageResource(R.drawable.screen);
        }
        if (TVConnectUtils.getInstance().getConnectableDevice() != null) {
            this.seekbar_time.setEnabled(TVConnectUtils.getInstance().getConnectableDevice().hasCapability(MediaControl.Seek));
        }
        this.seekbar_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.3
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    if (typeCast != 0) {
                        if (TVConnectUtils.getInstance().isConnectWeb) {
                            if (typeCast == 2) {
                                BrowserAction.seekAudio(seekBar.getProgress());
                            } else {
                                BrowserAction.seek(seekBar.getProgress());
                            }
                        } else if (TVConnectUtils.getInstance().isConnected()) {
                            mMediaControl.seek(seekBar.getProgress(), null);
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        if (TVConnectUtils.getInstance().isConnectWeb && ManagerDataPlay.getInstance().duration != null) {
            this.tvDuration.setText(Utils.formatTime(ManagerDataPlay.getInstance().duration.longValue()));
            this.seekbar_time.setMax(ManagerDataPlay.getInstance().duration.intValue());
        }
        switch (this.typeCast) {
            case 0:
                List<MediaModel> list = this.listMedia;
                if (list == null) {
                    Toast.makeText(this, "Sorry, we're getting an error, please try again", 0).show();
                    return;
                } else if (list.size() <= this.currentPos) {
                    return;
                } else {
                    this.ctPhoto.setVisibility(0);
                    this.ctVideo.setVisibility(8);
                    this.tvTitleTab.setText(getString(R.string.txt_photo));
                    castPhoto(ManagerDataPlay.getInstance().getListMedia().get(this.currentPos).getPhotoUri());
                    setImageOff();
                    return;
                }
            case 1:
                List<MediaModel> list2 = this.listMedia;
                if (list2 == null) {
                    Toast.makeText(this, "Sorry, we're getting an error, please try again", 0).show();
                    return;
                } else if (list2.size() <= this.currentPos) {
                    return;
                } else {
                    this.tvTitleTab.setText(getString(R.string.txt_video));
                    this.ctPhoto.setVisibility(8);
                    this.ctVideo.setVisibility(0);
                    castVideo();
                    return;
                }
            case 2:
                List<AudioModel> list3 = this.listAudio;
                if (list3 == null) {
                    Toast.makeText(this, "Sorry, we're getting an error, please try again", 0).show();
                    return;
                } else if (list3.size() <= this.currentPos) {
                    return;
                } else {
                    this.tvTitleTab.setText(getString(R.string.txt_song));
                    castAudio();
                    this.ctPhoto.setVisibility(8);
                    this.ctVideo.setVisibility(0);
                    return;
                }
            case 3:
                this.tvTitleTab.setText(getString(R.string.txt_youtube));
                castYoutube(ManagerDataPlay.getInstance().pathCast);
                this.ctPhoto.setVisibility(8);
                this.ctVideo.setVisibility(0);
                return;
            case 4:
                List<PhotoOnlineModel> list4 = this.listPhotoOnline;
                if (list4 == null) {
                    Toast.makeText(this, "Sorry, we're getting an error, please try again", 0).show();
                    return;
                } else if (list4.size() <= this.currentPos) {
                    return;
                } else {
                    this.ctPhoto.setVisibility(0);
                    this.ctVideo.setVisibility(8);
                    this.tvTitleTab.setText(getString(R.string.txt_photo_online));
                    castPhotoOnline(ManagerDataPlay.getInstance().getListPhotoOnl().get(this.currentPos).getImageURL());
                    setImageOnline();
                    return;
                }
            case 5:
            case 7:
            default:
                return;
            case 6:
                List<GoogleDriveItem> list5 = this.listDrive;
                if (list5 == null) {
                    Toast.makeText(this, "Sorry, we're getting an error, please try again", 0).show();
                    return;
                } else if (list5.size() <= this.currentPos) {
                    return;
                } else {
                    this.ctPhoto.setVisibility(0);
                    this.ctVideo.setVisibility(8);
                    this.tvTitleTab.setText("Photo Drive");
                    setImageDriver();
                    return;
                }
            case 8:
                this.tvTitleTab.setText("Video Browser");
                castVideoWeb(ManagerDataPlay.getInstance().pathCast);
                this.ctPhoto.setVisibility(8);
                this.ctVideo.setVisibility(0);
                return;
            case 9:
                ArrayList<downloadable_resource_model> arrayList = this.listPhotoBrowser;
                if (arrayList == null || arrayList.size() <= this.currentPos) {
                    return;
                }
                this.ctPhoto.setVisibility(0);
                this.ctVideo.setVisibility(8);
                this.tvTitleTab.setText("Photo Browser");
                castPhotoOnline(this.listPhotoBrowser.get(this.currentPos).getURL());
                setImageBrowser();
                return;
            case 10:
                ArrayList<downloadable_resource_model> arrayList2 = this.listAudioBrowser;
                if (arrayList2 == null || arrayList2.size() <= this.currentPos) {
                    return;
                }
                this.ctPhoto.setVisibility(8);
                this.ctVideo.setVisibility(0);
                this.tvTitleTab.setText("Audio Browser");
                castAudioBrowser();
                return;
        }
    }

    private void setImageOff() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(0);
        this.rcvImage.setLayoutManager(linearLayoutManager);
        final ImageAdapter imageAdapter = new ImageAdapter(this, new ArrayList());
        imageAdapter.setOnclick(new ImageAdapter.IItemClick() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.4
            @Override
            // com.magicapps.casttotv.tv.screen.tab.playcast.ImageAdapter.IItemClick
            public void clickItem(int i) {
                currentPos = i;
                vpImage.setCurrentItem(i, true);
                ManagerDataPlay.getInstance().getListMedia().get(currentPos).getTitle();
                castPhoto(ManagerDataPlay.getInstance().getListMedia().get(currentPos).getPhotoUri());
            }
        });
        this.rcvImage.setAdapter(imageAdapter);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new MediaModel());
        PageImageAdapter pageImageAdapter = new PageImageAdapter(arrayList);
        this.vpImage.setAdapter(pageImageAdapter);
        this.vpImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.5
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                imageAdapter.setSelectedPosition(i);
                linearLayoutManager.scrollToPosition(i);
                currentPos = i;
                ManagerDataPlay.getInstance().getListMedia().get(currentPos).getTitle();
                castPhoto(ManagerDataPlay.getInstance().getListMedia().get(currentPos).getPhotoUri());
            }
        });
        ImageAdapter imageAdapter2 = (ImageAdapter) this.rcvImage.getAdapter();
        if (imageAdapter2 != null) {
            imageAdapter2.clearItems();
            imageAdapter2.addItems(ManagerDataPlay.getInstance().getListMedia());
        }
        pageImageAdapter.clearItems();
        pageImageAdapter.addItems(ManagerDataPlay.getInstance().getListMedia());
        this.vpImage.setAdapter(pageImageAdapter);
        this.vpImage.setCurrentItem(this.currentPos, true);
    }

    private void setImageBrowser() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(0);
        this.rcvImage.setLayoutManager(linearLayoutManager);
        final ImageBrowserAdapter imageBrowserAdapter = new ImageBrowserAdapter(this, new ArrayList());
        imageBrowserAdapter.setOnclick(new ImageBrowserAdapter.IItemClick() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.6
            @Override
            // com.magicapps.casttotv.tv.screen.tab.playcast.ImageBrowserAdapter.IItemClick
            public void clickItem(int i) {
                currentPos = i;
                vpImage.setCurrentItem(i, true);
                castPhotoOnline(((downloadable_resource_model) listPhotoBrowser.get(currentPos)).getURL());
            }
        });
        this.rcvImage.setAdapter(imageBrowserAdapter);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new downloadable_resource_model());
        PageImageBrowserAdapter pageImageBrowserAdapter = new PageImageBrowserAdapter(arrayList);
        this.vpImage.setAdapter(pageImageBrowserAdapter);
        this.vpImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.7
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                imageBrowserAdapter.setSelectedPosition(i);
                linearLayoutManager.scrollToPosition(i);
                currentPos = i;
                castPhotoOnline(((downloadable_resource_model) listPhotoBrowser.get(currentPos)).getURL());
            }
        });
        ImageBrowserAdapter imageBrowserAdapter2 = (ImageBrowserAdapter) this.rcvImage.getAdapter();
        if (imageBrowserAdapter2 != null) {
            imageBrowserAdapter2.clearItems();
            imageBrowserAdapter2.addItems(this.listPhotoBrowser);
        }
        pageImageBrowserAdapter.clearItems();
        pageImageBrowserAdapter.addItems(this.listPhotoBrowser);
        this.vpImage.setAdapter(pageImageBrowserAdapter);
        this.vpImage.setCurrentItem(this.currentPos, true);
    }

    private void setImageOnline() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(0);
        this.rcvImage.setLayoutManager(linearLayoutManager);
        final ImageOnlineAdapter imageOnlineAdapter = new ImageOnlineAdapter(this, new ArrayList());
        imageOnlineAdapter.setOnclick(new ImageOnlineAdapter.IItemClick() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.8
            @Override
            // com.magicapps.casttotv.tv.screen.tab.playcast.ImageOnlineAdapter.IItemClick
            public void clickItem(int i) {
                currentPos = i;
                vpImage.setCurrentItem(i, true);
                castPhotoOnline(ManagerDataPlay.getInstance().getListPhotoOnl().get(currentPos).getImageURL());
            }
        });
        this.rcvImage.setAdapter(imageOnlineAdapter);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new PhotoOnlineModel());
        PageImageOnlineAdapter pageImageOnlineAdapter = new PageImageOnlineAdapter(arrayList);
        this.vpImage.setAdapter(pageImageOnlineAdapter);
        this.vpImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.9
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                imageOnlineAdapter.setSelectedPosition(i);
                linearLayoutManager.scrollToPosition(i);
                currentPos = i;
                castPhotoOnline(ManagerDataPlay.getInstance().getListPhotoOnl().get(currentPos).getImageURL());
            }
        });
        ImageOnlineAdapter imageOnlineAdapter2 = (ImageOnlineAdapter) this.rcvImage.getAdapter();
        if (imageOnlineAdapter2 != null) {
            imageOnlineAdapter2.clearItems();
            imageOnlineAdapter2.addItems(ManagerDataPlay.getInstance().getListPhotoOnl());
        }
        pageImageOnlineAdapter.clearItems();
        pageImageOnlineAdapter.addItems(ManagerDataPlay.getInstance().getListPhotoOnl());
        this.vpImage.setAdapter(pageImageOnlineAdapter);
        this.vpImage.setCurrentItem(this.currentPos, true);
    }

    private void setImageDriver() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(0);
        this.rcvImage.setLayoutManager(linearLayoutManager);
        final ImageDriverAdapter imageDriverAdapter = new ImageDriverAdapter(this, new ArrayList());
        imageDriverAdapter.setOnclick(new ImageDriverAdapter.IItemClick() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.10
            @Override
            // com.magicapps.casttotv.tv.screen.tab.playcast.ImageDriverAdapter.IItemClick
            public void clickItem(int i) {
                currentPos = i;
                vpImage.setCurrentItem(i, true);
                castPhotoOnline(ManagerDataPlay.getInstance().getListDriver().get(currentPos).getThumbnailLink());
            }
        });
        this.rcvImage.setAdapter(imageDriverAdapter);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new GoogleDriveItem());
        PageImageDriverAdapter pageImageDriverAdapter = new PageImageDriverAdapter(arrayList);
        this.vpImage.setAdapter(pageImageDriverAdapter);
        this.vpImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.11
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                imageDriverAdapter.setSelectedPosition(i);
                linearLayoutManager.scrollToPosition(i);
                currentPos = i;
                castPhotoOnline(ManagerDataPlay.getInstance().getListDriver().get(currentPos).getThumbnailLink());
            }
        });
        ImageDriverAdapter imageDriverAdapter2 = (ImageDriverAdapter) this.rcvImage.getAdapter();
        if (imageDriverAdapter2 != null) {
            imageDriverAdapter2.clearItems();
            imageDriverAdapter2.addItems(ManagerDataPlay.getInstance().getListDriver());
        }
        pageImageDriverAdapter.clearItems();
        pageImageDriverAdapter.addItems(ManagerDataPlay.getInstance().getListDriver());
        this.vpImage.setAdapter(pageImageDriverAdapter);
        this.vpImage.setCurrentItem(this.currentPos, true);
    }

    private void showRate() {
        try {
            if (((Boolean) SharedPrefsUtil.getInstance().get(Const.KEY_RATED, Boolean.class)).booleanValue()) {
                return;
            }
            this.closeRate = true;
        } catch (Exception unused) {
        }
    }

    private void castYoutube(String str) {
        if (this.port == 0) {
            this.port = nextFreePort(8000, 999);
        }
        try {
            Glide.with((FragmentActivity) this).load(ManagerDataPlay.getInstance().thumbCast).centerCrop().placeholder(R.drawable.imv_default).into(this.imvVideo);
            this.tvName.setText(ManagerDataPlay.getInstance().titleCast);
            if (TVConnectUtils.getInstance().isConnectWeb) {
                BrowserAction.playVideo(str);
                runOnUiThread(new Runnable() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.12
                    @Override // java.lang.Runnable
                    public void run() {
                        prLoading.setVisibility(8);
                    }
                });
                return;
            }
        } catch (Exception unused) {
        }
        try {
            ServeHTTPD2 serveHTTPD2 = this.serveHTTPD;
            if (serveHTTPD2 != null) {
                serveHTTPD2.stop();
            }
            ServeHTTPD2 serveHTTPD22 = new ServeHTTPD2(this.port);
            this.serveHTTPD = serveHTTPD22;
            serveHTTPD22.start();
        } catch (IOException unused2) {
        }
        Formatter.formatIpAddress(((WifiManager) getApplicationContext().getSystemService("wifi")).getConnectionInfo().getIpAddress());
        this.file = new File(str);
        try {
            MediaInfo build = new MediaInfo.Builder(str, "video/mp4").setTitle(this.file.getName()).setDescription("Casting your Video").build();
            build.getUrl();
            ((MediaPlayer) TVConnectUtils.getInstance().getConnectableDevice().getCapability(MediaPlayer.class)).playMedia(build, true, new MediaPlayer.LaunchListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.13
                @Override // com.connectsdk.service.capability.listeners.ErrorListener
                public void onError(ServiceCommandError serviceCommandError) {
                    serviceCommandError.toString();
                    isPlaying = false;
                    prLoading.setVisibility(8);
                    try {
                        Tracking.trackCast(PlayCastActivity.this, "cast_fail", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_youtube");
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }

                @Override // com.connectsdk.service.capability.listeners.ResponseListener
                public void onSuccess(MediaPlayer.MediaLaunchObject mediaLaunchObject) {
                    mediaLaunchObject.toString();
                    isPlaying = true;
                    imvPlayVideo.setImageResource(R.drawable.ic_pause);
                    controlDuration(mediaLaunchObject);
                    prLoading.setVisibility(8);
                    totalCassSS();
                    try {
                        Tracking.trackCast(PlayCastActivity.this, "cast_success", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_youtube");
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
            });
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    private void castVideoWeb(String str) {
        try {
            Tracking.trackCast(this, "cast_fail", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_youtube");
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e("##TAG", "castVideoWeb: Tracking" + e2.getMessage());
        }
        if (this.port == 0) {
            this.port = nextFreePort(8000, 999);
        }
        try {
            Glide.with((FragmentActivity) this).load(ManagerDataPlay.getInstance().thumbCast).centerCrop().placeholder(R.drawable.imv_default).into(this.imvVideo);
            this.tvName.setText(ManagerDataPlay.getInstance().titleCast);
            if (TVConnectUtils.getInstance().isConnectWeb) {
                BrowserAction.playVideo(str);
                runOnUiThread(new Runnable() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.14
                    @Override // java.lang.Runnable
                    public void run() {
                        prLoading.setVisibility(8);
                    }
                });
                return;
            }
        } catch (Exception unused) {
            Log.e("##TAG", "castVideoWeb: Glide" + unused.getMessage());
        }
        try {
            ServeHTTPD2 serveHTTPD2 = this.serveHTTPD;
            if (serveHTTPD2 != null) {
                serveHTTPD2.stop();
            } else {
                Log.e("##TAG", "castVideoWeb: serveHTTPD2 null");
            }
            ServeHTTPD2 serveHTTPD22 = new ServeHTTPD2(this.port);
            this.serveHTTPD = serveHTTPD22;
            serveHTTPD22.start();
        } catch (IOException unused2) {
            Log.e("##TAG", "castVideoWeb: " + unused2.getMessage());
        }
        Formatter.formatIpAddress(((WifiManager) getApplicationContext().getSystemService("wifi")).getConnectionInfo().getIpAddress());
        this.file = new File(str);
        try {
            String str2 = "video/mp4";
            if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice()) && str.contains("m3u8")) {
                str2 = "videos/hls";
            }
            MediaInfo build = new MediaInfo.Builder(str, str2).setTitle(this.file.getName()).setDescription("Casting your Video").build();
            build.getUrl();
            ((MediaPlayer) TVConnectUtils.getInstance().getConnectableDevice().getCapability(MediaPlayer.class)).playMedia(build, true, new MediaPlayer.LaunchListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.15
                @Override // com.connectsdk.service.capability.listeners.ErrorListener
                public void onError(ServiceCommandError serviceCommandError) {
                    serviceCommandError.toString();
                    isPlaying = false;
                    prLoading.setVisibility(8);
                    try {
                        Tracking.trackCast(PlayCastActivity.this, "cast_fail", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_youtube");
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }

                @Override // com.connectsdk.service.capability.listeners.ResponseListener
                public void onSuccess(MediaPlayer.MediaLaunchObject mediaLaunchObject) {
                    mediaLaunchObject.toString();
                    isPlaying = true;
                    imvPlayVideo.setImageResource(R.drawable.ic_pause);
                    controlDuration(mediaLaunchObject);
                    prLoading.setVisibility(8);
                    totalCassSS();
                    try {
                        Tracking.trackCast(PlayCastActivity.this, "cast_success", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_youtube");
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
            });
        } catch (Exception e3) {
            Log.e("##TAG", "castVideoWeb: " + e3.getMessage());
            e3.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void castPhotoOnline(String str) {
        this.prLoading.setVisibility(0);
        try {
            Tracking.trackCast(this, "start_cast", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_photo_online");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.port = nextFreePort(8000, 999);
        if (str.contains("s220")) {
            str = str.replace("s220", "s800");
            if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                PhotoUtils.Companion.castPhotoOnline(str, this, (DownloadManager) getSystemService("download"), new CastPhotoOnlineError() {
                    @Override
                    public void playAgainOnline(String str2) {
                        castPhotoOnline(str2);
                    }
                });
                return;
            }
        }
        try {
            Glide.with((FragmentActivity) this).asBitmap().load(str).centerCrop().placeholder(R.drawable.imv_default).into(this.imvPhoto);
        } catch (Exception unused) {
        }
        if (TVConnectUtils.getInstance().isConnectWeb) {
            String finalStr = str;
            Glide.with((FragmentActivity) this).asBitmap().load(str).into(new CustomTarget<Bitmap>() {
                @Override // com.bumptech.glide.request.target.Target
                public void onLoadCleared(Drawable drawable) {
                }

//                @Override // com.bumptech.glide.request.target.Target
//                public /* bridge */ void onResourceReady(Object obj, Transition transition) {
//                    onResourceReady((Bitmap) obj, (Transition<? super Bitmap>) transition);
//                }

                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    BrowserAction.displayImage(finalStr, String.valueOf(bitmap.getHeight()), String.valueOf(bitmap.getWidth()), "0");
                    runOnUiThread(new Runnable() {
                        @Override // java.lang.Runnable
                        public void run() {
                            prLoading.setVisibility(8);
                        }
                    });
                }
            });
            return;
        }
        int i = this.typeCast;
        if (i == 4) {
            if (this.listPhotoOnline.size() < 1) {
                return;
            }
        } else if (i == 9) {
            if (this.listPhotoBrowser.size() < 1) {
                return;
            }
        } else if (this.listDrive.size() < 1) {
            return;
        }
        try {
            ServeHTTPD2 serveHTTPD2 = this.serveHTTPD;
            if (serveHTTPD2 != null) {
                serveHTTPD2.stop();
            }
            ServeHTTPD2 serveHTTPD22 = new ServeHTTPD2(this.port);
            this.serveHTTPD = serveHTTPD22;
            serveHTTPD22.start();
            Formatter.formatIpAddress(((WifiManager) getApplicationContext().getSystemService("wifi")).getConnectionInfo().getIpAddress());
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        if (TVConnectUtils.getInstance().getConnectableDevice() == null) {
            return;
        }
        try {
            String string = getString(R.string.cast_photo);
            ((MediaPlayer) TVConnectUtils.getInstance().getConnectableDevice().getCapability(MediaPlayer.class)).displayImage(new MediaInfo.Builder(str, "image/jpeg").setTitle(string).setDescription("" + str).build(), new MediaPlayer.LaunchListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.19
                @Override // com.connectsdk.service.capability.listeners.ErrorListener
                public void onError(ServiceCommandError serviceCommandError) {
                    serviceCommandError.toString();
                    prLoading.setVisibility(8);
                    try {
                        Tracking.trackCast(PlayCastActivity.this, "cast_fail", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_photo_online");
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                }

                @Override // com.connectsdk.service.capability.listeners.ResponseListener
                public void onSuccess(MediaPlayer.MediaLaunchObject mediaLaunchObject) {
                    mediaLaunchObject.toString();
                    prLoading.setVisibility(8);
                    totalCassSS();
                    try {
                        Tracking.trackCast(PlayCastActivity.this, "cast_success", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_photo_online");
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                }
            });
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }

    public void castVideo() {
        Log.e("###TAG", "getId 2 : " + new ConnectableDevice().getId());
        Tracking.trackCast(this, "start_cast", new ConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_video_off");

        this.prLoading.setVisibility(0);
        if (this.port == 0) {
            this.port = nextFreePort(8000, 999);
        }
        List<MediaModel> list = this.listMedia;
        if (list == null || list.size() < 1) {
            return;
        }
        String photoUri = this.listMedia.get(this.currentPos).getPhotoUri();
        try {
            Glide.with((FragmentActivity) this).load(this.listMedia.get(this.currentPos).getPhotoUri()).placeholder(R.drawable.imv_default).centerCrop().into(this.imvVideo);
            this.tvName.setText(this.listMedia.get(this.currentPos).getTitle());
        } catch (Exception unused) {
        }
        try {
            ServeHTTPD2 serveHTTPD2 = this.serveHTTPD;
            if (serveHTTPD2 != null) {
                serveHTTPD2.stop();
            }
            ServeHTTPD2 serveHTTPD22 = new ServeHTTPD2(this.port);
            this.serveHTTPD = serveHTTPD22;
            serveHTTPD22.start();
        } catch (IOException unused2) {
        }
        String formatIpAddress = Formatter.formatIpAddress(((WifiManager) getApplicationContext().getSystemService("wifi")).getConnectionInfo().getIpAddress());
        this.file = new File(photoUri);
        String str = "http://" + formatIpAddress + ":" + this.port + this.file.getPath();
        Log.e("###TAG", "castVideo: " + str);
        try {
            if (TVConnectUtils.getInstance().isConnectWeb) {
                BrowserAction.playVideo(str);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        prLoading.setVisibility(8);
                    }
                });
                return;
            } else {
                Log.e("###TAG", "castVideo str is not connected to Web : ");
            }
        } catch (Exception e3) {
            Log.e("###TAG", "castVideo str Exception : " + e3.getMessage());
            e3.printStackTrace();
        }

        if (TVConnectUtils.getInstance().getConnectableDevice() == null) {
            Log.e("###TAG", "castVideo: returned");
            return;
        }

        try {
            ((MediaPlayer) TVConnectUtils.getInstance().getConnectableDevice().getCapability(MediaPlayer.class)).playMedia(new MediaInfo.Builder(str, "video/mp4").setTitle(this.file.getName()).setDescription("Casting your Video").build(), true, new MediaPlayer.LaunchListener() {
                @Override
                public void onError(ServiceCommandError serviceCommandError) {
                    Log.e("###TAG", "ServiceCommandError onError: " + serviceCommandError.getMessage());
                    serviceCommandError.toString();
                    isPlaying = false;
                    prLoading.setVisibility(8);
                    try {
                        Tracking.trackCast(PlayCastActivity.this, "cast_fail", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_video_off");
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(MediaPlayer.MediaLaunchObject mediaLaunchObject) {
                    mediaLaunchObject.toString();
                    Log.e("###TAG", "onSuccess: " + mediaLaunchObject.toString());
                    isPlaying = true;
                    imvPlayVideo.setImageResource(R.drawable.ic_pause);
                    controlDuration(mediaLaunchObject);
                    prLoading.setVisibility(8);
                    totalCassSS();
                    try {
                        Tracking.trackCast(PlayCastActivity.this, "cast_success", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_video_off");
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                }
            });
        } catch (Exception e4) {
            Log.e("###TAG", "MediaPlayer Exception: " + e4.getMessage());
            e4.printStackTrace();
        }
    }

    private void castAudioBrowser() {
        try {
            Tracking.trackCast(this, "start_cast", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_audio");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.prLoading.setVisibility(0);
        if (this.port == 0) {
            this.port = nextFreePort(8000, 999);
        }
        ArrayList<downloadable_resource_model> arrayList = this.listAudioBrowser;
        if (arrayList == null || arrayList.size() >= 1) {
            String url = this.listAudioBrowser.get(this.currentPos).getURL();
            try {
                Glide.with((FragmentActivity) this).asBitmap().load(Uri.fromFile(new File(url))).centerCrop().placeholder(R.drawable.imv_default).into(this.imvVideo);
                this.tvName.setText(this.listAudioBrowser.get(this.currentPos).getTitle());
            } catch (Exception e3) {
                StringBuilder sb = new StringBuilder();
                sb.append("ErrorImageAudio---");
                sb.append(e3.toString());
            }
            try {
                ServeHTTPD2 serveHTTPD2 = this.serveHTTPD;
                if (serveHTTPD2 != null) {
                    serveHTTPD2.stop();
                }
                ServeHTTPD2 serveHTTPD22 = new ServeHTTPD2(this.port);
                this.serveHTTPD = serveHTTPD22;
                serveHTTPD22.start();
            } catch (IOException unused) {
            }
            Formatter.formatIpAddress(((WifiManager) getApplicationContext().getSystemService("wifi")).getConnectionInfo().getIpAddress());
            this.file = new File(url);
            if (TVConnectUtils.getInstance().isConnectWeb) {
                BrowserAction.playAudio(url);
                runOnUiThread(new Runnable() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.22
                    @Override // java.lang.Runnable
                    public void run() {
                        prLoading.setVisibility(8);
                    }
                });
            } else if (TVConnectUtils.getInstance().getConnectableDevice() == null) {
            } else {
                try {
                    ((MediaPlayer) TVConnectUtils.getInstance().getConnectableDevice().getCapability(MediaPlayer.class)).playMedia(new MediaInfo.Builder(url, "audio/mp3").setTitle(this.file.getName()).setDescription("Casting your Audio").build(), true, new MediaPlayer.LaunchListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.23
                        @Override // com.connectsdk.service.capability.listeners.ErrorListener
                        public void onError(ServiceCommandError serviceCommandError) {
                            serviceCommandError.toString();
                            isPlaying = false;
                            prLoading.setVisibility(8);
                            try {
                                Tracking.trackCast(PlayCastActivity.this, "cast_fail", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_audio");
                            } catch (Exception e4) {
                                e4.printStackTrace();
                            }
                        }

                        @Override // com.connectsdk.service.capability.listeners.ResponseListener
                        public void onSuccess(MediaPlayer.MediaLaunchObject mediaLaunchObject) {
                            mediaLaunchObject.toString();
                            isPlaying = true;
                            imvPlayVideo.setImageResource(R.drawable.ic_pause);
                            controlDuration(mediaLaunchObject);
                            prLoading.setVisibility(8);
                            totalCassSS();
                            try {
                                Tracking.trackCast(PlayCastActivity.this, "cast_success", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_audio");
                            } catch (Exception e4) {
                                e4.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void castAudio() {
        try {
            Tracking.trackCast(this, "start_cast", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_audio");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.prLoading.setVisibility(0);
        if (this.port == 0) {
            this.port = nextFreePort(8000, 999);
        }
        if (this.listAudio.size() >= 1) {
            ArrayList<downloadable_resource_model> arrayList = this.listAudioBrowser;
            if (arrayList != null && arrayList.size() < 1) {
                return;
            }
            String songPath = this.listAudio.get(this.currentPos).getSongPath();
            try {
                Glide.with((FragmentActivity) this).asBitmap().load(Uri.fromFile(new File(songPath))).centerCrop().placeholder(R.drawable.imv_default).into(this.imvVideo);
                this.tvName.setText(this.listAudio.get(this.currentPos).getSongTitle());
            } catch (Exception e3) {
                StringBuilder sb = new StringBuilder();
                sb.append("ErrorImageAudio---");
                sb.append(e3.toString());
            }
            try {
                ServeHTTPD2 serveHTTPD2 = this.serveHTTPD;
                if (serveHTTPD2 != null) {
                    serveHTTPD2.stop();
                }
                ServeHTTPD2 serveHTTPD22 = new ServeHTTPD2(this.port);
                this.serveHTTPD = serveHTTPD22;
                serveHTTPD22.start();
            } catch (IOException unused) {
            }
            String formatIpAddress = Formatter.formatIpAddress(((WifiManager) getApplicationContext().getSystemService("wifi")).getConnectionInfo().getIpAddress());
            this.file = new File(songPath);
            String str = "http://" + formatIpAddress + ":" + this.port + this.file.getPath();
            if (TVConnectUtils.getInstance().isConnectWeb) {
                BrowserAction.playAudio(str);
                runOnUiThread(new Runnable() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.24
                    @Override // java.lang.Runnable
                    public void run() {
                        prLoading.setVisibility(8);
                    }
                });
            } else if (TVConnectUtils.getInstance().getConnectableDevice() == null) {
            } else {
                try {
                    ((MediaPlayer) TVConnectUtils.getInstance().getConnectableDevice().getCapability(MediaPlayer.class)).playMedia(new MediaInfo.Builder(str, "audio/mp3").setTitle(this.file.getName()).setDescription("Casting your Audio").build(), true, new MediaPlayer.LaunchListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.25
                        @Override // com.connectsdk.service.capability.listeners.ErrorListener
                        public void onError(ServiceCommandError serviceCommandError) {
                            serviceCommandError.toString();
                            isPlaying = false;
                            prLoading.setVisibility(8);
                            try {
                                Tracking.trackCast(PlayCastActivity.this, "cast_fail", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_audio");
                            } catch (Exception e4) {
                                e4.printStackTrace();
                            }
                        }

                        @Override // com.connectsdk.service.capability.listeners.ResponseListener
                        public void onSuccess(MediaPlayer.MediaLaunchObject mediaLaunchObject) {
                            mediaLaunchObject.toString();
                            isPlaying = true;
                            imvPlayVideo.setImageResource(R.drawable.ic_pause);
                            controlDuration(mediaLaunchObject);
                            prLoading.setVisibility(8);
                            totalCassSS();
                            try {
                                Tracking.trackCast(PlayCastActivity.this, "cast_success", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_audio");
                            } catch (Exception e4) {
                                e4.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void controlDuration(MediaPlayer.MediaLaunchObject mediaLaunchObject) {
        LaunchSession launchSession = mediaLaunchObject.launchSession;
        this.mMediaControl = mediaLaunchObject.mediaControl;
        stopUpdating();
        enableMedia();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopUpdating() {
        Timer timer = this.refreshTimer;
        if (timer == null) {
            return;
        }
        timer.cancel();
        this.refreshTimer = null;
    }

    private void enableMedia() {
        try {
            if (TVConnectUtils.getInstance().getConnectableDevice() != null && TVConnectUtils.getInstance().getConnectableDevice().hasCapability(MediaControl.PlayState_Subscribe) && !this.isPlaying) {
                this.mMediaControl.subscribePlayState(this.playStateListener);
                return;
            }
            if (this.mMediaControl != null) {
                this.mMediaControl.getDuration(this.durationListener);
            }
            startUpdating();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity$38  reason: invalid class name */
    /* loaded from: classes4.dex */
    public static class AnonymousClass38 {
        static final int[] $SwitchMap$com$connectsdk$service$capability$MediaControl$PlayStateStatus;

        static {
            int[] iArr = new int[MediaControl.PlayStateStatus.values().length];
            $SwitchMap$com$connectsdk$service$capability$MediaControl$PlayStateStatus = iArr;
            try {
                iArr[MediaControl.PlayStateStatus.Playing.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$connectsdk$service$capability$MediaControl$PlayStateStatus[MediaControl.PlayStateStatus.Finished.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startUpdating() {
        try {
            Timer timer = this.refreshTimer;
            if (timer != null) {
                timer.cancel();
                this.refreshTimer = null;
            }
            Timer timer2 = new Timer();
            this.refreshTimer = timer2;
            timer2.schedule(new TimerTask() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.28
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    if (mMediaControl != null && TVConnectUtils.getInstance().getConnectableDevice() != null && TVConnectUtils.getInstance().getConnectableDevice().hasCapability(MediaControl.Position)) {
                        mMediaControl.getPosition(positionListener);
                    }
                    if (mMediaControl == null || TVConnectUtils.getInstance().getConnectableDevice() == null || !TVConnectUtils.getInstance().getConnectableDevice().hasCapability(MediaControl.Duration) || totalTimeDuration > 0) {
                        return;
                    }
                    mMediaControl.getDuration(durationListener);
                }
            }, 0L, this.REFRESH_INTERVAL_MS);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private int nextFreePort(int i, int i2) {
        Random random = new Random();
        int nextInt = random.nextInt(i2);
        while (true) {
            int i3 = nextInt + i;
            if (isLocalPortFree(i3)) {
                return i3;
            }
            nextInt = random.nextInt(i2);
        }
    }

    private boolean isLocalPortFree(int i) {
        try {
            new ServerSocket(i).close();
            return true;
        } catch (IOException unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void castPhoto(String str) {
        try {
            Tracking.trackCast(this, "start_cast", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_photo_off");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.prLoading.setVisibility(0);
        this.port = nextFreePort(8000, 999);
        try {
            Glide.with((FragmentActivity) this).asBitmap().load(str).centerCrop().into(this.imvPhoto);
        } catch (Exception unused) {
        }
        if (this.listMedia.size() < 1) {
            return;
        }
        try {
            ServeHTTPD2 serveHTTPD2 = this.serveHTTPD;
            if (serveHTTPD2 != null) {
                serveHTTPD2.stop();
            }
            ServeHTTPD2 serveHTTPD22 = new ServeHTTPD2(this.port);
            this.serveHTTPD = serveHTTPD22;
            serveHTTPD22.start();
            String formatIpAddress = Formatter.formatIpAddress(((WifiManager) getApplicationContext().getSystemService("wifi")).getConnectionInfo().getIpAddress());
            if (!str.contains("http")) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(new File(str).getAbsolutePath(), options);
                int i = options.outHeight;
                int i2 = options.outWidth;
                str = "http://" + formatIpAddress + ":" + this.port + this.listMedia.get(this.currentPos).getPhotoUri();
                if (TVConnectUtils.getInstance().isConnectWeb) {
                    try {
                        BrowserAction.displayImage(str, String.valueOf(i), String.valueOf(i2), "0");
                        runOnUiThread(new Runnable() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.30
                            @Override // java.lang.Runnable
                            public void run() {
                                prLoading.setVisibility(8);
                            }
                        });
                        return;
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        return;
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append(this.listMedia.size());
            sb.append("");
            StringBuilder sb2 = new StringBuilder();
            sb2.append(this.currentPos);
            sb2.append("");
        } catch (IOException e4) {
            e4.printStackTrace();
        }
        if (TVConnectUtils.getInstance().getConnectableDevice() == null) {
            return;
        }
        try {
            ((MediaPlayer) TVConnectUtils.getInstance().getConnectableDevice().getCapability(MediaPlayer.class)).displayImage(new MediaInfo.Builder(str, "image/jpeg").setTitle(getString(R.string.cast_photo)).setDescription("" + str).build(), new MediaPlayer.LaunchListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.31
                @Override // com.connectsdk.service.capability.listeners.ErrorListener
                public void onError(ServiceCommandError serviceCommandError) {
                    serviceCommandError.toString();
                    prLoading.setVisibility(8);
                    try {
                        Tracking.trackCast(PlayCastActivity.this, "cast_fail", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_photo_off");
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                }

                @Override // com.connectsdk.service.capability.listeners.ResponseListener
                public void onSuccess(MediaPlayer.MediaLaunchObject mediaLaunchObject) {
                    mediaLaunchObject.toString();
                    prLoading.setVisibility(8);
                    totalCassSS();
                    try {
                        Tracking.trackCast(PlayCastActivity.this, "cast_success", TVConnectUtils.getInstance().getConnectableDevice().getId(), TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()), "cast_photo_off");
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                }
            });
        } catch (Exception e5) {
            e5.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void totalCassSS() {
        int i = this.numberCastSS + 1;
        this.numberCastSS = i;
        if (i < 3 || i == this.numberShowAds || this.closeRate) {
            return;
        }
        showRate();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imvNextPhoto /* 2131362392 */:
                try {
                    this.currentPos++;
                    int i = this.typeCast;
                    if (i == 0) {
                        List<MediaModel> list = this.listMedia;
                        if (list != null && list.size() > 0) {
                            if (this.currentPos >= this.listMedia.size()) {
                                this.currentPos = 0;
                            }
                            castPhoto(this.listMedia.get(this.currentPos).getPhotoUri());
                        }
                    } else if (i == 4) {
                        List<PhotoOnlineModel> list2 = this.listPhotoOnline;
                        if (list2 != null && list2.size() > 0) {
                            if (this.currentPos >= this.listPhotoOnline.size()) {
                                this.currentPos = 0;
                            }
                            castPhotoOnline(this.listPhotoOnline.get(this.currentPos).getImageURL());
                        }
                    } else {
                        List<GoogleDriveItem> list3 = this.listDrive;
                        if (list3 != null && list3.size() > 0) {
                            if (this.currentPos >= this.listDrive.size()) {
                                this.currentPos = 0;
                            }
                            castPhotoOnline(this.listDrive.get(this.currentPos).getThumbnailLink());
                        }
                    }
                    this.vpImage.setCurrentItem(this.currentPos, true);
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            case R.id.imvNextVideo5s /* 2131362393 */:
                try {
                    int i2 = this.currentPos + 1;
                    this.currentPos = i2;
                    int i3 = this.typeCast;
                    if (i3 == 2) {
                        if (i2 >= this.listAudio.size()) {
                            this.currentPos = 0;
                        }
                        castAudio();
                        return;
                    } else if (i3 == 1) {
                        if (i2 >= this.listMedia.size()) {
                            this.currentPos = 0;
                        }
                        castVideo();
                        return;
                    } else if (i3 != 3) {
                        return;
                    } else {
                        Toast.makeText(this, "Not support", 0).show();
                        return;
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                    return;
                }
            case R.id.imvPlayVideo /* 2131362397 */:
                AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                    if (TVConnectUtils.getInstance().isConnectWeb) {
                        int i4 = this.typeCast;
                        if (i4 == 2 || i4 == 10) {
                            if (this.isPlaying) {
                                this.isPlaying = false;
                                this.imvPlayVideo.setImageResource(R.drawable.ic_play);
                                BrowserAction.pauseAudio();
                                return;
                            }
                            this.imvPlayVideo.setImageResource(R.drawable.ic_pause);
                            this.isPlaying = true;
                            BrowserAction.playingAudio();
                            return;
                        } else if (this.isPlaying) {
                            this.isPlaying = false;
                            this.imvPlayVideo.setImageResource(R.drawable.ic_play);
                            BrowserAction.pause();
                            return;
                        } else {
                            this.imvPlayVideo.setImageResource(R.drawable.ic_pause);
                            this.isPlaying = true;
                            BrowserAction.play();
                            return;
                        }
                    } else if (this.isPlaying) {
                        try {
                            this.mMediaControl.pause(null);
                            this.isPlaying = false;
                            this.imvPlayVideo.setImageResource(R.drawable.ic_play);
                            return;
                        } catch (Exception e4) {
                            e4.printStackTrace();
                            return;
                        }
                    } else {
                        try {
                            this.mMediaControl.play(null);
                            this.imvPlayVideo.setImageResource(R.drawable.ic_pause);
                            this.isPlaying = true;
                            return;
                        } catch (Exception e5) {
                            e5.printStackTrace();
                            return;
                        }
                    }
                });
            case R.id.imvPreviousPhoto /* 2131362398 */:
                try {
                    this.currentPos--;
                    int i5 = this.typeCast;
                    if (i5 == 0) {
                        List<MediaModel> list4 = this.listMedia;
                        if (list4 != null && list4.size() > 0) {
                            if (this.currentPos < 0) {
                                this.currentPos = this.listMedia.size() - 1;
                            }
                            castPhoto(this.listMedia.get(this.currentPos).getPhotoUri());
                        }
                    } else if (i5 == 4) {
                        List<PhotoOnlineModel> list5 = this.listPhotoOnline;
                        if (list5 != null && list5.size() > 0) {
                            if (this.currentPos < 0) {
                                this.currentPos = this.listPhotoOnline.size() - 1;
                            }
                            castPhotoOnline(this.listPhotoOnline.get(this.currentPos).getImageURL());
                        }
                    } else {
                        List<GoogleDriveItem> list6 = this.listDrive;
                        if (list6 != null && list6.size() > 0) {
                            if (this.currentPos < 0) {
                                this.currentPos = this.listDrive.size() - 1;
                            }
                            castPhotoOnline(this.listDrive.get(this.currentPos).getThumbnailLink());
                        }
                    }
                    this.vpImage.setCurrentItem(this.currentPos, true);
                    return;
                } catch (Exception e6) {
                    e6.printStackTrace();
                    return;
                }
            case R.id.imvPreviousVideo5s /* 2131362399 */:
                try {
                    int i6 = this.currentPos - 1;
                    this.currentPos = i6;
                    int i7 = this.typeCast;
                    if (i7 == 2) {
                        if (i6 < 0) {
                            this.currentPos = this.listAudio.size() - 1;
                        }
                        castAudio();
                        return;
                    } else if (i7 == 1) {
                        if (i6 < 0) {
                            this.currentPos = this.listAudio.size() - 1;
                        }
                        castVideo();
                        return;
                    } else if (i7 != 3) {
                        return;
                    } else {
                        Toast.makeText(this, "Not support", 0).show();
                        return;
                    }
                } catch (Exception e7) {
                    e7.printStackTrace();
                    return;
                }
            case R.id.imvRemote /* 2131362400 */:
                gotoActivity(RemoteActivity.class);
                return;
            case R.id.llBack /* 2131362499 */:
                onBackPressed();
                return;
            case R.id.ll_mute /* 2131362564 */:
                setVolumeMute();
                return;
            case R.id.ll_show_list /* 2131362612 */:
                int i8 = this.typeCast;
                if (i8 == 1) {
                    showButtonSheet();
                    return;
                } else if (i8 != 2) {
                    return;
                } else {
                    showButtonSheetAudio();
                    return;
                }
            case R.id.ll_stopVideo /* 2131362613 */:
                if (TVConnectUtils.getInstance().isConnectWeb) {
                    if (this.typeCast == 2) {
                        BrowserAction.stopAudio();
                    } else {
                        BrowserAction.stop();
                    }
                }
                onBackPressed();
                return;
            case R.id.rlt_volume_down_media_player /* 2131362956 */:
                if (TVConnectUtils.getInstance().isConnectWeb) {
                    if (this.typeCast == 2) {
                        BrowserAction.volumeDownAudio(10);
                        return;
                    }
                    BrowserAction.volumeDown(10);
                    return;
                } else if (!TVConnectUtils.getInstance().isConnected()) {
                    return;
                } else {
                    setupVolume(false);
                    return;
                }
            case R.id.rlt_volume_up_media_player /* 2131362957 */:
                if (TVConnectUtils.getInstance().isConnectWeb) {
                    if (this.typeCast == 2) {
                        BrowserAction.volumeUpAudio(10);
                        return;
                    }
                    BrowserAction.volumeUp(10);
                    return;
                } else if (!TVConnectUtils.getInstance().isConnected()) {
                    return;
                } else {
                    setupVolume(true);
                    return;
                }
            default:
                return;
        }
    }

    public void setVolumeMute() {
        try {
            boolean z = true;
            if (TVConnectUtils.getInstance().isConnectWeb) {
                if (this.isMute) {
                    if (this.typeCast == 2) {
                        BrowserAction.unMuteAudio();
                    } else {
                        BrowserAction.unMute();
                    }
                } else if (this.typeCast == 2) {
                    BrowserAction.muteAudio();
                } else {
                    BrowserAction.mute();
                }
                this.isMute = !this.isMute;
            }
            if (TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()).equalsIgnoreCase("RokuTV")) {
                this.isMute = !this.isMute;
                performKeypress(KeypressKeyValues.VOLUME_MUTE);
            }
            VolumeControl volumeControl = (VolumeControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(VolumeControl.class);
            if (volumeControl == null) {
                return;
            }
            if (this.isMute) {
                z = false;
            }
            this.isMute = z;
            volumeControl.setMute(z, null);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void getListVideo() {
        DragVideoAdapter dragVideoAdapter = this.allVideoAdapter;
        if (dragVideoAdapter == null || dragVideoAdapter.getData() == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" size :");
        sb.append(this.allVideoAdapter.getData().size());
        ArrayList arrayList = new ArrayList();
        this.listMedia = arrayList;
        arrayList.addAll(this.allVideoAdapter.getData());
    }

    private void getListAudio() {
        DragAudioAdapter dragAudioAdapter = this.audioAdapter;
        if (dragAudioAdapter == null || dragAudioAdapter.getData() == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" size :");
        sb.append(this.audioAdapter.getData().size());
        ArrayList arrayList = new ArrayList();
        this.listAudio = arrayList;
        arrayList.addAll(this.audioAdapter.getData());
    }

    public void showButtonSheet() {
        getListVideo();
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);
        DragRecyclerView dragRecyclerView = (DragRecyclerView) bottomSheetDialog.findViewById(R.id.rcv_quickly_list);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.32
            @SuppressLint("ResourceType")
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                ((FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet)).setBackgroundResource(17170445);
            }
        });
        this.allVideoAdapter = new DragVideoAdapter(this, (ArrayList) this.listMedia, this.titleCurrent);
        dragRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dragRecyclerView.setAdapter(this.allVideoAdapter);
        try {
            List<MediaModel> list = this.listMedia;
            if (list != null) {
                int size = list.size();
                int i = this.currentPos;
                if (size > i) {
                    this.titleCurrent = this.listMedia.get(i).getTitle();
                    dragRecyclerView.scrollToPosition(this.currentPos);
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.allVideoAdapter.setHandleDragEnabled(true);
        this.allVideoAdapter.setLongPressDragEnabled(true);
        this.allVideoAdapter.setSwipeEnabled(true);
        this.allVideoAdapter.setListener(new DragVideoAdapter.IClickItem() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.33
            @Override
            // com.magicapps.casttotv.tv.screen.tab.playcast.DragVideoAdapter.IClickItem
            public void onClickItem(MediaModel mediaModel, int i2) {
                try {
                    currentPos = i2;
                    castVideo();
                    bottomSheetDialog.dismiss();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }

            @Override
            // com.magicapps.casttotv.tv.screen.tab.playcast.DragVideoAdapter.IClickItem
            public void onDeleteItem(MediaModel mediaModel, int i2) {
                try {
                    if (listMedia.size() <= i2) {
                        return;
                    }
                    allVideoAdapter.onSwiped(i2);
                    allVideoAdapter.notifyItemRemoved(i2);
                    getCurrentPos();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        });
        this.allVideoAdapter.setOnItemDragListener(new SimpleDragListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.34
            @Override
            // com.wonshinhyo.dragrecyclerview.SimpleDragListener, com.wonshinhyo.dragrecyclerview.OnDragListener
            public void onDrop(int i2, int i3) {
                super.onDrop(i2, i3);
                StringBuilder sb = new StringBuilder();
                sb.append("onDrop ");
                sb.append(i2);
                sb.append(" -> ");
                sb.append(i3);
                getCurrentPos();
            }

            @Override
            // com.wonshinhyo.dragrecyclerview.SimpleDragListener, com.wonshinhyo.dragrecyclerview.OnDragListener
            public void onSwiped(int i2) {
                super.onSwiped(i2);
                StringBuilder sb = new StringBuilder();
                sb.append("onSwiped ");
                sb.append(i2);
                allVideoAdapter.notifyDataSetChanged();
                getCurrentPos();
            }
        });
        bottomSheetDialog.show();
    }

    public void showButtonSheetAudio() {
        getListAudio();
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);
        DragRecyclerView dragRecyclerView = (DragRecyclerView) bottomSheetDialog.findViewById(R.id.rcv_quickly_list);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.35
            @SuppressLint("ResourceType")
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                ((FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet)).setBackgroundResource(17170445);
            }
        });
        String songTitle = this.listAudio.get(this.currentPos).getSongTitle();
        this.titleCurrent = songTitle;
        this.audioAdapter = new DragAudioAdapter(this, (ArrayList) this.listAudio, songTitle);
        dragRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dragRecyclerView.setAdapter(this.audioAdapter);
        List<AudioModel> list = this.listAudio;
        if (list != null) {
            int size = list.size();
            int i = this.currentPos;
            if (size > i) {
                dragRecyclerView.scrollToPosition(i);
            }
        }
        this.audioAdapter.setHandleDragEnabled(true);
        this.audioAdapter.setLongPressDragEnabled(true);
        this.audioAdapter.setSwipeEnabled(true);
        this.audioAdapter.setListener(new DragAudioAdapter.IClickItem() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.36
            @Override
            // com.magicapps.casttotv.tv.screen.tab.playcast.DragAudioAdapter.IClickItem
            public void onClickItem(AudioModel audioModel, int i2) {
                try {
                    currentPos = i2;
                    castAudio();
                    bottomSheetDialog.dismiss();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

            @Override
            // com.magicapps.casttotv.tv.screen.tab.playcast.DragAudioAdapter.IClickItem
            public void onDeleteItem(AudioModel audioModel, int i2) {
                try {
                    if (listAudio.size() <= i2) {
                        return;
                    }
                    audioAdapter.onSwiped(i2);
                    audioAdapter.notifyItemRemoved(i2);
                    getCurrentPosAudio();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        this.audioAdapter.setOnItemDragListener(new SimpleDragListener() { // from class: com.magicapps.casttotv.tv.screen.tab.playcast.PlayCastActivity.37
            @Override
            // com.wonshinhyo.dragrecyclerview.SimpleDragListener, com.wonshinhyo.dragrecyclerview.OnDragListener
            public void onDrop(int i2, int i3) {
                super.onDrop(i2, i3);
                StringBuilder sb = new StringBuilder();
                sb.append("onDrop ");
                sb.append(i2);
                sb.append(" -> ");
                sb.append(i3);
                getCurrentPosAudio();
            }

            @Override
            // com.wonshinhyo.dragrecyclerview.SimpleDragListener, com.wonshinhyo.dragrecyclerview.OnDragListener
            public void onSwiped(int i2) {
                super.onSwiped(i2);
                StringBuilder sb = new StringBuilder();
                sb.append("onSwiped ");
                sb.append(i2);
                audioAdapter.notifyDataSetChanged();
                getCurrentPosAudio();
            }
        });
        bottomSheetDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getCurrentPosAudio() {
        getListVideo();
        for (int i = 0; i < this.listAudio.size(); i++) {
            if (this.listAudio.get(i).getSongTitle().equalsIgnoreCase(this.titleCurrent)) {
                this.currentPos = i;
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getCurrentPos() {
        try {
            getListVideo();
            for (int i = 0; i < this.listMedia.size(); i++) {
                if (this.listMedia.get(i).getTitle().equalsIgnoreCase(this.titleCurrent)) {
                    this.currentPos = i;
                    return;
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void setupVolume(boolean z) {
        float max;
        try {
            if (TVType.getTVType(TVConnectUtils.getInstance().getConnectableDevice()).equalsIgnoreCase("RokuTV")) {
                if (z) {
                    performKeypress(KeypressKeyValues.VOLUME_UP);
                    return;
                } else {
                    performKeypress(KeypressKeyValues.VOLUME_DOWN);
                    return;
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        VolumeControl volumeControl = (VolumeControl) TVConnectUtils.getInstance().getConnectableDevice().getCapability(VolumeControl.class);
        if (volumeControl != null) {
            if (z) {
                max = Math.min(TVConnectUtils.getInstance().volume + 0.01f, 1.0f);
            } else {
                max = Math.max(TVConnectUtils.getInstance().volume - 0.01f, 0.0f);
            }
            TVConnectUtils.getInstance().volume = max;
            volumeControl.setVolume(TVConnectUtils.getInstance().volume, null);
        }
    }

    private void performKeypress(KeypressKeyValues keypressKeyValues) {
        if (TVConnectUtils.getInstance().isConnected()) {
            String deviceURL = CommandHelper.getDeviceURL(this);
            StringBuilder sb = new StringBuilder();
            sb.append("performKeypress: ");
            sb.append(deviceURL);
            performRequest(new JakuRequest(new KeypressRequest(deviceURL, keypressKeyValues.getValue()), null), RokuRequestTypes.keypress);
        }
    }

    private void performRequest(JakuRequest jakuRequest, RokuRequestTypes rokuRequestTypes) {
        if (getApplicationContext() != null) {
            Observable.fromCallable(new RxRequestTask(getApplicationContext(), jakuRequest, rokuRequestTypes)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    PlayCastActivity.lambda$performRequest$0(o);
                }
            });
        }
    }
}
