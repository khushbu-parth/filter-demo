package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.adapter.WhatsappStatusAdapter;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.databinding.FragmentWhatsappImageBinding;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.model.WhatsappStatusModel;

public class WhatsappImageFragment extends Fragment {
    FragmentWhatsappImageBinding binding;
    ArrayList<WhatsappStatusModel> statusModelArrayList;
    private File[] allfiles;
    private WhatsappStatusAdapter whatsappStatusAdapter;

    static int lambda$getData$1(Object obj, Object obj2) {
        File file = (File) obj;
        File file2 = (File) obj2;
        if (file.lastModified() > file2.lastModified()) {
            return -1;
        }
        return file.lastModified() < file2.lastModified() ? 1 : 0;
    }

    static int lambda$getData$2(Object obj, Object obj2) {
        File file = (File) obj;
        File file2 = (File) obj2;
        if (file.lastModified() > file2.lastModified()) {
            return -1;
        }
        return file.lastModified() < file2.lastModified() ? 1 : 0;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.binding = (FragmentWhatsappImageBinding) DataBindingUtil.inflate(layoutInflater, R.layout.fragment_whatsapp_image, viewGroup, false);
        initViews();
        return this.binding.getRoot();
    }

    private void initViews() {
        this.statusModelArrayList = new ArrayList<>();
        getData();
        this.binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public final void onRefresh() {
                WhatsappImageFragment.this.lambda$initViews$0$WhatsappImageFragment();
            }
        });
    }

    public void lambda$initViews$0$WhatsappImageFragment() {
        this.statusModelArrayList = new ArrayList<>();
        getData();
        this.binding.swiperefresh.setRefreshing(false);
    }

    private void getData() {
        this.allfiles = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses").listFiles();
        File[] listFiles = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/.Statuses").listFiles();
        try {
            Arrays.sort(this.allfiles, $$Lambda$WhatsappImageFragment$uI3_EBFKsDEnLnBrqTDwTVoQ68.INSTANCE);
            for (int i = 0; i < this.allfiles.length; i++) {
                File file = this.allfiles[i];
                if (Uri.fromFile(file).toString().endsWith(".png") || Uri.fromFile(file).toString().endsWith(".jpg")) {
                    this.statusModelArrayList.add(new WhatsappStatusModel("WhatsStatus: " + (i + 1), Uri.fromFile(file), this.allfiles[i].getAbsolutePath(), file.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Arrays.sort(listFiles, $$Lambda$WhatsappImageFragment$og71Jt8tHCd69UwVR9fhA6pRtnk.INSTANCE);
            for (int i2 = 0; i2 < listFiles.length; i2++) {
                File file2 = listFiles[i2];
                if (Uri.fromFile(file2).toString().endsWith(".png") || Uri.fromFile(file2).toString().endsWith(".jpg")) {
                    this.statusModelArrayList.add(new WhatsappStatusModel("WhatsStatusB: " + (i2 + 1), Uri.fromFile(file2), listFiles[i2].getAbsolutePath(), file2.getName()));
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (this.statusModelArrayList.size() != 0) {
            this.binding.tvNoResult.setVisibility(View.GONE);
        } else {
            this.binding.tvNoResult.setVisibility(View.VISIBLE);
        }
        this.whatsappStatusAdapter = new WhatsappStatusAdapter(getActivity(), this.statusModelArrayList);
        this.binding.rvFileList.setAdapter(this.whatsappStatusAdapter);
    }
}