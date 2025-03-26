package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.fragments.FragmentMenu;

public class SettingActivity extends AppCompatActivity {
    ImageView back_set;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_setting);
        ImageView imageView = (ImageView) findViewById(R.id.back_set);
        this.back_set = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SettingActivity.this.onBackPressed();
            }
        });
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.frame_sett, new FragmentMenu());
        beginTransaction.commit();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
