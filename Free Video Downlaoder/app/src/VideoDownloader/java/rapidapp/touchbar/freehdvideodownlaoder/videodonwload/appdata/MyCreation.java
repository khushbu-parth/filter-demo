package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;


public class MyCreation extends AppCompatActivity {
    public static ArrayList<String> videoList;
    private RecyclerView k;
    private Toolbar l;
    File[] listFile = null;
    private TextView m;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_my_creation);
        j();
    }

    public void getFromSdcard() {
        ArrayList<String> arrayList = new ArrayList<>();
        videoList = arrayList;
        arrayList.clear();
        File file = new File(Environment.getExternalStorageDirectory(), "Video Downloader");
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            this.listFile = listFiles;
            Arrays.sort(listFiles, new Comparator() {
                public int compare(Object obj, Object obj2) {
                    File file = (File) obj;
                    File file2 = (File) obj2;
                    if (file.lastModified() > file2.lastModified()) {
                        return -1;
                    }
                    return file.lastModified() < file2.lastModified() ? 1 : 0;
                }
            });
            for (File file2 : this.listFile) {
                if (file2.getName().contains(".mp4")) {
                    videoList.add(file2.getAbsolutePath());
                }
            }
        }
    }

    private void j() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.l = toolbar;
        setSupportActionBar(toolbar);
        ((TextView) this.l.findViewById(R.id.toolbar_title)).setText(getString(R.string.my_creation));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.l.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MyCreation.this.finish();
            }
        });
        this.m = (TextView) findViewById(R.id.txtNoVideo);
        this.k = (RecyclerView) findViewById(R.id.rv_videolist);
        setAdapter();
    }

    public void setAdapter() {
        getFromSdcard();
        Log.d("LOGTAG", "videoList size: " + videoList.size());
        if (videoList.size() <= 0) {
            this.m.setVisibility(View.VISIBLE);
            this.k.setVisibility(View.GONE);
        } else {
            this.m.setVisibility(View.GONE);
            this.k.setVisibility(View.VISIBLE);
        }
        bdayMyCreationAdapter bdaymycreationadapter = new bdayMyCreationAdapter(this, videoList, this);
        this.k.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.k.setAdapter(bdaymycreationadapter);
    }

    public class bdayMyCreationAdapter extends RecyclerView.Adapter<bdayMyCreationAdapter.MyHolder> {
        Activity a;
        public Context b;
        public List<String> c;
        private int d;

        public bdayMyCreationAdapter(Context context, ArrayList<String> arrayList, Activity activity) {
            this.b = context;
            this.c = arrayList;
            this.a = activity;
        }

        public MyHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MyHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.creation_item, viewGroup, false));
        }

        public void onBindViewHolder(MyHolder myHolder, int i) {
            final int adapterPosition = myHolder.getAdapterPosition();
            myHolder.v.setText(new File(this.c.get(adapterPosition)).getName());
            try {
                this.d = MediaPlayer.create(this.b, Uri.parse(this.c.get(adapterPosition))).getDuration();
                String format = String.format("%d:%d ", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) this.d)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) this.d) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) this.d)))});
                TextView textView = myHolder.x;
                textView.setText("Duration : " + format);
                File file = new File(this.c.get(adapterPosition));
                TextView textView2 = myHolder.w;
                textView2.setText("Size : " + a(file));
                Glide.with(this.b).load(this.c.get(adapterPosition)).into(myHolder.u);
                myHolder.s.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        bdayMyCreationAdapter bdaymycreationadapter = bdayMyCreationAdapter.this;
                        bdaymycreationadapter.a("Share Video", (String) bdaymycreationadapter.c.get(adapterPosition));
                    }
                });
                myHolder.t.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(bdayMyCreationAdapter.this.b);
                        builder.setMessage("Are you sure to delete ?");
                        builder.setCancelable(true);
                        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                File file = new File((String) bdayMyCreationAdapter.this.c.get(adapterPosition));
                                if (file.exists()) {
                                    file.delete();
                                    bdayMyCreationAdapter.this.c.remove(bdayMyCreationAdapter.this.c.get(adapterPosition));
                                }
                                bdayMyCreationAdapter.this.notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.create().show();
                    }
                });
                myHolder.g.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(bdayMyCreationAdapter.this.b, PlayVideoFromMyCreationActivity.class);
                        intent.putExtra("video_path", (String) bdayMyCreationAdapter.this.c.get(adapterPosition));
                        bdayMyCreationAdapter.this.b.startActivity(intent);
                    }
                });
                myHolder.q.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(bdayMyCreationAdapter.this.b, view);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.cre_delete:
                                        AlertDialog.Builder builder = new AlertDialog.Builder(bdayMyCreationAdapter.this.b);
                                        builder.setMessage("Are you sure to delete ?");
                                        builder.setCancelable(true);
                                        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                File file = new File((String) bdayMyCreationAdapter.this.c.get(adapterPosition));
                                                if (file.exists()) {
                                                    file.delete();
                                                    bdayMyCreationAdapter.this.c.remove(bdayMyCreationAdapter.this.c.get(adapterPosition));
                                                }
                                                bdayMyCreationAdapter.this.notifyDataSetChanged();
                                            }
                                        });
                                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                        builder.create().show();
                                        break;
                                    case R.id.cre_rename:
                                        final Dialog dialog = new Dialog(bdayMyCreationAdapter.this.b);
                                        dialog.requestWindowFeature(1);
                                        dialog.setContentView(R.layout.rename_dialog);
                                        dialog.setCancelable(false);
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                        final EditText editText = (EditText) dialog.findViewById(R.id.ren_edittext);
                                        ((TextView) dialog.findViewById(R.id.ren_ok)).setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View view) {
                                                if (TextUtils.isEmpty(editText.getText().toString())) {
                                                    editText.setError("Please Enter File Name");
                                                    return;
                                                }
                                                dialog.dismiss();
                                                File file = new File(Environment.getExternalStorageDirectory() + "/Independence day Movie Maker/");
                                                String str = null;
                                                Log.d("LOGTAG", "directory: " + file);
                                                if (file.exists()) {
                                                    for (File file2 : file.listFiles()) {
                                                        if (file2.isFile() && file2.getName().contains(".mp4") && file2.getName().equalsIgnoreCase(new File((String) bdayMyCreationAdapter.this.c.get(adapterPosition)).getName())) {
                                                            str = file2.getName();
                                                            Log.d("LOGTAG", "fileName: " + str);
                                                        }
                                                    }
                                                    File file3 = new File(file, str);
                                                    File file4 = new File(file, editText.getText().toString().trim() + ".mp4");
                                                    if (file3.exists()) {
                                                        boolean renameTo = file3.renameTo(file4);
                                                        MyCreation.this.setAdapter();
                                                        Log.d("LOGTAG", "isRename: " + renameTo);
                                                    }
                                                }
                                            }
                                        });
                                        ((TextView) dialog.findViewById(R.id.ren_cancel)).setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();
                                        break;
                                    case R.id.cre_share:
                                        bdayMyCreationAdapter.this.a("Share Video", (String) bdayMyCreationAdapter.this.c.get(adapterPosition));
                                        break;
                                }
                                return false;
                            }
                        });
                        popupMenu.inflate(R.menu.main_menu2);
                        popupMenu.show();
                    }
                });
            } catch (NullPointerException unused) {
            }
        }

        public int getItemCount() {
            return this.c.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            public LinearLayout g;
            final bdayMyCreationAdapter p;
            public ImageView q;
            public ImageView r;
            public ImageView s;
            public ImageView t;
            public ImageView u;
            public TextView v;
            public TextView w;
            public TextView x;

            public MyHolder(bdayMyCreationAdapter bdaymycreationadapter, View view) {
                super(view);
                this.p = bdaymycreationadapter;
                this.v = (TextView) view.findViewById(R.id.title);
                this.w = (TextView) view.findViewById(R.id.size);
                this.x = (TextView) view.findViewById(R.id.duration);
                this.u = (ImageView) view.findViewById(R.id.thumbnail);
                this.q = (ImageView) view.findViewById(R.id.overflow);
                this.r = (ImageView) view.findViewById(R.id.playpause);
                this.s = (ImageView) view.findViewById(R.id.share);
                this.g = (LinearLayout) view.findViewById(R.id.clickrel);
                this.t = (ImageView) view.findViewById(R.id.delete);
            }
        }

        public String a(File file) {
            if (!file.isFile()) {
                return "Unknown";
            }
            double length = (double) file.length();
            if (length < 1024.0d) {
                return String.valueOf(length).concat("B");
            }
            if (length <= 1024.0d || length >= 1048576.0d) {
                Double.isNaN(length);
                Double.isNaN(length);
                double round = (double) Math.round((length / 1232896.0d) * 100.0d);
                Double.isNaN(round);
                Double.isNaN(round);
                return String.valueOf(round / 100.0d).concat("MB");
            }
            Double.isNaN(length);
            Double.isNaN(length);
            double round2 = (double) Math.round((length / 1024.0d) * 100.0d);
            Double.isNaN(round2);
            Double.isNaN(round2);
            return String.valueOf(round2 / 100.0d).concat("KB");
        }

        public void a(String str, String str2) {
            MediaScannerConnection.scanFile(this.b, new String[]{str2}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String str, Uri uri) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("video/*");
                    intent.putExtra("android.intent.extra.STREAM", uri);
                    intent.addFlags(524288);
                    MyCreation.this.startActivity(Intent.createChooser(intent, "Share Video using"));
                }
            });
        }
    }

}
