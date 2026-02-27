package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.remote;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;

/* loaded from: classes4.dex */
public class PagerHomeAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public PagerHomeAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.fragments = new ArrayList<>();
        new ChannelFragment();
        this.fragments.clear();
        this.fragments.add(new RemoteFragment());
        this.fragments.add(new ChannelFragment());
    }

    @Override // androidx.fragment.app.FragmentPagerAdapter
    public Fragment getItem(int i) {
        return this.fragments.get(i);
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return this.fragments.size();
    }
}
