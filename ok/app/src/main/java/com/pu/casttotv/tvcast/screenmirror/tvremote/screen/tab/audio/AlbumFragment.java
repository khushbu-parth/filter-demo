package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.audio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseFragment;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.AudioAlbumModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.ManagerDataPlay;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AlbumFragment extends BaseFragment {
    private AudioAlbumAdapter audioAlbumAdapter;
    private RecyclerView rcvListSong;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_song, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View view) {
        this.rcvListSong = (RecyclerView) view.findViewById(R.id.rcvListSong);
        this.rcvListSong.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        AudioAlbumAdapter audioAlbumAdapter = new AudioAlbumAdapter(new ArrayList(), getContext(), 0);
        this.audioAlbumAdapter = audioAlbumAdapter;
        this.rcvListSong.setAdapter(audioAlbumAdapter);
        this.audioAlbumAdapter.setClickItem(new AudioAlbumAdapter.OnItemClickPhoto() {
            @Override
            public void itemClick(AudioAlbumModel audioAlbumModel) {
                ManagerDataPlay.getInstance().setListAudio(audioAlbumModel.getArrSong());
                ManagerDataPlay.getInstance().titleAudio = audioAlbumModel.getNameAlbum();
                AlbumFragment.this.startActivity(new Intent(AlbumFragment.this.getContext(), DetailAudioActivity.class));
            }
        });
        getAllAlbum();
    }

    private void getAllAlbum() {
        Observable.create(new ObservableOnSubscribe<ArrayList<AudioAlbumModel>>() {
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<ArrayList<AudioAlbumModel>> observableEmitter) {
                observableEmitter.onNext(Utils.getAllAlbumAudio(AlbumFragment.this.getContext()));
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList<AudioAlbumModel>>() {
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
            public void onNext(ArrayList<AudioAlbumModel> arrayList) {
                AlbumFragment.this.audioAlbumAdapter.setData(arrayList);
            }
        });
    }
}
