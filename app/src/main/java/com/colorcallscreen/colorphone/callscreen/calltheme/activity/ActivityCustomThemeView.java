package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ThemeModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Helper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.SwipeUpHelper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.File;

import pl.droidsonroids.gif.GifImageView;

public class ActivityCustomThemeView extends BaseActivity implements View.OnClickListener {
    String APPLIED;
    String APPLY;
    Button btnApplied;
    private Button chooseContact;
    Context context;
    private ProgressBar custom_loader;
    private int defaultTheme;
    private String downloadedTheme;
    GifImageView gifImageView;
    ImageView iVCallAccept;
    ImageView iVCallDecline;
    private ThemeModel model;
    private BroadcastReceiver receiver;
    private TextView tcvUserNo;
    String themeType;
    String themeUrl;
    private TextView tvUserName;
    CircleImageView userimg;
    boolean isDownloaded = false;
    boolean isThemeApplied = false;
    boolean isCustomTheme = false;
    boolean isDefaultTheme = false;

    private void autoDownlod() {
        if (!this.isDownloaded) {
            if (Utility.isInternetEnabled(this.context)) {
                downloadTheme();
                return;
            } else {
                Snackbar.make(this.gifImageView, this.context.getResources().getString(R.string.no_internet), -2).show();
                return;
            }
        }
        showButton();
    }

    private void buttonEvent() {
        if (this.isThemeApplied) {
            Toast.makeText(this.context, "Already applied", Toast.LENGTH_SHORT).show();
        } else if (this.isDefaultTheme) {
            Helper.applyTheme(this.defaultTheme);
            ActivityCustomThemeView activityCustomThemeView = ActivityCustomThemeView.this;
            activityCustomThemeView.chageButtonText(activityCustomThemeView.APPLIED);
            PreferenceUtils.getInstance().putPreference(Constants.APPLIED_THEME_NAME, this.model.getThemeImage());
            PreferenceUtils.getInstance().putPreference(Constants.APPLIED_THEME_TYPE, ThemeModel.THEME_TYPE.DEFAULT.toString());
        } else if (this.isCustomTheme) {
            ThemeModel themeModel = new ThemeModel();
            themeModel.setThumbnail("custom");
            themeModel.setThemeImage("custom");
            themeModel.setName("Custom");
            themeModel.setSource("custom");
            themeModel.setContentType("image");
            themeModel.setColor("ffffff");
            themeModel.setPersonName("Jessica");
            themeModel.setPersonPhoneNumber("626 202 6888");
            themeModel.setPersonImage("p1");
            themeModel.setCategory(Constants.THEME_KEY);
            Helper.applyTheme(Helper.getRealPathFromURI(this.context, this.themeUrl));
            ActivityCustomThemeView activityCustomThemeView = ActivityCustomThemeView.this;
            activityCustomThemeView.chageButtonText(activityCustomThemeView.APPLIED);
            PreferenceUtils.getInstance().putPreference(Constants.APPLIED_THEME_NAME, "Custom");
            PreferenceUtils.getInstance().putPreference(Constants.APPLIED_THEME_TYPE, "custom");
            ActivityFullScreenView.setThemeModel(themeModel);
            this.isThemeApplied = true;
        } else {
            if (!this.isDownloaded) {
                downloadTheme();
            }
            if (this.isDownloaded) {
                Helper.applyTheme(this.downloadedTheme);
                this.isThemeApplied = true;
                ActivityCustomThemeView activityCustomThemeView = ActivityCustomThemeView.this;
                activityCustomThemeView.chageButtonText(activityCustomThemeView.APPLIED);
                PreferenceUtils.getInstance().putPreference(Constants.APPLIED_THEME_NAME, this.model.getThemeImage());
                PreferenceUtils.getInstance().putPreference(Constants.APPLIED_THEME_TYPE, ThemeModel.THEME_TYPE.ONLINE.toString());
            }
        }
    }

    public void chageButtonText(String str) {
        this.btnApplied.setText(str);
    }

    private void checkStatus(int i) {
        if (Helper.isDefaultThemeApplied(i)) {
            chageButtonText(this.APPLIED);
            this.isThemeApplied = true;
            return;
        }
        chageButtonText(this.APPLY);
    }

    private void downloadTheme() {
        Helper.downloadTheme(this.context, this.themeUrl, this.model.getThemeImage());
    }

    private String getIfThemeDownloaded() {
        for (String str : Helper.getDownloadedTheme()) {
            ThemeModel themeModel = this.model;
            if (themeModel != null && str.contains(themeModel.getThemeImage())) {
                this.isDownloaded = true;
                chageButtonText(this.APPLY);
                return str;
            }
        }
        return null;
    }

    private boolean isThemeApplied(String str) {
        boolean isThemeApplied = Helper.isThemeApplied(str);
        this.isThemeApplied = isThemeApplied;
        if (isThemeApplied) {
            chageButtonText(this.APPLIED);
        }
        return this.isThemeApplied;
    }

    public void openContact() {
        Intent intent = new Intent(this, ActivityThemeContactList.class);
        intent.putExtra("_tn", this.model.getName());
        startActivityForResult(intent, 49);
    }

    public void showButton() {
        this.btnApplied.setVisibility(View.VISIBLE);
        this.custom_loader.setVisibility(View.GONE);
    }

