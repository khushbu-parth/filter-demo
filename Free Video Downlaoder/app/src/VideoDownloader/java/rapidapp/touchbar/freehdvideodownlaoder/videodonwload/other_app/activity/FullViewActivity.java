package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.activity;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Locale;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.adapter.ShowImagesAdapter;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.databinding.ActivityFullViewBinding;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.AppLangSessionManager;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.util.Utils;

public class FullViewActivity extends AppCompatActivity {
    AppLangSessionManager appLangSessionManager;
    ShowImagesAdapter showImagesAdapter;
    private int Position = 0;
    private FullViewActivity activity;
    private ActivityFullViewBinding binding;
    private ArrayList<File> fileArrayList;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = (ActivityFullViewBinding) DataBindingUtil.setContentView(this, R.layout.activity_full_view);
        getWindow().setFlags(1024, 1024);
        this.activity = this;
        this.appLangSessionManager = new AppLangSessionManager(this.activity);
        setLocale(this.appLangSessionManager.getLanguage());
        if (getIntent().getExtras() != null) {
            this.fileArrayList = (ArrayList) getIntent().getSerializableExtra("ImageDataFile");
            this.Position = getIntent().getIntExtra("Position", 0);
        }
        initViews();
    }

    public void initViews() {
        this.showImagesAdapter = new ShowImagesAdapter(this, this.fileArrayList, this);
        this.binding.vpView.setAdapter(this.showImagesAdapter);
        this.binding.vpView.setCurrentItem(this.Position);
        this.binding.vpView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                FullViewActivity.this.Position = i;
                PrintStream printStream = System.out;
                printStream.println("Current position==" + FullViewActivity.this.Position);
            }
        });
        this.binding.imDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FullViewActivity.this.activity);
                builder.setPositiveButton(FullViewActivity.this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (((File) FullViewActivity.this.fileArrayList.get(FullViewActivity.this.Position)).delete()) {
                            FullViewActivity.this.deleteFileAA(FullViewActivity.this.Position);
                        }
                    }
                });
                builder.setNegativeButton(FullViewActivity.this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog create = builder.create();
                create.setTitle(FullViewActivity.this.getResources().getString(R.string.do_u_want_to_dlt));
                create.show();
            }
        });
        this.binding.imShare.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (((File) FullViewActivity.this.fileArrayList.get(FullViewActivity.this.Position)).getName().contains(".mp4")) {

//                    File file = FullViewActivity.this.fileArrayList.get(FullViewActivity.this.Position);
//
//                    Uri uriForFile = FileProvider.getUriForFile( activity,"rapidapp.touchbar.freehdvideodownlaoder.videodonwload.provider", new File(file.getPath()));
//                    Intent intent = new Intent("android.intent.action.SEND");
//                    intent.setType("video/*");
//                    intent.putExtra("android.intent.extra.STREAM", uriForFile);
//                    startActivity(Intent.createChooser(intent, "Share to"));
////                    Uri parse = Uri.parse(str);
//                    try {
//                        startActivity(Intent.createChooser(intent, "Share Video using"));
//                    } catch (ActivityNotFoundException unused) {
//                        Toast.makeText(activity,getResources().getString(R.string.no_app_installed), Toast.LENGTH_LONG).show();
//                    }


                    Utils.shareVideo(FullViewActivity.this.activity, ((File) FullViewActivity.this.fileArrayList.get(FullViewActivity.this.Position)).getPath());


                    Log.d("SSSSS", "onClick: " + FullViewActivity.this.fileArrayList.get(FullViewActivity.this.Position) + "");
                    return;
                }
                Utils.shareImage(FullViewActivity.this.activity, ((File) FullViewActivity.this.fileArrayList.get(FullViewActivity.this.Position)).getPath());
            }
        });
        this.binding.imWhatsappShare.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (((File) FullViewActivity.this.fileArrayList.get(FullViewActivity.this.Position)).getName().contains(".mp4")) {
                    Utils.shareImageVideoOnWhatsapp(FullViewActivity.this.activity, ((File) FullViewActivity.this.fileArrayList.get(FullViewActivity.this.Position)).getPath(), true);
                } else {
                    Utils.shareImageVideoOnWhatsapp(FullViewActivity.this.activity, ((File) FullViewActivity.this.fileArrayList.get(FullViewActivity.this.Position)).getPath(), false);
                }
            }
        });
        this.binding.imClose.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                FullViewActivity.this.lambda$initViews$0$FullViewActivity(view);
            }
        });
    }

    public void lambda$initViews$0$FullViewActivity(View view) {
        onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.activity = this;
    }

    public void deleteFileAA(int i) {
        this.fileArrayList.remove(i);
        this.showImagesAdapter.notifyDataSetChanged();
        Utils.setToast(this.activity, getResources().getString(R.string.file_deleted));
        if (this.fileArrayList.size() == 0) {
            onBackPressed();
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void setLocale(String str) {
        Locale locale = new Locale(str);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
    }
}