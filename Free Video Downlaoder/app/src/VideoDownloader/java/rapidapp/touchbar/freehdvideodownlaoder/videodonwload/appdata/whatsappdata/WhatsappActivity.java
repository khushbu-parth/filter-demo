package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.whatsappdata;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;


public class WhatsappActivity extends AppCompatActivity {
    ViewPagerAdapter pager_adapter;
    TabLayout tabLayout;
    Toolbar toolbar;
    ViewPager viewPager;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.activity_whatsapp);
        pager_adapter = this.pager_adapter;
        if (pager_adapter != null) {
            pager_adapter.notifyDataSetChanged();
        }
//        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
//        this.toolbar = toolbar2;
//        setSupportActionBar(toolbar2);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle((CharSequence) "Whats App Status");
        this.tabLayout = (TabLayout) findViewById(R.id.tabs);
        this.viewPager = (ViewPager) findViewById(R.id.container);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.add_fragments(new PictureFragment(), "Pictures");
        viewPagerAdapter.add_fragments(new VideoFragment(), "Videos");
        this.viewPager.setAdapter(this.pager_adapter);
        this.tabLayout.setupWithViewPager(this.viewPager);
    }
//
//    public void onResume() {
////        ViewPagerAdapter viewPagerAdapter = this.pager_adapter;
//
//        super.onResume();
//    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
