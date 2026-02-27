package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.audio;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/* loaded from: classes4.dex */
public class AudioPagerAdapter extends FragmentStateAdapter {
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return 3;
    }

    public AudioPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override // androidx.viewpager2.adapter.FragmentStateAdapter
    public Fragment createFragment(int i) {
        if (i != 0) {
            if (i == 1) {
                return new AlbumFragment();
            }
            if (i == 2) {
                return new AritistFragment();
            }
            return new SongFragment();
        }
        return new SongFragment();
    }
}
