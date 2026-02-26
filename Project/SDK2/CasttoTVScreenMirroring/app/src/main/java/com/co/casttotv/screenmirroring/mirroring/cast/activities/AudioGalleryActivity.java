package com.co.casttotv.screenmirroring.mirroring.cast.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.ads.sdk.SdkManager;
import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.adapters.AudioAdapter;
import com.co.casttotv.screenmirroring.mirroring.cast.adapters.FragmentAdapter;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.DeviceManager;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.MediaData;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ActivityAudioGalleryBinding;
import com.co.casttotv.screenmirroring.mirroring.cast.fragments.AlbumFragment;
import com.co.casttotv.screenmirroring.mirroring.cast.fragments.SongFragment;
import com.co.casttotv.screenmirroring.mirroring.cast.models.FragmentModel;
import com.co.casttotv.screenmirroring.mirroring.cast.models.MediaModel;

import java.util.ArrayList;
import java.util.List;

public class AudioGalleryActivity extends AppCompatActivity {
    ActivityAudioGalleryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audio_gallery);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
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
        SdkManager.loadBanner(this, binding.bannerView);

        new LoadData().execute();
    }

    private List<FragmentModel> getList() {
        List<FragmentModel> modelList = new ArrayList<>();
        modelList.add(new FragmentModel("Song", SongFragment.newInstance()));
        modelList.add(new FragmentModel("Album", AlbumFragment.newInstance(true)));
        modelList.add(new FragmentModel("Artist", AlbumFragment.newInstance(false)));
        return modelList;
    }

    private class LoadData extends AsyncTask<Void, Void, ArrayList<MediaModel>> {

        @Override
        protected ArrayList<MediaModel> doInBackground(Void... voids) {
            return MediaData.getAllAudioFiles(AudioGalleryActivity.this);
        }

        @Override
        protected void onPostExecute(ArrayList<MediaModel> mediaModels) {
            super.onPostExecute(mediaModels);

            binding.viewpager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), getList()));
            binding.tabLayout.setupWithViewPager(binding.viewpager);

            binding.progress.setVisibility(View.GONE);
        }
    }
}