package com.co.casttotv.screenmirroring.mirroring.cast.activities;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
public abstract class BaseActivity extends AppCompatActivity {
    NetworkReceiver receiver = new NetworkReceiver();
    IntentFilter mIntentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onViewCreate(savedInstanceState);
    }

    protected abstract void onViewCreate(Bundle savedInstanceState);

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, mIntentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (activeNetworkInfo == null || activeNetworkInfo.getType() != 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("No Internet");
                builder.setMessage("Internet connection is lost, Please check your network resource and try again.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });
                AlertDialog create = builder.create();
                if (isFinishing()) {
                    return;
                }
                create.show();
            } else {
//                WebServer.startServer();
            }
        }
    }
}
