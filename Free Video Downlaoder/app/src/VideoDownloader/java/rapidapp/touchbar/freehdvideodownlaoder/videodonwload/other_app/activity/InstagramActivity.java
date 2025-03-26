package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import io.reactivex.observers.DisposableObserver;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.adapter.StoriesListAdapter;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.adapter.UserListAdapter;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.api.CommonClassForAPI;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.databinding.ActivityInstagramBinding;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.interfaces.UserListInterface;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model.Edge;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model.EdgeSidecarToChildren;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model.ResponseModel;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model.story.FullDetailModel;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model.story.StoryModel;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model.story.TrayModel;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.AppLangSessionManager;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.SharePrefs;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.Utils;

public class InstagramActivity extends AppCompatActivity implements UserListInterface {
    static final boolean $assertionsDisabled = false;
    AppLangSessionManager appLangSessionManager;
    CommonClassForAPI commonClassForAPI;
    Context context;
    StoriesListAdapter storiesListAdapter;
    UserListAdapter userListAdapter;
    private String PhotoUrl;
    private String VideoUrl;
    private InstagramActivity activity;
    private ActivityInstagramBinding binding;
    private ClipboardManager clipBoard;
    private DisposableObserver<JsonObject> instaObserver = new DisposableObserver<JsonObject>() {

        public void onNext(JsonObject jsonObject) {
            Utils.hideProgressDialog(InstagramActivity.this.activity);
            try {
                Log.e("onNext: ", jsonObject.toString());
                ResponseModel responseModel = (ResponseModel) new Gson().fromJson(jsonObject.toString(), new TypeToken<ResponseModel>() {
                }.getType());
                EdgeSidecarToChildren edge_sidecar_to_children = responseModel.getGraphql().getShortcode_media().getEdge_sidecar_to_children();
                if (edge_sidecar_to_children != null) {
                    List<Edge> edges = edge_sidecar_to_children.getEdges();
                    for (int i = 0; i < edges.size(); i++) {
                        if (edges.get(i).getNode().isIs_video()) {
                            InstagramActivity.this.VideoUrl = edges.get(i).getNode().getVideo_url();
                            Utils.startDownload(InstagramActivity.this.VideoUrl, Utils.RootDirectoryInsta, InstagramActivity.this.activity, InstagramActivity.this.getVideoFilenameFromURL(InstagramActivity.this.VideoUrl));
                            InstagramActivity.this.binding.etText.setText("");
                            InstagramActivity.this.VideoUrl = "";
                        } else {
                            InstagramActivity.this.PhotoUrl = edges.get(i).getNode().getDisplay_resources().get(edges.get(i).getNode().getDisplay_resources().size() - 1).getSrc();
                            Utils.startDownload(InstagramActivity.this.PhotoUrl, Utils.RootDirectoryInsta, InstagramActivity.this.activity, InstagramActivity.this.getImageFilenameFromURL(InstagramActivity.this.PhotoUrl));
                            InstagramActivity.this.PhotoUrl = "";
                            InstagramActivity.this.binding.etText.setText("");
                        }
                    }
                } else if (responseModel.getGraphql().getShortcode_media().isIs_video()) {
                    InstagramActivity.this.VideoUrl = responseModel.getGraphql().getShortcode_media().getVideo_url();
                    Utils.startDownload(InstagramActivity.this.VideoUrl, Utils.RootDirectoryInsta, InstagramActivity.this.activity, InstagramActivity.this.getVideoFilenameFromURL(InstagramActivity.this.VideoUrl));
                    InstagramActivity.this.VideoUrl = "";
                    InstagramActivity.this.binding.etText.setText("");
                } else {
                    InstagramActivity.this.PhotoUrl = responseModel.getGraphql().getShortcode_media().getDisplay_resources().get(responseModel.getGraphql().getShortcode_media().getDisplay_resources().size() - 1).getSrc();
                    Utils.startDownload(InstagramActivity.this.PhotoUrl, Utils.RootDirectoryInsta, InstagramActivity.this.activity, InstagramActivity.this.getImageFilenameFromURL(InstagramActivity.this.PhotoUrl));
                    InstagramActivity.this.PhotoUrl = "";
                    InstagramActivity.this.binding.etText.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable th) {
            Utils.hideProgressDialog(InstagramActivity.this.activity);
            th.printStackTrace();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(InstagramActivity.this.activity);
        }
    };
    private DisposableObserver<FullDetailModel> storyDetailObserver = new DisposableObserver<FullDetailModel>() {

        public void onNext(FullDetailModel fullDetailModel) {
            InstagramActivity.this.binding.RVUserList.setVisibility(View.VISIBLE);
            InstagramActivity.this.binding.prLoadingBar.setVisibility(View.GONE);
            try {
                InstagramActivity.this.storiesListAdapter = new StoriesListAdapter(InstagramActivity.this.activity, fullDetailModel.getReel_feed().getItems());
                InstagramActivity.this.binding.RVStories.setAdapter(InstagramActivity.this.storiesListAdapter);
                InstagramActivity.this.storiesListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable th) {
            InstagramActivity.this.binding.prLoadingBar.setVisibility(View.GONE);
            th.printStackTrace();
        }

        @Override
        public void onComplete() {
            InstagramActivity.this.binding.prLoadingBar.setVisibility(View.GONE);
        }
    };
    private DisposableObserver<StoryModel> storyObserver = new DisposableObserver<StoryModel>() {

        public void onNext(StoryModel storyModel) {
            InstagramActivity.this.binding.RVUserList.setVisibility(View.VISIBLE);
            InstagramActivity.this.binding.prLoadingBar.setVisibility(View.GONE);
            try {
                InstagramActivity.this.userListAdapter = new UserListAdapter(InstagramActivity.this.activity, storyModel.getTray(), InstagramActivity.this.activity);
                InstagramActivity.this.binding.RVUserList.setAdapter(InstagramActivity.this.userListAdapter);
                InstagramActivity.this.userListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable th) {
            InstagramActivity.this.binding.prLoadingBar.setVisibility(View.GONE);
            th.printStackTrace();
        }

        @Override
        public void onComplete() {
            InstagramActivity.this.binding.prLoadingBar.setVisibility(View.GONE);
        }
    };

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = (ActivityInstagramBinding) DataBindingUtil.setContentView(this, R.layout.activity_instagram);
        getWindow().setFlags(1024, 1024);
        this.activity = this;
        this.context = this;
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
        this.context = this;
        this.clipBoard = (ClipboardManager) this.activity.getSystemService(Context.CLIPBOARD_SERVICE);
        PasteText();
    }

    private void initViews() {
        this.clipBoard = (ClipboardManager) this.activity.getSystemService(Context.CLIPBOARD_SERVICE);
        this.binding.imBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                InstagramActivity.this.onBackPressed();
            }
        });

        this.binding.imInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                InstagramActivity.this.binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
            }
        });

        Glide.with((FragmentActivity) this.activity).load(Integer.valueOf((int) R.drawable.insta1)).into(this.binding.layoutHowTo.imHowto1);
        Glide.with((FragmentActivity) this.activity).load(Integer.valueOf((int) R.drawable.insta2)).into(this.binding.layoutHowTo.imHowto2);
        Glide.with((FragmentActivity) this.activity).load(Integer.valueOf((int) R.drawable.insta3)).into(this.binding.layoutHowTo.imHowto3);
        Glide.with((FragmentActivity) this.activity).load(Integer.valueOf((int) R.drawable.insta4)).into(this.binding.layoutHowTo.imHowto4);

        this.binding.layoutHowTo.tvHowTo1.setText(getResources().getString(R.string.opn_insta));
        this.binding.layoutHowTo.tvHowTo3.setText(getResources().getString(R.string.opn_insta));
        if (!SharePrefs.getInstance(this.activity).getBoolean(SharePrefs.ISSHOWHOWTOINSTA).booleanValue()) {
            SharePrefs.getInstance(this.activity).putBoolean(SharePrefs.ISSHOWHOWTOINSTA, true);
            this.binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
        } else {
            this.binding.layoutHowTo.LLHowToLayout.setVisibility(View.GONE);
        }

        this.binding.loginBtn1.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                InstagramActivity.this.lambda$initViews$0$InstagramActivity(view);
            }
        });

        this.binding.tvPaste.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                InstagramActivity.this.lambda$initViews$1$InstagramActivity(view);
            }
        });

        this.binding.LLOpenInstagram.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                InstagramActivity.this.lambda$initViews$2$InstagramActivity(view);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        this.binding.RVUserList.setLayoutManager(gridLayoutManager);
        this.binding.RVUserList.setNestedScrollingEnabled(false);
        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        if (SharePrefs.getInstance(this.activity).getBoolean(SharePrefs.ISINSTALOGIN).booleanValue()) {
            layoutCondition();
            callStoriesApi();
            this.binding.SwitchLogin.setChecked(true);
        } else {
            this.binding.SwitchLogin.setChecked(false);
        }
        this.binding.tvLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                InstagramActivity.this.startActivityForResult(new Intent(InstagramActivity.this.activity, LoginActivity.class), 100);
            }
        });
        this.binding.RLLoginInstagram.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (!SharePrefs.getInstance(InstagramActivity.this.activity).getBoolean(SharePrefs.ISINSTALOGIN).booleanValue()) {
                    InstagramActivity.this.startActivityForResult(new Intent(InstagramActivity.this.activity, LoginActivity.class), 100);
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(InstagramActivity.this.activity);
                builder.setPositiveButton(InstagramActivity.this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharePrefs.getInstance(InstagramActivity.this.activity).putBoolean(SharePrefs.ISINSTALOGIN, false);
                        SharePrefs.getInstance(InstagramActivity.this.activity).putString(SharePrefs.COOKIES, "");
                        SharePrefs.getInstance(InstagramActivity.this.activity).putString(SharePrefs.CSRF, "");
                        SharePrefs.getInstance(InstagramActivity.this.activity).putString(SharePrefs.SESSIONID, "");
                        SharePrefs.getInstance(InstagramActivity.this.activity).putString(SharePrefs.USERID, "");
                        if (SharePrefs.getInstance(InstagramActivity.this.activity).getBoolean(SharePrefs.ISINSTALOGIN).booleanValue()) {
                            InstagramActivity.this.binding.SwitchLogin.setChecked(true);
                        } else {
                            InstagramActivity.this.binding.SwitchLogin.setChecked(false);
                            InstagramActivity.this.binding.RVUserList.setVisibility(View.GONE);
                            InstagramActivity.this.binding.RVStories.setVisibility(View.GONE);
                            InstagramActivity.this.binding.tvViewStories.setText(InstagramActivity.this.activity.getResources().getText(R.string.view_stories));
                            InstagramActivity.this.binding.tvLogin.setVisibility(View.VISIBLE);
                        }
                        dialogInterface.cancel();
                    }
                });
                builder.setNegativeButton(InstagramActivity.this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog create = builder.create();
                create.setTitle(InstagramActivity.this.getResources().getString(R.string.do_u_want_to_download_media_from_pvt));
                create.show();
            }
        });
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getApplicationContext(), 1);
        this.binding.RVStories.setLayoutManager(gridLayoutManager2);
        this.binding.RVStories.setNestedScrollingEnabled(false);
        gridLayoutManager2.setOrientation(RecyclerView.VERTICAL);
    }

    public void lambda$initViews$0$InstagramActivity(View view) {
        String obj = this.binding.etText.getText().toString();
        if (obj.equals("")) {
            Utils.setToast(this.activity, getResources().getString(R.string.enter_url));
        } else if (!Patterns.WEB_URL.matcher(obj).matches()) {
            Utils.setToast(this.activity, getResources().getString(R.string.enter_valid_url));
        } else {
            GetInstagramData();
        }
    }

    public void lambda$initViews$1$InstagramActivity(View view) {
        PasteText();
    }

    public void lambda$initViews$2$InstagramActivity(View view) {
        Utils.OpenApp(this.activity, "com.instagram.android");
    }

    public void layoutCondition() {
        this.binding.tvViewStories.setText(this.activity.getResources().getString(R.string.stories));
        this.binding.tvLogin.setVisibility(View.GONE);
    }

    private void GetInstagramData() {
        try {
            Utils.createFileFolder();
            String host = new URL(this.binding.etText.getText().toString()).getHost();
            Log.e("initViews: ", host);
            if (host.equals("www.instagram.com")) {
                callDownload(this.binding.etText.getText().toString());
            } else {
                Utils.setToast(this.activity, getResources().getString(R.string.enter_valid_url));
            }
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
                        if (itemAt.getText().toString().contains("instagram.com")) {
                            this.binding.etText.setText(itemAt.getText().toString());
                        }
                    } else if (this.clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("instagram.com")) {
                        this.binding.etText.setText(this.clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }
                }
            } else if (stringExtra.contains("instagram.com")) {
                this.binding.etText.setText(stringExtra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUrlWithoutParameters(String str) {
        try {
            URI uri = new URI(str);
            return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), null, uri.getFragment()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.setToast(this.activity, getResources().getString(R.string.enter_valid_url));
            return "";
        }
    }

    private void callDownload(String str) {
        String str2 = getUrlWithoutParameters(str) + "?__a=1";
        try {
            if (!new Utils(this.activity).isNetworkAvailable()) {
                Utils.setToast(this.activity, getResources().getString(R.string.no_net_conn));
            } else if (this.commonClassForAPI != null) {
                Utils.showProgressDialog(this.activity);
                this.commonClassForAPI.callResult(this.instaObserver, str2, "ds_user_id=" + SharePrefs.getInstance(this.activity).getString(SharePrefs.USERID) + "; sessionid=" + SharePrefs.getInstance(this.activity).getString(SharePrefs.SESSIONID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getImageFilenameFromURL(String str) {
        try {
            return new File(new URL(str).getPath().toString()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".png";
        }
    }

    public String getVideoFilenameFromURL(String str) {
        try {
            return new File(new URL(str).getPath().toString()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.instaObserver.dispose();
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        try {
            super.onActivityResult(i, i2, intent);
            if (i == 100 && i2 == -1) {
                intent.getStringExtra("key");
                if (SharePrefs.getInstance(this.activity).getBoolean(SharePrefs.ISINSTALOGIN).booleanValue()) {
                    this.binding.SwitchLogin.setChecked(true);
                    layoutCondition();
                    callStoriesApi();
                    return;
                }
                this.binding.SwitchLogin.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private void callStoriesApi() {
        try {
            if (!new Utils(this.activity).isNetworkAvailable()) {
                Utils.setToast(this.activity, this.activity.getResources().getString(R.string.no_net_conn));
            } else if (this.commonClassForAPI != null) {
                this.binding.prLoadingBar.setVisibility(View.VISIBLE);
                CommonClassForAPI commonClassForAPI2 = this.commonClassForAPI;
                DisposableObserver<StoryModel> disposableObserver = this.storyObserver;
                commonClassForAPI2.getStories(disposableObserver, "ds_user_id=" + SharePrefs.getInstance(this.activity).getString(SharePrefs.USERID) + "; sessionid=" + SharePrefs.getInstance(this.activity).getString(SharePrefs.SESSIONID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userListClick(int i, TrayModel trayModel) {
        callStoriesDetailApi(String.valueOf(trayModel.getUser().getPk()));
    }

    private void callStoriesDetailApi(String str) {
        try {
            if (!new Utils(this.activity).isNetworkAvailable()) {
                Utils.setToast(this.activity, this.activity.getResources().getString(R.string.no_net_conn));
            } else if (this.commonClassForAPI != null) {
                this.binding.prLoadingBar.setVisibility(View.VISIBLE);
                CommonClassForAPI commonClassForAPI2 = this.commonClassForAPI;
                DisposableObserver<FullDetailModel> disposableObserver = this.storyDetailObserver;
                commonClassForAPI2.getFullDetailFeed(disposableObserver, str, "ds_user_id=" + SharePrefs.getInstance(this.activity).getString(SharePrefs.USERID) + "; sessionid=" + SharePrefs.getInstance(this.activity).getString(SharePrefs.SESSIONID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}