package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: classes4.dex */
public class VideosFragment extends Fragment {
    file_type File_type;
    private RecyclerView downloadsList;
    private View view;

    public VideosFragment(file_type file_typeVar) {
        this.File_type = file_typeVar;
    }

    public static VideosFragment newInstance(file_type file_typeVar) {
        VideosFragment videosFragment = new VideosFragment(file_typeVar);
        videosFragment.setArguments(new Bundle());
        return videosFragment;
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getArguments();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (this.view == null) {
            View inflate = layoutInflater.inflate(R.layout.fragment_videos, viewGroup, false);
            this.view = inflate;
            this.downloadsList = (RecyclerView) inflate.findViewById(R.id.downloadsCompletedList);
            File file = new File(SettingsManager.DOWNLOAD_FOLDER_VIDEO);
            if (this.File_type == file_type.AUDIO) {
                file = new File(SettingsManager.DOWNLOAD_FOLDER_AUDIO);
            }
            if (this.File_type == file_type.IMAGE) {
                file = new File(SettingsManager.DOWNLOAD_FOLDER_IMAGES);
            }
            if (file.exists()) {
                ArrayList arrayList = new ArrayList(Arrays.asList(file.listFiles()));
                if (!arrayList.isEmpty()) {
                    this.downloadsList.setAdapter(new DownloadsAdapter(arrayList, getActivity(), this.File_type));
                    this.downloadsList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    this.downloadsList.setHasFixedSize(true);
                }
            }
        }
        return this.view;
    }
}
