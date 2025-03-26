package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.liulishuo.filedownloader.FileDownloadMonitor;
import com.liulishuo.filedownloader.FileDownloader;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.WebApplication;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.databases.DBDownloadManager;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.listener.CustomListener;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.services.DownloadService;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.fragments.FragmentActive;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.fragments.FragmentHome;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.fragments.FragmentMenu;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.fragments.FragmentOffline;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.fragments.FragmentWeb;


public class MainActivity extends AppCompatActivity implements CustomListener {
    static final boolean a = (!MainActivity.class.desiredAssertionStatus());
    private static MainActivity activity;
    private View badge;
    public BottomNavigationView bottomNavigationView;
    private final Fragment browser = new FragmentWeb();
    private Fragment active = this.browser;
    private final Fragment completed = new FragmentOffline();
    private DBDownloadManager dbDownloadManager;
    private final FragmentManager fm = getSupportFragmentManager();
    private FrameLayout fm1;
    private FrameLayout fm2;
    private boolean isBound;
    private BottomNavigationItemView itemView;


    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            if (FragmentOffline.actionMode != null) {
                FragmentOffline.actionMode.finish();
            }
            if (FragmentActive.actionMode != null) {
                FragmentActive.actionMode.finish();
            }
            if (FragmentHome.actionMode != null) {
                FragmentHome.actionMode.finish();
            }
            FragmentWeb fragmentWeb = new FragmentWeb();
            switch (menuItem.getItemId()) {
                case R.id.navigation_completed:
                    MainActivity.this.position = 1;
                    MainActivity.this.ShowCompleted();
                    return true;
                case R.id.navigation_home:
                    if (fragmentWeb.mWebView != null) {
                        fragmentWeb.mWebView.onResume();
                        fragmentWeb.mWebView.resumeTimers();
                    }
                    MainActivity.this.ShowBrowser_Nav();
                    return true;
                case R.id.navigation_progress:
                    MainActivity.this.position = 2;
                    MainActivity.this.ShowProgress();
                    return true;
                default:
                    return false;
            }
        }
    };
    private BottomNavigationMenuView menuView;
    int position;
    private final Fragment queue = new FragmentActive();
    private final Fragment settings = new FragmentMenu();

    private void BackHandler() {
        ShowBrowser_Nav();
        FragmentWeb fragmentWeb = (FragmentWeb) getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (!a && fragmentWeb == null) {
            throw new AssertionError();
        } else if (fragmentWeb.CheckBack()) {
            fragmentWeb.ControlBack();
        }
    }

    public void ShowBrowser_Nav() {
        this.fm.beginTransaction().hide(this.active).show(this.browser).attach(this.browser).commit();
        this.active = this.browser;
        this.fm1.setVisibility(View.VISIBLE);
        this.fm2.setVisibility(View.GONE);
    }

    public void ShowCompleted() {
        this.fm.beginTransaction().hide(this.active).attach(this.completed).show(this.completed).commit();
        this.active = this.completed;
        this.fm1.setVisibility(View.GONE);
        this.fm2.setVisibility(View.VISIBLE);
    }

    public void ShowProgress() {
        this.fm.beginTransaction().hide(this.active).attach(this.queue).show(this.queue).commit();
        this.active = this.queue;
        this.fm1.setVisibility(View.GONE);
        this.fm2.setVisibility(View.VISIBLE);
    }

    private void ShowSettings() {
        this.fm.beginTransaction().hide(this.active).attach(this.settings).show(this.settings).commit();
        this.active = this.settings;
        this.fm1.setVisibility(View.GONE);
        this.fm2.setVisibility(View.VISIBLE);
    }

    public static MainActivity getInstance() {
        return activity;
    }

    private void processExtraData() {
        Uri data = getIntent().getData();
        if (data != null) {
            FragmentWeb fragmentWeb = new FragmentWeb();
            Bundle bundle = new Bundle();
            bundle.putString("url", data.toString());
            fragmentWeb.setArguments(bundle);
            this.fm.beginTransaction().replace(R.id.frame_container, fragmentWeb).commit();
            ShowBrowser_Nav();
        }
    }

    public void BrToHome() {
        this.bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        ShowBrowser_Nav();
    }

    public String ErrorHandler(int i) {
        int i2;
        if (i == 0) {
            i2 = R.string.br_fileexit;
        } else if (i == 1) {
            i2 = R.string.br_validname;
        } else if (i != 2) {
            return null;
        } else {
            i2 = R.string.queuelimit;
        }
        return getString(i2);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_main1);
        activity = this;
        this.dbDownloadManager = new DBDownloadManager(this);
        BottomNavigationView bottomNavigationView2 = (BottomNavigationView) findViewById(R.id.navigation);
        this.bottomNavigationView = bottomNavigationView2;
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView2.getChildAt(0);
        this.menuView = bottomNavigationMenuView;
        this.itemView = (BottomNavigationItemView) bottomNavigationMenuView.getChildAt(1);
        this.badge = LayoutInflater.from(this).inflate(R.layout.layout_counter, this.bottomNavigationView, false);
        this.fm1 = (FrameLayout) findViewById(R.id.frame_container);
        this.fm2 = (FrameLayout) findViewById(R.id.frame_container1);
        this.fm1.setVisibility(View.VISIBLE);
        this.fm2.setVisibility(View.GONE);

        this.fm.beginTransaction().add(R.id.frame_container1, this.settings, "4").hide(this.settings).addToBackStack("settings").commit();
        this.fm.beginTransaction().add(R.id.frame_container1, this.completed, "3").hide(this.completed).addToBackStack("completed").commit();
        this.fm.beginTransaction().add(R.id.frame_container1, this.queue, "2").hide(this.queue).addToBackStack("queue").commit();
        this.fm.beginTransaction().add(R.id.frame_container, this.browser, "1").show(this.browser).addToBackStack("browser").commit();
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this.mOnNavigationItemSelectedListener);
        show_badge();
        processExtraData();
    }

    public void onDestroy() {
        if (this.isBound) {
            unbindService(WebApplication.getInstance().downloadConnection);
        }
        FileDownloader.getImpl().unBindServiceIfIdle();
        FileDownloadMonitor.releaseGlobalMonitor();
        super.onDestroy();
    }

    public void onFragmentInteraction(String str) {
        ShowBrowser_Nav();
        FragmentWeb fragmentWeb = (FragmentWeb) getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (a || fragmentWeb != null) {
            fragmentWeb.updateUrl(str);
            return;
        }
        throw new AssertionError();
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processExtraData();
    }

    public void onStart() {
        super.onStart();
        if (WebApplication.getInstance().downloadIntent == null) {
            WebApplication.getInstance().downloadIntent = new Intent(this, DownloadService.class);
            this.isBound = bindService(WebApplication.getInstance().downloadIntent, WebApplication.getInstance().downloadConnection, 1);
            startService(WebApplication.getInstance().downloadIntent);
        }
    }

    public void show_badge() {
        int currentDownloadingCount = this.dbDownloadManager.getCurrentDownloadingCount();
        BottomNavigationItemView bottomNavigationItemView = this.itemView;
        if (!(bottomNavigationItemView == null || this.menuView == null)) {
            bottomNavigationItemView.removeView(this.badge);
            if (currentDownloadingCount > 0) {
                ((TextView) this.badge.findViewById(R.id.txt_badge)).setText(getString(R.string.blank, new Object[]{Integer.valueOf(currentDownloadingCount)}));
                this.itemView.addView(this.badge);
            } else {
                this.itemView.removeView(this.badge);
            }
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                MainActivity.this.show_badge();
            }
        }, 500);
    }

    public void show_blocked_url() {
        @SuppressLint("ResourceType") final Dialog dialog = new Dialog(activity, 16973839);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.color.black_trans);
        dialog.setContentView(R.layout.dialog_not_access);
        dialog.setCancelable(false);
        ((Button) dialog.findViewById(R.id.ivUnderstand)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void show_snack(String str) {
        Snackbar.make(findViewById(R.id.main_container), (CharSequence) str, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        FragmentWeb fragmentWeb = (FragmentWeb) getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (!a && fragmentWeb == null) {
            throw new AssertionError();
        } else if (this.bottomNavigationView.getSelectedItemId() == R.id.navigation_progress || this.bottomNavigationView.getSelectedItemId() == R.id.navigation_completed) {
            finish();
//            Glob.isRateUSClicked = true;
        } else if (fragmentWeb.CheckBack()) {
            BackHandler();
        } else if (fragmentWeb.main_search_lin.isShown()) {
            this.fm.beginTransaction().detach(this.browser).show(this.browser).addToBackStack("browser").commit();
            this.fm.beginTransaction().attach(this.browser).show(this.browser).addToBackStack("browser").commit();
        } else {
            finish();
//            Glob.isRateUSClicked = true;
        }
    }

}
