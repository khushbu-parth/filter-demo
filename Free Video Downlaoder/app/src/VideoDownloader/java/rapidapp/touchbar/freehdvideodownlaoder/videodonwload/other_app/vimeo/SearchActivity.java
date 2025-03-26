package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.vimeo;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;

@SuppressLint("WrongConstant")

public class SearchActivity extends BaseActivity {
    public static String mFilePath;
    private static ArrayList<HashMap<String, String>> arrayList;
    public Button btnCanceldownload;
    public FrameLayout download_fram_tray;
    public FrameLayout download_layout;
    public ListView listViewDialoge;
    public WebView webView;
    ResolutionAdapter adapter;
    ArrayList<ItemResolutionModel> arrResolution;
    LinearLayout calibMain;
    String class_id = "";
    Cursor cursor1;
    boolean flag_status_visible;
    ImageView imgBack;
    ProgressDialog progress;
    ProgressDialog progressBarDialog;
    MaterialSearchBar search;
    TextView txtStatus;
    ArrayList valArrResolution;
    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            try {
                SearchActivity.this.txtStatus.setVisibility(View.GONE);
                Toast.makeText(ctxt, "Download Complete", Toast.LENGTH_LONG).show();
                @SuppressLint("WrongConstant") final Snackbar bar = Snackbar.make(SearchActivity.this.btnCanceldownload, (CharSequence) "Download Complete", -2);
                ((TextView) bar.getView().findViewById(R.id.snackbar_text)).setTextColor(SearchActivity.this.getResources().getColor(R.color.easyPaisaGreen));
                bar.setActionTextColor(SearchActivity.this.getResources().getColor(R.color.teal_200));
                try {
                    if (SearchActivity.this.progressBarDialog.isShowing()) {
                        SearchActivity.this.progressBarDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bar.setAction((CharSequence) "Play", new OnClickListener() {
                    public void onClick(View v) {
                        bar.dismiss();
                        SearchActivity.this.finish();
                        SearchActivity.this.startActivity(new Intent(SearchActivity.this, VideoViewActivity.class).putExtra("url", SearchActivity.mFilePath));
                    }
                });
                bar.show();
            } catch (Exception e2) {
            }
        }
    };
    private int count;
    private DownloadManager downloadManager;
    private long downloadReference;
    private String idVideo;
    private JSONObject jsonObject;
    private JsonVimeoParser jsonVimeoParser;
    private String str;
    private String urlConfig;
    private String urlCur;
    private String urlOld;
    private Toolbar toolbar;
    private String statusText;
    private String reasonText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.count = 1;
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        this.imgBack = (ImageView) this.toolbar.findViewById(R.id.img_back_toolbar);
        this.txtStatus = (TextView) this.toolbar.findViewById(R.id.toolbar_status);
        this.imgBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearchActivity.this.onBackPressed();
            }
        });
        this.flag_status_visible = false;
        this.txtStatus.setVisibility(View.GONE);
        this.txtStatus.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearchActivity.this.progressBarCalculation();
                Query myDownloadQuery = new Query();
                myDownloadQuery.setFilterById(new long[]{SearchActivity.this.downloadReference});
                SearchActivity.this.cursor1 = SearchActivity.this.downloadManager.query(myDownloadQuery);
                if (SearchActivity.this.cursor1.moveToFirst()) {
                    CharSequence text = SearchActivity.this.checkStatus(SearchActivity.this.cursor1);
                    final Snackbar bar = Snackbar.make(SearchActivity.this.listViewDialoge, text, -2);
                    TextView tv = (TextView) bar.getView().findViewById(R.id.snackbar_text);
                    tv.setMaxLines(2);
                    tv.setTextColor(SearchActivity.this.getResources().getColor(R.color.easyPaisaGreen));
                    if (text.equals("STATUS SUCCESSFUL:")) {
                        bar.setActionTextColor(SearchActivity.this.getResources().getColor(R.color.teal_200));
                        bar.setAction((CharSequence) "Play", new OnClickListener() {
                            public void onClick(View v) {
                                bar.dismiss();
                            }
                        });
                    } else {
                        bar.setActionTextColor(SearchActivity.this.getResources().getColor(R.color.white));
                        bar.setAction((CharSequence) "Dismiss", new OnClickListener() {
                            public void onClick(View v) {
                                bar.dismiss();
                            }
                        });
                    }
                    bar.show();
                }
            }
        });
        initializeSearchView();
        initializeWebView();
        initializeDownloadView();
        LinearLayout calibirationll = (LinearLayout) findViewById(R.id.calibirationLayout);
        this.calibMain = (LinearLayout) findViewById(R.id.calibiration);
        Button btnGotIt = (Button) findViewById(R.id.got_it);
        if (read("calib", "null").equals("null")) {
            this.calibMain.setVisibility(View.VISIBLE);
        } else {
            this.calibMain.setEnabled(false);
            this.calibMain.setVisibility(View.GONE);
        }
        btnGotIt.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SearchActivity.this.save("calib", "value");
                SearchActivity.this.calibMain.setEnabled(false);
                SearchActivity.this.calibMain.setVisibility(View.GONE);
            }
        });
        this.btnCanceldownload.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearchActivity.this.download_fram_tray.setVisibility(View.GONE);
            }
        });
        this.download_layout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int flag = SearchActivity.this.listViewDialoge.getVisibility();
                if (SearchActivity.this.listViewDialoge.getVisibility() == View.GONE) {
                    SearchActivity.this.adapter.notifyDataSetChanged();
                    SearchActivity.this.listViewDialoge.setVisibility(View.VISIBLE);
                    return;
                }
                SearchActivity.this.listViewDialoge.setVisibility(View.GONE);
            }
        });
        this.listViewDialoge.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ArrayList<ItemResolutionModel> abc = SearchActivity.this.arrResolution;
                try {
                    SearchActivity.this.downloadFB(((ItemResolutionModel) SearchActivity.this.arrResolution.get(position)).getLink(), ((ItemResolutionModel) SearchActivity.this.arrResolution.get(position)).getIdVideo(), ((ItemResolutionModel) SearchActivity.this.arrResolution.get(position)).getNameVideo());
                    Toast.makeText(SearchActivity.this.getApplicationContext(), "Downloading Start", Toast.LENGTH_LONG).show();
                    SearchActivity.this.listViewDialoge.setVisibility(View.GONE);
                    SearchActivity.this.download_fram_tray.setVisibility(View.GONE);
                    SearchActivity.this.flag_status_visible = true;
                    SearchActivity.this.txtStatus.setVisibility(View.VISIBLE);
                    final Snackbar bar = Snackbar.make(SearchActivity.this.listViewDialoge, (CharSequence) "Downloading Please wait...", -2);
                    ((TextView) bar.getView().findViewById(R.id.snackbar_text)).setTextColor(SearchActivity.this.getResources().getColor(R.color.easyPaisaGreen));
                    bar.setActionTextColor(SearchActivity.this.getResources().getColor(R.color.white));
                    bar.setAction((CharSequence) "Status", new OnClickListener() {
                        public void onClick(View v) {
                            bar.dismiss();
                            Query myDownloadQuery = new Query();
                            myDownloadQuery.setFilterById(new long[]{SearchActivity.this.downloadReference});
                            Cursor cursor = SearchActivity.this.downloadManager.query(myDownloadQuery);
                            if (cursor.moveToFirst()) {
                                CharSequence text = SearchActivity.this.checkStatus(cursor);
                                final Snackbar bar = Snackbar.make(SearchActivity.this.listViewDialoge, text, -2);
                                TextView tv = (TextView) bar.getView().findViewById(R.id.snackbar_text);
                                tv.setMaxLines(2);
                                tv.setTextColor(SearchActivity.this.getResources().getColor(R.color.easyPaisaGreen));
                                if (text.equals("STATUS SUCCESSFUL:")) {
                                    bar.setActionTextColor(SearchActivity.this.getResources().getColor(R.color.teal_200));
                                    bar.setAction((CharSequence) "Play", new OnClickListener() {
                                        public void onClick(View v) {
                                            bar.dismiss();
                                        }
                                    });
                                } else {
                                    bar.setActionTextColor(SearchActivity.this.getResources().getColor(R.color.white));
                                    bar.setAction((CharSequence) "Dismiss", new OnClickListener() {
                                        public void onClick(View v) {
                                            bar.dismiss();
                                        }
                                    });
                                }
                                bar.show();
                            }
                        }
                    });
                    bar.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void progressBarCalculation() {
        this.progressBarDialog = new ProgressDialog(this);
        this.progressBarDialog.setTitle("Downloading Video, Please Wait...");
        this.progressBarDialog.setProgressStyle(1);
        this.progressBarDialog.setButton(-1, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        this.progressBarDialog.setProgress(0);
        new Thread(new Runnable() {
            public void run() {
                boolean downloading = true;
                DownloadManager manager = (DownloadManager) SearchActivity.this.getSystemService("download");
                while (downloading) {
                    Query q = new Query();
                    q.setFilterById(new long[]{SearchActivity.this.downloadReference});
                    Cursor cursor = manager.query(q);
                    if (cursor.moveToFirst()) {
                        int bytes_downloaded = cursor.getInt(cursor.getColumnIndex("bytes_so_far"));
                        int bytes_total = cursor.getInt(cursor.getColumnIndex("total_size"));
                        if (cursor.getInt(cursor.getColumnIndex("status")) == 8) {
                            downloading = false;
                        }
                        final int dl_progress = (int) ((((long) bytes_downloaded) * 100) / ((long) bytes_total));
                        SearchActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                SearchActivity.this.progressBarDialog.setProgress(dl_progress);
                            }
                        });
                    }
                    cursor.close();
                }
            }
        }).start();
        this.progressBarDialog.show();
    }

    private String checkStatus(Cursor cursor) {
        int status = cursor.getInt(cursor.getColumnIndex("status"));
        int reason = cursor.getInt(cursor.getColumnIndex("reason"));
        switch (status) {
            case 1:
                statusText = "STATUS PENDING";
                break;
            case 2:
                statusText = "STATUS RUNNING";
                break;
            case 4:
                statusText = "STATUS PAUSED";
                switch (reason) {
                    case 1:
                        reasonText = "PAUSED WAITING TO RETRY";
                        break;
                    case 2:
                        reasonText = "PAUSED WAITING FOR NETWORK";
                        break;
                    case 3:
                        reasonText = "PAUSED QUEUED FOR WIFI";
                        break;
                    case 4:
                        reasonText = "PAUSED UNKNOWN";
                        break;
                    default:
                        break;
                }
            case 8:
                statusText = "STATUS SUCCESSFUL";
                reasonText = "Filename:\n";
                break;
            case 16:
                statusText = "STATUS FAILED";
                switch (reason) {
                    case 1000:
                        reasonText = "ERROR UNKNOWN";
                        break;
                    case 1001:
                        reasonText = "ERROR FILE ERROR";
                        break;
                    case 1002:
                        reasonText = "ERROR UNHANDLED HTTP CODE";
                        break;
                    case 1004:
                        reasonText = "ERROR HTTP DATA ERROR";
                        break;
                    case 1005:
                        reasonText = "ERROR TOO_MANY REDIRECTS";
                        break;
                    case 1006:
                        reasonText = "ERROR INSUFFICIENT SPACE";
                        break;
                    case 1007:
                        reasonText = "ERROR DEVICE NOT FOUND";
                        break;
                    case 1008:
                        reasonText = "ERROR CANNOT RESUME";
                        break;
                    case 1009:
                        reasonText = "ERROR FILE ALREADY EXISTS";
                        break;
                    default:
                        break;
                }
        }
        return statusText + ":" + reasonText;
    }

    public void downloadFB(String vid_url, String vid_id, String name) {
        try {
            String mBaseFolderPath = "/storage/emulated/0/Download/StatusSaver/Vimeo/";
//            String mBaseFolderPath = Environment.getExternalStorageDirectory() + File.separator + "All_HD_Video_Downloader" + File.separator;
            if (!new File(mBaseFolderPath).exists()) {
                new File(mBaseFolderPath).mkdir();
            }
            if (vid_url.contains(".mp4")) {
                mFilePath = "file://" + mBaseFolderPath + name + "_" + vid_id + ".mp4";
            } else if (vid_url.contains(".3gp")) {
                mFilePath = "file://" + mBaseFolderPath + name + "_" + vid_id + ".3gp";
            } else if (vid_url.contains(".avi")) {
                mFilePath = "file://" + mBaseFolderPath + name + "_" + vid_id + ".avi";
            }
            Request req = new Request(Uri.parse(vid_url));
            req.setDestinationUri(Uri.parse(mFilePath));
            req.setNotificationVisibility(1);
            getApplicationContext();
            this.downloadManager = (DownloadManager) getSystemService("download");
            this.downloadReference = this.downloadManager.enqueue(req);
            registerReceiver(this.onComplete, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
            registerReceiver(this.onComplete, new IntentFilter("android.intent.action.DOWNLOAD_VOMPLETE"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeDownloadView() {
        this.download_fram_tray = (FrameLayout) findViewById(R.id.download_tray);
        this.download_layout = (FrameLayout) findViewById(R.id.download_layout);
        this.btnCanceldownload = (Button) findViewById(R.id.cancel_download);
        this.listViewDialoge = (ListView) findViewById(R.id.list_quilty);
    }

    private void initializeWebView() {
        this.webView = (WebView) findViewById(R.id.web_view);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebChromeClient(new WebChromeClient());
    }

    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    protected void initializeSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService("search");
        this.search = (MaterialSearchBar) findViewById(R.id.search);
        this.search.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 2);
                if (!isOnline()) {
                    Toast.makeText(getApplicationContext(), "No Network Connection!!!", Toast.LENGTH_SHORT).show();
                } else if (text.toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter some query to search!", Toast.LENGTH_SHORT).show();
                } else {
                    new QueryListener();
                    SearchActivity.this.class_id = "submit_query";
                    SearchActivity.this.searchViaVimeo(text.toString());
                }
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    public void searchViaVimeo(String query) {
        this.webView.loadUrl("https://vimeo.com/search/page:1/sort:relevance?q=" + query);
        getIdVideo();
    }

    public void getIdVideo() {
        this.webView.setWebViewClient(new WebClientLoading());
    }

    public void getLinkVideo() {
        new VideoLoading().execute(new Void[]{null, null, null});
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
        }
        return true;
    }

    public void save(String valueKey, String value) {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putString(valueKey, value);
        edit.commit();
    }

    public String read(String valueKey, String valueDefault) {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(valueKey, valueDefault);
    }

    public void onBackPressed() {
        if (this.progress != null && this.progress.isShowing()) {
            this.progress.dismiss();
        }
        if (this.listViewDialoge.getVisibility() == View.VISIBLE) {
            this.adapter.notifyDataSetChanged();
            this.listViewDialoge.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    private void Dialoge_show(boolean flag) {
        try {
            if (this.progress != null && this.progress.isShowing()) {
                this.progress.dismiss();
            }
            this.progress = ProgressDialog.show(this, null, null, true);
            this.progress.setContentView(R.layout.layout_dialoge);
            this.progress.setCancelable(false);
            this.progress.setCanceledOnTouchOutside(false);
            this.progress.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            if (flag) {
                this.progress.show();
            } else {
                this.progress.dismiss();
            }
        } catch (Exception e) {
        }
    }

    protected void onPause() {
        try {
            if (this.onComplete != null) {
                unregisterReceiver(this.onComplete);
            }
        } catch (Exception e) {
        }
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    class QueryListener {
        QueryListener() {
        }

        public boolean onQueryTextSubmit(String query) {
            SearchActivity.this.class_id = "submit_query";
            SearchActivity.this.searchViaVimeo(query);
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }

    class VideoLoading extends AsyncTask<Void, Void, ArrayList<ItemResolutionModel>> {
        VideoLoading() {
        }

        protected ArrayList<ItemResolutionModel> doInBackground(Void... params) {
            SearchActivity.this.urlConfig = "http://player.vimeo.com/video/" + SearchActivity.this.idVideo + "/config";
            SearchActivity.this.str = new TaskLoadData().makeServiceCall(SearchActivity.this.urlConfig, 1);
            if (SearchActivity.this.str != null) {
                try {
                    SearchActivity.this.jsonObject = new JSONObject(SearchActivity.this.str);
                    JsonVimeoParser.prepare(SearchActivity.this.jsonObject);
                    SearchActivity.this.jsonVimeoParser = JsonVimeoParser.getInstance();
                    String title = SearchActivity.this.jsonVimeoParser.getVideoTitle();
                    SearchActivity.arrayList = SearchActivity.this.jsonVimeoParser.getAllVideo();
                    SearchActivity.this.arrResolution = new ArrayList();
                    for (int i = 0; i < SearchActivity.arrayList.size(); i++) {
                        SearchActivity.this.arrResolution.add(new ItemResolutionModel(((String) ((HashMap) SearchActivity.arrayList.get(i)).get("height")) + "x" + ((String) ((HashMap) SearchActivity.arrayList.get(i)).get("width")), (String) ((HashMap) SearchActivity.arrayList.get(i)).get("url"), SearchActivity.this.idVideo, title));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return SearchActivity.this.arrResolution;
        }

        protected void onPreExecute() {
            SearchActivity.this.Dialoge_show(true);
        }

        protected void onPostExecute(ArrayList<ItemResolutionModel> stringStringHashMap) {
            SearchActivity.this.Dialoge_show(false);
            SearchActivity.this.download_fram_tray.setVisibility(View.VISIBLE);
            SearchActivity.this.adapter = new ResolutionAdapter(SearchActivity.this, SearchActivity.this.arrResolution);
            SearchActivity.this.listViewDialoge.setAdapter(SearchActivity.this.adapter);
            Toast.makeText(SearchActivity.this.getApplicationContext(), "Link captured please press download button to download file.", Toast.LENGTH_LONG).show();
            super.onPostExecute(stringStringHashMap);
        }
    }

    class WebClientLoading extends WebViewClient {
        WebClientLoading() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            SearchActivity.this.Dialoge_show(true);
            super.onPageStarted(view, url, favicon);
        }

        public void onLoadResource(WebView view, String url) {
            if (view.getUrl() != null && view.getUrl().matches("https://vimeo.com/([0-9]+)")) {
                SearchActivity.this.urlCur = view.getUrl();
                int pos;
                if (SearchActivity.this.urlOld == null && SearchActivity.this.count == 1) {
                    SearchActivity.this.urlOld = SearchActivity.this.urlCur;
                    pos = SearchActivity.this.urlOld.lastIndexOf("/");
                    if (pos != -1) {
                        SearchActivity.this.idVideo = SearchActivity.this.urlOld.substring(pos + 1);
                        SearchActivity.this.getLinkVideo();
                        SearchActivity.this.count = SearchActivity.this.count + 1;
                    }
                } else if (!SearchActivity.this.urlOld.equals(SearchActivity.this.urlCur) && SearchActivity.this.count == 2) {
                    SearchActivity.this.urlOld = SearchActivity.this.urlCur;
                    pos = SearchActivity.this.urlOld.lastIndexOf("/");
                    if (pos != -1) {
                        SearchActivity.this.idVideo = SearchActivity.this.urlOld.substring(pos + 1);
                        SearchActivity.this.getLinkVideo();
                    }
                }
            }
            super.onLoadResource(view, url);
        }

        public void onPageFinished(WebView view, String url) {
            SearchActivity.this.Dialoge_show(false);
            SearchActivity.this.search.clearFocus();
            super.onPageFinished(view, url);
        }
    }
}