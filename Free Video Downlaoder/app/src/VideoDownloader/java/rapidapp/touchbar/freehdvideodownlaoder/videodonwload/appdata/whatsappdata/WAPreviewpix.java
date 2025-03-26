package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.whatsappdata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;


public class WAPreviewpix extends AppCompatActivity {

    public static String DIRECTORY_TO_SAVE_MEDIA_NOW = "/storage/emulated/0/Video Downloader/";
    private static final String TAG_VIDURL = "video_url";
    String CurrentFile;
    String FileName;
    String ImgUrl;
    FloatingActionButton Save;
    ImageView Share;
    ImageView image;
    ProgressDialog pDialog;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.activity_wapreviewpix);
        this.Save = (FloatingActionButton) findViewById(R.id.Save);
        this.Share = (ImageView) findViewById(R.id.Share);
        this.image = (ImageView) findViewById(R.id.Imageview);
        Intent intent = getIntent();
        this.ImgUrl = intent.getStringExtra(TAG_VIDURL);
        this.CurrentFile = intent.getStringExtra("CurrentFile");
        this.FileName = intent.getStringExtra("FileName");
        ((RequestBuilder) Glide.with((FragmentActivity) this).load(this.ImgUrl).placeholder((int) R.mipmap.ic_launcher)).into(this.image);
        this.Save.setOnClickListener(new C04541());
        this.Share.setOnClickListener(new C04552());
    }

    class C04541 implements View.OnClickListener {
        C04541() {
        }

        public void onClick(View view) {
            try {
                File file = new File(WAPreviewpix.this.CurrentFile);
                WAPreviewpix.this.copyFile(file, new File(WAPreviewpix.DIRECTORY_TO_SAVE_MEDIA_NOW + WAPreviewpix.this.FileName), WAPreviewpix.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            WAPreviewpix.this.alert();
        }
    }

    class C04552 implements View.OnClickListener {
        C04552() {
        }

        public void onClick(View view) {
            Uri uriForFile = FileProvider.getUriForFile(WAPreviewpix.this, "rapidapp.touchbar.freehdvideodownlaoder.videodonwload.provider", new File(WAPreviewpix.this.CurrentFile));
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("video/*");
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            WAPreviewpix.this.startActivity(Intent.createChooser(intent, "Share to"));
        }
    }

    class C04563 implements DialogInterface.OnClickListener {
        C04563() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
        }
    }

    public void alert() {
        new AlertDialog.Builder(this).setTitle((CharSequence) "Alert Message").setMessage((CharSequence) "File Saved Successfully").setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new C04563()).show();
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
