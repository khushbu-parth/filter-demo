package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.vimeo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;

public class VideoViewActivity extends AppCompatActivity {
    FloatingActionButton fab;
    String filePathh1;
    File pictureFile;
    FloatingActionButton share;
    VideoView videoView;
    private Toolbar toolbar;

    public static void copyFile(File file, File file2) throws Throwable {
        FileChannel fileChannel;
        if (!file2.getParentFile().exists()) {
            file2.getParentFile().mkdirs();
        }
        if (!file2.exists()) {
            file2.createNewFile();
        }
        FileChannel fileChannel2 = null;
        try {
            FileChannel channel = new FileInputStream(file).getChannel();
            try {
                fileChannel = new FileOutputStream(file2).getChannel();
                try {
                    fileChannel.transferFrom(channel, 0, channel.size());
                    if (channel != null) {
                        channel.close();
                    }
                    if (fileChannel != null) {
                        fileChannel.close();
                    }
                } catch (Throwable th) {
                    Throwable th2 = th;
                    fileChannel2 = channel;
                    th = th2;
                    if (fileChannel2 != null) {
                        fileChannel2.close();
                    }
                    if (fileChannel != null) {
                        fileChannel.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                fileChannel2 = channel;
                fileChannel = null;
                if (fileChannel2 != null) {
                }
                if (fileChannel != null) {
                }
                throw th3;
            }
        } catch (Throwable th4) {
            fileChannel = null;
            if (fileChannel2 != null) {
            }
            if (fileChannel != null) {
            }
            throw th4;
        }
    }

    @SuppressLint("RestrictedApi")
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_video_view);
        this.filePathh1 = "/storage/emulated/0/Download/StatusSaver/Vimeo/";
        File file = new File(this.filePathh1);
        if (!file.exists()) {
            file.mkdir();
        }
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.share = (FloatingActionButton) findViewById(R.id.share);
        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.pictureFile = (File) getIntent().getExtras().get("picture");
        this.videoView = (VideoView) findViewById(R.id.videoView1);
        this.videoView.setVideoURI(Uri.parse(this.pictureFile.getAbsolutePath()));
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(this.videoView);
        this.videoView.setMediaController(mediaController);
        this.videoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                StringBuilder sb = new StringBuilder();
                sb.append("Duration = ");
                sb.append(VideoViewActivity.this.videoView.getDuration());
                Log.i("", sb.toString());
            }
        });
        this.share.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(VideoViewActivity.this.getApplicationContext().getPackageName());
                    sb.append("provider");
                    Uri uriForFile = FileProvider.getUriForFile(VideoViewActivity.this.getApplicationContext(), sb.toString(), VideoViewActivity.this.pictureFile);
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.SEND");
                    intent.setType("video/*");
                    intent.putExtra("android.intent.extra.SUBJECT", "");
                    intent.putExtra("android.intent.extra.TEXT", "");
                    intent.putExtra("android.intent.extra.STREAM", uriForFile);
                    startActivity(Intent.createChooser(intent, "Share Video"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (Global.check != 1) {
            this.fab.setVisibility(View.GONE);
        }
        this.fab.setOnClickListener(new C02533());
    }

    public void onResume() {
        super.onResume();
        this.videoView.start();
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    class C02533 implements OnClickListener {
        public void onClick(View view) {
            new C02521().run();
        }

        class C02521 implements Runnable {
            public void run() {
                try {
                    VideoViewActivity.copyFile(VideoViewActivity.this.pictureFile, new File(VideoViewActivity.this.filePathh1, VideoViewActivity.this.pictureFile.getName()));
                    Toast.makeText(VideoViewActivity.this, "Status Save Successfully in Gallery", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    StringBuilder sb = new StringBuilder();
                    sb.append("onClick: Error:");
                    sb.append(e.getMessage());
                    Log.e("RecyclerV", sb.toString());
                    Toast.makeText(VideoViewActivity.this, "Could not save, try again later", Toast.LENGTH_SHORT).show();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }
}