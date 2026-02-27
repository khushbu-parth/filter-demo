package com.pu.casttotv.tvcast.screenmirror.tvremote.screen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.ConnectActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.DialogDisconnect;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.photoonl.PhotoOnlineActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.youtube_browser.YoutubeBrowserActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;

public class WebCastingActivity extends AppCompatActivity {
    ImageView imvConnect;
    LinearLayout llBack, llConnect, llYoutube, llPhotoOnline, llVimeo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_casting);

        imvConnect = findViewById(R.id.imvConnect);

        llBack = findViewById(R.id.llBack);
        llBack.setOnClickListener(v -> onBackPressed());

        llConnect = findViewById(R.id.llConnect);
        llConnect.setOnClickListener(v -> {
            if (TVConnectUtils.getInstance().isConnected()) {
                new DialogDisconnect(this).show();
                return;
            }
            Intent intent = new Intent(this, ConnectActivity.class);
            startActivity(intent);
            Utils.nextScreen(this);
        });


        llYoutube = findViewById(R.id.llYoutube);
        llYoutube.setOnClickListener(v -> {
            Intent intent = new Intent(this, YoutubeBrowserActivity.class);
            intent.putExtra("browser_type", "youtube");
            startActivity(intent);
            Utils.nextScreen(this);
        });

        llPhotoOnline = findViewById(R.id.llPhotoOnline);
        llPhotoOnline.setOnClickListener(v -> {
            Intent intent = new Intent(this, PhotoOnlineActivity.class);
            startActivity(intent);
            Utils.nextScreen(this);
        });

        llVimeo = findViewById(R.id.llVimeo);
        llVimeo.setOnClickListener(v -> {
            Intent intent = new Intent(this, YoutubeBrowserActivity.class);
            intent.putExtra("browser_type", "vimeo");
            startActivity(intent);
            Utils.nextScreen(this);
        });

    }
}