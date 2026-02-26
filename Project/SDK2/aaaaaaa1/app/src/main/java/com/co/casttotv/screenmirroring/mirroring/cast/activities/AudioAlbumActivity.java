package com.co.casttotv.screenmirroring.mirroring.cast.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.adapters.AudioAdapter;
import com.co.casttotv.screenmirroring.mirroring.cast.adapters.FragmentAdapter;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.DeviceManager;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.MediaData;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ActivityAudioAlbumBinding;
import com.co.casttotv.screenmirroring.mirroring.cast.models.MediaModel;

import java.util.ArrayList;

public class AudioAlbumActivity extends AppCompatActivity {
    ActivityAudioAlbumBinding binding;
    Boolean isAlbum = false;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audio_album);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
        isAlbum = getIntent().getBooleanExtra("type", false);
        name = getIntent().getStringExtra("root");
        binding.toolbar.setTitle(name);

        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_cast) {
                if (DeviceManager.getInstance().isConnected()) {
                    DeviceManager.showDeviceDialog(this);
                } else
                    startActivity(new Intent(this, ScanningActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            }
            if (item.getItemId() == R.id.nav_how) {
                startActivity(new Intent(this, HowToActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            }
            return false;
        });

        new LoadData().execute();
    }

    private class LoadData extends AsyncTask<Void, Void, ArrayList<MediaModel>> {

        @Override
        protected ArrayList<MediaModel> doInBackground(Void... voids) {
            return MediaData.getAudiosFrom(AudioAlbumActivity.this, name, isAlbum);
        }

        @Override
        protected void onPostExecute(ArrayList<MediaModel> mediaModels) {
            super.onPostExecute(mediaModels);
            binding.setAdapter(new AudioAdapter(AudioAlbumActivity.this, mediaModels));

            if (mediaModels.size() == 0){
                binding.textNodata.setVisibility(View.VISIBLE);
            }
            binding.progress.setVisibility(View.GONE);
        }
    }
}