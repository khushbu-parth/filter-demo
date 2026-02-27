package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.photoonl;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.PhotoOnlineModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.ManagerDataPlay;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.ConnectActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.DialogDisconnect;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.playcast.PlayCastActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@SuppressLint("WrongConstant")
public class PhotoOnlineActivity extends BaseActivity {

    private PhotoOnlineAdapter adapter;
    private ArrayList<PhotoOnlineModel> arrayList;
    private int currentItem = 0;
    private EditText edSearch;
    private ImageView imvConnect;
    private ImageView imvSearch;
    private LinearLayout llBack;
    private LinearLayout llConnect;
    private LinearLayout llSearchVoice;
    private ProgressBar prLoading;
    private RecyclerView rcvList;
    private List<PhotoOnlineModel> youtubeModelList;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.fragment_youtube);

        EventBus.getDefault().register(this);
        initView();
        PhotoOnlineActivity.this.callbackDone();

    }

    public Unit callbackFail() {
        return Unit.INSTANCE;
    }

    public Unit callbackDone() {
        return Unit.INSTANCE;
    }

    public Unit actionCommon() {
        gotoPlay();
        return Unit.INSTANCE;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (TVConnectUtils.getInstance().isConnected()) {
            imvConnect.setImageResource(R.drawable.hover_screen);
        } else {
            imvConnect.setImageResource(R.drawable.screen);
        }
    }

    public void gotoPlay() {
        Intent intent = new Intent(this, PlayCastActivity.class);
        ManagerDataPlay.getInstance().setTypePlay(4);
        ManagerDataPlay.getInstance().setListPhotoOnl(this.arrayList);
        ManagerDataPlay.getInstance().setPosSelected(this.currentItem);
        startActivity(intent);
        Utils.nextScreen(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        this.llConnect = (LinearLayout) findViewById(R.id.llConnect);
        this.llBack = (LinearLayout) findViewById(R.id.llBack);
        this.imvConnect = (ImageView) findViewById(R.id.imvConnect);
        this.prLoading = (ProgressBar) findViewById(R.id.prLoading);
        this.llSearchVoice = (LinearLayout) findViewById(R.id.llSearchVoice);
        this.rcvList = (RecyclerView) findViewById(R.id.rcvList);
        this.imvSearch = (ImageView) findViewById(R.id.imvSearch);
        this.edSearch = (EditText) findViewById(R.id.edSearch);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(1);
        this.rcvList.setLayoutManager(gridLayoutManager);
        PhotoOnlineAdapter photoOnlineAdapter = new PhotoOnlineAdapter(new ArrayList(), this);
        this.adapter = photoOnlineAdapter;
        this.rcvList.setAdapter(photoOnlineAdapter);
        this.adapter.setClickItem(new PhotoOnlineAdapter.OnItemClickPhoto() {
            @Override
            public void itemClick(List<PhotoOnlineModel> list, int i) {
                if (TVConnectUtils.getInstance().isConnected()) {
                    PhotoOnlineActivity.this.arrayList = new ArrayList();
                    PhotoOnlineActivity.this.arrayList.addAll(list);
                    PhotoOnlineActivity.this.currentItem = i;
                    PhotoOnlineActivity.this.gotoPlay();
                }
                PhotoOnlineActivity.this.startActivity(new Intent(PhotoOnlineActivity.this, ConnectActivity.class));
                Utils.nextScreen(PhotoOnlineActivity.this);
            }
        });
        if (TVConnectUtils.getInstance().isConnected()) {
            imvConnect.setImageResource(R.drawable.hover_screen);
        } else {
            imvConnect.setImageResource(R.drawable.screen);
        }
        this.llSearchVoice.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PhotoOnlineActivity.this.promptSpeechInput();
            }
        });
        this.imvSearch.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (!PhotoOnlineActivity.this.edSearch.getText().toString().trim().isEmpty()) {
                    PhotoOnlineActivity photoOnlineActivity = PhotoOnlineActivity.this;
                    photoOnlineActivity.searchYoutube(photoOnlineActivity.edSearch.getText().toString());
                }
            }
        });
        this.llConnect.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (TVConnectUtils.getInstance().isConnected()) {
                    new DialogDisconnect(PhotoOnlineActivity.this).show();
                    return;
                }
                PhotoOnlineActivity.this.startActivity(new Intent(PhotoOnlineActivity.this, ConnectActivity.class));
                Utils.nextScreen(PhotoOnlineActivity.this);
            }
        });
        this.llBack.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.photoonl.PhotoOnlineActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PhotoOnlineActivity.this.onBackPressed();
            }
        });
        this.edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.magicapps.casttotv.tv.screen.tab.photoonl.PhotoOnlineActivity.6
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 3) {
                    String trim = PhotoOnlineActivity.this.edSearch.getText().toString().trim();
                    if (trim.isEmpty()) {
                        return true;
                    }
                    PhotoOnlineActivity.this.searchYoutube(trim);
                    PhotoOnlineActivity.this.showSoftKeyboard();
                    return true;
                }
                return false;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSoftKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void promptSpeechInput() {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        intent.putExtra("android.speech.extra.LANGUAGE", Locale.getDefault());
        intent.putExtra("android.speech.extra.PROMPT", getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this, getString(R.string.speech_not_supported), 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void searchYoutube(String str) {
        this.prLoading.setVisibility(0);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder addHeader = new Request.Builder().addHeader("Ocp-Apim-Subscription-Key", "e0d5f3bfc1814598891169a8bd6aaedd");
        okHttpClient.newCall(addHeader.url("https://api.cognitive.microsoft.com/bing/v7.0/images/search?q=" + str + "&count=50").build()).enqueue(new Callback() { // from class: com.magicapps.casttotv.tv.screen.tab.photoonl.PhotoOnlineActivity.7
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                Log.e("###TAG", "onFailure: " + iOException.getMessage());
                iOException.printStackTrace();
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if (response.isSuccessful()) {
                    try {
                        Log.e("###TAG", "onResponse: isSuccessful");
                        PhotoOnlineActivity.this.youtubeModelList = new ArrayList();
                        JSONArray jSONArray = new JSONObject(body.string()).getJSONArray("value");
                        for (int i = 0; i < jSONArray.length() - 1; i++) {
                            JSONObject jSONObject = jSONArray.getJSONObject(i);
                            PhotoOnlineModel photoOnlineModel = new PhotoOnlineModel();
                            photoOnlineModel.setImageName(jSONObject.getString("name"));
                            photoOnlineModel.setImageURL(jSONObject.getString("contentUrl"));
                            photoOnlineModel.setThumbURL(jSONObject.getString("thumbnailUrl"));
                            PhotoOnlineActivity.this.youtubeModelList.add(photoOnlineModel);
                        }
                        PhotoOnlineActivity.this.runOnUiThread(new Runnable() {
                            @Override // java.lang.Runnable
                            public void run() {
                                PhotoOnlineActivity.this.adapter.setData(PhotoOnlineActivity.this.youtubeModelList);
                                PhotoOnlineActivity.this.prLoading.setVisibility(8);
                                PhotoOnlineActivity.this.showSoftKeyboard();
                            }
                        });
                        return;
                    } catch (JSONException e2) {
                        Log.e("###TAG", "onFailure 2: " + e2.getMessage());
                        e2.printStackTrace();
                        PhotoOnlineActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PhotoOnlineActivity.this.prLoading.setVisibility(8);
                            }
                        });
                        return;
                    }
                } else {
                    Log.e("###TAG", "onResponse: Failed");
                }
                PhotoOnlineActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PhotoOnlineActivity.this.prLoading.setVisibility(8);
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1 && intent != null) {
            this.edSearch.setText(intent.getStringArrayListExtra("android.speech.extra.RESULTS").get(0));
            searchYoutube(this.edSearch.getText().toString());
        }
    }
}
