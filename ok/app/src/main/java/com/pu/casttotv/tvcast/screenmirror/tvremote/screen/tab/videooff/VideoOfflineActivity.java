package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.videooff;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MediaModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.PhotoAlbum;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.ManagerDataPlay;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.ConnectActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.DialogDisconnect;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.howto.HowToYouActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.photoff.PhotoOfflineAdapter;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.playcast.PlayCastActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;
import com.github.florent37.expansionpanel.ExpansionLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;

@SuppressLint("WrongConstant")
public class VideoOfflineActivity extends BaseActivity {
    private PhotoOfflineAdapter adapter;
    private int currentItem = 0;
    private ExpansionLayout expansionLayout;
    private ImageView imvConnect;
    private ArrayList<MediaModel> listVideo;
    private LinearLayout llBack;
    private LinearLayout llConnect;
    private LinearLayout llHelp;
//    private PhotoAlbumAdapter photoAlbumAdapter;
    private RecyclerView rcvList;
    private RecyclerView rcvListAlbum;
    private TextView tvNameSelected;
    private TextView tvTitleTab;
    private View viewHide;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_photo_offline);


        EventBus.getDefault().register(this);
        initView();
        getAllVideo();
    }

    public Unit callbackFail() {
        return Unit.INSTANCE;
    }

    public Unit callbackDone() {
        return Unit.INSTANCE;
    }

    public Unit actionCommon() {
        gotoPlay();
        return Unit.INSTANCE;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (TVConnectUtils.getInstance().isConnected()) {
            imvConnect.setImageResource(R.drawable.hover_screen);
        } else {
            imvConnect.setImageResource(R.drawable.screen);
        }
    }

    private void gotoPlay() {
        AdsManager.CallInterstitialAdLoad(this, 0, () -> {
            Intent intent = new Intent(this, PlayCastActivity.class);
            ManagerDataPlay.getInstance().setTypePlay(1);
            ManagerDataPlay.getInstance().setListMedia(this.listVideo);
            ManagerDataPlay.getInstance().setPosSelected(this.currentItem);
            ManagerDataPlay.getInstance().duration = this.listVideo.get(this.currentItem).getDuration();
            Log.e("##TAG", "listVideo: " + listVideo.iterator());
            Log.e("##TAG", "currentItem: " + currentItem);
            Log.e("##TAG", "listVideo currentItem : " + this.listVideo.get(this.currentItem).getDuration());
            startActivity(intent);
            Utils.nextScreen(this);
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getAllVideo() {
        Observable.create(new ObservableOnSubscribe<ArrayList<MediaModel>>() {
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<ArrayList<MediaModel>> observableEmitter) {
                observableEmitter.onNext(Utils.getMediaVideos(VideoOfflineActivity.this));
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList<MediaModel>>() {
            @Override // io.reactivex.Observer
            public void onComplete() {
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
            }

            @Override // io.reactivex.Observer
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(ArrayList<MediaModel> arrayList) {
                VideoOfflineActivity.this.adapter.setData(arrayList);
            }
        });
    }

    private void initView() {
        this.imvConnect = (ImageView) findViewById(R.id.imvConnect);
        this.llBack = (LinearLayout) findViewById(R.id.llBack);
        this.llConnect = (LinearLayout) findViewById(R.id.llConnect);
        this.expansionLayout = (ExpansionLayout) findViewById(R.id.expansionLayout);
        this.tvTitleTab = (TextView) findViewById(R.id.tvTitleTab);
        this.viewHide = findViewById(R.id.viewHide);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llSelected);
        this.rcvListAlbum = (RecyclerView) findViewById(R.id.rcvListAlbum);
        this.tvNameSelected = (TextView) findViewById(R.id.tvNameSelected);
        this.rcvList = (RecyclerView) findViewById(R.id.rcvList);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.llHelp);
        this.llHelp = linearLayout2;
        linearLayout2.setVisibility(0);
        this.llHelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(VideoOfflineActivity.this, HowToYouActivity.class);
                intent.putExtra("TYPE_HTY", 1);
                VideoOfflineActivity.this.startActivity(intent);
                Utils.nextScreen(VideoOfflineActivity.this);
            }
        });
        this.tvTitleTab.setText(getString(R.string.txt_video));
        this.tvNameSelected.setText(getString(R.string.all_video));
        this.listVideo = new ArrayList<>();

        GridLayoutManager manager = new GridLayoutManager(this, 3);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? manager.getSpanCount() : 1;
            }
        });
        this.rcvList.setLayoutManager(manager);
        this.adapter = new PhotoOfflineAdapter(new ArrayList(), this, 2);
        if (TVConnectUtils.getInstance().isConnected()) {
            imvConnect.setImageResource(R.drawable.hover_screen);
        } else {
            imvConnect.setImageResource(R.drawable.screen);
        }
        this.adapter.setClickItem(new PhotoOfflineAdapter.OnItemClickPhoto() {
            @Override
            public void itemClick(List<MediaModel> list, int i) {
                if (TVConnectUtils.getInstance().isConnected()) {
                    VideoOfflineActivity.this.listVideo.clear();
                    VideoOfflineActivity.this.listVideo.addAll(list);
                    VideoOfflineActivity.this.currentItem = i;
                    VideoOfflineActivity.this.gotoPlay();
                } else {
                    VideoOfflineActivity.this.startActivity(new Intent(VideoOfflineActivity.this, ConnectActivity.class));
                    Utils.nextScreen(VideoOfflineActivity.this);
                }
            }
        });
        this.rcvList.setAdapter(this.adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(1);
        this.rcvListAlbum.setLayoutManager(linearLayoutManager);
//        PhotoAlbumAdapter photoAlbumAdapter2 = new PhotoAlbumAdapter(this, new ArrayList());
//        this.photoAlbumAdapter = photoAlbumAdapter2;
//        this.rcvListAlbum.setAdapter(photoAlbumAdapter2);
//        this.photoAlbumAdapter.setItemClick(new PhotoAlbumAdapter.OnItemClick() {
//            @Override
//            public void onItemClick(int i, PhotoAlbum photoAlbum) {
//                VideoOfflineActivity.this.tvNameSelected.setText(photoAlbum.getName());
//                VideoOfflineActivity.this.adapter.setData(photoAlbum.getAlbumPhotos());
//                VideoOfflineActivity.this.expansionLayout.collapse(false);
//            }
//        });
        this.viewHide.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                VideoOfflineActivity.this.expansionLayout.collapse(false);
            }
        });
        this.rcvListAlbum.setVisibility(0);
        getAlbumPhoto();
        this.llBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                VideoOfflineActivity.this.onBackPressed();
            }
        });
        this.llConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TVConnectUtils.getInstance().isConnected()) {
                    new DialogDisconnect(VideoOfflineActivity.this).show();
                    return;
                }
                VideoOfflineActivity.this.startActivity(new Intent(VideoOfflineActivity.this, ConnectActivity.class));
                Utils.nextScreen(VideoOfflineActivity.this);
            }
        });
    }

    private void getAlbumPhoto() {
        Observable.create(new ObservableOnSubscribe<ArrayList<PhotoAlbum>>() {
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<ArrayList<PhotoAlbum>> observableEmitter) {
                observableEmitter.onNext(Utils.getVideoAlbums(VideoOfflineActivity.this));
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList<PhotoAlbum>>() {
            @Override // io.reactivex.Observer
            public void onComplete() {
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
            }

            @Override // io.reactivex.Observer
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(ArrayList<PhotoAlbum> arrayList) {
//                VideoOfflineActivity.this.photoAlbumAdapter.setData(arrayList);
            }
        });
    }
}