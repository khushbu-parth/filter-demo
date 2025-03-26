package com.pesonal.adsdk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.AdSettings;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static com.pesonal.adsdk.AppManage.mysharedpreferences;

public class ADS_SplashActivity extends AppCompatActivity {

    String bytemode = "";

    private Runnable runnable;
    private Handler refreshHandler;
    boolean is_retry;
    public static boolean need_internet = false;

    private AppOpenManager manager;
    boolean is_splash_ad_loaded = false;
    boolean on_sucess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_splash);


    }

    public void ADSinit(final Activity activity, final int cversion, final getDataListner myCallback1) {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.retry_layout, null);
        dialog.setContentView(view);
        final TextView retry_buttton = view.findViewById(R.id.retry_buttton);

        final SharedPreferences preferences = activity.getSharedPreferences("ad_pref", 0);
        final SharedPreferences.Editor editor_AD_PREF = preferences.edit();

        need_internet = preferences.getBoolean("need_internet", need_internet);

        if (!isNetworkAvailable() && need_internet) {
            is_retry = false;
            dialog.show();
        }

        mysharedpreferences = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        final boolean splash_ad = mysharedpreferences.getBoolean("app_AppOpenAdStatus", false);
        final String app_open_id = mysharedpreferences.getString("AppOpenID", "");
        if (splash_ad && !app_open_id.isEmpty() && isNetworkAvailable()) {
            loadAppOpenAd(activity, myCallback1);
        }


        dialog.dismiss();
        refreshHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (isNetworkAvailable()) {
                    is_retry = true;
                    retry_buttton.setText(activity.getString(R.string.retry));
                } else if (need_internet) {
                    dialog.show();
                    is_retry = false;
                    retry_buttton.setText(activity.getString(R.string.connect_internet));
                }
                refreshHandler.postDelayed(this, 1000);
            }
        };

        refreshHandler.postDelayed(runnable, 1000);

        retry_buttton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_retry) {
                    if (need_internet) {
                        myCallback1.reloadActivity();
                    } else {
                        myCallback1.onsuccess();
                    }


                } else {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                }
            }
        });



        Calendar calender = Calendar.getInstance();
        calender.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        String currentDate = df.format(calender.getTime());


        int addfdsf123;
        String status = mysharedpreferences.getString("firsttime", "true");
        final SharedPreferences.Editor editor = mysharedpreferences.edit();
        if (status.equals("true")) {
            editor.putString("date", currentDate).apply();
            editor.putString("firsttime", "false").apply();
            addfdsf123 = 13421;


        } else {
            String date = mysharedpreferences.getString("date", "");
            if (!currentDate.equals(date)) {
                editor.putString("date", currentDate).apply();
                addfdsf123 = 26894;
            } else {
                addfdsf123 = 87332;
            }
        }

        String akbsvl679056 = "ECC1CDF129DDA7E29DFA3C5310AD90980053869CF9393716538C2E68D6E59105239179F86B4355B92EA337A263FDE827";

        try {
            bytemode = AESSUtils.decryptA(activity, akbsvl679056);

        } catch (Exception e) {
            e.printStackTrace();
        }

        bytemode = bytemode + "?PHSUGSG6783019KG=" + activity.getPackageName();
        bytemode = bytemode + "&AFHJNTGDGD563200K=" + getKeyHash(activity);
        bytemode = bytemode + "&DTNHGNH7843DFGHBSA=" + addfdsf123;

        if (BuildConfig.DEBUG) {
            bytemode = bytemode + "&DBMNBXRY4500991G=TRSOFTAG12789I";
        } else {
            bytemode = bytemode + "&DBMNBXRY4500991G=TRSOFTAG82382I";
        }

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, bytemode, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean status = response.getBoolean("STATUS");
                    if (status == true) {

                        String need_in = response.getJSONObject("APP_SETTINGS").getString("app_needInternet");
                        if (need_in.endsWith("1")) {
                            need_internet = true;
                        } else {
                            need_internet = false;
                        }
                        editor_AD_PREF.putBoolean("need_internet", need_internet).apply();
                        editor_AD_PREF.putString("MORE_APP", response.getJSONArray("MORE_APP").toString()).apply();

                        SharedPreferences.Editor editor1 = mysharedpreferences.edit();
                        editor1.putString("response", response.toString());
                        editor1.apply();


                    } else {
                    }


                } catch (Exception e) {
                    if (need_internet) {
                        dialog.dismiss();
                        refreshHandler = new Handler();
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (isNetworkAvailable()) {
                                    is_retry = true;
                                    retry_buttton.setText(activity.getString(R.string.retry));
                                } else {
                                    dialog.show();
                                    is_retry = false;
                                    retry_buttton.setText(activity.getString(R.string.connect_internet));
                                }
                                refreshHandler.postDelayed(this, 1000);
                            }
                        };
                    } else {
                        myCallback1.onsuccess();
                    }
                }

                AppManage.getInstance(activity).getResponseFromPref(new getDataListner() {
                    @Override
                    public void onsuccess() {
                        boolean splash_ad2 = mysharedpreferences.getBoolean("app_AppOpenAdStatus", false);
                        String app_open_id2 = mysharedpreferences.getString("AppOpenID", "");
                        if (splash_ad && !app_open_id.isEmpty() && isNetworkAvailable()) {
                            if (is_splash_ad_loaded) {
                                manager.showAdIfAvailable(new AppOpenManager.splshADlistner() {
                                    @Override
                                    public void onsuccess() {
                                        myCallback1.onsuccess();
                                    }

                                    @Override
                                    public void onError(String error) {
                                        myCallback1.onsuccess();
                                    }
                                });
                            } else {
                                on_sucess = true;
                            }
                        } else if (splash_ad2 && !app_open_id2.isEmpty() && isNetworkAvailable()) {
                            on_sucess = true;
                            loadAppOpenAd(activity, myCallback1);
                        } else {
                            myCallback1.onsuccess();
                        }
                    }

                    @Override
                    public void onUpdate(String url) {
                        myCallback1.onUpdate(url);
                    }

                    @Override
                    public void onRedirect(String url) {
                        myCallback1.onRedirect(url);
                    }

                    @Override
                    public void reloadActivity() {

                    }

                    @Override
                    public void ongetExtradata(JSONObject extraData) {

                    }
                }, cversion);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (need_internet) {
                    dialog.dismiss();
                    refreshHandler = new Handler();
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (isNetworkAvailable()) {
                                is_retry = true;
                                retry_buttton.setText(activity.getString(R.string.retry));
                            } else {
                                dialog.show();
                                is_retry = false;
                                retry_buttton.setText(activity.getString(R.string.connect_internet));
                            }
                            refreshHandler.postDelayed(this, 1000);
                        }
                    };
                } else {
                    myCallback1.onsuccess();
                }
            }
        });
        jsonObjectRequest.setShouldCache(false);
        requestQueue.add(jsonObjectRequest);
    }

    private void loadAppOpenAd(Activity activity, final getDataListner myCallback1) {
        Log.e("===", "loadAppOpenAd: "+"LOFFFF" );

        manager = new AppOpenManager(activity);
        manager.fetchAd(new AppOpenManager.splshADlistner() {
            @Override
            public void onsuccess() {
                is_splash_ad_loaded = true;

                if (on_sucess) {
                    manager.showAdIfAvailable(new AppOpenManager.splshADlistner() {
                        @Override
                        public void onsuccess() {
                            Log.e("===", "onsuccess: "+"LOFFFF1" );

                            myCallback1.onsuccess();
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("===", "onError: "+"LOFFFF2" );

                            myCallback1.onsuccess();
                        }
                    });
                }

            }

            @Override
            public void onError(String error) {
                Log.e("===", "onError: "+"LOFFFF3" );

                is_splash_ad_loaded = true;
                if (on_sucess) {
                    manager.showAdIfAvailable(new AppOpenManager.splshADlistner() {
                        @Override
                        public void onsuccess() {

                            myCallback1.onsuccess();
                        }

                        @Override
                        public void onError(String error) {
                            myCallback1.onsuccess();
                        }
                    });
                }

            }
        });
    }

    private String getKeyHash(Activity activity) {
        PackageInfo info;
        try {
            info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = (Base64.encodeToString(md.digest(), Base64.NO_WRAP));
                return something.replace("+", "*");
            }
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();

        } catch (NoSuchAlgorithmException e) {

        } catch (Exception e) {

        }
        return null;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refreshHandler.removeCallbacks(runnable);
    }

}