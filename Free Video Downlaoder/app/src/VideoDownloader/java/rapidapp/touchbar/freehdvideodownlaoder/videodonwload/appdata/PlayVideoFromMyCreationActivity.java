package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;


public class PlayVideoFromMyCreationActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private String strSavedVideo;
    private VideoView vvMyCreationVideo;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_play_video_from_my_creation);
        this.strSavedVideo = getIntent().getStringExtra("video_path");
        bindView();
    }

    private void bindView() {
        VideoView videoView = (VideoView) findViewById(R.id.vvMyCreationVideo);
        this.vvMyCreationVideo = videoView;
        videoView.setVideoURI(Uri.parse(this.strSavedVideo));
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(this.vvMyCreationVideo);
        mediaController.setMediaPlayer(this.vvMyCreationVideo);
        this.vvMyCreationVideo.setMediaController(mediaController);
        this.vvMyCreationVideo.start();
        ImageView imageView = (ImageView) findViewById(R.id.ivBack);
        this.ivBack = imageView;
        imageView.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.ivBack) {
            onBackPressed();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
