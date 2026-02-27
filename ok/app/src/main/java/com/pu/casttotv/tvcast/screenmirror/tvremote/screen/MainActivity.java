package com.pu.casttotv.tvcast.screenmirror.tvremote.screen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.gson.Gson;
import com.pu.casttotv.tvcast.screenmirror.tvremote.BuildConfig;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.Ad_AppPurchaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.Ad_ExitActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.Ad_SplashActivity;
import com.adsdemo.vdapps.adsload.Ad_Dialogs;
import com.adsdemo.vdapps.adsload.Ad_Globals;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.adsdemo.vdapps.adsload.MoreApps.Ad_PlayStoreActivity;
import com.adsdemo.vdapps.adsload.interfaces.MyCallback;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.Globle.Ad_Dialog;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.drawer.views.DuoDrawerLayout;
import com.pu.casttotv.tvcast.screenmirror.tvremote.drawer.views.DuoMenuView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.drawer.widgets.DuoDrawerToggle;
import com.pu.casttotv.tvcast.screenmirror.tvremote.fcm.broadcast.PushNotificationReceiver;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.ModelSaleAll;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.audio.AudioActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.ConnectActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.DialogDisconnect;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.photoff.PhotoOfflineActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror.MenuMirrorActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.setting.SettingActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.videooff.VideoOfflineActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.youtube_browser.YoutubeBrowserActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.SharedPrefsUtil;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;


@SuppressLint("WrongConstant")
public class MainActivity extends BaseActivity {

    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;

