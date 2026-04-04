package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.adapter.WallpaperAdapter;
import com.colorcallscreen.colorphone.callscreen.calltheme.base.BaseModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.base.ResponseInterface;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.BoloThemes;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ThemeModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ThemeModelWrapper;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.ThemeWebService;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.PreferenceUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ActivityAllWallpapers extends AppCompatActivity {
    WallpaperAdapter adapter;
    private String request_url;
    RecyclerView rvWallpaper;
    AppCompatTextView txtTitle;
    private List<ThemeModel> themeList = new ArrayList();
    public String gaEventCategory = Constants.ThemeCategory;
    String WallpaperCategory = "Wallpaper_";


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_all_wallpapers);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ActivityAllWallpapers.this.onBackPressed();
            }
        });
        this.request_url = getIntent().getStringExtra("type");
        this.gaEventCategory = getIntent().getStringExtra("name");
        AppCompatTextView appCompatTextView = (AppCompatTextView) findViewById(R.id.txtTitle);
        this.txtTitle = appCompatTextView;
        appCompatTextView.setText(this.gaEventCategory);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvWallpaper);
        this.rvWallpaper = recyclerView;
        recyclerView.setHasFixedSize(true);
        if (this.request_url.contains("custom")) {
            loadOfflineThemes(this.request_url, false);
        } else {
            loadOfflineThemes(this.request_url, true);
        }
    }

    private void loadOfflineThemes(String str, final boolean z) {
        new BoloThemes().loadThemes(this, str, new BoloThemes.ThemeLoaded() {
            @Override
            public void onThemeLoaded(List<ThemeModel> list) {
                ActivityAllWallpapers.this.themeList.addAll(list);
                if (ActivityAllWallpapers.this.adapter != null) {
                    ActivityAllWallpapers.this.adapter.updateDataList(ActivityAllWallpapers.this.themeList);
                    ActivityAllWallpapers.this.adapter.notifyDataSetChanged();
                } else {
                    ActivityAllWallpapers activityAllWallpapers = ActivityAllWallpapers.this;
                    ActivityAllWallpapers activityAllWallpapers2 = ActivityAllWallpapers.this;
                    activityAllWallpapers.adapter = new WallpaperAdapter(activityAllWallpapers2, activityAllWallpapers2.themeList);
                    ActivityAllWallpapers.this.adapter.gaEventCategory = ActivityAllWallpapers.this.WallpaperCategory + ActivityAllWallpapers.this.gaEventCategory;
                    ActivityAllWallpapers.this.rvWallpaper.setAdapter(ActivityAllWallpapers.this.adapter);
                }
                if (z) {
                    ActivityAllWallpapers activityAllWallpapers3 = ActivityAllWallpapers.this;
                    activityAllWallpapers3.getThemeFromService(activityAllWallpapers3.request_url);
                }
            }
        });
    }

    public void getThemeFromService(final String str) {
        final boolean z;
        List<ThemeModel> themes = PreferenceUtils.getInstance().getThemes(str);
        if (themes != null) {
            Log.println(Log.ASSERT, "themes===", themes.size() + "");
            for (int i = 0; i < themes.size(); i++) {
                if (i != 0 && i % 6 == 0) {
                    this.themeList.add(null);
                }
                this.themeList.add(themes.get(i));
            }
            this.themeList.addAll(themes);
            this.adapter.updateDataList(this.themeList);
            this.adapter.notifyDataSetChanged();
            z = false;
        } else {
            z = true;
        }
        if (Utility.isConnected(true)) {
            new ThemeWebService().getThemes(this, str, new ResponseInterface() {
                @Override
                public void onResponse(BaseModel baseModel, String str2) {
                    if (baseModel != null) {
                        ThemeModelWrapper themeModelWrapper = (ThemeModelWrapper) baseModel;
                        if (z) {
                            Log.println(Log.ASSERT, "themessds===", themeModelWrapper.getData().getThemes().size() + "");
                            for (int i2 = 0; i2 < themeModelWrapper.getData().getThemes().size(); i2++) {
                                if (i2 != 0 && i2 % 6 == 0) {
                                    ActivityAllWallpapers.this.themeList.add(null);
                                }
                                ActivityAllWallpapers.this.themeList.add(themeModelWrapper.getData().getThemes().get(i2));
                            }
                            ActivityAllWallpapers.this.themeList.addAll(themeModelWrapper.getData().getThemes());
                            Log.println(Log.ASSERT, "themesws===", ActivityAllWallpapers.this.themeList.size() + "");
                            ActivityAllWallpapers.this.adapter.updateDataList(ActivityAllWallpapers.this.themeList);
                            ActivityAllWallpapers.this.adapter.notifyDataSetChanged();
                        }
                        PreferenceUtils.getInstance().setThemes(themeModelWrapper.getData().getThemes(), str);
                        return;
                    }
                    ActivityAllWallpapers activityAllWallpapers = ActivityAllWallpapers.this;
                    Toast.makeText(activityAllWallpapers, activityAllWallpapers.getString(R.string.no_internet_please), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.no_internet_please), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            if (i == 100) {
                if (intent != null) {
                    CropImage.activity(intent.getData()).setAspectRatio(9, 16).setGuidelines(CropImageView.Guidelines.ON).start(this);
                }
            } else if (i == 203) {
                CropImage.ActivityResult activityResult = CropImage.getActivityResult(intent);
                if (i2 != -1) {
                    if (i2 == 204) {
                        activityResult.getError();
                        return;
                    }
                    return;
                }
                Uri uri = activityResult.getUri();
                File file = new File(uri.getPath());
                File externalFilesDir = BoloApplication.getApplication().getExternalFilesDir(".bolo/" + file.getName());
                if (externalFilesDir.exists()) {
                    externalFilesDir.delete();
                }
                if (Build.VERSION.SDK_INT >= 26) {
                    try {
                        if (Files.copy(file.toPath(), externalFilesDir.toPath(), new CopyOption[0]) != null) {
                            if (!externalFilesDir.toString().startsWith("file://")) {
                                uri = Uri.parse("file://" + externalFilesDir.toString());
                            } else {
                                uri = Uri.parse(externalFilesDir.toString());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent2 = new Intent(ActivityAllWallpapers.this, ActivityCustomThemeView.class);
                intent2.putExtra(Constants.IMG_MAIN, uri.toString());
                intent2.putExtra(Constants.IMG_MAIN_URL, uri);
                intent2.putExtra("custom", true);
                ActivityAllWallpapers.this.startActivity(intent2);
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        WallpaperAdapter wallpaperAdapter = this.adapter;
        if (wallpaperAdapter != null) {
            wallpaperAdapter.updateDataList(this.themeList);
        }
    }
}
