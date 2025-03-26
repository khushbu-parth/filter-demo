package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import java.util.ArrayList;
import java.util.List;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.databases.DBBookmarkHelper;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.models.BookmarkItem;


public class HelpActivity extends AppIntro2 {
    private DBBookmarkHelper dbBookmarkHelper;
    private final List<BookmarkItem> items = new ArrayList();
    public AsyncTask task;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.dbBookmarkHelper = new DBBookmarkHelper(this);
        AddDefault();
        SliderPage sliderPage = new SliderPage();
        setDepthAnimation();
        showSkipButton(false);
        sliderPage.setTitle("Search Videos!");
        sliderPage.setDescription("Search video or input URL");
        sliderPage.setTitleColor(Color.parseColor("#FFFFFF"));
        sliderPage.setDescColor(Color.parseColor("#FFFFFF"));
//        sliderPage.setImageDrawable(R.drawable.h1);
        sliderPage.setBgColor(Color.parseColor("#14E98792"));
        addSlide(AppIntroFragment.newInstance(sliderPage));
        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Play Videos");
        sliderPage2.setDescription("Just play the video that you want to download.");
        sliderPage2.setTitleColor(Color.parseColor("#FFFFFF"));
        sliderPage2.setDescColor(Color.parseColor("#FFFFFF"));
//        sliderPage2.setImageDrawable(R.drawable.h2);
        sliderPage2.setBgColor(Color.parseColor("#14E98792"));
        addSlide(AppIntroFragment.newInstance(sliderPage2));
        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("Download");
        sliderPage3.setDescription("Click the download button");
//        sliderPage3.setImageDrawable(R.drawable.h3);
        sliderPage3.setTitleColor(Color.parseColor("#FFFFFF"));
        sliderPage3.setDescColor(Color.parseColor("#FFFFFF"));
        sliderPage3.setBgColor(Color.parseColor("#14E98792"));
        addSlide(AppIntroFragment.newInstance(sliderPage3));
        SliderPage sliderPage4 = new SliderPage();
        sliderPage4.setTitle("Restricted");
        sliderPage4.setDescription("Due to their terms of service, youtube video cannot be download");
//        sliderPage4.setImageDrawable(R.drawable.h4);
        sliderPage4.setTitleColor(Color.parseColor("#FFFFFF"));
        sliderPage4.setDescColor(Color.parseColor("#FFFFFF"));
        sliderPage4.setBgColor(Color.parseColor("#14E98792"));
        addSlide(AppIntroFragment.newInstance(sliderPage4));
    }

    public void onDonePressed(Fragment fragment) {
        super.onDonePressed(fragment);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.e("Extras", "is empty");
        } else if (extras.getInt("key") == 1) {
            Transfer();
        } else {
            finish();
        }
    }

    public void onSkipPressed(Fragment fragment) {
        super.onSkipPressed(fragment);
        finish();
    }

    public void Transfer() {
        try {
            if (this.task != null && this.task.getStatus() == AsyncTask.Status.RUNNING) {
                this.task.cancel(true);
            }
            finish();
            startActivity(new Intent(this, MainActivity.class));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void AddDefault() {
        try {
            if (this.dbBookmarkHelper.getTotalBookmarks() == 0) {
                Add_Items();
                for (int i = 0; i < this.items.size(); i++) {
                    this.dbBookmarkHelper.AddBookmark(this.items.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Add_Items() {
        this.items.add(new BookmarkItem(0, "Facebook", "https://facebook.com", "drawable/f", 0, 0));
        this.items.add(new BookmarkItem(0, "WhatsApp", "", "drawable/w", 1, 0));
        this.items.add(new BookmarkItem(0, "Instagram", "https://instagram.com", "drawable/i", 0, 0));
        this.items.add(new BookmarkItem(0, "Twitter", "https://twitter.com", "drawable/t", 0, 0));
        this.items.add(new BookmarkItem(0, "Dailymotion", "https://dailymotion.com", "drawable/d", 0, 0));
        this.items.add(new BookmarkItem(0, "Pinterest", "https://pinterest.com/", "drawable/p", 0, 0));
        this.items.add(new BookmarkItem(0, "Veoh", "https://veoh.com/", "drawable/v", 0, 0));
        this.items.add(new BookmarkItem(0, "Flickr", "https://flickr.com/", "drawable/fl", 0, 0));
    }
}
