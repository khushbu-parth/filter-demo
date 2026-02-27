package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.youtube_browser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.dialog.DialogNoResourcesFound;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.ManagerDataPlay;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.ConnectActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.DialogDisconnect;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.playcast.PlayCastActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Const;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.SharedPrefsUtil;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;
import com.monstertechno.adblocker.AdBlockerWebView;
import com.monstertechno.adblocker.util.AdBlocker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import kotlin.Unit;
import vimeoextractor.OnVimeoExtractionListener;
import vimeoextractor.VimeoExtractor;
import vimeoextractor.VimeoVideo;

@SuppressLint("WrongConstant")
public class YoutubeBrowserActivity extends BaseActivity implements View.OnClickListener {
    public static Boolean pageReload = Boolean.FALSE;
    DialogBSFYoutube dialogBSFYoutube;
    private ImageView imv_youtubeBack;
    private ImageView imv_youtubeBrowserBack;
    private ImageView imv_youtubeBrowserConnect;
    private ImageView imv_youtubeBrowserForward;
    private ImageView imv_youtubeBrowserHelp;
    private ImageView imv_youtubeBrowserHome;
    private ImageView imv_youtubeBrowserList;
    private ImageView imv_youtubeBrowserReload;
    private LinearProgressIndicator linearProgressIndicator;
    private WebView page;
    private TextView tv_youtubeBrowserTitle;
    private String url_default;
    ArrayList<YoutubeDto> youtubeDtoArrayList = new ArrayList<>();
    private int currentPos = 0;
    private String[] qualityList = {"540p", "240p", "360p", "720p", "1080p"};
    private String[] thumbList = {"640", "940", "1280", "base"};

