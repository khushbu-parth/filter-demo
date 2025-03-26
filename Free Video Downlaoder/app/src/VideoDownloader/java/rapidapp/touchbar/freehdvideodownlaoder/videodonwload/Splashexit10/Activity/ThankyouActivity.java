package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.Activity;

import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;


public class ThankyouActivity extends AppCompatActivity {

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thankyoudia);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((ImageView) findViewById(R.id.ok)).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                finishAffinity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}