        ViewHolder() {
            mDuoDrawerLayout = findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();

        }
    }

    private static final int APP_UPDATE_REQ_CODE = 123;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    ImageView imvConnect, llSetting;
    LinearLayout llDrawer, llConnect, llWebCasting, llMusic, llPhotoOff, llScreenMirror, llVideo, llYoutube, llPhotoOnline, llVimeo, ivInAppPurchase/*,llIPTV*/;
    DuoDrawerLayout drawer;
    ViewHolder mViewHolder;

    LinearLayout llApplayout, llSliderad;
    ImageView ivAppicon;
    int count = 0;
    TextView tvAppname;

    private FrameLayout iv_adtop;
    private Thread thread;
    private TextView txtVersion;
    private TextView tv_countslider;
    SharedPreferences.Editor myEdit;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            getActionBar().hide();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        AdsManager.CallNativeAdLoad(this, findViewById(R.id.native_container), AdsManager.NATIVE_BIG);

        initView();
        EventBus.getDefault().register(this);

        AdsMainload();
        ADCommanMethod();

        Ad_Dialogs.setRateDialog(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeInstallStateUpdateListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 68:
                if (grantResults.length > 0) {
                    boolean locationAccepted;
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
                        locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                                && grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    } else {
                        locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                                && grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    }
                    if (locationAccepted)
                        Toast.makeText(this, "Permission Granted, Now you can access Storage data.", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(this, "Permission Denied, You cannot access storage data.", Toast.LENGTH_SHORT).show();
                        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    (dialog, which) -> {
                                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
                                            requestPermissions(new String[]{
                                                    Manifest.permission.READ_MEDIA_AUDIO,
                                                    Manifest.permission.READ_MEDIA_IMAGES,
                                                    Manifest.permission.READ_MEDIA_VIDEO
                                            }, 68);
                                            return;
                                        }
                                        requestPermissions(new String[]{
                                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        }, 68);
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                finishAffinity();
                System.exit(0);
            }
        }
    }

    @Override
    public void onBackPressed() {
        int i = Ad_Globals.getRateClick(this, "checkRateClick");
        if (i == 0) {
            Ad_Dialog.setExitRateDialog(this);
            int newi = (i + 1);
            Ad_Globals.setRateClick(this, newi, "checkRateClick");
        } else {
            if (AdsManager.ExitScreen == 1) {
                startActivity(new Intent(this, Ad_ExitActivity.class));
            } else {
                Ad_Dialogs.setExitDialog(this);
            }
            if (i == 3) {
                Ad_Globals.setRateClick(this, 0, "checkRateClick");
                return;
            }
            int newi = (i + 1);
            Ad_Globals.setRateClick(this, newi, "checkRateClick");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (imvConnect == null) {
            return;
        }
        if (TVConnectUtils.getInstance().isConnected()) {
            imvConnect.setImageResource(R.drawable.hover_screen);
        } else {
            imvConnect.setImageResource(R.drawable.screen);
        }
        AdsManager.pref = getSharedPreferences("counter", Context.MODE_PRIVATE);
        AdsManager.finalAppCouner = AdsManager.pref.getInt("lang_us", 0);
        if (AdsManager.finalAppCouner > 0) {
            AdsManager.finalAppCouner--;
            tv_countslider.setText(String.valueOf(AdsManager.finalAppCouner));
        } else {
            tv_countslider.setText(String.valueOf(AdsManager.totalAppCouner - 1));
        }
        //AdsManager.show_gift_header(this, (FrameLayout) findViewById(R.id.iv_adtop), (TextView) findViewById(R.id.tv_counttop), View.INVISIBLE);
        AdsManager.show_gift_header(this, (FrameLayout) findViewById(R.id.iv_adtop), (TextView) findViewById(R.id.tv_counttop), View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessage().contains("KEY_CONNECT")) {
            if (TVConnectUtils.getInstance().isConnected()) {
                imvConnect.setImageResource(R.drawable.hover_screen);
            } else {
                imvConnect.setImageResource(R.drawable.screen);
            }
        }
    }

    private void initView() {

        imvConnect = findViewById(R.id.imvConnect);
        drawer = findViewById(R.id.drawer);

        ivInAppPurchase = findViewById(R.id.ivInAppPurchase);
        ivInAppPurchase.setOnClickListener(v -> {
            Intent intent = new Intent(this, AppPurchaseActivity.class);
            startActivity(intent);
            Utils.nextScreen(this);
        });

        mViewHolder = new ViewHolder();

        handleMenu();
        handleDrawer();

        llDrawer = findViewById(R.id.llDrawer);
        llDrawer.setOnClickListener(v -> {
            drawewrOpenClose();
        });

        llConnect = findViewById(R.id.llConnect);
        llConnect.setOnClickListener(v -> {

            if (TVConnectUtils.getInstance().isConnected()) {
                new DialogDisconnect(this).show();
                return;
            }
            AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                gotoActivityMain(ConnectActivity.class);
            });

        });

        llScreenMirror = findViewById(R.id.llScreenMirror);
        llScreenMirror.setOnClickListener(v -> AdsManager.CallInterstitialAdLoad(this, 0, () -> {
            gotoActivityMain(MenuMirrorActivity.class);
        }));

        llPhotoOff = findViewById(R.id.llPhotoOff);
        llPhotoOff.setOnClickListener(v -> {
            if (checkPermission()) {
                AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                    gotoActivityMain(PhotoOfflineActivity.class);
                });
            } else {
                askPermission();
            }
        });

        llVideo = findViewById(R.id.llVideo);
        llVideo.setOnClickListener(v -> {
            if (checkPermission()) {
                AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                    gotoActivityMain(VideoOfflineActivity.class);
                });
            } else {
                askPermission();
            }
        });

        llMusic = findViewById(R.id.llMusic);
        llMusic.setOnClickListener(v -> {
            if (checkPermission()) {
                gotoActivityMain(AudioActivity.class);
            } else {
                askPermission();
            }
        });

        llYoutube = findViewById(R.id.llYoutube);
        llYoutube.setOnClickListener(v -> {
            AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                Intent intent = new Intent(this, YoutubeBrowserActivity.class);
                intent.putExtra("browser_type", "youtube");
                startActivity(intent);
                Utils.nextScreen(this);
            });
        });

        llPhotoOnline = findViewById(R.id.llPhotoOnline);
        llPhotoOnline.setOnClickListener(v -> {
            AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                Intent intent = new Intent(this, YoutubeBrowserActivity.class);
                intent.putExtra("browser_type", "googlePhoto");
                startActivity(intent);
                Utils.nextScreen(this);
            });
        });

        llVimeo = findViewById(R.id.llVimeo);
        llVimeo.setOnClickListener(v -> {
            AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                Intent intent = new Intent(this, YoutubeBrowserActivity.class);
                intent.putExtra("browser_type", "vimeo");
                startActivity(intent);
                Utils.nextScreen(this);
            });
        });

        llSetting = findViewById(R.id.llSetting);
        llSetting.setOnClickListener(v -> {
            AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                gotoActivityMain(SettingActivity.class);
            });

        });


