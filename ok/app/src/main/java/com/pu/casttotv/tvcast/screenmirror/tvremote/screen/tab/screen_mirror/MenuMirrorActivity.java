package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.screen_mirror;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.howto.HowToYouActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;

import kotlin.Unit;

@SuppressLint("WrongConstant")
public class MenuMirrorActivity extends BaseActivity {

    private Button btn_smart_tv;
    private Button btn_web_browser;
    private LinearLayout llBack;
    private LinearLayout llHelp;
    private TextView tvTitleTab;
    private int type = 0;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_menu_mirror);


        this.tvTitleTab = (TextView) findViewById(R.id.tvTitleTab);
        this.btn_smart_tv = (Button) findViewById(R.id.btn_smart_tv);
        this.btn_web_browser = (Button) findViewById(R.id.btn_web_browser);
        this.llBack = (LinearLayout) findViewById(R.id.llBack);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llConnect);
        linearLayout.setVisibility(8);
        this.tvTitleTab.setText(getString(R.string.screen_mirror));
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.llHelp);
        this.llHelp = linearLayout2;
        linearLayout2.setVisibility(0);
        this.llHelp.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent(MenuMirrorActivity.this, HowToYouActivity.class);
                intent.putExtra("TYPE_HTY", 3);
                MenuMirrorActivity.this.startActivity(intent);
                Utils.nextScreen(MenuMirrorActivity.this);
            }
        });
        this.llBack.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MenuMirrorActivity.this.onBackPressed();
            }
        });
        this.btn_smart_tv.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MenuMirrorActivity.this.type = 0;
                MenuMirrorActivity.this.checkShowAds();
            }
        });
        this.btn_web_browser.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MenuMirrorActivity.this.type = 1;
                MenuMirrorActivity.this.checkShowAds();
            }
        });
        MenuMirrorActivity.this.callbackDone();
    }

    public void checkShowAds() {
        gotoMirror();
    }

    public Unit callbackFail() {
        return Unit.INSTANCE;
    }

    public Unit callbackDone() {
        return Unit.INSTANCE;
    }

    public Unit actionCommon() {
        gotoMirror();
        return Unit.INSTANCE;
    }

    private void gotoMirror() {
        if (this.type == 0) {
            gotoActivity(MirrorTVActivity.class);
            return;
        }
        gotoActivity(SMWebActivity.class);
    }

    @Override
    public void onBackPressed() {
super.onBackPressed();    }
}
