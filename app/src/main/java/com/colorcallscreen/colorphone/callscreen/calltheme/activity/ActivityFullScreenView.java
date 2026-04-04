package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ads.control.ads.AperoAd;
import com.ads.control.ads.AperoAdCallback;
import com.ads.control.ads.wrapper.ApAdError;
import com.ads.control.ads.wrapper.ApInterstitialAd;
import com.ads.control.config.AperoAdConfig;
import com.colorcallscreen.colorphone.callscreen.calltheme.Splash.StartActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.custom.GifWallpaper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.DownloadCompleteReceiver;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.SwipeUpHelper;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.custom.VideoWallpaper;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ThemeModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityFullScreenView extends BaseActivity implements DownloadCompleteReceiver.DownloadListener, MediaPlayer.OnErrorListener {
    public static final String THEMES_PREF_CONTACTs = "_person_contact_list";
    String APPLIED;
    String APPLY;
    private Button button;
    private ImageView chooseContact;
    private long downloadId;
    private String downloadedTheme;
    private GifWallpaper gifWallpaper;
    ImageView iVCallAccept;
    ImageView iVCallDecline;
    private ThemeModel model;
    private ProgressBar progressBar;
    private List<String> selectedNames;
    private List<String> selectedNumbers;
    private int stopPosition;
    private VideoWallpaper videoWallpaper;
    private boolean isDownloaded = false;
    private boolean isSelectedForSomeone = false;
    private int showIfDownlaodedCalledTimes = 0;

    private String idInter = "";
    private ApInterstitialAd mInterstitialAd;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityFullScreenView.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            ActivityFullScreenView.this.onDownloadComplete(intent);
        }
    };

    private boolean isThemeAppliedForContact() {
        return false;
    }

    public void applyTheme() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Helper.enableRandomTheme(false, false);
                if (ActivityFullScreenView.this.model.getSource().equals("offline")) {
                    if (ActivityFullScreenView.this.isSelectedForSomeone) {
                        ActivityFullScreenView.this.model.setAppliedTheme(ActivityFullScreenView.this.model.getThemeImage());
                        ActivityFullScreenView activityFullScreenView = ActivityFullScreenView.this;
                        activityFullScreenView.setThemeForPerson(activityFullScreenView.model);
                    } else {
                        ActivityFullScreenView.setTheme(ActivityFullScreenView.this.model.getThemeImage(), null, ActivityFullScreenView.this.model);
                    }
                } else if (ActivityFullScreenView.this.isSelectedForSomeone) {
                    ActivityFullScreenView.this.model.setAppliedTheme(ActivityFullScreenView.this.downloadedTheme);
                    ActivityFullScreenView activityFullScreenView2 = ActivityFullScreenView.this;
                    activityFullScreenView2.setThemeForPerson(activityFullScreenView2.model);
                } else {
                    ActivityFullScreenView.setTheme(ActivityFullScreenView.this.downloadedTheme, null, ActivityFullScreenView.this.model);
                }
                if (!ActivityFullScreenView.this.isSelectedForSomeone) {
                    ActivityFullScreenView.setAppliedThemeName(ActivityFullScreenView.this.model.getName());
                    ActivityFullScreenView.setAppliedThemeSource(ActivityFullScreenView.this.model.getSource());
                    ActivityFullScreenView.setThemeModel(ActivityFullScreenView.this.model);
                }
                if (ActivityFullScreenView.this.isSelectedForSomeone) {
                    return;
                }
                ActivityFullScreenView.this.setButtonText(ActivityFullScreenView.this.APPLIED);
            }
        });
    }

    public void buttonEvent() {
        if (isThemeApplied()) {
            Toast.makeText(this, (int) R.string.already_applied, Toast.LENGTH_SHORT).show();
        } else {
            applyTheme();
        }
    }

    public void downloadTheme() {
        Utility.logEventNew(this.model.getGaCategory(), this.model.getName() + "_download_started");
        if (!Utility.isInternetEnabled(this)) {
            Toast.makeText(this, (int) R.string.make_sure_internet, Toast.LENGTH_SHORT).show();
            Utility.logEventNew(this.model.getGaCategory(), "off_" + this.model.getName() + "_download_failed");
        }
        if (this.model.getContentType().equals("video")) {
            this.downloadId = this.videoWallpaper.download(ThemeModel.BASE_IMAGE + this.model.getThemeImage());
        } else {
            this.downloadId = this.gifWallpaper.download(ThemeModel.BASE_IMAGE + this.model.getThemeImage());
        }
    }

    public static String getAppliedTheme() {
        return PreferenceUtils.getInstance().getString(Constants.THEME_KEY);
    }

    private String getIfThemeDownloaded() {
        for (String str : Helper.getDownloadedTheme()) {
            ThemeModel themeModel = this.model;
            if (themeModel != null && str.contains(themeModel.getThemeImage())) {
                this.isDownloaded = true;
                setButtonText(this.APPLY);
                return str;
            }
        }
        return null;
    }

    public static String getNumberPrefList() {
        return PreferenceUtils.getInstance().getString(THEMES_PREF_CONTACTs);
    }

    public static ThemeModel getThemeModel() {
        return PreferenceUtils.getInstance().getThemeModel();
    }

    public static String getThemeNameForPerson(String str) {
        return PreferenceUtils.getInstance().getString(str);
    }

    public void hideButton() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.button.setVisibility(View.GONE);
        this.chooseContact.setVisibility(View.GONE);
    }

    private boolean isThemeApplied() {
        if (this.model.getContentType().equals("video")) {
            if (this.videoWallpaper.isApplied()) {
                setButtonText(this.APPLIED);
                return true;
            }
        } else if (this.gifWallpaper.isApplied()) {
            setButtonText(this.APPLIED);
            return true;
        }
        setButtonText(this.APPLY);
        return false;
    }

    private boolean isThemeUnlocked(String str, String str2) {
        return str.contains(str2);
    }

    public void openContact() {
        Intent intent = new Intent(this, ActivityThemeContactList.class);
        intent.putExtra("_tn", this.model.getName());
        startActivityForResult(intent, 49);
        Utility.logEventNew(this.model.getGaCategory(), this.model.getName() + "_open_contact_tap");
    }

    private void saveNumberPrefList(String str) {
        if (getNumberPrefList() == null) {
            PreferenceUtils.getInstance().putPreference(THEMES_PREF_CONTACTs, str);
        } else {
            String string = PreferenceUtils.getInstance().getString(THEMES_PREF_CONTACTs);
            StringBuilder sb = new StringBuilder();
            sb.append(string + ",");
            sb.append(str);
            PreferenceUtils.getInstance().putPreference(THEMES_PREF_CONTACTs, sb.toString());
        }
        showButton();
    }

    public static void setAppliedThemeName(String str) {
        PreferenceUtils.getInstance().putPreference(Constants.APPLIED_THEME_NAME, str);
    }

    public static void setAppliedThemeSource(String str) {
        PreferenceUtils.getInstance().putPreference(Constants.APPLIED_THEME_TYPE, str);
    }

    public void setButtonText(String str) {
        this.button.setText(str);
    }

    public static void setTheme(String str, String str2, ThemeModel themeModel) {
        Log.d("SetTheme===", themeModel.getGaCategory() + themeModel.getName() + "_applied_all");
        if (str2 == null) {
            Helper.applyTheme(str);
        } else {
            PreferenceUtils.getInstance().putPreference(str2, str);
        }
    }

    public void setThemeForPerson(ThemeModel themeModel) {
        for (String str : this.selectedNumbers) {
            String updatePhoneNumberWithISDCode = Utility.updatePhoneNumberWithISDCode(this, str);
            setThemeModel(themeModel, updatePhoneNumberWithISDCode);
            saveNumberPrefList(updatePhoneNumberWithISDCode);
        }
        StringBuilder sb = new StringBuilder();
        for (String str2 : this.selectedNames) {
            Log.d("_TEST", "setThemeForPerson: " + str2 + "::" + themeModel.getName());
            PreferenceUtils.getInstance().putPreference(str2, themeModel.getName());
            sb.append(str2 + ",");
        }
        Utility.logEventNew(themeModel.getGaCategory(), themeModel.getName() + "_applied_single");
    }

    public static void setThemeModel(ThemeModel themeModel) {
        PreferenceUtils.getInstance().setThemeModel(themeModel);
    }

    private void showButton() {
        this.progressBar.setVisibility(View.GONE);
        this.button.setVisibility(View.VISIBLE);
        this.chooseContact.setVisibility(View.GONE);
    }

    private void showIfThemeDownloaded() {
        this.showIfDownlaodedCalledTimes++;
        String ifThemeDownloaded = getIfThemeDownloaded();
        this.downloadedTheme = ifThemeDownloaded;
        if (this.isDownloaded && ifThemeDownloaded != null) {
            Utility.logEventNew(this.model.getGaCategory(), this.model.getName() + "_found_local");
            if (this.gifWallpaper == null || this.videoWallpaper == null) {
                this.gifWallpaper = (GifWallpaper) findViewById(R.id.gifImageView);
                this.videoWallpaper = (VideoWallpaper) findViewById(R.id.live_wallpaper);
            }
            if (this.model.getContentType().equals("video")) {
                this.videoWallpaper.setVisibility(View.VISIBLE);
                this.videoWallpaper.setOnErrorOnPlaying(new VideoWallpaper.OnErrorOnPlaying() {
                    @Override
                    public void onErrorOnPlaying(MediaPlayer mediaPlayer) {
                        try {
                            File file = new File(ActivityFullScreenView.this.downloadedTheme);
                            if (file.exists()) {
                                file.delete();
                            }
                        } catch (Exception unused) {
                        }
                        if (ActivityFullScreenView.this.showIfDownlaodedCalledTimes < 3) {
                            ActivityFullScreenView.this.downloadTheme();
                        }
                        ActivityFullScreenView.this.hideButton();
                    }
                });
                this.videoWallpaper.setWallpaper(this.downloadedTheme);
            } else if (this.model.getContentType().equals("gif")) {
                this.gifWallpaper.setGifWallpaper(this.downloadedTheme);
            } else {
                this.gifWallpaper.setWallpaper(this.downloadedTheme);
            }
            showButton();
        } else {
            downloadTheme();
        }
        isThemeApplied();
        isThemeAppliedForContact();
    }

    private void unRegisterBroadcast() {
        try {
            if (this.broadcastReceiver != null) {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(this.broadcastReceiver);
                this.broadcastReceiver = null;
            }
        } catch (Exception unused) {
        }
    }

    public void downloadComplete() {
        showButton();
        setButtonText(this.APPLY);
        showIfThemeDownloaded();
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 49 || intent == null) {
            return;
        }
        this.selectedNumbers = intent.getStringArrayListExtra(ActivityThemeContactList.CONTACTS);
        this.selectedNames = intent.getStringArrayListExtra("_cm");
        this.isSelectedForSomeone = true;
        buttonEvent();
    }

    @Override
    public void onBackPressed() {
        this.downloadId = -1L;
        unRegisterBroadcast();
        if (mInterstitialAd.isReady()) {

            AperoAd.getInstance().showInterstitialAdByTimes(ActivityFullScreenView.this, mInterstitialAd, new AperoAdCallback() {
                @Override
                public void onNextAction() {
                    finish();
                }

                @Override
                public void onAdFailedToShow(@Nullable ApAdError adError) {
                    super.onAdFailedToShow(adError);
                }

                @Override
                public void onInterstitialShow() {
                    super.onInterstitialShow();
                }
            }, true);
        } else {
            finish();
        }

    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_full_screen_view);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        configMediationProvider();
        loadAdInterstitial();
        this.model = (ThemeModel) getIntent().getSerializableExtra("model");
        this.APPLIED = getResources().getString(R.string.applied);
        this.APPLY = getResources().getString(R.string.set_for_all);
        try {
            if (this.broadcastReceiver != null) {
                LocalBroadcastManager.getInstance(this).registerReceiver(this.broadcastReceiver, new IntentFilter(DownloadCompleteReceiver.LOCAL_DOWNLOAD_ACTION));
            }
        } catch (Exception unused) {
        }
        this.iVCallDecline = (ImageView) findViewById(R.id.iVCallDecline);
        this.iVCallAccept = (ImageView) findViewById(R.id.iVCallAccept);
        new SwipeUpHelper(this, this.iVCallDecline).start(new SwipeUpHelper.SwipeCompeleteListner() {
            @Override
            public void onSwipeUpComplete() {
                Utility.vibrate(ActivityFullScreenView.this);
            }
        });
        this.iVCallDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.vibrate(ActivityFullScreenView.this);
            }
        });
        new SwipeUpHelper(this, this.iVCallAccept).start(new SwipeUpHelper.SwipeCompeleteListner() {
            @Override
            public void onSwipeUpComplete() {
                Utility.vibrate(ActivityFullScreenView.this);
            }
        });
        this.iVCallAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.vibrate(ActivityFullScreenView.this);
            }
        });
        this.progressBar = (ProgressBar) findViewById(R.id.custom_loader);
        this.gifWallpaper = (GifWallpaper) findViewById(R.id.gifImageView);
        VideoWallpaper videoWallpaper = (VideoWallpaper) findViewById(R.id.live_wallpaper);
        this.videoWallpaper = videoWallpaper;
        videoWallpaper.setOnErrorListener(this);
        ImageView button = (ImageView) findViewById(R.id.choose);
        this.chooseContact = button;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityFullScreenView.this.openContact();
            }
        });
        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
        circleImageView.setVisibility(View.VISIBLE);
        int resourceByName = Utility.getResourceByName(this, this.model.getPersonImage(), "raw");
        if (resourceByName != -1) {
            Picasso.get().load(resourceByName).into(circleImageView);
        }
        TextView textView = (TextView) findViewById(R.id.tvUserName);
        textView.setText(this.model.getPersonName());
        TextView textView2 = (TextView) findViewById(R.id.tcvUserNo);
        textView2.setText(this.model.getPersonPhoneNumber());
        String color = this.model.getColor();
        if (color != null && Helper.isColorCodeCorrect(color)) {
            textView.setTextColor(Color.parseColor("#" + color));
            textView2.setTextColor(Color.parseColor("#" + color));
        }
        Button button2 = (Button) findViewById(R.id.btnApplied);
        this.button = button2;
        button2.setTypeface(Utility.getMediumAppFont(this));
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isReady()) {

                    AperoAd.getInstance().showInterstitialAdByTimes(ActivityFullScreenView.this, mInterstitialAd, new AperoAdCallback() {
                        @Override
                        public void onNextAction() {
                            ActivityFullScreenView.this.buttonEvent();
                            finish();
                        }

                        @Override
                        public void onAdFailedToShow(@Nullable ApAdError adError) {
                            super.onAdFailedToShow(adError);
                        }

                        @Override
                        public void onInterstitialShow() {
                            super.onInterstitialShow();
                        }
                    }, true);
                } else {
                    ActivityFullScreenView.this.buttonEvent();
                    finish();
                }


            }
        });
        if (!this.model.getContentType().equals("image") && !this.model.getContentType().equals("gif")) {
            this.videoWallpaper.setName(this.model.getName());
            this.videoWallpaper.setTheme(this.model.getThemeImage());
            if (this.model.getSource().equals("offline")) {
                this.videoWallpaper.setVisibility(View.VISIBLE);
                this.videoWallpaper.setOfflineWallpaper(this.model.getThemeImage());
                isThemeApplied();
                showButton();
            } else {
                this.gifWallpaper.setThumbnail(ThemeModel.BASE_IMAGE + this.model.getThumbnail());
                showIfThemeDownloaded();
            }
        } else {
            this.gifWallpaper.setName(this.model.getName());
            this.gifWallpaper.setTheme(this.model.getThemeImage());
            if (this.model.getSource().equals("offline")) {
                this.gifWallpaper.setOfflineWallpaper(this.model.getThemeImage());
                isThemeApplied();
                showButton();
            } else {
                this.gifWallpaper.setThumbnail(ThemeModel.BASE_IMAGE + this.model.getThumbnail());
                showIfThemeDownloaded();
            }
        }
        isThemeAppliedForContact();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.downloadId = -1L;
        unRegisterBroadcast();
    }

    private void configMediationProvider() {
        if (AperoAd.getInstance().getMediationProvider() == AperoAdConfig.PROVIDER_ADMOB) {
            idInter = getResources().getString(R.string.admob_inter_id);
        }
    }

    private void loadAdInterstitial() {

        mInterstitialAd = AperoAd.getInstance().getInterstitialAds(this, idInter);
    }

    @Override
    public void onDownloadComplete(Intent intent) {
        long longExtra = intent.getLongExtra("extra_download_id", -1L);
        long j = this.downloadId;
        if (longExtra == j && Helper.isValidDownload(this, j)) {
            downloadComplete();
            Utility.logEventNew(this.model.getGaCategory(), this.model.getName() + "_download_success");
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        Toast.makeText(this, (int) R.string.make_sure_internet, Toast.LENGTH_SHORT).show();
        if (this.downloadedTheme != null) {
            new File(this.downloadedTheme).delete();
        }
        this.videoWallpaper.setVisibility(View.GONE);
        this.gifWallpaper.setVisibility(View.VISIBLE);
        this.gifWallpaper.setThumbnail(ThemeModel.BASE_IMAGE + this.model.getThumbnail());
        this.button.setVisibility(View.GONE);
        this.chooseContact.setVisibility(View.GONE);
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        VideoWallpaper videoWallpaper = this.videoWallpaper;
        if (videoWallpaper != null) {
            this.stopPosition = videoWallpaper.getCurrentPosition();
            this.videoWallpaper.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        VideoWallpaper videoWallpaper = this.videoWallpaper;
        if (videoWallpaper != null) {
            videoWallpaper.seekTo(this.stopPosition);
            this.videoWallpaper.start();
        }
    }

    public static ThemeModel getThemeModel(String str) {
        return PreferenceUtils.getInstance().getThemeModel(str);
    }

    public static void setThemeModel(ThemeModel themeModel, String str) {
        PreferenceUtils.getInstance().setThemeModel(themeModel, str);
    }
}
