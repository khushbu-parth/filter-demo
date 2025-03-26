package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.PointerIconCompat;

import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.material.snackbar.Snackbar;
import com.pesonal.adsdk.AppManage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Adapter.AppList_Adapter_splash;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.TokanData.CallAPI;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.TokanData.Glob;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.TokanData.PreferencesUtils;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities.MainActivity;

import static com.pesonal.adsdk.AppManage.ADMOB_I1;
import static com.pesonal.adsdk.AppManage.ADMOB_N1;
import static com.pesonal.adsdk.AppManage.FACEBOOK;
import static com.pesonal.adsdk.AppManage.FACEBOOK_I1;
import static com.pesonal.adsdk.AppManage.FACEBOOK_N1;

public class SplashActivity2 extends AppCompatActivity implements View.OnClickListener {

    GridView app_list;
    private ImageView banner_layout;
    private PreferencesUtils pref;
    private long diffMills = 0;
    private int totalHours = 0;
    public static ArrayList<String> listIcon2 = new ArrayList<String>();
    public static ArrayList<String> listName2 = new ArrayList<String>();
    public static ArrayList<String> listUrl2 = new ArrayList<String>();
    private boolean isAlreadyCall = false;
    private boolean dataAvailable = false;
    private LinearLayout nativeAdContainer;
    int counter = 0;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2000;
    ImageView start;
    private Intent l;
    private UnifiedNativeAd nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        counter = 0;
        pref = PreferencesUtils.getInstance(SplashActivity2.this);
        bind();
        setAppInList();


        AppManage.getInstance(SplashActivity2.this).show_NATIVE((ViewGroup) findViewById(R.id.native_container), ADMOB_N1, "");
        AppManage.getInstance(this).loadintertialads(this, ADMOB_I1, "");
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (!(i == PointerIconCompat.TYPE_GRAB && i2 == -1)) {
            if (i != 1023 || i2 != -1) {
                return;
            }
        }
        finish();
    }

    private void bind() {
        banner_layout = (ImageView) findViewById(R.id.banner_layout);
        if (isOnline()) {
            banner_layout.setVisibility(View.INVISIBLE);
        } else {
            banner_layout.setVisibility(View.VISIBLE);
        }
        app_list = (GridView) findViewById(R.id.gvMoreApps);
        start = (ImageView) findViewById(R.id.start);
        start.setOnClickListener(this);
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void callApiForApplist() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallAPI.callGet("", "splash_28/" + Glob.appID, false, new CallAPI.ResultCallBack() {
                    @Override
                    public void onSuccess(int responseCode, String strResponse) {
                        isAlreadyCall = true;
                        System.out.println("Response-" + strResponse);
                        System.out.println("Code-" + responseCode);
                        pref.setPrefString(PreferencesUtils.SPLASH_1_JSON, strResponse);
                        setTimeForApp();
                        setAppInList();
                    }

                    @Override
                    public void onCancelled() {

                    }

                    @Override
                    public void onFailure(int responseCode, String strResponse) {

                    }
                });

            }
        }).start();
    }

    private void setAppInList() {
        Calendar calander = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String strCurrDate = simpledateformat.format(calander.getTime());
        String strLastDate = pref.getPrefString(PreferencesUtils.TIME_OF_GET_APP_SPLASH);
        try {
            Date currDate = simpledateformat.parse(strCurrDate);
            Date lastDate = simpledateformat.parse(strLastDate);
            diffMills = currDate.getTime() - lastDate.getTime();
            totalHours = (int) (diffMills / (1000 * 60 * 60));
        } catch (Exception e) {
            e.printStackTrace();
            totalHours = 0;
        }
        if (totalHours >= 0 && totalHours < 6) {
            showMoreApps();
        } else {
            if (isOnline())
                callApiForApplist();
            else
                showMoreApps();
        }
    }

    private void showMoreApps() {
        String strResponse = pref.getPrefString(PreferencesUtils.SPLASH_1_JSON);
        if (!TextUtils.isEmpty(strResponse)) {
            JSONObject json = null;
            try {
                json = new JSONObject(strResponse);
                if (json.optString("ac_link") != null && !TextUtils.isEmpty(json.optString("ac_link")))
                    Glob.acc_link = json.optString("ac_link");
                if (json.optString("privacy_link") != null && !TextUtils.isEmpty(json.optString("privacy_link"))) {
                    Glob.privacy_link = json.optString("privacy_link");
                    Log.e("privacy", json.optString("privacy_link"));
                }
                JSONArray jArray = json.getJSONArray("data");
                if (jArray.length() != 0) {
                    dataAvailable = true;

                    listIcon2.clear();
                    listName2.clear();
                    listUrl2.clear();
                    for (int d = 0; d < jArray.length(); d++) {
                        JSONObject objJson = jArray.getJSONObject(d);
                        String photo_name = objJson.getString("application_name");
                        String photo_link = objJson.getString("application_link");
                        String photo_icon = objJson.getString("icon");
                        listIcon2.add("http://appbankstudio.in/appbank/images/" + photo_icon);
                        listName2.add(photo_name);
                        listUrl2.add(photo_link);
                    }
                    final AppList_Adapter_splash adapterApp1 = new AppList_Adapter_splash(this, listUrl2, listIcon2, listName2);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            app_list.setAdapter(adapterApp1);
                        }
                    });
                } else {
                    if (!isAlreadyCall)
                        callApiForApplist();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            callApiForApplist();
        }
        app_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(listUrl2.get(position));
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(SplashActivity2.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void setTimeForApp() {
        Calendar calander = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currDate = simpledateformat.format(calander.getTime());
        pref.setPrefString(PreferencesUtils.TIME_OF_GET_APP_SPLASH, currDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                callnext();
                break;
        }
    }

    private void callnext() {
        AppManage.getInstance(this).show_INTERSTIAL(this, new AppManage.MyCallback() {
            @Override
            public void callbackCall() {
                startActivity(new Intent(SplashActivity2.this, MainActivity.class));
            }
        });


    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (isOnline()) {
            String json = pref.getPrefString(PreferencesUtils.EXIT_JSON);
            if ((!TextUtils.isEmpty(json) || isOnline()) && dataAvailable) {

                if (counter == 0) {
                    Intent i = new Intent(SplashActivity2.this, BackActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }

            } else {
                if (doubleBackToExitPressedOnce) {
                    finish();
                    super.onBackPressed();
                }

                this.doubleBackToExitPressedOnce = true;
                Snackbar snackbar = Snackbar.make(banner_layout, "click BACK again to exit", Snackbar.LENGTH_SHORT);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
                Toast.makeText(SplashActivity2.this, "click BACK again to exit", Toast.LENGTH_LONG);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        } else {
            if (doubleBackToExitPressedOnce) {
                finish();
                super.onBackPressed();
            }

            this.doubleBackToExitPressedOnce = true;
            Snackbar snackbar = Snackbar.make(banner_layout, "click BACK again to exit", Snackbar.LENGTH_SHORT);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
            Toast.makeText(SplashActivity2.this, "click BACK again to exit", Toast.LENGTH_LONG);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


}
