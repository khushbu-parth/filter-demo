package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.fragments;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mancj.materialsearchbar.MaterialSearchBar;


import org.alfonz.utility.NetworkUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.DisclaimerActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.WebApplication;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.databases.DBBookmarkHelper;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.databases.DBDownloadManager;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.listener.CustomListener;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.BookmarkItem;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities.MainActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities.SettingActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utility.DownloadFileUtility;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utility.IntentUtility;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utility.PermissionUtility;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.AdBlocker;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.AndroidUtils;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.Constants;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.NetworkUtil;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils.StoreUserData;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.web.ObservableScrollViewCallbacks;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.web.ObservableWebView;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.web.ScrollState;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.activity.FacebookActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.activity.GalleryActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.activity.InstagramActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.activity.TwitterActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.activity.WhatsappActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.vimeo.SearchActivity;


public class FragmentWeb extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, ObservableScrollViewCallbacks, MaterialSearchBar.OnSearchActionListener {
    public CheckBox fav;
    public Context mContext;
    public CustomListener mListener;
    public View mRootView;
    public ObservableWebView mWebView;
    public LinearLayout main_page_lin;
    public LinearLayout main_search_lin;
    public String valid_url = "";
    LinearLayout adsize;
    ImageView daily;
    ImageView face;
    ImageView inst;
    ImageView search;
    EditText search_url;
    ImageView setting;
    ImageView twitt;
    ImageView gallery;
    ImageView vim;
    ImageView whats;
    String CopyValue = "";
    String CopyKey = "";
    private Animation aniFade_in;
    private Animation aniFade_out;
    private TranslateAnimation anim;
    private AppBarLayout appBar;
    private ImageView browser_more;
    private ImageView browser_refresh;
    private ImageView browser_stop;
    private SwipeRefreshLayout contentSwipeRefreshLayout;
    private DBBookmarkHelper dbBookmarkHelper;
    private FloatingActionButton download_button;
    private String mUrl;
    private MaterialSearchBar searchBar;
    private StoreUserData storeUserData;
    private ImageView dailymotion, vimeo, vk, fc2, naver, youku, tumblr, metacafe, myspace;
    private TextView howuse;

    public void onDownMotionEvent() {
    }

    public void onScrollChanged(int i, boolean z, boolean z2) {
    }

    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @SuppressLint("ResourceType")
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mContext = layoutInflater.getContext();
        this.storeUserData = new StoreUserData(this.mContext);
        View inflate = layoutInflater.inflate(R.layout.fragmentweb, viewGroup, false);
        this.mRootView = inflate;


