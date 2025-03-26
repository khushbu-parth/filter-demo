package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.halilibo.bettervideoplayer.BetterVideoPlayer;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;

public class VideoPlayer extends AppCompatActivity {
    private static final String ARGUMENT_NAME = "VIDEO_NAME";
    private static final String ARGUMENT_PATH = "VIDEO_PATH";
    private static final int UI_ANIMATION_DELAY = 300;
    public BetterVideoPlayer mBetterVideoPlayer;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        public void run() {
            VideoPlayer.this.mBetterVideoPlayer.setSystemUiVisibility(4871);
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        public void run() {
            VideoPlayer.this.hide();
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        public void run() {
            ActionBar supportActionBar = VideoPlayer.this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.show();
            }
        }
    };

    private void delayedHide() {
        this.mHideHandler.removeCallbacks(this.mHideRunnable);
        this.mHideHandler.postDelayed(this.mHideRunnable, 100);
    }

    public void hide() {
        this.mHideHandler.removeCallbacks(this.mShowPart2Runnable);
        this.mHideHandler.postDelayed(this.mHidePart2Runnable, 300);
    }

    public void onBackPressed() {
        BetterVideoPlayer betterVideoPlayer = this.mBetterVideoPlayer;
        if (betterVideoPlayer != null) {
            betterVideoPlayer.release();
        }
        finish();
    }

    public void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        super.onCreate(bundle);
        setContentView((int) R.layout.video_player);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String string = extras.getString(ARGUMENT_PATH);
            String string2 = extras.getString(ARGUMENT_NAME);
            BetterVideoPlayer betterVideoPlayer = (BetterVideoPlayer) findViewById(R.id.video_full);
            this.mBetterVideoPlayer = betterVideoPlayer;
            betterVideoPlayer.setSource(Uri.parse("file://".concat(String.valueOf(string))));
            this.mBetterVideoPlayer.getToolbar().setTitle((CharSequence) string2);
            this.mBetterVideoPlayer.getToolbar().setNavigationIcon((int) R.drawable.back_material);
            this.mBetterVideoPlayer.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    VideoPlayer.this.onBackPressed();
                }
            });
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.mBetterVideoPlayer.release();
    }

    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        delayedHide();
    }
}
