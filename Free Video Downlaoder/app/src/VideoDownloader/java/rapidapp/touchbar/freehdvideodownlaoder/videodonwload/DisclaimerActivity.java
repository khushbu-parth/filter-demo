package rapidapp.touchbar.freehdvideodownlaoder.videodonwload;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.R;
import rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.ui.activities.MainActivity;


public class DisclaimerActivity extends AppCompatActivity {


    private ImageView how_to_use_cross;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.howtouse);

        how_to_use_cross = (ImageView) findViewById(R.id.how_to_use_cross);
        how_to_use_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DisclaimerActivity.this, MainActivity.class));
            }
        });
    }
}