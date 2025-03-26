package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.whatsappdata;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;


public class VideoFragment extends Fragment {
    private static final String WHATSAPP_STATUSES_LOCATION = "/storage/emulated/0/WhatsApp/Media/.Statuses";
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_picture, viewGroup, false);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.pictures_recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        this.layoutManager = gridLayoutManager;
        this.recyclerView.setLayoutManager(gridLayoutManager);
        this.recyclerView.addItemDecoration(new ItemDecorations(4, 2));
        getListFiles(new File(WHATSAPP_STATUSES_LOCATION));
        this.recyclerView.setAdapter(new videoRecyclerAdapter(getListFiles(new File(WHATSAPP_STATUSES_LOCATION)), getActivity()));
        return inflate;
    }

    private ArrayList<File> getListFiles(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if ((file2.getName().endsWith(".mp4") || file2.getName().endsWith(".gif")) && !arrayList.contains(file2)) {
                    arrayList.add(file2);
                }
            }
        }
        return arrayList;
    }
}
