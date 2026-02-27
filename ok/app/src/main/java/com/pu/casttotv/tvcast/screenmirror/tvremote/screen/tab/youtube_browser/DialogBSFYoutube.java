package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.youtube_browser;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

@SuppressLint("WrongConstant")
public class DialogBSFYoutube extends BottomSheetDialogFragment implements View.OnClickListener, YoutubeListAdapter.OnItemClickListener {
    public static DialogBSFYoutube INSTANCE;
    private ImageView imv_dialogBsfYoutubeClose;
    private ImageView imv_dialogBsfYoutubeHelp;
    private ItemClickListener mListener;
    private RecyclerView recyclerView;
    private TextView tv_help;
    public YoutubeListAdapter youtubeListAdapter;
    private ArrayList<YoutubeDto> dtoArrayList = new ArrayList<>();
    private Boolean clickImvHelp = Boolean.FALSE;

    /* loaded from: classes4.dex */
    public interface ItemClickListener {
        void onClick(int i);
    }

    public static DialogBSFYoutube getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DialogBSFYoutube();
        }
        return INSTANCE;
    }

    public void setListener(ItemClickListener itemClickListener) {
        this.mListener = itemClickListener;
    }

    public void setData(ArrayList<YoutubeDto> arrayList) {
        try {
            ArrayList<YoutubeDto> arrayList2 = new ArrayList<>();
            this.dtoArrayList = arrayList2;
            arrayList2.addAll(arrayList);
            this.youtubeListAdapter.setData(this.dtoArrayList);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.dialog_bsf_youtube, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View view) {
        this.imv_dialogBsfYoutubeClose = (ImageView) view.findViewById(R.id.imv_dialogBsfYoutubeClose);
        this.imv_dialogBsfYoutubeHelp = (ImageView) view.findViewById(R.id.imv_dialogBsfYoutubeHelp);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.tv_help = (TextView) view.findViewById(R.id.tv_help);
        this.youtubeListAdapter = new YoutubeListAdapter(getActivity(), this.dtoArrayList, this);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(this.youtubeListAdapter);
        this.imv_dialogBsfYoutubeClose.setOnClickListener(this);
        this.imv_dialogBsfYoutubeHelp.setOnClickListener(this);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(0, R.style.CustomBottomSheetDialogTheme);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_dialogBsfYoutubeClose /* 2131362412 */:
                dismiss();
                return;
            case R.id.imv_dialogBsfYoutubeHelp /* 2131362413 */:
                if (!this.clickImvHelp.booleanValue()) {
                    this.tv_help.setVisibility(0);
                    this.clickImvHelp = Boolean.TRUE;
                    return;
                }
                this.tv_help.setVisibility(8);
                this.clickImvHelp = Boolean.FALSE;
                return;
            default:
                return;
        }
    }

    @Override
    // com.magicapps.casttotv.tv.screen.tab.youtube_browser.YoutubeListAdapter.OnItemClickListener
    public void onItemClick(int i) {
        ItemClickListener itemClickListener = this.mListener;
        if (itemClickListener != null) {
            itemClickListener.onClick(i);
        }
    }
}
