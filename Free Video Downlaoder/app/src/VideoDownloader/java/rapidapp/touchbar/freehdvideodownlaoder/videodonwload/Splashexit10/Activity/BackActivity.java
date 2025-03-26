package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.pesonal.adsdk.AppManage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Adapter.AppList_Adapter;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Adapter.AppList_Adapter_Banner;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Model.SplashModel;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.TokanData.CallAPI;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.TokanData.Glob;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.TokanData.PreferencesUtils;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.View.ExpandableGridView;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities.MainActivity;

import static com.pesonal.adsdk.AppManage.ADMOB_I1;
import static com.pesonal.adsdk.AppManage.ADMOB_N1;


public class BackActivity extends AppCompatActivity {
    ExpandableGridView app_list;
    ExpandableGridView app_list1;
    private PreferencesUtils pref;
    private int totalHours;
    private boolean isAlreadyCall = false;
    private ArrayList<SplashModel> exitList1 = new ArrayList<>();
    private ArrayList<SplashModel> exitList2 = new ArrayList<>();
    private Dialog dialog;
    private int counter = 0;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pref = PreferencesUtils.getInstance(this);
        AppManage.getInstance(this).loadintertialads(this, ADMOB_I1, "");

        app_list = (ExpandableGridView) findViewById(R.id.gvAppList);
        app_list.setExpanded(true);
        app_list1 = (ExpandableGridView) findViewById(R.id.gvAppList1);
        app_list1.setExpanded(true);
        setAppInList();
    }


    private void callApiForApplist() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallAPI.callGet("", "exit_28/" + Glob.appID, false, new CallAPI.ResultCallBack() {
                    @Override
                    public void onSuccess(int responseCode, String strResponse) {
                        isAlreadyCall = true;
                        System.out.println("Response-" + strResponse);
                        System.out.println("Code-" + responseCode);
                        pref.setPrefString(PreferencesUtils.EXIT_JSON, strResponse);
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
        String strLastDate = pref.getPrefString(PreferencesUtils.TIME_OF_GET_APP_EXIT);
        try {
            Date currDate = simpledateformat.parse(strCurrDate);
            Date lastDate = simpledateformat.parse(strLastDate);
            long diffMills = currDate.getTime() - lastDate.getTime();
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
        String strResponse = pref.getPrefString(PreferencesUtils.EXIT_JSON);
        if (!TextUtils.isEmpty(strResponse)) {
            JSONObject json = null;
            try {
                json = new JSONObject(strResponse);
                JSONArray jArray = json.getJSONArray("data");
                if (jArray.length() > 0) {
                    exitList1.clear();
                    exitList2.clear();
                    for (int d = 0; d < 15; d++) {
                        JSONObject objJson = jArray.getJSONObject(d);
                        String photo_name = objJson.getString("application_name");
                        String photo_link = objJson.getString("application_link");
                        String photo_icon = objJson.getString("icon");
                        exitList1.add(new SplashModel("http://appbankstudio.in/appbank/images/" + photo_icon, photo_name, photo_link));
                    }

                    for (int d = 15; d < jArray.length(); d++) {

                        JSONObject objJson = jArray.getJSONObject(d);
                        String photo_name = objJson.getString("application_name");
                        String photo_link = objJson.getString("application_link");
                        String photo_icon = objJson.getString("icon");

                        System.out.println("photo_name -" + photo_name);
                        System.out.println("photo_link -" + photo_link);
                        System.out.println("photo_icon -" + photo_icon);

                        exitList2.add(new SplashModel("http://appbankstudio.in/appbank/images/" + photo_icon, photo_name, photo_link));
                    }

                    final AppList_Adapter adapterApp = new AppList_Adapter(BackActivity.this, exitList1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            app_list.setAdapter(adapterApp);
                        }
                    });
                    final AppList_Adapter_Banner adapterApp1 = new AppList_Adapter_Banner(BackActivity.this, exitList2);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            app_list1.setAdapter(adapterApp1);
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
                Uri uri = Uri.parse(exitList1.get(position).getAppLink());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {

                    Toast.makeText(BackActivity.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                }
            }
        });
        app_list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(exitList2.get(position).getAppLink());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {

                    Toast.makeText(BackActivity.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void setTimeForApp() {
        Calendar calander = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currDate = simpledateformat.format(calander.getTime());
        pref.setPrefString(PreferencesUtils.TIME_OF_GET_APP_EXIT, currDate);
    }

    @Override
    public void onBackPressed() {
        opendialogexit();
    }


    private void opendialogexit() {
        dialog = new Dialog(BackActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.customdailog2);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        AppManage.getInstance(BackActivity.this).show_NATIVE((ViewGroup) dialog.findViewById(R.id.native_container), ADMOB_N1, "");


        ((ImageView) dialog.findViewById(R.id.btn_no)).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(BackActivity.this, SplashActivity1.class));
            }
        });
        ((ImageView) dialog.findViewById(R.id.btn_yes)).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(View view) {
                AppManage.getInstance(BackActivity.this).show_INTERSTIAL(BackActivity.this, new AppManage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        startActivity(new Intent(BackActivity.this, ThankyouActivity.class));
                    }
                });


            }
        });
        dialog.show();
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}
