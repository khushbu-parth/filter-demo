package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.databinding.ActivityGalleryBinding;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.fragment.FBDownloadedFragment;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.fragment.InstaDownloadedFragment;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.fragment.TwitterDownloadedFragment;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.fragment.VimeoDownloadedFragment;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.fragment.WhatsAppDowndlededFragment;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.AppLangSessionManager;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.Utils;

public class GalleryActivity extends AppCompatActivity {
    GalleryActivity activity;
    AppLangSessionManager appLangSessionManager;
    ActivityGalleryBinding binding;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = (ActivityGalleryBinding) DataBindingUtil.setContentView(this, R.layout.activity_gallery);
        getWindow().setFlags(1024, 1024);
        this.activity = this;
        this.appLangSessionManager = new AppLangSessionManager(this.activity);
        setLocale(this.appLangSessionManager.getLanguage());
        initViews();
    }

    public void initViews() {
        setupViewPager(this.binding.viewpager);
        this.binding.tabs.setupWithViewPager(this.binding.viewpager);
        this.binding.imBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                GalleryActivity.this.onBackPressed();
            }
        });
        for (int i = 0; i < this.binding.tabs.getTabCount(); i++) {
            this.binding.tabs.getTabAt(i).setCustomView((TextView) LayoutInflater.from(this.activity).inflate(R.layout.custom_tab, (ViewGroup) null));
        }
        this.binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
            }
        });
        Utils.createFileFolder();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.activity.getSupportFragmentManager(), 1);
        viewPagerAdapter.addFragment(new InstaDownloadedFragment(), "Instagram");
        viewPagerAdapter.addFragment(new WhatsAppDowndlededFragment(), "Whatsapp");
        viewPagerAdapter.addFragment(new FBDownloadedFragment(), "Facebook");
        viewPagerAdapter.addFragment(new TwitterDownloadedFragment(), "Twitter");
        viewPagerAdapter.addFragment(new VimeoDownloadedFragment(), "Vimeo");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
    }

    public void setLocale(String str) {
        Locale locale = new Locale(str);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleList = new ArrayList();

        ViewPagerAdapter(FragmentManager fragmentManager, int i) {
            super(fragmentManager, i);
        }

        @Override
        public Fragment getItem(int i) {
            return this.mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String str) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(str);
        }

        @Override
        public CharSequence getPageTitle(int i) {
            return this.mFragmentTitleList.get(i);
        }
    }
}