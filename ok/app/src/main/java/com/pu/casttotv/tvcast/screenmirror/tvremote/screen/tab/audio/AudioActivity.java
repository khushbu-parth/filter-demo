package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.audio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.AudioModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.ManagerDataPlay;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.ConnectActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.DialogDisconnect;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.howto.HowToYouActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.playcast.PlayCastActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.premium.IapUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

public class AudioActivity extends BaseActivity {
    public ArrayList<AudioModel> audioModelArrayList;
    public int currentItem = 0;
    private ImageView imvConnect;
    private LinearLayout llBack;
    private LinearLayout llConnect;
    private LinearLayout llHelp;
    private ViewGroup main_ads_native;
    private TabLayout tabLayout;
    private String[] titleAudio;
    private TextView tvTitleTab;
    private ViewPager2 viewPager2;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.fragment_audio);

        EventBus.getDefault().register(this);
        initView();
        AudioActivity.this.callbackDone();
    }

    public Unit callbackFail() {
        return Unit.INSTANCE;
    }

    public Unit callbackDone() {
        return Unit.INSTANCE;
    }

    public Unit actionCommon() {
        gotoPlay(this.audioModelArrayList, this.currentItem);
        return Unit.INSTANCE;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @SuppressLint("WrongConstant")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (TVConnectUtils.getInstance().isConnected()) {
            imvConnect.setImageResource(R.drawable.hover_screen);
        } else {
            imvConnect.setImageResource(R.drawable.screen);
        }
        if (IapUtils.isIapAll() || IapUtils.isPaymentMirror()) {
            this.main_ads_native.setVisibility(8);
        }
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        this.titleAudio = new String[]{getResources().getString(R.string.txt_song), getResources().getString(R.string.txt_album), getResources().getString(R.string.txt_artist)};
        this.tvTitleTab = (TextView) findViewById(R.id.tvTitleTab);
        this.llHelp = (LinearLayout) findViewById(R.id.llHelp);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        this.main_ads_native = (ViewGroup) findViewById(R.id.main_ads_native);
        this.imvConnect = (ImageView) findViewById(R.id.imvConnect);
        this.llConnect = (LinearLayout) findViewById(R.id.llConnect);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llBack);
        this.llBack = linearLayout;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AudioActivity.this.onBackPressed();
            }
        });
        if (TVConnectUtils.getInstance().isConnected()) {
            imvConnect.setImageResource(R.drawable.hover_screen);
        } else {
            imvConnect.setImageResource(R.drawable.screen);
        }
        this.tvTitleTab.setText(getString(R.string.txt_audio));
        this.llHelp.setVisibility(0);
        this.llHelp.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent(AudioActivity.this, HowToYouActivity.class);
                intent.putExtra("TYPE_HTY", 1);
                AudioActivity.this.startActivity(intent);
                Utils.nextScreen(AudioActivity.this);
            }
        });
        this.llConnect.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (TVConnectUtils.getInstance().isConnected()) {
                    new DialogDisconnect(AudioActivity.this).show();
                    return;
                }
                AudioActivity.this.startActivity(new Intent(AudioActivity.this, ConnectActivity.class));
                Utils.nextScreen(AudioActivity.this);
            }
        });
        this.audioModelArrayList = new ArrayList<>();
        this.viewPager2 = (ViewPager2) findViewById(R.id.viewPager2);
        this.viewPager2.setAdapter(new AudioPagerAdapter(this));

        new TabLayoutMediator(tabLayout, this.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(TabLayout.Tab tab, int i) {
                tab.setText(AudioActivity.this.titleAudio[i]);
            }
        }).attach();

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(10, 0, 10, 0);
            tab.requestLayout();
        }

        tabLayout.requestLayout();
    }

    public void gotoPlay(List<AudioModel> list, int i) {
        ArrayList<AudioModel> arrayList = this.audioModelArrayList;
        if (arrayList == null || arrayList.size() <= i) {
            return;
        }
        Intent intent = new Intent(this, PlayCastActivity.class);
        ManagerDataPlay.getInstance().setTypePlay(2);
        ManagerDataPlay.getInstance().setListAudio((ArrayList) list);
        ManagerDataPlay.getInstance().setPosSelected(i);
        ManagerDataPlay.getInstance().duration = Long.valueOf(list.get(i).getDuration());
        startActivity(intent);
        Utils.nextScreen(this);
    }
}
