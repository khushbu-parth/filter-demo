package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import com.pesonal.adsdk.AppManage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Adapter.SplashListAdapter;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Model.SplashModel;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.TokanData.CallAPI;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.TokanData.Glob;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.TokanData.PreferencesUtils;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.View.ExpandableGridView;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.WebActivity;

//import static com.pesonal.adsdk.AppManage.ADMOB_I1;

public class SplashActivity1 extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_moreapps, iv_rate, iv_start, privacy;
    ExpandableGridView mainmore;
    PreferencesUtils pref;
    private long diffMills = 0;
    private int totalHours = 0;
    private boolean dataAvailable = false;
    private boolean isAlreadyCall = false;
    private ArrayList<SplashModel> splashList1 = new ArrayList<>();
    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_splash);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pref = PreferencesUtils.getInstance(SplashActivity1.this);
        counter = 0;
        bind();
        setAppInList();
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

    void setTimeForApp() {
        Calendar calander = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currDate = simpledateformat.format(calander.getTime());
        pref.setPrefString(PreferencesUtils.TIME_OF_GET_APP_SPLASH, currDate);
    }

    private void showMoreApps() {
        String strResponse = pref.getPrefString(PreferencesUtils.SPLASH_1_JSON);
        if (!TextUtils.isEmpty(strResponse)) {
            JSONObject json = null;
            try {
                json = new JSONObject(strResponse);
                JSONArray jArray = json.getJSONArray("data");
                if (jArray.length() > 0) {
                    splashList1.clear();
                    for (int d = 0; d < jArray.length(); d++) {
                        JSONObject objJson = jArray.getJSONObject(d);
                        String photo_name = objJson.getString("application_name");
                        String photo_link = objJson.getString("application_link");
                        String photo_icon = objJson.getString("icon");
                        splashList1.add(new SplashModel("http://appbankstudio.in/appbank/images/" + photo_icon, photo_name, photo_link));
                    }
                    final SplashListAdapter splashListAdapter = new SplashListAdapter(SplashActivity1.this, splashList1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainmore.setAdapter(splashListAdapter);
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
        mainmore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(splashList1.get(position).getAppLink());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {

                    Toast.makeText(SplashActivity1.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void bind() {
        mainmore = (ExpandableGridView) findViewById(R.id.mainmore);
        iv_moreapps = (ImageView) findViewById(R.id.iv_moreapps);
        iv_moreapps.setOnClickListener(this);
        iv_rate = (ImageView) findViewById(R.id.iv_rate);
        iv_rate.setOnClickListener(this);
        iv_start = (ImageView) findViewById(R.id.iv_start);
        iv_start.setOnClickListener(this);
        privacy = (ImageView) findViewById(R.id.privacy);
        privacy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_moreapps:
                Uri uri = Uri.parse("https://play.google.com/store/apps/developer?id=Touch+Bar+Apps");
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(SplashActivity1.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.iv_rate:
                gotoStore();
                break;
            case R.id.iv_start:
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.viewpush));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkAndRequestPermissions()) {
                        callnext();
                    }
                } else {
                    callnext();
                }

                break;
            case R.id.privacy:
                startActivity(new Intent(SplashActivity1.this, WebActivity.class));
                break;
        }
    }

    private void callnext() {
        startActivity(new Intent(SplashActivity1.this, SplashActivity2.class));


    }


    public void onBackPressed() {
        if (isOnline()) {
        } else {
            finish();
            finishAffinity();
        }
    }

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2000;

    public void gotoStore() {
        Uri uri = Uri.parse(Glob.app_link);
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        myAppLinkToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            //the device hasn't installed Google Play
            Toast.makeText(SplashActivity1.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkAndRequestPermissions() {
        int writeStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (writeStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();

                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);


                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);


                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED


                    ) {
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)


                        ) {
                            showDialogOK("Permission required for this app", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            checkAndRequestPermissions();
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashActivity1.this).setMessage(message).setPositiveButton("OK", okListener).setNegativeButton("Cancel", okListener).create().show();
    }

}
