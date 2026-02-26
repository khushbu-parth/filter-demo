package com.co.casttotv.screenmirroring.mirroring.cast.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.co.casttotv.screenmirroring.mirroring.cast.models.FragmentModel;

import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
    List<FragmentModel> modelList;

    public FragmentAdapter(@NonNull FragmentManager fm, List<FragmentModel> modelList) {
        super(fm);
        this.modelList = modelList;
    }

    @Override
    public Fragment getItem(int position) {
        return modelList.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return modelList.get(position).getTitle();
    }
}
