package com.co.casttotv.screenmirroring.mirroring.cast.activities;

import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.ads.sdk.SdkManager;
import com.co.casttotv.screenmirroring.mirroring.cast.R;
import com.co.casttotv.screenmirroring.mirroring.cast.adapters.FolderAdapter;
import com.co.casttotv.screenmirroring.mirroring.cast.adapters.ImageAdapter;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.DeviceManager;
import com.co.casttotv.screenmirroring.mirroring.cast.constans.MediaData;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.ActivityImageGalleryBinding;
import com.co.casttotv.screenmirroring.mirroring.cast.databinding.DialogFoldersBinding;
import com.co.casttotv.screenmirroring.mirroring.cast.models.MediaModel;

import java.util.ArrayList;

public class ImageGalleryActivity extends BaseActivity {
    ActivityImageGalleryBinding binding;
    ImageAdapter adapter;
    ArrayList<String> folderList;
    ArrayList<MediaModel> dataList;
    Boolean isVideo = false;

    @Override
    protected void onViewCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_gallery);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
        isVideo = getIntent().getBooleanExtra(MediaData.TYPE_MEDIA_KEY, false);
        binding.toolbar.setTitle(getString(isVideo ? R.string.videos : R.string.photos));
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

        binding.textFolder.setText(isVideo ? "All Videos" : "All Photos");

        new LoadData().execute();
    }

    private class LoadData extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return isVideo ? MediaData.getVideoFolder(ImageGalleryActivity.this) : MediaData.getImageFolder(ImageGalleryActivity.this);
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            folderList = new ArrayList<>(strings);
            dataList = new ArrayList<>(isVideo ? MediaData.getVideosFrom(ImageGalleryActivity.this, folderList.get(0)) : MediaData.getImagesFrom(ImageGalleryActivity.this, folderList.get(0)));

            adapter = new ImageAdapter(ImageGalleryActivity.this, dataList, isVideo);
            binding.setAdapter(adapter);
            binding.llSelectFolder.setOnClickListener(view -> showFolder(folderList));

            if (dataList.size() != 0) {
                binding.textNodata.setVisibility(View.GONE);
            } else {
                binding.textNodata.setVisibility(View.VISIBLE);
            }

            binding.progress.setVisibility(View.GONE);
        }
    }

    private void showFolder(ArrayList<String> folderList) {
        final Dialog dialog = new Dialog(ImageGalleryActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DialogFoldersBinding foldersBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_folders, null, false);
        dialog.setContentView(foldersBinding.getRoot());
        foldersBinding.setFolderAdapter(new FolderAdapter(this, folderList, isVideo, folder -> {
            dataList.clear();
            if (isVideo) {
                dataList.addAll(MediaData.getVideosFrom(ImageGalleryActivity.this, folder));
            } else {
                dataList.addAll(MediaData.getImagesFrom(ImageGalleryActivity.this, folder));
            }
            if (dataList.size() != 0) {
                binding.textNodata.setVisibility(View.GONE);
            } else {
                binding.textNodata.setVisibility(View.VISIBLE);
            }
            if (adapter != null)
                adapter.Update(dataList);
            binding.textFolder.setText(folder);

            dialog.dismiss();
        }));
        try {
            dialog.show();
        } catch (Exception e) {
        }
    }

}