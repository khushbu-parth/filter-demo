package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.howto;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

/* loaded from: classes4.dex */
public class HowToYouActivity extends BaseActivity {
    private HtyAdapter adapter;
    private Button btn_got_it;
    private List<HtyModel> list;
    private LinearLayout llBack;
    private LinearLayout llConnect;
    private RecyclerView rcv_list;
    private TextView tvTitleTab;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_how_to_you);


        initView();
    }

    public Unit callbackFail() {
        return Unit.INSTANCE;
    }

    public Unit callbackDone() {
        return Unit.INSTANCE;
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        ImageView imageView = (ImageView) findViewById(R.id.imvConnect);
        this.llConnect = (LinearLayout) findViewById(R.id.llConnect);
        this.llBack = (LinearLayout) findViewById(R.id.llBack);
        this.llConnect.setVisibility(8);
        this.llBack.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                HowToYouActivity.this.onBackPressed();
            }
        });
        this.tvTitleTab = (TextView) findViewById(R.id.tvTitleTab);
        this.btn_got_it = (Button) findViewById(R.id.btn_got_it);
        this.rcv_list = (RecyclerView) findViewById(R.id.rcv_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(1);
        this.rcv_list.setLayoutManager(linearLayoutManager);
        HtyAdapter htyAdapter = new HtyAdapter(new ArrayList(), this);
        this.adapter = htyAdapter;
        this.rcv_list.setAdapter(htyAdapter);
        this.btn_got_it.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                HowToYouActivity.this.onBackPressed();
            }
        });
        getData(getIntent().getIntExtra("TYPE_HTY", 1));
        HowToYouActivity.this.callbackDone();
    }

    private void getData(int i) {
        this.list = new ArrayList();
        if (i == 1) {
            this.tvTitleTab.setText(getString(R.string.how_to_cast));
            this.list.add(null);
            this.list.add(new HtyModel(getString(R.string.guide_connect_same_wifi), R.drawable.how_to_use_1));
            this.list.add(new HtyModel(getString(R.string.hty_connect2), R.drawable.how_to_cast_6));
            this.list.add(new HtyModel(getString(R.string.hty_connect3), R.drawable.how_to_cast_7));
            this.list.add(null);
            this.list.add(new HtyModel(getString(R.string.hty_connect4), R.drawable.how_to_cast_8));
            this.list.add(new HtyModel(getString(R.string.hty_connect5), R.drawable.how_to_cast_9));
        } else if (i == 2) {
            this.tvTitleTab.setText(getString(R.string.how_to_remote));
            this.list.add(null);
            this.list.add(new HtyModel(getString(R.string.guide_connect_same_wifi), R.drawable.imv_hty1));
            this.list.add(new HtyModel(getString(R.string.hty_remote2), R.drawable.imv_hty_remote2));
            this.list.add(new HtyModel(getString(R.string.hty_remote3), R.drawable.imv_hty_remote3));
            this.list.add(null);
            this.list.add(new HtyModel(getString(R.string.hty_remote4), R.drawable.imv_hty_remote4));
            this.list.add(new HtyModel(getString(R.string.hty_remote5), R.drawable.imv_hty_remote5));
        } else {
            this.tvTitleTab.setText(getString(R.string.how_to_mirror));
            this.list.add(null);
            this.list.add(new HtyModel(getString(R.string.guide_connect_same_wifi), R.drawable.how_to_use_1));
            this.list.add(new HtyModel(getString(R.string.hty_mirror2), R.drawable.how_to_use_2));
            this.list.add(new HtyModel(getString(R.string.hty_mirror3), R.drawable.how_to_use_3));
            this.list.add(null);
            this.list.add(new HtyModel(getString(R.string.hty_mirror4), R.drawable.how_to_use_4));
            this.list.add(new HtyModel(getString(R.string.hty_mirror5), R.drawable.how_to_use_5));
        }
        this.adapter.setData(this.list);
    }

    @Override
    public void onBackPressed() {
        AdsManager.CallInterstitialAdLoad(this, 1, () -> {
            super.onBackPressed();
        });

    }
}
