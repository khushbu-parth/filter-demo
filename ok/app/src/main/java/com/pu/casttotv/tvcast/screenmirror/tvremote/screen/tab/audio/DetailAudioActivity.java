package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.audio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.AudioModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.ManagerDataPlay;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.ConnectActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.DialogDisconnect;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.playcast.PlayCastActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.premium.IapUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

@SuppressLint("WrongConstant")
public class DetailAudioActivity extends BaseActivity {
    private AudioAdapter adapterSong;
    public List<AudioModel> audioModelArrayList;
    private ImageView imvConnect;
    private LinearLayout llBack;
    private LinearLayout llConnect;
    private ViewGroup main_ads_native;
    private RecyclerView rcvListSong;
    private TextView tvTitleTab;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_detail_audio);
        EventBus.getDefault().register(this);
        initView();
    }

    public Unit callbackFail() {
        return Unit.INSTANCE;
    }

    public Unit callbackDone() {
        return Unit.INSTANCE;
    }

    public Unit actionCommon() {
        return Unit.INSTANCE;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.backScreen(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

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

    private void initView() {
        this.tvTitleTab = (TextView) findViewById(R.id.tvTitleTab);
        this.main_ads_native = (ViewGroup) findViewById(R.id.main_ads_native);
        this.imvConnect = (ImageView) findViewById(R.id.imvConnect);
        this.llConnect = (LinearLayout) findViewById(R.id.llConnect);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llBack);
        this.llBack = linearLayout;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DetailAudioActivity.this.onBackPressed();
            }
        });
        if (TVConnectUtils.getInstance().isConnected()) {
            imvConnect.setImageResource(R.drawable.hover_screen);
        } else {
            imvConnect.setImageResource(R.drawable.screen);
        }
        this.llConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TVConnectUtils.getInstance().isConnected()) {
                    new DialogDisconnect(DetailAudioActivity.this).show();
                    return;
                }
                DetailAudioActivity.this.startActivity(new Intent(DetailAudioActivity.this, ConnectActivity.class));
                Utils.nextScreen(DetailAudioActivity.this);
            }
        });
        this.audioModelArrayList = new ArrayList();
        this.rcvListSong = (RecyclerView) findViewById(R.id.rcvListSong);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(1);
        this.rcvListSong.setLayoutManager(linearLayoutManager);
        AudioAdapter audioAdapter = new AudioAdapter(new ArrayList(), this);
        this.adapterSong = audioAdapter;
        this.rcvListSong.setAdapter(audioAdapter);
        this.adapterSong.setClickItem(new AudioAdapter.OnItemClickPhoto() { // from class: com.magicapps.casttotv.tv.screen.tab.audio.DetailAudioActivity.3
            @Override
            public void itemClick(List<AudioModel> list, int i) {
                if (TVConnectUtils.getInstance().isConnected()) {
                    DetailAudioActivity.this.gotoPlay(list, i);
                    return;
                }
                DetailAudioActivity.this.startActivity(new Intent(DetailAudioActivity.this, ConnectActivity.class));
                Utils.nextScreen(DetailAudioActivity.this);
            }
        });
        List<AudioModel> listAudio = ManagerDataPlay.getInstance().getListAudio();
        this.audioModelArrayList = listAudio;
        if (listAudio == null || listAudio.size() <= 0) {
            return;
        }
        this.tvTitleTab.setText(ManagerDataPlay.getInstance().titleAudio);
        this.adapterSong.setData(this.audioModelArrayList);
    }

    public void gotoPlay(List<AudioModel> list, int i) {
        Intent intent = new Intent(this, PlayCastActivity.class);
        ManagerDataPlay.getInstance().setTypePlay(2);
        ManagerDataPlay.getInstance().setListAudio((ArrayList) list);
        ManagerDataPlay.getInstance().setPosSelected(i);
        ManagerDataPlay.getInstance().duration = Long.valueOf(list.get(i).getDuration());
        startActivity(intent);
        Utils.nextScreen(this);
    }
}
