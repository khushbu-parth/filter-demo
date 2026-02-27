package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/* loaded from: classes4.dex */
public class TabAdapter extends FragmentStateAdapter {
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return 3;
    }

    public TabAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override // androidx.viewpager2.adapter.FragmentStateAdapter
    public Fragment createFragment(int i) {
        if (i == 0) {
            return VideosFragment.newInstance(file_type.VIDEO);
        }
        if (i == 1) {
            return VideosFragment.newInstance(file_type.AUDIO);
        }
        return VideosFragment.newInstance(file_type.IMAGE);
    }
}
