package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.InterstitialAd;
import com.pesonal.adsdk.ADS_SplashActivity;
import com.pesonal.adsdk.getDataListner;

import org.json.JSONObject;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.TokanData.SendAppToken;


public class SplashScreen extends ADS_SplashActivity {
    private SharedPreferences sp;
    private String gm;
    static SharedPreferences.Editor editor;
    int i = 0;


    private void setView() {
        setContentView(R.layout.activity_splash_screen);
        setStoreToken(getString(R.string.app_name));
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setView();


        ADSinit(SplashScreen.this, getCurrentVersionCode(), new getDataListner() {
            @Override
            public void onsuccess() {
                startActivity(new Intent(SplashScreen.this, SplashActivity1.class));
                finish();

            }

            @Override
            public void onUpdate(String url) {
                Log.e("my_log", "onUpdate: " + url);
                showUpdateDialog(url);
            }

            @Override
            public void onRedirect(String url) {
                Log.e("my_log", "onRedirect: " + url);
                showRedirectDialog(url);
            }

            @Override
            public void reloadActivity() {
                startActivity(new Intent(SplashScreen.this, SplashScreen.class));
                finish();
            }

            @Override
            public void ongetExtradata(JSONObject extraData) {
                Log.e("my_log", "ongetExtradata: " + extraData.toString());
            }
        });

    }


    public void showRedirectDialog(final String url) {
        final Dialog dialog = new Dialog(SplashScreen.this);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.installnewappdialog, null);
        dialog.setContentView(view);
        TextView update = view.findViewById(R.id.update);
        TextView txt_title = view.findViewById(R.id.txt_title);
        TextView txt_decription = view.findViewById(R.id.txt_decription);

        update.setText("Install Now");
        txt_title.setText("Install our new app now and enjoy");
        txt_decription.setText("We have transferred our server, so install our new app by clicking the button below to enjoy the new features of app.");


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri marketUri = Uri.parse(url);
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                } catch (ActivityNotFoundException ignored1) {
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public void showUpdateDialog(final String url) {

        final Dialog dialog = new Dialog(SplashScreen.this);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.installnewappdialog, null);
        dialog.setContentView(view);
        TextView update = view.findViewById(R.id.update);
        TextView txt_title = view.findViewById(R.id.txt_title);
        TextView txt_decription = view.findViewById(R.id.txt_decription);

        update.setText("Update Now");
        txt_title.setText("Update our new app now and enjoy");
        txt_decription.setText("");
        txt_decription.setVisibility(View.GONE);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri marketUri = Uri.parse(url);
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                } catch (ActivityNotFoundException ignored1) {
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public int getCurrentVersionCode() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    getPackageName(), 0);
            return info.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private void setStoreToken(String token) {
        sp = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        gm = sp.getString("gm", "");
        if (i == 0) {
            if (gm.equals("")) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("gm", "0"); // Store
                editor.commit();
                gm = sp.getString("gm", "");
            } else {
            }
        }
        if (isOnline()) {
            try {
                if (gm.equals("0")) {
                    new SendAppToken(getApplicationContext()).execute(token);
                    editor = sp.edit();
                    editor.putString("gm", "1"); // Store
                    editor.commit();
                } else {
                }
            } catch (Exception e) {
            }
        } else {
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

}