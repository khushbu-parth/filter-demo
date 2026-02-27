package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.photoff;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.playcast.PlayCastActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.premium.IapUtils;
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
public class PhotoOfflineActivity extends BaseActivity {
    private PhotoOfflineAdapter adapter;
    private int currentItem = 0;
    ExpansionLayout expansionLayout;
    private ImageView imvConnect;
    private boolean isLoadAll;
    private ArrayList<MediaModel> listPhoto;
    private LinearLayout llBack;
    private LinearLayout llConnect;
    private LinearLayout llHelp;
    private ViewGroup main_ads_native;
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
        getAllPhoto();
        PhotoOfflineActivity.this.callbackDone();
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
        Utils.backScreen(this);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void gotoPlay() {
        AdsManager.CallInterstitialAdLoad(this, 0, () -> {
            try {
                Intent intent = new Intent(this, PlayCastActivity.class);
                ManagerDataPlay.getInstance().setTypePlay(0);
                ManagerDataPlay.getInstance().setListMedia(this.listPhoto);
                ManagerDataPlay.getInstance().setPosSelected(this.currentItem);
                startActivity(intent);
                Utils.nextScreen(this);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        });

    }

    private void initView() {
        this.expansionLayout = (ExpansionLayout) findViewById(R.id.expansionLayout);
        this.viewHide = findViewById(R.id.viewHide);
        this.main_ads_native = (ViewGroup) findViewById(R.id.main_ads_native);
        this.imvConnect = (ImageView) findViewById(R.id.imvConnect);
        this.tvTitleTab = (TextView) findViewById(R.id.tvTitleTab);
        this.llConnect = (LinearLayout) findViewById(R.id.llConnect);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llSelected);
        ImageView imageView = (ImageView) findViewById(R.id.imvRow);
        this.rcvListAlbum = (RecyclerView) findViewById(R.id.rcvListAlbum);
        this.tvNameSelected = (TextView) findViewById(R.id.tvNameSelected);
        this.llBack = (LinearLayout) findViewById(R.id.llBack);
        this.rcvList = (RecyclerView) findViewById(R.id.rcvList);
        this.listPhoto = new ArrayList<>();
        this.tvTitleTab.setText(getString(R.string.txt_photo));
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.llHelp);
        this.llHelp = linearLayout2;
        linearLayout2.setVisibility(0);
        this.llHelp.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent(PhotoOfflineActivity.this, HowToYouActivity.class);
                intent.putExtra("TYPE_HTY", 1);
                PhotoOfflineActivity.this.startActivity(intent);
                Utils.nextScreen(PhotoOfflineActivity.this);
            }
        });
        GridLayoutManager manager = new GridLayoutManager(this, 3);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? manager.getSpanCount() : 1;
            }
        });
        this.rcvList.setLayoutManager(manager);
        PhotoOfflineAdapter photoOfflineAdapter = new PhotoOfflineAdapter(new ArrayList(), this, 0);
        this.adapter = photoOfflineAdapter;
        photoOfflineAdapter.setClickItem(new PhotoOfflineAdapter.OnItemClickPhoto() {
            @Override
            public void itemClick(List<MediaModel> list, int i) {
                if (TVConnectUtils.getInstance().isConnected()) {
                    PhotoOfflineActivity.this.listPhoto.clear();
                    PhotoOfflineActivity.this.listPhoto.addAll(list);
                    PhotoOfflineActivity.this.currentItem = i;
                    Log.e("parth", "listPhoto: "+list.size());
                    Log.e("parth", "currentItem: "+i);
                    PhotoOfflineActivity.this.gotoPlay();
                    return;
                }
                PhotoOfflineActivity.this.startActivity(new Intent(PhotoOfflineActivity.this, ConnectActivity.class));
                Utils.nextScreen(PhotoOfflineActivity.this);
            }
        });
        this.rcvList.setAdapter(this.adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(1);
        this.rcvListAlbum.setLayoutManager(linearLayoutManager);
       // PhotoAlbumAdapter photoAlbumAdapter = new PhotoAlbumAdapter(this, new ArrayList());
      //  this.photoAlbumAdapter = photoAlbumAdapter;
       // this.rcvListAlbum.setAdapter(photoAlbumAdapter);
       // this.photoAlbumAdapter.setItemClick(new PhotoAlbumAdapter.OnItemClick() {
//            @Override
//            public void onItemClick(int i, PhotoAlbum photoAlbum) {
//                PhotoOfflineActivity.this.expansionLayout.collapse(false);
//                PhotoOfflineActivity.this.tvNameSelected.setText(photoAlbum.getName());
//                PhotoOfflineActivity.this.adapter.setData(photoAlbum.getAlbumPhotos());
//                PhotoOfflineActivity.this.listPhoto.clear();
//                PhotoOfflineActivity.this.listPhoto.addAll(photoAlbum.getAlbumPhotos());
//                PhotoOfflineActivity.this.isLoadAll = false;
//            }
//        });
        this.viewHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoOfflineActivity.this.expansionLayout.collapse(false);
            }
        });
        this.rcvListAlbum.setVisibility(View.VISIBLE);
        getAlbumPhoto();
        this.llConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TVConnectUtils.getInstance().isConnected()) {
                    new DialogDisconnect(PhotoOfflineActivity.this).show();
                    return;
                }
                PhotoOfflineActivity.this.startActivity(new Intent(PhotoOfflineActivity.this, ConnectActivity.class));
                Utils.nextScreen(PhotoOfflineActivity.this);
            }
        });
        this.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoOfflineActivity.this.onBackPressed();
            }
        });
        if (TVConnectUtils.getInstance().isConnected()) {
            imvConnect.setImageResource(R.drawable.hover_screen);
        } else {
            imvConnect.setImageResource(R.drawable.screen);
        }
    }

    private void getAllPhoto() {
        Observable.create(new ObservableOnSubscribe<ArrayList<MediaModel>>() {
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<ArrayList<MediaModel>> observableEmitter) {
                observableEmitter.onNext(Utils.getAllPhoto(PhotoOfflineActivity.this));
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList<MediaModel>>() { // from class: com.magicapps.casttotv.tv.screen.tab.photoff.PhotoOfflineActivity.7
            @Override // io.reactivex.Observer
            public void onComplete() {
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
            }

            @Override // io.reactivex.Observer
            public void onSubscribe(Disposable disposable) {
            }

            @Override // io.reactivex.Observer
            public void onNext(ArrayList<MediaModel> arrayList) {
                PhotoOfflineActivity.this.adapter.setData(arrayList);
                PhotoOfflineActivity.this.listPhoto.clear();
                PhotoOfflineActivity.this.listPhoto.addAll(arrayList);
            }
        });
    }

    private void getAlbumPhoto() {
        Observable.create(new ObservableOnSubscribe<ArrayList<PhotoAlbum>>() { // from class: com.magicapps.casttotv.tv.screen.tab.photoff.PhotoOfflineActivity.10
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<ArrayList<PhotoAlbum>> observableEmitter) {
                observableEmitter.onNext(Utils.getPhotoAlbums(PhotoOfflineActivity.this));
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList<PhotoAlbum>>() { // from class: com.magicapps.casttotv.tv.screen.tab.photoff.PhotoOfflineActivity.9
            @Override // io.reactivex.Observer
            public void onComplete() {
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
            }

            @Override // io.reactivex.Observer
            public void onSubscribe(Disposable disposable) {
            }

            @Override // io.reactivex.Observer
            public void onNext(ArrayList<PhotoAlbum> arrayList) {
//                PhotoOfflineActivity.this.photoAlbumAdapter.setData(arrayList);
            }
        });
    }
}
