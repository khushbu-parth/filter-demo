package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.content.ContentProviderOperation;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.ads.control.ads.AperoAd;
import com.ads.control.ads.AperoAdCallback;
import com.ads.control.ads.bannerAds.AperoBannerAdView;
import com.ads.control.ads.wrapper.ApAdError;
import com.ads.control.ads.wrapper.ApInterstitialAd;
import com.ads.control.config.AperoAdConfig;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ViewPagerAdapter;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogComponent;
import com.colorcallscreen.colorphone.callscreen.calltheme.fragments.ContactFragment;
import com.colorcallscreen.colorphone.callscreen.calltheme.fragments.MainFragment;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.ThemeDownloadService;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.CallLogUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.Splash.StartActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactLoaderBuilder;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.fragments.DialerFragment;
import com.colorcallscreen.colorphone.callscreen.calltheme.fragments.FavoriteFragment;
import com.colorcallscreen.colorphone.callscreen.calltheme.fragments.HistoryFragment;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.BoloCallHandler;

import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {
    public static boolean adTimer = false;
    public static FavoriteFragment favoriteFragment;
    public static CountDownTimer timer;
    ContactFragment contactFragment;
    MainFragment mainFragment;
    DialerFragment dialerFragment;
    public HistoryFragment historyFragment;
    AppCompatImageView ivBack;
    AppCompatImageView ivDelete;
    AppCompatImageView ivMore;
    AppCompatImageView ivSettings;
    public RelativeLayout mainTool;
    PopupWindow mypopupWindow;
    public RelativeLayout selectTool;
    public AppCompatTextView tv_title_lag;
    public AppCompatTextView txtSelect;
    public AppCompatTextView txtTitle;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    public int selectedCount = 0;
    public boolean isSelected = false;
    ImageView iV_contact,iV_history,iV_home,iV_dialer,iV_favourite;
    private String idInter = "";
    private ApInterstitialAd mInterstitialAd;
    private ArrayList<ContactModel> selectedContact = new ArrayList<>();
    AperoBannerAdView androBannerAdView;

    public interface OnClearclickListener {
        void onClear();
    }

    @Override 
    public void onBackPressed() {
        if (this.selectTool.getVisibility() == 0) {
            refreshAllContactList();
        } else {
            startActivity(new Intent(MainActivity.this, StartActivity.class));
        }
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        androBannerAdView = findViewById(R.id.bannerView);
        androBannerAdView.loadBanner(this, getString(R.string.admob_banner_id));
        configMediationProvider();
        loadAdInterstitial();
        BoloApplication.getApplication().initSettings(false);
        try {
            if (!ThemeDownloadService.isServiceRunning) {
                startService(new Intent(this, ThemeDownloadService.class));
            }
        } catch (Exception unused) {
        }
        PreferenceUtils.getInstance().putPreference(Constants.VOCIE_RECOGITION, false);
        ContactLoaderBuilder.getInstance(this, null).clearContacts();
        if (Build.VERSION.SDK_INT < 28) {
            if (ContextCompat.checkSelfPermission(this, BoloPermission.READ_CALL_LOG) != 0) {
                ActivityCompat.requestPermissions(this, new String[]{BoloPermission.READ_CALL_LOG, BoloPermission.PHONE_CALLS}, 102);
            }
        } else if (ContextCompat.checkSelfPermission(this, BoloPermission.PHONE_CALLS) != 0) {
            ActivityCompat.requestPermissions(this, new String[]{BoloPermission.PHONE_CALLS}, 102);
        }
        setBoloToDefaultApp();
        batteryOptimiseDialog();
        BoloCallHandler.getInstance().startPhoneStateService(this);
        this.txtTitle = (AppCompatTextView) findViewById(R.id.txtTitle);
        this.tv_title_lag = (AppCompatTextView) findViewById(R.id.tv_title_lag);
        this.txtSelect = (AppCompatTextView) findViewById(R.id.txtSelect);
        this.mainTool = (RelativeLayout) findViewById(R.id.mainTool);
        AppCompatImageView appCompatImageView2 = (AppCompatImageView) findViewById(R.id.ivSettings);
        this.ivSettings = appCompatImageView2;
        appCompatImageView2.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
        this.selectTool = (RelativeLayout) findViewById(R.id.selectTool);
        AppCompatImageView appCompatImageView3 = (AppCompatImageView) findViewById(R.id.ivBack);
        this.ivBack = appCompatImageView3;
        appCompatImageView3.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                MainActivity.this.onBackPressed();
            }
        });
        AppCompatImageView appCompatImageView4 = (AppCompatImageView) findViewById(R.id.ivDelete);
        this.ivDelete = appCompatImageView4;
        appCompatImageView4.setOnClickListener(new AnonymousClass4());
        AppCompatImageView appCompatImageView5 = (AppCompatImageView) findViewById(R.id.ivMore);
        this.ivMore = appCompatImageView5;
        appCompatImageView5.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                View inflate = ((LayoutInflater) MainActivity.this.getSystemService("layout_inflater")).inflate(R.layout.menu_clear, (ViewGroup) null);
                ((AppCompatTextView) inflate.findViewById(R.id.txtClearHistory)).setOnClickListener(new View.OnClickListener() { 
                    @Override 
                    public void onClick(View view2) {
                        CallLogComponent.removeAllRecents(MainActivity.this);
                        MainActivity.this.mypopupWindow.dismiss();
                    }
                });
                MainActivity.this.mypopupWindow = new PopupWindow(inflate, -2, -2, true);
                MainActivity.this.mypopupWindow.showAsDropDown(view, -20, -30);
            }
        });
        this.viewPager = (ViewPager2) findViewById(R.id.viewpager);
        iV_contact = findViewById(R.id.iV_contact);
        iV_history = findViewById(R.id.iV_history);
        iV_home = findViewById(R.id.iV_home);
        iV_dialer = findViewById(R.id.iV_dialer);
        iV_favourite = findViewById(R.id.iV_favourite);

        this.viewPagerAdapter = new ViewPagerAdapter(this);
        this.contactFragment = new ContactFragment();
        this.mainFragment = new MainFragment();
        this.historyFragment = new HistoryFragment();
        this.dialerFragment = new DialerFragment();
        favoriteFragment = new FavoriteFragment();
        this.viewPagerAdapter.add(this.contactFragment, "Contacts");
        this.viewPagerAdapter.add(this.historyFragment, "History");
        this.viewPagerAdapter.add(this.mainFragment, "Home");
        this.viewPagerAdapter.add(this.dialerFragment, "Dialpad");
        this.viewPagerAdapter.add(favoriteFragment, "Favorites");
        this.viewPager.setAdapter(this.viewPagerAdapter);

        unSlectAll();
        iV_home.setImageResource(R.drawable.sel_home);
        tv_title_lag.setText("Home");
        this.viewPager.setCurrentItem(2);
        this.viewPager.setUserInputEnabled(false);
        this.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

        iV_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isReady()) {

                    AperoAd.getInstance().showInterstitialAdByTimes(MainActivity.this, mInterstitialAd, new AperoAdCallback() {
                        @Override
                        public void onNextAction() {
                            Log.i("TAG", "onNextAction: start content and finish main");

                            tv_title_lag.setText("Contacts");
                            unSlectAll();
                            iV_contact.setImageResource(R.drawable.sel_contatct);
                            viewPager.setCurrentItem(0);
                        }

                        @Override
                        public void onAdFailedToShow(@Nullable ApAdError adError) {
                            super.onAdFailedToShow(adError);
                            Log.i("TAG", "onAdFailedToShow:" + adError.getMessage());
                        }

                        @Override
                        public void onInterstitialShow() {
                            super.onInterstitialShow();
                            Log.d("TAG", "onInterstitialShow");
                        }
                    }, true);
                } else {
                    tv_title_lag.setText("Contacts");
                    unSlectAll();
                    iV_contact.setImageResource(R.drawable.sel_contatct);
                    viewPager.setCurrentItem(0);
                }

            }
        });
        iV_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_title_lag.setText("History");
                unSlectAll();
                iV_history.setImageResource(R.drawable.sel_history);
                viewPager.setCurrentItem(1);
            }
        });
        iV_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isReady()) {

                    AperoAd.getInstance().showInterstitialAdByTimes(MainActivity.this, mInterstitialAd, new AperoAdCallback() {
                        @Override
                        public void onNextAction() {
                            Log.i("TAG", "onNextAction: start content and finish main");
                            tv_title_lag.setText("Home");
                            unSlectAll();
                            iV_home.setImageResource(R.drawable.sel_home);
                            viewPager.setCurrentItem(2);
                        }

                        @Override
                        public void onAdFailedToShow(@Nullable ApAdError adError) {
                            super.onAdFailedToShow(adError);
                            Log.i("TAG", "onAdFailedToShow:" + adError.getMessage());
                        }

                        @Override
                        public void onInterstitialShow() {
                            super.onInterstitialShow();
                            Log.d("TAG", "onInterstitialShow");
                        }
                    }, true);
                } else {
                    tv_title_lag.setText("Home");
                    unSlectAll();
                    iV_home.setImageResource(R.drawable.sel_home);
                    viewPager.setCurrentItem(2);
                }

            }
        });
        iV_dialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_title_lag.setText("Dialpad");
                unSlectAll();
                iV_dialer.setImageResource(R.drawable.sel_dialer);
                viewPager.setCurrentItem(3);
            }
        });
        iV_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_title_lag.setText("Home");
                unSlectAll();
                iV_home.setImageResource(R.drawable.sel_home);
                viewPager.setCurrentItem(2);
                if (mInterstitialAd.isReady()) {

                    AperoAd.getInstance().showInterstitialAdByTimes(MainActivity.this, mInterstitialAd, new AperoAdCallback() {
                        @Override
                        public void onNextAction() {
                            Log.i("TAG", "onNextAction: start content and finish main");
                            tv_title_lag.setText("Favorites");
                            unSlectAll();
                            iV_favourite.setImageResource(R.drawable.sel_favourite);
                            viewPager.setCurrentItem(4);
                        }

                        @Override
                        public void onAdFailedToShow(@Nullable ApAdError adError) {
                            super.onAdFailedToShow(adError);
                            Log.i("TAG", "onAdFailedToShow:" + adError.getMessage());
                        }

                        @Override
                        public void onInterstitialShow() {
                            super.onInterstitialShow();
                            Log.d("TAG", "onInterstitialShow");
                        }
                    }, true);
                } else {
                    tv_title_lag.setText("Favorites");
                    unSlectAll();
                    iV_favourite.setImageResource(R.drawable.sel_favourite);
                    viewPager.setCurrentItem(4);
                }

            }
        });
    }
    private void configMediationProvider() {
        if (AperoAd.getInstance().getMediationProvider() == AperoAdConfig.PROVIDER_ADMOB) {
            idInter = getResources().getString(R.string.admob_inter_id);
        } else {
            idInter = "c630fe3686063741";
        }
    }

    private void loadAdInterstitial() {

        mInterstitialAd = AperoAd.getInstance().getInterstitialAds(this, idInter);
    }
    private void unSlectAll() {
        iV_contact.setImageResource(R.drawable.unsel_contatc);
        iV_history.setImageResource(R.drawable.unsel_history);
        iV_home.setImageResource(R.drawable.unsel_home);
        iV_dialer.setImageResource(R.drawable.unsel_dialer);
        iV_favourite.setImageResource(R.drawable.unsel_favourite);
    }

    class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override 
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.contact_deleted);
            builder.setPositiveButton(R.string.delete, new AnonymousClass1());
            builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
            builder.show();
        }

        /* renamed from: com.colorcallscreen.colorphone.callscreen.calltheme.activity.MainActivity$4$1  reason: invalid class name */

        class AnonymousClass1 implements DialogInterface.OnClickListener {
            AnonymousClass1() {
            }

            @Override 
            public void onClick(DialogInterface dialogInterface, int i) {
                Iterator it = MainActivity.this.selectedContact.iterator();
                while (it.hasNext()) {
                    final ContactModel contactModel = (ContactModel) it.next();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override 
                                public void run() {
                                    ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
                                    arrayList.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection("contact_id=?", new String[]{String.valueOf(CallLogUtils.getContactID(MainActivity.this.getContentResolver(), contactModel.getDisplayNumber()))}).build());
                                    try {
                                        MainActivity.this.getContentResolver().applyBatch("com.android.contacts", arrayList);
                                    } catch (OperationApplicationException e) {
                                        e.printStackTrace();
                                    } catch (RemoteException e2) {
                                        e2.printStackTrace();
                                    }
                                    MainActivity.this.selectedContact.remove(contactModel);
                                    if (MainActivity.this.selectedContact.size() == 0) {
                                        MainActivity.this.refreshAllContactList();
                                    }
                                }
                            });
                        }
                    }).start();
                }
                Toast.makeText(MainActivity.this, "Contact deleted.", 0).show();
            }
        }
    }

    private void batteryOptimiseDialog() {
        int i = PreferenceUtils.getInstance().getInt("numberOfLaunch");
        if (i == 1) {
            new Handler().postDelayed(new Runnable() {
                @Override 
                public void run() {
                    MainActivity.this.optimizeBattery();
                }
            }, 500L);
        }
        PreferenceUtils.getInstance().putPreference("numberOfLaunch", i + 1);
    }

    public boolean optimizeBattery() {
        try {
            if (!PreferenceUtils.getInstance().getBoolean("OptimizeBatteryAsked", false) && Build.VERSION.SDK_INT >= 23) {
                Intent intent = new Intent();
                String packageName = getPackageName();
                if (!((PowerManager) getSystemService("power")).isIgnoringBatteryOptimizations(packageName)) {
                    intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivityForResult(intent, 1110);
                    PreferenceUtils.getInstance().putPreference("OptimizeBatteryAsked", true);
                    return false;
                }
            }
        } catch (Exception unused) {
        }
        return true;
    }

    public void setBoloToDefaultApp() {
        if (Utility.isAppDefaultSet(this)) {
            return;
        }
        Utility.openDefaultAppDialog(this);
    }

    public void onContactSelected(ArrayList<ContactModel> arrayList) {
        this.selectedContact = arrayList;
        if (arrayList.size() > 0) {
            this.mainTool.setVisibility(8);
            this.selectTool.setVisibility(0);
            return;
        }
        this.mainTool.setVisibility(0);
        this.selectTool.setVisibility(8);
    }


    public static void startTimer() {
        adTimer = true;
        CountDownTimer countDownTimer = new CountDownTimer(10000, 1000L) {
            @Override 
            public void onTick(long j) {
            }

            @Override
            public void onFinish() {
                MainActivity.adTimer = false;
                MainActivity.timer.cancel();
            }
        };
        timer = countDownTimer;
        countDownTimer.start();
    }

    public void refreshAllContactList() {
        this.selectedCount = 0;
        this.txtSelect.setText(this.selectedCount + " selected");
        this.isSelected = false;
        ArrayList<ContactModel> arrayList = new ArrayList<>();
        this.selectedContact = arrayList;
        onContactSelected(arrayList);
        if (this.contactFragment != null) {
            ContactLoaderBuilder.getInstance(this, null).clearContacts();
            this.contactFragment.loadContactFresh();
        }
    }
}
