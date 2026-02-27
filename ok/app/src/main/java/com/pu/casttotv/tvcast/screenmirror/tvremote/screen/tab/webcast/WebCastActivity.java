package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Patterns;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anthonycr.progress.AnimatedProgressBar;
import com.pu.casttotv.tvcast.screenmirror.tvremote.HistoryBrowser;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.DialogExitActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.ConnectActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.DialogDisconnect;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.history.HistoryBrowserActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.history.adapter.ContentHistoryAdapter;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.history.viewModel.HistoryViewModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.ironsource.mediationsdk.utils.IronSourceConstants;
import com.monstertechno.adblocker.AdBlockerWebView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Unit;

@SuppressLint("WrongConstant")
public class WebCastActivity extends BaseActivity implements View.OnClickListener {
    available_files_dialog _available_files_dialog;
    private AdBlocker adBlock;
    ImageView btn_search;
    ImageView btn_search_cancel;
    private ContentHistoryAdapter contentHistoryAdapter;
    CountDownTimer countDownTimer;
    private SSLSocketFactory defaultSSLSF;
    AutoCompleteTextView et_search_bar;
    private ImageView imvDelete;
    private ImageView imvToHistory;
    private ImageView imv_youtubeBack;
    private ImageView imv_youtubeBrowserBack;
    private ImageView imv_youtubeBrowserConnect;
    private ImageView imv_youtubeBrowserForward;
    private ImageView imv_youtubeBrowserHome;
    private ImageView imv_youtubeBrowserList;
    private ImageView imv_youtubeBrowserReload;
    InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        public void onStateUpdate(InstallState installState) {
            try {
                if (installState.installStatus() == 11) {
                    WebCastActivity.this.popupSnackbarForCompleteUpdate();
                } else if (installState.installStatus() != 4) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("InstallStateUpdatedListener: state: ");
                    sb.append(installState.installStatus());
                } else {
                    WebCastActivity webCastActivity = WebCastActivity.this;
                    AppUpdateManager appUpdateManager = webCastActivity.mAppUpdateManager;
                    if (appUpdateManager != null) {
                        appUpdateManager.unregisterListener(webCastActivity.installStateUpdatedListener);
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    };
    Boolean isAllFabsVisible;
    private boolean isRedirected;
    private String linkUrl = "";
    private LinearLayout llBuzzVideo;
    private LinearLayout llContent;
    private LinearLayout llESPN;
    private LinearLayout llFox;
    private LinearLayout llGoogle;
    private LinearLayout llIMDB;
    private LinearLayout llLiveStream;
    private LinearLayout llSounCloud;
    private LinearLayout llTwitch;
    private LinearLayout llVimeo;
    private LinearLayout llYTBGaming;
    private LinearLayout llYaHoo;
    private LinearLayout llYoutube;
    AnimatedProgressBar loadingPageProgress;
    AppUpdateManager mAppUpdateManager;
    Context mContext;
    int numberShow = 0;
    private RecyclerView rcvHistory;
    private LinearLayout rlAudio;
    private RelativeLayout rlData;
    private RelativeLayout rlHistory;
    private LinearLayout rlPhoto;
    private LinearLayout rlVideo;
    WebView simpleWebView;
    Timer timer = null;
    private String title = "";
    private TextView tvSizeAudio;
    private TextView tvSizePhoto;
    private TextView tvSizeVideo;
    private HistoryViewModel viewModel;

    public HistoryViewModel historyViewModel;

    public void wv_go_to_home() {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_web_cast);
        this.mContext = this;
        this.defaultSSLSF = HttpsURLConnection.getDefaultSSLSocketFactory();
        init_components();
        EventBus.getDefault().register(this);
        historyViewModel = (HistoryViewModel) new ViewModelProvider(this).get(HistoryViewModel.class);
        this.viewModel = historyViewModel;
        historyViewModel.getReadAllData().observe(this, new Observer<List<HistoryBrowser>>() {
            public void onChanged(List<HistoryBrowser> list) {
                if (list == null || list.size() <= 0) {
                    WebCastActivity.this.rlHistory.setVisibility(8);
                    return;
                }
                WebCastActivity.this.rlHistory.setVisibility(0);
                ArrayList arrayList = new ArrayList();
                arrayList.add(list.get(list.size() - 1));
                if (list.size() >= 2) {
                    arrayList.add(list.get(list.size() - 2));
                }
                if (WebCastActivity.this.contentHistoryAdapter != null) {
                    WebCastActivity.this.contentHistoryAdapter.setData(arrayList);
                }
            }
        });
        set_button_click_events();
        wv_go_to_home();
        disable_fab_button();
        try {
            onSharedIntent();
        } catch (Exception unused) {
        }
        checkAppUpdate();
        havePermissionForWriteStorage();
        Uri data = getIntent().getData();
        if (data != null) {
            this.et_search_bar.setText(data.toString());
            navigate_browser();
        }
        PrepareForAdBlockers();
    }

    private void onSharedIntent() {
        String stringExtra;
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (action.equals("android.intent.action.SEND") && type.startsWith("text/") && (stringExtra = intent.getStringExtra("android.intent.extra.TEXT")) != null) {
            CheckUrls(stringExtra);
        }
    }

    private void CheckUrls(String str) {
        List<String> extractUrls = Commons.extractUrls(str);
        if (extractUrls.size() == 0) {
            new SweetAlertDialog(this.mContext, 1).setTitleText(this.mContext.getString(R.string.Wait)).setContentText(this.mContext.getString(R.string.NoUrlFound)).show();
            return;
        }
        this.et_search_bar.setText(extractUrls.get(0));
        navigate_browser();
    }

    private void PrepareForAdBlockers() {
        File file = new File(this.mContext.getFilesDir(), "ad_filters.dat");
        try {
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                this.adBlock = (AdBlocker) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
            } else {
                this.adBlock = new AdBlocker();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(this.adBlock);
                objectOutputStream.close();
                fileOutputStream.close();
            }
        } catch (IOException | ClassNotFoundException e2) {
            e2.printStackTrace();
            this.adBlock = new AdBlocker();
        }
        updateAdFilters();
    }

