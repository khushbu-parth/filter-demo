package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.drive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("WrongConstant")
public class DetailDriverFragment extends BaseFragment {
    private FileAdapter adapter;
    private DriverFileActivity driverFileActivity;
    private RecyclerView rcvDetail;

    public void setData(List<GoogleDriveItem> list) {
        FileAdapter fileAdapter = this.adapter;
        if (fileAdapter != null) {
            fileAdapter.setData(list);
        }
    }

    @Override // com.magicapps.casttotv.tv.base.BaseFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_detail_driver, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View view) {
        this.rcvDetail = (RecyclerView) view.findViewById(R.id.rcvDetail);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(1);
        this.rcvDetail.setLayoutManager(linearLayoutManager);
        FileAdapter fileAdapter = new FileAdapter(new ArrayList());
        this.adapter = fileAdapter;
        fileAdapter.setListener(new FileAdapter.IItemClick() { // from class: com.magicapps.casttotv.tv.screen.tab.drive.DetailDriverFragment.1
            @Override // com.magicapps.casttotv.tv.screen.tab.drive.FileAdapter.IItemClick
            public void clickItem(GoogleDriveItem googleDriveItem, List<GoogleDriveItem> list) {
                if (DetailDriverFragment.this.driverFileActivity != null) {
                    DetailDriverFragment.this.driverFileActivity.gotoCast(googleDriveItem, list);
                }
            }
        });
        this.rcvDetail.setAdapter(this.adapter);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.driverFileActivity = (DriverFileActivity) context;
    }
}