    YouTubeExtractor youTubeExtractor;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_youtube_browser);
        AdsManager.CallNativeAdLoad(this, findViewById(R.id.native_container), AdsManager.NATIVE_SMALL);
        EventBus.getDefault().register(this);
        initView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessage().contains("KEY_CONNECT") && this.imv_youtubeBrowserConnect != null) {
            if (TVConnectUtils.getInstance().isConnected()) {
                this.imv_youtubeBrowserConnect.setImageResource(R.drawable.hover_screen);
            } else {
                this.imv_youtubeBrowserConnect.setImageResource(R.drawable.screen);
            }
        }
    }

    private void initView() {
        initFindViewById();
        String str = getIntent().getStringExtra("browser_type");
        String ans = str.equals("youtube") ? "https://www.youtube.com/" : str.equals("googlePhoto") ? "https://images.google.com/" : "https://vimeo.com/watch";
        this.url_default = ans;
        this.tv_youtubeBrowserTitle.setText(str.equals("youtube") ? "Cast Youtube" : str.equals("googlePhoto") ? "Google Image" : "Cast Vimeo");
        initWebView();
    }

    private void initWebView() {
        WebSettings settings = this.page.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        this.page.setWebChromeClient(new WebChromeClient() {
            @Override // android.webkit.WebChromeClient
            public void onProgressChanged(WebView webView, int i) {
                YoutubeBrowserActivity.this.linearProgressIndicator.setVisibility(0);
                YoutubeBrowserActivity.this.linearProgressIndicator.setProgress(i);
                if (i == 100) {
                    YoutubeBrowserActivity.this.linearProgressIndicator.setVisibility(8);
                }
                super.onProgressChanged(webView, i);
            }
        });
        new AdBlockerWebView.init(this).initializeWebView(this.page);
        this.page.setWebViewClient(new WebViewClient() {
            public String currentPage;

            {
                this.currentPage = YoutubeBrowserActivity.this.page.getUrl();
            }

            @Override // android.webkit.WebViewClient
            public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
                return AdBlockerWebView.blockAds(webView, str) ? AdBlocker.createEmptyResource() : super.shouldInterceptRequest(webView, str);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (!str.startsWith("intent")) {
                    return super.shouldOverrideUrlLoading(webView, str);
                }
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                if (!webResourceRequest.getUrl().toString().startsWith("intent")) {
                    return super.shouldOverrideUrlLoading(webView, webResourceRequest);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
            }

            @Override // android.webkit.WebViewClient
            public void onLoadResource(WebView webView, String str) {
                String url = webView.getUrl();
                if ((url.equals(this.currentPage) || url.contains("#searching")) && !YoutubeBrowserActivity.pageReload.booleanValue()) {
                    return;
                }
                YoutubeBrowserActivity.pageReload = Boolean.FALSE;
                this.currentPage = url;
                YoutubeBrowserActivity.this.youtubeDtoArrayList.clear();
                YoutubeBrowserActivity.this.imv_youtubeBrowserList.setImageResource(R.drawable.ic_yt_list_empty);
                YoutubeBrowserActivity.this.updateUIBackForward();
                if (this.currentPage.contains("https://m.youtube.com/watch?v=")) {
                    String replace = url.replace("https://m.youtube.com/watch?v=", "");
                    youTubeExtractor = new YouTubeExtractor(YoutubeBrowserActivity.this) {
                        @Override
                        protected void onExtractionComplete(SparseArray<YtFile> sparseArray, VideoMeta videoMeta) {
                            if (sparseArray == null) {
                                Log.e("###TAG", "sparseArray: NULL");
                                return;
                            }
                            Log.e("###TAG", "onExtractionComplete: Here");
                            for (Integer num = 0; num.intValue() < sparseArray.size(); num = Integer.valueOf(num.intValue() + 1)) {
                                YtFile ytFile = sparseArray.get(Integer.valueOf(sparseArray.keyAt(num.intValue())).intValue());
                                StringBuilder sb = new StringBuilder();
                                sb.append("URL: ");
                                sb.append(ytFile.getUrl());
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("getHeight: ");
                                sb2.append(ytFile.getFormat().getHeight());
                                if ((ytFile.getFormat().getExt().equals("mp4") || ytFile.getFormat().getExt().equals("m3u") || ytFile.getFormat().getExt().equals("m3u8")) && ((ytFile.getFormat().getHeight() == 144 || ytFile.getFormat().getHeight() == 360 || ytFile.getFormat().getHeight() == 240 || ytFile.getFormat().getHeight() == 720 || ytFile.getFormat().getHeight() == 1080 || ytFile.getFormat().getHeight() == 480) && ytFile.getFormat().getAudioBitrate() != -1)) {
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("URL: ");
                                    sb3.append(ytFile.getUrl());
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("AUDIO: ");
                                    sb4.append(ytFile.getFormat().getAudioBitrate());
                                    StringBuilder sb5 = new StringBuilder();
                                    sb5.append("AUDIO SS: ");
                                    sb5.append(ytFile.getFormat().getAudioCodec());
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append("getHeight: ");
                                    sb6.append(ytFile.getFormat().getHeight());
                                    ArrayList<YoutubeDto> arrayList = YoutubeBrowserActivity.this.youtubeDtoArrayList;
                                    String title = videoMeta.getTitle();
                                    String hqImageUrl = videoMeta.getHqImageUrl();
                                    String url2 = ytFile.getUrl();
                                    Log.e("###TAG", "url2: " + url2.toString());
                                    arrayList.add(new YoutubeDto(title, hqImageUrl, url2, ytFile.getFormat().getHeight() + "p", "mp4", videoMeta.getVideoLength()));
                                }
                                Log.e("###TAG", "sb: " + sb.toString());
                            }
                            YoutubeBrowserActivity.this.updateList();
                        }

                        /* JADX INFO: Access modifiers changed from: protected */
                        @Override // android.os.AsyncTask
                        public void onProgressUpdate(Void... voidArr) {
                            super.onProgressUpdate(voidArr);
                        }

                        @Override // android.os.AsyncTask
                        protected void onCancelled() {
                            super.onCancelled();
                        }

                        /* JADX INFO: Access modifiers changed from: protected */
                        @Override // android.os.AsyncTask
                        public void onCancelled(SparseArray<YtFile> sparseArray) {
                            super.onCancelled(sparseArray);
                        }
                    };
                    youTubeExtractor.extract("http://youtube.com/watch?v=" + replace);
                } else if (!this.currentPage.contains("https://vimeo.com/")) {
                } else {
                    String replace2 = url.replace("https://vimeo.com/", "");
                    if (!replace2.matches("\\d+")) {
                        return;
                    }
                    VimeoExtractor.getInstance().fetchVideoWithIdentifier(replace2, null, new OnVimeoExtractionListener() { // from class: com.magicapps.casttotv.tv.screen.tab.youtube_browser.YoutubeBrowserActivity.2.2
                        @Override // vimeoextractor.OnVimeoExtractionListener
                        public void onFailure(Throwable th) {
                        }

                        @Override // vimeoextractor.OnVimeoExtractionListener
                        public void onSuccess(VimeoVideo vimeoVideo) {
                            for (int i = 0; i < YoutubeBrowserActivity.this.qualityList.length; i++) {
                                if (vimeoVideo.getStreams().containsKey(YoutubeBrowserActivity.this.qualityList[i])) {
                                    int i2 = 0;
                                    while (true) {
                                        if (i2 >= YoutubeBrowserActivity.this.thumbList.length) {
                                            break;
                                        } else if (vimeoVideo.getThumbs().containsKey(YoutubeBrowserActivity.this.thumbList[i2])) {
                                            YoutubeBrowserActivity.this.youtubeDtoArrayList.add(new YoutubeDto(vimeoVideo.getTitle(), vimeoVideo.getThumbs().get(YoutubeBrowserActivity.this.thumbList[i2]), vimeoVideo.getStreams().get(YoutubeBrowserActivity.this.qualityList[i]), YoutubeBrowserActivity.this.qualityList[i], "mp4", vimeoVideo.getDuration()));
                                            break;
                                        } else {
                                            i2++;
                                        }
                                    }
                                }
                            }
                            YoutubeBrowserActivity.this.updateList();
                        }
                    });
                }
            }
        });
        this.page.loadUrl(this.url_default);
    }

    public void updateUIBackForward() {
        this.imv_youtubeBrowserBack.setImageResource(this.page.canGoBack() ? R.drawable.ic_yt_back : R.drawable.ic_yt_back_black);
        this.imv_youtubeBrowserForward.setImageResource(this.page.canGoBack() ? R.drawable.ic_yt_next : R.drawable.ic_yt_next_black);
    }

    public void updateList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    YoutubeBrowserActivity.this.dialogBSFYoutube = DialogBSFYoutube.getInstance();
                    YoutubeBrowserActivity.this.dialogBSFYoutube.setListener(new DialogBSFYoutube.ItemClickListener() {
                        @Override
                        public void onClick(int i) {
                            YoutubeBrowserActivity.this.currentPos = i;
                            if (!TVConnectUtils.getInstance().isConnected()) {
                                YoutubeBrowserActivity.this.gotoConnect();
                            } else {
                                YoutubeBrowserActivity.this.openActivityMediaPlayer();
                            }
                        }
                    });
                    DialogBSFYoutube dialogBSFYoutube = YoutubeBrowserActivity.this.dialogBSFYoutube;
                    if (dialogBSFYoutube != null && !dialogBSFYoutube.isAdded()) {
                        YoutubeBrowserActivity youtubeBrowserActivity = YoutubeBrowserActivity.this;
                        youtubeBrowserActivity.dialogBSFYoutube.show(youtubeBrowserActivity.getSupportFragmentManager(), "YoutubeBrowserActivity");
                        YoutubeBrowserActivity youtubeBrowserActivity2 = YoutubeBrowserActivity.this;
                        youtubeBrowserActivity2.dialogBSFYoutube.setData(youtubeBrowserActivity2.youtubeDtoArrayList);
                    }
                    YoutubeBrowserActivity.this.imv_youtubeBrowserList.setImageResource(YoutubeBrowserActivity.this.youtubeDtoArrayList.isEmpty() ? R.drawable.ic_yt_list_empty : R.drawable.ic_yt_list);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            DialogBSFYoutube dialogBSFYoutube = this.dialogBSFYoutube;
            if (dialogBSFYoutube == null || !dialogBSFYoutube.isAdded() || this.dialogBSFYoutube.getDialog() == null || !this.dialogBSFYoutube.getDialog().isShowing()) {
                return;
            }
            this.dialogBSFYoutube.dismiss();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void initFindViewById() {
        this.imv_youtubeBack = (ImageView) findViewById(R.id.imv_youtubeBack);
        this.imv_youtubeBrowserConnect = (ImageView) findViewById(R.id.imv_youtubeBrowserConnect);
        this.imv_youtubeBrowserHelp = (ImageView) findViewById(R.id.imv_youtubeBrowserHelp);
        this.page = (WebView) findViewById(R.id.page);
        this.imv_youtubeBrowserList = (ImageView) findViewById(R.id.imv_youtubeBrowserList);
        this.imv_youtubeBrowserBack = (ImageView) findViewById(R.id.imv_youtubeBrowserBack);
        this.imv_youtubeBrowserForward = (ImageView) findViewById(R.id.imv_youtubeBrowserForward);
        this.imv_youtubeBrowserHome = (ImageView) findViewById(R.id.imv_youtubeBrowserHome);
        this.imv_youtubeBrowserReload = (ImageView) findViewById(R.id.imv_youtubeBrowserReload);
        this.tv_youtubeBrowserTitle = (TextView) findViewById(R.id.tv_youtubeBrowserTitle);
        LinearProgressIndicator linearProgressIndicator = (LinearProgressIndicator) findViewById(R.id.linearProgress);
        this.linearProgressIndicator = linearProgressIndicator;
        linearProgressIndicator.setMax(100);
        this.imv_youtubeBack.setOnClickListener(this);
        this.imv_youtubeBrowserConnect.setOnClickListener(this);
        this.imv_youtubeBrowserHelp.setOnClickListener(this);
        this.imv_youtubeBrowserList.setOnClickListener(this);
        this.imv_youtubeBrowserBack.setOnClickListener(this);
        this.imv_youtubeBrowserForward.setOnClickListener(this);
        this.imv_youtubeBrowserHome.setOnClickListener(this);
        this.imv_youtubeBrowserReload.setOnClickListener(this);
        if (TVConnectUtils.getInstance().isConnected()) {
            this.imv_youtubeBrowserConnect.setImageResource(R.drawable.hover_screen);
        } else {
            this.imv_youtubeBrowserConnect.setImageResource(R.drawable.screen);
        }
    }

    public Unit actionCommon() {
        openActivityMediaPlayer();
        return Unit.INSTANCE;
    }

    public void openActivityMediaPlayer() {
        DialogBSFYoutube dialogBSFYoutube = this.dialogBSFYoutube;
        if (dialogBSFYoutube != null && dialogBSFYoutube.isAdded()) {
            this.dialogBSFYoutube.dismiss();
        }
        boolean booleanValue = ((Boolean) SharedPrefsUtil.getInstance().get(Const.KEY_TIER, Boolean.class)).booleanValue();
        if (this.youtubeDtoArrayList.size() <= 0 || this.youtubeDtoArrayList.size() <= this.currentPos) {
            return;
        }
        Intent intent = new Intent(this, PlayCastActivity.class);
        ManagerDataPlay.getInstance().titleCast = this.youtubeDtoArrayList.get(this.currentPos).getName();
        ManagerDataPlay.getInstance().pathCast = this.youtubeDtoArrayList.get(this.currentPos).getUrl();
        ManagerDataPlay.getInstance().setTypePlay(3);
        ManagerDataPlay.getInstance().thumbCast = this.youtubeDtoArrayList.get(this.currentPos).getThumbnail();
        ManagerDataPlay.getInstance().duration = Long.valueOf(this.youtubeDtoArrayList.get(this.currentPos).getDuration() * 1000);
        ManagerDataPlay.getInstance().setPosSelected(0);
        startActivity(intent);
        Utils.nextScreen(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_youtubeBack:
                onBackPressed();
                return;
            case R.id.imv_youtubeBrowserBack:
                WebView webView = this.page;
                if (!webView.canGoBack()) {
                    return;
                }
                webView.goBack();
                return;
            case R.id.imv_youtubeBrowserConnect:
                AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                    if (TVConnectUtils.getInstance().isConnected()) {
                        new DialogDisconnect(this).show();
                        return;
                    } else {
                        gotoConnect();
                        return;
                    }
                });

            case R.id.imv_youtubeBrowserForward:
                WebView webView2 = this.page;
                if (!webView2.canGoForward()) {
                    return;
                }
                webView2.goForward();
                return;
            case R.id.imv_youtubeBrowserHelp:
            default:
                return;
            case R.id.imv_youtubeBrowserHome:
                this.page.loadUrl(this.url_default);
                return;
            case R.id.imv_youtubeBrowserList:
                if (this.youtubeDtoArrayList.isEmpty()) {
                    new DialogNoResourcesFound(this, new DialogNoResourcesFound.RefreshListener() {
                        @Override
                        public void onClick() {
                            YoutubeBrowserActivity.pageReload = Boolean.TRUE;
                            YoutubeBrowserActivity.this.page.reload();
                        }
                    }).show();
                    return;
                }
                DialogBSFYoutube dialogBSFYoutube = this.dialogBSFYoutube;
                if (dialogBSFYoutube == null || dialogBSFYoutube.isAdded()) {
                    return;
                }
                this.dialogBSFYoutube.show(getSupportFragmentManager(), "YoutubeBrowserActivity");
                return;
            case R.id.imv_youtubeBrowserReload:
                pageReload = Boolean.TRUE;
                this.page.reload();
                return;
        }
    }

    public void gotoConnect() {
        startActivity(new Intent(this, ConnectActivity.class));
        Utils.nextScreen(this);
    }

    @Override
    public void onBackPressed() {
        AdsManager.CallInterstitialAdLoad(this, 1, () -> {
            super.onBackPressed();
        });

    }
}
