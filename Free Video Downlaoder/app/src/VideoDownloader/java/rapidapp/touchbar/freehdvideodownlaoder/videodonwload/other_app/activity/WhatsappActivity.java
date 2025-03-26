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
import java.util.Objects;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.databinding.ActivityWhatsappBinding;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.fragment.WhatsappImageFragment;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.fragment.WhatsappVideoFragment;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.AppLangSessionManager;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.Utils;

public class WhatsappActivity extends AppCompatActivity {
    AppLangSessionManager appLangSessionManager;
    private WhatsappActivity activity;
    private ActivityWhatsappBinding binding;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = (ActivityWhatsappBinding) DataBindingUtil.setContentView(this, R.layout.activity_whatsapp);
        getWindow().setFlags(1024, 1024);
        this.activity = this;
        Utils.createFileFolder();
        initViews();
        this.appLangSessionManager = new AppLangSessionManager(this.activity);
        setLocale(this.appLangSessionManager.getLanguage());
    }

    @Override
    public void onResume() {
        super.onResume();
        this.activity = this;
    }

    private void initViews() {
        setupViewPager(this.binding.viewpager);
        this.binding.tabs.setupWithViewPager(Objects.requireNonNull(this.binding.viewpager));
        this.binding.imBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                WhatsappActivity.this.onBackPressed();
            }
        });
        for (int i = 0; i < this.binding.tabs.getTabCount(); i++) {
            this.binding.tabs.getTabAt(i).setCustomView((TextView) LayoutInflater.from(this.activity).inflate(R.layout.custom_tab, (ViewGroup) null));
        }

        this.binding.LLOpenWhatsapp.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                WhatsappActivity.this.lambda$initViews$0$WhatsappActivity(view);
            }
        });
    }

    public void lambda$initViews$0$WhatsappActivity(View view) {
        Utils.OpenApp(this.activity, "com.whatsapp");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.activity.getSupportFragmentManager(), 1);
        viewPagerAdapter.addFragment(new WhatsappImageFragment(), getResources().getString(R.string.images));
        viewPagerAdapter.addFragment(new WhatsappVideoFragment(), getResources().getString(R.string.videos));
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
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