//        llIPTV = findViewById(R.id.llIPTV);
//        llIPTV.setOnClickListener(v -> {
//            typeToDetail = 7;
//            gotoActivityMain(RemoteActivity.class);
//            return;
//        });


        try {
            ModelSaleAll supperSale = SharedPrefsUtil.getInstance().getSupperSale();
            new Gson().toJson(supperSale);
            if (supperSale != null && supperSale.isStatus()) {
                setAlarm(this, supperSale);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void ADCommanMethod() {
        iv_adtop = findViewById(R.id.iv_adtop);
        tv_countslider = findViewById(R.id.tv_countslider);
        //Drawer Layout
        txtVersion = drawer.findViewById(R.id.txtVersion);
        txtVersion.setText("Version " + BuildConfig.VERSION_NAME + "");
        tvAppname = drawer.findViewById(R.id.tv_appname);
        ivAppicon = drawer.findViewById(R.id.iv_appicon);
        llApplayout = drawer.findViewById(R.id.ll_applayout);
        llSliderad = drawer.findViewById(R.id.ll_sliderad);

        if (AdsManager.moreAllList != null && AdsManager.moreAllList.size() != 0) {
            if (AdsManager.MoreAds == 1) {
                iv_adtop.setVisibility(View.VISIBLE);
                llApplayout.setVisibility(View.VISIBLE);
                llSliderad.setVisibility(View.VISIBLE);
            } else if (AdsManager.MoreAds == 2) {
                iv_adtop.setVisibility(View.VISIBLE);
                llApplayout.setVisibility(View.GONE);
                llSliderad.setVisibility(View.GONE);
            } else if (AdsManager.MoreAds == 3) {
                llSliderad.setVisibility(View.VISIBLE);
                iv_adtop.setVisibility(View.GONE);
                llApplayout.setVisibility(View.GONE);
            } else if (AdsManager.MoreAds == 4) {
                llSliderad.setVisibility(View.GONE);
                iv_adtop.setVisibility(View.GONE);
                llApplayout.setVisibility(View.VISIBLE);
            } else if (AdsManager.MoreAds == 23 || AdsManager.MoreAds == 32) {
                iv_adtop.setVisibility(View.VISIBLE);
                llSliderad.setVisibility(View.VISIBLE);
                llApplayout.setVisibility(View.GONE);
            } else if (AdsManager.MoreAds == 24 || AdsManager.MoreAds == 42) {
                iv_adtop.setVisibility(View.VISIBLE);
                llApplayout.setVisibility(View.VISIBLE);
                llSliderad.setVisibility(View.GONE);
            } else if (AdsManager.MoreAds == 34 || AdsManager.MoreAds == 43) {
                llSliderad.setVisibility(View.VISIBLE);
                llApplayout.setVisibility(View.VISIBLE);
                iv_adtop.setVisibility(View.GONE);
            }

        } else {
            iv_adtop.setVisibility(View.GONE);
            llApplayout.setVisibility(View.GONE);
            llSliderad.setVisibility(View.GONE);
        }
        if (AdsManager.AppPurchaseScreen == 1) {
            ivInAppPurchase.setVisibility(View.VISIBLE);
        } else {
            ivInAppPurchase.setVisibility(View.GONE);
        }

        iv_adtop.setOnClickListener(v -> {
            AdsManager.CallInterstitialAdLoad(MainActivity.this, 0, new MyCallback() {
                @Override
                public void callbackCall() {
                    startActivity(new Intent(MainActivity.this, Ad_PlayStoreActivity.class));
                }
            });
        });

        ivInAppPurchase.setOnClickListener(v -> {
            AdsManager.CallInterstitialAdLoad(MainActivity.this, 0, new MyCallback() {
                @Override
                public void callbackCall() {
                    startActivity(new Intent(MainActivity.this, Ad_AppPurchaseActivity.class));
                }
            });
        });
        if (AdsManager.moreAllList == null || AdsManager.moreAllList.size() <= 0) {
            llApplayout.setVisibility(View.GONE);
            llSliderad.setVisibility(View.GONE);
        } else {
            llApplayout.setVisibility(View.VISIBLE);
            llSliderad.setVisibility(View.VISIBLE);
        }


        llSliderad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Ad_PlayStoreActivity.class));
            }
        });
        llApplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + AdsManager.moreAllList.get(count).app_packageName);
                    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(myAppLinkToMarket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        boolean a = sharedPreferences.getBoolean("chack", false);
        Log.e("sssss", "ssss: " + a);

        if (!a) {
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putBoolean("chack", true);
            myEdit.commit();
        }

    }

    public void drawewrOpenClose() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            drawer.openDrawer(GravityCompat.END);
        }
    }

    private void handleMenu() {
        mViewHolder.mDuoMenuView.setOnMenuClickListener(type -> {
            if (type.equals("1")) {
                AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                    gotoActivityMain(MenuMirrorActivity.class);
                });
            } else if (type.equals("2")) {
                if (checkPermission()) {
                    AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                        gotoActivityMain(PhotoOfflineActivity.class);
                    });
                } else {
                    askPermission();
                }
            } else if (type.equals("3")) {
                if (checkPermission()) {
                    AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                        gotoActivityMain(VideoOfflineActivity.class);
                    });
                } else {
                    askPermission();
                }
            } else if (type.equals("4")) {
                if (checkPermission()) {
                    AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                        gotoActivityMain(AudioActivity.class);
                    });
                } else {
                    askPermission();
                }
            } else if (type.equals("5")) {
                AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                    Intent intent = new Intent(this, YoutubeBrowserActivity.class);
                    intent.putExtra("browser_type", "youtube");
                    startActivity(intent);
                    Utils.nextScreen(this);
                });
            } else if (type.equals("6")) {
                AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                    Intent intent = new Intent(this, YoutubeBrowserActivity.class);
                    intent.putExtra("browser_type", "googlePhoto");
                    startActivity(intent);
                    Utils.nextScreen(this);
                });
            } else if (type.equals("7")) {
                AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                    Intent intent = new Intent(this, YoutubeBrowserActivity.class);
                    intent.putExtra("browser_type", "vimeo");
                    startActivity(intent);
                    Utils.nextScreen(this);
                });
            } else if (type.equals("8")) {
                AdsManager.CallInterstitialAdLoad(this, 0, () -> {
                    gotoActivityMain(SettingActivity.class);
                });
            }
        });
    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();

        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Respond when the drawer's position changes
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                try {
                    thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                while (!thread.isInterrupted()) {
                                    Thread.sleep(10000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (AdsManager.moreAllList != null) {
                                                count++;
                                                if (count == AdsManager.moreAllList.size()) {
                                                    count = 0;
                                                }
                                            }

                                            if (AdsManager.moreAllList.size() > 0) {
                                                ((TextView) findViewById(R.id.tv_appname)).setText(AdsManager.moreAllList.get(count).app_name);
                                                Glide.with(MainActivity.this)
                                                        .load(AdsManager.moreAllList.get(count).app_logo)
                                                        .apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher))
                                                        .into((ImageView) findViewById(R.id.iv_appicon));
                                            }
                                        }
                                    });
                                }
                            } catch (InterruptedException e) {
                            }
                        }
                    };
                    thread.start();


                    if (AdsManager.moreAllList == null || AdsManager.moreAllList.size() == 0) {
                        tvAppname.setText(getResources().getString(R.string.app_name));
                        Glide.with(MainActivity.this)
                                .load(R.mipmap.ic_launcher)
                                .apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher))
                                .into((ImageView) findViewById(R.id.iv_appicon));
                    } else {
                        tvAppname.setText(AdsManager.moreAllList.get(count).app_name);
                        Glide.with(MainActivity.this)
                                .load(AdsManager.moreAllList.get(count).app_logo)
                                .apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher))
                                .into((ImageView) findViewById(R.id.iv_appicon));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                try {
                    thread.interrupt();
                    if (AdsManager.moreAllList != null) {
                        count++;
                        if (count == AdsManager.moreAllList.size()) {
                            count = 0;

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Respond when the drawer motion state changes
            }
        });

    }

    public void setAlarm(Context context, ModelSaleAll modelSaleAll) {
        Calendar instance = Calendar.getInstance();
        instance.set(11, 19);
        instance.set(12, 30);
        if (modelSaleAll != null) {
            instance.set(11, modelSaleAll.getHour());
            instance.set(12, modelSaleAll.getMinute());
        }
        instance.set(13, 0);
        instance.set(14, 0);
        if (Calendar.getInstance().after(instance)) {
            instance.add(5, 1);
        }
        ((AlarmManager) context.getSystemService("alarm")).setRepeating(0, instance.getTimeInMillis(), 86400000, PendingIntent.getBroadcast(context, 10000, new Intent(context, PushNotificationReceiver.class), 134217728));
    }

    private void gotoActivityMain(Class cls) {
        Intent intent = new Intent(this, cls);
        if (cls == YoutubeBrowserActivity.class) {
            intent.putExtra("browser_type", "youtube");
        }
        startActivity(intent);
        Utils.nextScreen(this);
    }

    private void AdsMainload() {
        if (AdsManager.AllAsdStutas == 1) {
            if (AdsManager.app_updateAppDialogStatus == 1) {
                appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
                installStateUpdatedListener = state -> {
                    if (state.installStatus() == InstallStatus.INSTALLED) {
                        removeInstallStateUpdateListener();
                    }
                };

                appUpdateManager.registerListener(installStateUpdatedListener);
                com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

                appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        startUpdateFlow(appUpdateInfo);
                    }
                });
            }

        } else {
            appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
            installStateUpdatedListener = state -> {
                if (state.installStatus() == InstallStatus.INSTALLED) {
                    removeInstallStateUpdateListener();
                }
            };

            appUpdateManager.registerListener(installStateUpdatedListener);
            com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    startUpdateFlow(appUpdateInfo);
                }
            });
        }
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void removeInstallStateUpdateListener() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_AUDIO);
            int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES);
            int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO);
            return result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED;
        }
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void askPermission() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
            }, 68);
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 68);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


}