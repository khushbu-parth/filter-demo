package com.pu.casttotv.tvcast.screenmirror.tvremote.SplashExit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.adsdemo.vdapps.adsload.interfaces.MyCallback;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.MainActivity;


public class Ad_CountryActivity extends AppCompatActivity {
    LinearLayout rl1, rl2, rl3, rl4, rl5, rl6, rl7, rl8, rl9, rl10, rl11, rl12;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7,tv8, tv9, tv10, tv11, tv12;
    ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9, iv10, iv11, iv12;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_country);
        AdsManager.CallNativeAdLoad(this, findViewById(R.id.native_container), AdsManager.NATIVE_MIDEUM);

        findViewById(R.id.tv_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsManager.CallInterstitialAdLoad(Ad_CountryActivity.this, 0, new MyCallback() {
                    @Override
                    public void callbackCall() {
                        if (AdsManager.LanguageScreen == 1) {
                            Intent intent = new Intent(Ad_CountryActivity.this, Ad_LanguageActivity.class);
                            startActivity(intent);
                        } else if (AdsManager.SwipeScreen == 1) {
                            Intent intent = new Intent(Ad_CountryActivity.this, Ad_SwipeScreenActivity.class);
                            startActivity(intent);
                        } else if (AdsManager.AppPurchaseScreen == 1) {
                            Intent intent = new Intent(Ad_CountryActivity.this, Ad_AppPurchaseActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Ad_CountryActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });

            }
        });

        rl1 = findViewById(R.id.rl1);
        rl2 = findViewById(R.id.rl2);
        rl3 = findViewById(R.id.rl3);
        rl4 = findViewById(R.id.rl4);
        rl5 = findViewById(R.id.rl5);
        rl6 = findViewById(R.id.rl6);
        rl7 = findViewById(R.id.rl7);
        rl8 = findViewById(R.id.rl8);
        rl9 = findViewById(R.id.rl9);
        rl10 = findViewById(R.id.rl10);
        rl11 = findViewById(R.id.rl11);
        rl12 = findViewById(R.id.rl12);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv7 = findViewById(R.id.tv7);
        tv8 = findViewById(R.id.tv8);
        tv9 = findViewById(R.id.tv9);
        tv10 = findViewById(R.id.tv10);
        tv11 = findViewById(R.id.tv11);
        tv12 = findViewById(R.id.tv12);
        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        iv4 = findViewById(R.id.iv4);
        iv5 = findViewById(R.id.iv5);
        iv6 = findViewById(R.id.iv6);
        iv7 = findViewById(R.id.iv7);
        iv8 = findViewById(R.id.iv8);
        iv9 = findViewById(R.id.iv9);
        iv10 = findViewById(R.id.iv10);
        iv11 = findViewById(R.id.iv11);
        iv12 = findViewById(R.id.iv12);

        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnSelectedAll();
                iv1.setVisibility(View.VISIBLE);
                tv1.setTextColor(getResources().getColor(R.color.AdhowerColor));
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnSelectedAll();
                iv2.setVisibility(View.VISIBLE);
                tv2.setTextColor(getResources().getColor(R.color.AdhowerColor));
            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnSelectedAll();
                iv3.setVisibility(View.VISIBLE);
                tv3.setTextColor(getResources().getColor(R.color.AdhowerColor));
            }
        });
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnSelectedAll();
                iv4.setVisibility(View.VISIBLE);
                tv4.setTextColor(getResources().getColor(R.color.AdhowerColor));
            }
        });
        rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnSelectedAll();
                iv5.setVisibility(View.VISIBLE);
                tv5.setTextColor(getResources().getColor(R.color.AdhowerColor));
            }
        });
        rl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnSelectedAll();
                iv6.setVisibility(View.VISIBLE);
                tv6.setTextColor(getResources().getColor(R.color.AdhowerColor));
            }
        });
        rl7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnSelectedAll();
                iv7.setVisibility(View.VISIBLE);
                tv7.setTextColor(getResources().getColor(R.color.AdhowerColor));
            }
        });
         rl8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnSelectedAll();
                iv8.setVisibility(View.VISIBLE);
                tv8.setTextColor(getResources().getColor(R.color.AdhowerColor));
            }
        });
         rl9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnSelectedAll();
                iv9.setVisibility(View.VISIBLE);
                tv9.setTextColor(getResources().getColor(R.color.AdhowerColor));
            }
        });
         rl10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnSelectedAll();
                iv10.setVisibility(View.VISIBLE);
                tv10.setTextColor(getResources().getColor(R.color.AdhowerColor));
            }
        });
         rl11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnSelectedAll();
                iv11.setVisibility(View.VISIBLE);
                tv11.setTextColor(getResources().getColor(R.color.AdhowerColor));
            }
        });
         rl12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnSelectedAll();
                iv12.setVisibility(View.VISIBLE);
                tv12.setTextColor(getResources().getColor(R.color.AdhowerColor));
            }
        });

    }

    private void UnSelectedAll() {
        iv1.setVisibility(View.GONE);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.GONE);
        iv4.setVisibility(View.GONE);
        iv5.setVisibility(View.GONE);
        iv6.setVisibility(View.GONE);
        iv7.setVisibility(View.GONE);
        iv8.setVisibility(View.GONE);
        iv9.setVisibility(View.GONE);
        iv10.setVisibility(View.GONE);
        iv11.setVisibility(View.GONE);
        iv12.setVisibility(View.GONE);

        tv1.setTextColor(getResources().getColor(R.color.AdTextDark));
        tv2.setTextColor(getResources().getColor(R.color.AdTextDark));
        tv3.setTextColor(getResources().getColor(R.color.AdTextDark));
        tv4.setTextColor(getResources().getColor(R.color.AdTextDark));
        tv5.setTextColor(getResources().getColor(R.color.AdTextDark));
        tv6.setTextColor(getResources().getColor(R.color.AdTextDark));
        tv7.setTextColor(getResources().getColor(R.color.AdTextDark));
        tv8.setTextColor(getResources().getColor(R.color.AdTextDark));
        tv9.setTextColor(getResources().getColor(R.color.AdTextDark));
        tv10.setTextColor(getResources().getColor(R.color.AdTextDark));
        tv11.setTextColor(getResources().getColor(R.color.AdTextDark));
        tv12.setTextColor(getResources().getColor(R.color.AdTextDark));
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