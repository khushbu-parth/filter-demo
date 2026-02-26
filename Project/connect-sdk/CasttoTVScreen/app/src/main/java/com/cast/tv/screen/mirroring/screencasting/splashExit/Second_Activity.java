package com.cast.tv.screen.mirroring.screencasting.splashExit;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.cast.tv.screen.mirroring.screencasting.BuildConfig;
import com.cast.tv.screen.mirroring.screencasting.R;
import com.cast.tv.screen.mirroring.screencasting.UI.main.MainCastActivity;
import com.library.info.CastTvAppManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class Second_Activity extends AppCompatActivity implements View.OnClickListener {

    private ImageView share_first_image, rateus_first_image, privacy_first_image;
    private ImageView start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getWindow().setFlags(1024, 1024);

        CastTvAppManager.getInstance(this).showNativeAds(this, findViewById(R.id.fl_native_banner), findViewById(R.id.native_space_img), 1);

        start = findViewById(R.id.start);
        share_first_image = findViewById(R.id.share_first_image);
        rateus_first_image = findViewById(R.id.rateus_first_image);
        privacy_first_image = findViewById(R.id.privacy_first_image);
        start.setOnClickListener(this);
        share_first_image.setOnClickListener(this);
        rateus_first_image.setOnClickListener(this);
        privacy_first_image.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                CastTvAppManager.getInstance(this).showInterstitialAd(this, () -> {
                    startActivity(new Intent(Second_Activity.this, MainCastActivity.class));
                    finish();
                });

                break;
            case R.id.share_first_image:
                share();
                break;
            case R.id.rateus_first_image:
                gotoStore();
                break;
            case R.id.privacy_first_image:
                if (isOnline()) {
                    CastTvAppManager.getInstance(this).showInterstitialAd(this, () -> {
                        startActivity(new Intent(Second_Activity.this, Web_Activity.class));
                    });

                } else {
                    Toast.makeText(Second_Activity.this, "No Internet Connection..", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void share() {
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.banner);
        Log.e("TAG", "onClick: " + b.toString());
        try {
            b.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
            Uri parse = FileProvider.getUriForFile(Second_Activity.this, BuildConfig.APPLICATION_ID + ".provider", saveImageExternal(b));
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, parse);
            intent.putExtra(Intent.EXTRA_TEXT, Glob.app_name + "\nCreated By : " + Glob.app_link);
            startActivity(Intent.createChooser(intent, "Share with"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("====", "onClick: " + e);
        }
    }

    public void gotoStore() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        myAppLinkToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @SuppressLint("LogNotTimber")
    private File saveImageExternal(Bitmap image) {
        File file = null;
        try {
            file = new File(Second_Activity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (IOException e) {
            Log.d("=====", "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return file;
    }

    @Override
    public void onBackPressed() {
        CastTvAppManager.getInstance(this).showInterstitialBackAd(this, () -> {
            startActivity(new Intent(Second_Activity.this, Exit_Activity.class));
            finish();
        });

    }
}