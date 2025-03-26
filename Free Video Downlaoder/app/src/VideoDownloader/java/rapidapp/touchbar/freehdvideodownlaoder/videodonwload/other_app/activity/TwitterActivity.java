package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import io.reactivex.observers.DisposableObserver;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.api.CommonClassForAPI;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.databinding.ActivityTwitterBinding;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model.TwitterResponse;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.AppLangSessionManager;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.SharePrefs;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.Utils;

public class TwitterActivity extends AppCompatActivity {
    static final boolean $assertionsDisabled = false;
    TwitterActivity activity;
    AppLangSessionManager appLangSessionManager;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;
    private ActivityTwitterBinding binding;
    private ClipboardManager clipBoard;
    private DisposableObserver<TwitterResponse> observer = new DisposableObserver<TwitterResponse>() {

        public void onNext(TwitterResponse twitterResponse) {
            Utils.hideProgressDialog(TwitterActivity.this.activity);
            try {
                TwitterActivity.this.VideoUrl = twitterResponse.getVideos().get(0).getUrl();
                if (twitterResponse.getVideos().get(0).getType().equals("image")) {
                    Utils.startDownload(TwitterActivity.this.VideoUrl, Utils.RootDirectoryTwitter, TwitterActivity.this.activity, TwitterActivity.this.getFilenameFromURL(TwitterActivity.this.VideoUrl, "image"));
                    TwitterActivity.this.binding.etText.setText("");
                    return;
                }
                TwitterActivity.this.VideoUrl = twitterResponse.getVideos().get(twitterResponse.getVideos().size() - 1).getUrl();
                Utils.startDownload(TwitterActivity.this.VideoUrl, Utils.RootDirectoryTwitter, TwitterActivity.this.activity, TwitterActivity.this.getFilenameFromURL(TwitterActivity.this.VideoUrl, "mp4"));
                TwitterActivity.this.binding.etText.setText("");
            } catch (Exception e) {
                e.printStackTrace();
                Utils.setToast(TwitterActivity.this.activity, TwitterActivity.this.getResources().getString(R.string.no_media_on_tweet));
            }
        }

        @Override
        public void onError(Throwable th) {
            Utils.hideProgressDialog(TwitterActivity.this.activity);
            th.printStackTrace();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(TwitterActivity.this.activity);
        }
    };

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = (ActivityTwitterBinding) DataBindingUtil.setContentView(this, R.layout.activity_twitter);
        getWindow().setFlags(1024, 1024);
        this.activity = this;
        this.commonClassForAPI = CommonClassForAPI.getInstance(this.activity);
        Utils.createFileFolder();
        initViews();
        this.appLangSessionManager = new AppLangSessionManager(this.activity);
        setLocale(this.appLangSessionManager.getLanguage());
    }

    @Override
    public void onResume() {
        super.onResume();
        this.activity = this;
        this.clipBoard = (ClipboardManager) this.activity.getSystemService(Context.CLIPBOARD_SERVICE);
        PasteText();
    }

    private void initViews() {
        this.clipBoard = (ClipboardManager) this.activity.getSystemService(Context.CLIPBOARD_SERVICE);
        this.binding.imBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                TwitterActivity.this.onBackPressed();
            }
        });
        this.binding.imInfo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                TwitterActivity.this.binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
            }
        });
        Glide.with((FragmentActivity) this.activity).load(Integer.valueOf((int) R.drawable.tw1)).into(this.binding.layoutHowTo.imHowto1);
        Glide.with((FragmentActivity) this.activity).load(Integer.valueOf((int) R.drawable.tw2)).into(this.binding.layoutHowTo.imHowto2);
        Glide.with((FragmentActivity) this.activity).load(Integer.valueOf((int) R.drawable.tw3)).into(this.binding.layoutHowTo.imHowto3);
        Glide.with((FragmentActivity) this.activity).load(Integer.valueOf((int) R.drawable.tw4)).into(this.binding.layoutHowTo.imHowto4);
        this.binding.layoutHowTo.tvHowTo1.setText(getResources().getString(R.string.open_twitter));
        this.binding.layoutHowTo.tvHowTo3.setText(getResources().getString(R.string.open_twitter));
        if (!SharePrefs.getInstance(this.activity).getBoolean(SharePrefs.ISSHOWHOWTOTWITTER).booleanValue()) {
            SharePrefs.getInstance(this.activity).putBoolean(SharePrefs.ISSHOWHOWTOTWITTER, true);
            this.binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
        } else {
            this.binding.layoutHowTo.LLHowToLayout.setVisibility(View.GONE);
        }
        this.binding.loginBtn1.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                TwitterActivity.this.lambda$initViews$0$TwitterActivity(view);
            }
        });
        this.binding.tvPaste.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                TwitterActivity.this.lambda$initViews$1$TwitterActivity(view);
            }
        });
        this.binding.LLOpenTwitter.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                TwitterActivity.this.lambda$initViews$2$TwitterActivity(view);
            }
        });
    }

    public void lambda$initViews$0$TwitterActivity(View view) {
        String obj = this.binding.etText.getText().toString();
        if (obj.equals("")) {
            Utils.setToast(this.activity, getResources().getString(R.string.enter_url));
        } else if (!Patterns.WEB_URL.matcher(obj).matches()) {
            Utils.setToast(this.activity, getResources().getString(R.string.enter_valid_url));
        } else {
            Utils.showProgressDialog(this.activity);
            GetTwitterData();
        }
    }

    public void lambda$initViews$1$TwitterActivity(View view) {
        PasteText();
    }

    public void lambda$initViews$2$TwitterActivity(View view) {
        Utils.OpenApp(this.activity, "com.twitter.android");
    }

    private void GetTwitterData() {
        try {
            Utils.createFileFolder();
            if (new URL(this.binding.etText.getText().toString()).getHost().contains("twitter.com")) {
                Long tweetId = getTweetId(this.binding.etText.getText().toString());
                if (tweetId != null) {
                    callGetTwitterData(String.valueOf(tweetId));
                    return;
                }
                return;
            }
            Utils.setToast(this.activity, getResources().getString(R.string.enter_url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Long getTweetId(String str) {
        try {
            return Long.valueOf(Long.parseLong(str.split("\\/")[5].split("\\?")[0]));
        } catch (Exception e) {
            Log.d("TAG", "getTweetId: " + e.getLocalizedMessage());
            return null;
        }
    }

    private void PasteText() {
        try {
            this.binding.etText.setText("");
            String stringExtra = getIntent().getStringExtra("CopyIntent");
            if (stringExtra.equals("")) {
                if (this.clipBoard.hasPrimaryClip()) {
                    if (this.clipBoard.getPrimaryClipDescription().hasMimeType("text/plain")) {
                        ClipData.Item itemAt = this.clipBoard.getPrimaryClip().getItemAt(0);
                        if (itemAt.getText().toString().contains("twitter.com")) {
                            this.binding.etText.setText(itemAt.getText().toString());
                        }
                    } else if (this.clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("twitter.com")) {
                        this.binding.etText.setText(this.clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }
                }
            } else if (stringExtra.contains("twitter.com")) {
                this.binding.etText.setText(stringExtra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callGetTwitterData(String str) {
        try {
            if (!new Utils(this.activity).isNetworkAvailable()) {
                Utils.setToast(this.activity, getResources().getString(R.string.no_net_conn));
            } else if (this.commonClassForAPI != null) {
                Utils.showProgressDialog(this.activity);
                this.commonClassForAPI.callTwitterApi(this.observer, "https://twittervideodownloaderpro.com/twittervideodownloadv2/index.php", str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilenameFromURL(String str, String str2) {
        if (str2.equals("image")) {
            try {
                return new File(new URL(str).getPath()).getName() + "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return System.currentTimeMillis() + ".jpg";
            }
        } else {
            try {
                return new File(new URL(str).getPath()).getName() + "";
            } catch (MalformedURLException e2) {
                e2.printStackTrace();
                return System.currentTimeMillis() + ".mp4";
            }
        }
    }

    public void setLocale(String str) {
        Locale locale = new Locale(str);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
    }
}