    public void showIfThemeDownloaded() {
        String ifThemeDownloaded = getIfThemeDownloaded();
        this.downloadedTheme = ifThemeDownloaded;
        if (!this.isDownloaded || ifThemeDownloaded == null) {
            return;
        }
        if (this.gifImageView == null) {
            this.gifImageView = (GifImageView) findViewById(R.id.gifImageView);
        }
        this.gifImageView.setImageURI(Uri.fromFile(new File(this.downloadedTheme)));
    }

    @Override 
    public void onClick(View view) {
        if (view.getId() == R.id.btnApplied) {
            buttonEvent();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.incomig_call_flash_view);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        this.context = this;
        this.APPLIED = getResources().getString(R.string.applied);
        this.APPLY = getResources().getString(R.string.apply);
        this.gifImageView = (GifImageView) findViewById(R.id.gifImageView);
        this.userimg = (CircleImageView) findViewById(R.id.circleImageView);
        this.tvUserName = (TextView) findViewById(R.id.tvUserName);
        this.tcvUserNo = (TextView) findViewById(R.id.tcvUserNo);
        this.custom_loader = (ProgressBar) findViewById(R.id.custom_loader);
        Button button = (Button) findViewById(R.id.btnApplied);
        this.btnApplied = button;
        button.setOnClickListener(this);
        this.iVCallDecline = (ImageView) findViewById(R.id.iVCallDecline);
        this.iVCallAccept = (ImageView) findViewById(R.id.iVCallAccept);
        new SwipeUpHelper(this, this.iVCallDecline).start(new SwipeUpHelper.SwipeCompeleteListner() {
            @Override
            public void onSwipeUpComplete() {
                Utility.vibrate(ActivityCustomThemeView.this.context);
            }
        });
        this.iVCallDecline.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                Utility.vibrate(ActivityCustomThemeView.this.context);
            }
        });
        new SwipeUpHelper(this, this.iVCallAccept).start(new SwipeUpHelper.SwipeCompeleteListner() {
            @Override
            public void onSwipeUpComplete() {
                Utility.vibrate(ActivityCustomThemeView.this.context);
            }
        });
        this.iVCallAccept.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                Utility.vibrate(ActivityCustomThemeView.this.context);
            }
        });
        Button button2 = (Button) findViewById(R.id.choose);
        this.chooseContact = button2;
        button2.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ActivityCustomThemeView.this.openContact();
            }
        });
        this.userimg.setVisibility(View.VISIBLE);
        int resourceByName = Utility.getResourceByName(this, "p5", "raw");
        if (resourceByName != -1) {
            Picasso.get().load(resourceByName).into(this.userimg);
        }
        boolean booleanExtra = getIntent().getBooleanExtra("custom", false);
        this.isCustomTheme = booleanExtra;
        if (booleanExtra) {
            this.themeUrl = getIntent().getStringExtra(Constants.IMG_MAIN);
            try {
                Picasso.get().load((Uri) getIntent().getParcelableExtra(Constants.IMG_MAIN_URL)).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(this.gifImageView);
                showButton();
                chageButtonText(this.APPLY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.themeType = getIntent().getStringExtra(Constants.THEME_TYPE);
            this.tvUserName.setText(this.model.getPersonName());
            this.tcvUserNo.setText(this.model.getPersonPhoneNumber());
            if (this.themeType.equals(ThemeModel.THEME_TYPE.ONLINE.toString())) {
                String stringExtra = getIntent().getStringExtra(Constants.IMG_THUMBNAIL);
                this.themeUrl = getIntent().getStringExtra(Constants.IMG_MAIN);
                Picasso.get().load(Utility.getResourceByName(this.context, this.model.getPersonImage(), "raw")).into(this.userimg);
                if (stringExtra != null) {
                    Picasso.get().load(stringExtra).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(this.gifImageView);
                }
                showIfThemeDownloaded();
                autoDownlod();
                isThemeApplied(this.model.getThemeImage());
            }
            if (this.themeType.equals(ThemeModel.THEME_TYPE.DEFAULT.toString())) {
                showButton();
                this.userimg.setImageResource(this.model.getDefaultPerson());
                int intExtra = getIntent().getIntExtra(Constants.IMG_MAIN, 0);
                this.defaultTheme = intExtra;
                if (intExtra != 0) {
                    Picasso.get().load(this.defaultTheme).placeholder(R.drawable.placeholder).into(this.gifImageView);
                }
                this.isDefaultTheme = true;
                checkStatus(this.defaultTheme);
            }
        }
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityCustomThemeView.9
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                ActivityCustomThemeView.this.showButton();
                ActivityCustomThemeView activityCustomThemeView = ActivityCustomThemeView.this;
                activityCustomThemeView.chageButtonText(activityCustomThemeView.APPLY);
                ActivityCustomThemeView.this.showIfThemeDownloaded();
            }
        };
        this.receiver = broadcastReceiver;
        try {
            registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
        } catch (Exception unused) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.gifImageView.setImageBitmap(null);
        this.context = null;
        this.userimg = null;
        this.gifImageView = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        BroadcastReceiver broadcastReceiver = this.receiver;
        if (broadcastReceiver != null && broadcastReceiver.isOrderedBroadcast()) {
            try {
                unregisterReceiver(this.receiver);
            } catch (Exception unused) {
            }
        }
    }
}
