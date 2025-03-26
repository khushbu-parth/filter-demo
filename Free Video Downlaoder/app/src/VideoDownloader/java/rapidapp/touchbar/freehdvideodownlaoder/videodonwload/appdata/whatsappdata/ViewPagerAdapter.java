package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.whatsappdata;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> tab_titles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void add_fragments(Fragment fragment, String str) {
        this.fragments.add(fragment);
        this.tab_titles.add(str);
    }

    public Fragment getItem(int i) {
        Log.e("===", "getItem: " + i);
        return this.fragments.get(i);

    }

    public int getCount() {
        Log.e("===", "" + fragments.size());

        return this.fragments.size();
    }

    public CharSequence getPageTitle(int i) {
        return this.tab_titles.get(i);
    }
}