    private boolean havePermissionForWriteStorage() {
        if (Build.VERSION.SDK_INT < 23) {
            initFolers();
            return true;
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE");
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 950);
            return false;
        } else {
            initFolers();
            return true;
        }
    }

    public void updateAdFilters() {
        AsyncTask.execute(new Runnable() {
            public void run() {
                WebCastActivity.this.adBlock.update(WebCastActivity.this.mContext);
            }
        });
    }

    public boolean checkUrlIfAds(String str) {
        AdBlocker adBlocker;
        if (str == null || (adBlocker = this.adBlock) == null) {
            return false;
        }
        return adBlocker.checkThroughFilters(str);
    }

    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback, androidx.fragment.app.FragmentActivity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 950) {
            return;
        }
        if (iArr.length <= 0 || iArr[0] != 0) {
            Toast.makeText(this.mContext, getString(R.string.Permissiondenied), 1).show();
        } else {
            initFolers();
        }
    }

    private void initFolers() {
        try {
            mkdirs(new File(SettingsManager.DOWNLOAD_FOLDER));
            mkdirs(new File(SettingsManager.DOWNLOAD_FOLDER_IMAGES));
            mkdirs(new File(SettingsManager.DOWNLOAD_FOLDER_AUDIO));
            mkdirs(new File(SettingsManager.DOWNLOAD_FOLDER_VIDEO));
        } catch (Exception unused) {
        }
    }

    private void mkdirs(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private void enable_fab_button() {
        this.imv_youtubeBrowserList.setImageResource(R.drawable.ic_yt_list);
    }

    private void enable_audio_fab() {
        enable_fab_button();
    }

    private void enable_video_fab() {
        enable_fab_button();
    }

    private void enable_images_fab() {
        enable_fab_button();
    }

    public void update_audio_fab_text() {
        try {
            if (this.imv_youtubeBrowserList.isEnabled()) {
                YoYo.with(Techniques.Tada).duration(300).repeat(5).playOn(findViewById(R.id.imv_youtubeBrowserList));
            }
            TextView textView = this.tvSizeAudio;
            textView.setText(static_variables.resourse_holder.getAudio_files().size() + "");
        } catch (Exception unused) {
        }
    }

    public void update_image_fab_text() {
        try {
            if (this.imv_youtubeBrowserList.isEnabled()) {
                YoYo.with(Techniques.Tada).duration(300).repeat(5).playOn(findViewById(R.id.imv_youtubeBrowserList));
            }
            TextView textView = this.tvSizePhoto;
            textView.setText(static_variables.resourse_holder.getImage_files().size() + "");
        } catch (Exception unused) {
        }
    }

    private void update_video_fab_text() {
        try {
            if (this.imv_youtubeBrowserList.isEnabled()) {
                YoYo.with(Techniques.Tada).duration(300).repeat(5).playOn(findViewById(R.id.imv_youtubeBrowserList));
            }
            TextView textView = this.tvSizeVideo;
            textView.setText(static_variables.resourse_holder.getVideo_files().size() + "");
        } catch (Exception unused) {
        }
    }

    private void disable_fab_button() {
        if (this.isAllFabsVisible.booleanValue()) {
            toggle_fab_buttons();
        }
        this.imv_youtubeBrowserList.setImageResource(R.drawable.ic_yt_list_empty);
        this.tvSizeAudio.setText("0");
        this.tvSizeVideo.setText("0");
        this.tvSizePhoto.setText("0");
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override // androidx.fragment.app.FragmentActivity, com.thntech.cast68.base.BaseActivity
    public void onResume() {
        super.onResume();
        if (this.imv_youtubeBrowserConnect == null) {
            return;
        }
        if (TVConnectUtils.getInstance().isConnected()) {
            this.imv_youtubeBrowserConnect.setImageResource(R.drawable.hover_screen);
        } else {
            this.imv_youtubeBrowserConnect.setImageResource(R.drawable.screen);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (this.imv_youtubeBrowserConnect == null) {
            return;
        }
        if (TVConnectUtils.getInstance().isConnected()) {
            this.imv_youtubeBrowserConnect.setImageResource(R.drawable.hover_screen);
        } else {
            this.imv_youtubeBrowserConnect.setImageResource(R.drawable.screen);
        }
    }

    private void updateUIBackForward() {
        this.imv_youtubeBrowserBack.setImageResource(this.simpleWebView.canGoBack() ? R.drawable.ic_yt_back : R.drawable.ic_yt_back_black);
        this.imv_youtubeBrowserForward.setImageResource(this.simpleWebView.canGoForward() ? R.drawable.ic_yt_next_black : R.drawable.ic_yt_next);
    }

    private void init_components() {
        this.loadingPageProgress = (AnimatedProgressBar) findViewById(R.id.loadingPageProgress);
        WebView webView = (WebView) findViewById(R.id.simpleWebView);
        this.simpleWebView = webView;
        webView.getSettings().setJavaScriptEnabled(true);
        this.simpleWebView.getSettings().setDomStorageEnabled(true);
        this.simpleWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        this.simpleWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.simpleWebView.setWebViewClient(new customWebClient());
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.et_search_bar);
        this.et_search_bar = autoCompleteTextView;
        autoCompleteTextView.setAdapter(new AutoCompleteAdapter(this.mContext, 17367043));
        new AdBlockerWebView.init(this).initializeWebView(this.simpleWebView);
        this.simpleWebView.setFindListener(new WebView.FindListener() {
            /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass4 */

            public void onFindResultReceived(int i, int i2, boolean z) {
            }
        });
        this.btn_search_cancel = (ImageView) findViewById(R.id.btn_search_cancel);
        this.btn_search = (ImageView) findViewById(R.id.btn_search);
        this.isAllFabsVisible = Boolean.FALSE;
        this.rcvHistory = (RecyclerView) findViewById(R.id.rcvHistory);
        this.imvDelete = (ImageView) findViewById(R.id.imvDelete);
        this.imvToHistory = (ImageView) findViewById(R.id.imvToHistory);
        this.rlHistory = (RelativeLayout) findViewById(R.id.rlHistory);
        this.imv_youtubeBrowserConnect = (ImageView) findViewById(R.id.imv_youtubeBrowserConnect);
        if (TVConnectUtils.getInstance().isConnected()) {
            this.imv_youtubeBrowserConnect.setImageResource(R.drawable.hover_screen);
        } else {
            this.imv_youtubeBrowserConnect.setImageResource(R.drawable.screen);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(1);
        this.rcvHistory.setLayoutManager(linearLayoutManager);
        ContentHistoryAdapter contentHistoryAdapter2 = new ContentHistoryAdapter();
        this.contentHistoryAdapter = contentHistoryAdapter2;
        contentHistoryAdapter2.setListener(new ContentHistoryAdapter.IItemClickHistory() {
            /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass5 */

            @Override
            // com.thntech.cast68.screen.tab.webcast.history.adapter.ContentHistoryAdapter.IItemClickHistory
            public void clickDelete(HistoryBrowser historyBrowser) {
                WebCastActivity.this.viewModel.deleteHistory(historyBrowser);
            }

            @Override
            // com.thntech.cast68.screen.tab.webcast.history.adapter.ContentHistoryAdapter.IItemClickHistory
            public void clickItem(HistoryBrowser historyBrowser) {
                WebCastActivity.this.et_search_bar.setText(historyBrowser.getLinkUrl());
                WebCastActivity.this.navigate_browser();
            }
        });
        this.rcvHistory.setAdapter(this.contentHistoryAdapter);
        this.rlData = (RelativeLayout) findViewById(R.id.rlData);
        this.rlPhoto = (LinearLayout) findViewById(R.id.rlPhoto);
        this.rlAudio = (LinearLayout) findViewById(R.id.rlAudio);
        this.rlVideo = (LinearLayout) findViewById(R.id.rlVideo);
        this.tvSizePhoto = (TextView) findViewById(R.id.tvSizePhoto);
        this.tvSizeAudio = (TextView) findViewById(R.id.tvSizeAudio);
        this.tvSizeVideo = (TextView) findViewById(R.id.tvSizeVideo);
        this.imv_youtubeBrowserList = (ImageView) findViewById(R.id.imv_youtubeBrowserList);
        this.imv_youtubeBrowserBack = (ImageView) findViewById(R.id.imv_youtubeBrowserBack);
        this.imv_youtubeBrowserHome = (ImageView) findViewById(R.id.imv_youtubeBrowserHome);
        this.imv_youtubeBrowserForward = (ImageView) findViewById(R.id.imv_youtubeBrowserForward);
        this.imv_youtubeBrowserReload = (ImageView) findViewById(R.id.imv_youtubeBrowserReload);
        this.imv_youtubeBack = (ImageView) findViewById(R.id.imv_youtubeBack);
        this.llContent = (LinearLayout) findViewById(R.id.llContent);
        this.simpleWebView.setVisibility(8);
        this.llYoutube = (LinearLayout) findViewById(R.id.llYoutube);
        this.llVimeo = (LinearLayout) findViewById(R.id.llVimeo);
        this.llBuzzVideo = (LinearLayout) findViewById(R.id.llBuzzVideo);
        this.llGoogle = (LinearLayout) findViewById(R.id.llGoogle);
        this.llSounCloud = (LinearLayout) findViewById(R.id.llSounCloud);
        this.llYaHoo = (LinearLayout) findViewById(R.id.llYaHoo);
        this.llIMDB = (LinearLayout) findViewById(R.id.llIMDB);
        this.llTwitch = (LinearLayout) findViewById(R.id.llTwitch);
        this.llFox = (LinearLayout) findViewById(R.id.llFox);
        this.llLiveStream = (LinearLayout) findViewById(R.id.llLiveStream);
        this.llESPN = (LinearLayout) findViewById(R.id.llESPN);
        this.llYTBGaming = (LinearLayout) findViewById(R.id.llYTBGaming);
        this.imvToHistory.setOnClickListener(this);
        this.imvDelete.setOnClickListener(this);
        this.imv_youtubeBrowserConnect.setOnClickListener(this);
        this.imv_youtubeBrowserList.setOnClickListener(this);
        this.imv_youtubeBrowserBack.setOnClickListener(this);
        this.imv_youtubeBrowserHome.setOnClickListener(this);
        this.imv_youtubeBrowserForward.setOnClickListener(this);
        this.imv_youtubeBrowserReload.setOnClickListener(this);
        this.imv_youtubeBack.setOnClickListener(this);
        this.rlData.setOnClickListener(this);
        this.rlVideo.setOnClickListener(this);
        this.rlAudio.setOnClickListener(this);
        this.rlPhoto.setOnClickListener(this);
        this.llYoutube.setOnClickListener(this);
        this.llVimeo.setOnClickListener(this);
        this.llBuzzVideo.setOnClickListener(this);
        this.llGoogle.setOnClickListener(this);
        this.llSounCloud.setOnClickListener(this);
        this.llYaHoo.setOnClickListener(this);
        this.llIMDB.setOnClickListener(this);
        this.llTwitch.setOnClickListener(this);
        this.llFox.setOnClickListener(this);
        this.llLiveStream.setOnClickListener(this);
        this.llESPN.setOnClickListener(this);
        this.llYTBGaming.setOnClickListener(this);
    }

    public Unit actionCommon() {
        return Unit.INSTANCE;
    }

    @Override // androidx.activity.ComponentActivity, androidx.fragment.app.FragmentActivity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1) {
            this.et_search_bar.setText(intent.getStringExtra(IronSourceConstants.EVENTS_RESULT));
            navigate_browser();
        }
    }

    public void onClick(View view) {
        boolean z = true;
        switch (view.getId()) {
            case R.id.imvDelete:
                this.viewModel.deleteAllHistory();
                return;
            case R.id.imvToHistory:
                startActivityForResult(new Intent(this, HistoryBrowserActivity.class), 100);
                Utils.nextScreen(this);
                return;
            case R.id.imv_youtubeBack:
                DialogExitActivity dialogExitActivity = new DialogExitActivity(this);
                dialogExitActivity.show();
                dialogExitActivity.setListener(new DialogExitActivity.IIExitMoreApp() {
                    @Override // com.thntech.cast68.dialog.DialogExitActivity.IIExitMoreApp
                    public void clickExitApp() {
                    }

                    @Override // com.thntech.cast68.dialog.DialogExitActivity.IIExitMoreApp
                    public void clickSubmit() {
                        WebCastActivity.this.onFinish();
                    }
                });
                return;
            case R.id.imv_youtubeBrowserBack:
                onBackPressed();
                return;
            case R.id.imv_youtubeBrowserConnect:
                if (TVConnectUtils.getInstance().isConnected()) {
                    new DialogDisconnect(this).show();
                    return;
                } else {
                    gotoActivity(ConnectActivity.class);
                    return;
                }
            case R.id.imv_youtubeBrowserForward:
                WebView webView = this.simpleWebView;
                if (webView.canGoForward()) {
                    webView.goForward();
                }
                if (this.simpleWebView.getVisibility() == 8) {
                    this.llContent.setVisibility(8);
                    this.simpleWebView.setVisibility(0);
                    return;
                }
                return;
            case R.id.imv_youtubeBrowserHome:
                this.simpleWebView.setVisibility(8);
                this.llContent.setVisibility(0);
                set_searchbar_text("");
                this.simpleWebView.loadUrl("");
                return;
            case R.id.imv_youtubeBrowserList:
                this.rlData.setVisibility(0);
                toggle_fab_buttons();
                return;
            case R.id.imv_youtubeBrowserReload:
                this.simpleWebView.reload();
                return;
            case R.id.llBuzzVideo:
                CheckUrls("https://www.buzzvideos.com/");
                return;
            case R.id.llESPN:
                CheckUrls("https://www.espn.com/");
                return;
            case R.id.llFox:
                CheckUrls("https://www.foxnews.com/");
                return;
            case R.id.llGoogle:
                CheckUrls("https://www.google.com/");
                return;
            case R.id.llIMDB:
                CheckUrls("https://www.imdb.com/");
                return;
            case R.id.llLiveStream:
                CheckUrls("https://livestream.com/");
                return;
            case R.id.llSounCloud:
                CheckUrls("https://mobile.twitter.com/");
                return;
            case R.id.llTwitch:
                CheckUrls("https://www.twitch.tv/");
                return;
            case R.id.llVimeo:
                CheckUrls("https://www.tiktok.com/");
                return;
            case R.id.llYTBGaming:
                CheckUrls("https://www.dailymotion.com");
                return;
            case R.id.llYaHoo:
                CheckUrls("https://www.instagram.com/");
                return;
            case R.id.llYoutube:
                CheckUrls("https://m.facebook.com/");
                return;
            case R.id.rlAudio:
                try {
                    file_type file_type = com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.file_type.AUDIO;
                    ArrayList<downloadable_resource_model> arrayList = static_variables.get_downloadable_resource_model_By_Type(file_type);
                    if (arrayList != null) {
                        if (arrayList.size() != 0) {
                            z = false;
                        }
                    }
                    available_files_dialog available_files_dialog = new available_files_dialog(this, file_type, z);
                    this._available_files_dialog = available_files_dialog;
                    available_files_dialog.show();
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            case R.id.rlData:
                this.rlData.setVisibility(8);
                return;
            case R.id.rlPhoto:
                try {
                    file_type file_type2 = file_type.IMAGE;
                    ArrayList<downloadable_resource_model> arrayList2 = static_variables.get_downloadable_resource_model_By_Type(file_type2);
                    if (arrayList2 != null) {
                        if (arrayList2.size() != 0) {
                            z = false;
                        }
                    }
                    available_files_dialog available_files_dialog2 = new available_files_dialog(this, file_type2, z);
                    this._available_files_dialog = available_files_dialog2;
                    available_files_dialog2.show();
                    return;
                } catch (Exception e3) {
                    e3.printStackTrace();
                    return;
                }
            case R.id.rlVideo:
                try {
                    file_type file_type3 = file_type.VIDEO;
                    ArrayList<downloadable_resource_model> arrayList3 = static_variables.get_downloadable_resource_model_By_Type(file_type3);
                    if (arrayList3 != null) {
                        if (arrayList3.size() != 0) {
                            z = false;
                        }
                    }
                    available_files_dialog available_files_dialog3 = new available_files_dialog(this, file_type3, z);
                    this._available_files_dialog = available_files_dialog3;
                    available_files_dialog3.show();
                    return;
                } catch (Exception e4) {
                    e4.printStackTrace();
                    return;
                }
            default:
                return;
        }
    }

    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        getMenuInflater().inflate(R.menu.toolbar, contextMenu);
    }

    public void toggle_fab_buttons() {
        if (!this.isAllFabsVisible.booleanValue()) {
            this.isAllFabsVisible = Boolean.TRUE;
        } else {
            this.isAllFabsVisible = Boolean.FALSE;
        }
    }

    private void set_button_click_events() {
        this.btn_search.setOnClickListener(new View.OnClickListener() {
            /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass7 */

            public void onClick(View view) {
                WebCastActivity.this.navigate_browser();
            }
        });
        this.btn_search_cancel.setOnClickListener(new View.OnClickListener() {
            /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass8 */

            public void onClick(View view) {
                WebCastActivity.this.set_searchbar_text("");
            }
        });
        this.et_search_bar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass9 */

            public void onFocusChange(View view, boolean z) {
                if (z && WebCastActivity.this.et_search_bar.getText().toString().equals(WebCastActivity.this.getResources().getString(R.string.home))) {
                    WebCastActivity.this.set_searchbar_text("");
                }
            }
        });
        this.et_search_bar.setOnKeyListener(new View.OnKeyListener() {
            /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass10 */

            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() != 0 || i != 66) {
                    return false;
                }
                WebCastActivity.this.navigate_browser();
                return true;
            }
        });
        this.et_search_bar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass11 */

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                WebCastActivity.this.navigate_browser();
            }
        });
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService("input_method");
        View currentFocus = getCurrentFocus();
        if (currentFocus == null) {
            currentFocus = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }

    public void findLocal() {
        if (this.et_search_bar.getText().toString().contains("facebook.com")) {
            this.simpleWebView.evaluateJavascript("(function() {return document.getElementsByTagName('html')[0].outerHTML;})();", new ValueCallback<String>() {
                /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass12 */

                public void onReceiveValue(String str) {
                    String nextString;
                    if (str != null) {
                        try {
                            JsonReader jsonReader = new JsonReader(new StringReader(str));
                            jsonReader.setLenient(true);
                            if (jsonReader.peek() == JsonToken.STRING && (nextString = jsonReader.nextString()) != null) {
                                String attr = Jsoup.parse(nextString).select("meta[property=\"og:video\"]").last().attr("content");
                                static_variables.resourse_holder.setVideo_files(new ArrayList<>());
                                static_variables.resourse_holder.add_Video(null, "video", attr, "Video", "page");
                                WebCastActivity.this.runOnUiThread(new Runnable() {
                                    /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass12.AnonymousClass1 */

                                    public void run() {
                                        WebCastActivity.this.update_image_fab_text();
                                        WebCastActivity.this.enable_Buttons();
                                    }
                                });
                            }
                        } catch (Exception unused) {
                        }
                    }
                }
            });
        }
        boolean z = false;
        for (String str : this.mContext.getResources().getStringArray(R.array.customised_searches)) {
            if (this.et_search_bar.getText().toString().contains(str)) {
                z = true;
            }
        }
        if (z) {
            this.simpleWebView.evaluateJavascript("(function() {return document.getElementsByTagName('html')[0].outerHTML;})();", new ValueCallback<String>() {
                /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass13 */

                public void onReceiveValue(String str) {
                    WebCastActivity webCastActivity = WebCastActivity.this;
                    List<CustomGrabberModel> Search = CustomSearch.Search(webCastActivity.mContext, webCastActivity.et_search_bar.getText().toString(), str);
                    if (Search.size() > 0) {
                        static_variables.resourse_holder = new resourse_holder_model();
                    }
                    for (CustomGrabberModel customGrabberModel : Search) {
                        if (!(customGrabberModel.getVideoUrl() == null || customGrabberModel.getVideoUrl() == "")) {
                            if (customGrabberModel.getM3u8().booleanValue()) {
                                static_variables.resourse_holder.add_Video(null, "m3u8", customGrabberModel.getVideoUrl(), "Video", "page");
                            } else {
                                if (static_variables.resourse_holder == null) {
                                    static_variables.resourse_holder = new resourse_holder_model();
                                }
                                static_variables.resourse_holder.add_Video(null, "video", customGrabberModel.getVideoUrl(), "Video", "page");
                            }
                        }
                    }
                    WebCastActivity.this.runOnUiThread(new Runnable() {
                        /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass13.AnonymousClass1 */

                        public void run() {
                            WebCastActivity.this.update_image_fab_text();
                            WebCastActivity.this.enable_Buttons();
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    public boolean onContextItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_Downloads) {
            startActivity(new Intent(this, DownloadsActivity.class));
            return true;
        }
        if (itemId != R.id.action_help) {
            switch (itemId) {
                case R.id.action_nonworking:
                    this.et_search_bar.setText("https://forms.gle/gvoduNgZ4VVCXMd38");
                    navigate_browser();
                    break;
                case R.id.action_other_apps:
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("YOUR play store Developer Page URL")));
                    return true;
                case R.id.action_pp:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getString(R.string.PrivacyPolicy));
                    WebView webView = new WebView(this);
                    webView.loadUrl("file:///android_asset/PrivacyPolicy.html");
                    webView.setWebViewClient(new WebViewClient());
                    builder.setView(webView);
                    builder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                        /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass14 */

                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                    return true;
//                case R.id.action_share:
                default:
                    return true;
                case R.id.action_share_app:
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.SUBJECT", getString(R.string.hithere));
                    intent.putExtra("android.intent.extra.TEXT", "Hi \nPlease check this Awesome Application. '" + getResources().getString(R.string.app_name) + "'\nYou'll love it. \n\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
                    startActivity(Intent.createChooser(intent, getString(R.string.Shareusing)));
                    return true;
            }
        }
        return true;
    }

    public void navigate_browser() {
        this.simpleWebView.setVisibility(0);
        this.llContent.setVisibility(8);
        hideKeyboard();
        if (!Patterns.WEB_URL.matcher(this.et_search_bar.getText()).matches()) {
            AutoCompleteTextView autoCompleteTextView = this.et_search_bar;
            autoCompleteTextView.setText("https://www.google.com/search?q=" + ((Object) this.et_search_bar.getText()));
        }
        this.simpleWebView.loadUrl(this.et_search_bar.getText().toString());
    }

    public void startTimer() {
        disable_fab_button();
        this.loadingPageProgress.setProgress(0);
        CountDownTimer r0 = new CountDownTimer(1100, 1000) {
            /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass15 */

            public final void onFinish() {
            }

            public final void onTick(long j) {
                if (WebCastActivity.this.loadingPageProgress.getProgress() < 80) {
                    AnimatedProgressBar animatedProgressBar = WebCastActivity.this.loadingPageProgress;
                    animatedProgressBar.setProgress(animatedProgressBar.getProgress() + 8);
                }
            }
        };
        this.countDownTimer = r0;
        r0.start();
        this.loadingPageProgress.setVisibility(0);
    }

    public void stopTimer() {
        try {
            this.countDownTimer.cancel();
        } catch (Exception unused) {
        }
        this.loadingPageProgress.setProgress(100);
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass16 */

            public void run() {
                WebCastActivity.this.loadingPageProgress.setProgress(0);
                WebCastActivity.this.loadingPageProgress.setVisibility(8);
            }
        }, 500);
    }

    public void set_searchbar_text(String str) {
        if (str.equals(getResources().getString(R.string.index_page))) {
            str = getResources().getString(R.string.home);
        }
        if (str.equals("")) {
            this.btn_search_cancel.setVisibility(4);
            this.et_search_bar.requestFocus();
        } else {
            this.btn_search_cancel.setVisibility(0);
        }
        this.et_search_bar.setText(str);
    }

    public class customWebClient extends WebViewClient {
        public customWebClient() {
        }

        @Override // android.webkit.WebViewClient
        public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
            if ((str.contains("ad") || str.contains("banner") || str.contains("pop")) && WebCastActivity.this.checkUrlIfAds(str)) {
                return new WebResourceResponse(null, null, null);
            }
            return super.shouldInterceptRequest(webView, str);
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (!str.startsWith("intent://")) {
                return super.shouldOverrideUrlLoading(webView, str);
            }
            try {
                Context context = WebCastActivity.this.simpleWebView.getContext();
                Intent parseUri = Intent.parseUri(str, 4);
                if (parseUri == null) {
                    return false;
                }
                ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(parseUri, 65536);
                if (parseUri.getScheme().equals("https") || parseUri.getScheme().equals("http")) {
                    WebCastActivity.this.simpleWebView.loadUrl(parseUri.getStringExtra("browser_fallback_url"));
                    return true;
                }
                if (resolveActivity != null) {
                    context.startActivity(parseUri);
                } else {
                    context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(parseUri.getStringExtra("browser_fallback_url"))));
                }
                return true;
            } catch (Exception unused) {
                return false;
            }
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            WebCastActivity.this.startTimer();
            WebCastActivity.this.isRedirected = false;
            super.onPageStarted(webView, str, bitmap);
            Timer timer = WebCastActivity.this.timer;
            if (timer != null) {
                timer.cancel();
                WebCastActivity.this.timer = null;
            }
            WebCastActivity.this.set_searchbar_text(str);
            static_variables.resourse_holder = new resourse_holder_model();
        }

        public void onPageFinished(WebView webView, String str) {
            if (static_variables.resourse_holder == null) {
                static_variables.resourse_holder = new resourse_holder_model();
            }
            static_variables.resourse_holder.setPage_title(webView.getTitle());
            if (!WebCastActivity.this.isRedirected) {
                WebCastActivity.this.stopTimer();
                WebCastActivity.this.enable_Buttons();
                WebCastActivity.this.findLocal();
                if (WebCastActivity.this.imv_youtubeBrowserList.isEnabled()) {
                    YoYo.with(Techniques.Tada).duration(300).repeat(5).playOn(WebCastActivity.this.findViewById(R.id.imv_youtubeBrowserList));
                }
            }
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }

        public void onLoadResource(WebView webView, String str) {
            WebCastActivity.this.saveLink(webView.getUrl(), webView.getTitle());
            WebCastActivity webCastActivity = WebCastActivity.this;
            if (!URLAddFilter.DoNotCheckIf(webCastActivity.mContext, webCastActivity.et_search_bar.getText().toString())) {
                new ContentSearch(WebCastActivity.this.mContext, str, webView.getUrl(), webView.getTitle()) {
                    /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.customWebClient.AnonymousClass1 */

                    @Override // com.thntech.cast68.screen.tab.webcast.ContentSearch
                    public void onStartInspectingURL() {
                        com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.Utils.disableSSLCertificateChecking();
                    }

                    @Override // com.thntech.cast68.screen.tab.webcast.ContentSearch
                    public void onFinishedInspectingURL(boolean z) {
                        HttpsURLConnection.setDefaultSSLSocketFactory(WebCastActivity.this.defaultSSLSF);
                    }

                    @Override // com.thntech.cast68.screen.tab.webcast.ContentSearch
                    public void onVideoFound(String str, String str2, String str3, String str4, String str5, boolean z, String str6, boolean z2) {
                        try {
                            static_variables.resourse_holder.add_Video(str, str2, str3, str4, str5);
                            if (static_variables.resourse_holder.getVideo_files().size() > 0) {
                                WebCastActivity.this.runOnUiThread(new Runnable() {
                                    /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.customWebClient.AnonymousClass1.AnonymousClass1 */

                                    public void run() {
                                        WebCastActivity.this.update_image_fab_text();
                                        WebCastActivity.this.enable_Buttons();
                                    }
                                });
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }

                    @Override // com.thntech.cast68.screen.tab.webcast.ContentSearch
                    public void onImageFound(String str, String str2, String str3, String str4, String str5, boolean z, String str6, boolean z2) {
                        try {
                            static_variables.resourse_holder.add_Image(str, str2, str3, str4, str5);
                            if (static_variables.resourse_holder.getImage_files().size() > 0) {
                                WebCastActivity.this.runOnUiThread(new Runnable() {
                                    /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.customWebClient.AnonymousClass1.AnonymousClass2 */

                                    public void run() {
                                        WebCastActivity.this.update_image_fab_text();
                                        WebCastActivity.this.enable_Buttons();
                                    }
                                });
                            }
                        } catch (Exception unused) {
                        }
                    }

                    @Override // com.thntech.cast68.screen.tab.webcast.ContentSearch
                    public void onAudioFound(String str, String str2, String str3, String str4, String str5, boolean z, String str6, boolean z2) {
                        if (str3 != null && str4 != null) {
                            try {
                                static_variables.resourse_holder.add_Audio(str, str2, str3, str4, str5);
                                if (static_variables.resourse_holder.getImage_files().size() > 0) {
                                    WebCastActivity.this.runOnUiThread(new Runnable() {
                                        /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.customWebClient.AnonymousClass1.AnonymousClass3 */

                                        public void run() {
                                            WebCastActivity.this.update_audio_fab_text();
                                            WebCastActivity.this.enable_Buttons();
                                        }
                                    });
                                }
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void saveLink(String str, String str2) {
        updateUIBackForward();
        try {
            if ((this.title.equals(str2) && this.linkUrl.equals(str)) || str.isEmpty()) {
                return;
            }
            if (!str2.isEmpty()) {
                if (!str2.contains("about:blank")) {
                    this.title = str2;
                    this.linkUrl = str;
                    this.viewModel.addHistory(new HistoryBrowser(0, this.title, this.linkUrl, com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast.Utils.getTypeBrowser(str.toLowerCase()), new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date())));
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void enable_Buttons() {
        if (!(static_variables.resourse_holder.getVideo_files() == null || static_variables.resourse_holder.getAudio_files() == null || static_variables.resourse_holder.getImage_files() == null || (static_variables.resourse_holder.getVideo_files().size() <= 0 && static_variables.resourse_holder.getAudio_files().size() <= 0 && static_variables.resourse_holder.getImage_files().size() <= 0))) {
            enable_audio_fab();
            enable_fab_button();
        }
        if (static_variables.resourse_holder.getVideo_files() != null && static_variables.resourse_holder.getVideo_files().size() > 0) {
            enable_video_fab();
            update_video_fab_text();
        }
        if (static_variables.resourse_holder.getImage_files() != null && static_variables.resourse_holder.getImage_files().size() > 0) {
            enable_images_fab();
            update_video_fab_text();
        }
    }

    public void popupSnackbarForCompleteUpdate() {
        try {
            Snackbar make = Snackbar.make(findViewById(R.id.simpleWebView), "An update has just been downloaded.", 2);
            make.setAction("RESTART", new View.OnClickListener() {
                /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass17 */

                public void onClick(View view) {
                    AppUpdateManager appUpdateManager = WebCastActivity.this.mAppUpdateManager;
                    if (appUpdateManager != null) {
                        appUpdateManager.completeUpdate();
                    }
                }
            });
            make.setDuration(50000);
            make.show();
        } catch (Resources.NotFoundException e2) {
            e2.printStackTrace();
        }
    }

    private void checkAppUpdate() {
        try {
            AppUpdateManager create = AppUpdateManagerFactory.create(this);
            this.mAppUpdateManager = create;
            create.registerListener(this.installStateUpdatedListener);
            this.mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                /* class com.thntech.cast68.screen.tab.webcast.WebCastActivity.AnonymousClass18 */

                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if (appUpdateInfo.updateAvailability() == 3 && appUpdateInfo.isUpdateTypeAllowed(0)) {
                        try {
                            WebCastActivity webCastActivity = WebCastActivity.this;
                            webCastActivity.mAppUpdateManager.startUpdateFlowForResult(appUpdateInfo, 0, webCastActivity, 201);
                        } catch (IntentSender.SendIntentException e2) {
                            e2.printStackTrace();
                        }
                    } else if (appUpdateInfo.installStatus() == 11) {
                        WebCastActivity.this.popupSnackbarForCompleteUpdate();
                    }
                }
            });
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // androidx.activity.ComponentActivity
    public void onBackPressed() {
        if (this.simpleWebView.copyBackForwardList().getCurrentIndex() > 0) {
            if (this.simpleWebView.copyBackForwardList().getCurrentIndex() == 1) {
                set_searchbar_text("");
                this.simpleWebView.loadUrl(this.et_search_bar.getText().toString());
                this.simpleWebView.setVisibility(8);
                this.llContent.setVisibility(0);
            }
            this.simpleWebView.goBack();
            return;
        }
        new DialogExitActivity(this).show();
    }
}