        if (getActivity().getIntent().getExtras() != null) {
            for (String str : getActivity().getIntent().getExtras().keySet()) {
                this.CopyKey = str;
                String string = getActivity().getIntent().getExtras().getString(this.CopyKey);
                if (this.CopyKey.equals("android.intent.extra.TEXT")) {
                    this.CopyValue = getActivity().getIntent().getExtras().getString(this.CopyKey);
                } else {
                    this.CopyValue = "";
                }
                callText(string);
            }
        }
        this.main_search_lin = (LinearLayout) this.mRootView.findViewById(R.id.main_search_lin);
        this.main_page_lin = (LinearLayout) this.mRootView.findViewById(R.id.main_page_lin);
        this.search_url = (EditText) this.mRootView.findViewById(R.id.search_url);
        this.setting = (ImageView) this.mRootView.findViewById(R.id.setting);
        this.search = (ImageView) this.mRootView.findViewById(R.id.search);
        this.face = (ImageView) this.mRootView.findViewById(R.id.face);
        this.inst = (ImageView) this.mRootView.findViewById(R.id.inst);
        this.vim = (ImageView) this.mRootView.findViewById(R.id.vim);
        this.daily = (ImageView) this.mRootView.findViewById(R.id.daily);
        this.twitt = (ImageView) this.mRootView.findViewById(R.id.twitt);
        this.whats = (ImageView) this.mRootView.findViewById(R.id.whats);
        this.gallery = (ImageView) this.mRootView.findViewById(R.id.gallery);
        howuse = (TextView) mRootView.findViewById(R.id.howuse);
        howuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DisclaimerActivity.class));

            }
        });
        dailymotion = (ImageView) this.mRootView.findViewById(R.id.dailymotion);
        vimeo = (ImageView) this.mRootView.findViewById(R.id.vimeo);
        vk = (ImageView) this.mRootView.findViewById(R.id.vk);
        fc2 = (ImageView) this.mRootView.findViewById(R.id.fc2);
        naver = (ImageView) this.mRootView.findViewById(R.id.naver);
        metacafe = (ImageView) this.mRootView.findViewById(R.id.metacafe);
        youku = (ImageView) this.mRootView.findViewById(R.id.youku);
        myspace = (ImageView) this.mRootView.findViewById(R.id.myspace);
        tumblr = (ImageView) this.mRootView.findViewById(R.id.tumblr);
        dailymotion.setOnClickListener(this);
        vimeo.setOnClickListener(this);
        vk.setOnClickListener(this);
        fc2.setOnClickListener(this);
        naver.setOnClickListener(this);
        metacafe.setOnClickListener(this);
        youku.setOnClickListener(this);
        myspace.setOnClickListener(this);
        tumblr.setOnClickListener(this);

        this.setting.setOnClickListener(this);
        this.search.setOnClickListener(this);
        this.face.setOnClickListener(this);
        this.inst.setOnClickListener(this);
        this.vim.setOnClickListener(this);
        this.daily.setOnClickListener(this);
        this.twitt.setOnClickListener(this);
        this.whats.setOnClickListener(this);
        this.gallery.setOnClickListener(this);
        this.mWebView = (ObservableWebView) this.mRootView.findViewById(R.id.fragment_main_webview);
        this.contentSwipeRefreshLayout = (SwipeRefreshLayout) this.mRootView.findViewById(R.id.container_content_swipeable);
        this.appBar = (AppBarLayout) this.mRootView.findViewById(R.id.browser_header);
        this.download_button = (FloatingActionButton) this.mRootView.findViewById(R.id.button_download);
        MaterialSearchBar materialSearchBar = (MaterialSearchBar) this.mRootView.findViewById(R.id.searchBar);
        this.searchBar = materialSearchBar;
        materialSearchBar.setOnSearchActionListener(this);
        this.dbBookmarkHelper = new DBBookmarkHelper(this.mContext);
        this.browser_refresh = (ImageView) this.mRootView.findViewById(R.id.browser_refresh);
        this.browser_more = (ImageView) this.mRootView.findViewById(R.id.browser_more);
        this.browser_stop = (ImageView) this.mRootView.findViewById(R.id.browser_stop);
        this.fav = (CheckBox) this.mRootView.findViewById(R.id.browser_like);
        this.aniFade_out = AnimationUtils.loadAnimation(this.mContext, 17432577);
        this.aniFade_in = AnimationUtils.loadAnimation(this.mContext, 17432576);
        this.aniFade_out.setDuration(300);
        this.aniFade_in.setDuration(300);
        this.browser_stop.setVisibility(View.GONE);
        this.download_button.hide();
        showDownloadBubble(false);
        setHasOptionsMenu(true);
        AppBarClick();
        this.mUrl = Constants.EngineHome(this.storeUserData.getSettings().getEngine());
        this.mWebView.setScrollViewCallbacks(this);
        this.download_button.setOnClickListener(this);
        this.fav.setOnClickListener(this);
        this.browser_more.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentWeb.this.showPopupMenu(view);
            }
        });
        Bundle arguments = getArguments();
        if (arguments != null) {
            updateUrl(arguments.getString("url"));
        }
        return this.mRootView;
    }

    private void callText(String str) {
        if (!str.contains("instagram.com")) {
            callInstaActivity();
        }
    }

    public void callWhatsappActivity() {

        startActivity(new Intent(getContext(), WhatsappActivity.class));


    }

    public void onSearchStateChanged(boolean z) {
        IconController(z ? Boolean.FALSE : Boolean.TRUE);
    }

    public void onSearchConfirmed(CharSequence charSequence) {
        String charSequence2 = charSequence.toString();
        this.searchBar.disableSearch();
        if (charSequence.length() > 0) {
            onclick(charSequence2);
            SearchLog(charSequence2);
        }
    }

    public void onButtonClicked(int i) {
        if (i != 1 && i == 3) {
            this.searchBar.disableSearch();
        }
    }

    public void onClick(View view) {
        String str = "";
        switch (view.getId()) {
            case R.id.browser_like:
                ObservableWebView observableWebView = this.mWebView;
                if (observableWebView != null && observableWebView.getUrl() != null) {
                    if (DBBookmarkHelper.getInstance(this.mContext).check_bookmark(this.mWebView.getUrl())) {
                        this.fav.setChecked(false);
                        DBBookmarkHelper.getInstance(this.mContext).DeleteBookmark(this.mWebView.getUrl());
                        show_snack(getString(R.string.bm_removed));
                        return;
                    }
                    this.fav.setChecked(true);
                    Bitmap favicon = this.mWebView.getFavicon();
                    File file = new File(this.mContext.getFilesDir().toString() + "/.thumbs");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    if (favicon != null) {
                        str = "BR" + System.currentTimeMillis() + ".png";
                        File file2 = new File(file, str);
                        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(favicon, 30, 30, true);
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file2);
                            createScaledBitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    this.dbBookmarkHelper.AddBookmark(new BookmarkItem(0, this.mWebView.getTitle(), this.mWebView.getUrl(), str, 0, 1));
                    show_snack(getString(R.string.bm_added));
                    return;
                }
                return;
            case R.id.button_download:
                ObservableWebView observableWebView2 = this.mWebView;
                if (observableWebView2 != null) {
                    str = observableWebView2.getTitle();
                }
                System.currentTimeMillis();
                try {
                    String fileNameFromURL = Constants.getFileNameFromURL(this.valid_url);
                    if (!NetworkUtil.isConnected()) {
                        show_snack(this.mContext.getString(R.string.nointernet));
                        return;
                    } else if (!NetworkUtil.getMobileConnectivityStatus(this.mContext)) {
                        ShowDownload(fileNameFromURL, this.valid_url, str);
                        return;
                    } else if (this.storeUserData.getSettings().isMobile()) {
                        ShowDownload(fileNameFromURL, this.valid_url, str);
                        return;
                    } else {
                        show_snack(this.mContext.getString(R.string.disabled_mobile));
                        return;
                    }
                } catch (Exception unused) {
                    show_snack(this.mContext.getString(R.string.nofile));
                    return;
                }
            case R.id.daily:
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));

                soicalClick("https://flickr.com/");
                return;
            case R.id.face:
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                callFacebookActivity();
                return;
            case R.id.inst:
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                callInstaActivity();
                return;
            case R.id.gallery:
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                callGalleryActivity();
                return;
            case R.id.search:
                soicalClick(this.search_url.getText().toString().trim());
                return;
            case R.id.setting:
                this.mContext.startActivity(new Intent(this.mContext, SettingActivity.class));
                return;
            case R.id.twitt:
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                callTwitterActivity();
                return;
            case R.id.vim:
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                soicalClick("https://veoh.com/");
                return;
            case R.id.dailymotion:
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                soicalClick("https://dailymotion.com");
                return;
            case R.id.vimeo:
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                startActivity(new Intent(getActivity(), SearchActivity.class));

                return;
            case R.id.vk:
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                soicalClick("https://m.vk.com");
                return;
            case R.id.fc2:
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                soicalClick("https://video.fc2.com");
                return;
            case R.id.naver:
                soicalClick("https://m.tv.naver.com");
                return;
            case R.id.metacafe:
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                soicalClick("https://www.metacafe.com");
                return;
            case R.id.youku:
                soicalClick("https://m.youku.com/");
                return;
            case R.id.myspace:
                soicalClick("https://myspace.com");
                return;
            case R.id.tumblr:
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                soicalClick("https://www.tumblr.com");
                return;
            case R.id.whats:
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewpush));
                callWhatsappActivity();
                return;
            default:
                return;
        }
    }

    public void callGalleryActivity() {
        startActivity(new Intent(getContext(), GalleryActivity.class));

    }

    public void callInstaActivity() {
        Intent intent = new Intent(getContext(), InstagramActivity.class);
        intent.putExtra("CopyIntent", CopyValue);
        startActivity(intent);
    }

    public void callTwitterActivity() {
        Intent intent = new Intent(getContext(), TwitterActivity.class);
        intent.putExtra("CopyIntent", CopyValue);
        startActivity(intent);

    }

    private void hideViews() {
        this.main_page_lin.setVisibility(View.GONE);
        this.main_search_lin.setVisibility(View.VISIBLE);
    }

    public void hideKeyBoard() {
        try {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void soicalClick(String str) {
        hideKeyBoard();
        hideViews();
        Search_Bar(str);
        this.searchBar.disableSearch();
        onclick(str);
        SearchLog(str);
    }

    private void AppBarClick() {
        this.browser_refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentWeb.this.Refresh();
            }
        });
        this.browser_stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentWeb.this.Stop();
            }
        });
    }

    public boolean CheckForward() {
        ObservableWebView observableWebView = this.mWebView;
        if (observableWebView != null) {
            return observableWebView.canGoForward();
        }
        return false;
    }

    public void ControlForward() {
        try {
            if (this.mWebView.canGoForward()) {
                this.mWebView.goForward();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Fav(String str) {
        CheckBox checkBox;
        boolean z;
        if (DBBookmarkHelper.getInstance(this.mContext).check_bookmark(str)) {
            checkBox = this.fav;
            z = true;
        } else {
            checkBox = this.fav;
            z = false;
        }
        checkBox.setChecked(z);
    }

    private void IconController(Boolean bool) {
        ImageView imageView;
        Animation animation;
        if (bool.booleanValue()) {
            this.browser_stop.setVisibility(View.GONE);
            this.browser_refresh.setVisibility(View.VISIBLE);
            this.browser_more.setVisibility(View.VISIBLE);
            this.fav.setVisibility(View.GONE);
            imageView = this.browser_refresh;
            animation = this.aniFade_in;
        } else {
            this.browser_stop.setVisibility(View.GONE);
            this.browser_refresh.setVisibility(View.GONE);
            this.fav.setVisibility(View.GONE);
            this.browser_more.setVisibility(View.GONE);
            imageView = this.browser_refresh;
            animation = this.aniFade_out;
        }
        imageView.startAnimation(animation);
    }

    public void callFacebookActivity() {
        Intent intent = new Intent(getContext(), FacebookActivity.class);
        intent.putExtra("CopyIntent", CopyValue);
        startActivity(intent);
    }

    public void Refresh() {
        ObservableWebView observableWebView = this.mWebView;
        if (observableWebView != null) {
            observableWebView.reload();
            ShowStop();
            showProgress(true);
        }
    }

    private void SearchLog(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("Device_id", AndroidUtils.firebase_id(getContext()));
        bundle.putString("search_term", str);
    }

    public void Search_Bar(String str) {
        this.searchBar.setText(str);
        if (this.mWebView != null) {
            this.searchBar.setPlaceHolder(str);
        }
    }

    private void ShowDownload(String str, String str2, String str3) {
        String str4 = "VID_" + System.currentTimeMillis() + ".mp4";
        if (!check_file(str) || !DBDownloadManager.getInstance(this.mContext).hasObject(str)) {
            str = str4;
        }
        int maximumDownload = new StoreUserData(this.mContext).getSettings().getMaximumDownload();
        File file = new File(WebApplication.getInstance().downloadService.getRoot_Path(str));
        if (str.isEmpty() || !DownloadFileUtility.check_extension(str)) {
            Toast.makeText(this.mContext, MainActivity.getInstance().ErrorHandler(1), Toast.LENGTH_SHORT).show();
        } else if (DBDownloadManager.getInstance(this.mContext).getCurrentDownloadingCount() >= maximumDownload) {
            Toast.makeText(this.mContext, MainActivity.getInstance().ErrorHandler(2), Toast.LENGTH_SHORT).show();
        } else if (!check_file(str) || !DBDownloadManager.getInstance(this.mContext).hasObject(str)) {
            Toast.makeText(this.mContext, MainActivity.getInstance().ErrorHandler(0), Toast.LENGTH_SHORT).show();
        } else {
            WebApplication.getInstance().downloadService.addDownloadData(str2, "", str, file.getAbsolutePath());
            try {
                sndData(str3, str, new URL(str2).getHost());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
//                    if (Glob.flag == 2) {
//                        Glob.flag = 0;
                    MainActivity.getInstance().ShowProgress();
                    MainActivity.getInstance().bottomNavigationView.getMenu().getItem(1).setChecked(true);
//                    } else if (SplashActivity.mInterstitialAdMob == null || !SplashActivity.mInterstitialAdMob.isLoaded()) {
//                        MainActivity.getInstance().ShowProgress();
//                        MainActivity.getInstance().bottomNavigationView.getMenu().getItem(1).setChecked(true);
//                    } else {
//                        SplashActivity.showAdmobInterstitial();
//                        Glob.flag = 1;
//                    }
                }
            }, 500);
        }
    }

    public void sndData(String str, String str2, String str3) {
        Bundle bundle = new Bundle();
        bundle.putString("Device_id", AndroidUtils.firebase_id(getContext()));
        bundle.putString("item_name", str2);
        bundle.putString("Title", str);
        bundle.putString("Url", str3);
    }

    public boolean check_file(String str) {
        return !new File(new File(WebApplication.getInstance().downloadService.getRoot_Path(str)).getAbsolutePath()).exists();
    }

    private void ShowRefresh() {
        this.browser_refresh.setVisibility(View.VISIBLE);
        this.browser_stop.setVisibility(View.GONE);
    }

    private void ShowStop() {
        this.browser_refresh.setVisibility(View.GONE);
        this.browser_stop.setVisibility(View.VISIBLE);
    }

    public void Stop() {
        ObservableWebView observableWebView = this.mWebView;
        if (observableWebView != null) {
            observableWebView.stopLoading();
            ShowRefresh();
        }
    }

    public void copyToClipboard(String str) {
        try {
            ((ClipboardManager) this.mContext.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Link", str));
            show_snack(getString(R.string.copylink));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isLinkExternal(String str) {
        for (String contains : Constants.LINKS_OPENED_IN_EXTERNAL_BROWSER) {
            if (str.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    public boolean isLinkInternal(String str) {
        for (String contains : Constants.LINKS_OPENED_IN_INTERNAL_WEBVIEW) {
            if (str.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    private void loadData() {
        if (NetworkUtility.isOnline(this.mContext)) {
            showProgress(true);
            String str = this.mUrl;
            if (str != null) {
                this.mWebView.loadUrl("javascript:document.open();document.close();");
                this.mWebView.loadUrl(str);
                Fav(this.mUrl);
                showDownloadBubble(false);
            }
        }
    }

    private void onclick(String str) {
        String lowerCase = str.toLowerCase();
        if (!AndroidUtils.isValidUrl(str)) {
            lowerCase = Constants.SearchQueries(this.storeUserData.getSettings().getEngine()) + lowerCase;
        } else if (!str.toLowerCase(Locale.ENGLISH).contains("http")) {
            lowerCase = "http://".concat(str);
        }
        if (!lowerCase.contains("youtube.com")) {
            this.mWebView.loadUrl(lowerCase);
        } else {
            MainActivity.getInstance().show_blocked_url();
        }
    }

    private void setupSwipeRefreshLayout() {
        if (this.mUrl != null || this.mWebView.getUrl() != null) {
            ((SwipeRefreshLayout) this.mRootView.findViewById(R.id.container_content_swipeable)).setOnRefreshListener(this);
        }
    }

    @SuppressLint("WrongConstant")
    private void setupView() {
        this.mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.4; sdk Build/KRT16L) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.getSettings().setAppCacheEnabled(true);
        this.mWebView.getSettings().setAppCachePath(this.mContext.getCacheDir().getAbsolutePath());
        this.mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        this.mWebView.getSettings().setDomStorageEnabled(true);
        this.mWebView.getSettings().setDatabaseEnabled(true);
        this.mWebView.getSettings().setGeolocationEnabled(true);
        this.mWebView.getSettings().setSupportZoom(true);
        this.mWebView.getSettings().setBuiltInZoomControls(false);
        this.mWebView.setScrollBarStyle(33554432);
        this.mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, (Paint) null);
        this.mWebView.setWebViewClient(new MyWebViewClient(this, this));
        this.mWebView.requestFocus(130);
    }

    public void showDownloadBubble(boolean z) {
        if (z) {
            this.download_button.show();
            if (this.anim == null) {
                TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 15.0f);
                this.anim = translateAnimation;
                translateAnimation.setRepeatCount(-1);
                this.anim.setRepeatMode(2);
                this.anim.setDuration(100);
                this.anim.setFillAfter(true);
                this.download_button.startAnimation(this.anim);
                return;
            }
            return;
        }
        TranslateAnimation translateAnimation2 = this.anim;
        if (translateAnimation2 != null && translateAnimation2.isInitialized()) {
            this.anim.cancel();
            this.anim = null;
        }
    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(this.mContext, R.style.AppTheme), view);
        try {
            Field[] declaredFields = popupMenu.getClass().getDeclaredFields();
            int length = declaredFields.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Field field = declaredFields[i];
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object obj = field.get(popupMenu);
                    Class.forName(obj.getClass().getName()).getMethod("setForceShowIcon", new Class[]{Boolean.TYPE}).invoke(obj, new Object[]{Boolean.TRUE});
                    break;
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popupMenu.getMenuInflater().inflate(R.menu.browser, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId != R.id.brshare) {
                    switch (itemId) {
                        case R.id.brback:
                            if (!FragmentWeb.this.CheckBack()) {
                                return true;
                            }
                            FragmentWeb.this.ControlBack();
                            return true;
                        case R.id.brbookmark:
                            FragmentWeb.this.mListener.BrToHome();
                            return true;
                        case R.id.brcopy:
                            try {
                                if (FragmentWeb.this.mWebView.getUrl() == null) {
                                    return true;
                                }
                                FragmentWeb.this.copyToClipboard(FragmentWeb.this.mWebView.getUrl());
                                return true;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return true;
                            }
                        case R.id.brforward:
                            if (!FragmentWeb.this.CheckForward()) {
                                return true;
                            }
                            FragmentWeb.this.ControlForward();
                            return true;
                        default:
                            return true;
                    }
                } else if (FragmentWeb.this.mWebView == null || FragmentWeb.this.mWebView.getUrl().isEmpty()) {
                    return true;
                } else {
                    IntentUtility.Share_Url(FragmentWeb.this.mContext, FragmentWeb.this.mWebView.getTitle(), FragmentWeb.this.mWebView.getUrl());
                    return true;
                }
            }
        });
        popupMenu.show();
    }

    public void showProgress(boolean z) {
        try {
            if (this.mUrl != null || this.mWebView.getUrl() != null) {
                this.contentSwipeRefreshLayout.setRefreshing(z);
                if (z) {
                    this.browser_stop.setVisibility(View.VISIBLE);
                    this.browser_refresh.setVisibility(View.GONE);
                    return;
                }
                this.browser_stop.setVisibility(View.GONE);
                this.browser_refresh.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void show_snack(String str) {
        Snackbar.make(this.mRootView.findViewById(R.id.web_container), (CharSequence) str, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    private void startAlphaAnimation(View view, int i) {
        AlphaAnimation alphaAnimation = i == 0 ? new AlphaAnimation(0.0f, 1.0f) : new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        view.startAnimation(alphaAnimation);
    }

    public boolean CheckBack() {
        ObservableWebView observableWebView = this.mWebView;
        if (observableWebView != null) {
            return observableWebView.canGoBack();
        }
        return false;
    }

    public void ControlBack() {
        try {
            if (this.mWebView.canGoBack()) {
                this.mWebView.goBack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (bundle != null) {
            this.mWebView.restoreState(bundle);
        }
        setupView();
        setupSwipeRefreshLayout();
        PermissionUtility.checkPermissionAccessLocation(this);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (CustomListener) context;
        } catch (ClassCastException unused) {
            throw new ClassCastException(context.toString() + " must implement BackControllerListener");
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        DBDownloadManager.getInstance(this.mContext);
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    @SuppressLint("WrongConstant")
    public void onRefresh() {
        if (NetworkUtility.isOnline(this.mContext)) {
            showProgress(true);
            String url = this.mWebView.getUrl();
            if (url == null || url.equals("")) {
                url = this.mUrl;
            }
            this.mWebView.loadUrl(url);
            return;
        }
        showProgress(false);
        Toast.makeText(getActivity(), this.mContext.getString(R.string.nointernet), Toast.LENGTH_LONG).show();
    }

    public void updateUrl(String str) {
        this.mUrl = str;
        showDownloadBubble(false);
        loadData();
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.mRootView = null;
    }

    class MyWebViewClient extends WebViewClient {
        private final Map<String, Boolean> loadedUrls;
        private boolean mSuccess;

        private MyWebViewClient() {
            this.mSuccess = true;
            this.loadedUrls = new HashMap();
        }

        MyWebViewClient(FragmentWeb fragmentWeb, FragmentWeb fragmentWeb2) {
            this();
        }

        public void onLoadResource(WebView webView, String str) {
            try {
                if (webView.getUrl() == null) {
                    FragmentWeb.this.showDownloadBubble(false);
                } else if (DownloadFileUtility.check_domains(str)) {
                    Log.e("Domain Blocked", str);
                } else {
                    if (DownloadFileUtility.isCheckUrl(str).contains("novideo")) {
                        if (!FragmentWeb.this.valid_url.contains(str)) {
                            FragmentWeb.this.showDownloadBubble(false);
                            return;
                        }
                    }
                    FragmentWeb.this.valid_url = str;
                    FragmentWeb.this.showDownloadBubble(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void doUpdateVisitedHistory(WebView webView, String str, boolean z) {
            super.doUpdateVisitedHistory(webView, str, z);
            webView.clearCache(true);
        }

        public void onPageFinished(WebView webView, String str) {
            try {
                if (FragmentWeb.this.getActivity() != null && this.mSuccess) {
                    FragmentWeb.this.showProgress(false);
                }
                this.mSuccess = true;
                if (webView.getUrl() != null) {
                    FragmentWeb.this.Search_Bar(webView.getUrl());
                }
            } catch (Exception e) {
                Log.d("=====", "onPageFinished: Exception " + e.getMessage());
                e.printStackTrace();
            }
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
            try {
                if (webView.getUrl() != null) {
                    FragmentWeb.this.Search_Bar(webView.getUrl());
                    if (DBBookmarkHelper.getInstance(FragmentWeb.this.mContext).check_bookmark(webView.getUrl())) {
                        FragmentWeb.this.fav.setChecked(true);
                    } else {
                        FragmentWeb.this.fav.setChecked(false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            try {
                if (FragmentWeb.this.getActivity() != null) {
                    this.mSuccess = false;
                    FragmentWeb.this.showProgress(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            Log.d("=====", "onReceivedError 23: ");
            try {
                onReceivedError(webView, webResourceError.getErrorCode(), webResourceError.getDescription().toString(), webResourceRequest.getUrl().toString());
            } catch (Exception e) {
                Log.d("=====", "onReceivedError: Exception " + e.getMessage());
                e.printStackTrace();
            }
        }

        public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
            boolean z;
            if (!this.loadedUrls.containsKey(str)) {
                z = AdBlocker.isAd(str);
                this.loadedUrls.put(str, Boolean.valueOf(z));
            } else {
                z = this.loadedUrls.get(str).booleanValue();
            }
            return z ? AdBlocker.createEmptyResource() : super.shouldInterceptRequest(webView, str);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (str != null) {
                try {
                    if (str.startsWith("http://") || str.startsWith("https://")) {
                        if (!str.contains("youtube.com")) {
                            boolean isLinkExternal = FragmentWeb.this.isLinkExternal(str);
                            boolean isLinkInternal = FragmentWeb.this.isLinkInternal(str);
                            if (!isLinkExternal && !isLinkInternal) {
                                isLinkExternal = false;
                            }
                            if (isLinkExternal) {
                                return true;
                            }
                            FragmentWeb.this.showProgress(true);
                            return false;
                        }
                        MainActivity.getInstance().show_blocked_url();
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return IntentUtility.startIntentActivity(FragmentWeb.this.getContext(), str);
        }
    }

}
