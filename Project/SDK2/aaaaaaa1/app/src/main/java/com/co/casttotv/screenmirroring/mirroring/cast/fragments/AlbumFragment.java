package com.co.casttotv.screenmirroring.mirroring.cast.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.adapters.AlbumAdapter;
import com.co.casttotv.screenmirroring.mirroring.cast.adapters.AudioAdapter;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.MediaData;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.FragmentAlbumBinding;

public class AlbumFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private Boolean isAlbum = false;
    AlbumAdapter adapter;

    public AlbumFragment() {
        // Required empty public constructor
    }

    public static AlbumFragment newInstance(Boolean album) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, album);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isAlbum = getArguments().getBoolean(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAlbumBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_album, container, false);
        adapter = new AlbumAdapter(getContext(), isAlbum ? MediaData.getAudioAlbums(getContext()) : MediaData.getAudioArtists(getContext()), isAlbum);
        binding.setAdapter(adapter);
        return binding.getRoot();
    }
}