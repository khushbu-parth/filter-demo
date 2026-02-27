package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.audio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.AudioAlbumModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.ManagerDataPlay;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;

/* loaded from: classes4.dex */
public class AritistFragment extends Fragment {
    private AudioAlbumAdapter audioArtistAdapter;
    private RecyclerView rcvListSong;

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_song, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View view) {
        this.rcvListSong = (RecyclerView) view.findViewById(R.id.rcvListSong);
        this.rcvListSong.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        AudioAlbumAdapter audioAlbumAdapter = new AudioAlbumAdapter(new ArrayList(), getContext(), 2);
        this.audioArtistAdapter = audioAlbumAdapter;
        this.rcvListSong.setAdapter(audioAlbumAdapter);
        this.audioArtistAdapter.setClickItem(new AudioAlbumAdapter.OnItemClickPhoto() { // from class: com.magicapps.casttotv.tv.screen.tab.audio.AritistFragment.1
            @Override // com.magicapps.casttotv.tv.screen.tab.audio.AudioAlbumAdapter.OnItemClickPhoto
            public void itemClick(AudioAlbumModel audioAlbumModel) {
                ManagerDataPlay.getInstance().setListAudio(audioAlbumModel.getArrSong());
                ManagerDataPlay.getInstance().titleAudio = audioAlbumModel.getArrSong().get(0).getSongArtist();
                AritistFragment.this.startActivity(new Intent(AritistFragment.this.getContext(), DetailAudioActivity.class));
            }
        });
        getAllArtist();
    }

    private void getAllArtist() {
        Observable.create(new ObservableOnSubscribe<ArrayList<AudioAlbumModel>>() { // from class: com.magicapps.casttotv.tv.screen.tab.audio.AritistFragment.3
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<ArrayList<AudioAlbumModel>> observableEmitter) {
                observableEmitter.onNext(Utils.getAllArtist(AritistFragment.this.getContext()));
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList<AudioAlbumModel>>() { // from class: com.magicapps.casttotv.tv.screen.tab.audio.AritistFragment.2
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
                AritistFragment.this.audioArtistAdapter.setData(arrayList);
            }
        });
    }
}
