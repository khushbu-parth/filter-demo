package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.File;
import java.util.ArrayList;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.databinding.FragmentHistoryBinding;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.activity.FullViewActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.activity.GalleryActivity;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.adapter.FileListAdapter;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.interfaces.FileListClickInterface;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.Utils;

public class FBDownloadedFragment extends Fragment implements FileListClickInterface {
    private GalleryActivity activity;
    private FragmentHistoryBinding binding;
    private ArrayList<File> fileArrayList;
    private FileListAdapter fileListAdapter;

    public static FBDownloadedFragment newInstance(String str) {
        FBDownloadedFragment fBDownloadedFragment = new FBDownloadedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("m", str);
        fBDownloadedFragment.setArguments(bundle);
        return fBDownloadedFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (GalleryActivity) context;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            getArguments().getString("m");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.activity = (GalleryActivity) getActivity();
        getAllFiles();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.binding = (FragmentHistoryBinding) DataBindingUtil.inflate(layoutInflater, R.layout.fragment_history, viewGroup, false);
        initViews();
        return this.binding.getRoot();
    }

    private void initViews() {
        this.binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public final void onRefresh() {
                FBDownloadedFragment.this.lambda$initViews$0$FBDownloadedFragment();
            }
        });
    }

    public void lambda$initViews$0$FBDownloadedFragment() {
        getAllFiles();
        this.binding.swiperefresh.setRefreshing(false);
    }

    private void getAllFiles() {
        this.fileArrayList = new ArrayList<>();
        File[] listFiles = Utils.RootDirectoryFacebookShow.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                this.fileArrayList.add(file);
            }
            this.fileListAdapter = new FileListAdapter(this.activity, this.fileArrayList, this);
            this.binding.rvFileList.setAdapter(this.fileListAdapter);
        }
    }

    @Override
    public void getPosition(int i, File file) {
        Intent intent = new Intent(this.activity, FullViewActivity.class);
        intent.putExtra("ImageDataFile", this.fileArrayList);
        intent.putExtra("Position", i);
        this.activity.startActivity(intent);
    }
}