package com.colorcallscreen.colorphone.callscreen.calltheme.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentStateAdapter {
    private final List<String> fragmentTitle;
    private final List<Fragment> fragments;

    public void add(Fragment fragment, String str) {
        this.fragments.add(fragment);
        this.fragmentTitle.add(str);
    }

    public String gettitle(int i) {
        return this.fragmentTitle.get(i);
    }

    public ViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.fragments = new ArrayList();
        this.fragmentTitle = new ArrayList();
    }

    @Override // androidx.viewpager2.adapter.FragmentStateAdapter
    public Fragment createFragment(int i) {
        return this.fragments.get(i);
    }

    @Override 
    public int getItemCount() {
        return this.fragments.size();
    }
}
