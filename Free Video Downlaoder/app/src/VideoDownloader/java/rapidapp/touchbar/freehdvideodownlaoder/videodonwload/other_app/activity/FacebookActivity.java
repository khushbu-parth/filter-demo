package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.api.CommonClassForAPI;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.databinding.ActivityFacebookBinding;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.AppLangSessionManager;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.SharePrefs;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.Utils;

public class FacebookActivity extends AppCompatActivity {
    static final boolean $assertionsDisabled = false;
    FacebookActivity activity;
    AppLangSessionManager appLangSessionManager;
    ActivityFacebookBinding binding;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;
    private ClipboardManager clipBoard;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = (ActivityFacebookBinding) DataBindingUtil.setContentView(this, R.layout.activity_facebook);
        getWindow().setFlags(1024, 1024);
        this.activity = this;
        this.appLangSessionManager = new AppLangSessionManager(this.activity);
        setLocale(this.appLangSessionManager.getLanguage());
        this.commonClassForAPI = CommonClassForAPI.getInstance(this.activity);
        Utils.createFileFolder();
        initViews();
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
                FacebookActivity.this.onBackPressed();
            }
        });
        this.binding.imInfo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                FacebookActivity.this.binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
            }
        });
        Glide.with((FragmentActivity) this.activity).load(Integer.valueOf((int) R.drawable.fb1)).into(this.binding.layoutHowTo.imHowto1);
        Glide.with((FragmentActivity) this.activity).load(Integer.valueOf((int) R.drawable.fb2)).into(this.binding.layoutHowTo.imHowto2);
        Glide.with((FragmentActivity) this.activity).load(Integer.valueOf((int) R.drawable.fb3)).into(this.binding.layoutHowTo.imHowto3);
        Glide.with((FragmentActivity) this.activity).load(Integer.valueOf((int) R.drawable.fb4)).into(this.binding.layoutHowTo.imHowto4);
        this.binding.layoutHowTo.tvHowTo1.setText(getResources().getString(R.string.opn_fb));
        this.binding.layoutHowTo.tvHowTo3.setText(getResources().getString(R.string.copy_video_link_frm_fb));
        if (!SharePrefs.getInstance(this.activity).getBoolean(SharePrefs.ISSHOWHOWTOFB).booleanValue()) {
            SharePrefs.getInstance(this.activity).putBoolean(SharePrefs.ISSHOWHOWTOFB, true);
            this.binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
        } else {
            this.binding.layoutHowTo.LLHowToLayout.setVisibility(View.GONE);
        }
        this.binding.loginBtn1.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                FacebookActivity.this.lambda$initViews$0$FacebookActivity(view);
            }
        });
        this.binding.tvPaste.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                FacebookActivity.this.lambda$initViews$1$FacebookActivity(view);
            }
        });
        this.binding.LLOpenFacebbook.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                FacebookActivity.this.lambda$initViews$2$FacebookActivity(view);
            }
        });
    }

    public void lambda$initViews$0$FacebookActivity(View view) {
        String obj = this.binding.etText.getText().toString();
        if (obj.equals("")) {
            Utils.setToast(this.activity, getResources().getString(R.string.enter_url));
        } else if (!Patterns.WEB_URL.matcher(obj).matches()) {
            Utils.setToast(this.activity, getResources().getString(R.string.enter_valid_url));
        } else {
            GetFacebookData();
        }
    }

    public void lambda$initViews$1$FacebookActivity(View view) {
        PasteText();
    }

    public void lambda$initViews$2$FacebookActivity(View view) {
        Utils.OpenApp(this.activity, "com.facebook.katana");
    }

    private void GetFacebookData() {
        try {
            Utils.createFileFolder();
            String host = new URL(this.binding.etText.getText().toString()).getHost();
            Log.e("initViews: ", host);
            if (host.contains("facebook.com")) {
                Utils.showProgressDialog(this.activity);
                new callGetFacebookData().execute(this.binding.etText.getText().toString());
                return;
            }
            Utils.setToast(this.activity, getResources().getString(R.string.enter_valid_url));
        } catch (Exception e) {
            e.printStackTrace();
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
                        if (itemAt.getText().toString().contains("facebook.com")) {
                            this.binding.etText.setText(itemAt.getText().toString());
                        }
                    } else if (this.clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("facebook.com")) {
                        this.binding.etText.setText(this.clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }
                }
            } else if (stringExtra.contains("facebook.com")) {
                this.binding.etText.setText(stringExtra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilenameFromURL(String str) {
        try {
            return new File(new URL(str).getPath()).getName() + ".mp4";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
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

    public class callGetFacebookData extends AsyncTask<String, Void, Document> {
        Document facebookDoc;

        callGetFacebookData() {
        }

        public void onPreExecute() {
            super.onPreExecute();
        }

        public Document doInBackground(String... strArr) {
            try {
                this.facebookDoc = Jsoup.connect(strArr[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("ContentValues", "doInBackground: Error");
            }
            return this.facebookDoc;
        }

        public void onPostExecute(Document document) {
            Utils.hideProgressDialog(FacebookActivity.this.activity);
            try {
                FacebookActivity.this.VideoUrl = document.select("meta[property=\"og:video\"]").last().attr("content");
                Log.e("onPostExecute: ", FacebookActivity.this.VideoUrl);
                if (!FacebookActivity.this.VideoUrl.equals("")) {
                    try {
                        Utils.startDownload(FacebookActivity.this.VideoUrl, Utils.RootDirectoryFacebook, FacebookActivity.this.activity, FacebookActivity.this.getFilenameFromURL(FacebookActivity.this.VideoUrl));
                        FacebookActivity.this.VideoUrl = "";
                        FacebookActivity.this.binding.etText.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (NullPointerException e2) {
                e2.printStackTrace();
            }
        }
    }
}