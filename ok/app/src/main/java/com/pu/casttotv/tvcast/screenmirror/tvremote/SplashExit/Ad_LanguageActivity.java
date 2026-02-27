package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.adsdemo.vdapps.adsload.interfaces.MyCallback;
import com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit.adapter.Ad_LanguagesAdapter;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.ObjectLanguage;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.MainActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.splash.CastSplashActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.SharedPrefsUtil;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;

import java.util.ArrayList;


public class Ad_LanguageActivity extends AppCompatActivity {

    RecyclerView rvLanguages;
    String strLanguage = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_language);

        AdsManager.CallNativeAdLoad(this, findViewById(R.id.native_container), AdsManager.NATIVE_SMALL);

        rvLanguages = findViewById(R.id.rvLanguages);
        rvLanguages.setLayoutManager(new LinearLayoutManager(Ad_LanguageActivity.this, RecyclerView.VERTICAL, false));
        rvLanguages.setHasFixedSize(false);
        rvLanguages.setAdapter(new Ad_LanguagesAdapter(Ad_LanguageActivity.this, getData(), ((position, objectLanguage) -> {
            strLanguage = objectLanguage.getKey();

        })));

        findViewById(R.id.tv_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = (String) SharedPrefsUtil.getInstance().get("KEY_LANGUAGE_SAVE", String.class);
                if (strLanguage.equals(str)) {
                    AdsManager.CallInterstitialAdLoad(Ad_LanguageActivity.this, 0, new MyCallback() {
                        @Override
                        public void callbackCall() {
                            if (AdsManager.SwipeScreen == 1) {
                                Intent intent = new Intent(Ad_LanguageActivity.this, Ad_SwipeScreenActivity.class);
                                startActivity(intent);
                            } else if (AdsManager.AppPurchaseScreen == 1) {
                                Intent intent = new Intent(Ad_LanguageActivity.this, Ad_AppPurchaseActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(Ad_LanguageActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    SharedPrefsUtil.getInstance().put("KEY_LANGUAGE_SAVE", strLanguage);
                    startActivity(new Intent(Ad_LanguageActivity.this, CastSplashActivity.class));
                    Utils.nextScreen(Ad_LanguageActivity.this);
                    finish();
                }
            }
        });


    }

    private ArrayList<ObjectLanguage> getData() {
        ArrayList<ObjectLanguage> languageArrayList = new ArrayList<>();
        languageArrayList.add(new ObjectLanguage(getString(R.string.lag_english), ""));
        languageArrayList.add(new ObjectLanguage(getString(R.string.lag_russian), "ru"));
        languageArrayList.add(new ObjectLanguage(getString(R.string.lag_french), "fr"));
        languageArrayList.add(new ObjectLanguage(getString(R.string.lag_spanish), "es"));
        languageArrayList.add(new ObjectLanguage(getString(R.string.lag_turkish), "tr"));
        languageArrayList.add(new ObjectLanguage(getString(R.string.lag_japan), "ja"));
        languageArrayList.add(new ObjectLanguage(getString(R.string.lag_kr), "kr"));
        languageArrayList.add(new ObjectLanguage(getString(R.string.lag_indonesian), "in"));
        languageArrayList.add(new ObjectLanguage(getString(R.string.lag_hindi), "hi"));
        languageArrayList.add(new ObjectLanguage(getString(R.string.lag_norwegian), "no"));
        languageArrayList.add(new ObjectLanguage(getString(R.string.lag_finnish), "fi"));
        languageArrayList.add(new ObjectLanguage(getString(R.string.lag_vn), "vi"));
        return languageArrayList;
    }

    @Override
    public void onBackPressed() {
        AdsManager.CallInterstitialAdLoad(this, 1, new MyCallback() {
            @Override
            public void callbackCall() {
                finish();
            }
        });
    }
}