package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.audio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.AudioModel;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.ConnectActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SongFragment extends Fragment {

    private AudioAdapter adapterSong;
    private AudioActivity audioActivity;
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
        this.rcvListSong = view.findViewById(R.id.rcvListSong);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        this.rcvListSong.setLayoutManager(linearLayoutManager);
        AudioAdapter audioAdapter = new AudioAdapter(new ArrayList(), getContext());
        this.adapterSong = audioAdapter;
        this.rcvListSong.setAdapter(audioAdapter);
        this.adapterSong.setClickItem(new AudioAdapter.OnItemClickPhoto() {
            @Override
            public void itemClick(List<AudioModel> list, int i) {
                if (TVConnectUtils.getInstance().isConnected()) {
                    if (SongFragment.this.audioActivity != null) {
                        SongFragment.this.audioActivity.gotoPlay(list, i);
                    }
                    return;
                }
                SongFragment.this.startActivity(new Intent(SongFragment.this.getContext(), ConnectActivity.class));
                Utils.nextScreen(SongFragment.this.getActivity());
            }
        });
        getAllSong();
    }

    private void getAllSong() {
        Observable.create(new ObservableOnSubscribe<ArrayList<AudioModel>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ArrayList<AudioModel>> observableEmitter) {
                observableEmitter.onNext(Utils.getAllAudioFromDevice(SongFragment.this.getContext()));
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList<AudioModel>>() {
            @Override
            public void onComplete() {
            }

            @Override
            public void onError(@NonNull Throwable th) {
            }

            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
            }

            @Override
            public void onNext(@NonNull ArrayList<AudioModel> arrayList) {
                if (SongFragment.this.audioActivity != null) {
                    SongFragment.this.audioActivity.audioModelArrayList.clear();
                    SongFragment.this.audioActivity.audioModelArrayList.addAll(arrayList);
                }
                SongFragment.this.adapterSong.setData(arrayList);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.audioActivity = (AudioActivity) context;
    }
}
