package com.adsdemo.vdapps.adsload.MoreApps;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adsdemo.vdapps.R;
import com.adsdemo.vdapps.adsload.AdsManager;
import com.adsdemo.vdapps.adsload.models.MoreAppModel;

import java.util.ArrayList;

public class Ad_MoreAppsFragment extends Fragment {

    private RecyclerView rvApplist;
    private RecyclerView rvApplist1;
    private Ad_MoreAppListAdapter objAppListAdapterSplash;
    private Ad_MoreAppListAdapter1 objAppListAdapterSplash1;
    private TextView noData;
    private LinearLayout llRecyclerList, llrvApplist1;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ad_fragment_more_apps, container, false);
        bind(view);
        return view;
    }

    private void bind(View view) {
        llRecyclerList = view.findViewById(R.id.llRecyclerList);
        llrvApplist1 = view.findViewById(R.id.llrvApplist1);
        rvApplist = view.findViewById(R.id.rvApplist);
        rvApplist1 = view.findViewById(R.id.rvApplist1);
        noData = view.findViewById(R.id.noData);
        setRecyclerviewLayout();
    }

    private void setRecyclerviewLayout() {
        rvApplist.setHasFixedSize(true);
        rvApplist1.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(getContext(), 3);
        rvApplist.setLayoutManager(layoutManager);
        rvApplist1.setLayoutManager(layoutManager1);
        setRecyclerView();
    }

    private void setRecyclerView() {
        if (AdsManager.moreAppsList == null) {
            llRecyclerList.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        } else {
            ArrayList<MoreAppModel> otherapp = new ArrayList<>();
            ArrayList<MoreAppModel> otherapp1 = new ArrayList<>();
            int i;
            if (AdsManager.moreAppsList.size() % 2 == 0) {
                i = AdsManager.moreAppsList.size() / 2;
            } else {
                i = (AdsManager.moreAppsList.size() / 2) + 1;
            }
            for (int k = 0; k < i; k++) {
                otherapp.add(AdsManager.moreAppsList.get(k));
            }
            for (int k = i; k < AdsManager.moreAppsList.size(); k++) {
                otherapp1.add(AdsManager.moreAppsList.get(k));
            }
            objAppListAdapterSplash = new Ad_MoreAppListAdapter(getContext(),otherapp);
            rvApplist.setAdapter(objAppListAdapterSplash);
            llrvApplist1.setVisibility(View.VISIBLE);
            rvApplist1.setVisibility(View.VISIBLE);
            objAppListAdapterSplash1 = new Ad_MoreAppListAdapter1(getContext(), otherapp1);
            rvApplist1.setAdapter(objAppListAdapterSplash1);

        }
    }
}