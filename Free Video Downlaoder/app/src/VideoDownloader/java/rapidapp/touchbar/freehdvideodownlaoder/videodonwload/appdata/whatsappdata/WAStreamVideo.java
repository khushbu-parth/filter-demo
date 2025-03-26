package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.whatsappdata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;


public class WAStreamVideo extends AppCompatActivity {

    public static String DIRECTORY_TO_SAVE_MEDIA_NOW = "/storage/emulated/0/Video Downloader/";
    private static final String TAG_VIDURL = "video_url";
    String CurrentFile;
    String FileName;
    FloatingActionButton Save;
    ImageView Share;
    String VideoURL;
    ProgressDialog pDialog;
    VideoView videoview;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_wastream_video);
        this.Save = (FloatingActionButton) findViewById(R.id.Save);
        this.Share = (ImageView) findViewById(R.id.Share);
        Intent intent = getIntent();
        this.VideoURL = intent.getStringExtra(TAG_VIDURL);
        this.CurrentFile = intent.getStringExtra("CurrentFile");
        this.FileName = intent.getStringExtra("FileName");
        this.videoview = (VideoView) findViewById(R.id.streaming_video_laner_rla);
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.pDialog = progressDialog;
        progressDialog.setTitle("Video Stream");
        this.pDialog.setMessage("Buffering...");
        this.pDialog.setIndeterminate(false);
        this.pDialog.setCancelable(false);
        this.pDialog.show();
        try {
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(this.videoview);
            Uri parse = Uri.parse(this.VideoURL);
            this.videoview.setMediaController(mediaController);
            this.videoview.setVideoURI(parse);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        this.videoview.requestFocus();
        this.videoview.setOnPreparedListener(new C04571());
        this.Save.setOnClickListener(new C04582());
        this.Share.setOnClickListener(new C04593());
    }

    class C04571 implements MediaPlayer.OnPreparedListener {
        C04571() {
        }

        public void onPrepared(MediaPlayer mediaPlayer) {
            WAStreamVideo.this.pDialog.dismiss();
            WAStreamVideo.this.videoview.start();
        }
    }

    class C04582 implements View.OnClickListener {
        C04582() {
        }

        public void onClick(View view) {
            try {
                File file = new File(WAStreamVideo.this.CurrentFile);
                WAStreamVideo.this.copyFile(file, new File(WAStreamVideo.DIRECTORY_TO_SAVE_MEDIA_NOW + WAStreamVideo.this.FileName), WAStreamVideo.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            WAStreamVideo.this.alert();
        }
    }

    class C04593 implements View.OnClickListener {
        C04593() {
        }

        public void onClick(View view) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("video/*");
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(WAStreamVideo.this.CurrentFile)));
            WAStreamVideo.this.startActivity(Intent.createChooser(intent, "Share to"));
        }
    }

    class C04604 implements DialogInterface.OnClickListener {
        C04604() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
        }
    }

    public void alert() {
        new AlertDialog.Builder(this).setTitle((CharSequence) "Alert Message").setMessage((CharSequence) "File Saved Successfully").setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new C04604()).show();
    }

    public void copyFile(File file, File file2, Activity activity) throws IOException {
        if (!file2.getParentFile().exists()) {
            file2.getParentFile().mkdirs();
        }
        if (!file2.exists()) {
            file2.createNewFile();
        }
        FileChannel channel = new FileInputStream(file).getChannel();
        try {
            FileChannel channel2 = new FileOutputStream(file2).getChannel();
            try {
                channel2.transferFrom(channel, 0, file.length());
                Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                intent.setData(Uri.fromFile(file2));
                activity.sendBroadcast(intent);
                if (channel != null) {
                    channel.close();
                }
                if (channel2 != null) {
                    channel2.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
